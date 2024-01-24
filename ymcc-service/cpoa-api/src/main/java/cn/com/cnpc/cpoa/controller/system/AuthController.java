package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppToken;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditSetDto;
import cn.com.cnpc.cpoa.enums.UserEnabledEnum;
import cn.com.cnpc.cpoa.enums.contractor.AcceStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.SetStateEnum;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.constractor.ContAccessService;
import cn.com.cnpc.cpoa.service.constractor.ContCreditSetService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.MD5Utils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StrUtils;
import cn.com.cnpc.cpoa.web.base.BaseController;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Console;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * 认证控制器
 *
 * @author scchenyong@189.cn
 * @create 2019-01-14
 */
@RestController
@RequestMapping("/auth")
public class AuthController extends BaseController {

    private static final String TOKEN_ERROR = "身份校验不通过,请先登录";
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    UserService userService;

    @Autowired
    ContAccessService contAccessService;

    @Autowired
    ContCreditSetService contCreditSetService;

    /**
     * 登陆
     *
     * @return
     */
    @Log(logContent = "用户登陆", logModule = LogModule.AUTH, logType = LogType.OPERATION)
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public AppMessage login(@RequestBody Map<String, String> params) {
        String account = params.get("account");
        String password = params.get("password");
        String verificationCode = params.get("verificationCode");
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return AppMessage.error("登陆信息错误");
        }

        SysUserDto userDto = userService.getByAccount(account);
        if (null == userDto) {
            return AppMessage.error("用户名或密码错误");
        }
        String md5pass = MD5Utils.MD5(password, userDto.getUserSalt());

        if (!md5pass.equals(userDto.getUserPassword())) {
            return AppMessage.errorObjId(userDto.getUserId(), "用户名或密码错误");
        }
        //禁用用户不得登入
        String enabled = userDto.getEnabled();
        if (UserEnabledEnum.UNENABLED.getKey().equals(enabled)) {
            return AppMessage.errorObjId(userDto.getUserId(), "用户已禁用，请联系管理员");
        }

        //验证验证码是否有效
        if (StrUtils.isEmpty(verificationCode)) {
            return AppMessage.errorObjId(userDto.getUserId(), "请输入验证码");
        }
        if (!verificationCode.equalsIgnoreCase(userDto.getUserVerificationCode())) {
            return AppMessage.errorObjId(userDto.getUserId(), "请输入正确的验证码");
        }
        if (DateUtils.getNowDate().getTime() > userDto.getUserEffectiveTime().getTime()) {
            return AppMessage.errorObjId(userDto.getUserId(), "验证码失效，请重新申请");

        }

        return AppMessage.result(AppToken.getByUserId(userDto.getUserId()));
    }

    @RequestMapping(value = "/renew", method = RequestMethod.POST)
    public AppMessage renew() {
        String token = ServletUtils.getToken();
        if (null == token || !AppToken.isSigned(token)) {
            return AppMessage.error(AppMessage.NOTOKEN_CODE, TOKEN_ERROR);
        }
        Map<String, Object> objectMap = AppToken.parser(ServletUtils.getToken());
        if (null == objectMap) {
            return AppMessage.error(AppMessage.NOTOKEN_CODE, TOKEN_ERROR);
        }
        String userId = (String) objectMap.get(AppToken.USER_KEY);
        return AppMessage.result(AppToken.getByUserId(userId));
    }

    /**
     * 登陆
     *
     * @return
     */
    @Log(logContent = "承包商登陆", logModule = LogModule.AUTH, logType = LogType.OPERATION)
    @RequestMapping(value = "/accLogin", method = RequestMethod.POST)
    public AppMessage accLogin(@RequestBody Map<String, String> params) {
        String acceCode = params.get("acceCode");
        String accessId = params.get("accessId");
        String acceType = params.get("acceType");

        if (StringUtils.isBlank(acceCode)) {
            return AppMessage.error("接入码不能为空");
        }

        Map<String, Object> param = new HashMap<>();
        param.put("accessId", accessId);
        BizContAccessDto accessDto = contAccessService.selectByKey(accessId);
        if (null == accessDto) {
            return AppMessage.error("承包商不存在，请联系统管理员");
        }

        if (ContractorConstant.AuditService.ACESSAUDITSERVICE.equals(acceType)) {
            //TODO 转MD5校验
            String eAccesCode = MD5Utils.MD5(accessDto.getAcceCode()).toLowerCase();
            if (!eAccesCode.equals(acceCode)) {
                return AppMessage.errorObjId(accessId, "接入码有误，请确认后再试");
            }
            String acceState = accessDto.getAcceState();
            if (AcceStateEnum.INVALID.equals(acceState)) {
                return AppMessage.errorObjId(accessId, "当前申请已失效，请联系相关经办人员");
            }
        } else {
            List<BizContCreditSetDto> contCreditSetDtos = contCreditSetService.selectContCreditSet(param);
            if (cn.com.cnpc.cpoa.utils.StringUtils.isEmpty(contCreditSetDtos)) {
                return AppMessage.errorObjId(accessId, "资质变更记录不存在，请联系管理员");
            }
            BizContCreditSetDto contCreditSetDto = contCreditSetDtos.get(0);
            //TODO 转MD5校验
            String eAccesCode = MD5Utils.MD5(contCreditSetDto.getSetCode()).toLowerCase();
            if (!acceCode.equals(eAccesCode)) {
                return AppMessage.errorObjId(accessId, "接入码有误，请确认后再试");
            }
            if (SetStateEnum.INVALID.equals(contCreditSetDto.getSetState())) {
                return AppMessage.errorObjId(accessId, "当前申请已失效，请联系相关经办人员");
            }
        }

        return AppMessage.result(AppToken.getByContId(accessDto.getContId(), accessId));
    }


    @RequestMapping(value = "/SSOLogin1", method = RequestMethod.GET)
    public void SSOLogin1(@RequestParam(value = "token", defaultValue = "") String token,
                          HttpServletResponse response) throws Exception {
       response.sendRedirect("http://10.132.6.72:8878/#/authsso?appKey=com.ccde.ajy.jyglxt&redirectURL=http://10.132.6.70:8080/api/auth/SSOLogin2");
       // sendRedirectUrl(token);
    }


    private void sendRedirectUrl(String token) throws IOException {
        HttpServletResponse response = ServletUtils.getResponse();
        String pageToRedirect = "http://10.132.6.70:8080/";
        //创建Cookie
        Cookie cookie = new Cookie("TOKEN", token);
        Cookie cookie2 = new Cookie("pms_p", "ODc2NTQzMjFfeWpB");
        //设置Cookie的最大生命周期,否则浏览器关闭后Cookie即失效
        cookie.setMaxAge(Integer.MAX_VALUE);
      //  cookie.setDomain(".cpoa.zhinglink.com");//With it or without, makes no difference.
        cookie.setPath("/");
        response.addCookie(cookie);

        cookie2.setMaxAge(Integer.MAX_VALUE);
       // cookie2.setDomain(".cpoa.zhinglink.com");//With it or without, makes no difference.
        cookie2.setPath("/");
        //将Cookie加到response中
        response.addCookie(cookie2);
        response.sendRedirect(pageToRedirect);
    }

    @RequestMapping(value = "/SSOLogin2", method = RequestMethod.GET)
    public String SSOLogin2(@RequestParam(value = "token", defaultValue = "") String token) throws UnsupportedEncodingException {
        if (token != null && !"".equals(token)) {
            try {
                // 根据MD5加密
                //String sign = MD5Utils.MD5(token.substring(16, 24));
                String sign = DigestUtils.md5DigestAsHex(token.substring(16, 24).getBytes());
                // 设置请求头
                HttpHeaders headers = new HttpHeaders();
                // 设置content-type为JSON方式
                headers.setContentType(MediaType.APPLICATION_JSON);
                // 设置请求体
                JSONObject postData = new JSONObject();
                postData.put("token", token);
                postData.put("sign", sign);

                logger.info("SSOLogin2请求参数："+JSONObject.toJSONString(postData));
                // 用HttpEntity封装整个请求报文
                HttpEntity<String> request = new HttpEntity<>(postData.toString(), headers);
                // 接收服务端返回的json字符串
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
                String result = restTemplate.postForObject("http://10.132.6.72:8878/sso/app/authToken", request, String.class);
                logger.info("SSOLogin2返回结果："+result);
                // 返回的json字符串，并判断code是否为200
                JSONObject json = JSONObject.parseObject(result);
                if ("200".equals(json.getString("code"))) {
                    String userName = json.getJSONObject("data").getString("name");
                    Map<String,Object> userParam =new HashMap<>();
                    userParam.put("userMail",userName+"@");
                    List<SysUserDto> sysUserDtos = userService.selectList(userParam);
                    if (CollectionUtil.isEmpty(sysUserDtos)) {
                        return getGbkMsg("Wrong user name or password");
                    }
                    //禁用用户不得登入
                    String enabled = sysUserDtos.get(0).getEnabled();
                    if (UserEnabledEnum.UNENABLED.getKey().equals(enabled)) {
                        return getGbkMsg("The user is disabled, please contact the administrator");
                    }

                    sendRedirectUrl(AppToken.getByUserId(sysUserDtos.get(0).getUserId()));

                    return getGbkMsg("login");
                } else {
                    return json.getString("message");
                }
            } catch (Exception e) {
                logger.error("SSOLogin2报错："+ e.getMessage(),e);
                return getGbkMsg("Login interface parsing error" + e.getMessage());
            }
        } else {
            return getGbkMsg("login");
        }
    }



    private String getGbkMsg(String msg) throws UnsupportedEncodingException {
       return msg;
    }

}

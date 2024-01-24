package cn.com.cnpc.cpoa.controller.wx;

import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppToken;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.WxMessageService;
import cn.com.cnpc.cpoa.service.WxService;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.WxUtils;
import cn.com.cnpc.cpoa.vo.UserVo;
import cn.com.cnpc.cpoa.vo.WxResult;
import com.alibaba.fastjson.JSONObject;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/5/5 16:21
 * @Description:微信公众号
 */
@RestController
@RequestMapping("/wx")
public class WxController {

    private Logger logger = Logger.getLogger(getClass());

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private WxService wxService;

    @Autowired
    private UserService userService;

    @Autowired
    private WxMessageService wxMessageService;

    public static final String userInfo="http://119.4.40.87:8011/wechat-api/wx/accessToken";

    /**
     * @Description: 微信授权回调用户信息
     * @Param: [code, returnUrl]
     * @returns: java.lang.String
     * @Date: 2018/8/11 15:08
     */
    @RequestMapping(value = "/userInfo",method = RequestMethod.GET)
    public AppMessage userInfo(@RequestParam(value ="code",defaultValue="") String code,
                           @RequestParam(value ="state",defaultValue="") String state) throws Exception{
        WxMpOAuth2AccessToken wxMpOAuth2AccessToken = new WxMpOAuth2AccessToken();
        String openId=null;
        try {
            if(StringUtils.isEmpty(code)){
                WxResult result=new WxResult();
                result.setOpenId(openId);
                return AppMessage.errorObjId(result,"失败，该用户已禁止授权！");
            }
//            String res = HttpUtil.get(userInfo+"?code="+code);
            logger.info("code------------->"+code);
            String res = WxUtils.sendGet(userInfo, "code="+code);
            wxMpOAuth2AccessToken = JSONObject.parseObject(res,WxMpOAuth2AccessToken.class);
            //WxMpUser wxMpUser = wxMpService.oauth2getUserInfo(wxMpOAuth2AccessToken, null);
            //logger.debugv("【微信网页授权获】获取用户信息：{}", wxMpUser);

            //openId = wxMpUser.getOpenId();

            openId =wxMpOAuth2AccessToken.getOpenId();
            Map<String,Object> param=new HashMap<>();
            param.put("openId",openId);
            logger.info("openId------------->"+openId);

            List<SysUserDto> sysUserDtos = userService.selectList(param);
            logger.info("sysUserDtos------------->"+JSON.marshal(sysUserDtos));

            if(null==sysUserDtos||sysUserDtos.size()==0){
                WxResult result=new WxResult();
                result.setOpenId(openId);
                return AppMessage.errorObjId(result,"失败，该用户未绑定！");
            }
            //已绑定的直接登陆 返回token
            String token = AppToken.getByUserId(sysUserDtos.get(0).getUserId());
            WxResult result=new WxResult();
            result.setOpenId(openId);
            result.setToken(token);
            return AppMessage.success(result,"授权成功");
        } catch (Exception e) {
            e.printStackTrace();
            logger.errorv("【微信网页授权】{}", e);
            WxResult result=new WxResult();
            result.setOpenId(openId);
            return AppMessage.errorObjId2(result,"授权超时");
        }
    }

    @RequestMapping(value = "/message", method = RequestMethod.GET)
    public AppMessage pushMsg(@RequestParam(value = "message", defaultValue = "") String message) {
        try {
            wxMessageService.push(message);
            return AppMessage.success(true, "发送成功");
        } catch (Exception e) {

            return AppMessage.errorObjId2(false, "发送失败");
        }
    }


    /**
     * 微信绑定
     * @param user
     * @return
     */
    @RequestMapping(value = "/binding", method = RequestMethod.PUT)
    public AppMessage binding(@RequestBody UserVo user) {
        return wxService.wxBinding(user);
    }


    /**
     * 微信解绑
     * @return
     */
    @RequestMapping(value = "/unBinding/{id}", method = RequestMethod.PUT)
    public AppMessage unBinding(@PathVariable String id) {
        return wxService.wxUnBinding(id);
    }



    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    public String query() {

        return "hi";
    }

}

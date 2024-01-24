package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppToken;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.enums.UserEnabledEnum;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.MD5Utils;
import cn.com.cnpc.cpoa.vo.UserVo;
import cn.com.cnpc.cpoa.vo.WxResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/5/5 17:21
 * @Description:微信服务
 */
@Service
public class WxService {
    @Autowired
    private UserService userService;

    /**
     * 微信登陆
     * @param userId
     * @return
     */
    public AppMessage wxLogin(String userId){
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(AppToken.USER_KEY, userId);
        return AppMessage.result(AppToken.create(keyMap, 480 * 1000 * 60));
    }

    public AppMessage wxBinding(UserVo user){
        String account = user.getAccount();
        String password = user.getPassWord();
        WxResult result=new WxResult();
        result.setOpenId(user.getWxopenid());

        //1 验证
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return AppMessage.error("登陆信息错误");
        }

        SysUserDto userDto = userService.getByAccount(account);
        if (null == userDto) {
            return AppMessage.errorObjId(result,"用户名或密码错误");
        }

        String md5pass = MD5Utils.MD5(password, userDto.getUserSalt());
        if (!md5pass.equals(userDto.getUserPassword())) {
            return AppMessage.errorObjId(result,"用户名或密码错误");
        }
        //禁用用户不得登入
        String enabled = userDto.getEnabled();
        if(UserEnabledEnum.UNENABLED.getKey().equals(enabled)){
            return AppMessage.errorObjId(result,"用户已禁用，请联系管理员");
        }
        //2 绑定
        String wxopenid = user.getWxopenid();

        userDto.setWxopenid(wxopenid);
        userDto.setWxbindaAt(DateUtils.getNowDate());
        userService.updateNotNull(userDto);

        AppMessage appMessage = wxLogin(userDto.getUserId());
        String token=(String)appMessage.getResult();
        result.setToken(token);
        return AppMessage.success(result,"绑定成功");
    }

    public AppMessage wxUnBinding(String id) {
        SysUserDto userDto = userService.selectByKey(id);
        userDto.setWxopenid(null);
        if(1!=userService.updateAll(userDto)){
            return AppMessage.errorObjId(id,"解绑失败");
        }
        return AppMessage.success(id,"解绑成功");
    }

}

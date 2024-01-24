package cn.com.cnpc.cpoa.controller.app;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "app首页用户展示")
@RestController
@RequestMapping("/hd/app/user")
public class UserAppController extends BaseController {
    @Autowired
    UserService userService;
    @ApiOperation("用户展示")
    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
//    @RequestParam(value = "userId", defaultValue = "") String userId
    public AppMessage homepage(){
        String userId = ServletUtils.getSessionUserId();
        SysUserDto sysUserDto = userService.selectByKey(userId);
        return AppMessage.success(sysUserDto, "查询立项详情成功");
    }
}

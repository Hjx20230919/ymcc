package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.service.SysRoleActionService;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/4/25 16:01
 */
@Api(tags = "角色菜单权限管理")
@RestController
@RequestMapping("/hd/roleMenuAuth")
public class SysRoleActionController extends BaseController {

    @Autowired
    private SysRoleActionService roleActionService;

    @ApiOperation("新增修改角色菜单权限")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleID",value = "角色id"),
            @ApiImplicitParam(name = "addID",value = "菜单id集合")})
    @Log(logContent = "新增修改角色菜单权限",logModule = LogModule.ROLE,logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST,produces = "application/json")
    public AppMessage addRoleMenuAuth(@RequestParam("roleID")String roleID,
                                      @RequestParam("addID") List<String> addID
                                      ) {
        return roleActionService.addRoleMenuAuth(roleID,addID);
    }

    /**
     * 查询角色拥有菜单权限
     * @return
     */
    @ApiOperation("查询角色拥有菜单权限")
    @ApiImplicitParams({@ApiImplicitParam(name = "roleID",value = "角色id")})
    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectMenuAuth(@RequestParam("roleID")String roleID) {
        return roleActionService.selectMenuAuth(roleID);
    }
}

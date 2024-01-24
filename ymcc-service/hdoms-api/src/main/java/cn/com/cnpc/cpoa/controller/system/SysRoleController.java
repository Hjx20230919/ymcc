package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.mapper.SysMenuDtoMapper;
import cn.com.cnpc.cpoa.service.MenuService;
import cn.com.cnpc.cpoa.service.SysRoleService;
import cn.com.cnpc.cpoa.utils.LocalCache;
import cn.com.cnpc.cpoa.vo.MenuVo;
import cn.com.cnpc.cpoa.vo.RoleVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO   用户角色管理
 * @date 2022/4/25 14:44
 */
@Api(tags = "角色管理")
@RestController
@RequestMapping("/hd/sys/role")
public class SysRoleController extends BaseController {

//    static  ThreadPoolExecutor executor = null;
//
//    static {
//        executor = new ThreadPoolExecutor( 5,
//                5,
//                1000,
//                TimeUnit.MILLISECONDS,
//                new LinkedBlockingDeque<>(),
//                new ThreadFactoryBuilder().setNameFormat("角色菜单缓存修改").build());
//    }

    @Autowired
    private SysRoleService roleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    SysMenuDtoMapper sysMenuDtoMapper;

    /**
     * 查询用户的角色（用户编辑角色时使用）
     *
     * @param pageNo
     * @param pageSize
     * @param userId
     * @return
     */
    @ApiOperation("查询用户的角色（用户编辑角色时使用）")
    @ApiImplicitParams({@ApiImplicitParam(name = "userId",value = "用户id")})
    @RequestMapping(value = "/user", method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectRole(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                 @RequestParam(value = "userId", defaultValue = "") String userId) {

        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);
        List<RoleVo> dtos = roleService.selectRole(params);
        TableDataInfo dataTable = getDataTable(dtos, page.getTotal());
        return AppMessage.success(dataTable, "查询用户角色信息成功");
    }

    /**
     * 查询角色含角色的菜单权限（编辑角色时使用）
     *
     * @param pageNo
     * @param pageSize
     * @param name
     * @return
     */
    @ApiOperation("查询角色含角色的菜单权限（编辑角色时使用）")
    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "角色名称"),
            @ApiImplicitParam(name = "roleId",value = "角色id")})
    @RequestMapping(method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectActionsRole(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                        @RequestParam(value = "name", defaultValue = "") String name,
                                        @RequestParam(value = "roleId", defaultValue = "") String roleId) {

        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("roleId", roleId);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);
        List<RoleVo> dtos = roleService.selectActionsRole(params);
        TableDataInfo dataTable = getDataTable(dtos, page.getTotal());
        return AppMessage.success(dataTable, "查询角色信息成功");
    }

    /**
     * 新增角色
     *
     * @param dto
     * @return
     * @throws ParseException
     */
    @ApiOperation("新增角色")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto",value = "角色实体类")})
    @Log(logContent = "新增角色",logModule = LogModule.ROLE,logType = LogType.OPERATION)
    @RequestMapping(value = "/actions", method = RequestMethod.POST,produces = "application/json")
    public AppMessage addRoleActions(@RequestBody RoleVo dto) throws ParseException {
        AppMessage appMessage = roleService.addRoleActions(dto);
        String  roleId = (String) appMessage.getResult();
//        executor.execute(() -> {
            //更新缓存
            LocalCache localCache = LocalCache.getInstance();
            //先查询一级菜单数据
            Map<String, Object> one = new HashMap<>();
            one.put("roleId", roleId);
            one.put("parentActionId", null);
            List<MenuVo> oneActions = sysMenuDtoMapper.selectActionsByUserId(one);
            //获取子级菜单
            menuService.selectChildren(oneActions,roleId);
            //填充meta数据
            menuService.setChildrenMeta(oneActions);
            //存入缓存
            localCache.putValue(roleId,oneActions);
//        });
        return appMessage;
    }

    /**
     * 修改角色
     *
     * @param dto
     * @return
     * @throws ParseException
     */
    @ApiOperation("修改角色")
    @ApiImplicitParams({@ApiImplicitParam(name = "dto",value = "修改后的角色实体类")})
    @Log(logContent = "修改角色",logModule = LogModule.ROLE,logType = LogType.OPERATION)
    @RequestMapping(value = "/actions", method = RequestMethod.PUT,produces = "application/json")
    public AppMessage updateRoleActions(@RequestBody RoleVo dto) throws ParseException {
        AppMessage appMessage = roleService.updateRoleActions(dto);
        //更新菜单树缓存
        String roleId = dto.getRoleId();
//        executor.execute(() -> {
            //更新缓存
            LocalCache localCache = LocalCache.getInstance();
            //先查询一级菜单数据
            Map<String, Object> one = new HashMap<>();
            one.put("roleId", roleId);
            one.put("parentActionId", null);
            List<MenuVo> oneActions = sysMenuDtoMapper.selectActionsByUserId(one);
            //获取子级菜单
            menuService.selectChildren(oneActions,roleId);
            //填充meta数据
            menuService.setChildrenMeta(oneActions);
            //存入缓存
            localCache.putValue(roleId,oneActions);
//        });
        return appMessage;
    }

    @ApiOperation("删除角色")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "角色id")})
    @Log(logContent = "删除角色",logModule = LogModule.ROLE,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE,produces = "application/json")
    public AppMessage deleteRole(@PathVariable String id) throws ParseException {

        return roleService.deleteRole(id);
    }





//    /**
//     * 查询所有角色
//     */
//    @RequestMapping(method = RequestMethod.GET)
//    public AppMessage query(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
//                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
//        Map<String, String> params = new HashMap<>();
//        HashMap<String, Object> hashMap = roleService.selectList(params,pageNum,pageSize);
//        return AppMessage.success(getDataTable((List<SysRoleDto>) hashMap.get("data"),(long)hashMap.get("total")),"查询所有角色成功");
//    }
//
//    @Log(logContent = "新增角色",logModule = LogModule.ROLE,logType = LogType.OPERATION)
//    @RequestMapping(method = RequestMethod.POST)
//    public AppMessage add( @RequestBody SysRolePo sysRolePo) {
//        return roleService.add(sysRolePo);
//    }
//
//    @Log(logContent = "修改角色",logModule = LogModule.ROLE,logType = LogType.OPERATION)
//    @RequestMapping(value = "/update", method = RequestMethod.POST)
//    public AppMessage update(@RequestBody SysRolePo sysRolePo) {
//        return roleService.update(sysRolePo);
//    }
//
//    @Log(logContent = "删除角色",logModule = LogModule.ROLE,logType = LogType.OPERATION)
//    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
//    public AppMessage delete(@PathVariable String id) {
//        return roleService.deleteByID(id);
//    }

}

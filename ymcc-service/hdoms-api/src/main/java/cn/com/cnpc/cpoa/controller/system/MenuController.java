package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.SysMenuDto;
import cn.com.cnpc.cpoa.service.MenuService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.MenuVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 11:25
 * @Description: 菜单控制器
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/hd/sys/actions")
public class MenuController extends BaseController{

    @Autowired
    MenuService menuService;

    @ApiOperation("查询菜单树")
    @ApiImplicitParams({@ApiImplicitParam(name = "menuFlag",value = "用户数据权限")})
    @RequestMapping(value = "/tree", method = RequestMethod.GET)
    public AppMessage selectTreeActions(@RequestParam(value = "menuFlag", defaultValue = "") String menuFlag) {
        List<MenuVo> dtos = menuService.selectTreeActions(menuFlag);
        return AppMessage.success(dtos, "查询菜单树成功");
    }

    /**
     * 查询所有菜单项
     */
    @ApiOperation("查询所有菜单项")
    @ApiImplicitParams({@ApiImplicitParam(name = "name",value = "菜单名称"),
            @ApiImplicitParam(name = "type",value = "类型"),
            @ApiImplicitParam(name = "parentName",value = "父菜单名称")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "name",defaultValue="") String name,
                            @RequestParam(value = "type",defaultValue="") String type,
                            @RequestParam(value = "parentName", defaultValue = "") String parentName) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("type", type);
        params.put("parentName", parentName);
        return menuService.selectMenuList(params);
    }

    @ApiOperation("新增菜单")
    @ApiImplicitParams({@ApiImplicitParam(name = "menu",value = "菜单实体")})
    @Log(logContent = "新增菜单",logModule = LogModule.MENU,logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage add( @RequestBody MenuVo menu) {
        String name = menu.getName();
        Map<String,Object> param2=new HashMap<>();
        param2.put("name",name);
        List<SysMenuDto> sysMenuDtos = menuService.selectList2(param2);
        if(null!=sysMenuDtos&&sysMenuDtos.size()>0){
            return AppMessage.error("新增菜单失败,菜单已存在");
        }
        String menuId= StringUtils.getUuid32();
        SysMenuDto menuDao=new SysMenuDto();
        BeanUtils.copyBeanProp(menuDao,menu);
        menuDao.setActionId(menuId);
        int save = menuService.save(menuDao);
        if (save == 1) {
            return AppMessage.success(menuDao.getActionId(),"新增菜单成功");
        }
        return AppMessage.error("新增菜单失败");
    }

    @ApiOperation("修改菜单")
    @ApiImplicitParams({@ApiImplicitParam(name = "menu",value = "修改后的菜单实体")})
    @Log(logContent = "修改菜单",logModule = LogModule.MENU,logType = LogType.OPERATION)
    @RequestMapping( method = RequestMethod.PUT)
    public AppMessage update(@RequestBody MenuVo menu) {
        String name = menu.getName();
        if(StringUtils.isNotEmpty(name)){
            Map<String,Object> param2=new HashMap<>();
            param2.put("name", name);
            List<SysMenuDto> sysMenuDtos = menuService.selectList2(param2);
            if(null!=sysMenuDtos&&sysMenuDtos.size()>0){
                return AppMessage.errorObjId(menu.getActionId(),"修改菜单失败,菜单已存在");
            }
        }

        SysMenuDto menuDao=new SysMenuDto();
        BeanUtils.copyBeanProp(menuDao,menu);
        int i = menuService.updateNotNull(menuDao);
        if(i==1){
            return AppMessage.success(menu.getActionId(), "更新菜单成功");
        }
        return AppMessage.errorObjId(menu.getActionId(),"更新菜单失败成功");
    }

    @ApiOperation("删除菜单")
    @ApiImplicitParams({@ApiImplicitParam(name = "id",value = "删除菜单id")})
    @Log(logContent = "删除菜单",logModule = LogModule.MENU,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete( @PathVariable String id) {
        int delete = menuService.delete(id);
        if (delete == 1) {
            return AppMessage.success(id, "删除菜单成功");
        }
        return AppMessage.errorObjId(id,"删除菜单失败");
    }

    @ApiOperation("查询当前页面按钮")
    @RequestMapping(value = "/button", method = RequestMethod.GET)
    public AppMessage selectButton(@RequestParam(value = "path", defaultValue = "") String path) {

        List<String> list = menuService.selectButton(path);
        return AppMessage.success(list, "查询当前页面按钮成功");
    }

    @Log(logContent = "测试异常",logModule = LogModule.MENU,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public AppMessage testLogException(@PathVariable String id) {
        throw new AppException("测试异常回滚");
    }

}

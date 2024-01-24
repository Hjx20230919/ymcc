package cn.com.cnpc.cpoa.controller.system;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.enums.UserEnabledEnum;
import cn.com.cnpc.cpoa.mapper.SysMenuDtoMapper;
import cn.com.cnpc.cpoa.mapper.SysRoleDtoMapper;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.utils.*;
import cn.com.cnpc.cpoa.vo.UserMenuVo;
import cn.com.cnpc.cpoa.vo.UserVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户控制器
 *
 * @author scchenyong@189.cn
 * @create 2019-01-14
 */
@Api(tags = "用户管理")
@RestController
@RequestMapping("/hd/user")
public class UserController extends BaseController {

    @Autowired
    UserService userService;

    @Autowired
    UserMenuService userMenuService;

    @Autowired
    DeptService deptService;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    private SysRoleDtoMapper roleDtoMapper;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private SysMenuDtoMapper menuDtoMapper;

    /**
     * 用户密码
     */
    @Value("${app.user.pass:12345678}")
    private String userPass;

    @Value(("${app.page.size:20}"))
    private int pageSize;

    @ApiOperation("新增用户")
    @Log(logContent = "新增用户", logModule = LogModule.USER, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage add(@RequestBody UserVo user) {
        SysUserDto userDto = new SysUserDto();
        String userName = user.getUserName();
        String account = user.getAccount();
        Map<String, Object> param = new HashMap<>();
        param.put("userAccount", account);
        List<SysUserDto> sysUserDtos = userService.selectPage(param);
        if (null != sysUserDtos && sysUserDtos.size() > 0) {
            return AppMessage.error("新增用户失败,账号已存在");
        }

        Map<String, Object> param2 = new HashMap<>();
        param2.put("userPhone", user.getUserPhone());
        List<SysUserDto> sysUserDtos2 = userService.selectPage(param2);
        if (null != sysUserDtos2 && sysUserDtos2.size() > 0) {
            return AppMessage.error("新增用户失败,用户电话已存在");
        }

        Map<String, Object> param3 = new HashMap<>();
        param3.put("userName", user.getUserName());
        List<SysUserDto> sysUserDtos3 = userService.selectPage(param3);
        if (null != sysUserDtos3 && sysUserDtos3.size() > 0) {
            return AppMessage.error("新增用户失败,用户名称已存在");
        }
        String userId = StringUtils.getUuid32();
        userDto.setUserId(userId);

        userDto.setUserAccount(user.getAccount());
        userDto.setUserName(userName);
        // 初始用户密码
        // 采用MD5+盐码方式混合
        userDto.setUserSalt(RandomUtils.code(6));
        userDto.setUserPassword(MD5Utils.MD5(MD5Utils.MD5(Constants.SALT_1 + "AJY_qgb123456"), userDto.getUserSalt()));
        //TODO  新增添加角色到角色用户表
        String roleName = user.getUserRole();
        HashMap<String, Object> roleMap = new HashMap<>(4);
        roleMap.put("cName", roleName);
        List<SysRoleDto> sysRoleDtos = roleDtoMapper.selectRole(roleMap);
        if (sysRoleDtos.size() > 0) {
            SysRoleDto sysRoleDto = sysRoleDtos.get(0);
            SysUserRoleDto userRoleDto = new SysUserRoleDto();
            userRoleDto.setRoleId(sysRoleDto.getRoleId());
            userRoleDto.setUserId(userId);
            userRoleDto.setUserActionId(StringUtils.getUuid32());
            userRoleService.save(userRoleDto);
        } else {
            return AppMessage.error("当前角色不存在，请确认后重试！！");
        }

        userDto.setUserPhone(user.getUserPhone());
        userDto.setUserMail(user.getUserMail());
        userDto.setDeptId(user.getDeptId());
        userDto.setCreateAt(DateUtils.getNowDate());
        userDto.setUpdateAt(DateUtils.getNowDate());
        userDto.setUserRole(roleName);
        userDto.setEnabled(UserEnabledEnum.ENABLED.getKey());
        userDto.setDataScope(user.getDataScope());
        userDto.setSysModule(user.getSysModule());
        int save = userService.save(userDto);
        if (save == 1) {
            return AppMessage.success(userDto.getUserId(), "新增用户成功");
        }
        return AppMessage.error("新增用户失败");
    }

    @Log(logContent = "修改用户", logModule = LogModule.USER, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id, @RequestBody UserVo user) {
        String account = user.getAccount();
        String userName = user.getUserName();
        String userPhone = user.getUserPhone();
        String userMail = user.getUserMail();
        String deptId = user.getDeptId();
        String passWord = user.getPassWord();
        SysUserDto userDtoFromDb = userService.selectByKey(id);
        if (StringUtils.isNotEmpty(account)) {
            String userAccount = userDtoFromDb.getUserAccount();
            if (!userAccount.equals(account)) {
                Map<String, Object> param = new HashMap<>();
                param.put("userAccount", account);
                List<SysUserDto> sysUserDtos = userService.selectPage(param);
                if (null != sysUserDtos && sysUserDtos.size() > 1) {
                    return AppMessage.error("修改用户失败,账号已存在");
                }
            }
        }

        if (StringUtils.isNotEmpty(userPhone)) {
            String userPhone1 = userDtoFromDb.getUserPhone();
            if (!userPhone1.equals(userPhone)) {
                Map<String, Object> param2 = new HashMap<>();
                param2.put("userPhone", user.getUserPhone());
                List<SysUserDto> sysUserDtos2 = userService.selectPage(param2);
                if (null != sysUserDtos2 && sysUserDtos2.size() > 0) {
                    return AppMessage.error("修改用户失败,用户电话已存在");
                }
            }
        }

        if (StringUtils.isNotEmpty(userName)) {
            String userName1 = userDtoFromDb.getUserName();
            if (!userName1.equals(userName)) {
                Map<String, Object> param3 = new HashMap<>();
                param3.put("userName", user.getUserName());
                List<SysUserDto> sysUserDtos3 = userService.selectPage(param3);
                if (null != sysUserDtos3 && sysUserDtos3.size() > 0) {
                    return AppMessage.error("修改用户失败,用户名称已存在");
                }
            }
        }

        //TODO  新增修改角色用户表
        String roleName = user.getUserRole();
        HashMap<String, Object> roleMap = new HashMap<>(4);
        roleMap.put("cName", roleName);
        List<SysRoleDto> sysRoleDtos = roleDtoMapper.selectRole(roleMap);
        if (sysRoleDtos.size() > 0) {
            SysRoleDto sysRoleDto = sysRoleDtos.get(0);
            //根据用户查询用户角色表，并修改角色
            HashMap<String, Object> userRoleMap = new HashMap<>(4);
            userRoleMap.put("userId", id);
            List<SysUserRoleDto> sysUserRoleDtos = userRoleService.selectUserRoles(userRoleMap);
            if (sysUserRoleDtos.size() > 0) {
                SysUserRoleDto userRoleDto = sysUserRoleDtos.get(0);
                userRoleDto.setRoleId(sysRoleDto.getRoleId());
                userRoleService.updateNotNull(userRoleDto);
            } else {//如果不存在则新增
                SysUserRoleDto userRoleDto = new SysUserRoleDto();
                userRoleDto.setRoleId(sysRoleDto.getRoleId());
                userRoleDto.setUserId(id);
                userRoleDto.setUserActionId(StringUtils.getUuid32());
                userRoleService.save(userRoleDto);
            }
        } else {
            return AppMessage.error("当前角色不存在，请确认后重试！！");
        }

        SysUserDto userDto = new SysUserDto();
        userDto.setUserId(id);
        userDto.setUserAccount(account);
        userDto.setUserName(userName);
        userDto.setUserPhone(userPhone);
        userDto.setUserMail(userMail);
        userDto.setDeptId(deptId);
        userDto.setUserRole(roleName);
        userDto.setEnabled(user.getEnabled());
        userDto.setDataScope(user.getDataScope());
        userDto.setSysModule(user.getSysModule());
        if (StringUtils.isNotEmpty(passWord)) {
            userDto.setUserSalt(RandomUtils.code(6));
            userDto.setUserPassword(MD5Utils.MD5(passWord, userDto.getUserSalt()));
        }
        userDto.setUpdateAt(DateUtils.getNowDate());
        userService.updateNotNull(userDto);
        return AppMessage.success(id, "更新用户成功");
    }

    @Log(logContent = "删除用户", logModule = LogModule.USER, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", id);
        //如果处于 合同 结算 步骤审核人中 则不能再被删除
        Integer countBusiness = userService.selectCountBusiness(params);
        if (countBusiness > 0) {
            return AppMessage.error("删除用户失败,用户处于业务中，无法删除");
        }
        boolean deleteChain = userService.deleteChain(id);

        if (deleteChain) {
            return AppMessage.success(id, "删除用户成功");
        }
        return AppMessage.error("删除用户失败");
    }

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                            @RequestParam(value = "userName", defaultValue = "") String userName,
                            @RequestParam(value = "userAccount", defaultValue = "") String userAccount,
                            @RequestParam(value = "userPhone", defaultValue = "") String userPhone) {
        // startPage();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userName", userName);
        params.put("userAccount", userAccount);
        params.put("userPhone", userPhone);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<SysUserDto> sysUserDtos = userService.selectPage(params);
        for (SysUserDto userDto : sysUserDtos) {
            //String s = MD5Utils.MD5("AJY_qgb123456", userDto.getUserSalt());
            String s = MD5Utils.MD5(MD5Utils.MD5(Constants.SALT_1 + "AJY_qgb123456"), userDto.getUserSalt());
            String userPassword = userDto.getUserPassword();
            if (s.equals(userPassword)) {
                userDto.setFlag("1");
            }
        }

        //3、获取分页查询后的数据
        //     PageInfo<SysUserDto> sysUserDtoPageInfo = new PageInfo<>(sysUserDtos);
        TableDataInfo dataTable = getDataTable(sysUserDtos);

        return AppMessage.success(dataTable, "查询用户成功");
        //return AppMessage.result(sysUserDtoPageInfo);
    }

    @ApiOperation("查询所有用户")
    @RequestMapping(method = RequestMethod.GET, value = "allUser")
    public AppMessage queryAllUser() {
        Map<String, Object> params = new HashMap<String, Object>(4);
        //2、执行查询
        List<SysUserDto> sysUserDtos = userService.selectPage(params);
        return AppMessage.success(sysUserDtos, "查询用户成功");
    }

    /**
     * 新增用户菜单
     *
     * @return
     */
    @Log(logContent = "新增用户相关菜单权限", logModule = LogModule.USER, logType = LogType.OPERATION)
    @RequestMapping(value = "/userMenu", method = RequestMethod.POST)
    public AppMessage addUserMenu(@RequestBody UserMenuVo userMenuVo) {

        SysUserMenuDto userActionsDto = new SysUserMenuDto();
        userActionsDto.setUserActionId(StringUtils.getUuid32());
        userActionsDto.setActionId(userMenuVo.getActionId());
        userActionsDto.setUserId(userMenuVo.getUserId());
        int save = userMenuService.save(userActionsDto);
        if (save == 1) {
            return AppMessage.success(userActionsDto.getUserId(), "新增用户菜单成功");
        }
        return AppMessage.error("新增用户菜单失败");
    }


    /**
     * 新增用户菜单
     *
     * @return
     */
    @Log(logContent = "删除用户相关菜单权限", logModule = LogModule.USER, logType = LogType.OPERATION)
    @RequestMapping(value = "/userMenu", method = RequestMethod.DELETE)
    public AppMessage deleteUserMenu(@RequestParam(value = "userId") String userId,
                                     @RequestParam(value = "actionId") String actionId) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("actionId", actionId);

        int delete = userMenuService.deleteByAny(params);
        if (delete == 1) {
            return AppMessage.success(actionId, "删除用户菜单成功");
        }
        return AppMessage.error("删除用户菜单失败");
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    @RequestMapping(value = "/currentUser", method = RequestMethod.GET)
    public AppMessage getCurrentUser() {
        String sessionUserId = ServletUtils.getSessionUserId();
        SysUserDto sysUserDto = userService.selectByKey(sessionUserId);
//        SysUserDto sysUserDto = userService.selectByKey("3985518face749fda7f25fe1aa83baeb");
        String deptId = sysUserDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        sysUserDto.setDeptName(null != deptDto ? deptDto.getDeptName() : "");
        if (StringUtils.isNotEmpty(sysUserDto.getEntrustUserId())) {
            SysUserDto userDto = userService.selectByKey(sysUserDto.getEntrustUserId());
            sysUserDto.setEntrustUserName(userDto.getUserName());
        }
        String s = MD5Utils.MD5(MD5Utils.MD5(Constants.SALT_1 + "AJY_qgb123456"), sysUserDto.getUserSalt());
        String userPassword = sysUserDto.getUserPassword();
        if (s.equals(userPassword)) {
            sysUserDto.setFlag("1");
        }
        return AppMessage.success(sysUserDto, "查询当前用户成功");
    }

    /**
     * 获取立项采购人员 黄家伟
     *
     * @return
     */
    @RequestMapping(value = "/proPurchaseUser", method = RequestMethod.GET)
    public AppMessage proPurchaseUser() {
        BizSysConfigDto proPurchaseUser = activitiService.getPrjSysConfig(Constants.PROPURCHASEUSER);
        BizSysConfigDto proSplitUser = activitiService.getPrjSysConfig(Constants.PROSPLITUSER);

        Map<String, Object> params = new HashMap<>();
        SysUserDto proPurchaseUserDto = userService.selectByKey(proPurchaseUser.getCfgValue());

        SysUserDto proSplitUserDto = userService.selectByKey(proSplitUser.getCfgValue());

        params.put("proPurchaseUser", proPurchaseUserDto);
        params.put("proSplitUser", proSplitUserDto);

        return AppMessage.success(params, "查询立项采购人员成功");
    }

    /**
     * 更具部门id获取用户信息
     *
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/dept", method = RequestMethod.GET)
    public AppMessage queryUser(@RequestParam(value = "deptId", defaultValue = "") String deptId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("deptId", deptId);
        List<SysUserDto> sysUserDtos = userService.selectPage(params);
        return AppMessage.success(sysUserDtos, "查询用户成功");
    }

    @Log(logContent = "重置密码", logModule = LogModule.USER, logType = LogType.OPERATION)
    @RequestMapping(value = "reset/{id}", method = RequestMethod.PUT)
    public AppMessage reset(@PathVariable String id) {
        SysUserDto userDto = new SysUserDto();
        userDto.setUserId(id);
        userDto.setUserSalt(RandomUtils.code(6));
        userDto.setUserPassword(MD5Utils.MD5(MD5Utils.MD5(Constants.SALT_1 + "AJY_qgb123456"), userDto.getUserSalt()));
        userDto.setUpdateAt(DateUtils.getNowDate());
        userService.updateNotNull(userDto);
        return AppMessage.success(id, "重置密码成功");
    }

    @Log(logContent = "修改密码", logModule = LogModule.USER, logType = LogType.OPERATION)
    @RequestMapping(value = "password/{id}", method = RequestMethod.PUT)
    public AppMessage password(@PathVariable String id,
                               @RequestParam(value = "oldPassWord") String oldPassWord,
                               @RequestParam(value = "newPassWord") String newPassWord
    ) {
        SysUserDto userDto = userService.selectByKey(id);
        String userPassword = userDto.getUserPassword();
        if (!userPassword.equals(MD5Utils.MD5(oldPassWord, userDto.getUserSalt()))) {
            return AppMessage.error(id, "密码不正确！");
        }

        SysUserDto userDto2 = new SysUserDto();
        userDto2.setUserId(id);
        userDto2.setUserSalt(RandomUtils.code(6));
        userDto2.setUserPassword(MD5Utils.MD5(newPassWord, userDto2.getUserSalt()));
        userDto2.setUpdateAt(DateUtils.getNowDate());
        userService.updateNotNull(userDto2);
        return AppMessage.success(id, "密码修改成功！");
    }

    @Log(logContent = "用户委托", logModule = LogModule.USER, logType = LogType.OPERATION)
    @RequestMapping(value = "entrust/{sid}/{tid}", method = RequestMethod.PUT)
    public AppMessage entrust(@PathVariable String sid, @PathVariable String tid) {
        SysUserDto userDto = new SysUserDto();
        userDto.setUserId(sid);
        userDto.setEntrustUserId(tid);
        userService.updateNotNull(userDto);
        return AppMessage.success(sid, "委托成功！");
    }

    @Log(logContent = "取消用户委托", logModule = LogModule.USER, logType = LogType.OPERATION)
    @RequestMapping(value = "unEntrust/{id}", method = RequestMethod.PUT)
    public AppMessage entrust(@PathVariable String id) {

        SysUserDto userDto = userService.selectByKey(id);
        userDto.setEntrustUserId(null);
        userService.updateAll(userDto);
        return AppMessage.success(id, "取消委托成功！");
    }

    @RequestMapping(value = "/verificationCode", method = RequestMethod.GET)
    public void getVerificationCode(HttpServletResponse response, @RequestParam(value = "account", defaultValue = "") String account) throws Exception {
        userService.getVerificationCode(response, account);
    }


}

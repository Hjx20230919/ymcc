package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.SysRoleActionDto;
import cn.com.cnpc.cpoa.domain.SysRoleDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.SysUserRoleDto;
import cn.com.cnpc.cpoa.enums.UserEnabledEnum;
import cn.com.cnpc.cpoa.mapper.SysRoleDtoMapper;
import cn.com.cnpc.cpoa.po.SysRolePo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.RoleVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/4/25 14:47
 */
@Service
public class SysRoleService extends AppService<SysRoleDto> {

    @Autowired
    private SysRoleDtoMapper roleDtoMapper;

    @Autowired
    private SysRoleActionService roleActionService;

    @Autowired
    private SysUserRoleService userRolesService;

    @Autowired
    private UserService userService;

    /**
     * 查询所有角色
     */
    public HashMap<String, Object> selectList(Map<String, String> params, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<SysRoleDto> roles = roleDtoMapper.selectList(params);
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("data",roles);
        hashMap.put("total",new PageInfo<>(roles).getTotal());
        return hashMap;
    }

    /**
     * 添加角色
     * @param sysRolePo 角色对象
     * @return
     */
    public AppMessage add(SysRolePo sysRolePo) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("name",sysRolePo.getName());
        List<SysRoleDto> sysRoleDtos = roleDtoMapper.selectList(hashMap);
        if (sysRoleDtos.size() > 0){
            return AppMessage.error("角色已存在，添加失败！！");
        }
        SysRoleDto sysRoleDto = new SysRoleDto();
        BeanUtils.copyBeanProp(sysRoleDto,sysRolePo);
        sysRoleDto.setRoleId(StringUtils.getUuid32());
        int save = save(sysRoleDto);
        if (save == 1) {
            return AppMessage.result("新增角色成功");
        }
        return AppMessage.error("新增角色失败！！");

    }

    /**
     * 修改角色
     * @param sysRolePo 角色对象
     * @return
     */
    public AppMessage update(SysRolePo sysRolePo) {
        String name = sysRolePo.getName();
        if(StringUtils.isNotEmpty(name)){
            Map<String,String> params=new HashMap<>();
            params.put("name", name);
            List<SysRoleDto> list = roleDtoMapper.selectList(params);
            if(null != list && list.size() > 0){
                return AppMessage.errorObjId(sysRolePo.getRoleId(),"修改角色失败,角色已存在");
            }
        }

        SysRoleDto sysRoleDto = new SysRoleDto();
        BeanUtils.copyBeanProp(sysRoleDto,sysRolePo);
        int i = updateNotNull(sysRoleDto);
        if(i == 1){
            return AppMessage.success(sysRoleDto.getRoleId(), "修改角色成功");
        }
        return AppMessage.errorObjId(sysRoleDto.getRoleId(),"修改角色失败");
    }

    /**
     * 删除角色
     * @param id    角色ID
     * @return
     */
    public AppMessage deleteById(String id) {
        int i = roleDtoMapper.deleteById(id);
        if (i == 1){
            return AppMessage.success(i, "删除角色成功");
        }
        return AppMessage.errorObjId(id,"删除角色失败，该角色为系统角色，不能删除！！");
    }

    public List<RoleVo> selectRole(Map<String, Object> params) {
        List<RoleVo> roleDtos = new ArrayList<>();
        //1 查询个人所拥有的角色
        List<SysRoleDto> userRoles = roleDtoMapper.selectUserRole(params);
        //2  查询所有角色遍历设置已有角色标志
        List<SysRoleDto> roles = selectAll();
        for (SysRoleDto role : roles) {
            RoleVo roleVo = RoleVo.builder().build();
            BeanUtils.copyBeanProp(roleVo,role);
            if (userRoles.contains(role)) {
                roleVo.setHaveFlag("1");
            }
            roleDtos.add(roleVo);
        }

        return roleDtos;

    }

    public List<RoleVo> selectActionsRole(Map<String, Object> params) {
        //1 查询角色基本信息
        List<SysRoleDto> roles = roleDtoMapper.selectRole(params);


        //2 查询角色所含的角色菜单权限信息
        List<SysRoleActionDto> roleActions = roleActionService.selectAll();
        Map<String, List<String>> roleActionMap = new HashMap<>();
        for (SysRoleActionDto roleAction : roleActions) {
            String roleId = roleAction.getRoleId();
            String actionId = roleAction.getActionId();
            List<String> list = roleActionMap.get(roleId);
            if (StringUtils.isEmpty(list)) {
                list = new ArrayList<>();
                list.add(actionId);
                roleActionMap.put(roleId, list);
            } else {
                list.add(actionId);
            }
        }

        //3 封装角色的菜单信息
        List<RoleVo> roleDtos = new ArrayList<>();
        for (SysRoleDto role : roles) {
            RoleVo roleVo = RoleVo.builder().build();
            BeanUtils.copyBeanProp(roleVo,role);
            roleVo.setActionIds(roleActionMap.get(roleVo.getRoleId()));
            roleDtos.add(roleVo);
        }

        return roleDtos;

    }

    @Transactional(rollbackFor = Exception.class)
    public AppMessage addRoleActions(RoleVo dto) {
        String name = dto.getName();
        Map<String, Object> roleParam = new HashMap<>();
        roleParam.put("cName", name);
        List<SysRoleDto> roles = roleDtoMapper.selectRole(roleParam);
        if (StringUtils.isNotEmpty(roles)) {
            throw new AppException("角色名已存在，请勿重复新增");
        }

        String roleId = StringUtils.getUuid32();
        SysRoleDto role = new SysRoleDto();
        BeanUtils.copyBeanProp(role,dto);
        role.setRoleId(roleId);

        List<String> actionIds = dto.getActionIds();
        List<SysRoleActionDto> roleActionsList = new ArrayList<>();
        for (String actionId : actionIds) {
            SysRoleActionDto roleActions = new SysRoleActionDto();
            roleActions.setRoleActionId(StringUtils.getUuid32());
            roleActions.setRoleId(roleId);
            roleActions.setActionId(actionId);
            roleActionsList.add(roleActions);
        }

        save(role);

        roleActionService.saveList(roleActionsList);

        return AppMessage.success(roleId, "新增角色成功");

    }

    @Transactional(rollbackFor = Exception.class)
    public AppMessage updateRoleActions(RoleVo dto) {
        SysRoleDto existRole = selectByKey(dto.getRoleId());
        String name = dto.getName();
        if (StringUtils.isNotEmpty(name) && !name.equals(existRole.getName())) {
            Map<String, Object> roleParam = new HashMap<>();
            roleParam.put("cName", name);
            List<SysRoleDto> roles = roleDtoMapper.selectRole(roleParam);
            if (StringUtils.isNotEmpty(roles)) {
                throw new AppException("角色名已存在");
            }

        }
        SysRoleDto role = new SysRoleDto();
        BeanUtils.copyBeanProp(role,dto);

        String roleId = dto.getRoleId();
        //1 查询角色菜单权限表，删除已有项
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", roleId);
        List<SysRoleActionDto> roleActions = roleActionService.selectRoleActions(params);
        List<Object> roleActionKeys = new ArrayList<>();
        for (SysRoleActionDto roleAction : roleActions) {
            roleActionKeys.add(roleAction.getRoleActionId());
        }

        roleActionService.deleteList(roleActionKeys);

        //2 新增项
        updateNotNull(role);

        List<String> actionIds = dto.getActionIds();
        List<SysRoleActionDto> roleActionsList = new ArrayList<>();
        for (String actionId : actionIds) {
            SysRoleActionDto roleAction = new SysRoleActionDto();
            roleAction.setRoleActionId(StringUtils.getUuid32());
            roleAction.setRoleId(roleId);
            roleAction.setActionId(actionId);
            roleActionsList.add(roleAction);
        }

        roleActionService.saveList(roleActionsList);


        return AppMessage.success(true, "修改角色成功");

    }

    @Transactional(rollbackFor = Exception.class)
    public AppMessage deleteRole(String id) {
        Map<String, Object> userRoleParams = new HashMap<>();
        userRoleParams.put("roleId", id);
        List<SysUserRoleDto> userRoles = userRolesService.selectUserRoles(userRoleParams);
        for (SysUserRoleDto userRoles1 : userRoles) {
            SysUserDto sysUser = userService.selectByKey(userRoles1.getUserId());
            if (sysUser.getEnabled().equals(UserEnabledEnum.ENABLED.getKey())) {
                throw new AppException("删除失败，该角色下已存在用户信息");
            }

        }

        //1 查询角色菜单权限表，删除已有项
        Map<String, Object> params = new HashMap<>();
        params.put("roleId", id);
        List<SysRoleActionDto> roleActions = roleActionService.selectRoleActions(params);
        List<Object> roleActionKeys = new ArrayList<>();
        for (SysRoleActionDto roleAction : roleActions) {
            roleActionKeys.add(roleAction.getRoleActionId());
        }

        roleActionService.deleteList(roleActionKeys);


        //2 删除菜单

        delete(id);

        return AppMessage.success(true, "删除角色成功");

    }
}

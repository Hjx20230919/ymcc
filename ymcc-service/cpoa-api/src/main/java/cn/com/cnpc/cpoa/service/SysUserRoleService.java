package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.SysRoleDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.SysUserRoleDto;
import cn.com.cnpc.cpoa.mapper.SysUserRoleDtoMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/4/25 17:41
 */
@Service
public class SysUserRoleService extends AppService<SysUserRoleDto> {

    @Autowired
    private SysUserRoleDtoMapper userRoleDtoMapper;

    @Autowired
    private UserService userService;
    
    @Autowired 
    private SysRoleService roleService;

    @Autowired
    private SysUserRoleService userRoleService;

    public String selectRoleIdByUserId(String userId){
        return userRoleDtoMapper.selectRoleIdByUserId(userId);
    }

    public List<SysUserRoleDto> selectUserRoles(Map<String, Object> userRoleParams) {
        return userRoleDtoMapper.selectUserRoles(userRoleParams);
    }

    public void initUserRole() {
        //查询用户角色未关联用户角色表用户
        List<SysUserDto> sysUserDtos = userService.selectUserNotRole();
        //查询所有角色
        Map<String, String> map = roleService.selectAll().stream().collect(Collectors.toMap(SysRoleDto::getName, SysRoleDto::getRoleId));
        sysUserDtos.parallelStream().forEach(p -> {
            if (map.get(p.getUserRole()) != null) {
                String roleId = map.get(p.getUserRole());
                SysUserRoleDto userRoleDto = new SysUserRoleDto();
                userRoleDto.setRoleId(roleId);
                userRoleDto.setUserId(p.getUserId());
                userRoleDto.setUserActionId(StringUtils.getUuid32());
                userRoleService.save(userRoleDto);
            }
        });
    }
}

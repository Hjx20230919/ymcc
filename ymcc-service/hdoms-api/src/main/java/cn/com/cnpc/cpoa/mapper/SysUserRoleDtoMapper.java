package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.SysUserRoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserRoleDtoMapper extends AppMapper<SysUserRoleDto> {

    String selectRoleIdByUserId(String userId);

    /**
     * 查询用户角色
     *
     * @param params
     * @return
     */
    List<SysUserRoleDto> selectUserRoles(Map<String, Object> params);
}

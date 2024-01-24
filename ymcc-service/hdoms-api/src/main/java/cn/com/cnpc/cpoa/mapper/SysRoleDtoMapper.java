package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.SysRoleDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysRoleDtoMapper extends AppMapper<SysRoleDto> {

    List<SysRoleDto> selectList(Map<String,String> map);

    int deleteById(String id);

    /**
     * 查询用户所含角色
     *
     * @param params
     * @return
     */
    List<SysRoleDto> selectUserRole(Map<String, Object> params);

    /**
     * 按条件查询角色
     *
     * @param params
     * @return
     */
    List<SysRoleDto> selectRole(Map<String, Object> params);
}

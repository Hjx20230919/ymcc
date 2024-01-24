package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.SysRoleActionDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysRoleActionDtoMapper extends AppMapper<SysRoleActionDto> {

    List<String> selectMenuAuthByRoleID(String id);

    List<String> selectActionUrlByRoleID(String id);

    void deleteByRoleID(@Param("roleID")String roleID);

    /**
     * 查询角色菜单
     *
     * @param params
     * @return
     */
    List<SysRoleActionDto> selectRoleActions(Map<String, Object> params);
}

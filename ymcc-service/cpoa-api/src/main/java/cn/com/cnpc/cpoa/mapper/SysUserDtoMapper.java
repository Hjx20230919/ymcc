package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


/**
 * 用户映射
 *
 * @author chenyong
 */
@Mapper
public interface SysUserDtoMapper extends AppMapper<SysUserDto> {

    /**
     * 查询用户列表
     *
     * @param params
     * @return
     */
    List<SysUserDto> selectList(Map<String, Object> params);

    List<SysUserDto> selectList2(Map<String, Object> param2);

    Integer selectCountBusiness(Map<String, Object> params);

    SysUserDto selectUserByContId(@Param("contId")String contId);

    SysUserDto selectUserByUserName(@Param("userName")String userName);

    List<SysUserDto> selectUserNotRole();
}

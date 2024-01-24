package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.SysUserMenuDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 15:35
 * @Description: 用户权限映射
 */
@Mapper
public interface SysUserMenuDtoMapper extends AppMapper<SysUserMenuDto> {

    int deleteByAny(Map<String, Object> params);
}

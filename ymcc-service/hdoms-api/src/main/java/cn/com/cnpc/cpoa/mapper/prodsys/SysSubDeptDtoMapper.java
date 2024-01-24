package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.SysSubDeptDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author anonymous
 * @create 28/02/2020 20:41
 * @since 1.0.0
 */
@Mapper
public interface SysSubDeptDtoMapper extends AppMapper<SysSubDeptDto> {
    List<SysSubDeptDto> selectList(Map<String, Object> params);
}

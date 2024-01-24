package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.BizWorkTableDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author anonymous
 * @create 12/04/2020 09:16
 * @since 1.0.0
 */
@Mapper
public interface BizWorkTableDtoMapper extends AppMapper<BizWorkTableDto> {
    List<BizWorkTableDto> selectList(Map<String, Object> params);
}

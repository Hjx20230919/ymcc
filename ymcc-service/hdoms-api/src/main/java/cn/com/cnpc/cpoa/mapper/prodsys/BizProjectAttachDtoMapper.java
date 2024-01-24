package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectAttachDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author anonymous
 * @create 15/03/2020 21:35
 * @since 1.0.0
 */
@Mapper
public interface BizProjectAttachDtoMapper extends AppMapper<BizProjectAttachDto> {
    List<BizProjectAttachDto> selectList(Map<String, Object> params);
}

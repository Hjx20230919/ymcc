package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectWorkDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <项目业务关联表映射>
 *
 * @author anonymous
 * @create 12/02/2020 23:33
 * @since 1.0.0
 */
@Mapper
public interface BizProjectWorkDtoMapper extends AppMapper<BizProjectWorkDto> {
    List<BizProjectWorkDto> selectList(Map<String, Object> params);
    List<BizProjectWorkDto> selectWithWorkList(Map<String, Object> params);
}

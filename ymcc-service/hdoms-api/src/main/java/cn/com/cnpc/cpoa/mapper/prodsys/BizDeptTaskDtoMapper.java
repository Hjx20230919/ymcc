package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.BizDeptTaskDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author anonymous
 * @create 22/04/2020 21:58
 * @since 1.0.0
 */
@Mapper
public interface BizDeptTaskDtoMapper extends AppMapper<BizDeptTaskDto> {
    List<BizDeptTaskDto> selectList(Map<String, Object> params);

    /**
     * 劳动竞赛评比季度各单位指标
     *
     * @param params
     * @return
     */
    List<BizDeptTaskDto> sumBySeason(Map<String, Object> params);
}

package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.BizRptLaborScoreDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <单位劳动竞赛得分>
 *
 * @author anonymous
 * @create 10/05/2020 09:52
 * @since 1.0.0
 */
@Mapper
public interface BizRptLaborScoreDtoMapper extends AppMapper<BizRptLaborScoreDto> {
    List<BizRptLaborScoreDto> selectList(Map<String, Object> params);

    List<BizRptLaborScoreDto> queryLaborScoreSeasons();
}

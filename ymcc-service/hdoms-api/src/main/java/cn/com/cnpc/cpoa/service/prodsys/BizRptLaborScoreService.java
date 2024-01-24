package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.prodsys.BizRptLaborScoreDto;
import cn.com.cnpc.cpoa.mapper.prodsys.BizRptLaborScoreDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <>
 *
 * @author anonymous
 * @create 29/04/2020 21:12
 * @since 1.0.0
 */
@Service
public class BizRptLaborScoreService extends AppService<BizRptLaborScoreDto> {

    @Autowired
    BizRptLaborScoreDtoMapper bizRptLaborScoreDtoMapper;

    public List<BizRptLaborScoreDto> selectList(Map<String, Object> params) {
        return bizRptLaborScoreDtoMapper.selectList(params);
    }

    public List<String> queryLaborScoreSeasons() {
        List<BizRptLaborScoreDto> dtos = bizRptLaborScoreDtoMapper.queryLaborScoreSeasons();
        return dtos == null || dtos.isEmpty() ? Collections.EMPTY_LIST : dtos.stream().map(BizRptLaborScoreDto::getQuarter).collect(Collectors.toList());
    }
}

package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewSummaryDto;
import cn.com.cnpc.cpoa.po.contractor.AccessSummaryPo;
import cn.com.cnpc.cpoa.po.contractor.ContReviewSummaryPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContReviewSummaryDtoMapper extends AppMapper<ContReviewSummaryDto> {
    int selectSummaryByContIdAndYear(Map<String,Object> param);

    List<ContReviewSummaryPo> selectSummaryByMap(Map<String,Object> param);

    List<AccessSummaryPo> selectSummaryByReviewAndAccessLevel(Map<String,Object> param);
}

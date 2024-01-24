package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizDealStatisticsHisDto;
import cn.com.cnpc.cpoa.po.BizDealStatisticsHisPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BizDealStatisticsHisDtoMapper extends AppMapper<BizDealStatisticsHisDto> {

    List<BizDealStatisticsHisPo> selectDealStatisticsHis(Map<String,Object> param);
}

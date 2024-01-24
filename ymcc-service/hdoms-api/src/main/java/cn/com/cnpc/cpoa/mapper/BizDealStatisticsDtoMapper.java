package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizDealStatisticsDto;
import cn.com.cnpc.cpoa.domain.VDealSttleDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizDealStatisticsDtoMapper extends AppMapper<BizDealStatisticsDto> {

    /**
     * 按条件查询合同统计信息
     * @param params
     * @return
     */
    List<BizDealStatisticsDto> selectDealStatistics(Map<String, Object> params);

    List<BizDealStatisticsDto> selectDealStatisticsSum(Map<String, Object> params);

    VDealSttleDto selectVDealSttle(Map<String, Object> params);
}

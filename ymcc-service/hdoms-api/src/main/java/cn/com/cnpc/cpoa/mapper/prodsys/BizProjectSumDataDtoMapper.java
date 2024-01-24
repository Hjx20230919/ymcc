package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectSumDataDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author wangjun
 * @create 23/03/2020 22:16
 * @since 1.0.0
 */
@Mapper
public interface BizProjectSumDataDtoMapper extends AppMapper<BizProjectSumDataDto> {
    List<BizProjectSumDataDto> selectList(Map<String, Object> params);

    List<BizProjectSumDataDto> selectListContractTypeAnalysis(Map<String, Object> params);

    List<BizProjectSumDataDto> selectListClientDeptAnalysis(Map<String, Object> params);

    List<BizProjectSumDataDto> selectListClientSubDeptAnalysis(Map<String, Object> params);

    /**
     * 市场区域分析
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListMarketAreaAnalysis(Map<String, Object> params);

    /**
     * 川渝地区页岩气
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListMarketAreaWorktypeCydqYyq(Map<String, Object> params);

    /**
     * 川渝地区非页岩气
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListMarketAreaWorktypeCydqFyyq(Map<String, Object> params);

    /**
     * 非川渝地区
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListMarketAreaWorktypeFcydq(Map<String, Object> params);

    /**
     * 川渝地区页岩气
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListMarketAreaServtypeCydqYyq(Map<String, Object> params);

    /**
     * 川渝地区非页岩气
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListMarketAreaServtypeCydqFyyq(Map<String, Object> params);

    /**
     * 非川渝地区
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListMarketAreaServtypeFcydq(Map<String, Object> params);

    /**
     * 业务专业类别表一
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListClasWorkType1(Map<String, Object> params);

    /**
     * 业务专业类别表二
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListClasWorkType2(Map<String, Object> params);

    /**
     * 业务专业类别表三
     *
     * @param params
     * @return
     */
    List<BizProjectSumDataDto> selectListClasServType(Map<String, Object> params);

    /**
     * 市场月报单项统计
     *
     * @param params
     * @return
     */
    String sumMonthly(Map<String, Object> params);
}

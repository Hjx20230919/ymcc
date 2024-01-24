/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * FileName: BizDealLastProjectDtoMapper
 * Author:   wangjun
 * Date:     2020/8/8 10:52
 */
package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.domain.prodsys.BizProjectSumDataDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author wangjun
 * @create 2020/8/8 10:52
 * @since 1.0.0
 */
@Mapper
public interface BizDealLastProjectDtoMapper {
    public List<BizProjectSumDataDto> selectListContractTypeAnalysisOne(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListContractTypeAnalysisTwo(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListContractTypeAnalysisThree(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListContractTypeAnalysisNZ(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListClientDeptAnalysis(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListClientSubDeptAnalysis(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListMarketAreaAnalysis(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListMarketAreaWorktype(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListMarketAreaServtype(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListClasWorkType1(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListClasWorkType2(Map<String, Object> params);

    public List<BizProjectSumDataDto> selectListClasServType(Map<String, Object> params);

    public String sumMonthly(Map<String, Object> params);

}

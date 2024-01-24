package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.domain.DealDataDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizAnalysisDto;
import cn.com.cnpc.cpoa.domain.prodsys.ClientDeptAnalysisDto;
import cn.com.cnpc.cpoa.domain.prodsys.ContractTypeAnalysisDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author wangjun
 * @create 23/03/2020 21:40
 * @since 1.0.0
 */
@Mapper
public interface FusionReportDtoMapper {
    List<ContractTypeAnalysisDto> selectListContractTypeAnalysisOne(Map<String, Object> params);

    List<ContractTypeAnalysisDto> selectListContractTypeAnalysisTwo(Map<String, Object> params);

    List<ContractTypeAnalysisDto> selectListContractTypeAnalysisThree(Map<String, Object> params);

    List<ContractTypeAnalysisDto> selectListContractTypeAnalysisNZ(Map<String, Object> params);

    List<DealDataDto> selectListdealdata(Map<String, Object> params);


    /**
     * report 4
     *
     * @param params
     * @return
     */
    List<ClientDeptAnalysisDto> selectListClientDeptAnalysis(Map<String, Object> params);

    List<ClientDeptAnalysisDto> selectListClientSubDeptAnalysis(Map<String, Object> params);

    List<BizAnalysisDto> selectListMarketAreaAnalysis(Map<String, Object> params);

    /**
     * 川渝地区页岩气
     *
     * @param params
     * @return
     */
    List<BizAnalysisDto> selectListMarketAreaWorktypeCydqYyq(Map<String, Object> params);

    /**
     * 川渝地区非页岩气
     *
     * @param params
     * @return
     */
    List<BizAnalysisDto> selectListMarketAreaWorktypeCydqFyyq(Map<String, Object> params);

    /**
     * 非川渝地区
     *
     * @param params
     * @return
     */
    List<BizAnalysisDto> selectListMarketAreaWorktypeFcydq(Map<String, Object> params);

    List<BizAnalysisDto> selectListMarketAreaWorktype(Map<String, Object> params);

    /**
     * 川渝地区页岩气
     *
     * @param params
     * @return
     */
    List<BizAnalysisDto> selectListMarketAreaServtypeCydqYyq(Map<String, Object> params);

    /**
     * 川渝地区非页岩气
     *
     * @param params
     * @return
     */
    List<BizAnalysisDto> selectListMarketAreaServtypeCydqFyyq(Map<String, Object> params);

    /**
     * 非川渝地区
     *
     * @param params
     * @return
     */
    List<BizAnalysisDto> selectListMarketAreaServtypeFcydq(Map<String, Object> params);

    List<BizAnalysisDto> selectListMarketAreaServtype(Map<String, Object> params);

    /**
     * 业务专业类别1
     *
     * @param params
     * @return
     */
    List<BizAnalysisDto> selectListClasWorkType1(Map<String, Object> params);

    /**
     * 业务专业类别2
     *
     * @param params
     * @return
     */
    List<BizAnalysisDto> selectListClasWorkType2(Map<String, Object> params);

    /**
     * 业务专业类别三级目录
     *
     * @param params
     * @return
     */
    List<BizAnalysisDto> selectListClasServType(Map<String, Object> params);

    public List<String> selectListContractorDept();
}

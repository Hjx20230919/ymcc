package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.domain.prodsys.BizAnalysisDto;
import cn.com.cnpc.cpoa.domain.prodsys.ClientDeptAnalysisDto;
import cn.com.cnpc.cpoa.domain.prodsys.ContractTypeAnalysisDto;
import cn.com.cnpc.cpoa.mapper.prodsys.FusionReportDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author wangjun
 * @create 23/03/2020 22:03
 * @since 1.0.0
 */
@Service
public class FusionReportService {

    @Autowired
    private FusionReportDtoMapper fusionReportDtoMapper;

    /**
     * 报表3：交易类型
     *
     * @param params
     * @return
     */
    public List<ContractTypeAnalysisDto> selectListContractTypeAnalysis(Map<String, Object> params) {
        List<ContractTypeAnalysisDto> retval = new ArrayList<>();
        // 关联+非关联
        List<ContractTypeAnalysisDto> list = fusionReportDtoMapper.selectListContractTypeAnalysisOne(params);
        if (!CollectionUtils.isEmpty(list)) {
            retval.addAll(list);
        }
        // 内责
        list = fusionReportDtoMapper.selectListContractTypeAnalysisNZ(params);
        if (!CollectionUtils.isEmpty(list)) {
            retval.addAll(list);
        }
        // 划拨/指令性
        list = fusionReportDtoMapper.selectListContractTypeAnalysisTwo(params);
        if (!CollectionUtils.isEmpty(list)) {
            retval.addAll(list);
        }
        // 三万元以下
        list = fusionReportDtoMapper.selectListContractTypeAnalysisThree(params);
        if (!CollectionUtils.isEmpty(list)) {
            retval.addAll(list);
        }
        return retval;
    }

    public List<ClientDeptAnalysisDto> selectListClientDeptAnalysis(Map<String, Object> params) {
        List<ClientDeptAnalysisDto> retval = new ArrayList<>();
        List<ClientDeptAnalysisDto> list = fusionReportDtoMapper.selectListClientDeptAnalysis(params);
        if (!CollectionUtils.isEmpty(list)) {
            retval.addAll(list);
        }
        return retval;
    }

    public List<ClientDeptAnalysisDto> selectListClientSubDeptAnalysis(Map<String, Object> params) {
        List<ClientDeptAnalysisDto> retval = new ArrayList<>();
        List<ClientDeptAnalysisDto> list = fusionReportDtoMapper.selectListClientSubDeptAnalysis(params);
        if (!CollectionUtils.isEmpty(list)) {
            retval.addAll(list);
        }
        return retval;
    }

    public List<BizAnalysisDto> selectListMarketAreaAnalysis(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListMarketAreaAnalysis(params);
    }

    /**
     * 川渝地区页岩气
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizAnalysisDto> selectListMarketAreaWorktypeCydqYyq(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListMarketAreaWorktypeCydqYyq(params);
    }

    /**
     * 川渝地区非页岩气
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizAnalysisDto> selectListMarketAreaWorktypeCydqFyyq(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListMarketAreaWorktypeCydqFyyq(params);
    }

    /**
     * 非川渝地区
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizAnalysisDto> selectListMarketAreaWorktypeFcydq(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListMarketAreaWorktypeFcydq(params);
    }

    public List<BizAnalysisDto> selectListMarketAreaWorktype(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListMarketAreaWorktype(params);
    }

    /**
     * 川渝地区页岩气
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizAnalysisDto> selectListMarketAreaServtypeCydqYyq(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListMarketAreaServtypeCydqYyq(params);
    }

    /**
     * 川渝地区非页岩气
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizAnalysisDto> selectListMarketAreaServtypeCydqFyyq(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListMarketAreaServtypeCydqFyyq(params);
    }

    /**
     * 非川渝地区
     *
     * @param params
     * @return
     */
    @Deprecated
    public List<BizAnalysisDto> selectListMarketAreaServtypeFcydq(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListMarketAreaServtypeFcydq(params);
    }

    public List<BizAnalysisDto> selectListMarketAreaServtype(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListMarketAreaServtype(params);
    }

    /**
     *
     * @param params
     * @return
     */
    public List<BizAnalysisDto> selectListClasWorkType1(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListClasWorkType1(params);
    }

    /**
     *
     * @param params
     * @return
     */
    public List<BizAnalysisDto> selectListClasWorkType2(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListClasWorkType2(params);
    }

    /**
     *
     * @param params
     * @return
     */
    public List<BizAnalysisDto> selectListClasServType(Map<String, Object> params) {
        return fusionReportDtoMapper.selectListClasServType(params);
    }

    public List<String> selectListContractorDept() {
        return fusionReportDtoMapper.selectListContractorDept();
    }
}

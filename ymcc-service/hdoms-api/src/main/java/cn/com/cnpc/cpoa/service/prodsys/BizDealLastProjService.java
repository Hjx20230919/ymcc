/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/8/8 10:49
 */
package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectSumDataDto;
import cn.com.cnpc.cpoa.mapper.prodsys.BizDealLastProjectDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <bizProj的历史数据，取代proj-sumdata的部分功能>
 *
 * @author wangjun
 * @create 2020/8/8 10:49
 * @since 1.0.0
 */
@Service
public class BizDealLastProjService extends AppService<BizProjectSumDataDto> {

    @Autowired
    private BizDealLastProjectDtoMapper bizDealLastProjectDtoMapper;

    public List<BizProjectSumDataDto> selectListContractTypeAnalysis(Map<String, Object> params) {
        List<BizProjectSumDataDto> result = new ArrayList<>();
        // 关联+非关联
        List<BizProjectSumDataDto> list = bizDealLastProjectDtoMapper.selectListContractTypeAnalysisOne(params);
        if (list != null && !list.isEmpty()) {
            result.addAll(list);
        }
        // 内责
        list = bizDealLastProjectDtoMapper.selectListContractTypeAnalysisNZ(params);
        if (list != null && !list.isEmpty()) {
            result.addAll(list);
        }
        // 划拨/指令性
        list = bizDealLastProjectDtoMapper.selectListContractTypeAnalysisTwo(params);
        if (list != null && !list.isEmpty()) {
            result.addAll(list);
        }
        // 三万元以下
        list = bizDealLastProjectDtoMapper.selectListContractTypeAnalysisThree(params);
        if (list != null && !list.isEmpty()) {
            result.addAll(list);
        }
        return result;
    }

    public List<BizProjectSumDataDto> selectListClientDeptAnalysis(Map<String, Object> params) {
        return bizDealLastProjectDtoMapper.selectListClientDeptAnalysis(params);
    }

    public List<BizProjectSumDataDto> selectListClientSubDeptAnalysis(Map<String, Object> params) {
        return bizDealLastProjectDtoMapper.selectListClientSubDeptAnalysis(params);
    }

    public List<BizProjectSumDataDto> selectListMarketAreaAnalysis(Map<String, Object> params) {
        return bizDealLastProjectDtoMapper.selectListMarketAreaAnalysis(params);
    }

    public List<BizProjectSumDataDto> selectListMarketAreaWorktype(Map<String, Object> params) {
        return bizDealLastProjectDtoMapper.selectListMarketAreaWorktype(params);
    }

    public List<BizProjectSumDataDto> selectListMarketAreaServtype(Map<String, Object> params) {
        return bizDealLastProjectDtoMapper.selectListMarketAreaServtype(params);
    }

    public List<BizProjectSumDataDto> selectListClasWorkType1(Map<String, Object> params) {
        return bizDealLastProjectDtoMapper.selectListClasWorkType1(params);
    }

    public List<BizProjectSumDataDto> selectListClasWorkType2(Map<String, Object> params) {
        return bizDealLastProjectDtoMapper.selectListClasWorkType2(params);
    }

    public List<BizProjectSumDataDto> selectListClasServType(Map<String, Object> params) {
        return bizDealLastProjectDtoMapper.selectListClasServType(params);
    }

    public String sumMonthly(Map<String, Object> params) {
        return bizDealLastProjectDtoMapper.sumMonthly(params);
    }
}

package cn.com.cnpc.cpoa.service.prodsys.report;

import cn.com.cnpc.cpoa.mapper.prodsys.FusionReportV2DtoMapper;
import cn.com.cnpc.cpoa.vo.prodsys.report.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 取代FusionReportService
 * <p>基于《20201030-报表调整.xlsx》</p>
 *
 * @author: sirjaime
 * @create: 2020-11-01 10:18
 */
@Service
public class FusionReportV2Service {

    public static final String YEAR_MONTH_START = "yearMonthStart";
    public static final String YEAR_MONTH_END = "yearMonthEnd";
    @Autowired
    FusionReportV2DtoMapper fusionReportV2DtoMapper;

    private String getSameMonthOfLastYear(String yearMonth) {
        return (Integer.parseInt(yearMonth.substring(0, 4)) - 1) + yearMonth.substring(4);
    }

    private Map<FusionReportType, FusionReportDataBuilder> builderMap = new HashMap<FusionReportType, FusionReportDataBuilder>() {
        {
            put(FusionReportType.MarketArea, new RptMarketAreaDataBuilder());
            put(FusionReportType.ClasType, new RptClasTypeDataBuilder());
            put(FusionReportType.ContractType, new RptContractTypeDataBuilder());
            put(FusionReportType.ClientDept, new RptClientDeptDataBuilder());
            put(FusionReportType.ClientSubDept, new RptClientSubDeptDataBuilder());
        }
    };

    public List<RptMarketAreaVO> queryRptMarketArea(Map<String, Object> params) {
        Objects.requireNonNull(params, "参数为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_START), "开始时间为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_END), "结束时间为空");

        // query cur data
        Map<String, Object> queryWrapper = new HashMap<>(params);
        List<RptMarketAreaVO> curList = fusionReportV2DtoMapper.queryRptMarketArea(queryWrapper);

        // query last data
        queryWrapper = new HashMap<>();
        queryWrapper.put(YEAR_MONTH_START, getSameMonthOfLastYear(params.get(YEAR_MONTH_START).toString()));
        queryWrapper.put(YEAR_MONTH_END, getSameMonthOfLastYear(params.get(YEAR_MONTH_END).toString()));
        List<RptMarketAreaVO> lastList = fusionReportV2DtoMapper.queryRptMarketAreaLast(queryWrapper);

        List<RptMarketAreaVO> result = builderMap.get(FusionReportType.MarketArea).build(curList, lastList);
        return Optional.ofNullable(result).orElse(new ArrayList<>());
    }

    public List<RptContractTypeVO> queryRptContractType(Map<String, Object> params) {
        Objects.requireNonNull(params, "参数为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_START), "开始时间为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_END), "结束时间为空");

        // query cur data
        Map<String, Object> queryWrapper = new HashMap<>(params);
        List<RptContractTypeVO> curList = fusionReportV2DtoMapper.queryRptContractType(queryWrapper);

        // query last data
        queryWrapper = new HashMap<>();
        queryWrapper.put(YEAR_MONTH_START, getSameMonthOfLastYear(params.get(YEAR_MONTH_START).toString()));
        queryWrapper.put(YEAR_MONTH_END, getSameMonthOfLastYear(params.get(YEAR_MONTH_END).toString()));
        List<RptContractTypeVO> lastList = fusionReportV2DtoMapper.queryRptContractTypeLast(queryWrapper);

        List<RptContractTypeVO> result = builderMap.get(FusionReportType.ContractType).build(curList, lastList);
        return Optional.ofNullable(result).orElse(new ArrayList<>());
    }

    public List<RptClasTypeVO> queryRptClasType(Map<String, Object> params) {
        Objects.requireNonNull(params, "参数为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_START), "开始时间为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_END), "结束时间为空");

        // query cur data
        Map<String, Object> queryWrapper = new HashMap<>(params);
        List<RptClasTypeVO> curList = fusionReportV2DtoMapper.queryRptClasType(queryWrapper);

        // query last data
        queryWrapper = new HashMap<>();
        queryWrapper.put(YEAR_MONTH_START, getSameMonthOfLastYear(params.get(YEAR_MONTH_START).toString()));
        queryWrapper.put(YEAR_MONTH_END, getSameMonthOfLastYear(params.get(YEAR_MONTH_END).toString()));
        List<RptClasTypeVO> lastList = fusionReportV2DtoMapper.queryRptClasTypeLast(queryWrapper);

        List<RptClasTypeVO> result = builderMap.get(FusionReportType.ClasType).build(curList, lastList);
        return Optional.ofNullable(result).orElse(new ArrayList<>());
    }

    public List<RptClientDeptVO> queryRptClientDept(Map<String, Object> params) {
        Objects.requireNonNull(params, "参数为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_START), "开始时间为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_END), "结束时间为空");

        // query cur data
        Map<String, Object> queryWrapper = new HashMap<>(params);
        List<RptClientDeptVO> curList = fusionReportV2DtoMapper.queryRptClientDept(queryWrapper);

        // query last data
        queryWrapper = new HashMap<>();
        queryWrapper.put(YEAR_MONTH_START, getSameMonthOfLastYear(params.get(YEAR_MONTH_START).toString()));
        queryWrapper.put(YEAR_MONTH_END, getSameMonthOfLastYear(params.get(YEAR_MONTH_END).toString()));
        List<RptClientDeptVO> lastList = fusionReportV2DtoMapper.queryRptClientDeptLast(queryWrapper);

        List<RptClientDeptVO> result = builderMap.get(FusionReportType.ClientDept).build(curList, lastList);
        return Optional.ofNullable(result).orElse(new ArrayList<>());
    }

    public List<RptClientSubDeptVO> queryRptClientSubDept(Map<String, Object> params) {
        Objects.requireNonNull(params, "参数为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_START), "开始时间为空");
        Objects.requireNonNull(params.get(YEAR_MONTH_END), "结束时间为空");
        Objects.requireNonNull(params.get("deptName"), "所属单位为空");

        // query cur data
        Map<String, Object> queryWrapper = new HashMap<>(params);
        List<RptClientSubDeptVO> curList = fusionReportV2DtoMapper.queryRptClientSubDept(queryWrapper);

        // query last data
        queryWrapper = new HashMap<>();
        queryWrapper.put(YEAR_MONTH_START, getSameMonthOfLastYear(params.get(YEAR_MONTH_START).toString()));
        queryWrapper.put(YEAR_MONTH_END, getSameMonthOfLastYear(params.get(YEAR_MONTH_END).toString()));
        queryWrapper.put("deptName", params.get("deptName"));
        List<RptClientSubDeptVO> lastList = fusionReportV2DtoMapper.queryRptClientSubDeptLast(queryWrapper);

        List<RptClientSubDeptVO> result = builderMap.get(FusionReportType.ClientSubDept).build(curList, lastList);
        return Optional.ofNullable(result).orElse(new ArrayList<>());
    }
}

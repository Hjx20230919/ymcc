package cn.com.cnpc.cpoa.controller.prodsys;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import cn.com.cnpc.cpoa.domain.prodsys.*;
import cn.com.cnpc.cpoa.enums.prodsys.CompanyTypeEnum;
import cn.com.cnpc.cpoa.enums.prodsys.ContractTypeEnum;
import cn.com.cnpc.cpoa.enums.prodsys.MarketTypeEnum;
import cn.com.cnpc.cpoa.enums.prodsys.WorkZoneEnum;
import cn.com.cnpc.cpoa.service.DeptService;
import cn.com.cnpc.cpoa.service.prodsys.*;
import cn.com.cnpc.cpoa.utils.BigDecimalUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.excel.JxlsUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.prodsys.*;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <>
 *
 * @author wangjun
 * @create 23/03/2020 21:46
 * @since 1.0.0
 */
@RestController
@RequestMapping("/hd/fusion/report")
public class FusionReportController extends BaseController {

    public static final String PARAM_WORK_ZONE = "workZone";
    public static final String PARAM_CONTRACT_TYPE = "contractType";
    public static final String PARAM_COMPANY_TYPE = "companyType";
    public static final String PARAM_MARKET_TYPE = "marketType";
    public static final String PARAM_WORK_ZONE_NOT = "workZoneNot";
    @Autowired
    private FusionReportService fusionReportService;

    @Autowired
    private BizProjectSumDataService projectSumDataService;

    @Autowired
    private BizDeptTaskService bizDeptTaskService;

    @Autowired
    private BizRptLaborScoreService bizRptLaborScoreService;

    @Autowired
    private BizRptMonthService bizRptMonthService;

    @Autowired
    DeptService deptService;

    @Autowired
    BizProjectService bizProjectService;

    @Autowired
    BizProjectSumDataService bizProjectSumDataService;

    @Autowired
    BizDealLastProjService bizDealLastProjService;

    @Value("${file.rptMonthTmplUrl}")
    private String rptMonthTmplUrl;

    @Value("${file.rptLaborScoreTmplUrl}")
    private String rptLaborScoreTmplUrl;

    @Value("${file.rptKpiSumTmplUrl}")
    private String rptKpiSumTmplUrl;

    @Value("${file.tempurl}")
    private String tempurl;

    @RequestMapping(value = "/contractTypeAnalysis", method = RequestMethod.GET)
    public AppMessage contractTypeAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                           @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {
        List<RptContractTypeAnalsisVo> retval = contractTypeAnalsisImpl(yearMonthStart, yearMonthEnd);
        return AppMessage.success(retval, "统计报表成功");
    }

    private List<RptContractTypeAnalsisVo> contractTypeAnalsisImpl(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart, @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        List<ContractTypeAnalysisDto> currentList = fusionReportService.selectListContractTypeAnalysis(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = bizDealLastProjService.selectListContractTypeAnalysis(params);

        List<String> contractTypeList = Arrays.asList("内部责任书", "关联交易", "非关联交易", "划拨/指令性", "三万以下");
        Map<String, ContractTypeAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(ContractTypeAnalysisDto::getContractType, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getContractType, n -> n)) : new HashMap<>();
        return contractTypeList.stream().map(contractType -> {
            RptContractTypeAnalsisVo item = new RptContractTypeAnalsisVo();
            Double lastAmount = lastMap.get(contractType) == null || lastMap.get(contractType).getContractPrice() == null ? 0 : lastMap.get(contractType).getContractPrice();
            Double currentAmount = currMap.get(contractType) == null || currMap.get(contractType).getCurrentAmount() == null ? 0 : currMap.get(contractType).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.setContractType(contractType);
            item.setLastAmount(lastAmount);
            item.setCurrentAmount(currentAmount);
            item.setGrowth(growth);
            item.setGrowthRatio(growthRatio);
            return item;
        }).collect(Collectors.toList());
    }

    @Deprecated
    private List<RptContractTypeAnalsisVo> contractTypeAnalsisImpl20200808(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart, @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        List<ContractTypeAnalysisDto> currentList = fusionReportService.selectListContractTypeAnalysis(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = projectSumDataService.selectListContractTypeAnalysis(params);

        List<String> contractTypeList = Arrays.asList("内部责任书", "关联交易", "非关联交易", "划拨/指令性", "三万元以下");
        Map<String, ContractTypeAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(ContractTypeAnalysisDto::getContractType, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getContractType, n -> n)) : new HashMap<>();
        return contractTypeList.stream().map(contractType -> {
            RptContractTypeAnalsisVo item = new RptContractTypeAnalsisVo();
            Double lastAmount = lastMap.get(contractType) == null || lastMap.get(contractType).getContractPrice() == null ? 0 : lastMap.get(contractType).getContractPrice();
            Double currentAmount = currMap.get(contractType) == null || currMap.get(contractType).getCurrentAmount() == null ? 0 : currMap.get(contractType).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.setContractType(contractType);
            item.setLastAmount(lastAmount);
            item.setCurrentAmount(currentAmount);
            item.setGrowth(growth);
            item.setGrowthRatio(growthRatio);
            return item;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/contractTypeAnalysis/export", method = RequestMethod.GET)
    public AppMessage contractTypeAnalysisExport(HttpServletResponse response,
                                                 @RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                                 @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {
        List<RptContractTypeAnalsisVo> retval = contractTypeAnalsisImpl(yearMonthStart, yearMonthEnd);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<RptContractTypeAnalsisVo> util = new ExcelUtil<RptContractTypeAnalsisVo>(RptContractTypeAnalsisVo.class);
            return util.exportExcelBrowser(response, retval, "交易类型分析");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    private String getSameMonthOfLastYear(String yearMonth) {
        return (Integer.parseInt(yearMonth.substring(0, 4)) - 1) + yearMonth.substring(4);
    }

    @RequestMapping(value = "/clientDeptList", method = RequestMethod.GET)
    public AppMessage clientDeptList() throws Exception {
        List<String> retval = fusionReportService.selectListContractorDept();
        return AppMessage.success(retval, "统计报表成功");
    }

    @RequestMapping(value = "/clientDeptAnalysis", method = RequestMethod.GET)
    public AppMessage clientDeptAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                         @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {
        List<RptClientDeptAnalysisVo> retval = clientDeptAnalysisImpl(yearMonthStart, yearMonthEnd);
        return AppMessage.success(retval, "统计报表成功");
    }

    private List<RptClientDeptAnalysisVo> clientDeptAnalysisImpl(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart, @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        List<ClientDeptAnalysisDto> currentList = fusionReportService.selectListClientDeptAnalysis(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = bizDealLastProjService.selectListClientDeptAnalysis(params);

        List<RptClientDeptAnalysisVo> result = new ArrayList<>();
        // 以当期金额排序，top10以后的以及单位为空的都归类为“其他单位”
        if (CollectionUtils.isNotEmpty(currentList)) {
            // 排除未知单位后排序
            List<ClientDeptAnalysisDto> sortedList = currentList.stream()
                    .filter(data -> !"未知".equals(data.getDeptName()))
                    .sorted(Comparator.comparing(ClientDeptAnalysisDto::getCurrentAmount).reversed())
                    .collect(Collectors.toList());
            // 取top10
            int top = Math.min(10, sortedList.size());
            List<ClientDeptAnalysisDto> topXList = new ArrayList<>();
            for (int i = 0; i < top; i++) {
                topXList.add(sortedList.get(i));
            }
            List<String> topXDeptList = topXList.stream().map(ClientDeptAnalysisDto::getDeptName).distinct().collect(Collectors.toList());
            double otherDeptCurrentAmount = currentList.stream()
                    .filter(data -> !topXDeptList.contains(data.getDeptName()))
                    .mapToDouble(ClientDeptAnalysisDto::getCurrentAmount)
                    .sum();

            Map<String, BizProjectSumDataDto> lastMap = Optional.ofNullable(lastList)
                    .map(ll -> ll.stream().collect(Collectors.toMap(BizProjectSumDataDto::getDeptName, n -> n)))
                    .orElse(new HashMap<>());
            double otherDeptLastAmount = Optional.ofNullable(lastList)
                    .map(ll -> ll.stream()
                            .filter(data -> !topXDeptList.contains(data.getDeptName()))
                            .mapToDouble(BizProjectSumDataDto::getContractPrice)
                            .sum()
                    ).orElse(0d);

            // make topX
            result = topXList.stream().map(currentDto -> {
                RptClientDeptAnalysisVo item = new RptClientDeptAnalysisVo();
                String deptName = currentDto.getDeptName();
                Double lastAmount = lastMap.get(deptName) == null || lastMap.get(deptName).getContractPrice() == null ? 0 : lastMap.get(deptName).getContractPrice();
                Double currentAmount = currentDto.getCurrentAmount() == null ? 0 : currentDto.getCurrentAmount();
                Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
                Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
                item.setDeptName(deptName);
                item.setLastAmount(lastAmount);
                item.setCurrentAmount(currentAmount);
                item.setGrowth(growth);
                item.setGrowthRatio(growthRatio);
                return item;
            }).collect(Collectors.toList());

            // make other
            Double otherGrowth = BigDecimalUtils.subtract(otherDeptCurrentAmount, otherDeptLastAmount);
            Float otherGrowthRatio = otherDeptLastAmount == 0 || otherDeptCurrentAmount == 0 ? 0 : BigDecimalUtils.divide(otherGrowth, otherDeptLastAmount).floatValue();
            RptClientDeptAnalysisVo other = new RptClientDeptAnalysisVo();
            other.setDeptName("其他单位");
            other.setCurrentAmount(otherDeptCurrentAmount);
            other.setLastAmount(otherDeptLastAmount);
            other.setGrowth(otherGrowth);
            other.setGrowthRatio(otherGrowthRatio);

            result.add(other);
        }

        return result;
    }

    @Deprecated
    private List<RptClientDeptAnalysisVo> clientDeptAnalysisImpl20200808(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart, @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        List<ClientDeptAnalysisDto> currentList = fusionReportService.selectListClientDeptAnalysis(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = projectSumDataService.selectListClientDeptAnalysis(params);

        List<String> clientDeptList = Arrays.asList("川庆钻探", "西南油气田", "长庆油田", "塔里木油田", "四川销售", "西南管道", "中海油", "未知");
        Map<String, ClientDeptAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(ClientDeptAnalysisDto::getDeptName, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getDeptName, n -> n)) : new HashMap<>();
        return clientDeptList.stream().map(deptName -> {
            RptClientDeptAnalysisVo item = new RptClientDeptAnalysisVo();
            Double lastAmount = lastMap.get(deptName) == null || lastMap.get(deptName).getContractPrice() == null ? 0 : lastMap.get(deptName).getContractPrice();
            Double currentAmount = currMap.get(deptName) == null || currMap.get(deptName).getCurrentAmount() == null ? 0 : currMap.get(deptName).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.setDeptName(deptName);
            item.setLastAmount(lastAmount);
            item.setCurrentAmount(currentAmount);
            item.setGrowth(growth);
            item.setGrowthRatio(growthRatio);
            return item;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/clientDeptAnalysis/export", method = RequestMethod.GET)
    public AppMessage clientDeptAnalysisExport(HttpServletResponse response,
                                               @RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                               @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {
        List<RptClientDeptAnalysisVo> retval = clientDeptAnalysisImpl(yearMonthStart, yearMonthEnd);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<RptClientDeptAnalysisVo> util = new ExcelUtil<RptClientDeptAnalysisVo>(RptClientDeptAnalysisVo.class);
            return util.exportExcelBrowser(response, retval, "业主单位分析");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    @RequestMapping(value = "/clientSubDeptAnalysis", method = RequestMethod.GET)
    public AppMessage clientSubDeptAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                            @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                            @RequestParam(value = "parentName", defaultValue = "") String parentName) throws Exception {
        List<RptClientSubDeptAnalysisVo> retval = clientSubDeptAnalysisImpl(yearMonthStart, yearMonthEnd, parentName);
        return AppMessage.success(retval, "统计报表成功");
    }

    private List<RptClientSubDeptAnalysisVo> clientSubDeptAnalysisImpl(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart, @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd, @RequestParam(value = "parentName", defaultValue = "") String parentName) {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        params.put("parentName", parentName);
        List<ClientDeptAnalysisDto> currentList = fusionReportService.selectListClientSubDeptAnalysis(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        params.put("parentName", parentName);
        List<BizProjectSumDataDto> lastList = bizDealLastProjService.selectListClientSubDeptAnalysis(params);

        List<RptClientSubDeptAnalysisVo> result = new ArrayList<>();
        // 以当期金额排序，top10以后的以及单位为空的都归类为“其他单位”
        if (CollectionUtils.isNotEmpty(currentList)) {
            // 排除未知二级单位后排序
            List<ClientDeptAnalysisDto> sortedList = currentList.stream()
                    .filter(data -> !"未知".equals(data.getSubDeptName()))
                    .sorted(Comparator.comparing(ClientDeptAnalysisDto::getCurrentAmount).reversed())
                    .collect(Collectors.toList());
            // 取top10
            int top = Math.min(10, sortedList.size());
            List<ClientDeptAnalysisDto> topXList = new ArrayList<>();
            for (int i = 0; i < top; i++) {
                topXList.add(sortedList.get(i));
            }
            List<String> topXDeptList = topXList.stream().map(ClientDeptAnalysisDto::getSubDeptName).distinct().collect(Collectors.toList());
            double otherDeptCurrentAmount = currentList.stream()
                    .filter(data -> !topXDeptList.contains(data.getSubDeptName()))
                    .mapToDouble(ClientDeptAnalysisDto::getCurrentAmount)
                    .sum();

            Map<String, BizProjectSumDataDto> lastMap = Optional.ofNullable(lastList)
                    .map(ll -> ll.stream().collect(Collectors.toMap(BizProjectSumDataDto::getSubDeptName, n -> n)))
                    .orElse(new HashMap<>());
            double otherDeptLastAmount = Optional.ofNullable(lastList)
                    .map(ll -> ll.stream()
                            .filter(data -> !topXDeptList.contains(data.getSubDeptName()))
                            .mapToDouble(BizProjectSumDataDto::getContractPrice)
                            .sum()
                    ).orElse(0d);

            // make topX
            result = topXList.stream().map(currentDto -> {
                RptClientSubDeptAnalysisVo item = new RptClientSubDeptAnalysisVo();
                String subDeptName = currentDto.getSubDeptName();
                Double lastAmount = lastMap.get(subDeptName) == null || lastMap.get(subDeptName).getContractPrice() == null ? 0 : lastMap.get(subDeptName).getContractPrice();
                Double currentAmount = currentDto.getCurrentAmount() == null ? 0 : currentDto.getCurrentAmount();
                Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
                Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
                item.setDeptName(currentDto.getDeptName());
                item.setSubDeptName(subDeptName);
                item.setLastAmount(lastAmount);
                item.setCurrentAmount(currentAmount);
                item.setGrowth(growth);
                item.setGrowthRatio(growthRatio);
                return item;
            }).collect(Collectors.toList());

            // make other
            Double otherGrowth = BigDecimalUtils.subtract(otherDeptCurrentAmount, otherDeptLastAmount);
            Float otherGrowthRatio = otherDeptLastAmount == 0 || otherDeptCurrentAmount == 0 ? 0 : BigDecimalUtils.divide(otherGrowth, otherDeptLastAmount).floatValue();
            RptClientSubDeptAnalysisVo other = new RptClientSubDeptAnalysisVo();
            other.setDeptName(parentName);
            other.setSubDeptName("其他单位");
            other.setCurrentAmount(otherDeptCurrentAmount);
            other.setLastAmount(otherDeptLastAmount);
            other.setGrowth(otherGrowth);
            other.setGrowthRatio(otherGrowthRatio);

            result.add(other);
        }

        return result;
    }

    @Deprecated
    private List<RptClientSubDeptAnalysisVo> clientSubDeptAnalysisImpl20200808(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart, @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd, @RequestParam(value = "parentName", defaultValue = "") String parentName) {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        params.put("parentName", parentName);
        List<ClientDeptAnalysisDto> currentList = fusionReportService.selectListClientSubDeptAnalysis(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        params.put("parentName", parentName);
        List<BizProjectSumDataDto> lastList = projectSumDataService.selectListClientSubDeptAnalysis(params);

//        List<String> clientDeptList = Arrays.asList("川庆钻探", "西南油气田", "长庆油田", "塔里木油田", "四川销售", "西南管道", "中海油", "未知");
//        Map<String, ClientDeptAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(ClientDeptAnalysisDto::getDeptName, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getSubDeptName, n -> n)) : new HashMap<>();
        return currentList.stream().map(currDto -> {
            String subDeptName = currDto.getSubDeptName();
            RptClientSubDeptAnalysisVo item = new RptClientSubDeptAnalysisVo();
            Double lastAmount = lastMap.get(subDeptName) == null || lastMap.get(subDeptName).getContractPrice() == null ? 0 : lastMap.get(subDeptName).getContractPrice();
            Double currentAmount = currDto.getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.setDeptName(currDto.getDeptName());
            item.setSubDeptName(subDeptName);
            item.setLastAmount(lastAmount);
            item.setCurrentAmount(currentAmount);
            item.setGrowth(growth);
            item.setGrowthRatio(growthRatio);
            return item;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/clientSubDeptAnalysis/export", method = RequestMethod.GET)
    public AppMessage clientSubDeptAnalysisExport(HttpServletResponse response,
                                                  @RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                                  @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                                  @RequestParam(value = "parentName", defaultValue = "") String parentName) throws Exception {
        List<RptClientSubDeptAnalysisVo> retval = clientSubDeptAnalysisImpl(yearMonthStart, yearMonthEnd, parentName);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<RptClientSubDeptAnalysisVo> util = new ExcelUtil<RptClientSubDeptAnalysisVo>(RptClientSubDeptAnalysisVo.class);
            return util.exportExcelBrowser(response, retval, "业主单位内部分析");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    @RequestMapping(value = "/bizProjSumData", method = RequestMethod.GET)
    public AppMessage queryBizProjSumData(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "yearMonth", defaultValue = "") String yearMonth,
            @RequestParam(value = "deptId", defaultValue = "") String deptId,
            @RequestParam(value = "clientId", defaultValue = "") String clientId,
            @RequestParam(value = "marketType", defaultValue = "") String marketType,
            @RequestParam(value = "companyType", defaultValue = "") String companyType,
            @RequestParam(value = "contractType", defaultValue = "") String contractType,
            @RequestParam(value = "workZone", defaultValue = "") String workZone,
            @RequestParam(value = "workType", defaultValue = "") String workType
    ) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonth", yearMonth);
        params.put("deptId", deptId);
        params.put("clientId", clientId);
        params.put("marketType", marketType);
        params.put("companyType", companyType);
        params.put("contractType", contractType);
        params.put("workZone", workZone);
        params.put("workType", workType);

        PageHelper.startPage(pageNo, pageSize);
        List<BizProjectSumDataDto> result = new ArrayList<>();
        result = projectSumDataService.selectList(params);
        TableDataInfo dataTable = getDataTable(result);
        return AppMessage.success(dataTable, "查询成功");
    }

    @RequestMapping(value = "/indicator/deptTask", method = RequestMethod.GET)
    public AppMessage queryBizDeptTask(
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(value = "yearMonth", defaultValue = "") String yearMonth,
            @RequestParam(value = "deptId", defaultValue = "") String deptId,
            @RequestParam(value = "clientId", defaultValue = "") String clientId,
            @RequestParam(value = "marketType", defaultValue = "") String marketType,
            @RequestParam(value = "companyType", defaultValue = "") String companyType,
            @RequestParam(value = "contractType", defaultValue = "") String contractType,
            @RequestParam(value = "workZone", defaultValue = "") String workZone,
            @RequestParam(value = "workType", defaultValue = "") String workType
    ) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonth", yearMonth);
        params.put("deptId", deptId);
        params.put("clientId", clientId);
        params.put("marketType", marketType);
        params.put("companyType", companyType);
        params.put("contractType", contractType);
        params.put("workZone", workZone);
        params.put("workType", workType);

        PageHelper.startPage(pageNo, pageSize);
        List<BizDeptTaskDto> result = new ArrayList<>();
        result = bizDeptTaskService.selectList(params);
        TableDataInfo dataTable = getDataTable(result);
        return AppMessage.success(dataTable, "查询成功");
    }

    @Log(logContent = "新增年度指标", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/indicator/deptTask", method = RequestMethod.POST)
    public AppMessage addDeptTask(@RequestBody BizDeptTaskDto vo) throws Exception {
        BizDeptTaskDto dto = new BizDeptTaskDto();
        dto.setDeptTaskId(StringUtils.getUuid32());
        dto.setYearMonth(vo.getYearMonth());
        dto.setDeptId(vo.getDeptId());
        dto.setMarketType(vo.getMarketType());
        dto.setCompanyType(vo.getCompanyType());
        dto.setContractType(vo.getContractType());
        dto.setWorkZone(vo.getWorkZone());
        dto.setWorkType(vo.getWorkType());
        dto.setBizTypeId(vo.getBizTypeId());
        dto.setTaskPrice(vo.getTaskPrice());
        dto.setNotes(vo.getNotes());
        if (1 != bizDeptTaskService.save(dto)) {
            return AppMessage.error("保存指标出错");
        }
        return AppMessage.success(dto.getDeptTaskId(), "保存指标成功");
    }

    @Log(logContent = "修改年度指标", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/indicator/deptTask/{id}", method = RequestMethod.PUT)
    public AppMessage updateDeptTask(@PathVariable String id, @RequestBody BizDeptTaskDto vo) throws Exception {
        BizDeptTaskDto indicator = bizDeptTaskService.selectByKey(id);
        if (null == indicator) {
            throw new AppException("当前指标不存在");
        }
        BizDeptTaskDto dto = new BizDeptTaskDto();
        dto.setDeptTaskId(id);
        dto.setYearMonth(vo.getYearMonth());
        dto.setDeptId(vo.getDeptId());
        dto.setMarketType(vo.getMarketType());
        dto.setCompanyType(vo.getCompanyType());
        dto.setContractType(vo.getContractType());
        dto.setWorkZone(vo.getWorkZone());
        dto.setWorkType(vo.getWorkType());
        dto.setBizTypeId(vo.getBizTypeId());
        dto.setTaskPrice(vo.getTaskPrice());
        dto.setNotes(vo.getNotes());
        if (1 != bizDeptTaskService.updateNotNull(dto)) {
            return AppMessage.error("保存指标出错");
        }
        return AppMessage.success(dto.getDeptTaskId(), "保存指标成功");
    }

    @Log(logContent = "删除指标", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/indicator/deptTask/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteDeptTask(@PathVariable String id) {
        BizDeptTaskDto indicator = bizDeptTaskService.selectByKey(id);
        if (null == indicator) {
            throw new AppException("当前指标不存在");
        }
        if (1 != bizDeptTaskService.delete(id)) {
            return AppMessage.success(id, "删除指标成功");
        }
        return AppMessage.errorObjId(id, "删除指标失败");
    }

    @RequestMapping(value = "/indicator/laborScore", method = RequestMethod.GET)
    public AppMessage queryLaborScore(
            @RequestParam(value = "quarter", defaultValue = "") String quarter
    ) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("quarter", quarter);

        List<BizRptLaborScoreDto> result = new ArrayList<>();
        result = bizRptLaborScoreService.selectList(params);
        return AppMessage.success(result, "查询成功");
    }

    @RequestMapping(value = "/indicator/laborScoreSeasons", method = RequestMethod.GET)
    public AppMessage queryLaborScoreSeasons() throws Exception {
        List<String> result = bizRptLaborScoreService.queryLaborScoreSeasons();
        return AppMessage.success(result, "查询成功");
    }

    @Log(logContent = "新增劳动竞赛得分", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/indicator/laborScore", method = RequestMethod.POST)
    public AppMessage addLaborScore(@RequestBody BizRptLaborScoreDto vo) throws Exception {
        BizRptLaborScoreDto dto = new BizRptLaborScoreDto();
        dto.setScoreId(StringUtils.getUuid32());
        dto.setCreateAt(new Date());
        dto.setDeptId(vo.getDeptId());
//        dto.setEndAt(vo.getEndAt());
        dto.setJbCbsyj(vo.getJbCbsyj());
        dto.setJbGkmyd(vo.getJbGkmyd());
        dto.setJbHj(vo.getJbHj());
        dto.setJbSckf(vo.getJbSckf());
        dto.setJbXqht(vo.getJbXqht());
        dto.setJfCbsyj(vo.getJfCbsyj());
        dto.setJfCxsc(vo.getJfCxsc());
        dto.setJfDbzd(vo.getJfDbzd());
        dto.setJfGkmyd(vo.getJfGkmyd());
        dto.setJfHj(vo.getJfHj());
        dto.setJfKhts(vo.getJfKhts());
        dto.setJfXqht(vo.getJfXqht());
        dto.setQuarter(vo.getQuarter());
//        dto.setStartAt(vo.getStartAt());
        dto.setZfHj(vo.getZfHj());

        if (1 != bizRptLaborScoreService.save(dto)) {
            return AppMessage.error("保存劳动竞赛得分出错");
        }
        return AppMessage.success(dto.getScoreId(), "保存劳动竞赛得分成功");
    }

    @Log(logContent = "修改劳动竞赛得分", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/indicator/laborScore/{id}", method = RequestMethod.PUT)
    public AppMessage updateLaborScore(@PathVariable String id, @RequestBody BizRptLaborScoreDto vo) throws Exception {
        BizRptLaborScoreDto laborScore = bizRptLaborScoreService.selectByKey(id);
        if (null == laborScore) {
            throw new AppException("当前劳动竞赛得分不存在");
        }
        BizRptLaborScoreDto dto = new BizRptLaborScoreDto();
        dto.setScoreId(id);
        dto.setCreateAt(vo.getCreateAt());
        dto.setDeptId(vo.getDeptId());
//        dto.setEndAt(vo.getEndAt());
        dto.setJbCbsyj(vo.getJbCbsyj());
        dto.setJbGkmyd(vo.getJbGkmyd());
        dto.setJbHj(vo.getJbHj());
        dto.setJbSckf(vo.getJbSckf());
        dto.setJbXqht(vo.getJbXqht());
        dto.setJfCbsyj(vo.getJfCbsyj());
        dto.setJfCxsc(vo.getJfCxsc());
        dto.setJfDbzd(vo.getJfDbzd());
        dto.setJfGkmyd(vo.getJfGkmyd());
        dto.setJfHj(vo.getJfHj());
        dto.setJfKhts(vo.getJfKhts());
        dto.setJfXqht(vo.getJfXqht());
        dto.setQuarter(vo.getQuarter());
//        dto.setStartAt(vo.getStartAt());
        dto.setZfHj(vo.getZfHj());

        if (1 != bizRptLaborScoreService.updateNotNull(dto)) {
            return AppMessage.error("保存劳动竞赛得分出错");
        }
        return AppMessage.success(dto.getScoreId(), "保存劳动竞赛得分成功");
    }

    @RequestMapping(value = "/indicator/laborScore/init/{quarter}", method = RequestMethod.GET)
    public AppMessage initLaborScore(@PathVariable String quarter) throws Exception {
        List<SysDeptDto> sysDeptDtos = deptService.selectList4LaborScore();

        Map<String, Object> params = new HashMap<>();
        String[] qs = quarter.split("Q");
        String year = qs[0];
        String season = qs[1];
        params.put("year", year);
        params.put("season", season);

        List<BizProjectDto> xqList = bizProjectService.sumBySeason(params);
        List<BizProjectDto> xqTop3List = bizProjectService.top3BySeason(params);
        List<BizProjectDto> newMarketList = bizProjectService.newMarketBySeason(params);
        List<BizDeptTaskDto> kpiList = bizDeptTaskService.sumBySeason(params);

        Map<String, BizProjectDto> xqMap = xqList == null || xqList.isEmpty() ? new HashMap<>() : xqList.stream().collect(Collectors.toMap(BizProjectDto::getDealContractId, n -> n));
//        Map<String, List<BizProjectDto>> xqTop3Map = xqTop3List == null || xqTop3List.isEmpty() ? new HashMap<>() : xqTop3List.stream().collect(Collectors.groupingBy(BizProjectDto::getDealContractId));
        Map<String, BizProjectDto> newMarketMap = newMarketList == null || newMarketList.isEmpty() ? new HashMap<>() : newMarketList.stream().collect(Collectors.toMap(BizProjectDto::getDealContractId, n -> n));
        Map<String, BizDeptTaskDto> kpiMap = kpiList == null || kpiList.isEmpty() ? new HashMap<>() : kpiList.stream().collect(Collectors.toMap(BizDeptTaskDto::getDeptId, n -> n));

        List<BizRptLaborScoreDto> result = new ArrayList<>();
        for (SysDeptDto deptDto : sysDeptDtos) {
            String deptId = deptDto.getDeptId();
            BizDeptTaskDto kpiDto = kpiMap.get(deptId);
            BizProjectDto xqDto = xqMap.get(deptId);
            BizProjectDto newMarketDto = newMarketMap.get(deptId);

            BizRptLaborScoreDto item = new BizRptLaborScoreDto();
            item.setDeptId(deptId);
            item.setDeptName(deptDto.getDeptName());
            item.setQuarter(quarter);
            item.setCreateAt(new Date());
            item.setJbSckf(0f);
            item.setJbGkmyd(0f);
            item.setJbCbsyj(0f);
            item.setJbHj(0f);
            item.setJfCxsc(0f);
            item.setJfDbzd(0f);
            item.setJfKhts(0f);
            item.setJfHj(0f);
            item.setJfGkmyd(0f);
            item.setJfCbsyj(0f);
            item.setZfHj(0f);

            // 完成新签合同量指标得基本分75分。
            // 未完成新签合同量指标的，新签合同量得分=季度实际新签合同量/季度新签合同量指标×75。新签合同包括合同、内责书、指令性任务和划拨费用。
            // 合同以经营管理系统数据为准，内责书以到企管部经营管理系统数据为准，指令性任务以生产运行管理系统实物工作量完成百分比测算，划拨费用以财务数据为准。
            // 同时，2019年签订但尚未开展业务，2020年才开始开展业务也计入新签合同。
            float jbXqht = 0f;
            // 不管新签是否有值，kpi是否录入，均填0，方便核对
            if (kpiDto != null && xqDto != null) {
                if (xqDto.getContractPrice() >= kpiDto.getTaskPrice())
                    jbXqht = 75f;
                else {
                    jbXqht = BigDecimalUtils.multiply(BigDecimalUtils.divide(xqDto.getContractPrice(), kpiDto.getTaskPrice()), 75d).floatValue();
                }
            }
            item.setJbXqht(jbXqht);

            // 新签合同超额完成量指标：新签合同超额完成量得分=（季度实际新签合同量-季度新签合同量指标）/季度新签合同量指标×15。该项最高得分为15分。
            float jfXqht = 0f;
            // 不管新签是否有值，kpi是否录入，均填0，方便核对
            if (kpiDto != null && xqDto != null) {
                jfXqht = BigDecimalUtils.multiply(BigDecimalUtils.divide(BigDecimalUtils.subtract(xqDto.getContractPrice(), kpiDto.getTaskPrice()), kpiDto.getTaskPrice()), 15).floatValue();
                if (jfXqht > 15f)
                    jfXqht = 15f;
            }
            item.setJfXqht(jfXqht);

            // 新签单笔大额合同量指标：最高的前三笔分别得分5分、3分和2分。
            float jfDbzd = 0f;
            if (xqTop3List != null && !xqTop3List.isEmpty()) {
                for (int i = 0, ln = xqTop3List.size(); i < ln; i++) {
                    if (xqTop3List.get(i).getDealContractId().equals(deptId)) {
                        jfDbzd += (i == 0 ? 5 : i == 1 ? 3 : 2);
                    }
                }
            }
            item.setJfDbzd(jfDbzd);

            // 创新市场合同量指标：首次进入新区域、新用户、新业务类型的市场，每个合同加2分。
            float jfCxsc = 0f;
            if (newMarketDto != null) {
                // newMarketDto.getContractPrice() 是合同数量
                jfCxsc = BigDecimalUtils.multiply(newMarketDto.getContractPrice(), 2f).floatValue();
            }
            item.setJfCxsc(jfCxsc);

            result.add(item);
        }

        return AppMessage.success(result, "初始化成功");
    }

    @Deprecated
    public AppMessage initLaborScore20200808(@PathVariable String quarter) throws Exception {
        List<SysDeptDto> sysDeptDtos = deptService.selectList4LaborScore();

        Map<String, Object> params = new HashMap<>();
        String[] qs = quarter.split("Q");
        String year = qs[0];
        String season = qs[1];
        params.put("year", year);
        params.put("season", season);

        List<BizProjectDto> xqList = bizProjectService.sumBySeason(params);
        List<BizProjectDto> xqTop3List = bizProjectService.top3BySeason(params);
        List<BizProjectDto> newMarketList = bizProjectService.newMarketBySeason(params);
        List<BizDeptTaskDto> kpiList = bizDeptTaskService.sumBySeason(params);

        Map<String, BizProjectDto> xqMap = xqList == null || xqList.isEmpty() ? new HashMap<>() : xqList.stream().collect(Collectors.toMap(BizProjectDto::getDealContractId, n -> n));
//        Map<String, List<BizProjectDto>> xqTop3Map = xqTop3List == null || xqTop3List.isEmpty() ? new HashMap<>() : xqTop3List.stream().collect(Collectors.groupingBy(BizProjectDto::getDealContractId));
        Map<String, BizProjectDto> newMarketMap = newMarketList == null || newMarketList.isEmpty() ? new HashMap<>() : newMarketList.stream().collect(Collectors.toMap(BizProjectDto::getDealContractId, n -> n));
        Map<String, BizDeptTaskDto> kpiMap = kpiList == null || kpiList.isEmpty() ? new HashMap<>() : kpiList.stream().collect(Collectors.toMap(BizDeptTaskDto::getDeptId, n -> n));

        List<BizRptLaborScoreDto> result = new ArrayList<>();
        for (SysDeptDto deptDto : sysDeptDtos) {
            String deptId = deptDto.getDeptId();
            BizDeptTaskDto kpiDto = kpiMap.get(deptId);
            BizProjectDto xqDto = xqMap.get(deptId);
            BizProjectDto newMarketDto = newMarketMap.get(deptId);

            BizRptLaborScoreDto item = new BizRptLaborScoreDto();
            item.setDeptId(deptId);
            item.setDeptName(deptDto.getDeptName());
            item.setQuarter(quarter);
            item.setCreateAt(new Date());
            item.setJbSckf(0f);
            item.setJbGkmyd(0f);
            item.setJbCbsyj(0f);
            item.setJbHj(0f);
            item.setJfCxsc(0f);
            item.setJfDbzd(0f);
            item.setJfKhts(0f);
            item.setJfHj(0f);
            item.setJfGkmyd(0f);
            item.setJfCbsyj(0f);
            item.setZfHj(0f);

            // 完成新签合同量指标得基本分75分。
            // 未完成新签合同量指标的，新签合同量得分=季度实际新签合同量/季度新签合同量指标×75。新签合同包括合同、内责书、指令性任务和划拨费用。
            // 合同以经营管理系统数据为准，内责书以到企管部经营管理系统数据为准，指令性任务以生产运行管理系统实物工作量完成百分比测算，划拨费用以财务数据为准。
            // 同时，2019年签订但尚未开展业务，2020年才开始开展业务也计入新签合同。
            float jbXqht = 0f;
            // 不管新签是否有值，kpi是否录入，均填0，方便核对
            if (kpiDto != null && xqDto != null) {
                if (xqDto.getContractPrice() >= kpiDto.getTaskPrice())
                    jbXqht = 75f;
                else {
                    jbXqht = BigDecimalUtils.multiply(BigDecimalUtils.divide(xqDto.getContractPrice(), kpiDto.getTaskPrice()), 75d).floatValue();
                }
            }
            item.setJbXqht(jbXqht);

            // 新签合同超额完成量指标：新签合同超额完成量得分=（季度实际新签合同量-季度新签合同量指标）/季度新签合同量指标×15。该项最高得分为15分。
            float jfXqht = 0f;
            // 不管新签是否有值，kpi是否录入，均填0，方便核对
            if (kpiDto != null && xqDto != null) {
                jfXqht = BigDecimalUtils.multiply(BigDecimalUtils.divide(BigDecimalUtils.subtract(xqDto.getContractPrice(), kpiDto.getTaskPrice()), kpiDto.getTaskPrice()), 15).floatValue();
                if (jfXqht > 15f)
                    jfXqht = 15f;
            }
            item.setJfXqht(jfXqht);

            // 新签单笔大额合同量指标：最高的前三笔分别得分5分、3分和2分。
            float jfDbzd = 0f;
            if (xqTop3List != null && !xqTop3List.isEmpty()) {
                for (int i = 0, ln = xqTop3List.size(); i < ln; i++) {
                    if (xqTop3List.get(i).getDealContractId().equals(deptId)) {
                        jfDbzd += (i == 0 ? 5 : i == 1 ? 3 : 2);
                    }
                }
            }
            item.setJfDbzd(jfDbzd);

            // 创新市场合同量指标：首次进入新区域、新用户、新业务类型的市场，每个合同加2分。
            float jfCxsc = 0f;
            if (newMarketDto != null) {
                // newMarketDto.getContractPrice() 是合同数量
                jfCxsc = BigDecimalUtils.multiply(newMarketDto.getContractPrice(), 2f).floatValue();
            }
            item.setJfCxsc(jfCxsc);

            result.add(item);
        }

        return AppMessage.success(result, "初始化成功");
    }

    @Log(logContent = "保存劳动竞赛得分", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/indicator/laborScore/save", method = RequestMethod.PUT)
    public AppMessage saveLaborScore(@RequestBody LaborScoreParamVo vo) throws Exception {
        List<BizRptLaborScoreDto> dtos = vo.getLaborScoreDtos();

        if (dtos == null || dtos.isEmpty() || StringUtils.isEmpty(dtos.get(0).getQuarter())) {
            return AppMessage.error("无效的保存操作，保存请求数据为空");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("quarter", dtos.get(0).getQuarter());
        List<BizRptLaborScoreDto> oldScoreList = bizRptLaborScoreService.selectList(params);

        BizRptLaborScoreDto score = null;
        BizRptLaborScoreDto old = null;
        for (BizRptLaborScoreDto dto : dtos) {
            score = new BizRptLaborScoreDto();
            score.setDeptId(dto.getDeptId());
            score.setDeptName(dto.getDeptName());
            score.setQuarter(dto.getQuarter());
            score.setCreateAt(dto.getCreateAt());
            score.setJbHj(dto.getJbHj());
            score.setJbCbsyj(dto.getJbCbsyj());
            score.setJbGkmyd(dto.getJbGkmyd());
            score.setJbSckf(dto.getJbSckf());
            score.setJbXqht(dto.getJbXqht());
            score.setJfCbsyj(dto.getJfCbsyj());
            score.setJfGkmyd(dto.getJfGkmyd());
            score.setJfHj(dto.getJfHj());
            score.setJfKhts(dto.getJfKhts());
            score.setJfDbzd(dto.getJfDbzd());
            score.setJfCxsc(dto.getJfCxsc());
            score.setJfXqht(dto.getJfXqht());
            score.setZfHj(dto.getZfHj());

            if (oldScoreList != null && !oldScoreList.isEmpty()) {
                old = oldScoreList.stream().filter(item -> item.getDeptId().equals(dto.getDeptId())).findFirst().orElse(null);
            }
            if (old == null) {
                score.setScoreId(StringUtils.getUuid32());
                bizRptLaborScoreService.save(score);
            } else {
                score.setScoreId(old.getScoreId());
                bizRptLaborScoreService.updateNotNull(score);
            }
        }
        return AppMessage.success(Collections.EMPTY_LIST, "保存劳动竞赛得分成功");
    }

    @RequestMapping(value = "/indicator/kpi-sum", method = RequestMethod.GET)
    public AppMessage queryYearlyKpiSum(@RequestParam(value = "dateStart", defaultValue = "") String dateStart,
                                        @RequestParam(value = "dateEnd", defaultValue = "") String dateEnd,
                                        @RequestParam(value = "kpiYear", defaultValue = "") String kpiYear) throws Exception {

        List<BizProjectYearlySumVo> result = new ArrayList<>();
        result = queryYearlyKpiSumImpl(dateStart, dateEnd, kpiYear);

        return AppMessage.success(result, "查询成功");
    }

    private List<BizProjectYearlySumVo> queryYearlyKpiSumImpl(String dateStart, String dateEnd, String kpiYear) {
        List<SysDeptDto> sysDeptDtos = deptService.selectList4LaborScore();

        Map<String, Object> params = new HashMap<>();
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        params.put("year", kpiYear);
        List<BizProjectYearlySumVo> projList = bizProjectService.sumByContractType(params);

        params.put("season", 1);
        List<BizProjectDto> s1ProjList = bizProjectService.sumBySeason(params);
        params.put("season", 2);
        List<BizProjectDto> s2ProjList = bizProjectService.sumBySeason(params);
        params.put("season", 3);
        List<BizProjectDto> s3ProjList = bizProjectService.sumBySeason(params);
        params.put("season", 4);
        List<BizProjectDto> s4ProjList = bizProjectService.sumBySeason(params);

        params = new HashMap<>();
        params.put("year", kpiYear);
        List<BizDeptTaskDto> yearKpiList = bizDeptTaskService.sumBySeason(params);

        params = new HashMap<>();
        params.put("year", kpiYear);
        params.put("season", 1);
        List<BizDeptTaskDto> s1List = bizDeptTaskService.sumBySeason(params);
        params.put("season", 2);
        List<BizDeptTaskDto> s2List = bizDeptTaskService.sumBySeason(params);
        params.put("season", 3);
        List<BizDeptTaskDto> s3List = bizDeptTaskService.sumBySeason(params);
        params.put("season", 4);
        List<BizDeptTaskDto> s4List = bizDeptTaskService.sumBySeason(params);

        Map<String, BizProjectYearlySumVo> projMap = projList == null || projList.isEmpty() ? Collections.EMPTY_MAP : projList.stream().collect(Collectors.toMap(BizProjectYearlySumVo::getDeptId, n -> n));
        Map<String, BizProjectDto> s1ProjMap = s1ProjList == null || s1ProjList.isEmpty() ? Collections.EMPTY_MAP : s1ProjList.stream().collect(Collectors.toMap(BizProjectDto::getDealContractId, n -> n));
        Map<String, BizProjectDto> s2ProjMap = s2ProjList == null || s2ProjList.isEmpty() ? Collections.EMPTY_MAP : s2ProjList.stream().collect(Collectors.toMap(BizProjectDto::getDealContractId, n -> n));
        Map<String, BizProjectDto> s3ProjMap = s3ProjList == null || s3ProjList.isEmpty() ? Collections.EMPTY_MAP : s3ProjList.stream().collect(Collectors.toMap(BizProjectDto::getDealContractId, n -> n));
        Map<String, BizProjectDto> s4ProjMap = s4ProjList == null || s4ProjList.isEmpty() ? Collections.EMPTY_MAP : s4ProjList.stream().collect(Collectors.toMap(BizProjectDto::getDealContractId, n -> n));

        Map<String, BizDeptTaskDto> kpiMap = yearKpiList == null || yearKpiList.isEmpty() ? Collections.EMPTY_MAP : yearKpiList.stream().collect(Collectors.toMap(BizDeptTaskDto::getDeptId, n -> n));
        Map<String, BizDeptTaskDto> s1Map = s1List == null || s1List.isEmpty() ? Collections.EMPTY_MAP : s1List.stream().collect(Collectors.toMap(BizDeptTaskDto::getDeptId, n -> n));
        Map<String, BizDeptTaskDto> s2Map = s2List == null || s2List.isEmpty() ? Collections.EMPTY_MAP : s2List.stream().collect(Collectors.toMap(BizDeptTaskDto::getDeptId, n -> n));
        Map<String, BizDeptTaskDto> s3Map = s3List == null || s3List.isEmpty() ? Collections.EMPTY_MAP : s3List.stream().collect(Collectors.toMap(BizDeptTaskDto::getDeptId, n -> n));
        Map<String, BizDeptTaskDto> s4Map = s4List == null || s4List.isEmpty() ? Collections.EMPTY_MAP : s4List.stream().collect(Collectors.toMap(BizDeptTaskDto::getDeptId, n -> n));

        List<BizProjectYearlySumVo> result = new ArrayList<>();
        BizProjectYearlySumVo vo = null;
        for (SysDeptDto deptDto : sysDeptDtos) {
            String deptId = deptDto.getDeptId();

            vo = new BizProjectYearlySumVo();
            vo.setYear(kpiYear);
            vo.setDeptId(deptId);
            vo.setDeptName(deptDto.getDeptName());

            BizDeptTaskDto kpiDto = kpiMap.get(deptId);
            vo.setTotalKpi(kpiDto == null ? 0 : kpiDto.getTaskPrice());

            BizProjectYearlySumVo projDto = projMap.get(deptId);
            vo.setDeal(projDto == null ? 0 : projDto.getDeal());
            vo.setNz(projDto == null ? 0 : projDto.getNz());
            vo.setInstruction(projDto == null ? 0 : projDto.getInstruction());
            vo.setTh(projDto == null ? 0 : projDto.getTh());

            BizDeptTaskDto s1Dto = s1Map.get(deptId);
            vo.setS1Kpi(s1Dto == null ? 0 : s1Dto.getTaskPrice());
            BizProjectDto s1ProjDto = s1ProjMap.get(deptId);
            vo.setS1Real(s1ProjDto == null ? 0 : s1ProjDto.getContractPrice());
            vo.setS1Ratio(BigDecimalUtils.divide(vo.getS1Real(), vo.getS1Kpi() == 0 ? 1 : vo.getS1Kpi()));

            BizDeptTaskDto s2Dto = s2Map.get(deptId);
            vo.setS2Kpi(s2Dto == null ? 0 : s2Dto.getTaskPrice());
            BizProjectDto s2ProjDto = s2ProjMap.get(deptId);
            vo.setS2Real(s2ProjDto == null ? 0 : s2ProjDto.getContractPrice());
            vo.setS2Ratio(BigDecimalUtils.divide(vo.getS2Real(), vo.getS2Kpi() == 0 ? 1 : vo.getS2Kpi()));

            BizDeptTaskDto s3Dto = s2Map.get(deptId);
            vo.setS3Kpi(s3Dto == null ? 0 : s3Dto.getTaskPrice());
            BizProjectDto s3ProjDto = s3ProjMap.get(deptId);
            vo.setS3Real(s3ProjDto == null ? 0 : s3ProjDto.getContractPrice());
            vo.setS3Ratio(BigDecimalUtils.divide(vo.getS3Real(), vo.getS3Kpi() == 0 ? 1 : vo.getS3Kpi()));

            BizDeptTaskDto s4Dto = s2Map.get(deptId);
            vo.setS4Kpi(s4Dto == null ? 0 : s4Dto.getTaskPrice());
            BizProjectDto s4ProjDto = s4ProjMap.get(deptId);
            vo.setS4Real(s4ProjDto == null ? 0 : s4ProjDto.getContractPrice());
            vo.setS4Ratio(BigDecimalUtils.divide(vo.getS4Real(), vo.getS4Kpi() == 0 ? 1 : vo.getS4Kpi()));

            result.add(vo);
        }

        return result;
    }

    @Log(logContent = "删除劳动竞赛得分", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/indicator/laborScore/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteLaborScore(@PathVariable String id) {
        BizRptLaborScoreDto dto = bizRptLaborScoreService.selectByKey(id);
        if (null == dto) {
            throw new AppException("当前劳动竞赛得分不存在");
        }
        if (1 != bizRptLaborScoreService.delete(id)) {
            return AppMessage.success(id, "删除劳动竞赛得分成功");
        }
        return AppMessage.errorObjId(id, "删除劳动竞赛得分失败");
    }

    @RequestMapping(value = "/marketAreaAnalysis", method = RequestMethod.GET)
    public AppMessage marketAreaAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                         @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {
        List<RptWorkZoneAnalysisVo> retval = marketAreaAnalysisImpl(yearMonthStart, yearMonthEnd);
        return AppMessage.success(retval, "统计报表成功");
    }

    private List<RptWorkZoneAnalysisVo> marketAreaAnalysisImpl(String yearMonthStart, String yearMonthEnd) {

        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        List<BizAnalysisDto> currentList = fusionReportService.selectListMarketAreaAnalysis(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = bizDealLastProjService.selectListMarketAreaAnalysis(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).distinct().collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getWorkZone).distinct().collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getWorkZone, n -> n)) : new HashMap<>();
        List<RptWorkZoneAnalysisVo> retval = keys.stream().map(itemKey -> {
            RptWorkZoneAnalysisVo item = new RptWorkZoneAnalysisVo();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.setWorkZone(itemKey);
            item.setLastAmount(lastAmount);
            item.setCurrentAmount(currentAmount);
            item.setGrowth(growth);
            item.setGrowthRatio(growthRatio);
            return item;
        }).collect(Collectors.toList());

        return retval;
    }

    @Deprecated
    private List<RptWorkZoneAnalysisVo> marketAreaAnalysisImpl20200808(String yearMonthStart, String yearMonthEnd) {

        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        List<BizAnalysisDto> currentList = fusionReportService.selectListMarketAreaAnalysis(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = projectSumDataService.selectListMarketAreaAnalysis(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getWorkZone).collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getWorkZone, n -> n)) : new HashMap<>();
        List<RptWorkZoneAnalysisVo> retval = keys.stream().map(itemKey -> {
            RptWorkZoneAnalysisVo item = new RptWorkZoneAnalysisVo();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.setWorkZone(itemKey);
            item.setLastAmount(lastAmount);
            item.setCurrentAmount(currentAmount);
            item.setGrowth(growth);
            item.setGrowthRatio(growthRatio);
            return item;
        }).collect(Collectors.toList());

        return retval;
    }

    @RequestMapping(value = "/marketAreaWorktypeAnalysis", method = RequestMethod.GET)
    public AppMessage marketAreaWorktypeAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                                 @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                                 @RequestParam(value = "workZone", defaultValue = "") String workZone) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);

        List<BizAnalysisDto> currentList = null;
        // boolean isCydq = workZone.indexOf("川渝地区") > -1;
        // boolean isYyq = workZone.indexOf("非页岩气") > -1;
        // if (isCydq) {
        //     if (isYyq) {
        //         currentList = fusionReportService.selectListMarketAreaWorktypeCydqYyq(params);
        //     } else {
        //         currentList = fusionReportService.selectListMarketAreaWorktypeCydqFyyq(params);
        //     }
        // } else {
        //     params.put("workZone", WorkZoneEnum.getEnumByValue(workZone));
        //     currentList = fusionReportService.selectListMarketAreaWorktypeFcydq(params);
        // }
        params.put("workZone", WorkZoneEnum.getEnumByValue(workZone));
        currentList = fusionReportService.selectListMarketAreaWorktype(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = null;
        // if (isCydq) {
        //     if (isYyq) {
        //         lastList = projectSumDataService.selectListMarketAreaWorktypeCydqYyq(params);
        //     } else {
        //         lastList = projectSumDataService.selectListMarketAreaWorktypeCydqFyyq(params);
        //     }
        // } else {
        //     params.put("workZone", workZone);
        //     lastList = projectSumDataService.selectListMarketAreaWorktypeFcydq(params);
        // }
        params.put("workZone", WorkZoneEnum.getEnumByValue(workZone));
        lastList = bizDealLastProjService.selectListMarketAreaWorktype(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getWorkType1).collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getWorkType1, n -> n)) : new HashMap<>();
        List<Map<String, Object>> retval = keys.stream().map(itemKey -> {
            Map<String, Object> item = new HashMap<>();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.put("workType1", itemKey);
            item.put("lastAmount", lastAmount);
            item.put("currentAmount", currentAmount);
            item.put("growth", growth);
            item.put("growthRatio", growthRatio);
            return item;
        }).collect(Collectors.toList());
        return AppMessage.success(retval, "统计报表成功");
    }

    @Deprecated
    public AppMessage marketAreaWorktypeAnalysis20200808(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                                         @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                                         @RequestParam(value = "workZone", defaultValue = "") String workZone) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);

        List<BizAnalysisDto> currentList = null;
        boolean isCydq = workZone.indexOf("川渝地区") > -1;
        boolean isYyq = workZone.indexOf("非页岩气") > -1;
        if (isCydq) {
            if (isYyq) {
                currentList = fusionReportService.selectListMarketAreaWorktypeCydqYyq(params);
            } else {
                currentList = fusionReportService.selectListMarketAreaWorktypeCydqFyyq(params);
            }
        } else {
            params.put("workZone", WorkZoneEnum.getEnumByValue(workZone));
            currentList = fusionReportService.selectListMarketAreaWorktypeFcydq(params);
        }

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = null;
        if (isCydq) {
            if (isYyq) {
                lastList = projectSumDataService.selectListMarketAreaWorktypeCydqYyq(params);
            } else {
                lastList = projectSumDataService.selectListMarketAreaWorktypeCydqFyyq(params);
            }
        } else {
            params.put("workZone", workZone);
            lastList = projectSumDataService.selectListMarketAreaWorktypeFcydq(params);
        }

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getWorkType1).collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getWorkType1, n -> n)) : new HashMap<>();
        List<Map<String, Object>> retval = keys.stream().map(itemKey -> {
            Map<String, Object> item = new HashMap<>();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.put("workType1", itemKey);
            item.put("lastAmount", lastAmount);
            item.put("currentAmount", currentAmount);
            item.put("growth", growth);
            item.put("growthRatio", growthRatio);
            return item;
        }).collect(Collectors.toList());
        return AppMessage.success(retval, "统计报表成功");
    }

    @RequestMapping(value = "/marketAreaServtypeAnalysis", method = RequestMethod.GET)
    public AppMessage marketAreaServtypeAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                                 @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                                 @RequestParam(value = "workZone", defaultValue = "") String workZone,
                                                 @RequestParam(value = "workType1", defaultValue = "") String workType1) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        params.put("workType1", workType1);

        List<BizAnalysisDto> currentList = null;
        // boolean isCydq = workZone.indexOf("川渝地区") > -1;
        // boolean isYyq = workZone.indexOf("非页岩气") > -1;
        // if (isCydq) {
        //     if (isYyq) {
        //         currentList = fusionReportService.selectListMarketAreaServtypeCydqYyq(params);
        //     } else {
        //         currentList = fusionReportService.selectListMarketAreaServtypeCydqFyyq(params);
        //     }
        // } else {
        //     params.put("workZone", WorkZoneEnum.getEnumByValue(workZone));
        //     currentList = fusionReportService.selectListMarketAreaServtypeFcydq(params);
        // }
        params.put("workZone", WorkZoneEnum.getEnumByValue(workZone));
        currentList = fusionReportService.selectListMarketAreaServtype(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        params.put("workType1", workType1);
        List<BizProjectSumDataDto> lastList = null;
        // if (isCydq) {
        //     if (isYyq) {
        //         lastList = projectSumDataService.selectListMarketAreaServtypeCydqYyq(params);
        //     } else {
        //         lastList = projectSumDataService.selectListMarketAreaServtypeCydqFyyq(params);
        //     }
        // } else {
        //     params.put("workZone", workZone);
        //     lastList = projectSumDataService.selectListMarketAreaServtypeFcydq(params);
        // }
        params.put("workZone", WorkZoneEnum.getEnumByValue(workZone));
        lastList = bizDealLastProjService.selectListMarketAreaServtype(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getServType).collect(Collectors.toList()));
        }
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getServType, n -> n)) : new HashMap<>();
        List<Map<String, Object>> retval = keys.stream().map(itemKey -> {
            Map<String, Object> item = new HashMap<>();
            Double lastAmount = lastMap.get(itemKey) == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.put("servType", itemKey);
            item.put("lastAmount", lastAmount);
            item.put("currentAmount", currentAmount);
            item.put("growth", growth);
            item.put("growthRatio", growthRatio);
            return item;
        }).collect(Collectors.toList());
        return AppMessage.success(retval, "统计报表成功");
    }

    @Deprecated
    public AppMessage marketAreaServtypeAnalysis20200808(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                                         @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                                         @RequestParam(value = "workZone", defaultValue = "") String workZone,
                                                         @RequestParam(value = "workType1", defaultValue = "") String workType1) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        params.put("workType1", workType1);

        List<BizAnalysisDto> currentList = null;
        boolean isCydq = workZone.indexOf("川渝地区") > -1;
        boolean isYyq = workZone.indexOf("非页岩气") > -1;
        if (isCydq) {
            if (isYyq) {
                currentList = fusionReportService.selectListMarketAreaServtypeCydqYyq(params);
            } else {
                currentList = fusionReportService.selectListMarketAreaServtypeCydqFyyq(params);
            }
        } else {
            params.put("workZone", WorkZoneEnum.getEnumByValue(workZone));
            currentList = fusionReportService.selectListMarketAreaServtypeFcydq(params);
        }

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        params.put("workType1", workType1);
        List<BizProjectSumDataDto> lastList = null;
        if (isCydq) {
            if (isYyq) {
                lastList = projectSumDataService.selectListMarketAreaServtypeCydqYyq(params);
            } else {
                lastList = projectSumDataService.selectListMarketAreaServtypeCydqFyyq(params);
            }
        } else {
            params.put("workZone", workZone);
            lastList = projectSumDataService.selectListMarketAreaServtypeFcydq(params);
        }

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getServType).collect(Collectors.toList()));
        }
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getServType, n -> n)) : new HashMap<>();
        List<Map<String, Object>> retval = keys.stream().distinct().map(itemKey -> {
            Map<String, Object> item = new HashMap<>();
            Double lastAmount = lastMap.get(itemKey) == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.put("servType", itemKey);
            item.put("lastAmount", lastAmount);
            item.put("currentAmount", currentAmount);
            item.put("growth", growth);
            item.put("growthRatio", growthRatio);
            return item;
        }).collect(Collectors.toList());
        return AppMessage.success(retval, "统计报表成功");
    }

    @RequestMapping(value = "/marketAreaAnalysis/export", method = RequestMethod.GET)
    public AppMessage marketAreaAnalysisExport(HttpServletResponse response,
                                               @RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                               @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {
        List<RptWorkZoneAnalysisVo> retval = marketAreaAnalysisImpl(yearMonthStart, yearMonthEnd);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<RptWorkZoneAnalysisVo> util = new ExcelUtil<RptWorkZoneAnalysisVo>(RptWorkZoneAnalysisVo.class);
            return util.exportExcelBrowser(response, retval, "市场区域分析");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    @RequestMapping(value = "/clasWorkType1Analysis", method = RequestMethod.GET)
    public AppMessage clasWorkType1Analysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                            @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {
        List<RptClasWorkTypeAnalysisVo> retval = clasWorkTypeAnalysisImpl(yearMonthStart, yearMonthEnd);
        return AppMessage.success(retval, "统计报表成功");
    }

    private List<RptClasWorkTypeAnalysisVo> clasWorkTypeAnalysisImpl(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart, @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        List<BizAnalysisDto> currentList = fusionReportService.selectListClasWorkType1(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = bizDealLastProjService.selectListClasWorkType1(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().filter(item -> StringUtils.isNotEmpty(item.getBizItem())).map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().filter(item -> StringUtils.isNotEmpty(item.getWorkType1())).map(BizProjectSumDataDto::getWorkType1).collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getWorkType1, n -> n)) : new HashMap<>();
        return keys.stream().map(itemKey -> {
            RptClasWorkTypeAnalysisVo item = new RptClasWorkTypeAnalysisVo();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.setWorkType1(itemKey);
            item.setLastAmount(lastAmount);
            item.setCurrentAmount(currentAmount);
            item.setGrowth(growth);
            item.setGrowthRatio(growthRatio);
            return item;
        }).collect(Collectors.toList());
    }

    private List<RptClasWorkTypeAnalysisVo> clasWorkTypeAnalysisImpl20200808(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart, @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        List<BizAnalysisDto> currentList = fusionReportService.selectListClasWorkType1(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        List<BizProjectSumDataDto> lastList = projectSumDataService.selectListClasWorkType1(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().filter(item -> StringUtils.isNotEmpty(item.getBizItem())).map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().filter(item -> StringUtils.isNotEmpty(item.getWorkType1())).map(BizProjectSumDataDto::getWorkType1).collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getWorkType1, n -> n)) : new HashMap<>();
        return keys.stream().map(itemKey -> {
            RptClasWorkTypeAnalysisVo item = new RptClasWorkTypeAnalysisVo();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.setWorkType1(itemKey);
            item.setLastAmount(lastAmount);
            item.setCurrentAmount(currentAmount);
            item.setGrowth(growth);
            item.setGrowthRatio(growthRatio);
            return item;
        }).collect(Collectors.toList());
    }

    @RequestMapping(value = "/clasWorkType2Analysis", method = RequestMethod.GET)
    public AppMessage clasWorkType2Analysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                            @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                            @RequestParam(value = "workType1", defaultValue = "") String workType1) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        params.put("workType1", workType1);
        List<BizAnalysisDto> currentList = fusionReportService.selectListClasWorkType2(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        params.put("workType1", workType1);
        List<BizProjectSumDataDto> lastList = bizDealLastProjService.selectListClasWorkType2(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getWorkType2).collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getWorkType2, n -> n)) : new HashMap<>();
        List<Map<String, Object>> retval = keys.stream().map(itemKey -> {
            Map<String, Object> item = new HashMap<>();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.put("workType2", itemKey);
            item.put("lastAmount", lastAmount);
            item.put("currentAmount", currentAmount);
            item.put("growth", growth);
            item.put("growthRatio", growthRatio);
            return item;
        }).collect(Collectors.toList());
        return AppMessage.success(retval, "统计报表成功");
    }

    public AppMessage clasWorkType2Analysis20200808(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                                    @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                                    @RequestParam(value = "workType1", defaultValue = "") String workType1) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        params.put("workType1", workType1);
        List<BizAnalysisDto> currentList = fusionReportService.selectListClasWorkType2(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        params.put("workType1", workType1);
        List<BizProjectSumDataDto> lastList = projectSumDataService.selectListClasWorkType2(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getWorkType2).collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getWorkType2, n -> n)) : new HashMap<>();
        List<Map<String, Object>> retval = keys.stream().map(itemKey -> {
            Map<String, Object> item = new HashMap<>();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.put("workType2", itemKey);
            item.put("lastAmount", lastAmount);
            item.put("currentAmount", currentAmount);
            item.put("growth", growth);
            item.put("growthRatio", growthRatio);
            return item;
        }).collect(Collectors.toList());
        return AppMessage.success(retval, "统计报表成功");
    }

    @RequestMapping(value = "/clasServTypeAnalysis", method = RequestMethod.GET)
    public AppMessage clasServTypeAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                           @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                           @RequestParam(value = "workType1", defaultValue = "") String workType1,
                                           @RequestParam(value = "workType2", defaultValue = "") String workType2) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        params.put("workType1", workType1);
        params.put("workType2", workType2);
        List<BizAnalysisDto> currentList = fusionReportService.selectListClasServType(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        params.put("workType1", workType1);
        params.put("workType2", workType2);
        List<BizProjectSumDataDto> lastList = bizDealLastProjService.selectListClasServType(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getServType).collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getServType, n -> n)) : new HashMap<>();
        List<Map<String, Object>> retval = keys.stream().map(itemKey -> {
            Map<String, Object> item = new HashMap<>();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.put("servType", itemKey);
            item.put("lastAmount", lastAmount);
            item.put("currentAmount", currentAmount);
            item.put("growth", growth);
            item.put("growthRatio", growthRatio);
            return item;
        }).collect(Collectors.toList());
        return AppMessage.success(retval, "统计报表成功");
    }

    @Deprecated
    public AppMessage clasServTypeAnalysis20200808(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                                   @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                                   @RequestParam(value = "workType1", defaultValue = "") String workType1,
                                                   @RequestParam(value = "workType2", defaultValue = "") String workType2) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        params.put("workType1", workType1);
        params.put("workType2", workType2);
        List<BizAnalysisDto> currentList = fusionReportService.selectListClasServType(params);

        params = new HashMap<>();
        params.put("yearMonthStart", getSameMonthOfLastYear(yearMonthStart));
        params.put("yearMonthEnd", getSameMonthOfLastYear(yearMonthEnd));
        params.put("workType1", workType1);
        params.put("workType2", workType2);
        List<BizProjectSumDataDto> lastList = projectSumDataService.selectListClasServType(params);

        List<String> keys = new ArrayList<>();
        if (currentList != null && !currentList.isEmpty()) {
            keys.addAll(currentList.stream().map(BizAnalysisDto::getBizItem).collect(Collectors.toList()));
        }
        if (lastList != null && !lastList.isEmpty()) {
            keys.addAll(lastList.stream().map(BizProjectSumDataDto::getServType).collect(Collectors.toList()));
        }
        keys = keys.stream().distinct().collect(Collectors.toList());
        Map<String, BizAnalysisDto> currMap = CollectionUtils.isNotEmpty(currentList) ? currentList.stream().collect(Collectors.toMap(BizAnalysisDto::getBizItem, n -> n)) : new HashMap<>();
        Map<String, BizProjectSumDataDto> lastMap = CollectionUtils.isNotEmpty(lastList) ? lastList.stream().collect(Collectors.toMap(BizProjectSumDataDto::getServType, n -> n)) : new HashMap<>();
        List<Map<String, Object>> retval = keys.stream().map(itemKey -> {
            Map<String, Object> item = new HashMap<>();
            Double lastAmount = lastMap.get(itemKey) == null || lastMap.get(itemKey).getContractPrice() == null ? 0 : lastMap.get(itemKey).getContractPrice();
            Double currentAmount = currMap.get(itemKey) == null || currMap.get(itemKey).getCurrentAmount() == null ? 0 : currMap.get(itemKey).getCurrentAmount();
            Double growth = BigDecimalUtils.subtract(currentAmount, lastAmount);
            Float growthRatio = lastAmount == 0 || currentAmount == 0 ? 0 : BigDecimalUtils.divide(growth, lastAmount).floatValue();
            item.put("servType", itemKey);
            item.put("lastAmount", lastAmount);
            item.put("currentAmount", currentAmount);
            item.put("growth", growth);
            item.put("growthRatio", growthRatio);
            return item;
        }).collect(Collectors.toList());
        return AppMessage.success(retval, "统计报表成功");
    }

    @RequestMapping(value = "/clasWorkType1Analysis/export", method = RequestMethod.GET)
    public AppMessage clasWorkType1AnalysisExport(HttpServletResponse response,
                                                  @RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                                  @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {
        List<RptClasWorkTypeAnalysisVo> retval = clasWorkTypeAnalysisImpl(yearMonthStart, yearMonthEnd);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<RptClasWorkTypeAnalysisVo> util = new ExcelUtil<RptClasWorkTypeAnalysisVo>(RptClasWorkTypeAnalysisVo.class);
            return util.exportExcelBrowser(response, retval, "业务专业类别分析");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    @RequestMapping(value = "/market/monthly/one", method = RequestMethod.GET)
    public AppMessage loadOneMarketMonthlyData(@RequestParam(value = "rptMonthName", defaultValue = "") String rptMonthName) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("rptMonthName", rptMonthName);
        List<BizRptMonthDto> list = bizRptMonthService.selectList(params);
        return AppMessage.success(list, "查询成功");
    }

    @RequestMapping(value = "/market/monthly", method = RequestMethod.GET)
    public AppMessage queryMarketMonthlyData(@RequestParam(value = "dateStart", defaultValue = "") String dateStart,
                                             @RequestParam(value = "dateEnd", defaultValue = "") String dateEnd,
                                             @RequestParam(value = "rptMonthName", defaultValue = "") String rptMonthName) throws Exception {
        // 新签今年累计
        BizRptMonthDto rptMonthDto = new BizRptMonthDto();
        sumMarketMonthlyXq(dateStart, dateEnd, rptMonthName, rptMonthDto);

        // 新签去年同期
        String lastYear = String.valueOf(Integer.parseInt(rptMonthName.substring(0, 4)) - 1);
        String lastYearMonthName = lastYear + rptMonthName.substring(4);
        sumMarketMonthlyXq2(lastYearMonthName, rptMonthDto);

        // 全年预计去年实际
        sumMarketMonthlyQn2(lastYear, rptMonthDto);

        return AppMessage.success(Arrays.asList(rptMonthDto), "查询成功");
    }

    /**
     * 新签今年累计
     *
     * @param dateStart
     * @param dateEnd
     * @param rptMonthName
     * @param rptMonthDto
     */
    private void sumMarketMonthlyXq(String dateStart, String dateEnd, String rptMonthName, BizRptMonthDto rptMonthDto) {
        Map<String, Object> params;// 新签今年累计
        params = new HashMap<>();
        params.put("dateStart", dateStart);
        params.put("dateEnd", dateEnd);
        params.put("yearMonth", rptMonthName);
        params.put("year", rptMonthName.substring(0, 4)); // 累计，故条件为year and <=month

        // 一、新签国内川庆内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.DOMESTIC.getKey());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CCDE_INNER.getKey());
        rptMonthDto.setXqGnCjnb(toTenThousand(bizProjectService.sumMonthly(params)));

        // 二、国内-集团内部-关联交易-川渝地区
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.DOMESTIC.getKey());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getKey());
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.RELATED.getKey());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getKey());
        rptMonthDto.setXqGnJtnbGlCy(toTenThousand(bizProjectService.sumMonthly(params)));

        // 三、国内-集团内部-关联交易-长庆地区
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getKey());
        rptMonthDto.setXqGnJtnbGlCq(toTenThousand(bizProjectService.sumMonthly(params)));

        // 四、国内-集团内部-非关联交易-川渝地区
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.UNRELATED.getKey());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getKey());
        rptMonthDto.setXqGnJtnbFglCy(toTenThousand(bizProjectService.sumMonthly(params)));

        // 五、国内-集团内部-非关联交易-长庆地区
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getKey());
        rptMonthDto.setXqGnJtnbFglCq(toTenThousand(bizProjectService.sumMonthly(params)));

        // 六、国内-集团内部-非关联交易-塔里木地区
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.TLM_ZONE.getKey());
        rptMonthDto.setXqGnJtnbFglTlm(toTenThousand(bizProjectService.sumMonthly(params)));

        // 七、国内-集团内部-非关联交易-其他地区
        params.remove(PARAM_WORK_ZONE);
        params.put(PARAM_WORK_ZONE_NOT, "1");
        rptMonthDto.setXqGnJtnbFglQt(toTenThousand(bizProjectService.sumMonthly(params)));

        // 八、国内-集团外部-国内反承包：无
        rptMonthDto.setXqGnJtwbGnfcb(0d);

        // 九、国内-集团外部-其他
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getKey());
        params.remove(PARAM_CONTRACT_TYPE);
        params.remove(PARAM_WORK_ZONE);
        params.remove(PARAM_WORK_ZONE_NOT);
        rptMonthDto.setXqGnJtwbQt(toTenThousand(bizProjectService.sumMonthly(params)));

        // 十、国外-集团内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.OVERSEAS.getKey());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getKey());
        rptMonthDto.setXqGwJtnb(toTenThousand(bizProjectService.sumMonthly(params)));

        // 十一、国外-集团外部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.OVERSEAS.getKey());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getKey());
        rptMonthDto.setXqGwJtwb(toTenThousand(bizProjectService.sumMonthly(params)));

        // 合计
        BigDecimal hj = new BigDecimal(rptMonthDto.getXqGnCjnb())
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbGlCy()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbGlCq()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglCy()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglCq()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglTlm()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglQt()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtwbGnfcb()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtwbQt()))
                .add(new BigDecimal(rptMonthDto.getXqGwJtnb()))
                .add(new BigDecimal(rptMonthDto.getXqGwJtwb()));
        rptMonthDto.setXqHj(hj.doubleValue());
    }

    /**
     * 新签去年同期
     *
     * @param rptMonthName
     * @param rptMonthDto
     */
    private void sumMarketMonthlyXq2(String rptMonthName, BizRptMonthDto rptMonthDto) {
        Map<String, Object> params;
        params = new HashMap<>();
        params.put("yearMonth", rptMonthName);
        params.put("year", rptMonthName.substring(0, 4)); // 累计，故条件为year and <=month

        // 国内-川庆内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.DOMESTIC.getKey());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CCDE_INNER.getKey());
        rptMonthDto.setXqGnCjnb2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-关联-川渝
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getKey());
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.RELATED.getKey());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getKey());
        rptMonthDto.setXqGnJtnbGlCy2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-关联-长庆
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getKey());
        rptMonthDto.setXqGnJtnbGlCq2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-非关联-川渝
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.UNRELATED.getKey());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getKey());
        rptMonthDto.setXqGnJtnbFglCy2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-非关联-长庆
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getKey());
        rptMonthDto.setXqGnJtnbFglCq2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-非关联-塔里木
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.TLM_ZONE.getKey());
        rptMonthDto.setXqGnJtnbFglTlm2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-非关联-其它
        params.remove(PARAM_WORK_ZONE);
        params.put(PARAM_WORK_ZONE_NOT, "1");
        rptMonthDto.setXqGnJtnbFglQt2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团外部-国内反承包 无
        rptMonthDto.setXqGnJtwbGnfcb2(0d);

        // 国内-集团外部-其它
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getKey());
        params.remove(PARAM_CONTRACT_TYPE);
        params.remove(PARAM_WORK_ZONE);
        params.remove(PARAM_WORK_ZONE_NOT);
        rptMonthDto.setXqGnJtwbQt2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国外-集团内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.OVERSEAS.getKey());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getKey());
        rptMonthDto.setXqGwJtnb2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国外-集团外部
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getKey());
        rptMonthDto.setXqGwJtwb2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 合计
        BigDecimal hj = new BigDecimal(rptMonthDto.getXqGnCjnb2())
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbGlCy2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbGlCq2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglCy2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglCq2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglTlm2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglQt2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtwbGnfcb2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtwbQt2()))
                .add(new BigDecimal(rptMonthDto.getXqGwJtnb2()))
                .add(new BigDecimal(rptMonthDto.getXqGwJtwb2()));
        rptMonthDto.setXqHj2(hj.doubleValue());
    }

    private void sumMarketMonthlyXq2_20200808(String rptMonthName, BizRptMonthDto rptMonthDto) {
        Map<String, Object> params;
        params = new HashMap<>();
        params.put("yearMonth", rptMonthName);

        // 国内-川庆内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.DOMESTIC.getValue());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CCDE_INNER.getValue());
        rptMonthDto.setXqGnCjnb2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 国内-集团内部-关联-川渝
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getValue());
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.RELATED.getValue());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getValue());
        rptMonthDto.setXqGnJtnbGlCy2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 国内-集团内部-关联-长庆
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getValue());
        rptMonthDto.setXqGnJtnbGlCq2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 国内-集团内部-非关联-川渝
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.UNRELATED.getValue());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getValue());
        rptMonthDto.setXqGnJtnbFglCy2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 国内-集团内部-非关联-长庆
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getValue());
        rptMonthDto.setXqGnJtnbFglCq2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 国内-集团内部-非关联-塔里木
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.TLM_ZONE.getValue());
        rptMonthDto.setXqGnJtnbFglTlm2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 国内-集团内部-非关联-其它
        params.remove(PARAM_WORK_ZONE);
        params.put(PARAM_WORK_ZONE_NOT, "1");
        rptMonthDto.setXqGnJtnbFglQt2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 国内-集团外部-国内反承包 无
        rptMonthDto.setXqGnJtwbGnfcb2(0d);

        // 国内-集团外部-其它
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getValue());
        params.remove(PARAM_CONTRACT_TYPE);
        params.remove(PARAM_WORK_ZONE);
        params.remove(PARAM_WORK_ZONE_NOT);
        rptMonthDto.setXqGnJtwbQt2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 国外-集团内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.OVERSEAS.getValue());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getValue());
        rptMonthDto.setXqGwJtnb2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 国外-集团外部
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getValue());
        rptMonthDto.setXqGwJtwb2(toTenThousand(bizProjectSumDataService.sumMonthly(params)));

        // 合计
        BigDecimal hj = new BigDecimal(rptMonthDto.getXqGnCjnb2())
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbGlCy2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbGlCq2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglCy2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglCq2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglTlm2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtnbFglQt2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtwbGnfcb2()))
                .add(new BigDecimal(rptMonthDto.getXqGnJtwbQt2()))
                .add(new BigDecimal(rptMonthDto.getXqGwJtnb2()))
                .add(new BigDecimal(rptMonthDto.getXqGwJtwb2()));
        rptMonthDto.setXqHj2(hj.doubleValue());
    }

    /**
     * 去年全年实际
     *
     * @param year
     * @param rptMonthDto
     */
    private void sumMarketMonthlyQn2(String year, BizRptMonthDto rptMonthDto) {
        Map<String, Object> params;
        params = new HashMap<>();
        params.put("year", year);

        // 国内-川庆内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.DOMESTIC.getKey());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CCDE_INNER.getKey());
        rptMonthDto.setQnGnCjnb2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-关联-川渝
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getKey());
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.RELATED.getKey());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getKey());
        rptMonthDto.setQnGnJtnbGlCy2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-关联-长庆
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getKey());
        rptMonthDto.setQnGnJtnbGlCq2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-非关联-川渝
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.UNRELATED.getKey());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getKey());
        rptMonthDto.setQnGnJtnbFglCy2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-非关联-长庆
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getKey());
        rptMonthDto.setQnGnJtnbFglCq2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-非关联-塔里木
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.TLM_ZONE.getKey());
        rptMonthDto.setQnGnJtnbFglTlm2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团内部-非关联-其它
        params.remove(PARAM_WORK_ZONE);
        params.put(PARAM_WORK_ZONE_NOT, "1");
        rptMonthDto.setQnGnJtnbFglQt2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国内-集团外部-国内反承包 无
        rptMonthDto.setQnGnJtwbGnfcb2(0d);

        // 国内-集团外部-其它
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getKey());
        params.remove(PARAM_CONTRACT_TYPE);
        params.remove(PARAM_WORK_ZONE);
        params.remove(PARAM_WORK_ZONE_NOT);
        rptMonthDto.setQnGnJtwbQt2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国外-集团内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.OVERSEAS.getKey());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getKey());
        rptMonthDto.setQnGwJtnb2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 国外-集团外部
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getKey());
        rptMonthDto.setQnGwJtwb2(toTenThousand(bizDealLastProjService.sumMonthly(params)));

        // 合计
        BigDecimal hj = new BigDecimal(rptMonthDto.getQnGnCjnb2())
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbGlCy2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbGlCq2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbFglCy2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbFglCq2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbFglTlm2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbFglQt2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtwbGnfcb2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtwbQt2()))
                .add(new BigDecimal(rptMonthDto.getQnGwJtnb2()))
                .add(new BigDecimal(rptMonthDto.getQnGwJtwb2()));
        rptMonthDto.setQnHj2(hj.doubleValue());
    }

    private void sumMarketMonthlyQn2_20200808(String year, BizRptMonthDto rptMonthDto) {
        Map<String, Object> params;
        params = new HashMap<>();
        params.put("year", year);

        // 国内-川庆内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.DOMESTIC.getValue());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CCDE_INNER.getValue());
        rptMonthDto.setQnGnCjnb2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 国内-集团内部-关联-川渝
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getValue());
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.RELATED.getValue());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getValue());
        rptMonthDto.setQnGnJtnbGlCy2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 国内-集团内部-关联-长庆
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getValue());
        rptMonthDto.setQnGnJtnbGlCq2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 国内-集团内部-非关联-川渝
        params.put(PARAM_CONTRACT_TYPE, ContractTypeEnum.UNRELATED.getValue());
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.SC_CQ_ZONE.getValue());
        rptMonthDto.setQnGnJtnbFglCy2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 国内-集团内部-非关联-长庆
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.CQ_ZONE.getValue());
        rptMonthDto.setQnGnJtnbFglCq2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 国内-集团内部-非关联-塔里木
        params.put(PARAM_WORK_ZONE, WorkZoneEnum.TLM_ZONE.getValue());
        rptMonthDto.setQnGnJtnbFglTlm2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 国内-集团内部-非关联-其它
        params.remove(PARAM_WORK_ZONE);
        params.put(PARAM_WORK_ZONE_NOT, "1");
        rptMonthDto.setQnGnJtnbFglQt2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 国内-集团外部-国内反承包 无
        rptMonthDto.setQnGnJtwbGnfcb2(0d);

        // 国内-集团外部-其它
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getValue());
        params.remove(PARAM_CONTRACT_TYPE);
        params.remove(PARAM_WORK_ZONE);
        params.remove(PARAM_WORK_ZONE_NOT);
        rptMonthDto.setQnGnJtwbQt2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 国外-集团内部
        params.put(PARAM_MARKET_TYPE, MarketTypeEnum.OVERSEAS.getValue());
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_INNER.getValue());
        rptMonthDto.setQnGwJtnb2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 国外-集团外部
        params.put(PARAM_COMPANY_TYPE, CompanyTypeEnum.CNPC_OUTER.getValue());
        rptMonthDto.setQnGwJtwb2(toTenThousand(projectSumDataService.sumMonthly(params)));

        // 合计
        BigDecimal hj = new BigDecimal(rptMonthDto.getQnGnCjnb2())
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbGlCy2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbGlCq2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbFglCy2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbFglCq2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbFglTlm2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtnbFglQt2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtwbGnfcb2()))
                .add(new BigDecimal(rptMonthDto.getQnGnJtwbQt2()))
                .add(new BigDecimal(rptMonthDto.getQnGwJtnb2()))
                .add(new BigDecimal(rptMonthDto.getQnGwJtwb2()));
        rptMonthDto.setQnHj2(hj.doubleValue());
    }

    @Log(logContent = "保存市场月报", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/market/monthly", method = RequestMethod.PUT)
    public AppMessage saveMarketMonthlyData(@RequestBody BizRptMonthDto monthDto) throws Exception {
        BizRptMonthDto rptMonthDto = new BizRptMonthDto();

        rptMonthDto.setRptMonthName(monthDto.getRptMonthName());
        rptMonthDto.setStartAt(monthDto.getStartAt());
        rptMonthDto.setEndAt(monthDto.getEndAt());
        rptMonthDto.setCreateAt(monthDto.getCreateAt());
        rptMonthDto.setCreateId(monthDto.getCreateId());
        rptMonthDto.setDeptId(monthDto.getDeptId());
        rptMonthDto.setDeptName(monthDto.getDeptName());
        rptMonthDto.setCheckId(monthDto.getCheckId());
        rptMonthDto.setLinkPhone(monthDto.getLinkPhone());
        rptMonthDto.setMarketDesc(monthDto.getMarketDesc());
        rptMonthDto.setMarketQ(monthDto.getMarketQ());
        rptMonthDto.setMarketPlan(monthDto.getMarketPlan());

        rptMonthDto.setXqHj(monthDto.getXqHj());
        rptMonthDto.setXqGnCjnb(monthDto.getXqGnCjnb());
        rptMonthDto.setXqGnJtnbGlCy(monthDto.getXqGnJtnbGlCy());
        rptMonthDto.setXqGnJtnbGlCq(monthDto.getXqGnJtnbGlCq());
        rptMonthDto.setXqGnJtnbFglCy(monthDto.getXqGnJtnbFglCy());
        rptMonthDto.setXqGnJtnbFglCq(monthDto.getXqGnJtnbFglCq());
        rptMonthDto.setXqGnJtnbFglTlm(monthDto.getXqGnJtnbFglTlm());
        rptMonthDto.setXqGnJtnbFglQt(monthDto.getXqGnJtnbFglQt());
        rptMonthDto.setXqGnJtwbGnfcb(monthDto.getXqGnJtwbGnfcb());
        rptMonthDto.setXqGnJtwbQt(monthDto.getXqGnJtwbQt());
        rptMonthDto.setXqGwJtnb(monthDto.getXqGwJtnb());
        rptMonthDto.setXqGwJtwb(monthDto.getXqGwJtwb());

        rptMonthDto.setXqHj2(monthDto.getXqHj2());
        rptMonthDto.setXqGnCjnb2(monthDto.getXqGnCjnb2());
        rptMonthDto.setXqGnJtnbGlCy2(monthDto.getXqGnJtnbGlCy2());
        rptMonthDto.setXqGnJtnbGlCq2(monthDto.getXqGnJtnbGlCq2());
        rptMonthDto.setXqGnJtnbFglCy2(monthDto.getXqGnJtnbFglCy2());
        rptMonthDto.setXqGnJtnbFglCq2(monthDto.getXqGnJtnbFglCq2());
        rptMonthDto.setXqGnJtnbFglTlm2(monthDto.getXqGnJtnbFglTlm2());
        rptMonthDto.setXqGnJtnbFglQt2(monthDto.getXqGnJtnbFglQt2());
        rptMonthDto.setXqGnJtwbGnfcb2(monthDto.getXqGnJtwbGnfcb2());
        rptMonthDto.setXqGnJtwbQt2(monthDto.getXqGnJtwbQt2());
        rptMonthDto.setXqGwJtnb2(monthDto.getXqGwJtnb2());
        rptMonthDto.setXqGwJtwb2(monthDto.getXqGwJtwb2());

        rptMonthDto.setXqTbzjyy(monthDto.getXqTbzjyy());

        rptMonthDto.setQnHj(monthDto.getQnHj());
        rptMonthDto.setQnGnCjnb(monthDto.getQnGnCjnb());
        rptMonthDto.setQnGnJtnbGlCy(monthDto.getQnGnJtnbGlCy());
        rptMonthDto.setQnGnJtnbGlCq(monthDto.getQnGnJtnbGlCq());
        rptMonthDto.setQnGnJtnbFglCy(monthDto.getQnGnJtnbFglCy());
        rptMonthDto.setQnGnJtnbFglCq(monthDto.getQnGnJtnbFglCq());
        rptMonthDto.setQnGnJtnbFglTlm(monthDto.getQnGnJtnbFglTlm());
        rptMonthDto.setQnGnJtnbFglQt(monthDto.getQnGnJtnbFglQt());
        rptMonthDto.setQnGnJtwbGnfcb(monthDto.getQnGnJtwbGnfcb());
        rptMonthDto.setQnGnJtwbQt(monthDto.getQnGnJtwbQt());
        rptMonthDto.setQnGwJtnb(monthDto.getQnGwJtnb());
        rptMonthDto.setQnGwJtwb(monthDto.getQnGwJtwb());

        rptMonthDto.setQnHj2(monthDto.getQnHj2());
        rptMonthDto.setQnGnCjnb2(monthDto.getQnGnCjnb2());
        rptMonthDto.setQnGnJtnbGlCy2(monthDto.getQnGnJtnbGlCy2());
        rptMonthDto.setQnGnJtnbGlCq2(monthDto.getQnGnJtnbGlCq2());
        rptMonthDto.setQnGnJtnbFglCy2(monthDto.getQnGnJtnbFglCy2());
        rptMonthDto.setQnGnJtnbFglCq2(monthDto.getQnGnJtnbFglCq2());
        rptMonthDto.setQnGnJtnbFglTlm2(monthDto.getQnGnJtnbFglTlm2());
        rptMonthDto.setQnGnJtnbFglQt2(monthDto.getQnGnJtnbFglQt2());
        rptMonthDto.setQnGnJtwbGnfcb2(monthDto.getQnGnJtwbGnfcb2());
        rptMonthDto.setQnGnJtwbQt2(monthDto.getQnGnJtwbQt2());
        rptMonthDto.setQnGwJtnb2(monthDto.getQnGwJtnb2());
        rptMonthDto.setQnGwJtwb2(monthDto.getQnGwJtwb2());

        rptMonthDto.setQnTbzjyy(monthDto.getQnTbzjyy());
//        rptMonthDto.setColumn84(monthDto.getColumn84());

//        BizRptMonthDto old = bizRptMonthService.selectByKey(monthDto.getRptMonthId());
        Map<String, Object> params = new HashMap<>();
        params.put("rptMonthName", monthDto.getRptMonthName());
        List<BizRptMonthDto> list = bizRptMonthService.selectList(params);
        BizRptMonthDto old = list == null || list.isEmpty() ? null : list.get(0);
        int save = -1;
        if (old == null) {
            // create
            rptMonthDto.setRptMonthId(StringUtils.getUuid32());
            save = bizRptMonthService.save(rptMonthDto);
        } else {
            // update
            rptMonthDto.setRptMonthId(old.getRptMonthId());
            save = bizRptMonthService.updateAll(rptMonthDto);
        }
        if (save != 1) {
            AppMessage.error("保存市场月报失败！");
        }
        return AppMessage.success(rptMonthDto.getRptMonthId(), "保存市场月报成功！");
    }

    private Double toTenThousand(String n) {
        double n1 = n == null ? 0 : Double.parseDouble(n);
        return BigDecimalUtils.divide(n1, 10000).doubleValue();
    }

    //    @Log(logContent = "市场月报导出", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/market/monthly/export", method = RequestMethod.GET)
    public AppMessage exportMarketMonthlyReport(HttpServletResponse response,
                                                @RequestParam(value = "rptMonthName", defaultValue = "") String rptMonthName,
                                                @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                                @RequestParam(value = "createAt", defaultValue = "") String createAt,
                                                @RequestParam(value = "createManName", defaultValue = "") String createManName,
                                                @RequestParam(value = "checkManName", defaultValue = "") String checkManName,
                                                @RequestParam(value = "linkPhone", defaultValue = "") String linkPhone,
                                                @RequestParam(value = "marketDesc", defaultValue = "") String marketDesc,
                                                @RequestParam(value = "marketQ", defaultValue = "") String marketQ,
                                                @RequestParam(value = "marketPlan", defaultValue = "") String marketPlan,
                                                @RequestParam(value = "xqHj", defaultValue = "") String xqHj,
                                                @RequestParam(value = "xqGnCjnb", defaultValue = "") String xqGnCjnb,
                                                @RequestParam(value = "xqGnJtnbGlCy", defaultValue = "") String xqGnJtnbGlCy,
                                                @RequestParam(value = "xqGnJtnbGlCq", defaultValue = "") String xqGnJtnbGlCq,
                                                @RequestParam(value = "xqGnJtnbFglCy", defaultValue = "") String xqGnJtnbFglCy,
                                                @RequestParam(value = "xqGnJtnbFglCq", defaultValue = "") String xqGnJtnbFglCq,
                                                @RequestParam(value = "xqGnJtnbFglTlm", defaultValue = "") String xqGnJtnbFglTlm,
                                                @RequestParam(value = "xqGnJtnbFglQt", defaultValue = "") String xqGnJtnbFglQt,
                                                @RequestParam(value = "xqGnJtwbGnfcb", defaultValue = "") String xqGnJtwbGnfcb,
                                                @RequestParam(value = "xqGnJtwbQt", defaultValue = "") String xqGnJtwbQt,
                                                @RequestParam(value = "xqGwJtnb", defaultValue = "") String xqGwJtnb,
                                                @RequestParam(value = "xqGwJtwb", defaultValue = "") String xqGwJtwb,
                                                @RequestParam(value = "xqHj2", defaultValue = "") String xqHj2,
                                                @RequestParam(value = "xqGnCjnb2", defaultValue = "") String xqGnCjnb2,
                                                @RequestParam(value = "xqGnJtnbGlCy2", defaultValue = "") String xqGnJtnbGlCy2,
                                                @RequestParam(value = "xqGnJtnbGlCq2", defaultValue = "") String xqGnJtnbGlCq2,
                                                @RequestParam(value = "xqGnJtnbFglCy2", defaultValue = "") String xqGnJtnbFglCy2,
                                                @RequestParam(value = "xqGnJtnbFglCq2", defaultValue = "") String xqGnJtnbFglCq2,
                                                @RequestParam(value = "xqGnJtnbFglTlm2", defaultValue = "") String xqGnJtnbFglTlm2,
                                                @RequestParam(value = "xqGnJtnbFglQt2", defaultValue = "") String xqGnJtnbFglQt2,
                                                @RequestParam(value = "xqGnJtwbGnfcb2", defaultValue = "") String xqGnJtwbGnfcb2,
                                                @RequestParam(value = "xqGnJtwbQt2", defaultValue = "") String xqGnJtwbQt2,
                                                @RequestParam(value = "xqGwJtnb2", defaultValue = "") String xqGwJtnb2,
                                                @RequestParam(value = "xqGwJtwb2", defaultValue = "") String xqGwJtwb2,
                                                @RequestParam(value = "xqHj3", defaultValue = "") String xqHj3,
                                                @RequestParam(value = "xqGnCjnb3", defaultValue = "") String xqGnCjnb3,
                                                @RequestParam(value = "xqGnJtnbGlCy3", defaultValue = "") String xqGnJtnbGlCy3,
                                                @RequestParam(value = "xqGnJtnbGlCq3", defaultValue = "") String xqGnJtnbGlCq3,
                                                @RequestParam(value = "xqGnJtnbFglCy3", defaultValue = "") String xqGnJtnbFglCy3,
                                                @RequestParam(value = "xqGnJtnbFglCq3", defaultValue = "") String xqGnJtnbFglCq3,
                                                @RequestParam(value = "xqGnJtnbFglTlm3", defaultValue = "") String xqGnJtnbFglTlm3,
                                                @RequestParam(value = "xqGnJtnbFglQt3", defaultValue = "") String xqGnJtnbFglQt3,
                                                @RequestParam(value = "xqGnJtwbGnfcb3", defaultValue = "") String xqGnJtwbGnfcb3,
                                                @RequestParam(value = "xqGnJtwbQt3", defaultValue = "") String xqGnJtwbQt3,
                                                @RequestParam(value = "xqGwJtnb3", defaultValue = "") String xqGwJtnb3,
                                                @RequestParam(value = "xqGwJtwb3", defaultValue = "") String xqGwJtwb3,
                                                @RequestParam(value = "xqTbzjyy", defaultValue = "") String xqTbzjyy,
                                                @RequestParam(value = "qnHj", defaultValue = "") String qnHj,
                                                @RequestParam(value = "qnGnCjnb", defaultValue = "") String qnGnCjnb,
                                                @RequestParam(value = "qnGnJtnbGlCy", defaultValue = "") String qnGnJtnbGlCy,
                                                @RequestParam(value = "qnGnJtnbGlCq", defaultValue = "") String qnGnJtnbGlCq,
                                                @RequestParam(value = "qnGnJtnbFglCy", defaultValue = "") String qnGnJtnbFglCy,
                                                @RequestParam(value = "qnGnJtnbFglCq", defaultValue = "") String qnGnJtnbFglCq,
                                                @RequestParam(value = "qnGnJtnbFglTlm", defaultValue = "") String qnGnJtnbFglTlm,
                                                @RequestParam(value = "qnGnJtnbFglQt", defaultValue = "") String qnGnJtnbFglQt,
                                                @RequestParam(value = "qnGnJtwbGnfcb", defaultValue = "") String qnGnJtwbGnfcb,
                                                @RequestParam(value = "qnGnJtwbQt", defaultValue = "") String qnGnJtwbQt,
                                                @RequestParam(value = "qnGwJtnb", defaultValue = "") String qnGwJtnb,
                                                @RequestParam(value = "qnGwJtwb", defaultValue = "") String qnGwJtwb,
                                                @RequestParam(value = "qnHj2", defaultValue = "") String qnHj2,
                                                @RequestParam(value = "qnGnCjnb2", defaultValue = "") String qnGnCjnb2,
                                                @RequestParam(value = "qnGnJtnbGlCy2", defaultValue = "") String qnGnJtnbGlCy2,
                                                @RequestParam(value = "qnGnJtnbGlCq2", defaultValue = "") String qnGnJtnbGlCq2,
                                                @RequestParam(value = "qnGnJtnbFglCy2", defaultValue = "") String qnGnJtnbFglCy2,
                                                @RequestParam(value = "qnGnJtnbFglCq2", defaultValue = "") String qnGnJtnbFglCq2,
                                                @RequestParam(value = "qnGnJtnbFglTlm2", defaultValue = "") String qnGnJtnbFglTlm2,
                                                @RequestParam(value = "qnGnJtnbFglQt2", defaultValue = "") String qnGnJtnbFglQt2,
                                                @RequestParam(value = "qnGnJtwbGnfcb2", defaultValue = "") String qnGnJtwbGnfcb2,
                                                @RequestParam(value = "qnGnJtwbQt2", defaultValue = "") String qnGnJtwbQt2,
                                                @RequestParam(value = "qnGwJtnb2", defaultValue = "") String qnGwJtnb2,
                                                @RequestParam(value = "qnGwJtwb2", defaultValue = "") String qnGwJtwb2,
                                                @RequestParam(value = "qnHj3", defaultValue = "") String qnHj3,
                                                @RequestParam(value = "qnGnCjnb3", defaultValue = "") String qnGnCjnb3,
                                                @RequestParam(value = "qnGnJtnbGlCy3", defaultValue = "") String qnGnJtnbGlCy3,
                                                @RequestParam(value = "qnGnJtnbGlCq3", defaultValue = "") String qnGnJtnbGlCq3,
                                                @RequestParam(value = "qnGnJtnbFglCy3", defaultValue = "") String qnGnJtnbFglCy3,
                                                @RequestParam(value = "qnGnJtnbFglCq3", defaultValue = "") String qnGnJtnbFglCq3,
                                                @RequestParam(value = "qnGnJtnbFglTlm3", defaultValue = "") String qnGnJtnbFglTlm3,
                                                @RequestParam(value = "qnGnJtnbFglQt3", defaultValue = "") String qnGnJtnbFglQt3,
                                                @RequestParam(value = "qnGnJtwbGnfcb3", defaultValue = "") String qnGnJtwbGnfcb3,
                                                @RequestParam(value = "qnGnJtwbQt3", defaultValue = "") String qnGnJtwbQt3,
                                                @RequestParam(value = "qnGwJtnb3", defaultValue = "") String qnGwJtnb3,
                                                @RequestParam(value = "qnGwJtwb3", defaultValue = "") String qnGwJtwb3,
                                                @RequestParam(value = "qnTbzjyy", defaultValue = "") String qnTbzjyy) {
//        BizRptMonthDto monthDto = null;

        Map<String, Object> model = new HashMap<>();
        model.put("rptMonthName", rptMonthName);
        model.put("deptName", deptName);
        model.put("createAt", JxlsUtils.dateFmt(new Date(Long.parseLong(createAt)), "yyyy-MM-dd"));
//        model.put("createAt", createAt);
        model.put("createManName", createManName);
        model.put("checkManName", checkManName);
        model.put("linkPhone", linkPhone);
        model.put("marketDesc", marketDesc);
        model.put("marketQ", marketQ);
        model.put("marketPlan", marketPlan);

        model.put("xqHj", xqHj);
        model.put("xqGnCjnb", xqGnCjnb);
        model.put("xqGnJtnbGlCy", xqGnJtnbGlCy);
        model.put("xqGnJtnbGlCq", xqGnJtnbFglCq);
        model.put("xqGnJtnbFglCy", xqGnJtnbFglCy);
        model.put("xqGnJtnbFglCq", xqGnJtnbFglCq);
        model.put("xqGnJtnbFglTlm", xqGnJtnbFglTlm);
        model.put("xqGnJtnbFglQt", xqGnJtnbFglQt);
        model.put("xqGnJtwbGnfcb", xqGnJtwbGnfcb);
        model.put("xqGnJtwbQt", xqGnJtwbQt);
        model.put("xqGwJtnb", xqGwJtnb);
        model.put("xqGwJtwb", xqGwJtwb);

        model.put("xqHj2", xqHj2);
        model.put("xqGnCjnb2", xqGnCjnb2);
        model.put("xqGnJtnbGlCy2", xqGnJtnbGlCy2);
        model.put("xqGnJtnbGlCq2", xqGnJtnbFglCq2);
        model.put("xqGnJtnbFglCy2", xqGnJtnbFglCy2);
        model.put("xqGnJtnbFglCq2", xqGnJtnbFglCq2);
        model.put("xqGnJtnbFglTlm2", xqGnJtnbFglTlm2);
        model.put("xqGnJtnbFglQt2", xqGnJtnbFglQt2);
        model.put("xqGnJtwbGnfcb2", xqGnJtwbGnfcb2);
        model.put("xqGnJtwbQt2", xqGnJtwbQt2);
        model.put("xqGwJtnb2", xqGwJtnb2);
        model.put("xqGwJtwb2", xqGwJtwb2);

        model.put("xqHj3", xqHj3);
        model.put("xqGnCjnb3", xqGnCjnb3);
        model.put("xqGnJtnbGlCy3", xqGnJtnbGlCy3);
        model.put("xqGnJtnbGlCq3", xqGnJtnbFglCq3);
        model.put("xqGnJtnbFglCy3", xqGnJtnbFglCy3);
        model.put("xqGnJtnbFglCq3", xqGnJtnbFglCq3);
        model.put("xqGnJtnbFglTlm3", xqGnJtnbFglTlm3);
        model.put("xqGnJtnbFglQt3", xqGnJtnbFglQt3);
        model.put("xqGnJtwbGnfcb3", xqGnJtwbGnfcb3);
        model.put("xqGnJtwbQt3", xqGnJtwbQt3);
        model.put("xqGwJtnb3", xqGwJtnb3);
        model.put("xqGwJtwb3", xqGwJtwb3);

        model.put("xqTbzjyy", xqTbzjyy);

        model.put("qnHj", qnHj);
        model.put("qnGnCjnb", qnGnCjnb);
        model.put("qnGnJtnbGlCy", qnGnJtnbGlCy);
        model.put("qnGnJtnbGlCq", qnGnJtnbGlCq);
        model.put("qnGnJtnbFglCy", qnGnJtnbFglCy);
        model.put("qnGnJtnbFglCq", qnGnJtnbFglCq);
        model.put("qnGnJtnbFglTlm", qnGnJtnbFglTlm);
        model.put("qnGnJtnbFglQt", qnGnJtnbFglQt);
        model.put("qnGnJtwbGnfcb", qnGnJtwbGnfcb);
        model.put("qnGnJtwbQt", qnGnJtwbQt);
        model.put("qnGwJtnb", qnGwJtnb);
        model.put("qnGwJtwb", qnGwJtwb);

        model.put("qnHj2", qnHj2);
        model.put("qnGnCjnb2", qnGnCjnb2);
        model.put("qnGnJtnbGlCy2", qnGnJtnbGlCy2);
        model.put("qnGnJtnbGlCq2", qnGnJtnbGlCq2);
        model.put("qnGnJtnbFglCy2", qnGnJtnbFglCy2);
        model.put("qnGnJtnbFglCq2", qnGnJtnbFglCq2);
        model.put("qnGnJtnbFglTlm2", qnGnJtnbFglTlm2);
        model.put("qnGnJtnbFglQt2", qnGnJtnbFglQt2);
        model.put("qnGnJtwbGnfcb2", qnGnJtwbGnfcb2);
        model.put("qnGnJtwbQt2", qnGnJtwbQt2);
        model.put("qnGwJtnb2", qnGwJtnb2);
        model.put("qnGwJtwb2", qnGwJtwb2);

        model.put("qnHj3", qnHj3);
        model.put("qnGnCjnb3", qnGnCjnb3);
        model.put("qnGnJtnbGlCy3", qnGnJtnbGlCy3);
        model.put("qnGnJtnbGlCq3", qnGnJtnbGlCq3);
        model.put("qnGnJtnbFglCy3", qnGnJtnbFglCy3);
        model.put("qnGnJtnbFglCq3", qnGnJtnbFglCq3);
        model.put("qnGnJtnbFglTlm3", qnGnJtnbFglTlm3);
        model.put("qnGnJtnbFglQt3", qnGnJtnbFglQt3);
        model.put("qnGnJtwbGnfcb3", qnGnJtwbGnfcb3);
        model.put("qnGnJtwbQt3", qnGnJtwbQt3);
        model.put("qnGwJtnb3", qnGwJtnb3);
        model.put("qnGwJtwb3", qnGwJtwb3);

        model.put("qnTbzjyy", qnTbzjyy);

        // String tmplName = "tmpl-biz-rpt-month.xlsx";
        JxlsUtils.exportExcel(response, rptMonthTmplUrl, tempurl, "市场月报" + rptMonthName + ".xlsx", model);
        return null;
    }

    @RequestMapping(value = "/indicator/laborScore/export", method = RequestMethod.GET)
    public AppMessage exportLaborScore(HttpServletResponse response,
                                       @RequestParam(value = "quarter", defaultValue = "") String quarter) {
        Map<String, Object> params = new HashMap<>();
        params.put("quarter", quarter);

        List<BizRptLaborScoreDto> result = new ArrayList<>();
        result = bizRptLaborScoreService.selectList(params);

        Map<String, Object> model = new HashMap<>();
        model.put("scoreList", result);

        // String tmplName = "tmpl-labor-score.xlsx";
        JxlsUtils.exportExcel(response, rptLaborScoreTmplUrl, tempurl, "劳动竞赛" + quarter + ".xlsx", model);
        return null;
    }

    @RequestMapping(value = "/indicator/kpi-sum/export", method = RequestMethod.GET)
    public AppMessage exportKpiSum(HttpServletResponse response,
                                   @RequestParam(value = "dateStart", defaultValue = "") String dateStart,
                                   @RequestParam(value = "dateEnd", defaultValue = "") String dateEnd,
                                   @RequestParam(value = "kpiYear", defaultValue = "") String kpiYear) {

        List<BizProjectYearlySumVo> result = new ArrayList<>();
        result = queryYearlyKpiSumImpl(dateStart, dateEnd, kpiYear);

        Map<String, Object> model = new HashMap<>();
        model.put("kpiList", result);

        // String tmplName = "tmpl-kpi-sum.xlsx";
        JxlsUtils.exportExcel(response, rptKpiSumTmplUrl, tempurl, "年度指标统计" + kpiYear + ".xlsx", model);
        return null;
    }
}

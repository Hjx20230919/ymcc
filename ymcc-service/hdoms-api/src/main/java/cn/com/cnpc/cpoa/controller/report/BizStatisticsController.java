package cn.com.cnpc.cpoa.controller.report;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.enums.DealStatusEnum;
import cn.com.cnpc.cpoa.enums.DealTypeEnum;
import cn.com.cnpc.cpoa.enums.SettlementStatusEnum;
import cn.com.cnpc.cpoa.enums.UserDataScopeEnum;
import cn.com.cnpc.cpoa.po.*;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.utils.*;
import cn.com.cnpc.cpoa.utils.excel.ExcelStatisticsUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.DealStatisticsVo;
import cn.com.cnpc.cpoa.vo.StatisticsDetailThreeVo;
import cn.com.cnpc.cpoa.vo.StatisticsDetailVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/21 9:41
 * @Description:统计报表
 */
@Api(tags = "统计报表管理")
@RestController
@RequestMapping("/hd/statistics")
public class BizStatisticsController extends BaseController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    UserService userService;

    @Autowired
    DealService dealService;

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;

    @Autowired
    private DeptService deptService;

    @RequestMapping(value = "/one", method = RequestMethod.GET)
    public AppMessage queryOne(@RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                               @RequestParam(value = "dealYear", defaultValue = "") String dealYear,
                               @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                               @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealYear", dealYear);
        params.put("dealIncome", dealIncome);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("firstDayOfYear", DateUtils.getThisYear());
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        return AppMessage.success(statisticsService.fillStatisticsOne(statisticsOnePos), "统计报表成功");
    }

    @RequestMapping(value = "/two", method = RequestMethod.GET)
    public AppMessage queryTwo(@RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                               @RequestParam(value = "dealYear", defaultValue = "") String dealYear,
                               @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                               @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd
    ) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealYear", dealYear);
        params.put("dealIncome", dealIncome);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("firstDayOfYear", DateUtils.getThisYear());
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        List<StatisticsTwoPo> statisticsTwoPos = statisticsService.selectListTwo(params);

        return AppMessage.success(statisticsTwoPos, "统计报表成功");
    }


    /**
     * 报表下载
     *
     * @return
     */
    @ApiOperation("合同履行审查审批表下载")
    @Log(logContent = "报表下载1", logModule = LogModule.STATISTICS, logType = LogType.OPERATION)
    @RequestMapping(value = "/exportOne", method = RequestMethod.GET)
    public AppMessage exportOne(HttpServletResponse response, @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                                @RequestParam(value = "dealYear", defaultValue = "") String dealYear,
                                @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                                @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealYear", dealYear);
        params.put("dealIncome", dealIncome);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("firstDayOfYear", DateUtils.getThisYear());
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 下载到浏览器
            // TODO 修改导出名称为 合同履行审查审批表，原名称为  合同收支报表
            String pdfName = "合同履行审查审批表.xls";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            ExcelStatisticsUtils.exportStatisticsOne(statisticsService.fillStatisticsOne(statisticsOnePos), outputStream, dealIncome);
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
        return null;
    }


    /**
     * 报表下载
     *
     * @return
     */
    @Log(logContent = "报表下载2", logModule = LogModule.STATISTICS, logType = LogType.OPERATION)
    @RequestMapping(value = "/exportTwo", method = RequestMethod.GET)
    public AppMessage exportTwo(HttpServletResponse response, @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                                @RequestParam(value = "dealYear", defaultValue = "") String dealYear,
                                @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                                @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealYear", dealYear);
        params.put("dealIncome", dealIncome);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("firstDayOfYear", DateUtils.getThisYear());
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        List<StatisticsTwoPo> statisticsTwoPos = statisticsService.selectListTwo(params);
        //获取模板
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 下载到浏览器
            String pdfName = "部门合同报表.xls";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            ExcelStatisticsUtils.exportStatisticsTwo(statisticsTwoPos, outputStream, dealIncome);
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
        return null;
    }

    /**
     * 集团合同下载
     *
     * @return
     */
    @Log(logContent = "报表明细查询导出", logModule = LogModule.STATISTICS, logType = LogType.OPERATION)
    @RequestMapping(value = "/detail/exportOne", method = RequestMethod.GET)
    public AppMessage export(HttpServletResponse response,
                             @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                             @RequestParam(value = "dealYear", defaultValue = "") String dealYear,
                             @RequestParam(value = "deptId", defaultValue = "") String deptId,
                             @RequestParam(value = "dealType", defaultValue = "") String dealType,
                             @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                             @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dealType", dealType);
        params.put("dataScope", dataScope);
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        params.put("firstDayOfYear", DateUtils.getThisYear());
        params.put("dealIncome", dealIncome);
        params.put("dealYear", dealYear);
        params.put("deptId", deptId);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);

        List<BizDealDto> sysDealDtos = dealService.selectListStatistics(params);
        String name = "";
        if (StringUtils.isNotEmpty(deptId)) {
            name = sysDealDtos.get(0).getDeptName();
        } else {
            name = DealTypeEnum.getEnumByKey(sysDealDtos.get(0).getDealType());
        }

        List<StatisticsDetailVo> dealVoS = new ArrayList<>();

        for (BizDealDto dealDto : sysDealDtos) {
            StatisticsDetailVo dealVo = new StatisticsDetailVo();
            //dealVo.setDealSignTime(DateUtils.);
            BeanUtils.copyBeanProp(dealVo, dealDto);
            dealVo.setDealType(DealTypeEnum.getEnumByKey(dealDto.getDealType()));
            dealVo.setDealStatus(DealStatusEnum.getEnumByKey(dealDto.getDealStatus()));
            dealVo.setSettleStatus(SettlementStatusEnum.getEnumByKey(dealDto.getSettleStatus()));
            dealVoS.add(dealVo);
        }
        String year = "currentYear".equals(dealYear) ? "本年" : "跨年";
        ExcelUtil<StatisticsDetailVo> util = new ExcelUtil<StatisticsDetailVo>(StatisticsDetailVo.class);
        return util.exportExcelBrowser(response, dealVoS, name + dealIncome + year + "报表明细");
    }

    /**
     * 度合同统计
     *
     * @param dealIncome
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/three", method = RequestMethod.GET)
    public AppMessage queryThree(@RequestParam(value = "dealIncome", defaultValue = "") String dealIncome) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        //List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        List<StatisticsThreePo> statisticsThreePos = statisticsService.selectListThree(params);
        StatisticsThreePo statisticsThreePo = new StatisticsThreePo();
        Integer sumCount = 0;
        Double dealValue = 0.0;
        Double settleNow = 0.0;
        for (StatisticsThreePo po : statisticsThreePos) {
            sumCount += po.getCount();
            dealValue = BigDecimalUtils.add(dealValue, po.getDealValue());
            settleNow = BigDecimalUtils.add(settleNow, po.getSettleNow());
        }
        statisticsThreePo.setYear("合计");
        statisticsThreePo.setCount(sumCount);
        statisticsThreePo.setDealValue(dealValue);
        statisticsThreePo.setSettleNow(settleNow);
        statisticsThreePos.add(statisticsThreePo);
        return AppMessage.success(statisticsThreePos, "查询统计报表成功");
    }

    /**
     * 年度合同统计-导出
     *
     * @return
     */
    @RequestMapping(value = "/exportThree", method = RequestMethod.GET)
    public AppMessage exportThree(HttpServletResponse response,
                                  @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        List<StatisticsThreePo> statisticsThreePos = statisticsService.selectListThree(params);
        StatisticsThreePo statisticsThreePo = new StatisticsThreePo();
        Integer sumCount = 0;
        Double dealValue = 0.0;
        Double settleNow = 0.0;
        for (StatisticsThreePo po : statisticsThreePos) {
            sumCount += po.getCount();
            dealValue = BigDecimalUtils.add(dealValue, po.getDealValue());
            settleNow = BigDecimalUtils.add(settleNow, po.getSettleNow());
        }
        statisticsThreePo.setYear("合计");
        statisticsThreePo.setCount(sumCount);
        statisticsThreePo.setDealValue(dealValue);
        statisticsThreePo.setSettleNow(settleNow);
        statisticsThreePos.add(statisticsThreePo);

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 下载到浏览器
            ExcelUtil<StatisticsThreePo> util = new ExcelUtil<StatisticsThreePo>(StatisticsThreePo.class);
            return util.exportExcelBrowser(response, statisticsThreePos, "年度合同统计表");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }


    @RequestMapping(value = "/four", method = RequestMethod.GET)
    public AppMessage queryFour(@RequestParam(value = "dealIncome", defaultValue = "") String dealIncome) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        List<StatisticsFourPo> statisticsFourPos = statisticsService.selectListFour(params);

        Integer count = 0;
        Double dealValue = 0.0;
        Double dealSettle = 0.0;
        Double dealSettleNow = 0.0;
        Double receivables = 0.0;

        for (StatisticsFourPo po : statisticsFourPos) {
            count += po.getCount();
            dealValue = BigDecimalUtils.add(dealValue, po.getDealValue());
            dealSettle = BigDecimalUtils.add(dealSettle, po.getDealSettle());
            dealSettleNow = BigDecimalUtils.add(dealSettleNow, po.getDealSettleNow());
            receivables = BigDecimalUtils.add(receivables, po.getReceivables());
        }
        StatisticsFourPo fourPo = new StatisticsFourPo();
        fourPo.setYear("合计");
        fourPo.setCount(count);
        fourPo.setDealValue(dealValue);
        fourPo.setDealSettle(dealSettle);
        fourPo.setDealSettleNow(dealSettleNow);
        fourPo.setReceivables(receivables);
        statisticsFourPos.add(fourPo);
        return AppMessage.success(statisticsFourPos, "查询统计报表成功");
    }

    /**
     * 跨年合同统计-导出
     *
     * @return
     */
    @RequestMapping(value = "/exportFour", method = RequestMethod.GET)
    public AppMessage exportFour(HttpServletResponse response,
                                 @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        //List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        List<StatisticsFourPo> statisticsFourPos = statisticsService.selectListFour(params);
        Integer count = 0;
        Double dealValue = 0.0;
        Double dealSettle = 0.0;
        Double dealSettleNow = 0.0;
        Double receivables = 0.0;

        for (StatisticsFourPo po : statisticsFourPos) {
            count += po.getCount();
            dealValue = BigDecimalUtils.add(dealValue, po.getDealValue());
            dealSettle = BigDecimalUtils.add(dealSettle, po.getDealSettle());
            dealSettleNow = BigDecimalUtils.add(dealSettleNow, po.getDealSettleNow());
            receivables = BigDecimalUtils.add(receivables, po.getReceivables());
        }
        StatisticsFourPo fourPo = new StatisticsFourPo();
        fourPo.setYear("合计");
        fourPo.setCount(count);
        fourPo.setDealValue(dealValue);
        fourPo.setDealSettle(dealSettle);
        fourPo.setDealSettleNow(dealSettleNow);
        fourPo.setReceivables(receivables);
        statisticsFourPos.add(fourPo);

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 下载到浏览器
            String pdfName = "跨年合同统计表";

            ExcelUtil<StatisticsFourPo> util = new ExcelUtil<StatisticsFourPo>(StatisticsFourPo.class);
            return util.exportExcelBrowser(response, statisticsFourPos, pdfName);
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }


    @RequestMapping(value = "/five", method = RequestMethod.GET)
    public AppMessage queryFive(@RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
            , @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart
            , @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        List<StatisticsFivePo> statisticsFivePos = statisticsService.selectListFive(params);
        Integer count = 0;
        Double dealValue = 0.0;
        Double dealSettle = 0.0;
        for (StatisticsFivePo po : statisticsFivePos) {
            count += po.getCount();
            dealValue = BigDecimalUtils.add(dealValue, po.getDealValue());
            dealSettle = BigDecimalUtils.add(dealSettle, po.getDealSettle());
        }
        StatisticsFivePo fivePo = new StatisticsFivePo();
        fivePo.setDealType("合计");
        fivePo.setCount(count);
        fivePo.setDealValue(dealValue);
        fivePo.setDealSettle(dealSettle);
        statisticsFivePos.add(fivePo);

        return AppMessage.success(statisticsFivePos, "查询统计报表成功");
    }

    /**
     * 跨年合同统计-导出
     *
     * @return
     */
    @RequestMapping(value = "/exportFive", method = RequestMethod.GET)
    public AppMessage exportFive(HttpServletResponse response
            , @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
            , @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart
            , @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        List<StatisticsFivePo> statisticsFivePos = statisticsService.selectListFive(params);
        Integer count = 0;
        Double dealValue = 0.0;
        Double dealSettle = 0.0;
        for (StatisticsFivePo po : statisticsFivePos) {
            po.setDealType(DealTypeEnum.getEnumByKey(po.getDealType()));
            count += po.getCount();
            dealValue = BigDecimalUtils.add(dealValue, po.getDealValue());
            dealSettle = BigDecimalUtils.add(dealSettle, po.getDealSettle());
        }
        StatisticsFivePo fivePo = new StatisticsFivePo();
        fivePo.setDealType("合计");
        fivePo.setCount(count);
        fivePo.setDealValue(dealValue);
        fivePo.setDealSettle(dealSettle);
        statisticsFivePos.add(fivePo);
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<StatisticsFivePo> util = new ExcelUtil<StatisticsFivePo>(StatisticsFivePo.class);
            return util.exportExcelBrowser(response, statisticsFivePos, "签定合同统计表");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    /**
     * 年度合同统计-详情
     *
     * @param dealIncome
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail/three", method = RequestMethod.GET)
    public AppMessage queryDetailThree(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                       @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
            , @RequestParam(value = "year", defaultValue = "") String year) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        params.put("year", year);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        PageHelper.startPage(pageNo, pageSize);

        //List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        List<StatisticsDetailThreeVo> dealVoS = statisticsService.selectListDetailThree(params);
        for (StatisticsDetailThreeVo vo : dealVoS) {
            vo.setDealType(DealTypeEnum.getEnumByKey(vo.getDealType()));
            vo.setDealStatus(DealStatusEnum.getEnumByKey(vo.getDealStatus()));
        }
        TableDataInfo dataTable = getDataTable(dealVoS);
        return AppMessage.success(dataTable, "查询统计报表详情成功");
    }

    /**
     * 跨年合同统计-详情
     *
     * @param dealIncome
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail/four", method = RequestMethod.GET)
    public AppMessage queryDetailFour(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize, @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
            , @RequestParam(value = "year", defaultValue = "") String year) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        params.put("year", year);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        PageHelper.startPage(pageNo, pageSize);
        //List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        List<StatisticsDetailVo> dealVoS = statisticsService.selectListDetailFour(params);
        for (StatisticsDetailVo vo : dealVoS) {
            vo.setDealType(DealTypeEnum.getEnumByKey(vo.getDealType()));
            vo.setDealStatus(DealStatusEnum.getEnumByKey(vo.getDealStatus()));
        }
        TableDataInfo dataTable = getDataTable(dealVoS);
        return AppMessage.success(dataTable, "查询统计报表详情成功");
    }

    /**
     * 签订合同统计-详情
     *
     * @param dealIncome
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail/five", method = RequestMethod.GET)
    public AppMessage queryDetailFive(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize, @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
            , @RequestParam(value = "dealType", defaultValue = "") String dealType
            , @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart
            , @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        params.put("dealType", dealType);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        PageHelper.startPage(pageNo, pageSize);
        //List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        List<StatisticsDetailVo> dealVoS = statisticsService.selectListDetailFive(params);
        for (StatisticsDetailVo vo : dealVoS) {
            vo.setDealType(DealTypeEnum.getEnumByKey(vo.getDealType()));
            vo.setDealStatus(DealStatusEnum.getEnumByKey(vo.getDealStatus()));
        }
        TableDataInfo dataTable = getDataTable(dealVoS);
        return AppMessage.success(dataTable, "查询统计报表详情成功");
    }

    /**
     * 年度合同统计-详情
     *
     * @param dealIncome
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail/exportThree", method = RequestMethod.GET)
    public AppMessage exportDetailThree(HttpServletResponse response, @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
            , @RequestParam(value = "year", defaultValue = "") String year) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        params.put("year", year);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        //List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        List<StatisticsDetailThreeVo> dealVoS = statisticsService.selectListDetailThree(params);
        for (StatisticsDetailThreeVo vo : dealVoS) {
            vo.setDealType(DealTypeEnum.getEnumByKey(vo.getDealType()));
            vo.setDealStatus(DealStatusEnum.getEnumByKey(vo.getDealStatus()));
            vo.setSettleStatus(SettlementStatusEnum.getEnumByKey(vo.getSettleStatus()));
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<StatisticsDetailThreeVo> util = new ExcelUtil<StatisticsDetailThreeVo>(StatisticsDetailThreeVo.class);
            return util.exportExcelBrowser(response, dealVoS, "年度合同详情表");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    /**
     * 跨年合同统计-详情
     *
     * @param dealIncome
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail/exportFour", method = RequestMethod.GET)
    public AppMessage exportDetailFour(HttpServletResponse response, @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
            , @RequestParam(value = "year", defaultValue = "") String year) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        params.put("year", year);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        //List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        List<StatisticsDetailVo> dealVoS = statisticsService.selectListDetailFour(params);
        for (StatisticsDetailVo vo : dealVoS) {
            vo.setDealType(DealTypeEnum.getEnumByKey(vo.getDealType()));
            vo.setDealStatus(DealStatusEnum.getEnumByKey(vo.getDealStatus()));
            vo.setSettleStatus(SettlementStatusEnum.getEnumByKey(vo.getSettleStatus()));
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<StatisticsDetailVo> util = new ExcelUtil<StatisticsDetailVo>(StatisticsDetailVo.class);
            return util.exportExcelBrowser(response, dealVoS, "跨年合同详情表");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    /**
     * 签订合同统计-详情
     *
     * @param dealIncome
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/detail/exportFive", method = RequestMethod.GET)
    public AppMessage exportDetailFive(HttpServletResponse response, @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
            , @RequestParam(value = "dealType", defaultValue = "") String dealType
            , @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart
            , @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();
        params.put("dealIncome", dealIncome);
        params.put("dealType", dealType);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        //List<StatisticsOnePo> statisticsOnePos = statisticsService.selectListOne(params);
        List<StatisticsDetailVo> dealVoS = statisticsService.selectListDetailFive(params);
        for (StatisticsDetailVo vo : dealVoS) {
            vo.setDealType(DealTypeEnum.getEnumByKey(vo.getDealType()));
            vo.setDealStatus(DealStatusEnum.getEnumByKey(vo.getDealStatus()));
            vo.setSettleStatus(SettlementStatusEnum.getEnumByKey(vo.getSettleStatus()));
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<StatisticsDetailVo> util = new ExcelUtil<StatisticsDetailVo>(StatisticsDetailVo.class);
            return util.exportExcelBrowser(response, dealVoS, "签订合同详情表");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }


    @ApiOperation("导出所有合同履行")
    @RequestMapping(value = "/exportStatistics", method = RequestMethod.GET)
    public AppMessage exportStatistics(HttpServletResponse response,
                                       @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                                       @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                       @RequestParam(value = "statType", defaultValue = "") String statType,
                                       @RequestParam(value = "deptId", defaultValue = "") String deptId) throws Exception {

        //TODO 按照当前登录用户部门限制数据
        Map<String, Object> params = new HashMap<>();

        SysUserDto sysUserDto = userService.selectByKey(ServletUtils.getSessionUserId());
        if (!Constants.sysUsers.containsKey(sysUserDto.getUserRole())) {
            params.put("deptId", sysUserDto.getDeptId());
        }else{
            params.put("deptId", deptId);
        }

        //params.put("dealYear",dealYear);
        params.put("dealIncome", dealIncome);
        params.put("dealName", dealName);
        params.put("statType", statType);
        Map<String, List<DealStatisticsVo>> dealStatisticsMap = bizDealStatisticsService.selectPreDealStatistics(params);

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 下载到浏览器
            String pdfName = DateUtils.getThisYear2() + "年安检院和科特合同（含跨年）合同履行结算情况统计表（" + dealIncome + "合同）.xlsx";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            if ("收入".equals(dealIncome)) {
                ExcelStatisticsUtils.exportInComeStatistics(dealStatisticsMap, outputStream, dealIncome);
            } else {
                ExcelStatisticsUtils.exportOutComeStatistics(dealStatisticsMap, outputStream, dealIncome);
            }

        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
        return null;
    }

    @RequestMapping(value = "/exportStatisticsSum", method = RequestMethod.GET)
    public AppMessage exportStatisticsSum(HttpServletResponse response,
                                          @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                                          @RequestParam(value = "deptId", defaultValue = "") String deptId) throws Exception {

        Map<String, Object> params = new HashMap<>();
        //   params.put("dealYear",dealYear);
        params.put("dealIncome", dealIncome);
        params.put("deptId", deptId);
        Map<String, List<DealStatisticsVo>> dealStatisticsMap = bizDealStatisticsService.selectPreDealStatisticsSum(params);

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 下载到浏览器
            String pdfName = DateUtils.getThisYear2() + "合同（含跨年）履行结算情况汇总表（" + dealIncome + "合同）.xlsx";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            if ("收入".equals(dealIncome)) {
                ExcelStatisticsUtils.exportInComeStatisticsSum(dealStatisticsMap, outputStream, dealIncome);
            } else {
                ExcelStatisticsUtils.exportOutComeStatisticsSum(dealStatisticsMap, outputStream, dealIncome);
            }

        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
        return null;
    }

    @ApiOperation("根据部门单位导出合同履行")
    @RequestMapping(value = "/exportStatisticsByDept", method = RequestMethod.GET)
    public AppMessage exportStatisticsByDept(HttpServletResponse response,
                                       @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                                       @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                       @RequestParam(value = "statType", defaultValue = "") String statType
                                       ) throws Exception {

        HashMap<String, Map<String, List<DealStatisticsVo>>> dataMap = new HashMap<>(16);
        List<SysDeptDto> deptDtos = deptService.selectAll();
        for (SysDeptDto deptDto : deptDtos) {
            Map<String, Object> params = new HashMap<>();
            params.put("deptId", deptDto.getDeptId());
            params.put("dealIncome", dealIncome);
            params.put("dealName", dealName);
            params.put("statType", statType);
            Map<String, List<DealStatisticsVo>> dealStatisticsMap = bizDealStatisticsService.selectPreDealStatistics(params);
            if (dealStatisticsMap.size() > 0){
                dataMap.put(deptDto.getDeptName(),dealStatisticsMap);
            }
        }

        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            // 下载到浏览器
            String pdfName = DateUtils.getThisYear2() + "年安检院和科特合同（含跨年）合同履行结算情况统计表（" + dealIncome + "合同）.xlsx";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), "iso-8859-1"));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            if ("收入".equals(dealIncome)) {
                ExcelStatisticsUtils.exportInComeStatisticsByDept(dataMap, outputStream, dealIncome);
            } else {
                ExcelStatisticsUtils.exportOutComeStatisticsByDept(dataMap, outputStream, dealIncome);
            }

        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }


        return null;
    }

}

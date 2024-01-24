package cn.com.cnpc.cpoa.controller.prodsys;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.service.prodsys.report.FusionReportV2Service;
import cn.com.cnpc.cpoa.vo.prodsys.report.*;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: sirjaime
 * @create: 2020-11-01 17:19
 */
@RestController
@RequestMapping("/fusion/report/v2")
public class FusionReportV2Controller extends BaseController {

    @Autowired
    FusionReportV2Service fusionReportV2Service;

    @RequestMapping(value = "/marketAreaAnalysis", method = RequestMethod.GET)
    public AppMessage marketAreaAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                         @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);

        List<RptMarketAreaVO> retval = fusionReportV2Service.queryRptMarketArea(params);

        return AppMessage.success(retval, "统计报表成功");
    }

    @RequestMapping(value = "/contractTypeAnalysis", method = RequestMethod.GET)
    public AppMessage contractTypeAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                           @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);

        List<RptContractTypeVO> retval = fusionReportV2Service.queryRptContractType(params);

        return AppMessage.success(retval, "统计报表成功");
    }

    @RequestMapping(value = "/clasTypeAnalysis", method = RequestMethod.GET)
    public AppMessage clasWorkType1Analysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                            @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);

        List<RptClasTypeVO> retval = fusionReportV2Service.queryRptClasType(params);

        return AppMessage.success(retval, "统计报表成功");
    }

    @RequestMapping(value = "/clientDeptAnalysis", method = RequestMethod.GET)
    public AppMessage clientDeptAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                         @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);

        List<RptClientDeptVO> retval = fusionReportV2Service.queryRptClientDept(params);

        return AppMessage.success(retval, "统计报表成功");
    }

    @RequestMapping(value = "/clientSubDeptAnalysis", method = RequestMethod.GET)
    public AppMessage clientSubDeptAnalysis(@RequestParam(value = "yearMonthStart", defaultValue = "") String yearMonthStart,
                                            @RequestParam(value = "yearMonthEnd", defaultValue = "") String yearMonthEnd,
                                            @RequestParam(value = "parentName", defaultValue = "") String parentName) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("yearMonthStart", yearMonthStart);
        params.put("yearMonthEnd", yearMonthEnd);
        params.put("deptName", parentName);

        List<RptClientSubDeptVO> retval = fusionReportV2Service.queryRptClientSubDept(params);

        return AppMessage.success(retval, "统计报表成功");
    }


}

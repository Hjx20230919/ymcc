package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.contractor.ContReviewSummaryPo;
import cn.com.cnpc.cpoa.po.contractor.ContReviewTaskPo;
import cn.com.cnpc.cpoa.service.constractor.ContReviewSummaryService;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-01  16:32·
 * @Description:考核汇总评价管理
 * @Version: 1.0
 */
@Api(tags = "考核汇总评价管理")
@RestController
@RequestMapping("/hd/reviewSummary")
public class ContReivewSummaryController extends BaseController {

    @Autowired
    private ContReviewSummaryService summaryService;

    @ApiOperation("查询考核汇总评价")
    @ApiImplicitParams({@ApiImplicitParam(name = "reviewYear",value = "项目年份"),
            @ApiImplicitParam(name = "taskStatus",value = "考评状态"),
            @ApiImplicitParam(name = "deptName",value = "考评部门"),
            @ApiImplicitParam(name = "createAtStart",value = "考评开始时间"),
            @ApiImplicitParam(name = "evalConclusion",value = "评价结果"),
            @ApiImplicitParam(name = "createAtEnd",value = "考评结束时间")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectReviewSummary(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                          @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                          @RequestParam(value = "reviewYear", defaultValue = "") String reviewYear,
                                          @RequestParam(value = "taskStatus", defaultValue = "") String taskStatus,
                                          @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                          @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                          @RequestParam(value = "evalConclusion", defaultValue = "") String evalConclusion,
                                          @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        HashMap<String, Object> param = new HashMap<>(8);
        param.put("reviewYear",reviewYear);
        param.put("taskStatus",taskStatus);
        param.put("deptName",deptName);
        param.put("createAtStart",createAtStart);
        param.put("createAtEnd",createAtEnd);
        param.put("evalConclusion",evalConclusion);
        HashMap<String, Object> dataMap = summaryService.selectReviewSummary(param,pageNum,pageSize);
        return AppMessage.success(getDataTable((List<ContReviewSummaryPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询考核汇总评价");
    }

    @ApiOperation("导出核汇总评价")
    @ApiImplicitParams({@ApiImplicitParam(name = "reviewYear",value = "项目年份"),
            @ApiImplicitParam(name = "taskStatus",value = "考评状态"),
            @ApiImplicitParam(name = "deptName",value = "考评部门"),
            @ApiImplicitParam(name = "createAtStart",value = "考评开始时间"),
            @ApiImplicitParam(name = "evalConclusion",value = "评价结果"),
            @ApiImplicitParam(name = "createAtEnd",value = "考评结束时间")})
    @RequestMapping(value = "/export",method = RequestMethod.GET)
    public void exportReviewSummary(HttpServletResponse response,
                                          @RequestParam(value = "reviewYear", defaultValue = "") String reviewYear,
                                          @RequestParam(value = "taskStatus", defaultValue = "") String taskStatus,
                                          @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                          @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                          @RequestParam(value = "evalConclusion", defaultValue = "") String evalConclusion,
                                          @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        HashMap<String, Object> param = new HashMap<>(8);
        param.put("reviewYear",reviewYear);
        param.put("taskStatus",taskStatus);
        param.put("deptName",deptName);
        param.put("createAtStart",createAtStart);
        param.put("createAtEnd",createAtEnd);
        param.put("evalConclusion",evalConclusion);
        summaryService.exportReviewSummary(param,response);
    }

    @ApiOperation("计算汇总评价")
    @RequestMapping(value = "/summary",method = RequestMethod.POST)
    public AppMessage countReviewSummary(@RequestBody List<String> reviewSummaryIds){
        return summaryService.countReviewSummary(reviewSummaryIds);
    }

    @ApiOperation("查询评价汇总表")
    @RequestMapping(value = "/accessSummary",method = RequestMethod.GET)
    public AppMessage selectAccessSummary(@RequestParam(value = "reviewYear", defaultValue = "") String reviewYear,
                                          @RequestParam(value = "accessLevel", defaultValue = "") String accessLevel){
        HashMap<String, Object> param = new HashMap<>(4);
        param.put("reviewYear",reviewYear);
        param.put("accessLevel",accessLevel);
        return summaryService.selectAccessSummary(param);
    }

    @ApiOperation("查询评价汇总详情")
    @RequestMapping(value = "/detail/{reviewSummaryId}",method = RequestMethod.GET)
    public AppMessage selectSummaryDetail(@PathVariable("reviewSummaryId")String reviewSummaryId){
        return summaryService.selectSummaryDetail(reviewSummaryId);
    }

}

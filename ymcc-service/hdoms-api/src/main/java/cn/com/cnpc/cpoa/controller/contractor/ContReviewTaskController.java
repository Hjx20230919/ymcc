package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.contractor.ContReviewTaskPo;
import cn.com.cnpc.cpoa.service.constractor.ContReviewTaskService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContReviewTaskVo;
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
 * @CreateTime: 2022-05-26  17:01
 * @Description: TODO   考评任务管理
 * @Version: 1.0
 */
@Api(tags = "考评任务管理")
@RestController
@RequestMapping("/hd/contReviewTask")
public class ContReviewTaskController extends BaseController {

    @Autowired
    private ContReviewTaskService reviewTaskService;

    @ApiOperation("查询考评任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "reviewYear",value = "项目年份"),
            @ApiImplicitParam(name = "taskStatus",value = "考评状态"),
            @ApiImplicitParam(name = "deptName",value = "考评部门"),
            @ApiImplicitParam(name = "createAtStart",value = "考评开始时间"),
            @ApiImplicitParam(name = "createAtEnd",value = "考评结束时间")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContReviewTaskByMap(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                @RequestParam(value = "reviewYear", defaultValue = "") String reviewYear,
                                                @RequestParam(value = "taskStatus", defaultValue = "") String taskStatus,
                                                @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                                @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                                @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd){
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("reviewYear",reviewYear);
        param.put("taskStatus",taskStatus);
        param.put("deptName",deptName);
        param.put("createAtStart",createAtStart);
        param.put("createAtEnd",createAtEnd);
        HashMap<String, Object> dataMap = reviewTaskService.selectContReviewTaskByMap(param,pageNum,pageSize);
        return AppMessage.success(getDataTable((List<ContReviewTaskPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询考评任务成功");
    }

    @ApiOperation("导出考评任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "reviewYear",value = "项目年份"),
            @ApiImplicitParam(name = "taskStatus",value = "考评状态"),
            @ApiImplicitParam(name = "deptName",value = "考评部门"),
            @ApiImplicitParam(name = "createAtStart",value = "考评开始时间"),
            @ApiImplicitParam(name = "createAtEnd",value = "考评结束时间")})
    @RequestMapping(value ="/export", method = RequestMethod.GET)
    public void exportContReviewTaskByMap(HttpServletResponse response,
                                          @RequestParam(value = "reviewYear", defaultValue = "") String reviewYear,
                                          @RequestParam(value = "taskStatus", defaultValue = "") String taskStatus,
                                          @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                          @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                          @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd){
        HashMap<String, Object> param = new HashMap<>(8);
        param.put("reviewYear",reviewYear);
        param.put("taskStatus",taskStatus);
        param.put("deptName",deptName);
        param.put("createAtStart",createAtStart);
        param.put("createAtEnd",createAtEnd);
        reviewTaskService.exportContReviewTaskByMap(param,response);
    }

    @ApiOperation("生成考评任务")
    @RequestMapping(value = "/{reviewTaskId}",method = RequestMethod.GET)
    public AppMessage createContReviewTask(@PathVariable("reviewTaskId")String reviewTaskId){
        return reviewTaskService.createContReviewTask(reviewTaskId);
    }

    @ApiOperation("修改考评状态")
    @RequestMapping(value = "/update/{reviewTaskId}",method = RequestMethod.GET)
    public AppMessage updateContReviewTask(@PathVariable("reviewTaskId")String reviewTaskId,@RequestParam("taskStatus") String taskStatus){
        return reviewTaskService.updateContReviewTask(reviewTaskId,taskStatus);
    }

    @ApiOperation("考核评价")
    @RequestMapping(value = "/accessEvaluate",method = RequestMethod.POST)
    public AppMessage accessEvaluate(@RequestBody ContReviewTaskVo vo){
        return reviewTaskService.accessEvaluate(vo);
    }

    @ApiOperation("提交审核")
    @RequestMapping(value = "/submitAudit/{reviewTaskId}",method = RequestMethod.GET)
    public AppMessage submitAudit(@PathVariable("reviewTaskId")String reviewTaskId) throws Exception{
        return reviewTaskService.submitAudit(reviewTaskId);
    }

    /**
     * 查询基层单位审核流程
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("查询基层单位审核流程")
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public AppMessage selectAuditItem(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                      @RequestParam(value = "taskStatus",defaultValue = "") String taskStatus,
                                      @RequestParam(value = "evalAtStart",defaultValue = "") String evalAtStart,
                                      @RequestParam(value = "evalAtEnd",defaultValue = "") String evalAtEnd,
                                      @RequestParam(value = "reviewYear",defaultValue = "") String reviewYear,
                                      @RequestParam(value = "deptId",defaultValue = "") String deptId,
                                      @RequestParam(value = "contName",defaultValue = "") String contName,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize){
        String userId = ServletUtils.getSessionUserId();
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("userId",userId);
        param.put("taskStatus",taskStatus);
        param.put("evalAtStart",evalAtStart);
        param.put("evalAtEnd",evalAtEnd);
        param.put("reviewYear",reviewYear);
        param.put("deptId",deptId);
        param.put("contName",contName);
        HashMap<String, Object> dataMap = reviewTaskService.selectAuditItem(pageNum, pageSize, param);
        return AppMessage.success(getDataTable((List<ContReviewTaskPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询基层单位审核流程");
    }

    @ApiOperation("初始化历史数据")
    @RequestMapping(value = "/initialize",method = RequestMethod.GET)
    public void initialize() {
        reviewTaskService.initialize();
    }

    @ApiOperation("删除考核任务")
    @RequestMapping(value = "/delete/{reviewTaskId}",method = RequestMethod.DELETE)
    public AppMessage deleteAudit(@PathVariable("reviewTaskId")String reviewTaskId) {
        return reviewTaskService.deleteAudit(reviewTaskId);
    }

}

package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.po.contractor.ContReviewOfficeDetailPo;
import cn.com.cnpc.cpoa.po.contractor.ContReviewTaskPo;
import cn.com.cnpc.cpoa.service.constractor.ContReviewOfficeDetailService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-30  16:48
 * @Description:职能部门考核管理
 * @Version: 1.0
 */
@Api(tags = "职能部门考核管理")
@RestController
@RequestMapping("/hd/officeDetail")
public class ContReviewOfficeDetailController extends BaseController {

    @Autowired
    private ContReviewOfficeDetailService officeDetailService;

    @ApiOperation("查询职能部门考评任务")
    @ApiImplicitParams({@ApiImplicitParam(name = "reviewYear",value = "项目年份"),
            @ApiImplicitParam(name = "taskStatus",value = "考评状态"),
            @ApiImplicitParam(name = "permission",value = "不传默认查询当前部门的数据，如需查询全部数据，需传值ALL"),
            @ApiImplicitParam(name = "deptName",value = "考评部门"),
            @ApiImplicitParam(name = "createAtStart",value = "考评开始时间"),
            @ApiImplicitParam(name = "createAtEnd",value = "考评结束时间")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContReviewTaskByMap(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                                @RequestParam(value = "permission", defaultValue = "") String permission,
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
        HashMap<String, Object> dataMap = officeDetailService.selectContReviewOfficeDetailByMap(param,pageNum,pageSize,permission);
        return AppMessage.success(getDataTable((List<ContReviewOfficeDetailPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询考评任务成功");
    }

    @ApiOperation("导出职能部门考评任务")
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
                                          @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("reviewYear", reviewYear);
        param.put("taskStatus", taskStatus);
        param.put("deptName", deptName);
        param.put("createAtStart", createAtStart);
        param.put("createAtEnd", createAtEnd);
        officeDetailService.exportContReviewTaskByMap(param, response);
    }

    @ApiOperation("职能部门考评")
    @ApiImplicitParams({@ApiImplicitParam(name = "officeDetailId",value = "任务id"),
            @ApiImplicitParam(name = "totalScore",value = "分数"),
            @ApiImplicitParam(name = "evalConclusion",value = "扣分原因"),
            @ApiImplicitParam(name = "notes",value = "备注")})
    @RequestMapping(value ="/{officeDetailId}", method = RequestMethod.GET)
    public AppMessage officeDetail(
                            @PathVariable("officeDetailId") String officeDetailId,
                            @RequestParam(value = "totalScore") String totalScore,
                            @RequestParam(value = "evalConclusion", defaultValue = "") String evalConclusion,
                            @RequestParam(value = "notes", defaultValue = "") String notes) throws Exception{
        return officeDetailService.officeDetail(officeDetailId,totalScore,evalConclusion,notes);
    }

    /**
     * 查询职能部门考核确认
     *
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation("查询职能部门考核确认")
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public AppMessage selectAuditItem(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "taskStatus",defaultValue = "") String taskStatus,
                                                   @RequestParam(value = "evalAtStart",defaultValue = "") String evalAtStart,
                                                   @RequestParam(value = "evalAtEnd",defaultValue = "") String evalAtEnd,
                                                   @RequestParam(value = "reviewYear",defaultValue = "") String reviewYear,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize){
        String userId = ServletUtils.getSessionUserId();
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("userId",userId);
        param.put("taskStatus",taskStatus);
        param.put("evalAtStart",evalAtStart);
        param.put("evalAtEnd",evalAtEnd);
        param.put("reviewYear",reviewYear);
        HashMap<String, Object> dataMap = officeDetailService.selectAuditItem(pageNum, pageSize, param);
        return AppMessage.success(getDataTable((List<ContReviewOfficeDetailPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询职能部门考核确认");
    }

    /**
     * 职能部门考核确认
     *
     * @return
     */
    @ApiOperation("职能部门考核确认")
    @ApiImplicitParam(name = "officeDetailIds",value = "选中考核任务的officeDetailId，多个已,分隔")
    @RequestMapping(value = "/officeAudit", method = RequestMethod.GET)
    public AppMessage auditOffice(@RequestParam(value = "status", defaultValue = "")String status,
                                  @RequestParam(value = "officeDetailIds", defaultValue = "")String officeDetailIds) throws Exception{
        //获取需要审批的officeDetailId
        List<String> list = Arrays.stream(officeDetailIds.split(",")).collect(Collectors.toList());
        return officeDetailService.auditOffice(status,list);
    }


}

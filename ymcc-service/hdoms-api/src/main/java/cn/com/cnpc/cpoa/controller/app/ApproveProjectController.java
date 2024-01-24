package cn.com.cnpc.cpoa.controller.app;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.KsVo;
import cn.com.cnpc.cpoa.vo.project.ProActivitiItemVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Api(tags = "app合同立项管理")
@RestController
@RequestMapping("/hd/appproject/appproProject")
public class ApproveProjectController extends BaseController {
    @Autowired
    ProjProjectService projProjectService;
    @ApiOperation("项目立项详情")
    @RequestMapping(value = "/appdetails", method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectProjectDetails(@RequestParam(value = "projId", defaultValue = "") String projId) {
        Map<String, Object> params = new HashMap<>();
        params.put("projId", projId);
        params.put("selContId", projId);
        List<ProjProjectVo> list = projProjectService.selectProjectDetails(params);
        return AppMessage.success(list, "查询立项详情成功");
    }

    @ApiOperation("查询流程中事项")
    @ApiImplicitParams({@ApiImplicitParam(name = "/deptName",value = "承办单位"),
            @ApiImplicitParam(name = "projName",value = "项目名称"),
            @ApiImplicitParam(name = "createAtStart",value = "创建开始时间"),
            @ApiImplicitParam(name = "createAtEnd",value = "创建结束时间"),
            @ApiImplicitParam(name = "selContType",value = "选商方式"),
            @ApiImplicitParam(name = "projPhase",value = "项目阶段")
    })
    @RequestMapping(value = "/process",method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectActivitiItem(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "projName", defaultValue = "") String projName,
                                         @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                         @RequestParam(value = "status", defaultValue = "") String status,
                                         @RequestParam(value = "userName", defaultValue = "") String userName,
                                         @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                         @RequestParam(value = "projPhase", defaultValue = "") String projPhase,
                                         @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                         @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("selContType", selContType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        params.put("projPhase", projPhase);
        params.put("status", status);

        setDataGrade(ServletUtils.getSessionUserId(), params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);


        List<ProActivitiItemVo> proActivitiItemVos = projProjectService.selectActivitiItem(params);
        TableDataInfo dataTable = getDataTable(proActivitiItemVos,page.getTotal());
        return AppMessage.success(dataTable, "查询流程中事项成功");
    }

    /**
     * 查询待办事项
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @ApiOperation("查询待办事项")
    @RequestMapping(value = "/audit", method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectAuditItem(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                      @RequestParam(value = "projName", defaultValue = "") String projName,
                                      @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                      @RequestParam(value = "status", defaultValue = "") String status,
                                      @RequestParam(value = "userName", defaultValue = "") String userName,
                                      @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                      @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                      @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("selContType", selContType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);

        params.put("userId", ServletUtils.getSessionUserId());

        PageHelper.startPage(pageNo, pageSize);
        List<ProActivitiItemVo> list;

        if (ContractorConstant.AuditType.AUDITING.equals(status)) {
            list = projProjectService.selectAuditItem(params);
        } else if (ContractorConstant.AuditType.AUDITED.equals(status)) {
            list = projProjectService.selectAuditedItem(params);
        }else{
            list = projProjectService.selectAuditedItem(params);
        }
        TableDataInfo dataTable = getDataTable(list);
        return AppMessage.success(dataTable, "查询待办事项成功");
    }

    @ApiOperation("查询待办事项和已办事项的数量")
    @RequestMapping(value = "/auditnum", method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectAuditItem1(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                      @RequestParam(value = "status", defaultValue = "") String status
                                      ) {
        Map<String, Object> params = new HashMap<>();
//        params.put("projName", projName);
//        params.put("selContType", selContType);
//        params.put("userName", userName);
//        params.put("deptName", deptName);
//        params.put("createAtStart", createAtStart);
//        params.put("createAtEnd", createAtEnd);
//        params.put("status", "auditing");
        params.put("userId", ServletUtils.getSessionUserId());

        Map<String, Object> params1 = new HashMap<>();
//        params1.put("projName", projName);
//        params1.put("selContType", selContType);
//        params1.put("userName", userName);
//        params1.put("deptName", deptName);
//        params1.put("createAtStart", createAtStart);
//        params1.put("createAtEnd", createAtEnd);
//        params.put("status", "auditing");
        params1.put("userId", ServletUtils.getSessionUserId());

        PageHelper.startPage(pageNo, pageSize);



        List<ProActivitiItemVo> list = projProjectService.selectAuditItem(params1);

        List<ProActivitiItemVo> proActivitiItemVos = projProjectService.selectAuditedItem(params1);
        KsVo ksVo = new KsVo();
        int backlognum = list.size();
        ksVo.setBacklognum(backlognum);
        int repliednum = proActivitiItemVos.size();
        ksVo.setRepliednum(repliednum);
        return AppMessage.success(ksVo, "查询待办事项成功");
    }



}

package cn.com.cnpc.cpoa.controller.app;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.constractor.ContContractorService;
import cn.com.cnpc.cpoa.service.constractor.ContProjectService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.KsVo;
import cn.com.cnpc.cpoa.vo.contractor.ContProjectVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContContractorData;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Api(tags = "app承包商管理")
@RestController
@RequestMapping("/hd/underwriter/contProject")
public class UnderwriterController extends BaseController {
    @Autowired
    ContProjectService contProjectService;

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    private BizCheckStepService diyCheckStepService;

    @ApiOperation("承包商待办列表")
    @RequestMapping(value = "/backlog", method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectAuditContProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                             @RequestParam(value = "projContName", defaultValue = "") String projContName,
                                             @RequestParam(value = "projContType", defaultValue = "") String projContType,
                                             @RequestParam(value = "userName", defaultValue = "") String userName,
                                             @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                             @RequestParam(value = "projState", defaultValue = "") String projState,
                                             @RequestParam(value = "accessDateStart", defaultValue = "") String accessDateStart,
                                             @RequestParam(value = "accessDateEnd", defaultValue = "") String accessDateEnd,
                                             @RequestParam(value = "projAccessType", defaultValue = "") String projAccessType,
                                             @RequestParam(value = "status", defaultValue = "") String status) {
        String userId = ServletUtils.getSessionUserId();

        Map<String, Object> params = new HashMap<>();
        params.put("projContName", projContName);
        params.put("projContType", projContType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("projState", projState);
        params.put("userId", userId);
        params.put("accessDateStart", accessDateStart);
        params.put("accessDateEnd", accessDateEnd);
        params.put("projAccessType", projAccessType);

        setDataGrade(userId, params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ContProjectVo> contProjectVos = new ArrayList<>();
        if (ContractorConstant.AuditType.AUDITING.equals(status)) {
            contProjectVos = contProjectService.selectAuditContProject(params);
        } else {
            contProjectVos = contProjectService.selectAuditedContProject(params);
        }
        TableDataInfo dataTable = getDataTable(contProjectVos, page.getTotal());
        return AppMessage.success(dataTable, "查询项目成功");
    }


    @ApiOperation("承包商流程中列表")
    @RequestMapping(value = "/underway", method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectActivitiItem(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "projContName",defaultValue="") String projContName,
                                         @RequestParam(value = "projContType",defaultValue="") String projContType,
                                         @RequestParam(value = "userName",defaultValue="") String userName,
                                         @RequestParam(value = "deptName",defaultValue="") String deptName,
                                         @RequestParam(value = "projState",defaultValue="") String projState,
                                         @RequestParam(value = "accessDateStart",defaultValue="") String accessDateStart,
                                         @RequestParam(value = "accessDateEnd",defaultValue="") String accessDateEnd){
        String userId = ServletUtils.getSessionUserId();
        Map<String,Object> params=new HashMap<>();
        params.put("projContName",projContName);
        params.put("projContType",projContType);
        params.put("userName",userName);
        params.put("deptName",deptName);
        params.put("projState",projState);
        params.put("userId", userId);
        params.put("accessDateStart",accessDateStart);
        params.put("accessDateEnd",accessDateEnd);

        setDataGrade(userId,params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);
        List<ContProjectVo> contProjectVos = contProjectService.selectActivitiItem(params);
        TableDataInfo dataTable = getDataTable(contProjectVos,page.getTotal());
        return AppMessage.success(dataTable, "查询项目成功");
    }

    @ApiOperation("查询承包商详情")
    @RequestMapping(value = "/contContractor/details", method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectContContractorDetails(@RequestParam(value = "accessId", defaultValue = "") String accessId) {
        ContContractorData contContractorData = contContractorService.selectContContractorDetails(accessId);

        return AppMessage.success(contContractorData, "查询承包商成功");
    }

    /**
     * 查询承包商、项目立项审核详情
     * @return
     */
    @RequestMapping(value="/details",method = RequestMethod.GET,produces = "application/json")
    public AppMessage queryDetails(@RequestParam(value = "objId",defaultValue="") String objId) {
        List<CheckStepPo> checkStepPoList = diyCheckStepService.selectDetails(objId);
        return AppMessage.success(checkStepPoList,"查询审核详情成功！");
    }

    @RequestMapping(value = "/audit", method = RequestMethod.GET,produces = "application/json")
    public AppMessage selectAuditContProject1(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,

                                             @RequestParam(value = "status", defaultValue = "") String status) {
        String userId = ServletUtils.getSessionUserId();

        Map<String, Object> params = new HashMap<>();

        params.put("userId", userId);


        setDataGrade(userId, params);


            List<ContProjectVo> contProjectVos = contProjectService.selectAuditContProject(params);
            List<ContProjectVo> contProjectVos1 = contProjectService.selectAuditedContProject(params);
        KsVo ksVo = new KsVo();
        int backlognum = contProjectVos.size();
        ksVo.setBacklognum(backlognum);
        int repliednum = contProjectVos1.size();
        ksVo.setRepliednum(repliednum);

        return AppMessage.success(ksVo, "查询项目成功");
    }
}

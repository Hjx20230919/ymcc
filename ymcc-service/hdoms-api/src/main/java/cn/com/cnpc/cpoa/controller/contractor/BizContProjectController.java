package cn.com.cnpc.cpoa.controller.contractor;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.service.constractor.ContProjectService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContProjectVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/9 20:07
 * @Description:项目申请
 */
@Api(tags = "项目准入申请")
@RestController
@RequestMapping("/hd/contractors/contProject")
public class BizContProjectController extends BaseController {


    @Autowired
    ContProjectService contProjectService;


    @Log(logContent = "新增项目", logModule = LogModule.CONTPROJECT, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage addContProject(@RequestBody ContProjectVo contProjectVo) throws Exception {
        //getRequest();
        String userId = ServletUtils.getSessionUserId();
        BizContProjectDto contProjectDto = contProjectService.addContProject(userId, contProjectVo);
        return AppMessage.success(contProjectDto.getProjId(), "新增项目成功");
    }


    @Log(logContent = "更新项目", logModule = LogModule.CONTPROJECT, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage upateContProject(@PathVariable String id, @RequestBody ContProjectVo contProjectVo) throws Exception {
        contProjectVo.setProjId(id);
        BizContProjectDto contProjectDto = contProjectService.upateContProject(id, ServletUtils.getSessionUserId(), contProjectVo);
        return AppMessage.success(contProjectDto.getProjId(), "更新项目成功");
    }

    @Log(logContent = "保存提交项目", logModule = LogModule.CONTPROJECT, logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/{id}", method = RequestMethod.PUT)
    public AppMessage submitContProject(@PathVariable String id, @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        contProjectService.submitContProject(id, ServletUtils.getSessionUserId(), type);
        return AppMessage.success(id, "保存提交项目");
    }

    @Log(logContent = "删除项目", logModule = LogModule.CONTPROJECT, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteContProject(@PathVariable String id) {

        if (1 == contProjectService.delete(id)) {
            return AppMessage.success(id, "删除项目成功");
        }
        return AppMessage.errorObjId(id, "删除项目失败");

    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public AppMessage selectContProjectDetails(@RequestParam(value = "projId", defaultValue = "") String projId) {
        Map<String, Object> params = new HashMap<>();
        params.put("projId", projId);
        List<ContProjectVo> contProjectVos = contProjectService.selectContProject(params);
        return AppMessage.success(contProjectVos.get(0), "查询项目成功");
    }

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectContProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                        @RequestParam(value = "projContName", defaultValue = "") String projContName,
                                        @RequestParam(value = "projContType", defaultValue = "") String projContType,
                                        @RequestParam(value = "userName", defaultValue = "") String userName,
                                        @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                        @RequestParam(value = "projAccessType", defaultValue = "") String projAccessType,
                                        @RequestParam(value = "projState", defaultValue = "") String projState,
                                        @RequestParam(value = "applyDateStart", defaultValue = "") String applyDateStart,
                                        @RequestParam(value = "applyDateEnd", defaultValue = "") String applyDateEnd,
                                        @RequestParam(value = "accessLevel", defaultValue = "") String accessLevel) {
        Map<String, Object> params = new HashMap<>();
        params.put("projContName", projContName);
        params.put("projContType", projContType);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("projState", projState);
        params.put("projAccessType", projAccessType);
        params.put("applyDateStart", applyDateStart);
        params.put("applyDateEnd", applyDateEnd);
        params.put("accessLevel", accessLevel);
        params.put("accessLevel2", ContractorConstant.projAccesstypeMap.get(accessLevel));

        String userId = ServletUtils.getSessionUserId();
        setDataGrade(userId, params);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ContProjectVo> contProjectVos = contProjectService.selectContProject(params);
        TableDataInfo dataTable = getDataTable(contProjectVos, page.getTotal());
        return AppMessage.success(dataTable, "查询项目成功");
    }

    @ApiOperation("根据用户查询准入审核列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "projContName",value = "承包商名称"),
                        @ApiImplicitParam(name = "projContType",value = "专业类别"),
                        @ApiImplicitParam(name = "userName",value = "经办人"),
                        @ApiImplicitParam(name = "deptName",value = "部门名称"),
                        @ApiImplicitParam(name = "projState",value = "当前状态"),
                        @ApiImplicitParam(name = "accessDateStart",value = "准入开始时间"),
                        @ApiImplicitParam(name = "accessDateEnd",value = "准入结束时间"),
                        @ApiImplicitParam(name = "projAccessType",value = "准入级别"),
                        @ApiImplicitParam(name = "status",value = "审核状态，auditing为待审核，audited为已审核"),
    })
    @RequestMapping(value = "/audit", method = RequestMethod.GET)
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

    /**
     * 查询承包商可用内容
     *
     * @return
     */
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public AppMessage selectContProjectContent(@RequestParam(value = "projContCode", defaultValue = "") String projContCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("contOrgNo", projContCode);
        Map<String, String> contentMap = contProjectService.selectContProjectContent(params);
        return AppMessage.success(contentMap, "查询承包商可用内容成功");
    }


    /**
     * 查询项目内容下拉列表成功
     *
     * @return
     */
    @RequestMapping(value = "/contentTab", method = RequestMethod.GET)
    public AppMessage selectContProjectContentTab() {

        return AppMessage.success(ContractorConstant.proCategoryMap, "查询项目内容下拉列表成功");
    }


    /**
     * 查询项目内容下拉列表成功
     *
     * @return
     */
    @RequestMapping(value = "/allContentTab", method = RequestMethod.GET)
    public AppMessage selectAllContProjectContentTab() {

        return AppMessage.success(ContractorConstant.allProCategoryMap, "查询全量项目内容下拉列表成功");
    }

    /**
     * 对退回的准入申请删除
     *
     * @return
     */
    @ApiOperation("对退回的准入申请删除")
    @RequestMapping(value = "/delete/{projId}", method = RequestMethod.DELETE)
    public AppMessage deleteContProjectByProjId(@PathVariable("projId")String projId) {
        return contProjectService.deleteContProjectByProjId(projId);
    }

}

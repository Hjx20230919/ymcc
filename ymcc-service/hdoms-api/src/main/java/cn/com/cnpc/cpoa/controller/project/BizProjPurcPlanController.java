package cn.com.cnpc.cpoa.controller.project;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizSysConfigDto;
import cn.com.cnpc.cpoa.domain.SysMenuDto;
import cn.com.cnpc.cpoa.domain.SysRoleActionDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcPlanDto;
import cn.com.cnpc.cpoa.mapper.SysMenuDtoMapper;
import cn.com.cnpc.cpoa.service.SysConfigService;
import cn.com.cnpc.cpoa.service.SysRoleActionService;
import cn.com.cnpc.cpoa.service.SysUserRoleService;
import cn.com.cnpc.cpoa.service.project.ProjPlanListService;
import cn.com.cnpc.cpoa.service.project.ProjPurcPlanService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.project.IviteContVo;
import cn.com.cnpc.cpoa.vo.project.ProjPlanListVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectPlanVo;
import cn.com.cnpc.cpoa.vo.project.ProjPurcPlanVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/12/13 20:24
 * @Description:
 */
@Api(tags = "采购计划管理")
@RestController
@RequestMapping("/hd/project/projPurcPlan")
public class BizProjPurcPlanController extends BaseController {

    @Autowired
    ProjPurcPlanService projPurcPlanService;

    @Autowired
    ProjPlanListService projPlanListService;

    @Autowired
    private SysMenuDtoMapper menuDtoMapper;

    @Autowired
    private SysUserRoleService userRoleService;

    @Autowired
    private SysRoleActionService roleActionService;


    @Value("${file.tempurl}")
    private String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private String baseFontUrl;

    private static Logger logger = LoggerFactory.getLogger(BizProjPurcPlanController.class);

    @ApiOperation("查询采购方案综合信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "dealNo",value = "项目编号"),
                        @ApiImplicitParam(name = "planNo",value = "采购方案编号")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectProjPurcPlan(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "projName", defaultValue = "") String projName,
                                         @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                         @RequestParam(value = "projStatus", defaultValue = "") String projStatus,
                                         @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                         @RequestParam(value = "planNo", defaultValue = "") String planNo,
                                         @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                         @RequestParam(value = "projPhase", defaultValue = "") String projPhase,
                                         @RequestParam(value = "freezeStatus", defaultValue = "0") String freezeStatus,
                                         @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                         @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("projStatus", projStatus);
        params.put("dealNo", dealNo);
        params.put("planNo", planNo);
        params.put("deptName", deptName);
        params.put("selContType", selContType);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        params.put("projPhase", projPhase);
        params.put("freezeStatus", freezeStatus);
        String userId = ServletUtils.getSessionUserId();
        setDataGrade(userId, params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ProjProjectPlanVo> list = projPurcPlanService.selectProjPurcPlan(params);
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询采购方案综合信息成功");
    }


    @RequestMapping(value = "/planList", method = RequestMethod.GET)
    public AppMessage selectPlanList(@RequestParam(value = "planId", defaultValue = "") String planId) {
        Map<String, Object> params = new HashMap<>();
        params.put("planId", planId);
        List<ProjPlanListVo> list = projPlanListService.selectPlanListVo(params);
        return AppMessage.success(list, "查询采购计划明细表列表成功");
    }

    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public AppMessage selectAuditProjPurcPlan(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                              @RequestParam(value = "projName", defaultValue = "") String projName,
                                              @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                              @RequestParam(value = "projStatus", defaultValue = "") String projStatus,
                                              @RequestParam(value = "userName", defaultValue = "") String userName,
                                              @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                              @RequestParam(value = "status", defaultValue = "") String status,
                                              @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                              @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("projStatus", projStatus);
        params.put("userName", userName);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        params.put("selContType", selContType);
        params.put("userId", ServletUtils.getSessionUserId());

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);


        List<ProjProjectPlanVo> list;
        if (ContractorConstant.AuditType.AUDITING.equals(status)) {
            list = projPurcPlanService.selectAuditProjPurcPlan(params);
        } else {
            list = projPurcPlanService.selectAuditedProjPurcPlan(params);
        }
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询当前审核采购方案成功");
    }


    @Log(logContent = "新增采购方案", logModule = LogModule.PROJPURCPLAN, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage addProjPurcPlan(@RequestBody ProjPurcPlanVo vo,
                                      @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        //TODO   新增对承包商资质的审核，如果资质过期则不提交审核
        List<String> contNameList = projPurcPlanService.selectAptitudeByContName(vo.getProjPlanListVos());
        if (contNameList.size() > 0){
            return AppMessage.error("新增采购方案失败，失败原因：以下承包商" + contNameList.toString() + "资质过期，请重新选择后提交！");
        }

        BizProjPurcPlanDto dto = projPurcPlanService.addProjPurcPlan(vo, ServletUtils.getSessionUserId(), type);
        return AppMessage.success(dto.getProjId(), "新增采购方案成功");
    }

    @Log(logContent = "更新采购方案", logModule = LogModule.PROJPURCPLAN, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage updateProjPurcPlan(@PathVariable String id,
                                         @RequestBody ProjPurcPlanVo vo,
                                         @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        //TODO 新增判断对承包商资质审核，如资质过期则不提交审核
        List<String> contNameList = projPurcPlanService.selectContractorAptitude(id);
        if (contNameList.size() > 0){
            return AppMessage.error("更新采购方案失败，失败原因：以下承包商" + contNameList.toString() + "资质过期，请重新选择后提交！");
        }

        vo.setPlanId(id);
        projPurcPlanService.updateProjPurcPlan(vo, ServletUtils.getSessionUserId(), type);
        return AppMessage.success(id, "更新采购方案成功");
    }

    @ApiOperation("维护采购方案")
    @Log(logContent = "维护采购方案", logModule = LogModule.PROJPURCRES, logType = LogType.OPERATION)
    @RequestMapping(value = "/maintain/{planId}", method = RequestMethod.PUT)
    public AppMessage maintainProjPlan( @PathVariable String planId,
                                        @RequestBody ProjPurcPlanVo vo) throws Exception {

        projPurcPlanService.maintainProjPlan(planId,ServletUtils.getSessionUserId(),vo.getPlanNotes(),vo.getProjPlanListVos(), vo.getAttachVos(), vo.getRecomContReason());
        return AppMessage.success(planId, "维护采购方案成功");
    }


    @Log(logContent = "删除采购方案", logModule = LogModule.PROJPURCPLAN, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        projPurcPlanService.deleteChain(id);
        return AppMessage.success(id, "删除采购方案成功");
    }


    @Log(logContent = "保存提交采购方案", logModule = LogModule.PROJPURCPLAN, logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/{id}", method = RequestMethod.PUT)
    public AppMessage submitProjPurcPlan(@PathVariable String id,
                                         @RequestParam(value = "type", defaultValue = "") String type,
                                         @RequestBody AuditVo auditVo) throws Exception {
        //TODO 新增判断对承包商资质审核，如资质过期则不提交审核
        List<String> contNameList = projPurcPlanService.selectContractorAptitude(id);
        if (contNameList.size() > 0){
            return AppMessage.error("采购方案提交失败，失败原因：以下承包商" + contNameList.toString() + "资质过期，请重新选择后提交！");
        }

        projPurcPlanService.submitProjPurcPlan(id, ServletUtils.getSessionUserId(), type,auditVo);

        return AppMessage.success(id, "保存提交采购方案信息成功！");
    }


    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public AppMessage selectPurcPlanDetails(@RequestParam(value = "planId", defaultValue = "") String planId) {
        Map<String, Object> params = new HashMap<>();
        params.put("planId", planId);
        ProjPurcPlanVo projPurcPlanVo = projPurcPlanService.selectPurcPlanDetails(params);
        return AppMessage.success(projPurcPlanVo, "查询采购详情成功");
    }


    @RequestMapping(value = "/iviteConts", method = RequestMethod.GET)
    public AppMessage selectIviteConts(@RequestParam(value = "selContId", defaultValue = "") String selContId,
                                       @RequestParam(value = "projId", defaultValue = "") String projId) {
        Map<String, Object> params = new HashMap<>();
        params.put("selContId", selContId);
        params.put("projId", projId);
        List<IviteContVo> iviteContVos = projPurcPlanService.selectIviteConts(params);
        return AppMessage.success(iviteContVos, "查询可用承包商列表成功");
    }

    /**
     * 导出 采购方案审批表.pdf
     *
     * @return
     */
    @Log(logContent = "采购方案审批表", logModule = LogModule.PROJPURCPLAN, logType = LogType.OPERATION)
    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public void pdf(HttpServletResponse response,
                    @RequestParam(value = "planId") String planId) throws Exception {

        try {
            // 下载到浏览器
            String pdfName = "采购方案审批表.pdf";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            projPurcPlanService.buildPlanPDF(response, TEMPURL, PDFPicUrl, baseFontUrl, planId);

        } catch (Exception e) {
            logger.error("导出采购方案审批表出错" + e.getMessage(), e);
            throw new AppException(e.getMessage());
        }

    }

    @ApiOperation("采购方案隐藏")
    @Log(logContent = "采购方案隐藏", logModule = LogModule.PROJPURCPLAN, logType = LogType.OPERATION)
    @RequestMapping(value = "/freeze/{planId}", method = RequestMethod.GET)
    public AppMessage freeze(@PathVariable("planId") String planId) {
        int i = projPurcPlanService.freeze(planId);
        if (i == 0){
            return AppMessage.result("采购方案隐藏失败！！");
        }
        return AppMessage.success(planId, "采购方案已隐藏");
    }

    @ApiOperation("采购方案显示")
    @Log(logContent = "采购方案显示", logModule = LogModule.PROJPURCPLAN, logType = LogType.OPERATION)
    @RequestMapping(value = "/unfreeze", method = RequestMethod.GET)
    public AppMessage unfreeze(@RequestParam(value = "planIds") String planIds) {
        Arrays.stream(planIds.split(",")).forEach(p ->  projPurcPlanService.unfreeze(p));
        return AppMessage.result("采购方案显示已完成");
    }

//    @ApiOperation("采购方案回退")
//    @ApiImplicitParams({@ApiImplicitParam(name = "planId",value = "采购计划id"),
//            @ApiImplicitParam(name = "projId",value = "项目申请id"),
//            @ApiImplicitParam(name = "selContId",value = "选商id")})
//    @Log(logContent = "采购方案回退", logModule = LogModule.PROJPURCPLAN, logType = LogType.OPERATION)
//    @RequestMapping(value = "/back", method = RequestMethod.GET)
//    public AppMessage back(@RequestParam("planId")String planId,
//                           @RequestParam("projId")String projId,
//                           @RequestParam(value = "resultId",required = false,defaultValue = "")String resultId,
//                           @RequestParam("selContId")String selContId) {
//        return projPurcPlanService.planBack(planId,projId,selContId,resultId);
//    }

}

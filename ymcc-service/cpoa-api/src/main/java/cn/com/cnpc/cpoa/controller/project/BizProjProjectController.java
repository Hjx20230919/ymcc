package cn.com.cnpc.cpoa.controller.project;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.domain.project.BizProjResultListDto;
import cn.com.cnpc.cpoa.enums.project.ProProjectStatusEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.enums.project.SelContTypeEnum;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.service.project.ProjPurcResultService;
import cn.com.cnpc.cpoa.service.project.ProjResultListService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.project.*;
import cn.com.cnpc.cpoa.web.base.BaseController;
import cn.hutool.core.lang.Console;
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
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 19:25
 * @Description:
 */
@Api(tags = "合同立项管理")
@RestController
@RequestMapping("/project/proProject")
public class BizProjProjectController extends BaseController {

    @Autowired
    ProjProjectService projProjectService;

    @Autowired
    ProjectAuditService projectAuditService;

    @Autowired
    ProjPurcResultService projPurcResultService;

    @Autowired
    ProjResultListService projResultListService;

    @Value("${file.tempurl}")
    private String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private String baseFontUrl;

    private static Logger logger = LoggerFactory.getLogger(BizProjProjectController.class);


    @Log(logContent = "新增项目", logModule = LogModule.PROPROJECT, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage addProject(@RequestBody ProjProjectVo vo,
                                 @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        BizProjProjectDto dto = projProjectService.addProject(vo, ServletUtils.getSessionUserId(), type);
        return AppMessage.success(dto.getProjId(), "新增项目成功");
    }

    @Log(logContent = "更新项目", logModule = LogModule.PROPROJECT, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage updateProject( @PathVariable String id,
                                    @RequestBody ProjProjectVo vo,
                                    @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        //   vo.setCreateId(userId);
        vo.setProjId(id);
        projProjectService.updateProject(vo, userId, type);
        return AppMessage.success(id, "更新项目成功");
    }

    @ApiOperation("项目立项维护")
    @Log(logContent = "维护项目", logModule = LogModule.PROPROJECT, logType = LogType.OPERATION)
    @RequestMapping(value = "/maintain/{projId}", method = RequestMethod.PUT)
    public AppMessage maintainProject( @PathVariable String projId,
                                     @RequestBody ProjProjectVo vo) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        vo.setProjId(projId);
        projProjectService.maintainProject(vo, userId, projId);
        return AppMessage.success(projId, "维护项目成功");
    }

    @ApiOperation("查询立项综合信息")
    @ApiImplicitParams({@ApiImplicitParam(name = "projName",value = "工程/服务名称"),
            @ApiImplicitParam(name = "selContType",value = "选商方式，多个以,分隔"),
            @ApiImplicitParam(name = "projStatus",value = "项目状态"),
            @ApiImplicitParam(name = "deptName",value = "经办部门"),
            @ApiImplicitParam(name = "createAtStart",value = "创建开始时间"),
            @ApiImplicitParam(name = "createAtEnd",value = "创建结束时间"),
            @ApiImplicitParam(name = "joinStatus",value = "承包商参与项目状态,如果参与项目值为‘参与’"),
            @ApiImplicitParam(name = "joinName",value = "参与项目承包商名称"),
            @ApiImplicitParam(name = "bidStatus",value = "承包商中标项目状态，如果中标值为‘中标’"),
            @ApiImplicitParam(name = "bidName",value = "中标承包商名称"),
            @ApiImplicitParam(name = "projPhase",value = "项目阶段"),
            @ApiImplicitParam(name = "dealNo",value = "项目编号")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "projName", defaultValue = "") String projName,
                                    @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                    @RequestParam(value = "projStatus", defaultValue = "") String projStatus,
                                    @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                    @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                    @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                    @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd,
                                    @RequestParam(value = "joinStatus", defaultValue = "") String joinStatus,
                                    @RequestParam(value = "joinName", defaultValue = "") String joinName,
                                    @RequestParam(value = "bidStatus", defaultValue = "") String bidStatus,
                                    @RequestParam(value = "userName", defaultValue = "") String userName,
                                    @RequestParam(value = "bidName", defaultValue = "") String bidName,
                                    @RequestParam(value = "projPhase", defaultValue = "") String projPhase,
                                    @RequestParam(value = "payType", defaultValue = "") String payType) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        if(StringUtils.isNotEmpty(selContType)){
            params.put("selContTypes",selContType.split(","));
        }
        params.put("payType", payType);
        params.put("projStatus", projStatus);
        params.put("dealNo", dealNo);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        params.put("projPhase", projPhase);
        params.put("joinStatus", joinStatus);
        params.put("joinName", joinName);
        params.put("bidStatus", bidStatus);
        params.put("bidName", bidName);
        params.put("userName", userName);

        setDataGrade(ServletUtils.getSessionUserId(), params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);
        List<ProjProjectVo> list = projProjectService.selectProject(params);
        for (ProjProjectVo projProjectVo : list) {
            if (projProjectVo.getResultId() != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("resultId", projProjectVo.getResultId());
                //2 查询选商列表
                List<BizProjResultListDto> projResultListDtos = projResultListService.selectResultList(map);
                BigDecimal totalValue = new BigDecimal(0);
                for (BizProjResultListDto projResultListDto : projResultListDtos) {
                    if (projResultListDto.getLimitTotalPrice() != null) {
                        BigDecimal limitTotalPrice = projResultListDto.getLimitTotalPrice();
                        totalValue = totalValue.add(limitTotalPrice);
                    }
                }
                projProjectVo.setTotalValue(totalValue);
            } else {
                projProjectVo.setTotalValue(new BigDecimal(0));
            }
        }
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询立项综合信息成功");
    }

    @ApiOperation("一键导出所有立项信息")
    @RequestMapping(value = "/export",method = RequestMethod.GET)
    public AppMessage exportProject(HttpServletResponse response) {
        Map<String, Object> params = new HashMap<>();
        String selContType = "buildProjectSingle,buildProjectNegotiation,buildProjectInside,openTender,noTender";
        if(StringUtils.isNotEmpty(selContType)){
            params.put("selContTypes",selContType.split(","));
        }
        setDataGrade(ServletUtils.getSessionUserId(), params);

        ExcelUtil<ProjProjectVo> util = new ExcelUtil<ProjProjectVo>(ProjProjectVo.class);
        List<ProjProjectVo> list = projProjectService.selectProject(params);
        for (ProjProjectVo projProjectVo:list) {
            projProjectVo.setProjPhase(ProjPhaseEnum.getEnumByKey(projProjectVo.getProjPhase()));
            projProjectVo.setSelContType(SelContTypeEnum.getEnumByKey(projProjectVo.getSelContType()));
            projProjectVo.setProjStatus(ProProjectStatusEnum.getEnumByKey(projProjectVo.getProjStatus()));
            if (projProjectVo.getResultId() != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("resultId", projProjectVo.getResultId());
                //2 查询选商列表
                List<BizProjResultListDto> projResultListDtos = projResultListService.selectResultList(map);
                BigDecimal totalValue = new BigDecimal(0);
                for (BizProjResultListDto projResultListDto : projResultListDtos) {
                    if (projResultListDto.getLimitTotalPrice() != null) {
                        BigDecimal limitTotalPrice = projResultListDto.getLimitTotalPrice();
                        totalValue = totalValue.add(limitTotalPrice);
                    }
                }
                projProjectVo.setTotalValue(totalValue);
            } else {
                projProjectVo.setTotalValue(new BigDecimal(0));
            }
        }
        return util.exportExcelBrowser(response, list, "项目立项综合信息");
    }

    @ApiOperation("条件导出立项信息")
    @RequestMapping(value = "/exportbycondition",method = RequestMethod.GET)
    public AppMessage exportProject1(HttpServletResponse response,
                                     @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                     @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                     @RequestParam(value = "projName", defaultValue = "") String projName,
                                     @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                     @RequestParam(value = "projStatus", defaultValue = "") String projStatus,
                                     @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                     @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                     @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                     @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd,
                                     @RequestParam(value = "joinStatus", defaultValue = "") String joinStatus,
                                     @RequestParam(value = "joinName", defaultValue = "") String joinName,
                                     @RequestParam(value = "bidStatus", defaultValue = "") String bidStatus,
                                     @RequestParam(value = "userName", defaultValue = "") String userName,
                                     @RequestParam(value = "bidName", defaultValue = "") String bidName,
                                     @RequestParam(value = "projPhase", defaultValue = "") String projPhase,
                                     @RequestParam(value = "payType", defaultValue = "") String payType) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        if(StringUtils.isNotEmpty(selContType)){
            params.put("selContTypes",selContType.split(","));
        }
        params.put("payType", payType);
        params.put("projStatus", projStatus);
        params.put("dealNo", dealNo);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        params.put("projPhase", projPhase);
        params.put("joinStatus", joinStatus);
        params.put("joinName", joinName);
        params.put("bidStatus", bidStatus);
        params.put("bidName", bidName);
        params.put("userName", userName);
//        String selContType = "buildProjectSingle,buildProjectNegotiation,buildProjectInside,openTender,noTender";
//        if(StringUtils.isNotEmpty(selContType)){
//            params.put("selContTypes",selContType.split(","));
//        }
        setDataGrade(ServletUtils.getSessionUserId(), params);

        ExcelUtil<ProjProjectVo> util = new ExcelUtil<ProjProjectVo>(ProjProjectVo.class);
        List<ProjProjectVo> list = projProjectService.selectProject(params);
        for (ProjProjectVo projProjectVo:list) {
            projProjectVo.setProjPhase(ProjPhaseEnum.getEnumByKey(projProjectVo.getProjPhase()));
            projProjectVo.setSelContType(SelContTypeEnum.getEnumByKey(projProjectVo.getSelContType()));
            projProjectVo.setProjStatus(ProProjectStatusEnum.getEnumByKey(projProjectVo.getProjStatus()));
            if (projProjectVo.getResultId() != null) {
                Map<String, Object> map = new HashMap<>();
                map.put("resultId", projProjectVo.getResultId());
                //2 查询选商列表
                List<BizProjResultListDto> projResultListDtos = projResultListService.selectResultList(map);
                BigDecimal totalValue = new BigDecimal(0);
                for (BizProjResultListDto projResultListDto : projResultListDtos) {
                    if (projResultListDto.getLimitTotalPrice() != null) {
                        BigDecimal limitTotalPrice = projResultListDto.getLimitTotalPrice();
                        totalValue = totalValue.add(limitTotalPrice);
                    }
                }
                projProjectVo.setTotalValue(totalValue);
            } else {
                projProjectVo.setTotalValue(new BigDecimal(0));
            }
        }
        return util.exportExcelBrowser(response, list, "项目立项综合信息");
    }





    @Log(logContent = "删除项目", logModule = LogModule.PROPROJECT, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        boolean delete = projProjectService.deleteProAttach(id);
        if (delete) {
            return AppMessage.success(id, "删除项目成功");
        }
        return AppMessage.errorObjId(id, "删除项目失败");
    }

    @Log(logContent = "保存提交项目", logModule = LogModule.PROPROJECT, logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/{id}", method = RequestMethod.PUT)
    public AppMessage submitProject( @PathVariable String id,
                                    @RequestParam(value = "type", defaultValue = "") String type) throws Exception {

        projProjectService.submitProject(id, ServletUtils.getSessionUserId(), type);

        return AppMessage.success(id, "保存提交合同成功！");
    }

    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public AppMessage selectAuditProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
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
        //setDataGrade(getSessionUserId(),params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ProjProjectVo> list;

        if (ContractorConstant.AuditType.AUDITING.equals(status)) {
            list = projProjectService.selectAuditProject(params);
        } else {
            list = projProjectService.selectAuditedProject(params);
        }

        return AppMessage.success(getDataTable(list,page.getTotal()), "查询立项审核信息成功");
    }


    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public AppMessage selectProjectDetails(@RequestParam(value = "projId", defaultValue = "") String projId) {
        Map<String, Object> params = new HashMap<>();
        params.put("projId", projId);
        params.put("selContId", projId);
        List<ProjProjectVo> list = projProjectService.selectProjectDetails(params);
        return AppMessage.success(list, "查询立项详情成功");
    }

    /**
     * 查询可用选商
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/selContPros", method = RequestMethod.GET)
    public AppMessage selectSelContPro(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                       @RequestParam(value = "projName", defaultValue = "") String projName) {
        Map<String, Object> param = new HashMap<>();
        param.put("projName", projName);
        setDataGrade(ServletUtils.getSessionUserId(), param);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ProjProjectVo> list = projProjectService.selectSelContPro(param);
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询可用选商成功");
    }

    /**
     * 查询可用采购计划
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/projPurcPlan", method = RequestMethod.GET)
    public AppMessage selectProjPurcPlan(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "projName", defaultValue = "") String projName) {
        Map<String, Object> param = new HashMap<>();
        param.put("projName", projName);
        setDataGrade(ServletUtils.getSessionUserId(), param);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ProjProjectVo> list = projProjectService.selectProjPurcPlan(param);
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询可用采购计划");
    }


    /**
     * 查询可用采购结果
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/projPurcResult", method = RequestMethod.GET)
    public AppMessage selectProjPurcResult(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                         @RequestParam(value = "projName", defaultValue = "") String projName) {
        Map<String, Object> param = new HashMap<>();
        param.put("projName", projName);
        setDataGrade(ServletUtils.getSessionUserId(), param);
        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ProjProjectVo> list = projProjectService.selectProjPurcResult(param);
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询可用采购结果");
    }


    /**
     * 导出 立项审批表.pdf
     *
     * @return
     */
    @Log(logContent = "导出立项审批表", logModule = LogModule.PROPROJECT, logType = LogType.OPERATION)
    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public void pdf(HttpServletResponse response,
                    @RequestParam(value = "projId", defaultValue = "") String projId,
                    @RequestParam(value = "selContId", defaultValue = "") String selContId,
                    @RequestParam(value = "planId", defaultValue = "") String planId,
                    @RequestParam(value = "resultId", defaultValue = "") String resultId) throws Exception {

        try {


            // 下载到浏览器
            //String pdfName = ProjectConstant.projProPdfTypeMap.get(projProjectVo.getSelContType()) ;
            String pdfName = "工程/服务采购审批表.pdf" ;

            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            projProjectService.buildOpenTenderPDF(response, TEMPURL, PDFPicUrl, baseFontUrl,projId,selContId,planId,resultId);

        } catch (Exception e) {
            logger.error("导出立项审批表出错" + e.getMessage(), e);
            throw new AppException(e.getMessage());
        }

    }


    @Log(logContent = "删除立项回退流程",logModule = LogModule.PROPROJECT,logType = LogType.OPERATION)
    @RequestMapping(value = "/deleteBack/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteBack( @PathVariable String id,
                                  @RequestParam(value = "projType", defaultValue = "") String projType) {

        ProjectAuditService auditService = projectAuditService.getAuditService(projType);
        auditService.deleteBack(id);

        return AppMessage.errorObjId(id,"删除立项回退流程成功");
    }




}

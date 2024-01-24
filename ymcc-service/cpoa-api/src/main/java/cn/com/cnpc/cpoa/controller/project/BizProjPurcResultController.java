package cn.com.cnpc.cpoa.controller.project;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizSysConfigDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcResultDto;
import cn.com.cnpc.cpoa.domain.project.BizProjResultListDto;
import cn.com.cnpc.cpoa.service.SysConfigService;
import cn.com.cnpc.cpoa.service.project.ProjPurcResultService;
import cn.com.cnpc.cpoa.service.project.ProjResultListService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectResultVo;
import cn.com.cnpc.cpoa.vo.project.ProjPurcResultVo;
import cn.com.cnpc.cpoa.vo.project.ProjRsultListVo;
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
 * @Date: 2019/12/14 9:43
 * @Description:
 */
@Api(tags = "采购结果管理")
@RestController
@RequestMapping("/project/projPurcResult")
public class BizProjPurcResultController extends BaseController {
    @Autowired
    ProjPurcResultService projPurcResultService;

    @Autowired
    ProjResultListService projResultListService;

    @Autowired
    private SysConfigService configService;

    @Value("${file.tempurl}")
    private String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private String baseFontUrl;

    private static Logger logger = LoggerFactory.getLogger(BizProjPurcResultController.class);


    @ApiOperation("采购结果查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "dealNo",value = "项目编号"),
                        @ApiImplicitParam(name = "planNo",value = "采购结果编号")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectProjPurcResult(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
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
        String userId = ServletUtils.getSessionUserId();
        params.put("freezeStatus", freezeStatus);
        setDataGrade(userId, params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ProjProjectResultVo> list = projPurcResultService.selectProjPurcResult(params);
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询采购结果综合信息成功");
    }


    @RequestMapping(value = "/resultList", method = RequestMethod.GET)
    public AppMessage selectResultList(@RequestParam(value = "resultId", defaultValue = "") String resultId) {
        Map<String, Object> params = new HashMap<>();
        params.put("resultId", resultId);
        List<BizProjResultListDto> list = projResultListService.selectResultList(params);
        return AppMessage.success(list, "查询采购计划明细表列表成功");
    }

    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public AppMessage selectAuditProjPurcResult(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
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


        List<ProjProjectResultVo> list;
        if (ContractorConstant.AuditType.AUDITING.equals(status)) {
            list = projPurcResultService.selectAuditProjPurcResult(params);
        } else {
            list = projPurcResultService.selectAuditedProjPurcResult(params);
        }
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询当前审核采购结果成功");
    }


    @Log(logContent = "新增采购结果", logModule = LogModule.PROJPURCRES, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage addProjPurcResult(@RequestBody ProjPurcResultVo vo,
                                        @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        BizProjPurcResultDto dto = projPurcResultService.addProjPurcResult(vo, ServletUtils.getSessionUserId(), type);
        return AppMessage.success(dto.getProjId(), "新增采购结果成功");
    }

    @Log(logContent = "更新采购结果", logModule = LogModule.PROJPURCRES, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage updateProjPurcResult( @PathVariable String id,
                                           @RequestBody ProjPurcResultVo vo,
                                           @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        vo.setResultId(id);
        projPurcResultService.updateProjPurcResult(vo, ServletUtils.getSessionUserId(), type);
        return AppMessage.success(id, "更新采购结果成功");
    }

    @ApiOperation("维护采购结果")
    @Log(logContent = "维护采购结果", logModule = LogModule.PROJPURCRES, logType = LogType.OPERATION)
    @RequestMapping(value = "/maintain/{resultId}", method = RequestMethod.PUT)
    public AppMessage maintainProjPurcResult( @PathVariable String resultId,
                                              @RequestBody ProjPurcResultVo vo) throws Exception {

        projPurcResultService.maintainProjPurcResult(resultId,ServletUtils.getSessionUserId(),vo.getResultNotes(), vo.getProjRsultListVos(), vo.getAttachVos());
        return AppMessage.success(resultId, "维护采购结果成功");
    }


    @Log(logContent = "删除采购结果", logModule = LogModule.PROJPURCRES, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        projPurcResultService.deleteChain(id);
        return AppMessage.success(id, "删除采购结果成功");
    }


    @Log(logContent = "保存提交采购结果", logModule = LogModule.PROJPURCRES, logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/{id}", method = RequestMethod.PUT)
    public AppMessage submitProjPurcResult( @PathVariable String id,
                                           @RequestParam(value = "type", defaultValue = "") String type,
                                            @RequestBody AuditVo auditVo) throws Exception {

        projPurcResultService.submitProjPurcResult(id, ServletUtils.getSessionUserId(), type,auditVo);

        return AppMessage.success(id, "保存提交采购结果信息成功！");
    }


    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public AppMessage selectPurcPlanDetails(@RequestParam(value = "resultId", defaultValue = "") String resultId) {
        Map<String, Object> params = new HashMap<>();
        params.put("resultId", resultId);
        ProjPurcResultVo projPurcResultVo = projPurcResultService.selectPurcResultDetails(params);
        return AppMessage.success(projPurcResultVo, "查询采购详情成功");
    }

    /**
     * 导出 采购结果审批表.pdf
     *
     * @return
     */
    @Log(logContent = "采购结果审批表", logModule = LogModule.PROJPURCRES, logType = LogType.OPERATION)
    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public void pdf(HttpServletResponse response,
                    @RequestParam(value = "resultId") String resultId) throws Exception {

        try {
            // 下载到浏览器
            String pdfName = "采购结果审批表.pdf";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            projPurcResultService.buildResultPDF(response, TEMPURL, PDFPicUrl, baseFontUrl, resultId);

        } catch (Exception e) {
            logger.error("导出采购结果审批表出错" + e.getMessage(), e);
            throw new AppException(e.getMessage());
        }

    }

    @ApiOperation("采购结果隐藏")
    @ApiImplicitParams({@ApiImplicitParam(name = "resultId",value = "采购结果id")})
    @Log(logContent = "采购结果隐藏", logModule = LogModule.PROJPURCRES, logType = LogType.OPERATION)
    @RequestMapping(value = "/freeze/{resultId}", method = RequestMethod.GET)
    public AppMessage freeze(@PathVariable("resultId") String resultId) {
        int i = projPurcResultService.freeze(resultId);
        if (i == 0){
            return AppMessage.result("采购结果隐藏失败！！");
        }
        return AppMessage.success(resultId, "采购结果已隐藏");
    }


    @ApiOperation("采购结果显示")
    @ApiImplicitParams({@ApiImplicitParam(name = "resultIds",value = "采购结果id,多个以,分隔")})
    @Log(logContent = "采购结果显示", logModule = LogModule.PROJPURCRES, logType = LogType.OPERATION)
    @RequestMapping(value = "/unfreeze", method = RequestMethod.GET)
    public AppMessage unfreeze(@RequestParam("resultIds") String resultIds) {
        Arrays.stream(resultIds.split(",")).forEach(p -> projPurcResultService.unfreeze(p));
        return AppMessage.result( "采购结果已显示");
    }

}

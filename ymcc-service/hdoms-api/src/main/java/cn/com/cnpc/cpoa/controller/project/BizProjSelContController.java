package cn.com.cnpc.cpoa.controller.project;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.project.BizProjSelContDto;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.service.constractor.ContContractorService;
import cn.com.cnpc.cpoa.service.project.ProjContListService;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.service.project.ProjSelContService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.DealSettleCompreVo;
import cn.com.cnpc.cpoa.vo.contractor.ContManageQueryVo;
import cn.com.cnpc.cpoa.vo.project.ProjContListVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectSelVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectVo;
import cn.com.cnpc.cpoa.vo.project.ProjSelContVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:13
 * @Description:
 */
@Api(tags = "选商管理")
@RestController
@RequestMapping("/hd/project/projSelCont")
public class BizProjSelContController extends BaseController {
    @Autowired
    ProjProjectService projProjectService;

    @Autowired
    ProjSelContService projSelContService;
    @Autowired
    ContContractorService contContractorService;

    @Autowired
    ProjContListService projContListService;

    @Value("${file.tempurl}")
    private String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private String baseFontUrl;

    private static Logger logger = LoggerFactory.getLogger(BizProjSelContController.class);

    @ApiOperation("选商信息查询")
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage selectProjSelCont(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                        @RequestParam(value = "projName", defaultValue = "") String projName,
                                        @RequestParam(value = "projStatus", defaultValue = "") String projStatus,
                                        @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                        @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                        @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                        @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                        @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("projStatus", projStatus);
        params.put("dealNo", dealNo);
        params.put("selContType", selContType);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        SysUserDto userDto = userService.selectByKey(ServletUtils.getSessionUserId());
        params.put("dataScopeDept", userDto.getDeptId());
//        setDataGrade(ServletUtils.getSessionUserId(), params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ProjProjectSelVo> list = projSelContService.selectProjSelCont(params);
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询选商综合信息成功");
    }



    @ApiOperation("数据维护中选商信息查询")
    @RequestMapping(value = "/maintainn",method = RequestMethod.GET)
    public AppMessage selectProjSelCont2(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                        @RequestParam(value = "projName", defaultValue = "") String projName,
                                        @RequestParam(value = "projStatus", defaultValue = "") String projStatus,
                                        @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                        @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                        @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                        @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                        @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("projStatus", projStatus);
        params.put("dealNo", dealNo);
        params.put("selContType", selContType);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
//        SysUserDto userDto = userService.selectByKey(ServletUtils.getSessionUserId());
//        params.put("dataScopeDept", userDto.getDeptId());
//        setDataGrade(ServletUtils.getSessionUserId(), params);

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);

        List<ProjProjectSelVo> list = projSelContService.selectProjSelCont(params);
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询选商综合信息成功");
    }



    @RequestMapping(value = "/export",method = RequestMethod.GET)
    public AppMessage selectProjSelCont(HttpServletResponse response,
                                        @RequestParam(value = "projName", defaultValue = "") String projName,
                                        @RequestParam(value = "projStatus", defaultValue = "") String projStatus,
                                        @RequestParam(value = "selContType", defaultValue = "") String selContType,
                                        @RequestParam(value = "userName", defaultValue = "") String userName,
                                        @RequestParam(value = "deptName", defaultValue = "") String deptName,
                                        @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                        @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd) {
        Map<String, Object> params = new HashMap<>();
        params.put("projName", projName);
        params.put("projStatus", projStatus);
        params.put("userName", userName);
        params.put("selContType", selContType);
        params.put("deptName", deptName);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);

        setDataGrade(ServletUtils.getSessionUserId(), params);

        ExcelUtil<ProjProjectSelVo> util = new ExcelUtil<ProjProjectSelVo>(ProjProjectSelVo.class);
        List<ProjProjectSelVo> list = projSelContService.selectProjSelCont(params);

        for (ProjProjectSelVo projProjectSelVo:list) {
            projProjectSelVo.setProjPhase(ProjPhaseEnum.getEnumByKey(projProjectSelVo.getProjPhase()));
        }
        return util.exportExcelBrowser(response, list, "选商综合信息");
    }




    @RequestMapping(value = "/audit", method = RequestMethod.GET)
    public AppMessage selectAuditProjSelCont(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                             @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                             @RequestParam(value = "projName", defaultValue = "") String projName,
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
        params.put("userId", ServletUtils.getSessionUserId());

        Page<Object> page = PageHelper.startPage(pageNo, pageSize);


        List<ProjProjectSelVo> list;
        if (ContractorConstant.AuditType.AUDITING.equals(status)) {
            list = projSelContService.selectAuditProjSelCont(params);
        } else {
            list = projSelContService.selectAuditedProjSelCont(params);
        }
        return AppMessage.success(getDataTable(list,page.getTotal()), "查询选商当前审核成功");
    }

    @Log(logContent = "新增选商", logModule = LogModule.PROSELCONT, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage addSelCont(
            @RequestBody ProjSelContVo vo,
            @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        BizProjSelContDto dto = projSelContService.addSelCont(vo, ServletUtils.getSessionUserId(), type);
        return AppMessage.success(dto.getProjId(), "新增选商成功");
    }

    @Log(logContent = "更新选商", logModule = LogModule.PROSELCONT, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage updateSelCont(@PathVariable String id,
                                    @RequestBody ProjSelContVo vo,
                                    @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        vo.setSelContId(id);
        projSelContService.updateSelCont(vo, ServletUtils.getSessionUserId(), type);
        return AppMessage.success(id, "更新选商成功");
    }

    @ApiOperation("维护选商成功")
    @Log(logContent = "维护选商", logModule = LogModule.PROSELCONT, logType = LogType.OPERATION)
    @RequestMapping(value = "/maintain/{selContId}", method = RequestMethod.PUT)
    public AppMessage maintainSelCont(@PathVariable String selContId,
                                      @RequestBody ProjSelContVo vo) throws Exception {
        projSelContService.maintainSelCont(selContId, vo.getAttachVos(), vo.getProjContListVos(), vo.getNotes(), vo.getContQlyReq(),ServletUtils.getSessionUserId(),vo.getProjName(),vo.getDealName());
        return AppMessage.success(selContId, "维护选商成功");
    }

    @Log(logContent = "删除选商", logModule = LogModule.PROSELCONT, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        boolean delete = projSelContService.deleteSelAttach(id);
        if (delete) {
            return AppMessage.success(id, "删除选商成功");
        }
        return AppMessage.errorObjId(id, "删除选商失败");
    }


    @Log(logContent = "保存提交选商", logModule = LogModule.PROSELCONT, logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/{id}", method = RequestMethod.PUT)
    public AppMessage submitSelCont(@PathVariable String id,
                                    @RequestParam(value = "type", defaultValue = "") String type) throws Exception {

        projSelContService.submitSelCont(id, ServletUtils.getSessionUserId(), type);

        return AppMessage.success(id, "保存提交选商信息成功！");
    }


    @RequestMapping(value = "/contList", method = RequestMethod.GET)
    public AppMessage selectContList(@RequestParam(value = "selContId", defaultValue = "") String selContId,@RequestParam(value = "projId", defaultValue = "") String projId) {
        Map<String, Object> params = new HashMap<>();

        if(StrUtil.isBlank(selContId)){
            params.put("projId", projId);

            List<ProjProjectVo> list = projProjectService.selectProjectDetails(params);
            List<ProjContListVo> allProjContListVos = list.stream()
                    .flatMap(projectVo -> projectVo.getProjContListVos().stream())
                    .collect(Collectors.toList());
            return AppMessage.success(allProjContListVos, "查询承包商列表成功");
        }else {
            params.put("selContId", selContId);
        }
        List<ProjContListVo> list = projContListService.selectContList(params);
        return AppMessage.success(list, "查询承包商列表成功");
    }


    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public AppMessage selectSelDetails(@RequestParam(value = "selContId", defaultValue = "") String selContId) {
        Map<String, Object> params = new HashMap<>();
        params.put("selContId", selContId);
        ProjSelContVo projSelContVo = projSelContService.selectSelDetails(params);
        return AppMessage.success(projSelContVo, "查询选商详情成功");
    }

    /**
     * 导出 工程/服务采购选商审批表.pdf
     *
     * @return
     */
    @Log(logContent = "导出工程/服务采购选商审批表", logModule = LogModule.PROPROJECT, logType = LogType.OPERATION)
    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public void pdf(HttpServletResponse response,
                    @RequestParam(value = "selContId") String selContId) throws Exception {

        try {

            // 下载到浏览器
            String pdfName = "工程/服务采购选商审批表.pdf";

            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            projSelContService.buildSelContPDF(response, TEMPURL, PDFPicUrl, baseFontUrl, selContId);

        } catch (Exception e) {
            logger.error("导出工程/服务采购选商审批表出错" + e.getMessage(), e);
            throw new AppException(e.getMessage());
        }

    }
}

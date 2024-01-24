package cn.com.cnpc.cpoa.controller.bid;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.bid.BidCertiBorrowPo;
import cn.com.cnpc.cpoa.po.bid.BidProjectPo;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.bid.BidProjectService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.bid.BidCertiBorrowVo;
import cn.com.cnpc.cpoa.vo.bid.BidProjectVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-04  17:28
 * @Description:
 * @Version: 1.0
 */
@Api(tags = "招标项目综合管理")
@RestController
@RequestMapping("/bidProject")
public class BidProjectSController extends BaseController {

    @Autowired
    private BidProjectService projectService;

    @Autowired
    private BizCheckStepService diyCheckStepService;

    @Value("${file.tempurl}")
    private String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private String baseFontUrl;

    /**
     * 查询招标项目信息
     * @return
     */
    @ApiOperation(value = "查询招标项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projNo",value = "招标项目编号"),
            @ApiImplicitParam(name = "projName",value = "招标项目名称"),
            @ApiImplicitParam(name = "bidOpenAtStart",value = "开标时间（开始）"),
            @ApiImplicitParam(name = "bidOpenAtEnd",value = "开标时间（结束）"),
            @ApiImplicitParam(name = "publishAtStart",value = "发布时间（开始）"),
            @ApiImplicitParam(name = "publishAtEnd",value = "发布时间（结束）"),
            @ApiImplicitParam(name = "projStatus",value = "项目状态"),
            @ApiImplicitParam(name = "deptId",value = "部门id"),
            @ApiImplicitParam(name = "bidName",value = "中标公司名称"),
            @ApiImplicitParam(name = "isBid",value = "是否中标，0 为是，1 为否")})
    @RequestMapping(method = RequestMethod.GET,value = "")
    public AppMessage selectProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "projNo", defaultValue = "") String projNo,
                                    @RequestParam(value = "projName", defaultValue = "") String projName,
                                    @RequestParam(value = "bidOpenAtStart",defaultValue = "")String bidOpenAtStart,
                                    @RequestParam(value = "bidOpenAtEnd",defaultValue = "")String bidOpenAtEnd,
                                    @RequestParam(value = "publishAtStart",defaultValue = "")String publishAtStart,
                                    @RequestParam(value = "publishAtEnd",defaultValue = "")String publishAtEnd,
                                    @RequestParam(value = "projStatus",defaultValue = "")String projStatus,
                                    @RequestParam(value = "deptId",defaultValue = "")String deptId,
                                    @RequestParam(value = "isBid",defaultValue = "")String isBid,
                                    @RequestParam(value = "bidName",defaultValue = "")String bidName,
                                    @RequestParam(value = "bidProjId",defaultValue = "")String bidProjId
    ) {
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("projNo",projNo);
        param.put("projName",projName);
        param.put("bidOpenAtStart",bidOpenAtStart);
        param.put("bidOpenAtEnd",bidOpenAtEnd);
        param.put("publishAtStart",publishAtStart);
        param.put("publishAtEnd",publishAtEnd);
        param.put("projStatus",projStatus);
        param.put("deptId",deptId);
        param.put("isBid",isBid);
        param.put("bidName",bidName);
        param.put("bidProjId",bidProjId);
        setDataGrade(ServletUtils.getSessionUserId(), param);
        HashMap<String,Object> dataMap = projectService.selectProject(pageSize,pageNum,param);
        return AppMessage.success(getDataTable((List<BidProjectPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询招标项目信息成功");
    }

    /**
     * 导出招标项目信息
     * @return
     */
    @ApiOperation(value = "导出招标项目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "projNo",value = "招标项目编号"),
            @ApiImplicitParam(name = "projName",value = "招标项目名称"),
            @ApiImplicitParam(name = "bidOpenAtStart",value = "开标时间（开始）"),
            @ApiImplicitParam(name = "bidOpenAtEnd",value = "开标时间（结束）"),
            @ApiImplicitParam(name = "publishAtStart",value = "发布时间（开始）"),
            @ApiImplicitParam(name = "publishAtEnd",value = "发布时间（结束）"),
            @ApiImplicitParam(name = "projStatus",value = "项目状态"),
            @ApiImplicitParam(name = "deptId",value = "部门id"),
            @ApiImplicitParam(name = "isBid",value = "是否中标，0 为是，1 为否")})
    @RequestMapping(method = RequestMethod.GET,value = "/export")
    public void exportProject(
                                    @RequestParam(value = "projNo", defaultValue = "") String projNo,
                                    @RequestParam(value = "projName", defaultValue = "") String projName,
                                    @RequestParam(value = "bidOpenAtStart",defaultValue = "")String bidOpenAtStart,
                                    @RequestParam(value = "bidOpenAtEnd",defaultValue = "")String bidOpenAtEnd,
                                    @RequestParam(value = "publishAtStart",defaultValue = "")String publishAtStart,
                                    @RequestParam(value = "publishAtEnd",defaultValue = "")String publishAtEnd,
                                    @RequestParam(value = "projStatus",defaultValue = "")String projStatus,
                                    @RequestParam(value = "deptId",defaultValue = "")String deptId,
                                    @RequestParam(value = "isBid",defaultValue = "")String isBid,
                                    HttpServletResponse response
    ) {
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("projNo",projNo);
        param.put("projName",projName);
        param.put("bidOpenAtStart",bidOpenAtStart);
        param.put("bidOpenAtEnd",bidOpenAtEnd);
        param.put("publishAtStart",publishAtStart);
        param.put("publishAtEnd",publishAtEnd);
        param.put("projStatus",projStatus);
        param.put("deptId",deptId);
        param.put("isBid",isBid);
        projectService.exportProject(param,response);
    }


    /**
     * 招标项目投标状态修改
     * @return
     */
    @ApiOperation(value = "招标项目投标状态修改")
    @ApiImplicitParam(name = "status",value = "投标归档status传Complete")
    @RequestMapping(method = RequestMethod.GET,value = "/status/{bidProjId}")
    public AppMessage statusBidProject(@PathVariable("bidProjId") String bidProjId,@RequestParam("status")String status) {
        return projectService.statusBidProject(bidProjId,status);
    }

    /**
     * 招标项目会审状态修改
     * @return
     */
    @ApiOperation(value = "招标项目会审状态修改")
    @ApiImplicitParam(name = "isJointCheckup",value = "如果需要会审，则为1，取消会审为0")
    @RequestMapping(method = RequestMethod.GET,value = "/isJointCheckup/{bidProjId}")
    public AppMessage isJointCheckupBidProject(@PathVariable("bidProjId") String bidProjId,@RequestParam("isJointCheckup")int isJointCheckup) {
        return projectService.isJointCheckupBidProject(bidProjId,isJointCheckup);
    }

    /**
     * 发起标书会审
     * @param bidProjId
     * @return
     */
    @ApiOperation(value = "发起标书会审")
    @RequestMapping(method = RequestMethod.GET,value = "/initActiviti/{bidProjId}")
    public AppMessage initActivitiBidProject(@PathVariable("bidProjId") String bidProjId) {
        return projectService.initActivitiBidProject(bidProjId);
    }

    /**
     * 查询标书会审审批
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询标书会审审批")
    @RequestMapping(method = RequestMethod.GET,value = "/selectActiviti")
    public AppMessage selectActivitiBidProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> param = new HashMap<>(8);
        HashMap<String,Object> dataMap = projectService.selectActivitiBidProject(pageSize,pageNum,param);
        return AppMessage.success(getDataTable((List<BidProjectPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询标书会审审批成功");
    }

    /**
     * 查询审核详情
     *
     * @return
     */
    @ApiOperation("查询审核详情")
    @RequestMapping(value = "/step/details", method = RequestMethod.GET)
    public AppMessage queryDetails(@RequestParam(value = "objId", defaultValue = "") String objId,@RequestParam(value = "objType", defaultValue = "") String objType) {
        List<CheckStepPo> checkStepPoList = diyCheckStepService.selectBidProjectDetails(objId,objType);
        return AppMessage.success(checkStepPoList, "查询审核详情成功！");
    }

    /**
     * 审核操作
     *
     * @return
     */
    @ApiOperation("审核操作")
    @RequestMapping(value = "/step/audit", method = RequestMethod.POST)
    public AppMessage audit( @RequestBody AuditVo auditVo) throws Exception {

        boolean isSuccess = projectService.audit(auditVo);

        return AppMessage.success(isSuccess, "审核成功！");
    }


    /**
     * 自定义审核流程
     * @param auditVo
     * @return
     * @throws Exception
     */
    @ApiOperation("自定义审核流程")
    @RequestMapping(value="/step/diy",method = POST)
    public AppMessage add( @RequestBody AuditVo auditVo) throws Exception {
        String userId= ServletUtils.getSessionUserId();
        if(projectService.saveProDiyCheckStep(auditVo,userId)){
            return AppMessage.result("新增自定义审核流程成功");
        }
        return AppMessage.error("新增自定义审核流程失败");
    }

    /**
     * 查询招标流程中事项
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询招标流程中事项")
    @RequestMapping(method = RequestMethod.GET,value = "/activitiItem")
    public AppMessage selectActivitiItemBidProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                                   @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> param = new HashMap<>(8);
        HashMap<String,Object> dataMap = projectService.selectActivitiItemBidProject(pageSize,pageNum,param);
        return AppMessage.success(getDataTable((List<BidProjectPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询招标流程中事项成功");
    }

    /**
     * 修改招标项目信息
     *
     * @return
     */
    @ApiOperation(value = "修改招标项目信息")
    @RequestMapping(method = RequestMethod.PUT, value = "/update")
    public AppMessage updateBidProject(@RequestBody BidProjectVo projectVo) {
        return projectService.updateBidProject(projectVo);
    }

    /**
     * 查询中标结果公司
     *
     * @return
     */
    @ApiOperation(value = "查询中标结果公司")
    @RequestMapping(method = RequestMethod.GET, value = "/bidder/{bidProjId}")
    public AppMessage selectProBidder(@PathVariable("bidProjId") String bidProjId) {
        return projectService.selectProBidder(bidProjId);
    }

    /**
     * 投标资料申请提交
     *
     * @return
     */
    @ApiOperation(value = "投标资料申请提交")
    @RequestMapping(method = POST, value = "/submit")
    public AppMessage submitBidTender(@RequestBody BidCertiBorrowVo certiBorrowVo) {
        return projectService.submitBidTender(certiBorrowVo);
    }

    /**
     * 查询企业资质审批
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询企业资质审批")
    @RequestMapping(method = RequestMethod.GET,value = "/selectActiviti/bidTender")
    public AppMessage selectActivitiBidTender(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> param = new HashMap<>(8);
        HashMap<String,Object> dataMap = projectService.selectActivitiBidTender(pageSize,pageNum,param);
        return AppMessage.success(getDataTable((List<BidProjectPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询企业资质审批成功");
    }

    /**
     * 查询人事资料审批
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询人事资料审批")
    @RequestMapping(method = RequestMethod.GET,value = "/selectActiviti/personnel")
    public AppMessage selectActivitiPersonnel(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                              @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> param = new HashMap<>(8);
        HashMap<String,Object> dataMap = projectService.selectActivitiPersonnel(pageSize,pageNum,param);
        return AppMessage.success(getDataTable((List<BidProjectPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询人事资料审批成功");
    }

    /**
     * 查询投标资料申请
     *
     * @return
     */
    @ApiOperation(value = "查询投标资料申请")
    @RequestMapping(method = RequestMethod.GET, value = "/bidTender/{bidProjId}")
    public AppMessage selectBidTender(@PathVariable("bidProjId") String bidProjId) {
        return projectService.selectBidTender(bidProjId);
    }

    /**
     * 查询投标资料申请
     *
     * @return
     */
    @ApiOperation(value = "查询投企业资质或者人事资料详情")
    @RequestMapping(method = RequestMethod.GET, value = "/certiBorrow/{certiBorrowId}")
    public AppMessage selectCertiBorrowDetail(@PathVariable("certiBorrowId") String certiBorrowId) {
        return projectService.selectCertiBorrowDetail(certiBorrowId);
    }

    /**
     * 投标终止
     *
     * @return
     */
    @ApiOperation(value = "投标终止")
    @RequestMapping(method = RequestMethod.GET, value = "/stop/{bidProjId}")
    public AppMessage stopBidding(@PathVariable("bidProjId") String bidProjId) {
        return projectService.stopBidding(bidProjId);
    }

    /**
     * 查询投标终止审批
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询投标终止审批")
    @RequestMapping(method = RequestMethod.GET,value = "/selectStopActiviti")
    public AppMessage selectStopBidProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                           @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> param = new HashMap<>(8);
        HashMap<String,Object> dataMap = projectService.selectStopBidProject(pageSize,pageNum,param);
        return AppMessage.success(getDataTable((List<BidProjectPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询投标终止审批成功");
    }


    /**
     * 查询投标项目待办事项
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查询投标项目待办事项")
    @RequestMapping(method = RequestMethod.GET,value = "/auditingActiviti")
    public AppMessage selectAuditingBidProject(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        HashMap<String, Object> param = new HashMap<>(8);
        HashMap<String,Object> dataMap = projectService.selectAuditingBidProject(pageSize,pageNum,param);
        return AppMessage.success(getDataTable((List<BidProjectPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询投标项目待办事项成功");
    }

    /**
     * 公司资质图片下载
     * @return
     */
    @ApiOperation(value = "公司资质图片下载")
    @RequestMapping(method = RequestMethod.GET,value = "/export/{certiBorrowId}/{userCertiId}")
    public AppMessage exportCerti(@PathVariable("certiBorrowId") String certiBorrowId,@PathVariable("userCertiId") String userCertiId,HttpServletResponse response) {
        return projectService.exportCerti(certiBorrowId,response,userCertiId);
    }

    /**
     * 查询投标资料申请详情
     *
     * @return
     */
    @ApiOperation(value = "查询投标资料申请详情")
    @RequestMapping(method = RequestMethod.GET, value = "/bidTender/detail")
    public AppMessage selectBidTenderDetail(@RequestParam("certiBorrowId")String certiBorrowId) {
        return projectService.selectBidTenderDetail(certiBorrowId);
    }

    /**
     * 新增资质借取
     *
     * @return
     */
    @ApiOperation(value = "新增资质借取")
    @RequestMapping(method = POST, value = "/add/certiBorrow")
    public AppMessage addCertiBorrow(@RequestBody BidCertiBorrowVo certiBorrowVo) {
        return projectService.addCertiBorrow(certiBorrowVo);
    }

    /**
     * 查询资质借取
     * @return
     */
    @ApiOperation(value = "查询资质借取")
    @RequestMapping(method = RequestMethod.GET,value = "/selectCertiBorrow")
    public AppMessage selectCertiBorrow(@RequestParam(value = "pageNum", defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", defaultValue = "20") int pageSize
    ) {
        HashMap<String, Object> param = new HashMap<>(8);
        String userId = ServletUtils.getSessionUserId();
        String deptId = userService.selectByKey(userId).getDeptId();
        param.put("deptId",deptId);
        HashMap<String,Object> dataMap = projectService.selectCertiBorrow(pageSize,pageNum,param);
        return AppMessage.success(getDataTable((List<BidCertiBorrowPo>)dataMap.get("data"),(long)dataMap.get("total")),"查询资质借取成功");
    }

    /**
     * 删除资质借取
     * @return
     */
    @ApiOperation(value = "删除资质借取")
    @RequestMapping(method = RequestMethod.GET,value = "/deleteCertiBorrow")
    public AppMessage deleteCertiBorrow(@RequestParam(value = "certiBorrowId", defaultValue = "") String certiBorrowId
    ) {
        return projectService.deleteCertiBorrow(certiBorrowId);
    }


    /**
     * 修改资质借取
     *
     * @return
     */
    @ApiOperation(value = "修改资质借取")
    @RequestMapping(method = POST, value = "/update/certiBorrow")
    public AppMessage updateCertiBorrow(@RequestBody BidCertiBorrowVo certiBorrowVo) {
        return projectService.updateCertiBorrow(certiBorrowVo);
    }

    /**
     * 导出 投标项目表.pdf
     *
     * @return
     */
    @RequestMapping(value = "/pdf", method = RequestMethod.GET)
    public void pdf(HttpServletResponse response,
                    @RequestParam(value = "bidProjId", defaultValue = "") String bidProjId) throws Exception {

        try {
            // 下载到浏览器
            String pdfName = "投标项目信息表.pdf" ;

            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            projectService.buildBidProjectPDF(response, TEMPURL, PDFPicUrl, baseFontUrl,bidProjId);

        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }

    }
}

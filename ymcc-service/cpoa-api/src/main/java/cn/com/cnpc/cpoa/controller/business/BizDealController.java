package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectAttachDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectWorkDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.enums.prodsys.ContractStateEnum;
import cn.com.cnpc.cpoa.enums.prodsys.ContractTypeEnum;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.service.prodsys.FusionReportService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.pdf.PDFBuildUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.utils.word.WordTemplate;
import cn.com.cnpc.cpoa.vo.*;
import cn.com.cnpc.cpoa.vo.prodsys.BizProjectParamVo;
import cn.com.cnpc.cpoa.vo.prodsys.BizProjectVo;
import cn.com.cnpc.cpoa.vo.prodsys.BizProjectWorkVo;
import cn.com.cnpc.cpoa.vo.prodsys.SyncRet;
import cn.com.cnpc.cpoa.web.base.BaseController;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 合同控制器
 *
 * @author scchenyong@189.cn
 */
@Api(tags = "合同控制器")
@RestController
@RequestMapping("/deal")
public class BizDealController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(BizDealController.class);
    @Autowired
    private BizDealBizService bizDealBizService;
    @Autowired
    private FusionReportService fusionReportService;

    @Autowired
    private BizDealinService bizDealinService;
    @Autowired
    private BizDealZlhbService bizDealZlhbService;
    @Autowired
    DealService dealService;
    @Autowired
    AttachService attachService;

    @Autowired
    DeptService deptService;

    @Autowired
    UserService userService;

    @Autowired
    ImportLogService importLogService;

    @Autowired
    SettlementService settlementService;

    @Autowired
    private ActivitiService activitiService;


    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    private BizCheckStepService diyCheckStepService;

    @Autowired
    private BizDealExService bizDealExService;
    //
    @Autowired
    private BizDealHdService bizDealHdService;

    @Autowired
    WxMessageService wxMessageService;

    @Autowired
    CheckNoticeService checkNoticeService;

    @Autowired
    LogService logService;

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;


    @Value("${file.wordTemplateUrl}")
    private String wordTemplateUrl;

    @Value("${file.tempurl}")
    private String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private String baseFontUrl;

    @Value("${templateId.passTemplateId}")
    private String passTemplateId;

    @ApiOperation("修改合同")
    @ApiImplicitParam(name = "status", value = "如果为修改则传update")
    @Log(logContent = "修改合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id, @RequestBody DealParamVo paramVo) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        BizDealDto deal = dealService.selectByKey(id);
        if (null == deal) {
            throw new AppException("当前合同不存在");
        }

        DealVo dealVo = paramVo.getDealVo();
        List<AttachVo> attachVos = paramVo.getAttachVos();
        String type = paramVo.getType();
        //准备
        BizDealDto dealDto = new BizDealDto();
        List<BizAttachDto> attachDtos = new ArrayList<>();
        List<BizDealAttachDto> dealAttachDtos = new ArrayList<>();

        //查询用户所在部门
        SysUserDto sysUserDto = userService.selectByKey(userId);

        //只能修改草稿 和 回退合同。前端控制
        //boolean canUpdate=dealService.canUpdate(dealVo.getDealStatus());
        if (true) {
            dealDto.setDealId(id);
            dealDto.setDealName(dealVo.getDealName());
            dealDto.setDealValue(dealVo.getDealValue());
            dealDto.setCategoryId(dealVo.getCategoryId());
            dealDto.setDealSignTime(dealVo.getDealSignTime());
            dealDto.setDeptId(sysUserDto.getDeptId());
//            dealDto.setDealbiz(dealVo.getDealBiz());
            dealDto.setContractId(dealVo.getContractId());
            dealDto.setDealIncome(dealVo.getDealIncome());
            dealDto.setDealFunds(dealVo.getDealFunds());
            dealDto.setDealReportNo(dealVo.getDealReportNo());
            dealDto.setDealContract(dealVo.getDealContract());
            dealDto.setDealDispute(dealVo.getDealDispute());
            dealDto.setDealStart(dealVo.getDealStart());
            dealDto.setDealEnd(dealVo.getDealEnd());
            dealDto.setDealSelection(dealVo.getDealSelection());
            dealDto.setDealSettlement(dealVo.getDealSettlement());
            dealDto.setSettleDate(dealVo.getSettleDate());
            dealDto.setDealNotes(dealVo.getDealNotes());
            dealDto.setDealCurrency(dealVo.getDealCurrency());
            dealDto.setSubtypeId(dealVo.getSubtypeId());
            dealDto.setDealStatus(DealWebTypeEnum.getEnumByKey(type));
            dealDto.setDealType(dealVo.getDealType());
            dealDto.setPaymentType(dealVo.getPaymentType());
            dealDto.setPaymentTeq(dealVo.getPaymentTeq());
            dealDto.setDealValueAfter(dealVo.getDealValueAfter());
            dealDto.setDealValueBefore(dealVo.getDealValueBefore());
            dealDto.setHaveTax(dealVo.getHaveTax());
            dealDto.setTaxRate(dealVo.getTaxRate());
            dealDto.setDealNo(dealVo.getDealNo());
            dealDto.setProjectNo(dealVo.getProjectNo());
            //2 附件
            for (AttachVo attachVo : attachVos) {
                //String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");

                if (attachService.isDoubleFile(attachVos)) {
                    return AppMessage.errorObjId(id, "不能上传重复的文件！");
                }

                String attachId = attachVo.getAttachId();

                BizAttachDto attachDto = new BizAttachDto();
                attachDto.setAttachId(attachId);
                attachDto.setOwnerId(id);
                // attachDto.setOwnerType(FileOwnerTypeEnum.DEAL.getKey());
                attachDto.setFileName(attachVo.getFileName());
                attachDto.setFileType(attachVo.getFileType());
                attachDto.setFileUri(attachVo.getFileUri());
                attachDto.setFileSize(attachVo.getFileSize());
                attachDto.setCreateTime(DateUtils.getNowDate());
                attachDto.setUserId(attachVo.getUserId());
                attachDto.setNotes(attachVo.getNotes());
                attachDtos.add(attachDto);

                // 3合同附件
                BizDealAttachDto dealAttachDto = new BizDealAttachDto();
                dealAttachDto.setId(StringUtils.getUuid32());
                dealAttachDto.setDealId(id);
                dealAttachDto.setAttachId(attachId);
                dealAttachDtos.add(dealAttachDto);
            }
            boolean updateChain = dealService.updateChain(dealDto, attachDtos, dealAttachDtos, type, userId, paramVo.getStatus());
            if (updateChain) {

                if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type) || DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)) {
                    //生成合同履行统计记录
                    bizDealStatisticsService.addDealStatistics(dealDto);
                }

                return AppMessage.success(id, "更新合同成功");
            }
        } else {
            return AppMessage.errorObjId(id, "更新合同失败,合同正在审核中");
        }

        return AppMessage.errorObjId(id, "更新合同失败");
    }


    @Log(logContent = "删除合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        //不能删除已经存在结算的合同上
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", id);
        List<BizSettlementDto> settlementDtos = settlementService.selectList(param);
        if (null != settlementDtos && settlementDtos.size() > 0) {
            return AppMessage.errorObjId(id, "删除合同失败,该合同已存在结算");
        }
        String dealNo = dealService.selectByKey(id).getDealNo();

        boolean delete = dealService.deleteDealAttach(id);
        if (delete) {
            return AppMessage.success(dealNo, "删除合同成功");
        }
        return AppMessage.errorObjId(dealNo, "删除合同失败");
    }

    @ApiOperation("删除宏大合同")
    @Log(logContent = "删除宏大合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "hd/{id}", method = RequestMethod.DELETE)
    public AppMessage hddelete(@PathVariable String id) {
        //不能删除已经存在结算的合同上
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", id);
        List<BizSettlementDto> settlementDtos = settlementService.selectList(param);
        if (null != settlementDtos && settlementDtos.size() > 0) {
            return AppMessage.errorObjId(id, "删除合同失败,该合同已存在结算");
        }
        BizDealHdDto bizDealHdDto = bizDealHdService.selectHd(param);
        String dealNo = bizDealHdDto.getDealNo();
        boolean delete = bizDealHdService.deleteDealAttach(id);
        if (delete) {
            return AppMessage.success(dealNo, "删除合同成功");
        }
        return AppMessage.errorObjId(dealNo, "删除合同失败");
    }

//    @ApiOperation("修改宏大合同")
//    @ApiImplicitParam(name = "status",value = "如果为修改则传update")
//    @Log(logContent = "修改宏大合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
//    @RequestMapping(value = "/hd/{id}", method = RequestMethod.PUT)
//    public AppMessage hdupdate( @PathVariable String id, @RequestBody DealParamVo paramVo) throws Exception {
//        String userId = ServletUtils.getSessionUserId();
//
//        BizDealHdDto deal = bizDealHdService.selectByKey(id);
//        if (null == deal) {
//            throw new AppException("当前合同不存在");
//        }
//
//        DealVo dealVo = paramVo.getDealVo();
//        List<AttachVo> attachVos = paramVo.getAttachVos();
//        String type = paramVo.getType();
//        //准备
//        BizDealHdDto dealDto = new BizDealHdDto();
//        List<BizAttachDto> attachDtos = new ArrayList<>();
//        List<BizDealAttachDto> dealAttachDtos = new ArrayList<>();
//
//        //查询用户所在部门
//        SysUserDto sysUserDto = userService.selectByKey(userId);
//
//        //只能修改草稿 和 回退合同。前端控制
//        //boolean canUpdate=dealService.canUpdate(dealVo.getDealStatus());
//        if (true) {
//            dealDto.setDealId(id);
//            dealDto.setDealName(dealVo.getDealName());
//            dealDto.setDealValue(dealVo.getDealValue());
//            dealDto.setCategoryId(dealVo.getCategoryId());
//            dealDto.setDealSignTime(dealVo.getDealSignTime());
//            dealDto.setDeptId(sysUserDto.getDeptId());
//            dealDto.setDealbiz(dealVo.getDealBiz());
//            dealDto.setContractId(dealVo.getContractId());
//            dealDto.setDealIncome(dealVo.getDealIncome());
//            dealDto.setDealFunds(dealVo.getDealFunds());
//            dealDto.setDealReportNo(dealVo.getDealReportNo());
//            dealDto.setDealContract(dealVo.getDealContract());
//            dealDto.setDealDispute(dealVo.getDealDispute());
//            dealDto.setDealStart(dealVo.getDealStart());
//            dealDto.setDealEnd(dealVo.getDealEnd());
//            dealDto.setDealSelection(dealVo.getDealSelection());
//            dealDto.setDealSettlement(dealVo.getDealSettlement());
//            dealDto.setSettleDate(dealVo.getSettleDate());
//            dealDto.setDealNotes(dealVo.getDealNotes());
//            dealDto.setDealCurrency(dealVo.getDealCurrency());
//            dealDto.setSubtypeId(dealVo.getSubtypeId());
//            dealDto.setDealStatus(DealWebTypeEnum.getEnumByKey(type));
//            dealDto.setDealType(dealVo.getDealType());
//            dealDto.setPaymentType(dealVo.getPaymentType());
////            dealDto.setPaymentTeq(dealVo.getPaymentTeq());
//            dealDto.setDealValueAfter(dealVo.getDealValueAfter());
//            dealDto.setDealValueBefore(dealVo.getDealValueBefore());
//            dealDto.setHaveTax(dealVo.getHaveTax());
//            dealDto.setTaxRate(dealVo.getTaxRate());
//            dealDto.setDealNo(dealVo.getDealNo());
//            dealDto.setProjectNo(dealVo.getProjectNo());
//            //2 附件
//            for (AttachVo attachVo : attachVos) {
//                //String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");
//
//                if (attachService.isDoubleFile(attachVos)) {
//                    return AppMessage.errorObjId(id, "不能上传重复的文件！");
//                }
//
//                String attachId = attachVo.getAttachId();
//
//                BizAttachDto attachDto = new BizAttachDto();
//                attachDto.setAttachId(attachId);
//                attachDto.setOwnerId(id);
//                // attachDto.setOwnerType(FileOwnerTypeEnum.DEAL.getKey());
//                attachDto.setFileName(attachVo.getFileName());
//                attachDto.setFileType(attachVo.getFileType());
//                attachDto.setFileUri(attachVo.getFileUri());
//                attachDto.setFileSize(attachVo.getFileSize());
//                attachDto.setCreateTime(DateUtils.getNowDate());
//                attachDto.setUserId(attachVo.getUserId());
//                attachDto.setNotes(attachVo.getNotes());
//                attachDtos.add(attachDto);
//
//                // 3合同附件
//                BizDealAttachDto dealAttachDto = new BizDealAttachDto();
//                dealAttachDto.setId(StringUtils.getUuid32());
//                dealAttachDto.setDealId(id);
//                dealAttachDto.setAttachId(attachId);
//                dealAttachDtos.add(dealAttachDto);
//            }
//            boolean updateChain = bizDealHdService.updateChain(dealDto, attachDtos, dealAttachDtos, type, userId,paramVo.getStatus());
//            if (updateChain) {
//
////                if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)||DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)){
////                    //生成合同履行统计记录
////                    bizDealStatisticsService.addDealStatistics(dealDto);
////                }
//
//                return AppMessage.success(id, "更新合同成功");
//            }
//        } else {
//            return AppMessage.errorObjId(id, "更新合同失败,合同正在审核中");
//        }
//
//        return AppMessage.errorObjId(id, "更新合同失败");
//    }


    @ApiOperation("排除合同")
    @Log(logContent = "排除合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/exclude", method = RequestMethod.POST)
    public AppMessage exclude(@RequestBody DealParamVo dealexVo) throws Exception {

        Map<String, Object> params = new HashMap<>();
        String dealId1 = dealexVo.getDealVo().getDealId();
        params.put("dealId", dealId1);
//        bizDealinService.delete(dealId1);
//        List<BizAttachDto> attachVos = attachService.selectListByDealId(params);
        String userId = ServletUtils.getSessionUserId();
        String type = dealexVo.getType();
        //准备
        BizDealExDto bizDealExDto = new BizDealExDto();

        //查询用户所在部门
        SysUserDto sysUserDto = userService.selectByKey(userId);

        //1保存合同
        String dealId = dealexVo.getDealVo().getDealId();
        bizDealExDto.setDealId(dealId);
        bizDealExDto.setDealNo(dealexVo.getDealVo().getDealNo());
        bizDealExDto.setProjectNo(dealexVo.getDealVo().getProjectNo());
//        bizDealExDto.setDealbiz(dealexVo.getDealVo().getDealBiz());

        bizDealExDto.setDealName(dealexVo.getDealVo().getDealName());
        bizDealExDto.setDealValue(dealexVo.getDealVo().getDealValue());
        bizDealExDto.setCategoryId(dealexVo.getDealVo().getCategoryId());
        bizDealExDto.setDealSignTime(dealexVo.getDealVo().getDealSignTime());
        bizDealExDto.setDeptId(sysUserDto.getDeptId());
        bizDealExDto.setContractId(dealexVo.getDealVo().getContractId());
        bizDealExDto.setDealIncome(dealexVo.getDealVo().getDealIncome());
        bizDealExDto.setDealFunds(dealexVo.getDealVo().getDealFunds());
        bizDealExDto.setDealReportNo(dealexVo.getDealVo().getDealReportNo());
        bizDealExDto.setDealContract(dealexVo.getDealVo().getDealContract());
        bizDealExDto.setDealDispute(dealexVo.getDealVo().getDealDispute());
        bizDealExDto.setUserId(dealexVo.getDealVo().getUserId());
        bizDealExDto.setDealStart(dealexVo.getDealVo().getDealStart());
        bizDealExDto.setDealEnd(dealexVo.getDealVo().getDealEnd());
        bizDealExDto.setDealSelection(dealexVo.getDealVo().getDealSelection());
        bizDealExDto.setDealSettlement(dealexVo.getDealVo().getDealSettlement());
        bizDealExDto.setSettleDate(dealexVo.getDealVo().getSettleDate());
        bizDealExDto.setDealNotes(dealexVo.getDealVo().getDealNotes());
        bizDealExDto.setCreateAt(dealexVo.getDealVo().getCreateAt());
        bizDealExDto.setDealCurrency(dealexVo.getDealVo().getDealCurrency());
        bizDealExDto.setSubtypeId(dealexVo.getDealVo().getSubtypeId());
        bizDealExDto.setDealStatus(dealexVo.getDealVo().getDealStatus());
        bizDealExDto.setDealType(dealexVo.getDealVo().getDealType());
        bizDealExDto.setPaymentType(dealexVo.getDealVo().getPaymentType());
//        bizDealExDto.setPaymentReq(dealexVo.getDealVo().getPaymentReq());
        bizDealExDto.setDealValueAfter(dealexVo.getDealVo().getDealValueAfter());
        bizDealExDto.setDealValueBefore(dealexVo.getDealVo().getDealValueBefore());
        bizDealExDto.setHaveTax(dealexVo.getDealVo().getHaveTax());
        bizDealExDto.setTaxRate(dealexVo.getDealVo().getTaxRate());
        bizDealExDto.setChangeDesc(dealexVo.getDealVo().getChangeDesc());
        bizDealExDto.setPlacedTime(new Date());
//        bizDealExService.save(bizDealExDto);
//        //2 附件
//        for (BizAttachDto attachVo : attachVos) {
//            // String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");
//            String attachId = attachVo.getAttachId();
//            BizAttachDto attachDto = new BizAttachDto();
//            attachDto.setAttachId(attachId);
//            attachDto.setOwnerId(dealId);
//            attachDto.setOwnerType(FileOwnerTypeEnum.DEAL.getKey());
//            attachDto.setFileName(attachVo.getFileName());
//            attachDto.setFileType(attachVo.getFileType());
//            attachDto.setFileUri(attachVo.getFileUri());
//            attachDto.setFileSize(attachVo.getFileSize());
//            attachDto.setCreateTime(DateUtils.getNowDate());
//            attachDto.setUserId(userId);
//            attachDto.setNotes(attachVo.getNotes());
//            attachDtos.add(attachDto);
//
//            // 3合同附件
//            BizDealAttachDto dealAttachDto = new BizDealAttachDto();
//            dealAttachDto.setId(StringUtils.getUuid32());
//            dealAttachDto.setDealId(dealId);
//            dealAttachDto.setAttachId(attachId);
//            //TODO  新增合同上传的附件增加附件类型，类型为deal
//            dealAttachDto.setDealFileType("deal");
//            dealAttachDtos.add(dealAttachDto);
//        }
        if (!bizDealExService.saveChain(bizDealExDto, type)) {
            return AppMessage.error("新增合同出错");
        }

//        if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
//            //生成合同履行统计记录
//            bizDealStatisticsService.addDealStatistics(bizDealExDto);
//        }
        return AppMessage.success(dealId, "排除合同");


    }

    @ApiOperation("加入合同")
    @Log(logContent = "加入合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/joinht", method = RequestMethod.POST)
    public AppMessage joinht(@RequestBody DealParamVo dealinVo) throws Exception {

        Map<String, Object> params = new HashMap<>();
        String dealId1 = dealinVo.getDealVo().getDealId();
        params.put("dealId", dealId1);
        List<BizAttachDto> attachVos = attachService.selectListByDealId(params);

        String userId = ServletUtils.getSessionUserId();
        String type = dealinVo.getType();
        //准备
        BizDealInDto bizDealInDto = new BizDealInDto();
        List<BizAttachDto> attachDtos = new ArrayList<>();
        List<BizDealAttachDto> dealAttachDtos = new ArrayList<>();

        //查询用户所在部门
        SysUserDto sysUserDto = userService.selectByKey(userId);

        //1保存合同
//        String dealId = StringUtils.getUuid32();
        bizDealInDto.setDealId(dealId1);
        bizDealInDto.setDealIncome(dealinVo.getDealVo().getDealIncome());
        bizDealInDto.setDealNo(dealinVo.getDealVo().getDealNo());
        bizDealInDto.setProjectNo(dealinVo.getDealVo().getProjectNo());
        bizDealInDto.setDealName(dealinVo.getDealVo().getDealName());
        bizDealInDto.setDealBiz(dealinVo.getDealVo().getDealBiz());
//        bizDealInDto.setMarketType(dealinVo.getDealVo().getMarketType());
//        bizDealInDto.setCompanyType(dealinVo.getDealVo().getCompanyType());
//        bizDealInDto.setContractType(dealinVo.getDealVo().getcon);
//        bizDealInDto.setWorkZone(dealinVo.getDealVo().getWorkZone());
        bizDealInDto.setDealValue(dealinVo.getDealVo().getDealValue());
        bizDealInDto.setCategoryId(dealinVo.getDealVo().getCategoryId());
        bizDealInDto.setDealSignTime(dealinVo.getDealVo().getDealSignTime());
        bizDealInDto.setDeptId(sysUserDto.getDeptId());
        bizDealInDto.setContractId(dealinVo.getDealVo().getContractId());
        bizDealInDto.setDealIncome(dealinVo.getDealVo().getDealIncome());
        bizDealInDto.setDealFunds(dealinVo.getDealVo().getDealFunds());
        bizDealInDto.setDealReportNo(dealinVo.getDealVo().getDealReportNo());
        bizDealInDto.setDealContract(dealinVo.getDealVo().getDealContract());
        bizDealInDto.setDealDispute(dealinVo.getDealVo().getDealDispute());
        bizDealInDto.setUserId(dealinVo.getDealVo().getUserId());
        bizDealInDto.setDealStart(dealinVo.getDealVo().getDealStart());
        bizDealInDto.setDealEnd(dealinVo.getDealVo().getDealEnd());
        bizDealInDto.setDealSelection(dealinVo.getDealVo().getDealSelection());
        bizDealInDto.setDealSettlement(dealinVo.getDealVo().getDealSettlement());
        bizDealInDto.setSettleDate(dealinVo.getDealVo().getSettleDate());
        bizDealInDto.setDealNotes(dealinVo.getDealVo().getDealNotes());
        bizDealInDto.setCreateAt(dealinVo.getDealVo().getCreateAt());
        bizDealInDto.setDealCurrency(dealinVo.getDealVo().getDealCurrency());
        bizDealInDto.setSubtypeId(dealinVo.getDealVo().getSubtypeId());
        bizDealInDto.setDealStatus(dealinVo.getDealVo().getDealStatus());
        bizDealInDto.setDealType(dealinVo.getDealVo().getDealType());
        bizDealInDto.setPaymentType(dealinVo.getDealVo().getPaymentType());
//        bizDealInDto.setPaymentReq(dealinVo.getDealVo().getPaymentReq());
        bizDealInDto.setDealValueAfter(dealinVo.getDealVo().getDealValueAfter());
        bizDealInDto.setDealValueBefore(dealinVo.getDealVo().getDealValueBefore());
        bizDealInDto.setHaveTax(dealinVo.getDealVo().getHaveTax());
        bizDealInDto.setTaxRate(dealinVo.getDealVo().getTaxRate());
        bizDealInDto.setChangeDesc(dealinVo.getDealVo().getChangeDesc());
        bizDealInDto.setPlacedTime(new Date());
//        bizDealExService.save(bizDealInDto);
        //2 附件
        for (BizAttachDto attachVo : attachVos) {
            // String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");
            String attachId = attachVo.getAttachId();
            BizAttachDto attachDto = new BizAttachDto();
            attachDto.setAttachId(attachId);
            attachDto.setOwnerId(dealId1);
            attachDto.setOwnerType(FileOwnerTypeEnum.DEAL.getKey());
            attachDto.setFileName(attachVo.getFileName());
            attachDto.setFileType(attachVo.getFileType());
            attachDto.setFileUri(attachVo.getFileUri());
            attachDto.setFileSize(attachVo.getFileSize());
            attachDto.setCreateTime(DateUtils.getNowDate());
            attachDto.setUserId(userId);
            attachDto.setNotes(attachVo.getNotes());
            attachDtos.add(attachDto);

            // 3合同附件
//            BizDealAttachDto dealAttachDto = new BizDealAttachDto();
//            dealAttachDto.setId(StringUtils.getUuid32());
//            dealAttachDto.setDealId(dealId1);
//            dealAttachDto.setAttachId(attachId);
//            //TODO  新增合同上传的附件增加附件类型，类型为deal
//            dealAttachDto.setDealFileType("deal");
//            dealAttachDtos.add(dealAttachDto);
        }
        if (!bizDealinService.saveChain(bizDealInDto, attachDtos, dealAttachDtos, type)) {
            return AppMessage.error("新增合同出错");
        }

        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            //生成合同履行统计记录
            bizDealStatisticsService.addDealStatistics(bizDealInDto);
        }
        return AppMessage.success("dealId", "新增合同成功");


    }

    @ApiOperation("添加业务板块")
    @Log(logContent = "添加业务板块", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/business", method = RequestMethod.PUT)
    public AppMessage business(@RequestBody BusinessVo dealinVo) throws Exception {

        List<NessVo> nessVo = dealinVo.getNessVo();
        List<NessVo> filteredList = nessVo.stream()
                .filter(nessVo1 -> !nessVo1.getDealType().equals("INSTRUCTION") && !nessVo1.getDealType().equals("TRANSFER"))
                .collect(Collectors.toList());
        List<NessVo> filteredList2 = nessVo.stream()
                .filter(nessVow -> nessVow.getDealType().equals("INSTRUCTION") || nessVow.getDealType().equals("TRANSFER"))
                .collect(Collectors.toList());
        List<String> ids = filteredList2.stream()
                .map(NessVo::getId)
                .collect(Collectors.toList());
        String dealBiz1 = dealinVo.getDealBiz();
        if (filteredList2.size()!=0){
            bizDealZlhbService.updateByid(ids,dealBiz1);

        }
        for (NessVo vo : filteredList) {
            Map<String, Object> map = new HashMap<>();
            String dealBiz = dealinVo.getDealBiz();

            String dealType = vo.getDealType();
            String id = vo.getId();
            map.put("dealId", id);
            BizDealBizDto bizDealBizDto = new BizDealBizDto();

            List<BizDealBizDto> bizDealBizDtos = bizDealBizService.selectBydealBydealbiz(map);
            if (bizDealBizDtos.size() != 0) {
                bizDealBizDto.setDealBiz(dealBiz);
                bizDealBizDto.setDealId(id);
                bizDealBizDto.setDealNo(vo.getDealNo());
                bizDealBizService.updateAll(bizDealBizDto);
            } else {
                bizDealBizDto.setDealBiz(dealBiz);
                bizDealBizDto.setDealId(id);
                bizDealBizDto.setDealNo(vo.getDealNo());
                bizDealBizService.save(bizDealBizDto);
            }
        }


//        BizDealInDto bizDealDto = bizDealinService.selectByKey(id);
//        bizDealDto.setDealBiz(dealBiz);
//        bizDealinService.updateAll(bizDealDto);

        return AppMessage.success("dealId", "新增业务板块成功");


    }

    @ApiOperation("修改指令划拨业务板块")
    @Log(logContent = "修改指令划拨业务板块", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/zlhbbusiness", method = RequestMethod.PUT)
    public AppMessage zlhbbusiness(@RequestBody DealZlhbVo zlhbVo) throws Exception {

        List<String> ids = zlhbVo.getIds();
        String dealBiz = zlhbVo.getDealBiz();
        bizDealZlhbService.updateByid(ids,dealBiz);


//        BizDealInDto bizDealDto = bizDealinService.selectByKey(id);
//        bizDealDto.setDealBiz(dealBiz);
//        bizDealinService.updateAll(bizDealDto);

        return AppMessage.success("dealId", "新增业务板块成功");


    }

    @ApiOperation("新增宏大合同")
    @Log(logContent = "新增宏大合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/grand", method = RequestMethod.POST)
    public AppMessage grandsave( @RequestBody DealParamVo dealHdVo) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        DealVo dealHd = dealHdVo.getDealVo();
        List<AttachVo> attachVos = dealHdVo.getAttachVos();
        String type = dealHdVo.getType();
        //准备
//        BizDealExDto bizDealExDto = new BizDealExDto();
        List<BizAttachDto> attachDtos = new ArrayList<>();
        List<BizDealAttachDto> dealAttachDtos = new ArrayList<>();

        //查询用户所在部门
        SysUserDto sysUserDto = userService.selectByKey(userId);
        String dealId = StringUtils.getUuid32();
        BizDealHdDto bizDealHdDto = new BizDealHdDto();
        bizDealHdDto.setDealId(dealId);
        bizDealHdDto.setDealNo(bizDealHdService.getDealNo("HD"));
        bizDealHdDto.setProjectNo(dealHd.getProjectNo());
        bizDealHdDto.setDealName(dealHd.getDealName());
        bizDealHdDto.setDealValue(dealHd.getDealValue());
        bizDealHdDto.setCategoryId(dealHd.getCategoryId());
        bizDealHdDto.setDealSignTime(dealHd.getDealSignTime());
        bizDealHdDto.setDeptId(sysUserDto.getDeptId());
        bizDealHdDto.setContractId(dealHd.getContractId());
        bizDealHdDto.setDealIncome(dealHd.getDealIncome());
        bizDealHdDto.setDealFunds(dealHd.getDealFunds());
        bizDealHdDto.setDealReportNo(dealHd.getDealReportNo());
        bizDealHdDto.setDealContract(dealHd.getDealContract());
        bizDealHdDto.setDealDispute(dealHd.getDealDispute());
        bizDealHdDto.setUserId(userId);
        bizDealHdDto.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
        bizDealHdDto.setDealStart(dealHd.getDealStart());
        bizDealHdDto.setDealEnd(dealHd.getDealEnd());
        bizDealHdDto.setDealSelection(dealHd.getDealSelection());
        bizDealHdDto.setDealSettlement(dealHd.getDealSettlement());
        bizDealHdDto.setSettleDate(dealHd.getSettleDate());
        bizDealHdDto.setDealNotes(dealHd.getDealNotes());
        bizDealHdDto.setCreateAt(dealHd.getCreateAt());
        bizDealHdDto.setDealCurrency(dealHd.getDealCurrency());
        bizDealHdDto.setSubtypeId(dealHd.getSubtypeId());
//        bizDealHdDto.setDealStatus(dealHd.getDealStatus());
        bizDealHdDto.setDealType(dealHd.getDealType());
        bizDealHdDto.setPaymentType(dealHd.getPaymentType());
//        bizDealHdDto.setPaymentReq(dealHd.getPaymentReq());
        bizDealHdDto.setDealValueAfter(dealHd.getDealValueAfter());
        bizDealHdDto.setDealValueBefore(dealHd.getDealValueBefore());
        bizDealHdDto.setHaveTax(dealHd.getHaveTax());
        bizDealHdDto.setTaxRate(dealHd.getTaxRate());
        bizDealHdDto.setChangeDesc(dealHd.getChangeDesc());
        bizDealHdDto.setPlacedTime(new Date());
//        bizDealHdService.save(bizDealHdDto);
//2 附件
//        for (AttachVo attachVo : attachVos) {
//            // String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");
//            String attachId = attachVo.getAttachId();
//            BizAttachDto attachDto = new BizAttachDto();
//            attachDto.setAttachId(attachId);
//            attachDto.setOwnerId(dealId);
//            attachDto.setOwnerType(FileOwnerTypeEnum.DEAL.getKey());
//            attachDto.setFileName(attachVo.getFileName());
//            attachDto.setFileType(attachVo.getFileType());
//            attachDto.setFileUri(attachVo.getFileUri());
//            attachDto.setFileSize(attachVo.getFileSize());
//            attachDto.setCreateTime(DateUtils.getNowDate());
//            attachDto.setUserId(userId);
//            attachDto.setNotes(attachVo.getNotes());
//            attachDtos.add(attachDto);
//
//            // 3合同附件
//            BizDealAttachDto dealAttachDto = new BizDealAttachDto();
//            dealAttachDto.setId(StringUtils.getUuid32());
//            dealAttachDto.setDealId(dealId);
//            dealAttachDto.setAttachId(attachId);
//            //TODO  新增合同上传的附件增加附件类型，类型为deal
//            dealAttachDto.setDealFileType("deal");
//            dealAttachDtos.add(dealAttachDto);
//        }
        if (!bizDealHdService.saveChain(bizDealHdDto, type)) {
            return AppMessage.error("新增合同出错");
        }

//        if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
//            //生成合同履行统计记录
//            bizDealStatisticsService.addDealStatistics(bizDealExDto);
//        }
        return AppMessage.success(dealId, "新增合同成功");
    }



    @ApiOperation("查询宏大合同")
    @Log(logContent = "查询宏大合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/querygrand", method = RequestMethod.GET)
    public AppMessage querygrandsave(@RequestParam(value = "dealId", defaultValue = "", required = false) String dealId) throws Exception {
        BizDealHdDto bizDealHdDto = bizDealHdService.selectByKey(dealId);
        return AppMessage.success(bizDealHdDto, "查询合同成功");
    }








    @ApiOperation("新增指令划拨合同")
    @Log(logContent = "新增指令划拨合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/zlhb", method = RequestMethod.POST)
    public AppMessage zlhbsave( @RequestBody DealParamVo dealHdVo) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        DealVo dealHd = dealHdVo.getDealVo();
        List<AttachVo> attachVos = dealHdVo.getAttachVos();
        String type = dealHdVo.getType();
        //准备
        BizDealZlhbDto bizDealZlhbDto = new BizDealZlhbDto();
//        List<BizAttachDto> attachDtos = new ArrayList<>();
//        List<BizDealAttachDto> dealAttachDtos = new ArrayList<>();

        //查询用户所在部门
        SysUserDto sysUserDto = userService.selectByKey(userId);
        String dealId = StringUtils.getUuid32();
        bizDealZlhbDto.setDealId(dealId);
        bizDealZlhbDto.setDealNo(bizDealZlhbService.getDealNo(dealHd.getDealType()));
//        bizDealZlhbDto.setProjectNo(dealHd.getProjectNo());
//        companyType
        bizDealZlhbDto.setCompanyType(dealHd.getCompanyType());
//        bizDealZlhbDto.setDealBiz(dealHd.getDealBiz());
//        workZone
        bizDealZlhbDto.setWorkZone(dealHd.getWorkZone());
        bizDealZlhbDto.setMarketType(dealHd.getMarketType());
        bizDealZlhbDto.setContractType(dealHd.getDealType());
        bizDealZlhbDto.setDealName(dealHd.getDealName());
        bizDealZlhbDto.setDealValue(dealHd.getDealValue());
//        bizDealZlhbDto.setCategoryId(dealHd.getCategoryId());
        bizDealZlhbDto.setDealSignTime(dealHd.getDealSignTime());
        bizDealZlhbDto.setDeptId(sysUserDto.getDeptId());
        bizDealZlhbDto.setContractId(dealHd.getContractId());

        bizDealZlhbDto.setDealType(dealHd.getDealType());


//2 附件
//        for (AttachVo attachVo : attachVos) {
//            // String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");
//            String attachId = attachVo.getAttachId();
//            BizAttachDto attachDto = new BizAttachDto();
//            attachDto.setAttachId(attachId);
//            attachDto.setOwnerId(dealId);
//            attachDto.setOwnerType(FileOwnerTypeEnum.DEAL.getKey());
//            attachDto.setFileName(attachVo.getFileName());
//            attachDto.setFileType(attachVo.getFileType());
//            attachDto.setFileUri(attachVo.getFileUri());
//            attachDto.setFileSize(attachVo.getFileSize());
//            attachDto.setCreateTime(DateUtils.getNowDate());
//            attachDto.setUserId(userId);
//            attachDto.setNotes(attachVo.getNotes());
//            attachDtos.add(attachDto);
//
//            // 3合同附件
//            BizDealAttachDto dealAttachDto = new BizDealAttachDto();
//            dealAttachDto.setId(StringUtils.getUuid32());
//            dealAttachDto.setDealId(dealId);
//            dealAttachDto.setAttachId(attachId);
//            //TODO  新增合同上传的附件增加附件类型，类型为deal
//            dealAttachDto.setDealFileType("deal");
//            dealAttachDtos.add(dealAttachDto);
//        }
        if (!bizDealZlhbService.saveChain(bizDealZlhbDto, type)) {
            return AppMessage.error("新增合同出错");
        }

//        if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
//            //生成合同履行统计记录
//            bizDealStatisticsService.addDealStatistics(bizDealExDto);
//        }
        return AppMessage.success(dealId, "新增合同成功");
    }

    @ApiOperation("删除指令划拨合同")
    @Log(logContent = "删除指令划拨合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "zlhb/{id}", method = RequestMethod.DELETE)
    public AppMessage zlhbdelete( @PathVariable String id) {
        //不能删除已经存在结算的合同上
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", id);
        List<BizSettlementDto> settlementDtos = settlementService.selectList(param);
        if (null != settlementDtos && settlementDtos.size() > 0) {
            return AppMessage.errorObjId(id, "删除合同失败,该合同已存在结算");
        }
        String dealNo = bizDealZlhbService.selectByKey(id).getDealNo();

        boolean delete = bizDealZlhbService.deleteDealAttach(id);
        if (delete) {
            return AppMessage.success(dealNo, "删除合同成功");
        }
        return AppMessage.errorObjId(dealNo, "删除合同失败");
    }

    @ApiOperation("我的合同查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "dealInCome",value = "在合同数据库页面默认传=收入，其他页面不用传"),
    @ApiImplicitParam(name = "dealType",value = "合同类型")})
    @RequestMapping(method = RequestMethod.GET)
    public AppMessage queryPage( @RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
                                 @RequestParam(value = "dealNo", defaultValue = "", required = false) String dealNo,
                                 @RequestParam(value = "dealId", defaultValue = "", required = false) String dealId,
                                 @RequestParam(value = "dealName", defaultValue = "", required = false) String dealName,
                                 @RequestParam(value = "dealValueMax", defaultValue = "", required = false) String dealValueMax,
                                 @RequestParam(value = "dealValueMin", defaultValue = "", required = false) String dealValueMin,
                                 @RequestParam(value = "contractId", defaultValue = "", required = false) String contractId,
                                 @RequestParam(value = "dealSignTimeStart", defaultValue = "", required = false) String dealSignTimeStart,
                                 @RequestParam(value = "dealSignTimeEnd", defaultValue = "", required = false) String dealSignTimeEnd,
                                 @RequestParam(value = "dealType", defaultValue = "", required = false) String dealType,
                                 @RequestParam(value = "statusList", defaultValue = "", required = false) String statusList,
                                 @RequestParam(value = "dealStart", defaultValue = "", required = false) String dealStart,
                                 @RequestParam(value = "dealStartFlag", defaultValue = "", required = false) String dealStartFlag,
                                 @RequestParam(value = "dealEnd", defaultValue = "", required = false) String dealEnd,
                                 @RequestParam(value = "dealEndFlag", defaultValue = "", required = false) String dealEndFlag,
                                 @RequestParam(value = "dealReportNo", defaultValue = "", required = false) String dealReportNo,
                                 @RequestParam(value = "dealInCome", defaultValue = "", required = false) String dealInCome,
                                 @RequestParam(value = "typeList", defaultValue = "", required = false) String typeList,
                                 @RequestParam(value = "dealIncome", defaultValue = "", required = false) String dealIncome,
                                 @RequestParam(value = "settlementAmount", defaultValue = "", required = false) String settlementAmount,
                                 @RequestParam(value = "symbolsymbol", defaultValue = "", required = false) String symbolsymbol

    ) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealNo", dealNo);
        params.put("dealId", dealId);
        params.put("dealName", dealName);
        params.put("dealValueMax", dealValueMax);
        params.put("dealValueMin", dealValueMin);
        params.put("contractId", contractId);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("dealType", dealType);
        params.put("dealInCome", dealInCome);
        params.put("settlementAmount", settlementAmount);
        params.put("symbolsymbol", symbolsymbol);

        params.put("userIdSpecial", userId);
        params.put("deptIdSpecial", userDto.getDeptId());

        params.put("dealReportNo", dealReportNo);
        if (StringUtils.isNotEmpty(typeList)) {
            params.put("typeList", typeList.split(","));
        }
        params.put("dealIncome", dealIncome);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
//            params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        params.put("userId2", userId);
        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            params.put("statusList", statusList.split(","));
        }
        //履行开始时间
        params.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        params.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        params.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        params.put("dealEndFlag", dealEndFlag);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<BizDealDto> sysDealDtos = dealService.selectList(params);
//        bizDealHdService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysDealDtos);
        return AppMessage.success(dataTable, "查询合同成功");
    }

    @ApiOperation("加入合同合同我的合同查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "dealInCome",value = "在合同数据库页面默认传=收入，其他页面不用传"),
            @ApiImplicitParam(name = "dealType",value = "合同类型")})
    @RequestMapping(value = "/maintainjr",method = RequestMethod.GET)
    public AppMessage queryPagejr( @RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNo,
                                         @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
                                         @RequestParam(value = "dealNo", defaultValue = "", required = false) String dealNo,
                                         @RequestParam(value = "dealId", defaultValue = "", required = false) String dealId,
                                         @RequestParam(value = "dealName", defaultValue = "", required = false) String dealName,
                                         @RequestParam(value = "dealValueMax", defaultValue = "", required = false) String dealValueMax,
                                         @RequestParam(value = "dealValueMin", defaultValue = "", required = false) String dealValueMin,
                                         @RequestParam(value = "contractId", defaultValue = "", required = false) String contractId,
                                         @RequestParam(value = "dealSignTimeStart", defaultValue = "", required = false) String dealSignTimeStart,
                                         @RequestParam(value = "dealSignTimeEnd", defaultValue = "", required = false) String dealSignTimeEnd,
                                         @RequestParam(value = "dealType", defaultValue = "", required = false) String dealType,
                                         @RequestParam(value = "statusList", defaultValue = "", required = false) String statusList,
                                         @RequestParam(value = "dealStart", defaultValue = "", required = false) String dealStart,
                                         @RequestParam(value = "dealStartFlag", defaultValue = "", required = false) String dealStartFlag,
                                         @RequestParam(value = "dealEnd", defaultValue = "", required = false) String dealEnd,
                                         @RequestParam(value = "dealEndFlag", defaultValue = "", required = false) String dealEndFlag,
                                         @RequestParam(value = "dealReportNo", defaultValue = "", required = false) String dealReportNo,
                                         @RequestParam(value = "dealInCome", defaultValue = "", required = false) String dealInCome,
                                         @RequestParam(value = "typeList", defaultValue = "", required = false) String typeList,
                                         @RequestParam(value = "dealIncome", defaultValue = "", required = false) String dealIncome,
                                         @RequestParam(value = "settlementAmount", defaultValue = "", required = false) String settlementAmount,
                                         @RequestParam(value = "symbolsymbol", defaultValue = "", required = false) String symbolsymbol

    ) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealNo", dealNo);
        params.put("dealId", dealId);
        params.put("dealName", dealName);
        params.put("dealValueMax", dealValueMax);
        params.put("dealValueMin", dealValueMin);
        params.put("contractId", contractId);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("dealType", dealType);
        params.put("dealInCome", dealInCome);
        params.put("settlementAmount", settlementAmount);
        params.put("symbolsymbol", symbolsymbol);

        params.put("userIdSpecial", userId);
        params.put("deptIdSpecial", userDto.getDeptId());

        params.put("dealReportNo", dealReportNo);
        if (StringUtils.isNotEmpty(typeList)) {
            params.put("typeList", typeList.split(","));
        }
        params.put("dealIncome", dealIncome);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
//            params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        params.put("userId2", userId);
        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            params.put("statusList", statusList.split(","));
        }
        //履行开始时间
        params.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        params.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        params.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        params.put("dealEndFlag", dealEndFlag);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
//        //2、执行查询

        List<BizDealDto> sysDealDtos = dealService.selectList(params);
//        List<BizDealHdDto> bizDealHdDtos = bizDealHdService.selectList(params);
//        List<BizDealZlhbDto> bizDealZlhbDtos = bizDealZlhbService.selectList(params);
//
//        List<Object> combinedList = new ArrayList<>();
//        combinedList.addAll(sysDealDtos);
//        combinedList.addAll(bizDealHdDtos);
//        combinedList.addAll(bizDealZlhbDtos);
////        bizDealHdService.selectList(params);
//        //3、获取分页查询后的数据
//        TableDataInfo dataTable = getDataTable(sysDealDtos,bizDealHdDtos,bizDealZlhbDtos,1,50);
        //2、执行查询
//        List<BizDealInDto> sysDealDtos = bizDealinService.selectList(params);
//        bizDealHdService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysDealDtos);
        return AppMessage.success(dataTable, "查询合同成功");
    }


    @ApiOperation("合同数据我的合同查询")
    @ApiImplicitParams({@ApiImplicitParam(name = "dealInCome",value = "在合同数据库页面默认传=收入，其他页面不用传"),
            @ApiImplicitParam(name = "dealType",value = "合同类型")})
    @RequestMapping(value = "/maintain",method = RequestMethod.GET)
    public AppMessage queryPagemaintain( @RequestParam(value = "pageNum", defaultValue = "1", required = false) int pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize,
                                 @RequestParam(value = "dealNo", defaultValue = "", required = false) String dealNo,
                                 @RequestParam(value = "dealId", defaultValue = "", required = false) String dealId,
                                 @RequestParam(value = "dealName", defaultValue = "", required = false) String dealName,
                                 @RequestParam(value = "dealValueMax", defaultValue = "", required = false) String dealValueMax,
                                 @RequestParam(value = "dealValueMin", defaultValue = "", required = false) String dealValueMin,
                                 @RequestParam(value = "contractId", defaultValue = "", required = false) String contractId,
                                 @RequestParam(value = "dealSignTimeStart", defaultValue = "", required = false) String dealSignTimeStart,
                                 @RequestParam(value = "dealSignTimeEnd", defaultValue = "", required = false) String dealSignTimeEnd,
                                 @RequestParam(value = "dealType", defaultValue = "", required = false) String dealType,
                                 @RequestParam(value = "statusList", defaultValue = "", required = false) String statusList,
                                 @RequestParam(value = "dealStart", defaultValue = "", required = false) String dealStart,
                                 @RequestParam(value = "dealStartFlag", defaultValue = "", required = false) String dealStartFlag,
                                 @RequestParam(value = "dealEnd", defaultValue = "", required = false) String dealEnd,
                                 @RequestParam(value = "dealEndFlag", defaultValue = "", required = false) String dealEndFlag,
                                 @RequestParam(value = "dealReportNo", defaultValue = "", required = false) String dealReportNo,
                                 @RequestParam(value = "dealInCome", defaultValue = "", required = false) String dealInCome,
                                 @RequestParam(value = "typeList", defaultValue = "", required = false) String typeList,
                                 @RequestParam(value = "dealIncome", defaultValue = "", required = false) String dealIncome,
                                 @RequestParam(value = "settlementAmount", defaultValue = "", required = false) String settlementAmount,
                                 @RequestParam(value = "symbolsymbol", defaultValue = "", required = false) String symbolsymbol,
                                 @RequestParam(value = "dealBiz", defaultValue = "", required = false) String dealBiz,
                                 @RequestParam(value = "marketType", defaultValue = "", required = false) String marketType

    ) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealBiz", dealBiz);
        if (StringUtils.isNotEmpty(typeList)) {
            params.put("typeList", typeList.split(","));
        }
        params.put("marketType", marketType);
        params.put("dealNo", dealNo);
        params.put("dealId", dealId);
        params.put("dealName", dealName);
        params.put("dealValueMax", dealValueMax);
        params.put("dealValueMin", dealValueMin);
        params.put("contractId", contractId);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("dealType", dealType);
        params.put("dealInCome", dealInCome);
        params.put("settlementAmount", settlementAmount);
        params.put("symbolsymbol", symbolsymbol);

        params.put("userIdSpecial", userId);
        params.put("deptIdSpecial", userDto.getDeptId());

        params.put("dealReportNo", dealReportNo);
        if (StringUtils.isNotEmpty(typeList)) {
            params.put("typeList", typeList.split(","));
        }
        params.put("dealIncome", dealIncome);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
//            params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        params.put("userId2", userId);
        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            params.put("statusList", statusList.split(","));
        }
        //履行开始时间
        params.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        params.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        params.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        params.put("dealEndFlag", dealEndFlag);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);

        //2、执行查询
//        List<BizDealInDto> sysDealDtos = bizDealinService.selectList(params);
        List<DealDataDto> dealDataDtos = fusionReportService.selectListdealdata(params);

        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(dealDataDtos);
        return AppMessage.success(dataTable, "查询合同成功");
    }

    /**
     * 新增合同
     *
     * @param
     * @param paramVo
     * @return
     */
    @ApiOperation("新增合同")
    @Log(logContent = "新增合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage add( @RequestBody DealParamVo paramVo) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        DealVo dealVo = paramVo.getDealVo();
        List<AttachVo> attachVos = paramVo.getAttachVos();
        String type = paramVo.getType();
        //准备
        BizDealDto dealDto = new BizDealDto();
        List<BizAttachDto> attachDtos = new ArrayList<>();
        List<BizDealAttachDto> dealAttachDtos = new ArrayList<>();

        //查询用户所在部门
        SysUserDto sysUserDto = userService.selectByKey(userId);

        //1保存合同
        String dealId = StringUtils.getUuid32();
        dealDto.setDealId(dealId);

        dealDto.setDealNo(dealService.getDealNo(dealVo.getDealType()));
        dealDto.setDealName(dealVo.getDealName());
        dealDto.setDealValue(dealVo.getDealValue());
        dealDto.setCategoryId(dealVo.getCategoryId());
        dealDto.setDealSignTime(dealVo.getDealSignTime());

        dealDto.setDeptId(sysUserDto.getDeptId());

        dealDto.setContractId(dealVo.getContractId());
        dealDto.setDealIncome(dealVo.getDealIncome());
        dealDto.setDealFunds(dealVo.getDealFunds());
        dealDto.setDealReportNo(dealVo.getDealReportNo());
        dealDto.setDealContract(dealVo.getDealContract());
        dealDto.setDealDispute(dealVo.getDealDispute());
        dealDto.setUserId(userId);
        dealDto.setDealStart(dealVo.getDealStart());
        dealDto.setDealEnd(dealVo.getDealEnd());
        dealDto.setDealSelection(dealVo.getDealSelection());
        dealDto.setDealSettlement(dealVo.getDealSettlement());
        dealDto.setSettleDate(dealVo.getSettleDate());
        dealDto.setDealNotes(dealVo.getDealNotes());
        dealDto.setCreateAt(DateUtils.getNowDate());
        dealDto.setDealCurrency(dealVo.getDealCurrency());
        dealDto.setSubtypeId(dealVo.getSubtypeId());
        dealDto.setDealStatus(DealWebTypeEnum.getEnumByKey(type));
        dealDto.setDealType(dealVo.getDealType());
        dealDto.setPaymentType(dealVo.getPaymentType());
        dealDto.setPaymentTeq(dealVo.getPaymentTeq());
        dealDto.setDealValueAfter(dealVo.getDealValueAfter());
        dealDto.setDealValueBefore(dealVo.getDealValueBefore());
        dealDto.setHaveTax(dealVo.getHaveTax());
        dealDto.setTaxRate(dealVo.getTaxRate());
        //    dealDto.setDealValueBefore(dealVo.getDealValue());
        //2 附件
        for (AttachVo attachVo : attachVos) {
            // String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");
            String attachId = attachVo.getAttachId();
            BizAttachDto attachDto = new BizAttachDto();
            attachDto.setAttachId(attachId);
            attachDto.setOwnerId(dealId);
            attachDto.setOwnerType(FileOwnerTypeEnum.DEAL.getKey());
            attachDto.setFileName(attachVo.getFileName());
            attachDto.setFileType(attachVo.getFileType());
            attachDto.setFileUri(attachVo.getFileUri());
            attachDto.setFileSize(attachVo.getFileSize());
            attachDto.setCreateTime(DateUtils.getNowDate());
            attachDto.setUserId(userId);
            attachDto.setNotes(attachVo.getNotes());
            attachDtos.add(attachDto);

            // 3合同附件
            BizDealAttachDto dealAttachDto = new BizDealAttachDto();
            dealAttachDto.setId(StringUtils.getUuid32());
            dealAttachDto.setDealId(dealId);
            dealAttachDto.setAttachId(attachId);
            //TODO  新增合同上传的附件增加附件类型，类型为deal
            dealAttachDto.setDealFileType("deal");
            dealAttachDtos.add(dealAttachDto);
        }
        if (!dealService.saveChain(dealDto, attachDtos, dealAttachDtos, type)) {
            return AppMessage.error("新增合同出错");
        }

        if(DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
            //生成合同履行统计记录
            bizDealStatisticsService.addDealStatistics(dealDto);
        }
        return AppMessage.success(dealId, "新增合同成功");
    }

    /**
     * 查询当前用户可审核合同列表
     *
     * @return
     */
    @RequestMapping(value = "audit", method = RequestMethod.GET)
    public AppMessage query( @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                            @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                            @RequestParam(value = "dealId", defaultValue = "") String dealId,
                            @RequestParam(value = "dealName", defaultValue = "") String dealName,
                            @RequestParam(value = "dealValueMax", defaultValue = "") String dealValueMax,
                            @RequestParam(value = "dealValueMin", defaultValue = "") String dealValueMin,
                            @RequestParam(value = "contractId", defaultValue = "") String contractId,
                            @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                            @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd,
                            @RequestParam(value = "dealType", defaultValue = "") String dealType,
                            @RequestParam(value = "statusList", defaultValue = "") String statusList,
                            @RequestParam(value = "dealStart", defaultValue = "") String dealStart,
                            @RequestParam(value = "dealStartFlag", defaultValue = "") String dealStartFlag,
                            @RequestParam(value = "dealEnd", defaultValue = "") String dealEnd,
                            @RequestParam(value = "dealEndFlag", defaultValue = "") String dealEndFlag,
                            @RequestParam(value = "status", defaultValue = "") String status) {
        String userId = ServletUtils.getSessionUserId();
        Map<String, Object> params = new HashMap<>();
        params.put("entrustUserId", userId);
        List<SysUserDto> userDtos = userService.selectPage(params);

        Map<String, Object> param = new HashMap<>();
        if (null != userDtos && userDtos.size() > 0) {
            param.put("entrustUserId", "true");
        }

        param.put("userId", userId);
        param.put("dealId", dealId);
        param.put("dealNo", dealNo);
        param.put("dealName", dealName);
        param.put("dealValueMax", dealValueMax);
        param.put("dealValueMin", dealValueMin);
        param.put("contractId", contractId);
        param.put("dealSignTimeStart", dealSignTimeStart);
        param.put("dealSignTimeEnd", dealSignTimeEnd);
        param.put("dealType", dealType);
        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            param.put("statusList", statusList.split(","));
        }
        //履行开始时间
        param.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        param.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        param.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        param.put("dealEndFlag", dealEndFlag);

        //审核状态（待审核auditing、已审核audited）
        // param.put("status", status);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<BizDealDto> dealAttachDtos = new ArrayList<>();

        if ("audited".equals(status)) {
            //查询用户已审核合同
            dealAttachDtos = dealService.selectAuditedList(param);
        } else {
            dealAttachDtos = dealService.selectUserList(param);
        }

        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(dealAttachDtos);
        return AppMessage.success(dataTable, "查询当前用户可审核合同列表！");
    }

    /**
     * 集团合同上传
     *
     * @param file
     * @return
     */
    @Log(logContent = "集团合同上传", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/importData", method = RequestMethod.POST, produces = "application/json")
    public AppMessage upload( @RequestParam("file") MultipartFile file) throws Exception {

        ExcelUtil<DealImportVo> util = new ExcelUtil<DealImportVo>(DealImportVo.class);
        String filename = file.getOriginalFilename();
        List<DealImportVo> dealVo = util.importExcel3(filename,file.getInputStream());
        AppMessage appMessage = importLogService.saveDealDtos(dealVo);
        return appMessage;
    }


    /**
     * 集团合同下载
     *
     * @return
     */
    @Log(logContent = "合同下载", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    public AppMessage export( HttpServletResponse response,
                             @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                             @RequestParam(value = "dealName", defaultValue = "") String dealName,
                             @RequestParam(value = "userId", defaultValue = "") String userId,
                             @RequestParam(value = "dealValueMax", defaultValue = "") String dealValueMax,
                             @RequestParam(value = "dealValueMin", defaultValue = "") String dealValueMin,
                             @RequestParam(value = "contractId", defaultValue = "") String contractId,
                             @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                             @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd,
                             @RequestParam(value = "dealType", defaultValue = "") String dealType,
                             @RequestParam(value = "dealId", defaultValue = "") String dealId,
                             @RequestParam(value = "statusList", defaultValue = "") String statusList,
                             @RequestParam(value = "dealStart", defaultValue = "") String dealStart,
                             @RequestParam(value = "dealStartFlag", defaultValue = "") String dealStartFlag,
                             @RequestParam(value = "dealEnd", defaultValue = "") String dealEnd,
                             @RequestParam(value = "dealEndFlag", defaultValue = "") String dealEndFlag,
                             @RequestParam(value = "deptId", defaultValue = "") String deptId,
                             @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                             @RequestParam(value = "typeList", defaultValue = "") String typeList) throws Exception {
        String sessionUserId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(sessionUserId);
        String dataScope = userDto.getDataScope();

        Map<String, Object> param = new HashMap<>();
        //    param.put("userId",userId);
        param.put("userId2", userId);
        param.put("dealNo", dealNo);
        param.put("dealName", dealName);
        param.put("dealValueMax", dealValueMax);
        param.put("dealValueMin", dealValueMin);
        param.put("contractId", contractId);
        param.put("dealSignTimeStart", dealSignTimeStart);
        param.put("dealSignTimeEnd", dealSignTimeEnd);
        param.put("dealType", dealType);
        param.put("dealId", dealId);
        param.put("deptId", deptId);
        param.put("dealIncome", dealIncome);

        //仅针对我的合同导出
        if (StringUtils.isNotEmpty(userId)) {
            param.put("userIdSpecial", userId);
            param.put("deptIdSpecial", userDto.getDeptId());
        }

        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            param.put("statusList", statusList.split(","));
        }

        //履行开始时间
        param.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        param.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        param.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        param.put("dealEndFlag", dealEndFlag);

        if (StringUtils.isNotEmpty(typeList)) {
            //合同状态
            param.put("typeList", typeList.split(","));
        }

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            param.put("dataScopeDept", userDto.getDeptId());
        } else {
            param.put("dataScopeSelf", sessionUserId);
        }

        List<BizDealDto> sysDealDtos = dealService.selectListForExport(param);
        List<DealVo> dealVoS = new ArrayList<>();

        for (BizDealDto dealDto : sysDealDtos) {
            DealVo dealVo = new DealVo();
            //dealVo.setDealSignTime(DateUtils.);
            BeanUtils.copyBeanProp(dealVo, dealDto);
            dealVo.setDealStatus(DealStatusEnum.getEnumByKey(dealDto.getDealStatus()));
            dealVo.setSettleStatus(SettlementStatusEnum.getEnumByKey(dealDto.getSettleStatus()));
            dealVoS.add(dealVo);
        }
        ExcelUtil<DealVo> util = new ExcelUtil<DealVo>(DealVo.class);
        return util.exportExcelBrowser(response, dealVoS, "合同信息");
    }

    @Log(logContent = "合同数据下载", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/dealexport", method = RequestMethod.GET)
    public AppMessage dealexport( HttpServletResponse response,
                              @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                              @RequestParam(value = "dealName", defaultValue = "") String dealName,
                              @RequestParam(value = "userId", defaultValue = "") String userId,
                              @RequestParam(value = "dealValueMax", defaultValue = "") String dealValueMax,
                              @RequestParam(value = "dealValueMin", defaultValue = "") String dealValueMin,
                              @RequestParam(value = "contractId", defaultValue = "") String contractId,
                              @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                              @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd,
                              @RequestParam(value = "dealType", defaultValue = "") String dealType,
                              @RequestParam(value = "dealId", defaultValue = "") String dealId,
                              @RequestParam(value = "statusList", defaultValue = "") String statusList,
                              @RequestParam(value = "dealStart", defaultValue = "") String dealStart,
                              @RequestParam(value = "dealStartFlag", defaultValue = "") String dealStartFlag,
                              @RequestParam(value = "dealEnd", defaultValue = "") String dealEnd,
                              @RequestParam(value = "dealEndFlag", defaultValue = "") String dealEndFlag,
                              @RequestParam(value = "deptId", defaultValue = "") String deptId,
                              @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                              @RequestParam(value = "typeList", defaultValue = "") String typeList) throws Exception {
        String sessionUserId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(sessionUserId);
        String dataScope = userDto.getDataScope();

        Map<String, Object> param = new HashMap<>();
        //    param.put("userId",userId);
        param.put("userId2", userId);
        param.put("dealNo", dealNo);
        param.put("dealName", dealName);
        param.put("dealValueMax", dealValueMax);
        param.put("dealValueMin", dealValueMin);
        param.put("contractId", contractId);
        param.put("dealSignTimeStart", dealSignTimeStart);
        param.put("dealSignTimeEnd", dealSignTimeEnd);
        param.put("dealType", dealType);
        param.put("dealId", dealId);
        param.put("deptId", deptId);
        param.put("dealIncome", dealIncome);

        //仅针对我的合同导出
        if (StringUtils.isNotEmpty(userId)) {
            param.put("userIdSpecial", userId);
            param.put("deptIdSpecial", userDto.getDeptId());
        }

        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            param.put("statusList", statusList.split(","));
        }

        //履行开始时间
        param.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        param.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        param.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        param.put("dealEndFlag", dealEndFlag);

        if (StringUtils.isNotEmpty(typeList)) {
            //合同状态
            param.put("typeList", typeList.split(","));
        }

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            param.put("dataScopeDept", userDto.getDeptId());
        } else {
            param.put("dataScopeSelf", sessionUserId);
        }
//        List<BizDealDto> sysDealDtos = dealService.selectList(params);
//        List<BizDealHdDto> bizDealHdDtos = bizDealHdService.selectList(params);
//        List<BizDealZlhbDto> bizDealZlhbDtos = bizDealZlhbService.selectList(params);

        List<BizDealDto> sysDealDtos = dealService.selectListForExport(param);
        List<DealVo> dealVoS = new ArrayList<>();

        for (BizDealDto dealDto : sysDealDtos) {
            DealVo dealVo = new DealVo();
            //dealVo.setDealSignTime(DateUtils.);
            BeanUtils.copyBeanProp(dealVo, dealDto);
            dealVo.setDealStatus(DealStatusEnum.getEnumByKey(dealDto.getDealStatus()));
            dealVo.setSettleStatus(SettlementStatusEnum.getEnumByKey(dealDto.getSettleStatus()));
            dealVoS.add(dealVo);
        }
        ExcelUtil<DealVo> util = new ExcelUtil<DealVo>(DealVo.class);
        return util.exportExcelBrowser(response, dealVoS, "合同信息");
    }


    @Log(logContent = "保存提交合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/{id}", method = RequestMethod.PUT)
    public AppMessage update( @PathVariable String id, @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        BizDealDto dealDto = dealService.selectByKey(id);
        if (null == dealDto) {
            throw new AppException("当前合同不存在");
        }
        dealDto.setDealStatus(DealStatusEnum.BUILDAUDITING.getKey());
        dealService.updateNotNull(dealDto);
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            String stepNo = bizCheckStepService.selectMaxStepNo(dealDto.getDealId());
            activitiService.initBaseActiviti(dealDto.getUserId(), dealDto.getDealId(), "deal", dealDto.getDeptId(), stepNo, null);
        } else {
            bizCheckStepService.updateBackObj(id);
        }
        //新增履行合同统计
        bizDealStatisticsService.addDealStatistics(dealDto);
        return AppMessage.success(id, "保存提交合同成功！");
    }

    @ApiOperation("变更合同")
    @ApiImplicitParam(name = "status",value = "如果为变更则传change")
    @Log(logContent = "变更合同", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/change/{id}", method = RequestMethod.PUT)
    public AppMessage change(@PathVariable String id,@RequestBody DealParamVo paramVo) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto sysUserDto = userService.selectByKey(userId);
        BizDealDto dealDto = dealService.selectByKey(id);
        String dealStatus = dealDto.getDealStatus();
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", id);
        boolean auditing = settlementService.isAuditing(param);
        if (auditing) {
            return AppMessage.errorObjId(id, "变更失败，还有正在审核的结算");
        }
        if(null==dealDto){
            throw new AppException("当前合同不存在");
        }
        if (DealStatusEnum.SIGNING.getKey().equals(dealStatus)) {
            return AppMessage.errorObjId(id, "变更失败，合同签订中！");
        }
        Double dealValueAfter = paramVo.getDealVo().getDealValueAfter();
        if (!dealService.isAvailableAmount(dealValueAfter, id)) {
            return AppMessage.errorObjId(id, "变更失败，已履行金额大于更改后的标的金额！");
        }
        StringBuffer dealBefore=new StringBuffer();
        StringBuffer dealAfter=new StringBuffer();
        dealBefore.append(JSON.marshal(dealDto));
        try{
            //履行中到变更审核中
            BizDealDto dealDto1 = dealService.getDealInfo(paramVo, null,id);
//                        dealDto1.setDealId(id);
//                        dealDto1.setDealValueBefore(dealDto.getDealValue());
//                        dealDto1.setDealValueAfter(dealValue);
//                    dealDto.setDealValue(dealValue);
            dealDto1.setDealStatus(DealStatusEnum.CHANGEAUDITING.getKey());
           // dealDto1.set
            List<BizAttachDto> attachDtos = dealService.getAttachInfo(paramVo);
            List<BizDealAttachDto> dealAttachDtos = dealService.getDealAttachInfo(paramVo, id);
            //  dealService.updateNotNull(dealDto1);
            boolean updateChain = dealService.updateChain(dealDto1, attachDtos, dealAttachDtos, null, userId,paramVo.getStatus());
            if (DealStatusEnum.PROGRESSAUDITING.getKey().equals(dealStatus)) {
                String stepNo = bizCheckStepService.selectMaxStepNo(dealDto.getDealId());
                activitiService.initBaseActiviti(userId, id, "deal", dealDto.getDeptId(), stepNo, "change");
            }else{
                //变更回退到变更审核中
                bizCheckStepService.updateBackObj(id);
            }
            if (updateChain) {
                return AppMessage.success(id, "变更合同成功");
            }
        }finally {
            BizDealDto bizDealDto = dealService.selectByKey(id);
            dealAfter.append(JSON.marshal(bizDealDto));
            StringBuffer content=new StringBuffer();
            content.append(sysUserDto.getUserName());
            content.append("执行了更改合同操作，更改前的合同信息为[");
            content.append(dealBefore) ;
            content.append("]");
            content.append("更改后的合同信息为[");
            content.append(dealAfter) ;
            content.append("]");
            SysLogDto log=setSysLog(userId,
                    LogModule.COMPREHENSIVE.toString(),
                    id,LogType.OPERATION.toString(),
                    content.toString());
            logService.save(log);
        }
        return AppMessage.success(id, "变更合同成功！");
    }

    @Log(logContent = "申请归档", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/placing/{id}", method = RequestMethod.PUT)
    public AppMessage placing( @PathVariable String id) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", id);
        boolean auditing = settlementService.isAuditing(param);
        if (!auditing) {
            BizDealDto dealDto = dealService.selectByKey(id);
            dealDto.setDealStatus(DealStatusEnum.PLACING.getKey());
            dealService.updateNotNull(dealDto);
        } else {
            return AppMessage.errorObjId(id, "申请归档失败，还有正在审核的结算");
        }
        return AppMessage.success(id, "申请归档成功！");
    }

    @Log(logContent = "归档完毕", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/placed/{id}", method = RequestMethod.PUT)
    public AppMessage placed( @PathVariable String id) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", id);
        boolean auditing = settlementService.isAuditing(param);
        if (!auditing) {
            BizDealDto dealDto = dealService.selectByKey(id);
            if (null == dealDto) {
                throw new AppException("当前合同不存在");
            }
            dealDto.setDealStatus(DealStatusEnum.PLACED.getKey());
            dealDto.setPlacedTime(DateUtils.getNowDate());
            dealService.updateNotNull(dealDto);
        } else {
            return AppMessage.errorObjId(id, "归档失败，还有正在审核的结算");
        }
        return AppMessage.success(id, "归档完毕！");
    }


    @Log(logContent = "批量归档完毕", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/placed/multiple", method = RequestMethod.PUT)
    public AppMessage placedMultiple(@RequestBody Map<String,List<String>> map) throws Exception {
        List<String> ids = map.get("dealIds");
        for (String id:ids) {
            Map<String, Object> param = new HashMap<>();
            param.put("dealId", id);
            boolean auditing = settlementService.isAuditing(param);
            if (!auditing) {
                BizDealDto dealDto = dealService.selectByKey(id);
                if (null == dealDto) {
                    throw new AppException("当前合同不存在");
                }
                dealDto.setDealStatus(DealStatusEnum.PLACED.getKey());
                dealDto.setPlacedTime(DateUtils.getNowDate());
                dealService.updateNotNull(dealDto);
            } else {
                return AppMessage.errorObjId(id, "批量归档失败，还有正在审核的结算");
            }

        }
        return AppMessage.success(ids.get(0), "批量归档完毕！");

    }


    @Log(logContent = "驳回归档", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/placedBack/{id}", method = RequestMethod.PUT)
    public AppMessage placedBack( @PathVariable String id) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", id);
        boolean auditing = settlementService.isAuditing(param);
        if (!auditing) {
            BizDealDto dealDto = dealService.selectByKey(id);
            if (null == dealDto) {
                throw new AppException("当前合同不存在");
            }
            dealDto.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
            dealService.updateNotNull(dealDto);
        } else {
            return AppMessage.errorObjId(id, "驳回归档，还有正在审核的结算");
        }
        return AppMessage.success(id, "驳回归档！");
    }


    /**
     * 导出 合同签约审查审批表.docx
     *
     * @return
     */
    @Log(logContent = "导出合同签约审查审批表", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "word", method = RequestMethod.GET)
    public void word( @RequestParam(value = "dealId") String dealId,
                     HttpServletResponse response,
                     @RequestParam(value = "userId") String userId) throws Exception {
        // String userId= getSessionUserId(request);
        Map<String, Object> wordDataMap = new HashMap<String, Object>();// 存储报表全部数据
        Map<String, Object> parametersMap = new HashMap<String, Object>();// 存储报表中不循环的数据
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            //添加 合同签约审查审批表基础信息
            // BizDealDto dealDto = dealService.selectByKey(dealId);

            Map<String, Object> params = new HashMap<>();
            params.put("dealId", dealId);
            List<BizDealDto> sysDealDtos = dealService.selectList(params);
            BizDealDto dealDto = sysDealDtos.get(0);
            String dealNo = dealDto.getDealNo();
            String dealName = dealDto.getDealName();
            String dealType = dealDto.getDealType();
            Date dealStart = dealDto.getDealStart();
            Date dealEnd = dealDto.getDealEnd();
            String dealIncome = dealDto.getDealIncome();
            String dealFunds = dealDto.getDealFunds();
            String dealSelection = dealDto.getDealSelection();
            String dealNotes = dealDto.getDealNotes();
            Double dealValue = dealDto.getDealValue();
            String dealCurrency = dealDto.getDealCurrency();
            String deptName = dealDto.getDeptName();
            String userName = dealDto.getUserName();
            String contName = dealDto.getContName();
            String dutyMan = dealDto.getDutyMan();
            String categoryName = dealDto.getCategoryName();
            //签约单位
            String dealContract = dealDto.getDealContract();
            //签约人
            String dealMan =dealService.getDealContractCharge(dealContract,dealId);
            parametersMap.put("dealNo", dealNo);
            parametersMap.put("dealName", dealName);
            parametersMap.put("dealType", DealTypeEnum.getEnumByKey(dealType));
            parametersMap.put("dealStart", null != dealStart ? DateUtils.dateTime(dealStart) : null);
            parametersMap.put("dealEnd", null != dealEnd ? DateUtils.dateTime(dealEnd) : null);
            //资金流向 INCOME   收入 OUTCOME  支出
            parametersMap.put("dealIncome", dealIncome);
            if ("OUTCOME".equalsIgnoreCase(dealIncome)) {
                //付款方
                parametersMap.put("dealContract", dealContract);
                //收款方
                parametersMap.put("contName", contName);
            } else {
                //付款方
                parametersMap.put("dealContract", contName);
                //收款方
                parametersMap.put("contName", dealContract);
            }


            parametersMap.put("dealFunds", dealFunds);
            parametersMap.put("dealSelection", dealSelection);
            parametersMap.put("dealNotes", dealNotes);
            parametersMap.put("dealValue", 0 != dealValue ? df.format(dealValue) : "0.00");
            parametersMap.put("dealCurrency", dealCurrency);
            parametersMap.put("deptName", deptName);
            parametersMap.put("userName", userName);
            parametersMap.put("dutyMan", dutyMan);
            parametersMap.put("categoryName", categoryName);
            parametersMap.put("dealMan", dealMan);


            List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();

            //添加 合同签约审查审批表审核意见
            List<CheckStepPo> checkStepPoList = diyCheckStepService.selectDetails(dealId, null, userId);
            for (CheckStepPo checkStepPo : checkStepPoList) {
                Map<String, Object> map = new HashMap<>();
                map.put("deptName", checkStepPo.getDeptName());
                map.put("userName", checkStepPo.getUserName());
                map.put("checkNode", checkStepPo.getCheckNode());
                map.put("checkTime", null != checkStepPo.getCheckTime() ? DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()) : null);
                table1.add(map);
            }

            wordDataMap.put("table1", table1);
            wordDataMap.put("parametersMap", parametersMap);
            File file = new File(wordTemplateUrl);//改成你本地文件所在目录


            // 读取word模板
            FileInputStream fileInputStream = new FileInputStream(file);
            WordTemplate template = new WordTemplate(fileInputStream);

            // 替换数据
            template.replaceDocument(wordDataMap);


            // 下载到浏览器
            String fileName = "合同签约审查审批表-" + dealDto.getDealNo() + dealDto.getUserName() + ".docx";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            BufferedOutputStream bos = null;

            //生成文件
            //       File outputFile=new File("D:\\word\\输出.docx");//改成你本地文件所在目录
//        FileOutputStream fos  = new FileOutputStream(outputFile);
//        template.getDocument().write(fos);
            //输出到浏览器
            try {
                bos = new BufferedOutputStream(response.getOutputStream());
                template.write(bos);
                bos.flush();
            } finally {

                if (null != bos) {
                    bos.close();
                }

            }
        } catch (Exception e) {
            throw new AppException(e.getMessage());
        }

        // return AppMessage.success(userId,"导出合同签约审查审批表成功！");
    }


    /**
     * 查询正在归档的合同
     *
     * @return
     */
    @RequestMapping(value = "/placed", method = RequestMethod.GET)
    public AppMessage queryPlaced( @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                  @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                  @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                  @RequestParam(value = "dealId", defaultValue = "") String dealId,
                                  @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                  @RequestParam(value = "dealValueMax", defaultValue = "") String dealValueMax,
                                  @RequestParam(value = "dealValueMin", defaultValue = "") String dealValueMin,
                                  @RequestParam(value = "contractId", defaultValue = "") String contractId,
                                  @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                                  @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd,
                                  @RequestParam(value = "dealType", defaultValue = "") String dealType,
                                  @RequestParam(value = "dealReportNo", defaultValue = "") String dealReportNo) {
        Map<String, Object> param = new HashMap<>();
        //  param.put("userId",userId);
        // param.put("dealId",dealId);
        param.put("dealNo", dealNo);
        param.put("dealName", dealName);
        param.put("dealValueMax", dealValueMax);
        param.put("dealValueMin", dealValueMin);
        param.put("contractId", contractId);
        param.put("dealSignTimeStart", dealSignTimeStart);
        param.put("dealSignTimeEnd", dealSignTimeEnd);
        param.put("dealType", dealType);
        param.put("dealStatus", DealStatusEnum.PLACING.getKey());
        param.put("dealReportNo", dealReportNo);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<BizDealDto> dealAttachDtos = dealService.selectList(param);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(dealAttachDtos);
        return AppMessage.success(dataTable, "查询正在归档的合同列表！");
    }

    /**
     * 查询类型下的汇总
     *
     * @param pageNo
     * @param pageSize
     * @param dealNo
     * @param dealName
     * @param dealValueMax
     * @param dealValueMin
     * @param contractId
     * @param dealSignTimeStart
     * @param dealSignTimeEnd
     * @param dealType
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public AppMessage queryAll( @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                               @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                               @RequestParam(value = "dealName", defaultValue = "") String dealName,
                               @RequestParam(value = "dealValueMax", defaultValue = "") String dealValueMax,
                               @RequestParam(value = "dealValueMin", defaultValue = "") String dealValueMin,
                               @RequestParam(value = "contractId", defaultValue = "") String contractId,
                               @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                               @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd,
                               @RequestParam(value = "dealType", defaultValue = "") String dealType,
                               @RequestParam(value = "statusList", defaultValue = "") String statusList,
                               @RequestParam(value = "dealStart", defaultValue = "") String dealStart,
                               @RequestParam(value = "dealStartFlag", defaultValue = "") String dealStartFlag,
                               @RequestParam(value = "dealEnd", defaultValue = "") String dealEnd,
                               @RequestParam(value = "dealEndFlag", defaultValue = "") String dealEndFlag,
                               @RequestParam(value = "dealReportNo", defaultValue = "") String dealReportNo,
                               @RequestParam(value = "deptId", defaultValue = "") String deptId,
                               @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
    ) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dealNo", dealNo);
        params.put("dealName", dealName);
        params.put("dealValueMax", dealValueMax);
        params.put("dealValueMin", dealValueMin);
        params.put("contractId", contractId);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("dealType", dealType);
        params.put("dataScope", dataScope);
        params.put("dealReportNo", dealReportNo);
        params.put("deptId", deptId);
        params.put("dealIncome", dealIncome);
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }

        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            params.put("statusList", statusList.split(","));
        }

        //履行开始时间
        params.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        params.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        params.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        params.put("dealEndFlag", dealEndFlag);

        params.put("userId2", userId);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<BizDealDto> sysDealDtos = dealService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysDealDtos);
        return AppMessage.success(dataTable, "查询汇总合同成功");
    }

    @RequestMapping(value = "dealId", method = RequestMethod.GET)
    public AppMessage query( @RequestParam(value = "dealId", defaultValue = "") String dealId
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", dealId);
        List<BizDealDto> sysDealDtos = dealService.selectList(params);
        return AppMessage.success(sysDealDtos, "查询合同成功");
    }
    @RequestMapping(value = "dealinId", method = RequestMethod.GET)
    public AppMessage queryin( @RequestParam(value = "dealId", defaultValue = "") String dealId
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", dealId);
        List<BizDealInDto> sysDealDtos = bizDealinService.selectList(params);
        return AppMessage.success(sysDealDtos, "查询合同成功");
    }


    @Log(logContent = "合同签订完成", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/signed/{id}", method = RequestMethod.PUT)
    public AppMessage signed( @PathVariable String id) throws Exception {
        String sessionUserId = ServletUtils.getSessionUserId();

        BizDealDto dealDto = new BizDealDto();
        dealDto.setDealId(id);
        dealDto.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
        dealDto.setDealSignTime(DateUtils.getNowDate());
        dealService.updateNotNull(dealDto);

        //通过通知-通知承办人
        String userId = dealService.selectByKey(id).getUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String passMessage = wxMessageService.getPassMessage("deal", id, userDto.getWxopenid(), sessionUserId, "同意签订", DateUtils.getNowDate(), "合同-签订审批");
        checkNoticeService.saveNotice(id, "deal", CheckNoticeTypeEnum.PASS.getKey(), userDto.getWxopenid(), passTemplateId, passMessage);
        return AppMessage.success(id, "合同签订完成！");
    }


    /**
     * 导出 合同签约审查审批表.pdf
     *
     * @return
     */
    @Log(logContent = "导出合同签约审查审批表pdf格式", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "pdf", method = RequestMethod.GET)
    public void pdf( @RequestParam(value = "dealId") String dealId,
                    HttpServletResponse response,
                    @RequestParam(value = "userId") String userId) throws Exception {
        // String userId= getSessionUserId(request);
        Map<String, Object> wordDataMap = new HashMap<String, Object>();// 存储报表全部数据
        Map<String, Object> parametersMap = new HashMap<String, Object>();// 存储报表中不循环的数据
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            //添加 合同签约审查审批表基础信息
            // BizDealDto dealDto = dealService.selectByKey(dealId);

            Map<String, Object> params = new HashMap<>();
            params.put("dealId", dealId);
            List<BizDealDto> sysDealDtos = dealService.selectList(params);
            BizDealDto dealDto = sysDealDtos.get(0);
            String dealNo = dealDto.getDealNo();
            String dealName = dealDto.getDealName();
            String dealType = dealDto.getDealType();
            Date dealStart = dealDto.getDealStart();
            Date dealEnd = dealDto.getDealEnd();
            String dealIncome = dealDto.getDealIncome();
            String dealFunds = dealDto.getDealFunds();
            String dealSelection = dealDto.getDealSelection();
            String dealNotes = dealDto.getDealNotes();
            Double dealValue = dealDto.getDealValue();
            String dealCurrency = dealDto.getDealCurrency();
            String deptName = dealDto.getDeptName();
            String userName = dealDto.getUserName();
            String contName = dealDto.getContName();
            String dutyMan = dealDto.getDutyMan();
            String categoryName = dealDto.getCategoryName();
            //签约单位
            String dealContract = dealDto.getDealContract();
            //签约人
            String dealMan =dealService.getDealContractCharge(dealContract,dealId);
            String haveTax = "否";
            //是否含税
            if (null != dealDto.getHaveTax()) {
                haveTax = dealDto.getHaveTax() == 1 ? "是" : "否";
            }
            //税率
            String taxRate = null != dealDto.getTaxRate() ? dealDto.getTaxRate() + "%" : null;


            parametersMap.put("dealNo", dealNo);
            parametersMap.put("dealName", dealName);
            parametersMap.put("dealType", DealTypeEnum.getEnumByKey(dealType));
            parametersMap.put("dealStart", null != dealStart ? DateUtils.dateTime(dealStart) : "");
            parametersMap.put("dealEnd", null != dealEnd ? DateUtils.dateTime(dealEnd) : "");
            //资金流向 INCOME   收入 OUTCOME  支出
            parametersMap.put("dealIncome", dealIncome);
            parametersMap.put("dealContract", dealContract);
            parametersMap.put("contName", contName);
            parametersMap.put("dealFunds", dealFunds);
            parametersMap.put("dealSelection", dealSelection);
            parametersMap.put("dealNotes", dealNotes);
            parametersMap.put("dealValue", 0 != dealValue ? df.format(dealValue) : "0.00");
            parametersMap.put("dealCurrency", dealCurrency);
            parametersMap.put("deptName", deptName);
            parametersMap.put("userName", userName);
            parametersMap.put("dutyMan", dutyMan);
            parametersMap.put("categoryName", categoryName);
            parametersMap.put("dealMan", dealMan);
            parametersMap.put("haveTax", haveTax);
            parametersMap.put("taxRate", taxRate);

            List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();

            //添加 合同签约审查审批表审核意见
            List<CheckStepPo> checkStepPoList = diyCheckStepService.selectDetails(dealId, null, userId);
            for (CheckStepPo checkStepPo : checkStepPoList) {
                Map<String, Object> map = new HashMap<>();
                map.put("deptName", checkStepPo.getDeptName());
                map.put("userName", checkStepPo.getUserName());
                map.put("checkNode", checkStepPo.getCheckNode());
                map.put("checkTime", null != checkStepPo.getCheckTime() ? DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()) : null);
                table1.add(map);
            }

            wordDataMap.put("table1", table1);
            wordDataMap.put("parametersMap", parametersMap);


            // 下载到浏览器
            String pdfName = "合同签约审查审批表-" + dealDto.getDealNo() + dealDto.getUserName() + ".pdf";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(pdfName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");
            PDFBuildUtils.buildDealPDF(wordDataMap, response, TEMPURL, PDFPicUrl, baseFontUrl);

        } catch (Exception e) {
            e.printStackTrace();
            throw new AppException(e.getMessage());
        }

        // return AppMessage.success(userId,"导出合同签约审查审批表成功！");
    }

    /**
     * 查询类型下的汇总
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/statistics", method = RequestMethod.GET)
    public AppMessage statistics( @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                 @RequestParam(value = "dealYear", defaultValue = "") String dealYear,
                                 @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,

                                 @RequestParam(value = "deptId", defaultValue = "") String deptId,
                                 @RequestParam(value = "dealType", defaultValue = "") String dealType,
                                @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                                 @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd

    ) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("dealType", dealType);
        params.put("dataScope", dataScope);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        params.put("firstDayOfYear", DateUtils.getThisYear());
        params.put("dealIncome", dealIncome);
        params.put("dealYear", dealYear);
        params.put("deptId", deptId);
        //params.put("userId", userId);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<BizDealDto> sysDealDtos = dealService.selectListStatistics(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysDealDtos);
        return AppMessage.success(dataTable, "查询汇总合同成功");
    }


    @RequestMapping(value = "/audit/count", method = RequestMethod.GET)
    public AppMessage selectAuditCount(HttpServletRequest request) {
        String userId = ServletUtils.getSessionUserId();
        Map<String, Object> params = new HashMap<>();
        params.put("entrustUserId", userId);
        List<SysUserDto> userDtos = userService.selectPage(params);

        Map<String, Object> param = new HashMap<>();
        if (null != userDtos && userDtos.size() > 0) {
            param.put("entrustUserId", "true");
        }
        param.put("userId", userId);
        String selectAuditCount = dealService.selectAuditCount(param);
        return AppMessage.success(selectAuditCount, "查询待审核合同数量成功");
    }

    @RequestMapping(value = "/dealOutTime/count", method = RequestMethod.GET)
    public AppMessage dealOutTime(HttpServletRequest request) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }


        List<BizDealDto> list = dealService.selectDealOutTime(params);
        return AppMessage.success(list.size(), "查询履行超时合同");
    }

    @RequestMapping(value = "/dealOutTime", method = RequestMethod.GET)
    public AppMessage dealOutTimeList(
                                      @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                      @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        PageHelper.startPage(pageNo, pageSize);
        List<BizDealDto> list = dealService.selectDealOutTime(params);
        TableDataInfo dataTable = getDataTable(list);
        return AppMessage.success(dataTable, "查询履行超时合同");
    }

    /**
     * 集团合同下载
     *
     * @return
     */
    @Log(logContent = "超时合同下载", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/dealOutTime/export", method = RequestMethod.GET)
    public AppMessage export( HttpServletResponse response) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<>();

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }

        List<BizDealDto> sysDealDtos = dealService.selectDealOutTime(params);
        List<DealOutTimeVo> dealVoS = new ArrayList<>();

        for (BizDealDto dealDto : sysDealDtos) {
            DealOutTimeVo dealVo = new DealOutTimeVo();
            //dealVo.setDealSignTime(DateUtils.);
            BeanUtils.copyBeanProp(dealVo, dealDto);
            dealVo.setDealStatus(DealTypeEnum.getEnumByKey(dealDto.getDealType()));
            dealVo.setDealType(DealStatusEnum.getEnumByKey(dealDto.getDealStatus()));
            dealVoS.add(dealVo);
        }
        ExcelUtil<DealOutTimeVo> util = new ExcelUtil<DealOutTimeVo>(DealOutTimeVo.class);
        return util.exportExcelBrowser(response, dealVoS, "履行超时合同");
    }


    @RequestMapping(value = "/startproj", method = RequestMethod.GET)
    public AppMessage query4Project(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                    @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                    @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                    @RequestParam(value = "dealId", defaultValue = "") String dealId,
                                    @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                    @RequestParam(value = "dealValueMax", defaultValue = "") String dealValueMax,
                                    @RequestParam(value = "dealValueMin", defaultValue = "") String dealValueMin,
                                    @RequestParam(value = "contractId", defaultValue = "") String contractId,
                                    @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                                    @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd,
                                    @RequestParam(value = "dealType", defaultValue = "") String dealType,
                                    @RequestParam(value = "statusList", defaultValue = "") String statusList,
                                    @RequestParam(value = "dealStart", defaultValue = "") String dealStart,
                                    @RequestParam(value = "dealStartFlag", defaultValue = "") String dealStartFlag,
                                    @RequestParam(value = "dealEnd", defaultValue = "") String dealEnd,
                                    @RequestParam(value = "dealEndFlag", defaultValue = "") String dealEndFlag,
                                    @RequestParam(value = "dealReportNo", defaultValue = "") String dealReportNo,
                                    @RequestParam(value = "typeList", defaultValue = "") String typeList,
                                    @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                                    @RequestParam(value = "hasJoinedDeal", defaultValue = "") String hasJoinedDeal,
                                    @RequestParam(value = "deptId", defaultValue = "") String deptId
    ) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealNo", dealNo);
        params.put("dealId", dealId);
        params.put("dealName", dealName);
        params.put("dealValueMax", dealValueMax);
        params.put("dealValueMin", dealValueMin);
        params.put("contractId", contractId);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("dealType", dealType);

        params.put("userIdSpecial", userId);
        params.put("deptIdSpecial", userDto.getDeptId());

        params.put("dealReportNo", dealReportNo);
        if (StringUtils.isNotEmpty(typeList)) {
            params.put("typeList", typeList.split(","));
        }
        params.put("dealIncome", dealIncome);
        params.put("hasJoinedDeal", hasJoinedDeal);
        params.put("deptId", deptId);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        params.put("userId2", userId);
        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            params.put("statusList", statusList.split(","));
        }
        //履行开始时间
        params.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        params.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        params.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        params.put("dealEndFlag", dealEndFlag);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<BizDealDto> sysDealDtos = dealService.selectList3(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysDealDtos);
        return AppMessage.success(dataTable, "查询合同成功");
    }

    /**
     *
     * @param args userId logModule logObject logType logContent
     * @return
     */
    private SysLogDto setSysLog(String...args){
        SysLogDto log=new SysLogDto();
        log.setLogId(UUID.randomUUID().toString().trim().replaceAll("-", ""));
        log.setUserId(args[0]);
        log.setLogTime(new Date());
        log.setLogModule(args[1]);
        log.setLogObject(args[2]);
        log.setLogType(args[3]);
        log.setLogContent(args[4]);

        return log;
    }

    @ApiOperation("合同拆分")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "dealId",value = "合同id"),
            @ApiImplicitParam(name = "deptId",value = "新的承办部门id"),
            @ApiImplicitParam(name = "userName",value = "新的承办人"),
            @ApiImplicitParam(name = "dealValue",value = "新合同金额")
    })
    /**
     * 合同拆分
     */
    @RequestMapping(value = "/dealSplit",method = RequestMethod.GET)
    public AppMessage dealSplit(
            @RequestParam("dealId")String dealId,
            @RequestParam("deptId")String deptId,
            @RequestParam("userName")String userName,
            @RequestParam("dealValue")String dealValue)throws Exception{
        return dealService.dealSplit(dealId,deptId,userName,dealValue);
    }
}

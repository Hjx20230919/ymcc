package cn.com.cnpc.cpoa.controller.prodsys;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.BizDealAttachDto;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsApprovalService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsCheckStepService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.pdf.PDFBuildUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.DealParamVo;
import cn.com.cnpc.cpoa.vo.DealVo;
import cn.com.cnpc.cpoa.vo.prodsys.DealPsVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

/**
 * <>
 *
 * @author anonymous
 * @create 23/02/2020 10:15
 * @since 1.0.0
 */
@RestController
@RequestMapping("/psdeal")
public class BizDealPsController extends BaseController {

    private static final Logger log = LoggerFactory.getLogger(BizDealPsController.class);

    @Autowired
    BizDealPsService dealService;

    @Autowired
    AttachService attachService;

    @Autowired
    DeptService deptService;

    @Autowired
    UserService userService;

    @Autowired
    DealService dealDService;

    @Autowired
    private BizDealPsCheckStepService bizCheckStepService;

    @Autowired
    private BizDealPsCheckStepService diyCheckStepService;

    @Autowired
    private BizDealPsApprovalService activitiService;

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;

    @Value("${file.tempurl}")
    private String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private String baseFontUrl;

    @Log(logContent = "修改提前开工", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id, @RequestBody DealParamVo paramVo) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        BizDealPsDto deal = dealService.selectByKey(id);
        if (null == deal) {
            throw new AppException("当前提前开工不存在");
        }

        DealVo dealVo = paramVo.getDealVo();
        List<AttachVo> attachVos = paramVo.getAttachVos();
        String type = paramVo.getType();
        //准备
        BizDealPsDto dealDto = new BizDealPsDto();
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
            boolean updateChain = dealService.updateChain(dealDto, attachDtos, dealAttachDtos, type, userId);
            if (updateChain) {
                if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)||DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)) {
                    //新增履行合同统计记录（含删除）
                    BizDealDto bizDealDto=new BizDealDto();
                    BeanUtils.copyBeanProp(bizDealDto,dealDto);
                    bizDealStatisticsService.addDealStatistics(bizDealDto);
                }

                return AppMessage.success(id, "更新提前开工成功");
            }
        } else {
            return AppMessage.errorObjId(id, "更新提前开工失败,提前开工正在审核中");
        }

        return AppMessage.errorObjId(id, "更新提前开工失败");
    }

    @Log(logContent = "删除提前开工", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete( @PathVariable String id) {
        //不能删除已经存在结算的合同上
//        Map<String, Object> param = new HashMap<>();
//        param.put("dealId", id);
//        List<BizSettlementDto> settlementDtos = settlementService.selectList(param);
//        if (null != settlementDtos && settlementDtos.size() > 0) {
//            return AppMessage.errorObjId(id, "删除合同失败,该合同已存在结算");
//        }
        String dealNo = dealService.selectByKey(id).getDealNo();

        boolean delete = dealService.deleteDealAttach(id);
        if (delete) {
            return AppMessage.success(dealNo, "删除提前开工成功");
        }
        return AppMessage.errorObjId(dealNo, "删除提前开工失败");
    }

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage queryPage( @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                 @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                                 @RequestParam(value = "dealId", defaultValue = "") String dealId,
                                 @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                 @RequestParam(value = "dealValueMax", defaultValue = "") String dealValueMax,
                                 @RequestParam(value = "dealValueMin", defaultValue = "") String dealValueMin,
                                 @RequestParam(value = "contractId", defaultValue = "") String contractId,
                                 @RequestParam(value = "deptId", defaultValue = "") String deptId,
                                 @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                                 @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd,
                                 @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                 @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd,
                                 @RequestParam(value = "dealType", defaultValue = "") String dealType,
                                 @RequestParam(value = "statusList", defaultValue = "") String statusList,
                                 @RequestParam(value = "dealStart", defaultValue = "") String dealStart,
                                 @RequestParam(value = "dealStartFlag", defaultValue = "") String dealStartFlag,
                                 @RequestParam(value = "dealEnd", defaultValue = "") String dealEnd,
                                 @RequestParam(value = "dealEndFlag", defaultValue = "") String dealEndFlag,
                                 @RequestParam(value = "dealReportNo", defaultValue = "") String dealReportNo,
                                 @RequestParam(value = "typeList", defaultValue = "") String typeList,
                                 @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome
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
        params.put("deptId", deptId);
        params.put("dealSignTimeStart", dealSignTimeStart);
        params.put("dealSignTimeEnd", dealSignTimeEnd);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);
        params.put("dealType", dealType);

        params.put("userIdSpecial", userId);
        params.put("deptIdSpecial", userDto.getDeptId());

        params.put("dealReportNo", dealReportNo);
        if (StringUtils.isNotEmpty(typeList)) {
            params.put("typeList", typeList.split(","));
        }
        params.put("dealIncome", dealIncome);

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
        List<BizDealPsDto> sysDealDtos = dealService.selectList3(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysDealDtos);
        return AppMessage.success(dataTable, "查询提前开工成功");
    }

    /**
     * 新增合同
     *
     * @param paramVo
     * @return
     */
    @Log(logContent = "新增提前开工", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(method = RequestMethod.POST)
    public AppMessage add( @RequestBody DealParamVo paramVo) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        DealVo dealVo = paramVo.getDealVo();
        List<AttachVo> attachVos = paramVo.getAttachVos();
        String type = paramVo.getType();
        //准备
        BizDealPsDto dealDto = new BizDealPsDto();
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
            attachDto.setOwnerType(FileOwnerTypeEnum.DEALPS.getKey());
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
            dealAttachDtos.add(dealAttachDto);
        }
        if (!dealService.saveChain(dealDto, attachDtos, dealAttachDtos, type)) {
            return AppMessage.error("新增提前开工出错");
        }
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            //新增履行合同统计记录
            BizDealDto bizDealDto=new BizDealDto();
            BeanUtils.copyBeanProp(bizDealDto,dealDto);
            bizDealStatisticsService.addDealStatistics(bizDealDto);
        }


        return AppMessage.success(dealId, "新增提前开工成功");
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
        List<BizDealPsDto> dealAttachDtos = new ArrayList<>();

        if ("audited".equals(status)) {
            //查询用户已审核合同
            dealAttachDtos = dealService.selectAuditedList(param);
        } else {
            dealAttachDtos = dealService.selectUserList(param);
        }

        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(dealAttachDtos);
        return AppMessage.success(dataTable, "查询当前用户可审核提前开工列表！");
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
                              @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                              @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd,
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
        param.put("createAtStart", createAtStart);
        param.put("createAtEnd", createAtEnd);
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

        List<BizDealPsDto> sysDealDtos = dealService.selectListForExport(param);
        List<DealPsVo> dealVoS = new ArrayList<>();

        for (BizDealPsDto dealDto : sysDealDtos) {
            DealPsVo dealVo = new DealPsVo();
            //dealVo.setDealSignTime(DateUtils.);
            BeanUtils.copyBeanProp(dealVo, dealDto);
            dealVo.setDealStatus(DealStatusEnum.getEnumByKey(dealDto.getDealStatus()));
            dealVo.setSettleStatus(SettlementStatusEnum.getEnumByKey(dealDto.getSettleStatus()));
            dealVo.setDealType(DealTypeEnum.getEnumByKey(dealDto.getDealType()));
            dealVoS.add(dealVo);
        }
        ExcelUtil<DealPsVo> util = new ExcelUtil<DealPsVo>(DealPsVo.class);
        return util.exportExcelBrowser(response, dealVoS, "提前开工信息");
    }


    @Log(logContent = "保存提交提前开工", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/{id}", method = RequestMethod.PUT)
    public AppMessage update( @PathVariable String id, @RequestParam(value = "type", defaultValue = "") String type) throws Exception {
        BizDealPsDto dealDto = dealService.selectByKey(id);
        if (null == dealDto) {
            throw new AppException("当前提前开工不存在");
        }
        dealDto.setDealStatus(DealStatusEnum.BUILDAUDITING.getKey());
        dealService.updateNotNull(dealDto);
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            String stepNo = bizCheckStepService.selectMaxStepNo(dealDto.getDealId());
            activitiService.initBaseActiviti(dealDto.getUserId(), dealDto.getDealId(), CheckTypeEnum.DEALPS.getKey(), dealDto.getDeptId(), stepNo, null);
        } else {
            bizCheckStepService.updateBackObj(id);
        }

        return AppMessage.success(id, "保存提交提前开工成功！");
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
        List<BizDealPsDto> sysDealDtos = dealService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(sysDealDtos);
        return AppMessage.success(dataTable, "查询汇总提前开工成功");
    }

    @RequestMapping(value = "dealId", method = RequestMethod.GET)
    public AppMessage query( @RequestParam(value = "dealId", defaultValue = "") String dealId
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", dealId);
        List<BizDealPsDto> sysDealDtos = dealService.selectList(params);
        return AppMessage.success(sysDealDtos, "查询提前开工成功");
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
            List<BizDealPsDto> sysDealDtos = dealService.selectList(params);
            BizDealPsDto dealDto = sysDealDtos.get(0);
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
            String dealMan =dealDService.getDealContractCharge(dealContract,dealId);
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
            parametersMap.put("dealStart", null != dealStart ? DateUtils.dateTime(dealStart) : null);
            parametersMap.put("dealEnd", null != dealEnd ? DateUtils.dateTime(dealEnd) : null);
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
        return AppMessage.success(selectAuditCount, "查询待审核提前开工数量成功");
    }

}

package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.SettlementAuditPo;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.utils.*;
import cn.com.cnpc.cpoa.utils.pdf.PDFBuildUtils;
import cn.com.cnpc.cpoa.utils.word.WordTemplate;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.SettleDetailVo;
import cn.com.cnpc.cpoa.vo.SettleParamVo;
import cn.com.cnpc.cpoa.vo.SettlementVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * 结算金额控制器
 *
 * @author scchenyong@189.cn
 * @create 2019-01-14
 */
@RestController
@RequestMapping("/settlement")
public class BizSettlementController extends BaseController{

    private static Logger logger = LoggerFactory.getLogger(BizSettlementController.class);

    @Autowired
    SettlementService settlementService;

    @Autowired
    UserService userService;

    @Autowired
    DealService dealService;

    @Autowired
    private BizCheckStepService diyCheckStepService;
    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    private ActivitiService activitiService;

    @Autowired
    AttachService attachService;

    @Autowired
    SettleDetailService settleDetailService;


    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    DeptService deptService;

    @Autowired
    FreezeService freezeService;

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;

    @Value("${file.wordSettleTemplateUrl}")
    private  String wordSettleTemplateUrl;

    @Value("${file.tempurl}")
    private  String TEMPURL;

    @Value("${file.PDFPicUrl}")
    private  String PDFPicUrl;

    @Value("${file.baseFontUrl}")
    private  String baseFontUrl;



    @Log(logContent = "新增结算",logModule = LogModule.SETTLEMENT,logType = LogType.OPERATION)
    @RequestMapping(method = POST)
    public AppMessage add(@RequestBody SettleParamVo settleParamVo)throws Exception {
        String userId= ServletUtils.getSessionUserId();
        //准备
        SettlementVo settlementVo = settleParamVo.getSettlementVo();
        List<AttachVo> attachVos = settleParamVo.getAttachVos();
        List<SettleDetailVo> settleDetailVos = settleParamVo.getSettleDetailVos();
        String type = settleParamVo.getType();
        //判断是否在审核中
        Map<String,Object> param=new HashMap<>();
        param.put("dealId",settlementVo.getDealId());
        boolean auditing = settlementService.isAuditing(param);
        //获取指定人
        BizDealDto bizDealDto = dealService.selectByKey(settlementVo.getDealId());

        //正在审核中 不能发起新增
        if(auditing){
            return AppMessage.error("新增结算失败，该结算正在处理中");
        }

        if("收入".equals(bizDealDto.getDealIncome())||settlementService.isAvailableAmount(settlementVo.getSettleAmount(),settlementVo.getDealId())){
            List<BizAttachDto> attachDtos=new ArrayList<>();
            List<BizSettlementAttachDto> settlementAttachDtos=new ArrayList<>();

            if(!DealStatusEnum.PROGRESSAUDITING.getKey().equals(bizDealDto.getDealStatus())){
                return AppMessage.error("新增结算失败，合同不在履行中");
            }

            String settleId= StringUtils.getUuid32();
            //1 新增结算
            BizSettlementDto settlementDto=new BizSettlementDto();
            settlementDto.setSettleId(settleId);
            settlementDto.setSettleStatus(SettleWebTypeEnum.getEnumByKey(type));
            settlementDto.setDeptId(bizDealDto.getDeptId());
            settlementDto.setUserId(userId);
            settlementDto.setDealId(settlementVo.getDealId());
            settlementDto.setSettleAmount(settlementVo.getSettleAmount());
            settlementDto.setSettleAcount(settlementVo.getSettleAcount());
            settlementDto.setCreateTime(freezeService.getSettleDate());
            settlementDto.setPayableTime(settlementVo.getPayableTime());
            settlementDto.setActualPayAmount(settlementVo.getActualPayAmount());
            settlementDto.setActualPayTime(settlementVo.getActualPayTime());
            settlementDto.setActualPayMan(settlementVo.getActualPayMan());
            settlementDto.setActualPayNotes(settlementVo.getActualPayNotes());
            settlementDto.setNotes(settlementVo.getNotes());
            settlementDto.setSettleBank(settlementVo.getSettleBank());
            settlementDto.setSettleAcount(settlementVo.getSettleAcount());
            settlementDto.setActualPayMan(settlementVo.getActualPayMan());

            settlementDto.setContName2(settlementVo.getContName2());
            settlementDto.setOrgNo(settlementVo.getOrgNo());
            settlementDto.setContAddrss(settlementVo.getContAddrss());
            settlementDto.setLinkNum(settlementVo.getLinkNum());
//        int save = settlementService.save(settlementDto);
//        if (save == 1) {
//            return AppMessage.result(settlementDto.getUserId());
//        }
            //2 附件
            for (AttachVo attachVo:attachVos) {
               // String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");
                String attachId=attachVo.getAttachId();
                BizAttachDto attachDto =new BizAttachDto();
                attachDto.setAttachId(attachId);
                attachDto.setOwnerId(settleId);
                attachDto.setOwnerType(FileOwnerTypeEnum.SETTLE.getKey());
                attachDto.setFileName(attachVo.getFileName());
                attachDto.setFileType(attachVo.getFileType());
                attachDto.setFileUri(attachVo.getFileUri());
                attachDto.setFileSize(attachVo.getFileSize());
                attachDto.setCreateTime(DateUtils.getNowDate());
                attachDto.setUserId(userId);
                attachDto.setNotes(attachVo.getNotes());
                attachDtos.add(attachDto);

                // 3合同附件
                BizSettlementAttachDto settlementAttachDto=new BizSettlementAttachDto();
                settlementAttachDto.setId(StringUtils.getUuid32());
                settlementAttachDto.setSettleId(settleId);
                settlementAttachDto.setAttachId(attachId);
                settlementAttachDtos.add(settlementAttachDto);
            }
            //3 结算详情
            List<BizSettleDetailDto> bizSettleDetailDtos =new ArrayList<>();
            for (SettleDetailVo settleDetailVo:settleDetailVos) {
                BizSettleDetailDto settleDetailDto=new BizSettleDetailDto();
                BeanUtils.copyBeanProp(settleDetailDto,settleDetailVo);
                settleDetailDto.setDetailId(StringUtils.getUuid32());
                bizSettleDetailDtos.add(settleDetailDto);
            }


            boolean is=settlementService.saveChain(settlementDto, attachDtos, settlementAttachDtos, type,bizDealDto,bizSettleDetailDtos);
            if(is){
                if(SettleWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
                    bizDealStatisticsService.addDealStatistics(dealService.selectByKey(settlementDto.getDealId()));
                }
                return AppMessage.success(settleId, "新增结算成功");
            }
        }else{
            return AppMessage.error("新增结算失败，结算总额超上限");
        }

        return AppMessage.error("新增结算失败");
    }


    @Log(logContent = "修改结算",logModule = LogModule.SETTLEMENT,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id, @RequestBody SettleParamVo settleParamVo)throws Exception {

        BizSettlementDto bizSettlementDto = settlementService.selectByKey(id);
        if(null==bizSettlementDto){
            throw new AppException("当前结算不存在");
        }

        String userId= ServletUtils.getSessionUserId();
        //准备
        SettlementVo settlementVo = settleParamVo.getSettlementVo();
        List<AttachVo> attachVos = settleParamVo.getAttachVos();
        List<SettleDetailVo> settleDetailVos = settleParamVo.getSettleDetailVos();
        String type = settleParamVo.getType();
        List<BizAttachDto> attachDtos=new ArrayList<>();
        List<BizSettlementAttachDto> settlementAttachDtos=new ArrayList<>();
        //查询用户所在部门
        SysUserDto sysUserDto = userService.selectByKey(userId);
        //获取指定人
        BizDealDto bizDealDto = dealService.selectByKey(settlementVo.getDealId());

        Map<String,Object> param=new HashMap<>();
        param.put("dealId",settlementVo.getDealId());
        boolean auditing = settlementService.isAuditing(param);

        logger.info("结算状态"+settlementVo.getSettleStatus());

        //正在审核中 不能发起新增。 回退可以
        if(auditing&&!SettlementStatusEnum.BACK.getKey().equals(settlementVo.getSettleStatus())){
            return AppMessage.errorObjId(id,"更新结算失败，该结算正在审核中");
        }
        //只能修改草稿 和 结算回退
        //boolean canUpdate=settlementService.canUpdate(settlementVo.getSettleStatus());
        if("收入".equals(bizDealDto.getDealIncome())||settlementService.isAvailableAmount(settlementVo.getSettleAmount(),settlementVo.getDealId())){
            if(!DealStatusEnum.PROGRESSAUDITING.getKey().equals(bizDealDto.getDealStatus())){
                return AppMessage.errorObjId(id,"修改结算失败，合同不在履行中");
            }

            if(true) {
                //1 修改结算
                BizSettlementDto settlementDto = new BizSettlementDto();
                settlementDto.setSettleId(id);
                settlementDto.setSettleStatus(SettleWebTypeEnum.getEnumByKey(type));
                settlementDto.setDeptId(sysUserDto.getDeptId());
                settlementDto.setUserId(userId);
                settlementDto.setDealId(settlementVo.getDealId());
                settlementDto.setSettleAmount(settlementVo.getSettleAmount());
                settlementDto.setSettleAcount(settlementVo.getSettleAcount());
                settlementDto.setPayableTime(settlementVo.getPayableTime());
                settlementDto.setActualPayAmount(settlementVo.getActualPayAmount());
                settlementDto.setActualPayTime(settlementVo.getActualPayTime());
                settlementDto.setActualPayMan(settlementVo.getActualPayMan());
                settlementDto.setActualPayNotes(settlementVo.getActualPayNotes());
                settlementDto.setNotes(settlementVo.getNotes());
                settlementDto.setSettleBank(settlementVo.getSettleBank());
                settlementDto.setSettleAcount(settlementVo.getSettleAcount());
                settlementDto.setActualPayMan(settlementVo.getActualPayMan());

                settlementDto.setContName2(settlementVo.getContName2());
                settlementDto.setOrgNo(settlementVo.getOrgNo());
                settlementDto.setContAddrss(settlementVo.getContAddrss());
                settlementDto.setLinkNum(settlementVo.getLinkNum());

//        settlementService.updateNotNull(settlementDto);

                //2 附件
                for (AttachVo attachVo : attachVos) {
                   // String attachId = UUID.randomUUID().toString().trim().replaceAll("-", "");
                    if(attachService.isDoubleFile(attachVos)){
                        return AppMessage.errorObjId(id,"不能上传重复的文件！");
                    }

                    String attachId =attachVo.getAttachId();
                    BizAttachDto attachDto = new BizAttachDto();
                    attachDto.setAttachId(attachId);
                    attachDto.setOwnerId(id);
                    attachDto.setFileName(attachVo.getFileName());
                    attachDto.setFileType(attachVo.getFileType());
                    attachDto.setFileUri(attachVo.getFileUri());
                    attachDto.setFileSize(attachVo.getFileSize());
                    attachDto.setCreateTime(DateUtils.getNowDate());
                    attachDto.setUserId(attachVo.getUserId());
                    attachDto.setNotes(attachVo.getNotes());
                    attachDtos.add(attachDto);

                    // 3合同附件
                    BizSettlementAttachDto settlementAttachDto = new BizSettlementAttachDto();
                    settlementAttachDto.setId(StringUtils.getUuid32());
                    settlementAttachDto.setSettleId(id);
                    settlementAttachDto.setAttachId(attachId);
                    settlementAttachDtos.add(settlementAttachDto);
                }
                List<BizSettleDetailDto>  bizSettleDetailDtos=new ArrayList<>();
                //3 结算详情
                for (SettleDetailVo settleDetailVo:settleDetailVos) {
                    BizSettleDetailDto settleDetailDto=new BizSettleDetailDto();
                    BeanUtils.copyBeanProp(settleDetailDto,settleDetailVo);
                    settleDetailDto.setDetailId(StringUtils.getUuid32());
                    bizSettleDetailDtos.add(settleDetailDto);
                }

                boolean updateChain = settlementService.updateChain(settlementDto, attachDtos, settlementAttachDtos, type,userId,bizDealDto,bizSettleDetailDtos);
                if(updateChain){
                    if(SettleWebTypeEnum.BUILDAUDITING.getKey().equals(type)||SettleWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)){
                        bizDealStatisticsService.addDealStatistics(dealService.selectByKey(settlementDto.getDealId()));
                    }
                    return AppMessage.success(id, "更新合同结算成功");
                }
            }else {
                return AppMessage.errorObjId(id,"结算正在审核中");
            }
        }else{
            return AppMessage.errorObjId(id,"更新结算失败，结算总额超上限");
        }
        return AppMessage.success(id, "更新结算成功");
    }


    @Log(logContent = "删除结算",logModule = LogModule.SETTLEMENT,logType = LogType.OPERATION)
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        String dealId=settlementService.selectByKey(id).getDealId();
        String dealNo=dealService.selectByKey(dealId).getDealNo();
        Boolean detele = settlementService.deleteChain(id);
        if (detele) {
            return AppMessage.success(dealNo, "删除结算成功");
        }
        return AppMessage.errorObjId(dealNo,"删除结算失败");
    }

    @RequestMapping(method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "pageNum",defaultValue = "1") int pageNo,
                               @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
                               @RequestParam(value = "settleStatus",defaultValue="") String settleStatus,
                               @RequestParam(value = "dealId",defaultValue="") String dealId) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("settleStatus", settleStatus);
        params.put("dealId", dealId);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<BizSettlementDto> bizSettlementDtoDtos = settlementService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(bizSettlementDtoDtos);
        return AppMessage.success(dataTable, "查询结算成功");
    }

    /**
     * 查询当前用户可审核结算列表
     * @return
     */
    @RequestMapping(value="audit",method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "pageNum",defaultValue = "1") int pageNo,
                                                        @RequestParam(value = "pageSize",defaultValue = "20") int pageSize,
                                                        @RequestParam(value = "dealNo",defaultValue="") String dealNo,
                                                        @RequestParam(value ="dealName",defaultValue="") String dealName,
                                                        @RequestParam(value ="settleId",defaultValue="") String settleId,
                                                        @RequestParam(value ="dealValueMax",defaultValue="") String dealValueMax,
                                                        @RequestParam(value ="dealValueMin",defaultValue="") String dealValueMin,
                                                        @RequestParam(value ="contractId",defaultValue="") String contractId,
                                                        @RequestParam(value ="dealSignTimeStart",defaultValue="") String dealSignTimeStart,
                                                        @RequestParam(value ="dealSignTimeEnd",defaultValue="") String dealSignTimeEnd,
                                                        @RequestParam(value ="dealType",defaultValue="") String dealType,
                                                        @RequestParam(value ="status",defaultValue="") String status) {
        String userId= ServletUtils.getSessionUserId();
        Map<String,Object> params=new HashMap<>();
        params.put("entrustUserId",userId);
        List<SysUserDto> userDtos = userService.selectPage(params);

        Map<String,Object> param=new HashMap<>();
        if(null!=userDtos&&userDtos.size()>0){
            param.put("entrustUserId","true");
        }

        param.put("userId",userId);
   //     param.put("settleId",settleId);
        param.put("dealNo", dealNo);
        param.put("dealName", dealName);
        param.put("dealValueMax", dealValueMax);
        param.put("dealValueMin", dealValueMin);
        param.put("contractId", contractId);
        param.put("dealSignTimeStart", dealSignTimeStart);
        param.put("dealSignTimeEnd", dealSignTimeEnd);
        param.put("dealType", dealType);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        List<SettlementAuditPo> settlementDtos=null;
        //2、执行查询
        if("audited".equals(status)){
            //查询用户已审核合同
            settlementDtos=settlementService.selectAuditedList(param);
        }else{
            settlementDtos=settlementService.selectUserList(param);
        }
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(settlementDtos);
        return AppMessage.success(dataTable,"查询当前用户可审核结算列表！");
    }


    @Log(logContent = "保存提交结算",logModule = LogModule.SETTLEMENT,logType = LogType.OPERATION)
    @RequestMapping(value = "/submit/{id}", method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id,@RequestParam(value ="type",defaultValue="") String type) throws Exception {
        BizSettlementDto settlementDto = settlementService.selectByKey(id);
        if(null==settlementDto){
            throw new AppException("当前结算不存在");
        }
        settlementDto.setSettleStatus(SettlementStatusEnum.BUILDAUDITING.getKey());
        settlementService.updateNotNull(settlementDto);
        BizDealDto dealDto = dealService.selectByKey(settlementDto.getDealId());
        if(SettleWebTypeEnum.BUILDAUDITING.getKey().equals(type)){
            String stepNo = bizCheckStepService.selectMaxStepNo(settlementDto.getSettleId());
            activitiService.initBaseActiviti(settlementDto.getUserId(),settlementDto.getSettleId(),"settle",dealDto.getDeptId(),stepNo,null);
        }else{
            bizCheckStepService.updateBackObj(id);
        }
        //新增履行合同统计
        bizDealStatisticsService.addDealStatistics(dealDto);
        return AppMessage.success(id,"保存提交结算成功！");
    }


    /**
     * 导出 合同签约审查审批表.docx
     * @return
     */
    @Log(logContent = "导出合同付款审批表",logModule = LogModule.SETTLEMENT,logType = LogType.OPERATION)
    @RequestMapping(value="word",method = RequestMethod.GET)
    public void word(
                     @RequestParam(value = "settleId") String settleId,
                     @RequestParam(value = "userId") String userId,
                     HttpServletResponse response) throws Exception{
        //   String userId= getSessionUserId(request);
        Map<String, Object> wordDataMap = new HashMap<String, Object>();// 存储报表全部数据
        Map<String, Object> parametersMap = new HashMap<String, Object>();// 存储报表中不循环的数据
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            BizSettlementDto settlementDto = settlementService.selectByKey(settleId);
            String dealId = settlementDto.getDealId();
            //添加 合同签约审查审批表基础信息
            //  BizDealDto dealDto = dealService.selectByKey(settlementDto.getDealId());
            Map<String,Object> params=new HashMap<>();
            params.put("dealId",dealId);
            List<BizDealDto> sysDealDtos = dealService.selectList(params);
            BizDealDto dealDto =sysDealDtos.get(0);
            //  String dealReportNo = dealDto.getDealReportNo();
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
            String dealContract = dealDto.getDealContract();

            String paymentType = dealDto.getPaymentType();

            //已结算金额
            Double dealSettlement = dealDto.getDealSettlement();
            //本次结算金额
            Double settleAmount = settlementDto.getSettleAmount();
            //应付款时间
            //Date payableTime = settlementDto.getPayableTime();
            //实际付款时间
            Date actualPayTime = settlementDto.getActualPayTime();
            //String actualPayMan = settlementDto.getActualPayMan();
            //String notes = settlementDto.getNotes();
            //  Double actualPayAmount = settlementDto.getActualPayAmount();
            String settleBank = settlementDto.getSettleBank();
            String settleAcount = settlementDto.getSettleAcount();


            //  parametersMap.put("dealReportNo",dealReportNo);
            parametersMap.put("dealNo",dealNo);
            parametersMap.put("dealName",dealName);
            parametersMap.put("dealType", DealTypeEnum.getEnumByKey(dealType));
            parametersMap.put("dealStart",null!=dealStart?DateUtils.dateTime(dealStart):null);
            parametersMap.put("dealEnd",null!=dealEnd?DateUtils.dateTime(dealEnd):null);
            parametersMap.put("dealIncome",dealIncome);
            //资金流向 INCOME   收入 OUTCOME  支出
            parametersMap.put("dealIncome",dealIncome);
            if("OUTCOME".equalsIgnoreCase(dealIncome)){
                //付款方
                parametersMap.put("dealContract",dealContract);
                //收款方
                parametersMap.put("contName",contName);
            }else{
                //付款方
                parametersMap.put("dealContract",contName);
                //收款方
                parametersMap.put("contName",dealContract);
            }

            parametersMap.put("dealFunds",dealFunds);
            parametersMap.put("dealSelection",dealSelection);
            parametersMap.put("dealNotes",dealNotes);
            parametersMap.put("dealValue",0!=dealValue?df.format(dealValue):"0.00");
            parametersMap.put("dealCurrency",dealCurrency);
            parametersMap.put("deptName",deptName);
            parametersMap.put("userName",userName);
        //    parametersMap.put("contName",contName);
            parametersMap.put("dutyMan",dutyMan);
         //   parametersMap.put("dealContract",dealContract);
            parametersMap.put("paymentType",paymentType);
            parametersMap.put("dealSettlement",0!=dealSettlement?df.format(dealSettlement):"0.00");
            parametersMap.put("settleAmount",0!=settleAmount?df.format(settleAmount):"0.00");
            //     parametersMap.put("payableTime",null!=payableTime?DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(payableTime):null);
            parametersMap.put("actualPayTime",null!=actualPayTime?DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(actualPayTime):null);
            // parametersMap.put("actualPayMan",actualPayMan);
            // parametersMap.put("notes",notes);
            //   parametersMap.put("actualPayAmount",actualPayAmount);
            parametersMap.put("settleBank",settleBank);
            parametersMap.put("settleAcount",settleAcount);

            parametersMap.put("remainAmount",BigDecimalUtils.subtract(dealValue,dealSettlement)==0?0.00: df.format(BigDecimalUtils.subtract(dealValue,dealSettlement)));


            List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();

            //添加 合同签约审查审批表审核意见
            List<CheckStepPo> checkStepPoList = diyCheckStepService.selectDetails(null,settlementDto.getSettleId(),userId);
            for (CheckStepPo checkStepPo:checkStepPoList) {
                Map<String, Object> map=new HashMap<>();
                map.put("deptName", checkStepPo.getDeptName());
                map.put("userName", checkStepPo.getUserName());
                map.put("checkNode", checkStepPo.getCheckNode());
                map.put("checkTime",null!=checkStepPo.getCheckTime()?DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()):null);
                table1.add(map);
            }

            wordDataMap.put("table1", table1);
            wordDataMap.put("parametersMap", parametersMap);
            File file = new File(wordSettleTemplateUrl);//改成你本地文件所在目录


            // 读取word模板
            FileInputStream fileInputStream = new FileInputStream(file);
            WordTemplate template = new WordTemplate(fileInputStream);

            // 替换数据
            template.replaceDocument(wordDataMap);

            // 下载到浏览器
            String fileName="合同付款审批表-"+dealDto.getDealNo()+dealDto.getUserName()+DateUtils.dateTime(settlementDto.getCreateTime())+".docx";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            BufferedOutputStream bos = null;
            //输出到浏览器
            try{
                bos = new BufferedOutputStream(response.getOutputStream());
                template.write(bos);
                bos.flush();
            }finally {

                if(null!=bos){
                    bos.close();
                }
            }
        }catch (Exception e){
            throw  new AppException(e.getMessage());
        }
    }
    /**
     * 导出 合同签约审查审批表.pdf
     * @return
     */
    @Log(logContent = "导出合同付款审批表PDF",logModule = LogModule.SETTLEMENT,logType = LogType.OPERATION)
    @RequestMapping(value="pdf",method = RequestMethod.GET)
    public void pdf(@RequestParam(value = "settleId") String settleId,
                     @RequestParam(value = "userId") String userId,
                     HttpServletResponse response) throws Exception{
        //   String userId= getSessionUserId(request);
        Map<String, Object> wordDataMap = new HashMap<String, Object>();// 存储报表全部数据
        Map<String, Object> parametersMap = new HashMap<String, Object>();// 存储报表中不循环的数据
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            BizSettlementDto settlementDto = settlementService.selectByKey(settleId);
            String dealId = settlementDto.getDealId();
            //添加 合同签约审查审批表基础信息
            //  BizDealDto dealDto = dealService.selectByKey(settlementDto.getDealId());
            Map<String,Object> params=new HashMap<>();
            params.put("dealId",dealId);
            List<BizDealDto> sysDealDtos = dealService.selectList(params);
            BizDealDto dealDto =sysDealDtos.get(0);
            //  String dealReportNo = dealDto.getDealReportNo();
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
            String dealReportNo = dealDto.getDealReportNo();
            String dealContract = dealDto.getDealContract();

            String paymentType = dealDto.getPaymentType();

            //已结算金额
            Double dealSettlement = dealDto.getDealSettlement();
            //本次结算金额
            Double settleAmount = settlementDto.getSettleAmount();
            //应付款时间
            //Date payableTime = settlementDto.getPayableTime();
            //实际付款时间
            Date actualPayTime = settlementDto.getActualPayTime();
            //String actualPayMan = settlementDto.getActualPayMan();
            //String notes = settlementDto.getNotes();
            //  Double actualPayAmount = settlementDto.getActualPayAmount();
            String settleBank = settlementDto.getSettleBank();
            String settleAcount = settlementDto.getSettleAcount();
            String contName2 = settlementDto.getContName2();
            String orgNo = settlementDto.getOrgNo();
            String contAddrss = settlementDto.getContAddrss();
            String linkNum = settlementDto.getLinkNum();
            //创建时间
            Date createTime=settlementDto.getCreateTime();

            parametersMap.put("contName2",contName2);
            parametersMap.put("orgNo",orgNo);
            parametersMap.put("contAddrss",contAddrss);
            parametersMap.put("linkNum",linkNum);
            parametersMap.put("settleBank",settleBank);
            parametersMap.put("settleAcount",settleAcount);


            //  parametersMap.put("dealReportNo",dealReportNo);
            parametersMap.put("dealNo",dealNo);
            parametersMap.put("dealName",dealName);
            parametersMap.put("dealReportNo",dealReportNo);
            parametersMap.put("dealType", DealTypeEnum.getEnumByKey(dealType));
            parametersMap.put("dealStart",null!=dealStart?DateUtils.dateTime(dealStart):"");
            parametersMap.put("dealEnd",null!=dealEnd?DateUtils.dateTime(dealEnd):"");
            parametersMap.put("dealIncome",dealIncome);


            //资金流向 INCOME   收入 OUTCOME  支出
            parametersMap.put("dealIncome",dealIncome);
            if("支出".equalsIgnoreCase(dealIncome)){
                //付款方
                parametersMap.put("dealContract",dealContract);
                //收款方
                parametersMap.put("contName",contName);
            }else{
                //付款方
                parametersMap.put("dealContract",contName);
                //收款方
                parametersMap.put("contName",dealContract);
            }

            parametersMap.put("dealFunds",dealFunds);
            parametersMap.put("dealSelection",dealSelection);
            parametersMap.put("dealNotes",dealNotes);
            parametersMap.put("dealValue",0!=dealValue?df.format(dealValue):"0.00");
            parametersMap.put("dealCurrency",dealCurrency);
            parametersMap.put("deptName",deptName);
            parametersMap.put("userName",userName);
           // parametersMap.put("contName",contName);
            parametersMap.put("dutyMan",dutyMan);
         //   parametersMap.put("dealContract",dealContract);
            parametersMap.put("paymentType",paymentType);
            parametersMap.put("dealSettlement",dealSettlement==0?0.00:df.format(dealSettlement));
            parametersMap.put("settleAmount",settleAmount==0?0.00:df.format(settleAmount));
            //     parametersMap.put("payableTime",null!=payableTime?DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(payableTime):null);
            parametersMap.put("actualPayTime",null!=actualPayTime?DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(actualPayTime):null);
            // parametersMap.put("actualPayMan",actualPayMan);
            // parametersMap.put("notes",notes);
            //   parametersMap.put("actualPayAmount",actualPayAmount);
            parametersMap.put("settleBank",settleBank);
            parametersMap.put("settleAcount",settleAcount);



            parametersMap.put("remainAmount",BigDecimalUtils.subtract(dealValue,dealSettlement)==0?0.00: df.format(BigDecimalUtils.subtract(dealValue,dealSettlement)));


            List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();

            //添加 合同签约审查审批表审核意见
            List<CheckStepPo> checkStepPoList = diyCheckStepService.selectDetails(null,settlementDto.getSettleId(),userId);
            for (CheckStepPo checkStepPo:checkStepPoList) {
                Map<String, Object> map=new HashMap<>();
                map.put("deptName", checkStepPo.getDeptName());
                map.put("userName", checkStepPo.getUserName());
                map.put("checkNode", checkStepPo.getCheckNode());
                map.put("checkTime",null!=checkStepPo.getCheckTime()?DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()):null);
                table1.add(map);
            }
            CheckStepPo checkStepPo=checkStepPoList.get(checkStepPoList.size()-1);
            parametersMap.put("createTime",DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(createTime));
            parametersMap.put("downTime",null!=checkStepPo.getCheckTime()?DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()):null);

            wordDataMap.put("table1", table1);
            wordDataMap.put("parametersMap", parametersMap);

            // 下载到浏览器
            String income="支出".equals(dealIncome)?"付款":"收款";
            parametersMap.put("income",income);
            String fileName="合同("+income+")审批表-"+dealDto.getDealNo()+dealDto.getUserName()+DateUtils.dateTime(settlementDto.getCreateTime())+".pdf";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            PDFBuildUtils.buildSettlementPDF(wordDataMap, response,TEMPURL,PDFPicUrl,baseFontUrl);
        }catch (Exception e){
            throw  new AppException(e.getMessage());
        }
    }

    @RequestMapping(value="details",method = RequestMethod.GET)
    public AppMessage queryDetails(@RequestParam(value = "settleId",defaultValue="") String settleId) {

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("settleId", settleId);

        List<BizSettleDetailDto> bizSettleDetailDtos = settleDetailService.selectList(params);
        return AppMessage.success(bizSettleDetailDtos, "查询结算详情成功");
    }

    /**
     * 导出 导出申请开票单.pdf
     * @return
     */
    @Log(logContent = "导出申请开票单",logModule = LogModule.SETTLEMENT,logType = LogType.OPERATION)
    @RequestMapping(value="pdfDetails",method = RequestMethod.GET)
    public void pdfDetails(@RequestParam(value = "settleId") String settleId,
                           @RequestParam(value = "userId") String userId,
                    HttpServletResponse response) throws Exception{
        Map<String, Object> wordDataMap = new HashMap<String, Object>();// 存储报表全部数据
        Map<String, Object> parametersMap = new HashMap<String, Object>();// 存储报表中不循环的数据
        DecimalFormat df = new DecimalFormat("#.00");
        try {
            BizSettlementDto settlementDto = settlementService.selectByKey(settleId);
            String dealId = settlementDto.getDealId();
            //添加 合同签约审查审批表基础信息
            //  BizDealDto dealDto = dealService.selectByKey(settlementDto.getDealId());
            Map<String,Object> params=new HashMap<>();
            params.put("dealId",dealId);
            List<BizDealDto> sysDealDtos = dealService.selectList(params);
            BizDealDto dealDto =sysDealDtos.get(0);

            String contName = dealDto.getContName();
            String dealContract = dealDto.getDealContract();
            String dealName = dealDto.getDealName();
            String dealNo = dealDto.getDealNo();

            parametersMap.put("dealNo",dealNo);
            parametersMap.put("dealName",dealName);
            parametersMap.put("contName",contName);
            parametersMap.put("dealContract",dealContract);

            String settleBank = settlementDto.getSettleBank();
            String settleAcount = settlementDto.getSettleAcount();
            String contName2 = settlementDto.getContName2();
            String orgNo = settlementDto.getOrgNo();
            String contAddrss = settlementDto.getContAddrss();
            String linkNum = settlementDto.getLinkNum();
            String notes = settlementDto.getNotes();

            parametersMap.put("contName2",contName2);
            parametersMap.put("orgNo",orgNo);
            parametersMap.put("contAddrss",contAddrss);
            parametersMap.put("linkNum",linkNum);
            parametersMap.put("settleBank",settleBank);
            parametersMap.put("notes",notes);
            parametersMap.put("settleAcount",settleAcount);

            List<Map<String, Object>> table1 = new ArrayList<Map<String, Object>>();
            Map<String,Object> param=new HashMap<>();
            param.put("settleId",settleId);
            List<BizSettleDetailDto> bizSettleDetailDtos = settleDetailService.selectList(param);
            Double detailMoneySum=0.0;
            Double detailTaxSum=0.0;
            for (BizSettleDetailDto bizSettleDetailDto:bizSettleDetailDtos) {
                Map<String, Object> map=new HashMap<>();
                map.put("detailName", bizSettleDetailDto.getDetailName());
                map.put("detailModel", bizSettleDetailDto.getDetailModel());
                map.put("detailUnit", bizSettleDetailDto.getDetailUnit());
                map.put("detailTotal", null!=bizSettleDetailDto.getDetailTotal()?bizSettleDetailDto.getDetailTotal():0);
                map.put("detailPrice", null!=bizSettleDetailDto.getDetailPrice()?bizSettleDetailDto.getDetailPrice():0.0);
                map.put("detailMoney", null!=bizSettleDetailDto.getDetailMoney()?bizSettleDetailDto.getDetailMoney():0.0);
                map.put("detailRate", null!=bizSettleDetailDto.getDetailRate()?bizSettleDetailDto.getDetailRate():0);
                map.put("detailTax", null!=bizSettleDetailDto.getDetailTax()?bizSettleDetailDto.getDetailTax():0);
                table1.add(map);

                detailMoneySum=BigDecimalUtils.add(detailMoneySum,bizSettleDetailDto.getDetailMoney());
                detailTaxSum=BigDecimalUtils.add(detailTaxSum,bizSettleDetailDto.getDetailTax());
            }

            parametersMap.put("detailMoneySum",detailMoneySum);
            parametersMap.put("detailTaxSum",detailTaxSum);

            //财资部
            Map<String,Object> param2=new HashMap<>();
            param2.put("cfgCode","SAP_DEPARTMENT");
            List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(param2);
            String cfgValue = bizSysConfigDtos.get(0).getCfgValue();

            Map<String,Object> param3=new HashMap<>();
            param3.put("deptId",cfgValue);
            List<SysUserDto> sysUserDtos = userService.selectList(param3);
            List<String> userIds=new ArrayList<>();
            for (SysUserDto userDto:sysUserDtos) {
                userIds.add(userDto.getUserId());
            }

            List<Map<String, Object>> table2 = new ArrayList<Map<String, Object>>();

            //添加 合同签约审查审批表审核意见
            List<CheckStepPo> checkStepPoList = diyCheckStepService.selectDetails(null,settlementDto.getSettleId(),userId);
            for (int i=0;i<checkStepPoList.size();i++) {
                CheckStepPo checkStepPo=checkStepPoList.get(i);
                Map<String, Object> map=new HashMap<>();
                String userId1 = checkStepPo.getUserId();
                if(i<2||userIds.contains(userId1)){
                    map.put("deptName", checkStepPo.getDeptName());
                    map.put("userName", checkStepPo.getUserName());
                    map.put("checkNode", checkStepPo.getCheckNode());
                    map.put("checkTime",null!=checkStepPo.getCheckTime()?DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()):null);
                    table2.add(map);
                }
            }

            wordDataMap.put("table1", table1);
            wordDataMap.put("table2", table2);
            wordDataMap.put("parametersMap", parametersMap);

            // 下载到浏览器
            String fileName="申请开票单.pdf";
            response.setContentType("application/octet-stream;charset=ISO8859-1");
            response.setHeader("Content-Disposition", "attachment;filename="+new String(fileName.getBytes(), StandardCharsets.ISO_8859_1));
            response.addHeader("Pargam", "no-cache");
            response.addHeader("Cache-Control", "no-cache");

            PDFBuildUtils.buildSettleDetailPDF(wordDataMap,response,TEMPURL,PDFPicUrl,baseFontUrl);
        }catch (Exception e){
           e.printStackTrace();
        }


    }

    @RequestMapping(value = "/audit/count",method = RequestMethod.GET)
    public AppMessage selectAuditCount() {
        String userId= ServletUtils.getSessionUserId();
        Map<String,Object> params=new HashMap<>();
        params.put("entrustUserId",userId);
        List<SysUserDto> userDtos = userService.selectPage(params);

        Map<String,Object> param=new HashMap<>();
        if(null!=userDtos&&userDtos.size()>0){
            param.put("entrustUserId","true");
        }
        param.put("userId",userId);
        String selectAuditCount = settlementService.selectAuditCount(param);
        return AppMessage.success(selectAuditCount, "查询待审核结算数量成功");
    }

}

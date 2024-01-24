package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.enums.DealTypeEnum;
import cn.com.cnpc.cpoa.enums.SettlementStatusEnum;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.service.prodsys.BizProjectManager;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.*;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/9/8 9:40
 * @Description:综合管理
 */
@RestController
@RequestMapping("/hd/comprehensive")
public class BizComprehensiveController extends BaseController{

    @Autowired
    ComprehensiveService comprehensiveService;
    @Autowired
    UserService userService;

    @Autowired
    DealService dealService;

    @Autowired
    LogService logService;

    @Autowired
    CheckStepService checkStepService;

    @Autowired
    CheckManService checkManService;

    @Autowired
    CheckNoticeService checkNoticeService;

    @Autowired
    AttachService attachService;

    @Autowired
    DealAttachService dealAttachService;

    @Autowired
    SettlementService settlementService;

    @Autowired
    SettlementAttachService settlementAttachService;

    @Autowired
    SettleDetailService settleDetailService;

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;

    @Autowired
    BizProjectManager bizProjectManager;


    /**
     * 综合查询结算
     * @return
     */
    @RequestMapping(value = "/settlement",method = RequestMethod.GET)
    public AppMessage query( @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                            @RequestParam(value = "dealType", defaultValue = "") String dealType,
                            @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                            @RequestParam(value = "dealName", defaultValue = "") String dealName,
                            @RequestParam(value = "contractId", defaultValue = "") String contractId,
                            @RequestParam(value = "statusList", defaultValue = "") String statusList,
                            @RequestParam(value = "dealReportNo", defaultValue = "") String dealReportNo,
                            @RequestParam(value = "deptId", defaultValue = "") String deptId,
                            @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                            @RequestParam(value ="settleTimeStart",defaultValue="") String settleTimeStart,
                            @RequestParam(value ="settleTimeEnd",defaultValue="") String settleTimeEnd) {


        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealType",dealType);
        params.put("dealNo",dealNo);
        params.put("dealName",dealName);
        params.put("contractId",contractId);
        if (StringUtils.isNotEmpty(statusList)) {
            //结算状态
            params.put("statusList", statusList.split(","));
        }
        params.put("dealReportNo",dealReportNo);
        params.put("deptId",deptId);
        params.put("dealIncome",dealIncome);
        params.put("settleTimeStart",settleTimeStart);
        params.put("settleTimeEnd",settleTimeEnd);
        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        //2、执行查询
        List<DealSettleCompreVo> dealSettleCompreVos = comprehensiveService.selectCompreList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(dealSettleCompreVos);
        return AppMessage.success(dataTable, "查询结算综合信息成功");
    }


    /**
     * 综合查询结算--导出
     * @return
     */
    @RequestMapping(value = "/settlement/export",method = RequestMethod.GET)
    public AppMessage export( HttpServletResponse response,
                             @RequestParam(value = "dealType", defaultValue = "") String dealType,
                             @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                             @RequestParam(value = "dealName", defaultValue = "") String dealName,
                             @RequestParam(value = "contractId", defaultValue = "") String contractId,
                             @RequestParam(value = "statusList", defaultValue = "") String statusList,
                             @RequestParam(value = "dealReportNo", defaultValue = "") String dealReportNo,
                             @RequestParam(value = "deptId", defaultValue = "") String deptId,
                             @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                             @RequestParam(value ="settleTimeStart",defaultValue="") String settleTimeStart,
                             @RequestParam(value ="settleTimeEnd",defaultValue="") String settleTimeEnd
    ){

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealType",dealType);
        params.put("dealNo",dealNo);
        params.put("dealName",dealName);
        params.put("contractId",contractId);
        if (StringUtils.isNotEmpty(statusList)) {
            //结算状态
            params.put("statusList", statusList.split(","));
        }
        params.put("dealReportNo",dealReportNo);
        params.put("deptId",deptId);
        params.put("dealIncome",dealIncome);
        params.put("settleTimeStart",settleTimeStart);
        params.put("settleTimeEnd",settleTimeEnd);
        List<DealSettleCompreVo> dealSettleCompreVos = comprehensiveService.selectCompreList(params);
        for (DealSettleCompreVo dealSettleCompreVo:  dealSettleCompreVos) {
            dealSettleCompreVo.setDealType(DealTypeEnum.getEnumByKey(dealSettleCompreVo.getDealType()));
            dealSettleCompreVo.setSettleStatus(SettlementStatusEnum.getEnumByKey(dealSettleCompreVo.getSettleStatus()));
        }
        ExcelUtil<DealSettleCompreVo> util = new ExcelUtil<DealSettleCompreVo>(DealSettleCompreVo.class);
        return util.exportExcelBrowser(response, dealSettleCompreVos, "结算综合管理信息表");
    }


    @RequestMapping(value = "/deal/{id}", method = RequestMethod.PUT)
    public AppMessage update( @PathVariable String id,
                             @RequestBody DealParamVo paramVo) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        StringBuffer dealBefore=new StringBuffer();
        StringBuffer dealAfter=new StringBuffer();
        try{
            BizDealDto bizDealDto = dealService.selectByKey(id);
            dealBefore.append(JSON.marshal(bizDealDto));

            DealVo dealVo = paramVo.getDealVo();
            List<AttachVo> attachVos = paramVo.getAttachVos();
            String type = paramVo.getType();
            //准备
            BizDealDto dealDto = new BizDealDto();
            List<BizAttachDto> attachDtos = new ArrayList<>();
            List<BizDealAttachDto> dealAttachDtos = new ArrayList<>();
            dealDto.setDealId(id);
            dealDto.setDealNo(dealVo.getDealNo());
            dealDto.setUserId(dealVo.getUserId());
            dealDto.setRelatedcontract(dealVo.getRelatedcontract());
            dealDto.setFrameDeal(dealVo.getFrameDeal().toString());
            dealDto.setDealName(dealVo.getDealName());
            dealDto.setDealValue(dealVo.getDealValue());
            dealDto.setCategoryId(dealVo.getCategoryId());
            dealDto.setDealSignTime(dealVo.getDealSignTime());
            dealDto.setDeptId(dealVo.getDeptId());
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
       //     dealDto.setDealStatus(DealWebTypeEnum.getEnumByKey(type));
            dealDto.setDealType(dealVo.getDealType());
            dealDto.setPaymentType(dealVo.getPaymentType());
            dealDto.setPaymentTeq(dealVo.getPaymentTeq());
            dealDto.setDealValueAfter(dealVo.getDealValueAfter());
            dealDto.setDealValueBefore(dealVo.getDealValueBefore());
            dealDto.setHaveTax(dealVo.getHaveTax());
            dealDto.setTaxRate(dealVo.getTaxRate());
        //    dealDto.setDealNo(dealVo.getDealNo());
            //2 附件
            for (AttachVo attachVo : attachVos) {
                //String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");

                if (attachService.isDoubleFile(attachVos)) {
                    return AppMessage.errorObjId(id, "不能上传重复的文件！");
                }

                String attachId = attachVo.getAttachId();

                BizAttachDto attachDto = new BizAttachDto();
                attachDto.setAttachId(attachId);
                attachDto.setOwnerId(dealDto.getDealId());
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
            boolean updateChain = dealService.updateChain(dealDto, attachDtos, dealAttachDtos, type, userId,"");
            if (updateChain) {
             //   bizDealDto.setDealValue(bizDealDto.getDealValueAfter());
                bizProjectManager.bizDealChanged(dealDto);
                //新增履行合同统计
                bizDealStatisticsService.addDealStatistics(dealDto);
                return AppMessage.success(id, "更新合同成功");
            }
            return AppMessage.errorObjId(id, "更新合同失败");

        }finally {
            BizDealDto bizDealDto = dealService.selectByKey(id);
            dealAfter.append(JSON.marshal(bizDealDto));
            StringBuffer content=new StringBuffer();
            content.append("管理员执行了修改合同操作，修改前的合同信息为[");
            content.append(dealBefore) ;
            content.append("]");
            content.append("修改后的合同信息为[");
            content.append(dealAfter) ;
            content.append("]");
            SysLogDto log=setSysLog(userId,
                    LogModule.COMPREHENSIVE.toString(),
                    id,LogType.OPERATION.toString(),
                    content.toString());
            logService.save(log);
        }

    }

    @RequestMapping(value = "/deal/{id}", method = RequestMethod.DELETE)
    public AppMessage delete( @PathVariable String id) {

        String userId = ServletUtils.getSessionUserId();

        if(StringUtils.isEmpty(id)){
            throw new AppException("id不能为空");
        }
        //不能删除已经存在结算的合同上
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", id);
        List<BizSettlementDto> settlementDtos = settlementService.selectList(param);
        if (null != settlementDtos && settlementDtos.size() > 0) {
            return AppMessage.errorObjId(id, "删除合同失败,该合同已存在结算");
        }

        Map<String,Object> param1 =new HashMap<>();
        param1.put("dealId",id);
        List<String> stepIds=new ArrayList<>();
        List<String> manIds=new ArrayList<>();
        List<String> attachIds=new ArrayList<>();
        List<String> dealAttachIds=new ArrayList<>();
        List<String> noticeIds=new ArrayList<>();

        Map<String,Object> resList=new HashMap<>();

        //查询要删除的步骤
        List<BizCheckStepDto> checkStepDtos = checkStepService.selectList(param1);

        for (BizCheckStepDto checkStepDto: checkStepDtos ) {
            String stepId = checkStepDto.getStepId();
            stepIds.add(stepId);

            Map<String,Object> param2 =new HashMap<>();
            param2.put("stepId", stepId);

            //查询要删除的人员
            List<BizCheckManDto> bizCheckManDtos = checkManService.selectList(param2);
            for (BizCheckManDto manDto:bizCheckManDtos) {
                manIds.add(manDto.getManId());
            }
        }

        //查询要删除的附件
        Map<String,Object> param3 =new HashMap<>();
        param3.put("dealId",id);
        List<BizAttachDto> bizAttachDtos = attachService.selectListByDealId(param3);
        for (BizAttachDto attachDto:bizAttachDtos) {
            attachIds.add(attachDto.getAttachId());
        }
        //查询要删除的合同附件
        Map<String,Object> param4 =new HashMap<>();
        param4.put("dealId",id);
        List<BizDealAttachDto> bizDealAttachDtos = dealAttachService.selectList(param4);
        for (BizDealAttachDto dealAttachDto:bizDealAttachDtos) {
            dealAttachIds.add(dealAttachDto.getId());
        }
        //查询要删除的提示消息
        Map<String,Object> param5 =new HashMap<>();
        param5.put("noticeObjId",id);
        List<BizCheckNoticeDto> checkNoticeDtos=checkNoticeService.selectList(param5);
        for (BizCheckNoticeDto checkNoticeDto:checkNoticeDtos) {
            noticeIds.add(checkNoticeDto.getNoticeId());
        }
        resList.put("stepIds",stepIds);
        resList.put("manIds",manIds);
        resList.put("attachIds",attachIds);
        resList.put("dealAttachIds",dealAttachIds);
        resList.put("noticeIds",noticeIds);
        resList.put("dealId",id);

        StringBuffer dealBefore=new StringBuffer();
        try {
            BizDealDto bizDealDto = dealService.selectByKey(id);
            dealBefore.append(JSON.marshal(bizDealDto));

            comprehensiveService.deleteDeal(resList);

            //删除履行合同统计
            bizDealStatisticsService.deleteDealStatistics(bizDealDto);
            return AppMessage.success(id,"删除合同成功");
        }catch (Exception e){
            return AppMessage.errorObjId(id, "删除合同失败");
        }finally {
            StringBuffer content=new StringBuffer();
            content.append("管理员执行了删除合同操作，删除前的合同信息为[");
            content.append(dealBefore) ;
            content.append("]");
            SysLogDto log=setSysLog(userId,
                    LogModule.COMPREHENSIVE.toString(),
                    id,LogType.OPERATION.toString(),
                    content.toString());
            logService.save(log);
        }

    }

    @RequestMapping(value = "/settlement/{id}", method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id,
                             @RequestBody SettleParamVo settleParamVo)throws Exception {
        String userId=ServletUtils.getSessionUserId();
        StringBuffer settleBefore=new StringBuffer();
        StringBuffer settleAfter=new StringBuffer();
        try {
            BizSettlementDto bizSettlementDto = settlementService.selectByKey(id);
            userId=bizSettlementDto.getUserId();
            Map<String,Object> param=new HashMap<>();
            param.put("settleId",id);
            List<BizSettleDetailDto> settleDetailDtos = settleDetailService.selectList(param);
            settleBefore.append(JSON.marshal(bizSettlementDto));
            settleBefore.append(JSON.marshal(settleDetailDtos));
            //准备
            SettlementVo settlementVo = settleParamVo.getSettlementVo();
            List<AttachVo> attachVos = settleParamVo.getAttachVos();
            List<SettleDetailVo> settleDetailVos = settleParamVo.getSettleDetailVos();
            String type = settleParamVo.getType();
            List<BizAttachDto> attachDtos=new ArrayList<>();
            List<BizSettlementAttachDto> settlementAttachDtos=new ArrayList<>();
            //获取指定人
            BizDealDto bizDealDto = dealService.selectByKey(settlementVo.getDealId());

            //1 修改结算
            BizSettlementDto settlementDto = new BizSettlementDto();
            settlementDto.setSettleId(id);
          //  settlementDto.setSettleStatus(SettleWebTypeEnum.getEnumByKey(type));
          //  settlementDto.setDeptId(sysUserDto.getDeptId());
           // settlementDto.setUserId(userId);
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
                //新增履行合同统计
                bizDealStatisticsService.addDealStatistics(bizDealDto);
                return AppMessage.success(id, "更新合同结算成功");
            }
            return AppMessage.errorObjId(id, "更新结算失败");

        }finally {
            BizSettlementDto bizSettlementDto = settlementService.selectByKey(id);
            Map<String,Object> param=new HashMap<>();
            param.put("settleId",id);
            List<BizSettleDetailDto> settleDetailDtos = settleDetailService.selectList(param);
            settleAfter.append(JSON.marshal(bizSettlementDto));
            settleAfter.append(JSON.marshal(settleDetailDtos));

            StringBuffer content=new StringBuffer();
            content.append("管理员执行了修改结算操作，修改前的结算信息为[");
            content.append(settleBefore) ;
            content.append("]");
            content.append("修改后的结算信息为[");
            content.append(settleAfter) ;
            content.append("]");
            SysLogDto log=setSysLog(userId,
                    LogModule.COMPREHENSIVE.toString(),
                    id,LogType.OPERATION.toString(),
                    content.toString());
            logService.save(log);
        }
    }

    @RequestMapping(value = "/settlement/{id}", method = RequestMethod.DELETE)
    public AppMessage deleteSettle( @PathVariable String id) {

        String userId = ServletUtils.getSessionUserId();
        if(StringUtils.isEmpty(id)){
            throw new AppException("id不能为空");
        }

        Map<String,Object> param1 =new HashMap<>();
        param1.put("settleId",id);
        List<String> stepIds=new ArrayList<>();
        List<String> manIds=new ArrayList<>();
        List<String> attachIds=new ArrayList<>();
        List<String> settleAttachIds=new ArrayList<>();
        List<String> noticeIds=new ArrayList<>();
        List<String> detailIds=new ArrayList<>();

        Map<String,Object> resList=new HashMap<>();

        //查询要删除的步骤
        List<BizCheckStepDto> checkStepDtos = checkStepService.selectList(param1);

        for (BizCheckStepDto checkStepDto: checkStepDtos ) {
            String stepId = checkStepDto.getStepId();
            stepIds.add(stepId);

            Map<String,Object> param2 =new HashMap<>();
            param2.put("stepId", stepId);

            //查询要删除的人员
            List<BizCheckManDto> bizCheckManDtos = checkManService.selectList(param2);
            for (BizCheckManDto manDto:bizCheckManDtos) {
                manIds.add(manDto.getManId());
            }
        }

        //查询要删除的附件
        Map<String,Object> param3 =new HashMap<>();
        param3.put("settleId",id);
        List<BizAttachDto> bizAttachDtos = attachService.selectListBySettleId(param3);
        for (BizAttachDto attachDto:bizAttachDtos) {
            attachIds.add(attachDto.getAttachId());
        }
        //查询要删除的合同附件
        Map<String,Object> param4 =new HashMap<>();
        param4.put("settleId",id);
        List<BizSettlementAttachDto> settlementAttachDtos = settlementAttachService.selectList(param4);
        for (BizSettlementAttachDto settlementAttachDto:settlementAttachDtos) {
            settleAttachIds.add(settlementAttachDto.getId());
        }
        //查询要删除的提示消息
        Map<String,Object> param5 =new HashMap<>();
        param5.put("noticeObjId",id);
        List<BizCheckNoticeDto> checkNoticeDtos=checkNoticeService.selectList(param5);
        for (BizCheckNoticeDto checkNoticeDto:checkNoticeDtos) {
            noticeIds.add(checkNoticeDto.getNoticeId());
        }

        Map<String,Object> param6=new HashMap<>();
        param6.put("settleId",id);
        List<BizSettleDetailDto> bizSettleDetailDtos = settleDetailService.selectList(param6);
        for (BizSettleDetailDto settleDetailDto:bizSettleDetailDtos) {
            detailIds.add(settleDetailDto.getDetailId());
        }

        resList.put("stepIds",stepIds);
        resList.put("manIds",manIds);
        resList.put("attachIds",attachIds);
        resList.put("settleAttachIds",settleAttachIds);
        resList.put("noticeIds",noticeIds);
        resList.put("detailIds",detailIds);
        resList.put("settleId",id);

        StringBuffer settleBefore=new StringBuffer();
        try {
            BizSettlementDto bizSettlementDto = settlementService.selectByKey(id);
            settleBefore.append(JSON.marshal(bizSettlementDto));
            settleBefore.append(JSON.marshal(bizSettleDetailDtos));
            comprehensiveService.deleteSettle(resList);

            //新增履行合同统计
            bizDealStatisticsService.addDealStatistics(dealService.selectByKey(bizSettlementDto.getDealId()));
            return AppMessage.success(id,"删除结算成功");
        }catch (Exception e){
            return AppMessage.errorObjId(id, "删除结算失败");
        }finally {
            StringBuffer content=new StringBuffer();
            content.append("管理员执行了删除结算操作，删除前的结算信息为[");
            content.append(settleBefore) ;
            content.append("]");
            SysLogDto log=setSysLog(userId,
                    LogModule.COMPREHENSIVE.toString(),
                    id,LogType.OPERATION.toString(),
                    content.toString());
            logService.save(log);
        }

    }

    /**
     *
     * @param args userId logModule logObject logType logContent
     * @return
     */
    private SysLogDto setSysLog(String...args){
        SysLogDto log=new SysLogDto();
        log.setLogId(StringUtils.getUuid32());
        log.setUserId(args[0]);
        log.setLogTime(new Date());
        log.setLogModule(args[1]);
        log.setLogObject(args[2]);
        log.setLogType(args[3]);
        log.setLogContent(args[4]);

        return log;
    }




}

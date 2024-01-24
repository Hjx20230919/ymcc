package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.mapper.prodsys.BizDealPsDtoMapper;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.DiyStepParamVo;
import cn.com.cnpc.cpoa.vo.DiyStepVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <copy from ActivitiService>
 * PS Deal approval process
 *
 * @author anonymous
 * @create 23/02/2020 23:29
 * @since 1.0.0
 */
@Service
public class BizDealPsApprovalService {

    @Autowired
    private CheckManService checkManService;

    @Autowired
    private CheckStepService checkStepService;

    @Autowired
    private DeptService deptService;

//    @Autowired
//    SettlementService settlementService;

    @Autowired
    private BizDealPsDtoMapper bizDealDtoMapper;
//    @Autowired
//    BizSettlementDtoMapper bizSettlementDtoMapper;

    @Autowired
    BizDealPsService dealService;

    @Autowired
    BizDealPsCheckStepService bizCheckStepService;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    WxMessageService wxMessageService;

    @Autowired
    CheckNoticeService checkNoticeService;

    @Autowired
    UserService userService;

    @Autowired
    ConstractorAuditService constractorAuditService;
    @Autowired
    ProjectAuditService projectAuditService;

    @Value("${templateId.passTemplateId}")
    private String passTemplateId;

    @Value("${templateId.refuseTemplateId}")
    private String refuseTemplateId;

    @Value("${templateId.pendTemplateId}")
    private String pendTemplateId;


    /**
     * 添加基础审核流程
     * 1 用户所在部门负责人
     * 2 企管部审核人
     */
    @Transactional
    public void initBaseActiviti(String userId, String dealId, String dealType, String deptId, String stepNo, String flag) throws Exception {
        try {
            Date nowDate = DateUtils.getNowDate();
            Map<String, Object> param = new HashMap<>();
            param.put("userId", userId);
            //1 获取用户所在部门负责人
            SysDeptDto sysDeptDto = deptService.selectByKey(deptId);
            String deptManager = sysDeptDto.getDeptManager();


            //2 获取企管部门负责人
//            Map<String,Object> param2=new HashMap<>();
//            param2.put("isEmp",1);
//            SysDeptDto sysDeptDto2 = deptService.selectList(param2).get(0);
//            String deptManager2 = sysDeptDto2.getDeptManager();
            //查询指定分发用户
            Map<String, Object> param2 = new HashMap<>();
            param2.put("cfgCode", Constants.DEAL_PS_SPLIT_USER);
            List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(param2);
            BizSysConfigDto bizSysConfigDto = bizSysConfigDtos.get(0);
            String deptManager2 = bizSysConfigDto.getCfgValue();

            if (Integer.valueOf(stepNo) == 0 || StringUtils.isNotEmpty(flag)) {
                //3 新建发起人
                BizCheckStepDto checkStepDto = new BizCheckStepDto();
                String step = StringUtils.getUuid32();

                checkStepDto.setStepId(step);
                if (StringUtils.isNotEmpty(flag)) {
                    int st = Integer.valueOf(stepNo) + 1;
                    checkStepDto.setStepNo(st);
                    checkStepDto.setStepName("变更");
                    stepNo = st + "";
                } else {
                    checkStepDto.setStepNo(0);
                    checkStepDto.setStepName("发起");
                }
                checkStepDto.setStepCreateAt(nowDate);
                checkStepDto.setStepCreateUser(userId);
                checkStepDto.setStepState(CheckStepStateEnum.PASS.getKey());
                checkStepDto.setStepBeginAt(nowDate);
                checkStepDto.setStepEndAt(nowDate);
                checkStepDto.setCheckObjId(dealId);
                checkStepDto.setCheckObjType(dealType);
                checkStepDto.setCheckDeptId(userService.selectByKey(userId).getDeptId());
                checkStepService.save(checkStepDto);

                //4 新增发起人
                BizCheckManDto checkManDto = new BizCheckManDto();
                checkManDto.setManId(StringUtils.getUuid32());
                checkManDto.setStepId(step);
                checkManDto.setUserId(userId);
                checkManDto.setCheckState(CheckManStateEnum.PENDED.getKey());
                checkManDto.setCheckTime(DateUtils.getNowDate());
                checkManDto.setCheckNode("通过");
                checkManDto.setCheckResult(CheckManResultEnum.PASS.getKey());
                checkManService.save(checkManDto);

            }

            //如果部门负责人不存在，则跳过此步骤
            if (StringUtils.isNotEmpty(deptManager)) {
                //5 新建第一步审核步骤
                BizCheckStepDto checkStepDto1 = new BizCheckStepDto();
                String step1 = StringUtils.getUuid32();

                checkStepDto1.setStepId(step1);
                checkStepDto1.setStepNo(Integer.valueOf(stepNo) + 1);
                checkStepDto1.setStepName("部门审核");
                checkStepDto1.setStepCreateAt(nowDate);
                checkStepDto1.setStepCreateUser(userId);
                //           checkStepDto.setStepState("初始");
                checkStepDto1.setStepBeginAt(nowDate);
                checkStepDto1.setCheckObjId(dealId);
                checkStepDto1.setCheckObjType(dealType);
                checkStepDto1.setCheckDeptId(userService.selectByKey(deptManager).getDeptId());

                checkStepService.save(checkStepDto1);

                //6 新增第一步处理人

                BizCheckManDto checkManDto1 = new BizCheckManDto();
                checkManDto1.setManId(StringUtils.getUuid32());
                checkManDto1.setStepId(step1);
                checkManDto1.setUserId(deptManager);
                checkManDto1.setCheckState(CheckManStateEnum.PENDING.getKey());
//            checkManDto1.setCheckTime(DateUtils.getNowDate());
                checkManDto1.setCheckNode(null);
                checkManDto1.setCheckResult(null);
                checkManService.save(checkManDto1);

                //待审批通知
                if (CheckTypeEnum.DEALPS.getKey().equals(dealType)) {
                    SysUserDto userDto = userService.selectByKey(deptManager);
                    String pendingMessage = wxMessageService.getPendingMessage(dealType, dealId, userDto.getWxopenid(), nowDate, "提前开工审批");
                    checkNoticeService.saveNotice(dealId, dealType, CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
                }/* else {
                    SysUserDto userDto = userService.selectByKey(deptManager);
                    String pendingMessage = wxMessageService.getPendingMessage(dealType, dealId, userDto.getWxopenid(), nowDate, "合同-结算审批");
                    checkNoticeService.saveNotice(dealId, dealType, CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
                }*/
            }


            // 7 新建第二步审核
            BizCheckStepDto checkStepDto2 = new BizCheckStepDto();
            String step2 = StringUtils.getUuid32();

            checkStepDto2.setStepId(step2);
            checkStepDto2.setStepNo(Integer.valueOf(stepNo) + 2);
            checkStepDto2.setStepName("企管审核");
            checkStepDto2.setStepCreateAt(nowDate);
            checkStepDto2.setStepCreateUser(userId);
//            checkStepDto2.setStepState("初始");
//            checkStepDto2.setStepBeginAt(DateUtils.getNowDate());
            checkStepDto2.setCheckObjId(dealId);
            checkStepDto2.setCheckObjType(dealType);
            checkStepDto2.setCheckDeptId(userService.selectByKey(deptManager2).getDeptId());
            checkStepDto2.setStepSplit(1);
            checkStepService.save(checkStepDto2);

            // 8 新建第二步审核人
            BizCheckManDto checkManDto2 = new BizCheckManDto();
            checkManDto2.setManId(StringUtils.getUuid32());
            checkManDto2.setStepId(step2);
            checkManDto2.setUserId(deptManager2);
            checkManDto2.setCheckState(CheckManStateEnum.PENDING.getKey());
            //           checkManDto2.setCheckTime(DateUtils.getNowDate());
            checkManDto2.setCheckNode(null);
            checkManDto2.setCheckResult(null);
            checkManService.save(checkManDto2);
        } catch (Exception e) {
            throw new AppException("新建初始化流程出错" + e.getMessage());
        }
    }

    @Transactional
    public boolean buildDiyActiviti(AuditVo auditVo, String userId) {
        List<DiyStepParamVo> diyStepParamVos = auditVo.getDiyStepParamVo();
        String stepNoStr = null;
        if (auditVo.getObjectType().equals(CheckTypeEnum.DEALPS.getKey())) {
            stepNoStr = bizCheckStepService.selectMaxStepNo(auditVo.getDealId());
        } /*else {
            stepNoStr = bizCheckStepService.selectMaxStepNo(auditVo.getSettleId());
        }*/
        for (int j = 0; j < diyStepParamVos.size(); j++) {
            DiyStepParamVo diyStepParamVo = diyStepParamVos.get(j);
            List<DiyStepVo> diyStepVos = diyStepParamVo.getDiyStepVos();
            Integer stepNo = Integer.valueOf(stepNoStr) + (j + 1);
            for (int i = 0; i < diyStepVos.size(); i++) {
                Date nowDate = DateUtils.getNowDate();
                DiyStepVo diyStepVo = diyStepVos.get(i);

                //1 新建审核步骤
                BizCheckStepDto checkStepDto = new BizCheckStepDto();
                String stepId = StringUtils.getUuid32();
                checkStepDto.setStepId(stepId);
                checkStepDto.setStepNo(stepNo);
                checkStepDto.setStepName(diyStepParamVo.getStepName());
                checkStepDto.setStepCreateAt(nowDate);
                checkStepDto.setStepCreateUser(userId);
                checkStepDto.setCheckDeptId(userService.selectByKey(diyStepVo.getCheckMan()).getDeptId());
//            checkStepDto.setStepState("初始");
                //第一个步骤应该设置开始时间
                if (j == 0) {
                    checkStepDto.setStepBeginAt(nowDate);
                }
                checkStepDto.setCheckObjId(diyStepParamVo.getCheckObjId());
                checkStepDto.setCheckObjType(diyStepParamVo.getCheckObjType());
                int save = checkStepService.save(checkStepDto);
                if (save != 1) {
                    throw new AppException("保存新建审核步骤出错");
                }

                //2 新增处理人
                BizCheckManDto checkManDto = new BizCheckManDto();
                checkManDto.setManId(StringUtils.getUuid32());
                checkManDto.setStepId(stepId);
                checkManDto.setUserId(diyStepVo.getCheckMan());
                checkManDto.setCheckState(CheckManStateEnum.PENDING.getKey());
                //           checkManDto.setCheckTime(nowDate);
                checkManDto.setCheckNode(null);
                checkManDto.setCheckResult(null);
                int save2 = checkManService.save(checkManDto);
                if (save2 != 1) {
                    throw new AppException("保存处理人出错");
                }
            }
        }
        //提交审核
        passActiviti(auditVo);
        return true;
    }

    /**
     * 推进审核流程
     */
    @Transactional
    public boolean passActiviti(AuditVo auditVo) {
        try {
            String auditStatus = auditVo.getAuditStatus();
            String stepId = auditVo.getStepId();
            String chekNode = auditVo.getChekNode();
            String dealId = auditVo.getDealId();
            String settleId = auditVo.getSettleId();
            String manId = auditVo.getManId();
            boolean isDeal = CheckTypeEnum.DEALPS.getKey().equals(auditVo.getObjectType());

            //1 更新审核步骤。若为最后一步则更新步骤表
            BizCheckStepDto checkStepDto = new BizCheckStepDto();
            checkStepDto.setStepId(stepId);
            checkStepDto.setStepState(CheckManResultEnum.PASS.getKey());
            Date nowDate = DateUtils.getNowDate();
            checkStepDto.setStepEndAt(nowDate);
            checkStepService.updateNotNull(checkStepDto);

            // 2更新处理人
            BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
            String checkResult = bizCheckManDto.getCheckResult();
            if (CheckStepStateEnum.REFUSE.getKey().equals(checkResult)) {
                chekNode = "【再审通过】" + chekNode;
                chekNode = bizCheckManDto.getCheckNode() + "|" + chekNode;
            }
            BizCheckManDto checkManDto = new BizCheckManDto();
            checkManDto.setManId(manId);
            checkManDto.setCheckState(CheckManStateEnum.PENDED.getKey());
            checkManDto.setCheckTime(nowDate);
            checkManDto.setCheckNode(chekNode);
            checkManDto.setCheckResult(auditStatus);
            checkManService.updateNotNull(checkManDto);

            //3判断是否是 合同所有步骤是否审核完、
            List<BizCheckStepDto> lastStep = new ArrayList<>();
            boolean isLastStep1 = false;
            Map<String, Object> param = new HashMap<>();
            if (isDeal) {
                param.put("objId", dealId);
            }/* else {
                param.put("objId", settleId);
            }*/
            param.put("stepId", stepId);
            lastStep = checkStepService.getLastStepByDealId(param);
            if (null != lastStep && lastStep.size() > 0) {
                BizCheckStepDto checkStepDto1 = lastStep.get(0);
                if (auditVo.getStepNo().equals("" + checkStepDto1.getStepNo())) {
                    isLastStep1 = true;
                }
            }

            //4 查询当前 审核同步骤下  还有没待审核
            Map<String, Object> params = new HashMap<>();
            if (isDeal) {
                params.put("objId", dealId);
            }/* else {
                params.put("objId", settleId);
            }*/
            params.put("stepNo", auditVo.getStepNo());

            List<BizCheckStepDto> list = checkStepService.selectLastStepByObjectId(params);
            if (null == list || list.size() == 0) { //当前审核最后一步。
                //   if(Integer.valueOf(auditVo.getStepNo())>2){
                if (isDeal) {
                    if (isLastStep1) {
                        //如果是最后一步，且都是审核完成了。则更新合同状态为 progressAuditing。
                        BizDealPsDto dealDto = new BizDealPsDto();
                        dealDto.setDealId(dealId);
                        BizDealPsDto bizDealDto = dealService.selectByKey(dealId);
                        String dealStatus = bizDealDto.getDealStatus();
                        if (DealStatusEnum.CHANGEAUDITING.getKey().equals(dealStatus)) {
                            dealDto.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
                            dealDto.setDealValue(bizDealDto.getDealValueAfter());
                        } else {
                            dealDto.setDealSignTime(DateUtils.getNowDate());
                            dealDto.setDealStatus(DealStatusEnum.PROGRESSAUDITING.getKey());
                        }
                        dealService.updateNotNull(dealDto);

                        //保存审批通知
                        //通过通知-通知承办人
                        String userId = dealService.selectByKey(dealId).getUserId();
                        SysUserDto userDto = userService.selectByKey(userId);
                        String passMessage = wxMessageService.getPassMessage(auditVo.getObjectType(), dealId, userDto.getWxopenid(), bizCheckManDto.getUserId(), chekNode, nowDate, "提前开工审批");
                        checkNoticeService.saveNotice(dealId, auditVo.getObjectType(), CheckNoticeTypeEnum.PASS.getKey(), userDto.getWxopenid(), passTemplateId, passMessage);
                    } else {
                        //如果不是最后一步，且都是审核完成了。则更新下次合同的开始时间
                        Map<String, Object> par = new HashMap<>();
                        par.put("dealId", dealId);
                        par.put("stepNo", Integer.valueOf(auditVo.getStepNo()) + 1);
                        List<BizCheckStepDto> bizCheckStepDtos = checkStepService.selectList(par);
                        for (BizCheckStepDto bizCheckStepDto : bizCheckStepDtos) {
                            bizCheckStepDto.setStepBeginAt(nowDate);
                            checkStepService.updateNotNull(bizCheckStepDto);
                        }

                        //保存审批通知
                        //待审批通知--通知待审核人
                        Map<String, Object> param1 = new HashMap<>();
                        param1.put("dealId", dealId);
                        List<BizDealPsDto> bizDealDtos = bizDealDtoMapper.selectUserNameList(param1);
                        for (BizDealPsDto bizDealDto : bizDealDtos) {
                            String userId = bizDealDto.getUserId();
                            SysUserDto userDto = userService.selectByKey(userId);
                            String pendingMessage = wxMessageService.getPendingMessage(auditVo.getObjectType(), dealId, userDto.getWxopenid(), nowDate, "提前开工审批");
                            checkNoticeService.saveNotice(dealId, auditVo.getObjectType(), CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
                        }
                    }
                } /*else {
                    if (isLastStep1) {
                        //所有步骤审核完 需要更新结算 审核状态 为完成
                        BizSettlementDto settlementDto = new BizSettlementDto();
                        settlementDto.setSettleId(settleId);
                        settlementDto.setSettleStatus(SettlementStatusEnum.DOWN.getKey());
                        settlementDto.setDownTime(DateUtils.getNowDate());
                        settlementService.updateNotNull(settlementDto);

                        //更新履行金额
                        BizSettlementDto settlementDto1 = settlementService.selectByKey(settleId);
                        String dealId1 = settlementDto1.getDealId();
                        BizDealDto dealDto = dealService.selectByKey(dealId1);

                        dealDto.setDealSettlement(BigDecimalUtils.add(null != dealDto.getDealSettlement() ? dealDto.getDealSettlement() : 0.00, settlementDto1.getSettleAmount()));
                        dealService.updateNotNull(dealDto);

                        //保存审批通知
                        //通过通知-通知承办人
                        String userId = dealDto.getUserId();
                        SysUserDto userDto = userService.selectByKey(userId);
                        String passMessage = wxMessageService.getPassMessage(auditVo.getObjectType(), settleId, userDto.getWxopenid(), bizCheckManDto.getUserId(), chekNode, nowDate, "合同-结算审批");
                        checkNoticeService.saveNotice(settleId, auditVo.getObjectType(), CheckNoticeTypeEnum.PASS.getKey(), userDto.getWxopenid(), passTemplateId, passMessage);

                    } else {
                        //结算则更新下一步的开始时间
                        Map<String, Object> par = new HashMap<>();
                        par.put("settleId", auditVo.getSettleId());
                        par.put("stepNo", Integer.valueOf(auditVo.getStepNo()) + 1);
                        List<BizCheckStepDto> bizCheckStepDtos = checkStepService.selectList(par);
                        for (BizCheckStepDto bizCheckStepDto : bizCheckStepDtos) {
                            bizCheckStepDto.setStepBeginAt(nowDate);
                            checkStepService.updateNotNull(bizCheckStepDto);
                        }

                        //保存审批通知
                        //待审批通知--通知待审核人
                        Map<String, Object> param1 = new HashMap<>();
                        param1.put("settleId", settleId);
                        List<BizSettlementDto> bizSettlementDtos = bizSettlementDtoMapper.selectUserNameList(param1);
                        for (BizSettlementDto bizSettlementDto : bizSettlementDtos) {
                            String userId = bizSettlementDto.getUserId();
                            SysUserDto userDto = userService.selectByKey(userId);
                            String pendingMessage = wxMessageService.getPendingMessage(auditVo.getObjectType(), settleId, userDto.getWxopenid(), nowDate, "合同-结算审批");
                            checkNoticeService.saveNotice(settleId, auditVo.getObjectType(), CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
                        }
                    }
                }*/

            }
        } catch (Exception e) {
            throw new AppException("推进审核流程" + e.getMessage());
        }
        return true;
    }

    /**
     * 回退审核流程
     */
    @Transactional
    public void backActiviti(AuditVo auditVo) {
        try {

            String auditStatus = auditVo.getAuditStatus();
            String stepId = auditVo.getStepId();
            String chekNode = auditVo.getChekNode();
            String dealId = auditVo.getDealId();
//            String settleId = auditVo.getSettleId();
            String manId = auditVo.getManId();

            if (StringUtils.isNotEmpty(dealId)) {
                //防止出现 同时退回的情况
                BizDealPsDto dealDto = dealService.selectByKey(dealId);
                String dealStatus = dealDto.getDealStatus();
                if (!DealStatusEnum.BUILDAUDITING.getKey().equals(dealStatus) && !DealStatusEnum.CHANGEAUDITING.getKey().equals(dealStatus)) {
                    throw new AppException("该提前开工已有退回记录，暂时无法发起退回");
                }
            }/* else {
                BizSettlementDto bizSettlementDto = settlementService.selectByKey(settleId);
                String settleStatus = bizSettlementDto.getSettleStatus();
                if (!SettlementStatusEnum.BUILDAUDITING.getKey().equals(settleStatus)) {
                    throw new AppException("该结算已有退回记录，暂时无法发起退回");
                }
            }*/

            // 0 更新当前步骤 处理人
            //更新审核步骤
            BizCheckStepDto checkStepDto = new BizCheckStepDto();
            checkStepDto.setStepId(stepId);
            checkStepDto.setStepState(CheckManResultEnum.BACK.getKey());
            //1 查询系统中当前步骤是否还有未处理 有则不设置
            checkStepDto.setStepEndAt(DateUtils.getNowDate());
            checkStepService.updateNotNull(checkStepDto);

            BizCheckManDto bizCheckManDto2 = checkManService.selectByKey(manId);
            String checkResult = bizCheckManDto2.getCheckResult();
            if (CheckStepStateEnum.REFUSE.getKey().equals(checkResult)) {
                chekNode = "【再审回退】" + chekNode;
                chekNode = bizCheckManDto2.getCheckNode() + "|" + chekNode;
            }

            // 更新处理人
            BizCheckManDto checkManDto = new BizCheckManDto();
            checkManDto.setManId(manId);
            checkManDto.setCheckState(CheckManStateEnum.PENDED.getKey());
            checkManDto.setCheckTime(DateUtils.getNowDate());
            checkManDto.setCheckNode(chekNode);
            checkManDto.setCheckResult(auditStatus);
            checkManService.updateNotNull(checkManDto);

//            //1 查询该步骤下所有待审核流程
//
//            List<CheckStepPo> checkStepPos = diyCheckStepService.selectRemainList(dealId, settleId);
//            //2 删除未审核流程
//            for (CheckStepPo checkStepPo:checkStepPos) {
//                checkStepService.delete(checkStepPo.getStepId());
//                checkManService.delete(checkStepPo.getManId());
//            }
            //3 将合同回退给发起人。
            if (StringUtils.isNotEmpty(dealId)) {
                BizDealPsDto dealDto = new BizDealPsDto();
                dealDto.setDealId(dealId);

                String dealStaus = dealService.selectByKey(dealId).getDealStatus();
                if (DealStatusEnum.BUILDAUDITING.getKey().equals(dealStaus)) {
                    dealDto.setDealStatus(DealStatusEnum.BACK.getKey());
                } else {
                    //变更退回
                    dealDto.setDealStatus(DealStatusEnum.CHANGEBACK.getKey());
                }

                dealService.updateNotNull(dealDto);

                //保存审批通知
                //拒绝通知-通知承办人
                String userId = dealService.selectByKey(dealId).getUserId();
                SysUserDto userDto = userService.selectByKey(userId);
                BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
                String refuseMessage = wxMessageService.getRefuseMessage(auditVo.getObjectType(), dealId, userDto.getWxopenid(), bizCheckManDto.getUserId(), chekNode, DateUtils.getNowDate(), "提前开工审批");
                checkNoticeService.saveNotice(dealId, auditVo.getObjectType(), CheckNoticeTypeEnum.REFUSE.getKey(), userDto.getWxopenid(), refuseTemplateId, refuseMessage);

            } /*else {
                BizSettlementDto settlementDto = new BizSettlementDto();
                settlementDto.setSettleId(settleId);
                settlementDto.setSettleStatus(SettlementStatusEnum.BACK.getKey());
                settlementService.updateNotNull(settlementDto);

                //保存审批通知
                //拒绝通知-通知承办人
                BizSettlementDto bizSettlementDto = settlementService.selectByKey(settleId);
                String userId = bizSettlementDto.getUserId();
                SysUserDto userDto = userService.selectByKey(userId);
                BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
                String refuseMessage = wxMessageService.getRefuseMessage(auditVo.getObjectType(), settleId, userDto.getWxopenid(), bizCheckManDto.getUserId(), chekNode, DateUtils.getNowDate(), "合同-结算审批");
                checkNoticeService.saveNotice(settleId, auditVo.getObjectType(), CheckNoticeTypeEnum.REFUSE.getKey(), userDto.getWxopenid(), refuseTemplateId, refuseMessage);

            }*/

        } catch (Exception e) {
            throw new AppException("推进审核流程" + e.getMessage());
        }
    }

    /**
     * 获取当前部门负责人
     *
     * @param userId
     */
    public SysDeptDto getOwnerDept(String userId) {
        SysUserDto userDto = userService.selectByKey(userId);
        //1 获取用户所在部门负责人
        return deptService.selectByKey(userDto.getDeptId());

    }

    /**
     * 获取指定审核人
     *
     * @param cfgCode
     * @return
     */
    public BizSysConfigDto getPrjSysConfig(String cfgCode) {
        //查询指定分发用户
        Map<String, Object> param = new HashMap<>();
        param.put("cfgCode", cfgCode);
        List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(param);
        return bizSysConfigDtos.get(0);
    }

    /**
     * 获取当前审核步骤
     *
     * @param nowDate
     * @param args
     * @return
     */
    public BizCheckStepDto getCheckStep(Date nowDate, String... args) {
        BizCheckStepDto checkStepDto = new BizCheckStepDto();
        checkStepDto.setStepId(StringUtils.getUuid32());
        checkStepDto.setStepNo(Integer.valueOf(args[0]));
        checkStepDto.setStepName(args[1]);
        checkStepDto.setStepCreateAt(nowDate);
        checkStepDto.setStepCreateUser(args[2]);
        checkStepDto.setStepState(args[3]);
//        checkStepDto.setStepBeginAt(nowDate);
//        checkStepDto.setStepEndAt(nowDate);
        checkStepDto.setCheckObjId(args[4]);
        checkStepDto.setCheckObjType(args[5]);
        return checkStepDto;
    }


    /**
     * 获取审核人
     *
     * @param nowDate
     * @param args
     * @return
     */
    public BizCheckManDto getCheckMan(Date nowDate, String... args) {
        BizCheckManDto checkManDto = new BizCheckManDto();
        checkManDto.setManId(StringUtils.getUuid32());
        checkManDto.setStepId(args[0]);
        checkManDto.setUserId(args[1]);
        checkManDto.setCheckState(args[2]);
        if (StringUtils.isNotEmpty(args[4])) {
            checkManDto.setCheckTime(nowDate);
        }
        checkManDto.setCheckNode(args[3]);
        checkManDto.setCheckResult(args[4]);
        return checkManDto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveInitMap(Map<String, Object> initMap) {
        BizCheckStepDto checkStep1 = (BizCheckStepDto) initMap.get("checkStep1");
        BizCheckStepDto checkStep2 = (BizCheckStepDto) initMap.get("checkStep2");
        BizCheckStepDto checkStep3 = (BizCheckStepDto) initMap.get("checkStep3");
        BizCheckManDto checkMan1 = (BizCheckManDto) initMap.get("checkMan1");
        BizCheckManDto checkMan2 = (BizCheckManDto) initMap.get("checkMan2");
        BizCheckManDto checkMan3 = (BizCheckManDto) initMap.get("checkMan3");

        if (null != checkStep1) {
            checkStepService.save(checkStep1);
        }

        if (null != checkStep2) {
            checkStepService.save(checkStep2);
        }

        if (null != checkStep3) {
            checkStepService.save(checkStep3);
        }

        if (null != checkMan1) {
            checkManService.save(checkMan1);
        }

        if (null != checkMan2) {
            checkManService.save(checkMan2);
        }

        if (null != checkMan3) {
            checkManService.save(checkMan3);
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public void saveProInitMap(Map<String, Object> initMap, int stepNo) {
        List<BizCheckStepDto> checkStepDtos = new ArrayList<>();
        List<BizCheckManDto> checkManDtos = new ArrayList<>();

        for (int i = 0; i < stepNo; i++) {
            BizCheckStepDto checkStepDto = (BizCheckStepDto) initMap.get("checkStep" + i);
            BizCheckManDto checkManDto = (BizCheckManDto) initMap.get("checkMan" + i);
            checkStepDtos.add(checkStepDto);
            checkManDtos.add(checkManDto);
        }
        checkStepService.saveList(checkStepDtos);
        checkManService.saveList(checkManDtos);
    }


    public boolean isLastStep(AuditVo auditVo) {
        Map<String, Object> param = new HashMap<>();
        param.put("objId", auditVo.getObjId());
        param.put("stepId", auditVo.getStepId());
        List<BizCheckStepDto> lastStep = checkStepService.getLastStepByDealId(param);
        if (null != lastStep && lastStep.size() > 0) {
            BizCheckStepDto checkStepDto1 = lastStep.get(0);
            return auditVo.getStepNo().equals("" + checkStepDto1.getStepNo());
        }
        return false;
    }

    public boolean hasNoDealStep(AuditVo auditVo) {
        Map<String, Object> params = new HashMap<>();
        params.put("objId", auditVo.getObjId());
        params.put("stepNo", auditVo.getStepNo());
        List<BizCheckStepDto> list = checkStepService.selectLastStepByObjectId(params);
        return !StringUtils.isEmpty(list);
    }

    /**
     * 同步骤下是否还有待审核
     *
     * @param auditVo
     * @return
     */
    public boolean isSameLastStep(AuditVo auditVo) {
        //4 查询当前 审核同步骤下  还有没待审核
        Map<String, Object> params = new HashMap<>();
        params.put("objId", auditVo.getObjId());
        params.put("stepNo", auditVo.getStepNo());
        List<BizCheckStepDto> list = checkStepService.selectLastStepByObjectId(params);
        return !StringUtils.isNotEmpty(list);
    }


    @Transactional
    public boolean buildDiyContActiviti(AuditVo auditVo, String userId) throws Exception {
        List<DiyStepParamVo> diyStepParamVos = auditVo.getDiyStepParamVo();
        String stepNoStr = bizCheckStepService.selectMaxStepNo(auditVo.getObjId());

        initStepAndMan(stepNoStr, userId, diyStepParamVos);

        ConstractorAuditService projectAuditService = constractorAuditService.getAuditService(auditVo.getObjectType());
        //提交审核
        projectAuditService.passActiviti(auditVo);
        return true;
    }

    @Transactional
    public boolean buildDiyProActiviti(AuditVo auditVo, String userId) throws Exception {
        List<DiyStepParamVo> diyStepParamVos = auditVo.getDiyStepParamVo();
        String stepNoStr = bizCheckStepService.selectMaxStepNo(auditVo.getObjId());

        initStepAndMan(stepNoStr, userId, diyStepParamVos);

        ProjectAuditService auditService = projectAuditService.getAuditService(auditVo.getObjectType());
        //提交审核
        auditService.passActiviti(auditVo);
        return true;
    }


    public void initStepAndMan(String stepNoStr, String userId, List<DiyStepParamVo> diyStepParamVos) {
        for (int j = 0; j < diyStepParamVos.size(); j++) {
            DiyStepParamVo diyStepParamVo = diyStepParamVos.get(j);
            List<DiyStepVo> diyStepVos = diyStepParamVo.getDiyStepVos();
            Integer stepNo = Integer.valueOf(stepNoStr) + (j + 1);
            for (int i = 0; i < diyStepVos.size(); i++) {
                Date nowDate = DateUtils.getNowDate();
                DiyStepVo diyStepVo = diyStepVos.get(i);

                //1 新建审核步骤
                BizCheckStepDto checkStepDto = getCheckStep(nowDate, String.valueOf(stepNo), diyStepParamVo.getStepName(), userId,
                        null, diyStepParamVo.getCheckObjId(), diyStepParamVo.getCheckObjType(),diyStepVo.getCheckMan());
                //第一个步骤应该设置开始时间
                if (j == 0) {
                    checkStepDto.setStepBeginAt(nowDate);
                }
                checkStepService.save(checkStepDto);

                //2 新增处理人
                BizCheckManDto checkManDto = getCheckMan(nowDate, checkStepDto.getStepId(), diyStepVo.getCheckMan(), CheckManStateEnum.PENDING.getKey(),
                        null, null);

                checkManService.save(checkManDto);

            }
        }
    }
}

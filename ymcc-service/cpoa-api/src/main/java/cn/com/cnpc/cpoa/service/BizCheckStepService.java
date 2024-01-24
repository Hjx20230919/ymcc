package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditSetDto;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTaskDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.enums.CheckManStateEnum;
import cn.com.cnpc.cpoa.enums.CheckStepStateEnum;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.DealStatusEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewTaskEnum;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.project.ProjProjectPo;
import cn.com.cnpc.cpoa.service.constractor.ContAccessService;
import cn.com.cnpc.cpoa.service.constractor.ContCreditSetService;
import cn.com.cnpc.cpoa.service.constractor.ContProjectService;
import cn.com.cnpc.cpoa.service.constractor.ContReviewTaskService;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsApprovalService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsService;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.service.project.ProjPurcPlanService;
import cn.com.cnpc.cpoa.service.project.ProjPurcResultService;
import cn.com.cnpc.cpoa.service.project.ProjSelContService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.DiyStepParamVo;
import cn.com.cnpc.cpoa.vo.DiyStepVo;
import cn.com.cnpc.cpoa.vo.contractor.ContAccessVo;
import cn.com.cnpc.cpoa.vo.contractor.ContProjectVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectPlanVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectResultVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectSelVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectVo;
import com.alibaba.fastjson.JSONObject;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 18:38
 * @Description:
 */
@Service
public class BizCheckStepService {


    @Autowired
    private CheckStepService checkStepService;


    @Autowired
    private ActivitiService activitiService;

    @Autowired
    DealService dealService;

    @Autowired
    CheckManService checkManService;

    @Autowired
    SettlementService settlementService;

    @Autowired
    ConstractorAuditService constractorAuditService;


    @Autowired
    ProjectAuditService projectAuditService;

    @Autowired
    BizDealPsService dealPsService;

    @Autowired
    BizDealPsApprovalService dealPsApprovalService;

    @Autowired
    ContProjectService contProjectService;

    @Autowired
    ContAccessService contAccessService;

    @Autowired
    ContCreditSetService contCreditSetService;

    @Autowired
    ProjProjectService projProjectService;

    @Autowired
    ProjSelContService projSelContService;

    @Autowired
    ProjPurcPlanService projPurcPlanService;

    @Autowired
    ProjPurcResultService projPurcResultService;

    @Autowired
    UserService userService;

    @Autowired
    private ProjCheckLogService projCheckLogService;

    @Autowired
    private ContReviewTaskService reviewTaskService;

    @Autowired
    private ProjSavingRateService savingRateService;

    @Transactional
    public boolean saveDiyCheckStep(AuditVo auditVo, String userId) throws Exception {

        return activitiService.buildDiyActiviti(auditVo, userId);
    }


    public List<CheckStepPo> selectDetails(String dealId, String settleId, String userId) {
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", dealId);
        param.put("settleId", settleId);
        List<CheckStepPo> checkSteplist = new ArrayList<>();
        //  查询合同 下所有 审核步骤.
        if (StringUtils.isNotEmpty(dealId)) {
            checkSteplist = checkStepService.selectDetailsByDealId(param);
            //获取当前审核步骤
            Map<String, Object> params = new HashMap<>();
            params.put("objId", dealId);
            //   List<BizDealDto> dealDtos = dealService.selectUserList(params);
            List<BizCheckStepDto> checkStepDtos = checkStepService.selectMinStepNo(params);
            for (BizCheckStepDto checkStepDto : checkStepDtos) {
                String stepId = checkStepDto.getStepId();
                for (CheckStepPo stepPo : checkSteplist) {
                    if (stepId.equals(stepPo.getStepId())) {
                        stepPo.setIsCurrentStep("1");
                    }
                }
            }

        } else {
            //  查询合同结算下 所有 审核步骤
            checkSteplist = checkStepService.selectDetailsBySettleId(param);
            //获取当前审核步骤
            Map<String, Object> params = new HashMap<>();
            params.put("objId", settleId);
            //   List<BizDealDto> dealDtos = dealService.selectUserList(params);
            //String stepNo= checkStepService.selectMaxStepNo(params);
            List<BizCheckStepDto> checkStepDtos = checkStepService.selectMinStepNo(params);
            for (BizCheckStepDto checkStepDto : checkStepDtos) {
                String stepId = checkStepDto.getStepId();
                for (CheckStepPo stepPo : checkSteplist) {
                    if (stepId.equals(stepPo.getStepId())) {
                        stepPo.setIsCurrentStep("1");
                    }
                }

            }
        }
        return checkSteplist;
    }


    public List<CheckStepPo> selectDetails(String objId) {
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", objId);
        List<CheckStepPo> checkSteplist = checkStepService.selectDetailsByDealId(param);
        //获取当前审核步骤
        Map<String, Object> params = new HashMap<>();
        params.put("objId", objId);
        //   List<BizDealDto> dealDtos = dealService.selectUserList(params);
        List<BizCheckStepDto> checkStepDtos = checkStepService.selectMinStepNo(params);
        for (BizCheckStepDto checkStepDto : checkStepDtos) {
            String stepId = checkStepDto.getStepId();
            for (CheckStepPo stepPo : checkSteplist) {
                if (stepId.equals(stepPo.getStepId())) {
                    stepPo.setIsCurrentStep("1");
                }
            }
        }
        return checkSteplist;
    }

    public List<CheckStepPo> selectBidProjectDetails(String objId,String objType) {
        Map<String, Object> param = new HashMap<>();
        param.put("dealId", objId);
        param.put("objType",objType);
        List<CheckStepPo> checkSteplist = checkStepService.selectDetailsByDealId(param);
        //获取当前审核步骤
        Map<String, Object> params = new HashMap<>();
        params.put("objId", objId);
        params.put("objType", objType);
        List<BizCheckStepDto> checkStepDtos = checkStepService.selectBidProjectMinStepNo(params);
        for (BizCheckStepDto checkStepDto : checkStepDtos) {
            String stepId = checkStepDto.getStepId();
            for (CheckStepPo stepPo : checkSteplist) {
                if (stepId.equals(stepPo.getStepId())) {
                    stepPo.setIsCurrentStep("1");
                }
            }
        }
        return checkSteplist;
    }

    public List<CheckStepPo> selectDetailsPdf(Map<String, Object> param) {
        return checkStepService.selectDetailsByObjId(param);
    }

    public CheckStepPo selectDetailsByObjIdAndObjType(Map<String, Object> param) {
        return checkStepService.selectDetailsByObjIdAndObjType(param);
    }

    public boolean audit(AuditVo auditVo) {
        if (checkAudited(auditVo.getStepId())) {
            throw new AppException("该流程已被审核，请勿重复点击");
        }
        synchronized (this) {
            String auditStatus = auditVo.getAuditStatus();
            checkObj(auditVo);
            boolean isDealPs = CheckTypeEnum.DEALPS.getKey().equals(auditVo.getObjectType());
            if (CheckStepStateEnum.PASS.getKey().equalsIgnoreCase(auditStatus)) {
                //通过
                if (isDealPs) {
                    dealPsApprovalService.passActiviti(auditVo);
                } else {
                    activitiService.passActiviti(auditVo);
                }
            } else if (CheckStepStateEnum.REFUSE.getKey().equalsIgnoreCase(auditStatus)) {
                //拒绝
                if (isDealPs) {
                    dealPsApprovalService.backActiviti(auditVo);
                } else {
                    activitiService.backActiviti(auditVo);
                }
            } else {
                throw new AppException("审核出错！审核状态不正确");
            }
        }
        return true;
    }

    public void checkObj(AuditVo auditVo) {
        String dealId = auditVo.getDealId();
        String settleId = auditVo.getSettleId();
        if (StringUtils.isNotEmpty(dealId)) {
            if (CheckTypeEnum.DEALPS.getKey().equals(auditVo.getObjectType())) {
                if (null == dealPsService.selectByKey(dealId)) {
                    throw new AppException("当前提前开工合同不存在");
                }
            } else if (null == dealService.selectByKey(dealId)) {
                throw new AppException("当前合同不存在");
            }
        } else if (StringUtils.isNotEmpty(settleId)) {
            if (null == settlementService.selectByKey(settleId)) {
                throw new AppException("当前结算不存在");
            }
        }
    }


    public String selectMaxStepNo(String objId) {
        Map<String, Object> param = new HashMap<>();
        param.put("objId", objId);
        String stepNo = checkStepService.selectMaxStepNo(param);
        return stepNo;
    }


    /**
     * 回退提交时 的操作
     *
     * @param objId
     * @throws Exception
     */
    public void updateBackObj(String objId) throws Exception {
        //1 查询当前合同有退回记录的值
        Map<String, Object> params2 = new HashMap<>();
        params2.put("objId", objId);
        //2更新checkman表 状态为未处理 结果不变（更新node时判断是否加前缀用的）
        List<BizCheckManDto> bizCheckManDtos = checkManService.selectBackList(params2);
        for (BizCheckManDto bizCheckManDto : bizCheckManDtos) {
            bizCheckManDto.setCheckState(CheckManStateEnum.PENDING.getKey());
//            bizCheckManDto.setCheckResult(null);
//            bizCheckManDto.setCheckNode(null);
            //  bizCheckManDto.setCheckTime(null);
            checkManService.updateAll(bizCheckManDto);
            //3更新checkstep表的结果为null，通过时判断是否是最后一步时用到
            String stepId = bizCheckManDto.getStepId();
            BizCheckStepDto checkStepDto = checkStepService.selectByKey(stepId);
            checkStepDto.setStepState(null);
            checkStepService.updateAll(checkStepDto);
        }
    }


    public synchronized boolean auditContractor(AuditVo auditVo) throws Exception {
        if (checkAudited(auditVo.getStepId())) {
            throw new AppException("该流程已被审核，请勿重复点击");
        }
        ConstractorAuditService projectAuditService = constractorAuditService.getAuditService(auditVo.getObjectType());
        if (CheckStepStateEnum.PASS.getKey().equalsIgnoreCase(auditVo.getAuditStatus())) {
            //通过
            projectAuditService.passActiviti(auditVo);
        } else if (CheckStepStateEnum.REFUSE.getKey().equalsIgnoreCase(auditVo.getAuditStatus())) {
            //拒绝
            projectAuditService.backActiviti(auditVo);
        } else {
            throw new AppException("审核出错！审核状态不正确");
        }
        return true;
    }

    public synchronized boolean auditProject(AuditVo auditVo) throws Exception {
        if (checkAudited(auditVo.getStepId())) {
            throw new AppException("该流程已被审核，请勿重复点击");
        }
        ProjectAuditService auditService = projectAuditService.getAuditService(auditVo.getObjectType());
        if (CheckStepStateEnum.PASS.getKey().equalsIgnoreCase(auditVo.getAuditStatus())) {
            //通过
            auditService.passActiviti(auditVo);
        } else if (CheckStepStateEnum.REFUSE.getKey().equalsIgnoreCase(auditVo.getAuditStatus())) {
            //拒绝
            auditService.backActiviti(auditVo);
        } else {
            throw new AppException("审核出错！审核状态不正确");
        }
        if (activitiService.isLastStep(auditVo)) {
            //TODO  如果是最后一步，并且为合同立项时，将项目添加到任务管理表
            ProjProjectPo projProjectPo = projProjectService.selectProjComplete(auditVo.getObjId());
            if(Optional.ofNullable(projProjectPo).isPresent()){
                ContReviewTaskDto reviewTaskDto = ContReviewTaskDto.builder()
                        .reviewTaskId(StringUtils.getUuid32())
                        .createAt(DateUtils.getNowDate())
                        .createId(ServletUtils.getSessionUserId())
                        .taskStatus(ContReviewTaskEnum.WAIT_REVIEW.getKey())
                        .reviewYear(projProjectPo.getProjAt())
                        .contId(projProjectPo.getContId() == null ? "" : projProjectPo.getContId())
                        .ownerDeptId(projProjectPo.getOwnerDeptId())
                        .projId(projProjectPo.getProjId())
                        .dealValue(projProjectPo.getLimitTotalPrice() == null ? 0f : projProjectPo.getLimitTotalPrice().floatValue())
                        .accName(projProjectPo.getProjContent())
                        .accType(projProjectPo.getAccessLevel())
                        .build();
                reviewTaskService.save(reviewTaskDto);



                //TODO  添加资金节约率表
                ProjSavingRateDto projSavingRateDto = new ProjSavingRateDto();
                projSavingRateDto.setSavingRateId(StringUtils.getUuid32());
                projSavingRateDto.setCalcYear(projProjectPo.getProjAt());
                projSavingRateDto.setProjId(projProjectPo.getProjId());
                savingRateService.save(projSavingRateDto);
            }

        }
        return true;
    }

    public boolean checkAudited(String stepId) {

        BizCheckStepDto checkStepDto = checkStepService.selectByKey(stepId);
        return StringUtils.isNotEmpty(checkStepDto.getStepState());
    }


    public boolean saveContDiyCheckStep(AuditVo auditVo, String userId) throws Exception {
        return activitiService.buildDiyContActiviti(auditVo, userId);
    }

    public boolean saveProDiyCheckStep(AuditVo auditVo, String userId) throws Exception {
        return activitiService.buildDiyProActiviti(auditVo, userId);
    }

    /**
     * 按条件查询流程中的流程
     *
     * @param params
     * @return
     */
    public List<CheckStepPo> queryEditSteps(Map<String, Object> params) {
        String dealNo = (String) params.get("dealNo");
        String contOrgNo = (String) params.get("contOrgNo");
        String projectNo = (String) params.get("projectNo");

        if (StringUtils.isNotEmpty(dealNo)) {
            return getDealStepPos(dealNo);
        } else if (StringUtils.isNotEmpty(contOrgNo)) {
            return getContStepPos(contOrgNo);
        } else {
            return getProjectStepPos(projectNo);
        }
    }


    private List<CheckStepPo> getDealStepPos(String dealNo) {
        List<CheckStepPo> checkStepPos = new ArrayList<>();

        Map<String, Object> dealParams = new HashMap<>();
        dealParams.put("dealNo", dealNo);
        List<BizDealDto> bizDealDtos = dealService.selectList(dealParams);
        if (StringUtils.isEmpty(bizDealDtos) || !Constants.dealHandStatus.contains(bizDealDtos.get(0).getDealStatus())) {
            return checkStepPos;
        } else {
            BizDealDto dealDto = bizDealDtos.get(0);
            String dealId = dealDto.getDealId();
            //合同正在處理中
            if (!DealStatusEnum.PROGRESSAUDITING.getKey().equals(dealDto.getDealStatus())) {
                return selectDetails(dealId);

            } else {
                //結算在處理中
                Map<String, Object> settleParams = new HashMap<>();
                settleParams.put("dealId", dealId);
                List<BizSettlementDto> bizSettlementDtos = settlementService.selectList(settleParams);
                for (BizSettlementDto settlementDto : bizSettlementDtos) {
                    if (Constants.settleHandStatus.contains(settlementDto.getSettleStatus())) {
                        return selectDetails(settlementDto.getSettleId());
                    }
                }

            }
        }

        return checkStepPos;
    }


    private List<CheckStepPo> getContStepPos(String contOrgNo) {
        List<CheckStepPo> checkStepPos = new ArrayList<>();
        Map<String, Object> contProParams = new HashMap<>();
        contProParams.put("projContCode", contOrgNo);

        List<ContProjectVo> contProjectVos = contProjectService.selectContProject(contProParams);

        for (ContProjectVo contProjectVo : contProjectVos) {
            //承包商立项
            if (Constants.contProHandStatus.contains(contProjectVo.getProjState())) {
                return selectDetails(contProjectVo.getProjId());
            } else {
                //准入
                String projId = contProjectVo.getProjId();
                Map<String, Object> contAccParams = new HashMap<>();
                contAccParams.put("projId", projId);
                List<ContAccessVo> contAccessVos = contAccessService.selectContAccess(contAccParams);
                if (StringUtils.isNotEmpty(contAccessVos)) {
                    ContAccessVo contAccessVo = contAccessVos.get(0);
                    String acceState = contAccessVo.getAcceState();
                    String acceId = contAccessVo.getAcceId();
                    if (Constants.contProHandStatus.contains(acceState)) {
                        return selectDetails(acceId);
                    } else {
                        //变更
                        Map<String, Object> contSetParams = new HashMap<>();
                        contSetParams.put("acceId", acceId);
                        List<BizContCreditSetDto> contCreditSetDtos = contCreditSetService.selectContCreditSet(contSetParams);
                        for (BizContCreditSetDto bizContCreditSetDto : contCreditSetDtos) {
                            if (Constants.contProHandStatus.contains(bizContCreditSetDto.getSetState())) {
                                return selectDetails(bizContCreditSetDto.getSetId());
                            }
                        }

                    }

                }
            }

        }

        return checkStepPos;
    }


    private List<CheckStepPo> getProjectStepPos(String projectNo) {
        List<CheckStepPo> checkStepPos = new ArrayList<>();
        Map<String, Object> proProParams = new HashMap<>();
        proProParams.put("dealNo", projectNo);
        //立项
        List<ProjProjectVo> projProjectVos = projProjectService.selectProject(proProParams);
        for (ProjProjectVo projProjectVo : projProjectVos) {
            String projStatus = projProjectVo.getProjStatus();
            if (Constants.projectHandStatus.contains(projStatus)) {
                return selectDetails(projProjectVo.getProjId());
            } else {
                //选商
                Map<String, Object> proSelParams = new HashMap<>();
                proSelParams.put("projId", projProjectVo.getProjId());
                List<ProjProjectSelVo> projProjectSelVos = projSelContService.selectProjSelCont(proSelParams);
                if (StringUtils.isNotEmpty(projProjectSelVos)) {
                    ProjProjectSelVo projProjectSelVo = projProjectSelVos.get(0);
                    if (Constants.projectHandStatus.contains(projProjectSelVo.getProjStatus())) {
                        return selectDetails(projProjectSelVo.getSelContId());
                    }
                }

                //采购方案
                List<ProjProjectPlanVo> projProjectPlanVos = projPurcPlanService.selectProjPurcPlan(proSelParams);
                if (StringUtils.isNotEmpty(projProjectPlanVos)) {
                    ProjProjectPlanVo projProjectPlanVo = projProjectPlanVos.get(0);
                    if (Constants.projectHandStatus.contains(projProjectPlanVo.getProjStatus())) {
                        return selectDetails(projProjectPlanVo.getPlanId());
                    }
                }

                //采购结果
                List<ProjProjectResultVo> projProjectResultVos = projPurcResultService.selectProjPurcResult(proSelParams);
                if (StringUtils.isNotEmpty(projProjectResultVos)) {
                    ProjProjectResultVo projProjectResultVo = projProjectResultVos.get(0);
                    if (Constants.projectHandStatus.contains(projProjectResultVo.getProjStatus())) {
                        return selectDetails(projProjectResultVo.getResultId());
                    }
                }
            }
        }


        return checkStepPos;

    }

    /**
     * 更新step表的部门id，man表的人员（当时为啥不写在一张表- -）
     **/
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void updateStepById(AuditVo auditVo) {
        List<DiyStepParamVo> diyStepParamVos = auditVo.getDiyStepParamVo();
        String userId = ServletUtils.getSessionUserId();
        String stepId = auditVo.getStepId();
        BizCheckStepDto checkStepDto1 = checkStepService.selectByKey(stepId);
        Integer stepNo = checkStepDto1.getStepNo();
        String checkObjId = checkStepDto1.getCheckObjId();

        List<Object> stepKeys = new ArrayList<>();
        List<Object> manKeys = new ArrayList<>();

        // 获取要删除的step和man表记录里
        if (StringUtils.isNotEmpty(checkObjId)) {
            Map<String, Object> stepNoMap = new HashMap<>();
            stepNoMap.put("nextStepNo", String.valueOf(stepNo));
            stepNoMap.put("objId", checkObjId);
            //查询步骤大于当前的既是要删除的
            List<BizCheckStepDto> checkStepDtos = checkStepService.selectList(stepNoMap);
            if (checkStepDtos.size() > 20) {
                throw new AppException("您操作的流程过多，请和开发人员确认后协助删除");
            }
            for (BizCheckStepDto checkStepDto : checkStepDtos) {
                Map<String, Object> manMap = new HashMap<>();
                manMap.put("stepId", checkStepDto.getStepId());
                List<BizCheckManDto> checkManDtos = checkManService.selectList(manMap);
                for (BizCheckManDto checkManDto : checkManDtos) {
                    //TODO  如果已经审核，则记录日志后在删除
                    if (!CheckManStateEnum.PENDING.getKey().equals(checkManDto.getCheckState())) {
                        HashMap<String, Object> hashMap = new HashMap<>(16);
                        hashMap.put("checkStep",checkStepDto);
                        hashMap.put("checkMan",checkManDto);
                        ProjCheckLogDto projCheckLogDto = ProjCheckLogDto.builder()
                                .checkLogId(StringUtils.getUuid32())
                                .checkObjId(checkStepDto.getCheckObjId())
                                .checkObjType(checkStepDto.getCheckObjType())
                                .createId(userId)
                                .createAt(DateUtils.getNowDate())
                                .planLog(JSONObject.toJSONString(hashMap))
                                .build();
                        projCheckLogService.save(projCheckLogDto);
                    }
                    manKeys.add(checkManDto.getManId());
                }

                stepKeys.add(checkStepDto.getStepId());
            }

        } else {
            throw new AppException("修改流程出错，请和开发人员联系");
        }

        checkStepService.deleteList(stepKeys);

        checkManService.deleteList(manKeys);


        activitiService.initStepAndMan(String.valueOf(stepNo), ServletUtils.getSessionUserId(), diyStepParamVos);




    }
}

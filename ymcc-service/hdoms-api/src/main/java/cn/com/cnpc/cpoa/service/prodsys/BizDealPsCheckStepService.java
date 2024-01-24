package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizCheckManDto;
import cn.com.cnpc.cpoa.domain.BizCheckStepDto;
import cn.com.cnpc.cpoa.enums.CheckManStateEnum;
import cn.com.cnpc.cpoa.enums.CheckStepStateEnum;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.CheckManService;
import cn.com.cnpc.cpoa.service.CheckStepService;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <copy from BizCheckStepService>
 * PS Deal approval process
 *
 * @author anonymous
 * @create 23/02/2020 23:31
 * @since 1.0.0
 */
@Service
public class BizDealPsCheckStepService {


    @Autowired
    private CheckStepService checkStepService;

    @Autowired
    private BizDealPsApprovalService activitiService;

    @Autowired
    BizDealPsService dealService;

    @Autowired
    CheckManService checkManService;

//    @Autowired
//    SettlementService settlementService;

    @Autowired
    ConstractorAuditService constractorAuditService;


    @Autowired
    ProjectAuditService projectAuditService;


    @Transactional
    public boolean saveDiyCheckStep(AuditVo auditVo, String userId) {

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
        //  查询合同 下所有 审核步骤.
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

    public List<CheckStepPo> selectDetailsPdf(Map<String, Object> param) {
        // param.put("objId", objId);
        //param.put("stepstate", stepstate);
        //  查询合同 下所有 审核步骤.
        return checkStepService.selectDetailsByObjId(param);
    }

    public boolean audit(AuditVo auditVo) {
        synchronized (this) {
            String auditStatus = auditVo.getAuditStatus();
            checkObj(auditVo);
            if (CheckStepStateEnum.PASS.getKey().equalsIgnoreCase(auditStatus)) {
                //通过
                activitiService.passActiviti(auditVo);
            } else if (CheckStepStateEnum.REFUSE.getKey().equalsIgnoreCase(auditStatus)) {
                //拒绝
                activitiService.backActiviti(auditVo);
            } else {
                throw new AppException("审核出错！审核状态不正确");
            }
        }
        return true;
    }

    public void checkObj(AuditVo auditVo) {
        String dealId = auditVo.getDealId();
//        String settleId = auditVo.getSettleId();
        if (StringUtils.isNotEmpty(dealId)) {
            if (null == dealService.selectByKey(dealId)) {
                throw new AppException("当前提前开工不存在");
            }
        }/* else if (StringUtils.isNotEmpty(settleId)) {
            if (null == settlementService.selectByKey(settleId)) {
                throw new AppException("当前结算不存在");
            }
            ;
        }*/
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
            //  bizCheckManDto.setCheckTime(null);
            checkManService.updateNotNull(bizCheckManDto);

            //3更新checkstep表的结果为null，通过时判断是否是最后一步时出错
            String stepId = bizCheckManDto.getStepId();
            BizCheckStepDto checkStepDto = checkStepService.selectByKey(stepId);
            checkStepDto.setStepState(null);
            checkStepService.updateAll(checkStepDto);
        }
    }


    public synchronized boolean auditContractor(AuditVo auditVo) throws Exception {
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
        return true;
    }


    public boolean saveContDiyCheckStep(AuditVo auditVo, String userId) throws Exception {
        return activitiService.buildDiyContActiviti(auditVo, userId);
    }

    public boolean saveProDiyCheckStep(AuditVo auditVo, String userId) throws Exception {
        return activitiService.buildDiyProActiviti(auditVo, userId);
    }
}

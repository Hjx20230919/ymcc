package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.enums.prodsys.ContractStateEnum;
import cn.com.cnpc.cpoa.mapper.prodsys.BizProjectDtoMapper;
import cn.com.cnpc.cpoa.service.*;
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
 * <>
 *
 * @author wangjun
 * @create 05/04/2020 17:43
 * @since 1.0.0
 */
@Service
public class BizProjectApprovalService {

    @Autowired
    private CheckManService checkManService;

    @Autowired
    private CheckStepService checkStepService;

    @Autowired
    private DeptService deptService;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    WxMessageService wxMessageService;

    @Autowired
    CheckNoticeService checkNoticeService;

    @Autowired
    UserService userService;

    @Autowired
    BizProjectCheckStepService bizCheckStepService;

    @Autowired
    BizProjectService bizProjectService;

    @Autowired
    BizProjectDtoMapper bizProjectDtoMapper;

    @Value("${templateId.passTemplateId}")
    private String passTemplateId;

    @Value("${templateId.refuseTemplateId}")
    private String refuseTemplateId;

    @Value("${templateId.pendTemplateId}")
    private String pendTemplateId;

    /**
     * init approval activiti for contractType includes INSTRUCTION or TRANSFER
     * 指令划拨流程：提出人-部门负责人-生产系统管理员—后续排流程
     *
     * @param userId
     * @param checkObjId
     * @param checkObjType
     * @param deptId
     * @param stepNo
     * @param flag
     * @throws Exception
     */
    @Transactional
    public void initActivitiOfInstruction(String userId, String checkObjId, String checkObjType, String deptId, String stepNo, String flag) throws Exception {
        try {
            Date nowDate = DateUtils.getNowDate();

            // 获取部门负责人
            SysDeptDto sysDeptDto = deptService.selectByKey(deptId);
            String deptManager = sysDeptDto.getDeptManager();

            Map<String, Object> param = new HashMap<>();
            param.put("cfgCode", Constants.INSTRUCTION_PROJECT_DISPATCHER);
            List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(param);
            BizSysConfigDto bizSysConfigDto = bizSysConfigDtos.get(0);
            String deptManager2 = bizSysConfigDto.getCfgValue();

            // step 1 提出人
            if (Integer.valueOf(stepNo) == 0 || StringUtils.isNotEmpty(flag)) {
                // new step
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
                checkStepDto.setCheckObjId(checkObjId);
                checkStepDto.setCheckObjType(checkObjType);
                checkStepDto.setCheckDeptId(userService.selectByKey(userId).getDeptId());
                checkStepService.save(checkStepDto);

                // new checkMan
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

            // step 2 发起人所属部门
            // division manager maybe not exists
            if (StringUtils.isNotEmpty(deptManager)) {
                // 新建部门审核步骤
                BizCheckStepDto checkStepDto1 = new BizCheckStepDto();
                String step1 = StringUtils.getUuid32();

                checkStepDto1.setStepId(step1);
                checkStepDto1.setStepNo(Integer.valueOf(stepNo) + 1);
                checkStepDto1.setStepName("部门审核");
                checkStepDto1.setStepCreateAt(nowDate);
                checkStepDto1.setStepCreateUser(userId);
                checkStepDto1.setStepBeginAt(nowDate);
                checkStepDto1.setCheckObjId(checkObjId);
                checkStepDto1.setCheckObjType(checkObjType);
                checkStepDto1.setCheckDeptId(userService.selectByKey(deptManager).getDeptId());
                checkStepService.save(checkStepDto1);

                // 新建部门审核处理人
                BizCheckManDto checkManDto1 = new BizCheckManDto();
                checkManDto1.setManId(StringUtils.getUuid32());
                checkManDto1.setStepId(step1);
                checkManDto1.setUserId(deptManager);
                checkManDto1.setCheckState(CheckManStateEnum.PENDING.getKey());
                checkManDto1.setCheckNode(null);
                checkManDto1.setCheckResult(null);
                checkManService.save(checkManDto1);

                // notify checkMan
                SysUserDto userDto = userService.selectByKey(deptManager);
                String pendingMessage = wxMessageService.getPendingMessage(checkObjType, checkObjId, userDto.getWxopenid(), nowDate, "项目开工审批");
                checkNoticeService.saveNotice(checkObjId, checkObjType, CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
            }

            // step 3 生产系统管理员(流程分发员)
            {
                BizCheckStepDto checkStepDto2 = new BizCheckStepDto();
                String step2 = StringUtils.getUuid32();

                checkStepDto2.setStepId(step2);
                checkStepDto2.setStepNo(Integer.valueOf(stepNo) + 2);
                checkStepDto2.setStepName("生产系统管理员审核");
                checkStepDto2.setStepCreateAt(nowDate);
                checkStepDto2.setStepCreateUser(userId);
                checkStepDto2.setCheckObjId(checkObjId);
                checkStepDto2.setCheckObjType(checkObjType);
                checkStepDto2.setStepSplit(1);
                checkStepDto2.setCheckDeptId(userService.selectByKey(deptManager2).getDeptId());
                checkStepService.save(checkStepDto2);

                // 8 新建第二步审核人
                BizCheckManDto checkManDto2 = new BizCheckManDto();
                checkManDto2.setManId(StringUtils.getUuid32());
                checkManDto2.setStepId(step2);
                checkManDto2.setUserId(deptManager2);
                checkManDto2.setCheckState(CheckManStateEnum.PENDING.getKey());
                checkManDto2.setCheckNode(null);
                checkManDto2.setCheckResult(null);
                checkManService.save(checkManDto2);
            }

        } catch (Exception e) {
            throw new AppException("新建初始化流程出错" + e.getMessage());
        }
    }

    /**
     * init approval activiti for project.referUnit is diffrent from deal.deptId
     * 代签流程：提出人-实施单位部门负责人审核（确认）
     *
     * @param userId
     * @param checkObjId
     * @param checkObjType
     * @param deptId
     * @param stepNo
     * @param flag
     * @throws Exception
     */
    @Transactional
    public void initActivitiOfDelegation(String userId, String checkObjId, String checkObjType, String deptId, String stepNo, String flag) throws Exception {
        try {
            Date nowDate = DateUtils.getNowDate();

            // 获取部门负责人
            SysDeptDto sysDeptDto = deptService.selectByKey(deptId);
            String deptManager = sysDeptDto.getDeptManager();

            // step 1 提出人
            if (Integer.valueOf(stepNo) == 0 || StringUtils.isNotEmpty(flag)) {
                // new step
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
                checkStepDto.setCheckObjId(checkObjId);
                checkStepDto.setCheckObjType(checkObjType);
                checkStepDto.setCheckDeptId(userService.selectByKey(userId).getDeptId());

                checkStepService.save(checkStepDto);

                // new checkMan
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

            // step 2 发起人所属部门
            // division manager maybe not exists
            if (StringUtils.isNotEmpty(deptManager)) {
                // 新建部门审核步骤
                BizCheckStepDto checkStepDto1 = new BizCheckStepDto();
                String step1 = StringUtils.getUuid32();

                checkStepDto1.setStepId(step1);
                checkStepDto1.setStepNo(Integer.valueOf(stepNo) + 1);
                checkStepDto1.setStepName("部门审核");
                checkStepDto1.setStepCreateAt(nowDate);
                checkStepDto1.setStepCreateUser(userId);
                checkStepDto1.setStepBeginAt(nowDate);
                checkStepDto1.setCheckObjId(checkObjId);
                checkStepDto1.setCheckObjType(checkObjType);
                checkStepDto1.setCheckDeptId(userService.selectByKey(deptManager).getDeptId());

                checkStepService.save(checkStepDto1);

                // 新建部门审核处理人
                BizCheckManDto checkManDto1 = new BizCheckManDto();
                checkManDto1.setManId(StringUtils.getUuid32());
                checkManDto1.setStepId(step1);
                checkManDto1.setUserId(deptManager);
                checkManDto1.setCheckState(CheckManStateEnum.PENDING.getKey());
                checkManDto1.setCheckNode(null);
                checkManDto1.setCheckResult(null);
                checkManService.save(checkManDto1);

                // notify checkMan
                SysUserDto userDto = userService.selectByKey(deptManager);
                String pendingMessage = wxMessageService.getPendingMessage(checkObjType, checkObjId, userDto.getWxopenid(), nowDate, "项目开工审批");
                checkNoticeService.saveNotice(checkObjId, checkObjType, CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
            }

        } catch (Exception e) {
            throw new AppException("新建初始化流程出错" + e.getMessage());
        }
    }

    /**
     * init approval activiti for deal which splited into multiple project
     *
     * @param userId
     * @param checkObjId
     * @param checkObjType
     * @param deptId
     * @param stepNo
     * @param flag
     * @throws Exception
     */
    @Transactional
    public void initActivitiOfMultipleProject(String userId, String checkObjId, String checkObjType, String deptId, String stepNo, String flag) throws Exception {
        try {
            Date nowDate = DateUtils.getNowDate();

            // 获取部门负责人
            SysDeptDto sysDeptDto = deptService.selectByKey(deptId);
            String deptManager = sysDeptDto.getDeptManager();

            // step 1 提出人
            if (Integer.valueOf(stepNo) == 0 || StringUtils.isNotEmpty(flag)) {
                // new step
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
                checkStepDto.setCheckObjId(checkObjId);
                checkStepDto.setCheckObjType(checkObjType);
                checkStepDto.setCheckDeptId(userService.selectByKey(userId).getDeptId());
                checkStepService.save(checkStepDto);

                // new checkMan
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

            // step 2 发起人所属部门
            // division manager maybe not exists
            if (StringUtils.isNotEmpty(deptManager)) {
                // 新建部门审核步骤
                BizCheckStepDto checkStepDto1 = new BizCheckStepDto();
                String step1 = StringUtils.getUuid32();

                checkStepDto1.setStepId(step1);
                checkStepDto1.setStepNo(Integer.valueOf(stepNo) + 1);
                checkStepDto1.setStepName("部门审核");
                checkStepDto1.setStepCreateAt(nowDate);
                checkStepDto1.setStepCreateUser(userId);
                checkStepDto1.setStepBeginAt(nowDate);
                checkStepDto1.setCheckObjId(checkObjId);
                checkStepDto1.setCheckObjType(checkObjType);
                checkStepDto1.setCheckDeptId(userService.selectByKey(deptManager).getDeptId());
                checkStepService.save(checkStepDto1);

                // 新建部门审核处理人
                BizCheckManDto checkManDto1 = new BizCheckManDto();
                checkManDto1.setManId(StringUtils.getUuid32());
                checkManDto1.setStepId(step1);
                checkManDto1.setUserId(deptManager);
                checkManDto1.setCheckState(CheckManStateEnum.PENDING.getKey());
                checkManDto1.setCheckNode(null);
                checkManDto1.setCheckResult(null);
                checkManService.save(checkManDto1);

                // notify checkMan
                SysUserDto userDto = userService.selectByKey(deptManager);
                String pendingMessage = wxMessageService.getPendingMessage(checkObjType, checkObjId, userDto.getWxopenid(), nowDate, "项目开工审批");
                checkNoticeService.saveNotice(checkObjId, checkObjType, CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
            }

        } catch (Exception e) {
            throw new AppException("新建初始化流程出错" + e.getMessage());
        }
    }

    @Transactional
    public boolean buildDiyActiviti(AuditVo auditVo, String userId) {
        List<DiyStepParamVo> diyStepParamVos = auditVo.getDiyStepParamVo();
        String stepNoStr = null;
        if (auditVo.getObjectType().equals(CheckTypeEnum.INSTRUCTION.getKey())) {
            stepNoStr = bizCheckStepService.selectMaxStepNo(auditVo.getDealId());
        } else {
            // TODO multiProject or delegate project no need diy activiti
        }
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
            String dealId = auditVo.getDealId(); // dealId here means project.contractId
            String manId = auditVo.getManId();

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
            param.put("objId", dealId);
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
            params.put("objId", dealId);
            params.put("stepNo", auditVo.getStepNo());

            List<BizCheckStepDto> list = checkStepService.selectLastStepByObjectId(params);
            if (null == list || list.size() == 0) { //当前审核最后一步。
                //   if(Integer.valueOf(auditVo.getStepNo())>2){
                if (isLastStep1) {
                    //如果是最后一步，且都是审核完成了。则更新合同状态为 progressAuditing。
                    BizProjectDto dealDto = new BizProjectDto();
                    dealDto.setContractId(dealId);
                    BizProjectDto bizDealDto = bizProjectService.selectByKey(dealId);
                    dealDto.setContractState(ContractStateEnum.CONFIRMED.getKey());
                    bizProjectService.updateNotNull(dealDto);

                    //保存审批通知
                    //通过通知-通知承办人
                    String userId = bizProjectService.selectByKey(dealId).getCreateUser();
                    SysUserDto userDto = userService.selectByKey(userId);
                    String passMessage = wxMessageService.getPassMessage(auditVo.getObjectType(), dealId, userDto.getWxopenid(), bizCheckManDto.getUserId(), chekNode, nowDate, "项目开工审批");
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
                    param1.put("contractId", dealId);
                    List<SysUserDto> checkManDtos = bizProjectDtoMapper.selectUserNameList(param1);
                    for (SysUserDto chkMan : checkManDtos) {
                        String userId = chkMan.getUserId();
                        SysUserDto userDto = userService.selectByKey(userId);
                        String pendingMessage = wxMessageService.getPendingMessage(auditVo.getObjectType(), dealId, userDto.getWxopenid(), nowDate, "项目开工审批");
                        checkNoticeService.saveNotice(dealId, auditVo.getObjectType(), CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
                    }
                }
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
            String contractId = auditVo.getDealId();
//            String settleId = auditVo.getSettleId();
            String manId = auditVo.getManId();

            if (StringUtils.isNotEmpty(contractId)) {
                //防止出现 同时退回的情况
                BizProjectDto dealDto = bizProjectService.selectByKey(contractId);
                String dealStatus = dealDto.getContractState();
                if (ContractStateEnum.BACK.getKey().equals(dealStatus)) {
                    throw new AppException("该项目开工已有退回记录，暂时无法发起退回");
                }
            }

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

            //3 回退给发起人。
            if (StringUtils.isNotEmpty(contractId)) {
                BizProjectDto dealDto = new BizProjectDto();
                dealDto.setContractId(contractId);
                dealDto.setContractState(ContractStateEnum.BACK.getKey());

                bizProjectService.updateNotNull(dealDto);

                //保存审批通知
                //拒绝通知-通知承办人
                String userId = bizProjectService.selectByKey(contractId).getCreateUser();
                SysUserDto userDto = userService.selectByKey(userId);
                BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
                String refuseMessage = wxMessageService.getRefuseMessage(auditVo.getObjectType(), contractId, userDto.getWxopenid(), bizCheckManDto.getUserId(), chekNode, DateUtils.getNowDate(), "项目开工审批");
                checkNoticeService.saveNotice(contractId, auditVo.getObjectType(), CheckNoticeTypeEnum.REFUSE.getKey(), userDto.getWxopenid(), refuseTemplateId, refuseMessage);

            }

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

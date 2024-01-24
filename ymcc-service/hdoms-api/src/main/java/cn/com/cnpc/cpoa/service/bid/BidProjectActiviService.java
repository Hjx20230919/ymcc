package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.bid.BidCertiBorrowDto;
import cn.com.cnpc.cpoa.domain.bid.BidProjectDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.mapper.BizDealDtoMapper;
import cn.com.cnpc.cpoa.mapper.BizSettlementDtoMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.service.prodsys.BizProjectManager;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.BigDecimalUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.DiyStepParamVo;
import cn.com.cnpc.cpoa.vo.DiyStepVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: Yuxq
 * @Date: 2022/7/10 11:17
 * @Description:标书会审流程相关
 */
@Service
public class BidProjectActiviService {

    @Autowired
    private CheckManService checkManService;

    @Autowired
    private CheckStepService checkStepService;

    @Autowired
    private DeptService deptService;


    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    private SysConfigService sysConfigService;

    @Autowired
    private WxMessageService wxMessageService;

    @Autowired
    private CheckNoticeService checkNoticeService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidCertiBorrowService certiBorrowService;

    @Autowired
    private BidProjectService bidProjectService;

    @Autowired
    private ActivitiService activitiService;

    @Value("${templateId.pendTemplateId}")
    private String pendTemplateId;


    /**
     * 添加基础审核流程
     * 1 用户所在部门负责人
     * 2 企管部审核人
     */
    @Transactional
    public void initBaseActiviti(String userId, String bidProjId, String type, String deptId, String stepNo) {
        Date nowDate = DateUtils.getNowDate();
        try {
            Map<String, Object> param = new HashMap<>(4);
            param.put("userId", userId);
            //1 获取用户所在部门负责人
            SysDeptDto sysDeptDto = deptService.selectByKey(deptId);
            String deptManager = sysDeptDto.getDeptManager();

            //查询指定分发用户
            Map<String, Object> param2 = new HashMap<>();
            if (type.equals(CheckTypeEnum.CERTIBORROW.getKey())) {
                param2.put("cfgCode", Constants.BIDDING_CERTIBORROW_SPLIT_USER);
            } else {
                param2.put("cfgCode", Constants.BIDPROJECT_SPLIT_USER);
            }
            List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(param2);
            BizSysConfigDto bizSysConfigDto = bizSysConfigDtos.get(0);
            String deptManager2 = bizSysConfigDto.getCfgValue();

            if (Integer.valueOf(stepNo) == 0 ) {
                //3 新建发起人
                BizCheckStepDto checkStepDto = new BizCheckStepDto();
                String step = UUID.randomUUID().toString().trim().replaceAll("-", "");
                checkStepDto.setStepId(step);
                checkStepDto.setStepNo(0);
                checkStepDto.setStepName("发起");
                checkStepDto.setStepCreateAt(nowDate);
                checkStepDto.setStepCreateUser(userId);
                checkStepDto.setStepState(CheckStepStateEnum.PASS.getKey());
                checkStepDto.setStepBeginAt(nowDate);
                checkStepDto.setStepEndAt(nowDate);
                checkStepDto.setCheckObjId(bidProjId);
                checkStepDto.setCheckObjType(type);
                checkStepDto.setCheckDeptId(userService.selectByKey(userId).getDeptId());
                checkStepService.save(checkStepDto);

                //4 新增发起人
                BizCheckManDto checkManDto = new BizCheckManDto();
                checkManDto.setManId(StringUtils.getUuid32());
                checkManDto.setStepId(step);
                checkManDto.setUserId(userId);
                checkManDto.setCheckState(CheckManStateEnum.PENDED.getKey());
                checkManDto.setCheckTime(nowDate);
                checkManDto.setCheckNode("通过");
                checkManDto.setCheckResult(CheckManResultEnum.PASS.getKey());
                checkManService.save(checkManDto);

            }

            //如果部门负责人不存在，则跳过此步骤
            if (StringUtils.isNotEmpty(deptManager) ) {
                //5 新建第一步审核步骤
                BizCheckStepDto checkStepDto1 = new BizCheckStepDto();
                String step1 = StringUtils.getUuid32();
                checkStepDto1.setStepId(step1);
                checkStepDto1.setStepNo(Integer.valueOf(stepNo) + 1);
                checkStepDto1.setStepName("部门审核");
                checkStepDto1.setStepCreateAt(nowDate);
                checkStepDto1.setStepCreateUser(userId);
                checkStepDto1.setStepBeginAt(nowDate);
                checkStepDto1.setCheckObjId(bidProjId);
                checkStepDto1.setCheckObjType(type);
                checkStepDto1.setCheckDeptId(userService.selectByKey(deptManager).getDeptId());
                checkStepService.save(checkStepDto1);

                //6 新增第一步处理人
                BizCheckManDto checkManDto1 = new BizCheckManDto();
                checkManDto1.setManId(StringUtils.getUuid32());
                checkManDto1.setStepId(step1);
                checkManDto1.setUserId(deptManager);
                checkManDto1.setCheckState(CheckManStateEnum.PENDING.getKey());
                checkManDto1.setCheckNode(null);
                checkManDto1.setCheckResult(null);
                checkManService.save(checkManDto1);


                SysUserDto userDto = userService.selectByKey(deptManager);
//                String pendingMessage = wxMessageService.getBidProjectMessage(bidProjId, userDto.getWxopenid(), nowDate, "标书会审审批");
//                checkNoticeService.saveNotice(bidProjId, type, CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
            }


            // 7 新建第二步审核
            BizCheckStepDto checkStepDto2 = new BizCheckStepDto();
            String step2 = StringUtils.getUuid32();
            checkStepDto2.setStepId(step2);
            checkStepDto2.setStepNo(Integer.valueOf(stepNo) + 2);
            checkStepDto2.setStepName("市场生产部");
            checkStepDto2.setStepCreateAt(nowDate);
            checkStepDto2.setStepCreateUser(userId);
            checkStepDto2.setCheckObjId(bidProjId);
            checkStepDto2.setCheckObjType(type);
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
        } catch (Exception e) {
            throw new AppException("新建初始化流程出错" + e.getMessage());
        }
    }

    @Transactional
    public boolean buildDiyActiviti(AuditVo auditVo, String userId) throws Exception {
        List<DiyStepParamVo> diyStepParamVos = auditVo.getDiyStepParamVo();
        String stepNoStr = bizCheckStepService.selectMaxStepNo(auditVo.getObjId());

        initStepAndMan(stepNoStr, userId, diyStepParamVos);

        //提交审核
        passActiviti(auditVo);
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
                        null, diyStepParamVo.getCheckObjId(), diyStepParamVo.getCheckObjType(), diyStepVo.getCheckMan());
                //第一个步骤应该设置开始时间
                if (j == 0) {
                    checkStepDto.setStepBeginAt(nowDate);
                }
                checkStepService.save(checkStepDto);

                //2 新增处理人
                BizCheckManDto checkManDto = getCheckMan(nowDate, checkStepDto.getStepId(), diyStepVo.getCheckMan(), CheckManStateEnum.PENDING.getKey(),
                        null, null);

                checkManService.save(checkManDto);
                stepNo ++;
            }
        }
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
        checkStepDto.setCheckObjId(args[4]);
        checkStepDto.setCheckObjType(args[5]);
        checkStepDto.setCheckDeptId(userService.selectByKey(args[6]).getDeptId());
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

    /**
     * 推进审核流程
     */
    @Transactional
    public boolean passActiviti(AuditVo auditVo) {
        Date nowDate = DateUtils.getNowDate();
        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());
        //如果当前步骤为最后一步且审批类型为投标资料申请，修改公司资质借用状态为同意
        if (activitiService.isLastStepByObjIdAndObjType(auditVo) && (auditVo.getObjectType().equals(CheckTypeEnum.CERTIBORROW.getKey()) ||
                auditVo.getObjectType().equals(CheckTypeEnum.PERSONNELFILE.getKey()))) {
            if (auditVo.getObjectType().equals(CheckTypeEnum.CERTIBORROW.getKey())) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("objId",auditVo.getObjId());
                hashMap.put("objType",CheckTypeEnum.PERSONNELFILE.getKey());
                List<CheckStepPo> checkStepPos = checkStepService.selectLastStepAudting(hashMap);
                if (checkStepPos.get(0) == null) {
                    BidCertiBorrowDto bidCertiBorrowDto = certiBorrowService.selectByKey(auditVo.getObjId());
                    bidCertiBorrowDto.setCheckStatus(BidCertiBorrowEnum.AGREE.getValue());
                    bidCertiBorrowDto.setCertiBorrowStatus(BidCertiBorrowEnum.AUDITED.getKey());
                    certiBorrowService.updateNotNull(bidCertiBorrowDto);
                }
            } else if (auditVo.getObjectType().equals(CheckTypeEnum.PERSONNELFILE.getKey())) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("objId",auditVo.getObjId());
                hashMap.put("objType",CheckTypeEnum.CERTIBORROW.getKey());
                List<CheckStepPo> checkStepPos = checkStepService.selectLastStepAudting(hashMap);
                if (checkStepPos.get(0) == null) {
                    BidCertiBorrowDto bidCertiBorrowDto = certiBorrowService.selectByKey(auditVo.getObjId());
                    bidCertiBorrowDto.setCheckStatus(BidCertiBorrowEnum.AGREE.getValue());
                    bidCertiBorrowDto.setCertiBorrowStatus(BidCertiBorrowEnum.AUDITED.getKey());
                    certiBorrowService.updateNotNull(bidCertiBorrowDto);
                }
            }
        }else if (activitiService.isLastStepByObjIdAndObjType(auditVo) && auditVo.getObjectType().equals(CheckTypeEnum.STOPBIDPROJECT.getKey())) {
            //如果为最后一步，并且审批类型为投标终止，则修改状态为完成
            String bidProjectId = auditVo.getObjId();
            BidProjectDto bidProjectDto = bidProjectService.selectByKey(bidProjectId);
            bidProjectDto.setProjStatus(BidProjectStatusEnum.CANCEL.getKey());
            bidProjectService.updateNotNull(bidProjectDto);
        }
        return true;
    }

    public void defaultUpdateStep(AuditVo auditVo,Date nowDate,String checkResult){
        String auditStatus=auditVo.getAuditStatus();
        String stepId = auditVo.getStepId();
        String chekNode=auditVo.getChekNode();
        String manId = auditVo.getManId();


        checkStepService.checkUpdate(nowDate,stepId, checkResult);


        checkManService.checkUpdate(nowDate,manId, CheckManStateEnum.PENDED.getKey(),chekNode,auditStatus);

    }

    /**
     * 回退审核流程
     */
    @Transactional
    public void backActiviti(AuditVo auditVo) {
        Date nowDate = DateUtils.getNowDate();
        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.BACK.getKey());
        if (auditVo.getObjectType() != null && auditVo.getObjectType().equals(CheckTypeEnum.BIDPROJECT.getKey())) {
            //修改招标项目状态为退回
            BidProjectDto bidProjectDto = bidProjectService.selectByKey(auditVo.getObjId());
            bidProjectDto.setProjStatus(BidProjectStatusEnum.BACK.getKey());
            bidProjectService.updateNotNull(bidProjectDto);

        } else if (auditVo.getObjectType() != null && auditVo.getObjectType().equals(CheckTypeEnum.CERTIBORROW.getKey())){
            //修改投标资料申请为不同意
            BidCertiBorrowDto bidCertiBorrowDto = certiBorrowService.selectByKey(auditVo.getObjId());
            bidCertiBorrowDto.setCheckStatus(BidCertiBorrowEnum.DISAGREE.getValue());
            certiBorrowService.updateNotNull(bidCertiBorrowDto);
        }

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


}

package cn.com.cnpc.cpoa.service.constractor.audit;

import cn.com.cnpc.cpoa.domain.BizCheckManDto;
import cn.com.cnpc.cpoa.domain.BizCheckStepDto;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewOfficeDetailDto;
import cn.com.cnpc.cpoa.enums.CheckManResultEnum;
import cn.com.cnpc.cpoa.enums.CheckManStateEnum;
import cn.com.cnpc.cpoa.enums.CheckStepStateEnum;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewOfficeDetailEnum;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-30  11:17
 * @Description: 考评任务审核流程处理
 * @Version: 1.0
 */
@Service
public class ContReviewOfficeDetailAuditService extends ConstractorAuditService{

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initActiviti(String userId, String objId) throws Exception {
        Map<String, Object> initMap = getDefaultStepMap(userId, objId, DateUtils.getNowDate());

        activitiService.saveProInitMap(initMap, 2);
    }

    public Map<String, Object> getDefaultStepMap(String userId, String objId, Date nowDate) {
        Map<String, Object> initMap = new HashMap<>();

        SysDeptDto ownerDept = activitiService.getOwnerDept(userId);
        String deptManager = ownerDept.getDeptManager();
        addAudit(nowDate,0,launchMsg,userId,objId,userId,initMap);

        //2 获取项目审核 部门负责人信息
        if (StringUtils.isNotEmpty(deptManager)) {
            addAudit(nowDate,1,deptMsg,userId,objId,deptManager,initMap);
        }
        return initMap;

    }

    /**
     * 添加审核步骤
     * @param nowDate   步骤创建时间
     * @param stepNo    当前步骤
     * @param msg   步骤名称
     * @param createUserId  创建人id
     * @param checkObjId    审核类型id
     * @param auditUserId   审核人id
     * @param initMap   步骤集合
     */
    private void addAudit(Date nowDate,int stepNo,String msg,String createUserId,String checkObjId,String auditUserId,Map<String, Object> initMap){
        if (stepNo == 0){
            //1 获取项目审核 发起人信息
            BizCheckStepDto checkStep0 = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), msg, createUserId,
                    CheckStepStateEnum.PASS.getKey(), checkObjId, CheckTypeEnum.OFFICE_TASK.getKey(),createUserId);
            checkStep0.setStepBeginAt(nowDate);
            checkStep0.setStepEndAt(nowDate);

            BizCheckManDto checkMan0 = activitiService.getCheckMan(nowDate, checkStep0.getStepId(), createUserId, CheckManStateEnum.PENDED.getKey(),
                    passMsg, CheckManResultEnum.PASS.getKey());

            initMap.put("checkStep" + stepNo, checkStep0);
            initMap.put("checkMan" + stepNo, checkMan0);
        }else {
            //1 新建审核步骤
            BizCheckStepDto checkStepDto = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), msg, createUserId,
                    null, checkObjId,CheckTypeEnum.OFFICE_TASK.getKey(),auditUserId);
            //2 新增处理人
            BizCheckManDto checkManDto = activitiService.getCheckMan(nowDate, checkStepDto.getStepId(), auditUserId, CheckManStateEnum.PENDING.getKey(),
                    null, null);
            initMap.put("checkStep" + stepNo, checkStepDto);
            initMap.put("checkMan" + stepNo, checkManDto);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void passActiviti(String officeDetailId,String stepId,String manId,String chekNode,String status) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(stepId,manId,chekNode,status, nowDate, CheckManResultEnum.PASS.getKey());
        //修改任务状态
        ContReviewOfficeDetailDto officeDetailDto = officeDetailService.selectByKey(officeDetailId);
        officeDetailDto.setTaskStatus(ContReviewOfficeDetailEnum.DOWN.getKey());
        officeDetailService.updateNotNull(officeDetailDto);

    }

    public void defaultUpdateStep(String stepId,String manId,String chekNode,String status,Date nowDate,String checkResult){
        //1 更新审核步骤。若为最后一步则更新步骤表
        checkStepService.checkUpdate(nowDate,stepId, checkResult);

        // 2更新处理人
        checkManService.checkUpdate(nowDate,manId, CheckManStateEnum.PENDED.getKey(),chekNode,status);

    }


    @Transactional(rollbackFor = Exception.class)
    public void backActiviti(String officeDetailId,String stepId,String manId,String chekNode,String status) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(stepId,manId,chekNode,status, nowDate, CheckManResultEnum.BACK.getKey());
        ContReviewOfficeDetailDto officeDetailDto = officeDetailService.selectByKey(officeDetailId);
        officeDetailDto.setTaskStatus(ContReviewOfficeDetailEnum.BACK.getKey());
        officeDetailService.updateNotNull(officeDetailDto);


    }

}

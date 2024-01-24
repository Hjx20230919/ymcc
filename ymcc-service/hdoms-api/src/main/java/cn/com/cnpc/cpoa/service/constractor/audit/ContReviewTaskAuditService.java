package cn.com.cnpc.cpoa.service.constractor.audit;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.domain.BizCheckManDto;
import cn.com.cnpc.cpoa.domain.BizCheckStepDto;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTaskDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.enums.contractor.ContReviewTaskEnum;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.wx.WxMessageContent;
import cn.com.cnpc.cpoa.vo.wx.WxMessageData;
import cn.com.cnpc.cpoa.vo.wx.WxMessageVo;
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
public class ContReviewTaskAuditService extends ConstractorAuditService{

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void initActiviti(String userId, String objId) throws Exception {
        Map<String, Object> initMap = getDefaultStepMap(userId, objId, DateUtils.getNowDate());
//        Date nowDate = DateUtils.getNowDate();
//        //沈栩锐机关审核
//        HashMap<String, Object> param = new HashMap<>(16);
//        param.put("userName",userName);
//        List<SysUserDto> sysUserDtos = userService.selectList2(param);
//        String officeId = sysUserDtos.get(0).getUserId();
//        addAudit(nowDate,2,organMsg,userId,objId,officeId,initMap);
//        //罗宇部长审核
//        BizSysConfigDto prjSysConfig = activitiService.getPrjSysConfig(Constants.CONTSPLITUSER);
//        addAudit(nowDate,3,ministerMsg,userId,objId,prjSysConfig.getCfgValue(),initMap);

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
                    CheckStepStateEnum.PASS.getKey(), checkObjId, CheckTypeEnum.TASK.getKey(),createUserId);
            checkStep0.setStepBeginAt(nowDate);
            checkStep0.setStepEndAt(nowDate);

            BizCheckManDto checkMan0 = activitiService.getCheckMan(nowDate, checkStep0.getStepId(), createUserId, CheckManStateEnum.PENDED.getKey(),
                    passMsg, CheckManResultEnum.PASS.getKey());

            initMap.put("checkStep" + stepNo, checkStep0);
            initMap.put("checkMan" + stepNo, checkMan0);
        }else {
            //1 新建审核步骤
            BizCheckStepDto checkStepDto = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), msg, createUserId,
                    null, checkObjId,CheckTypeEnum.TASK.getKey(),auditUserId);
            //2 新增处理人
            BizCheckManDto checkManDto = activitiService.getCheckMan(nowDate, checkStepDto.getStepId(), auditUserId, CheckManStateEnum.PENDING.getKey(),
                    null, null);
            initMap.put("checkStep" + stepNo, checkStepDto);
            initMap.put("checkMan" + stepNo, checkManDto);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());


        //3 最后一步修改基层考评任务状态
        if (activitiService.isLastStep(auditVo)) {
            ContReviewTaskDto contReviewTaskDto = taskService.selectByKey(auditVo.getObjId());
            contReviewTaskDto.setTaskStatus(ContReviewTaskEnum.DOWN.getKey());
            taskService.updateNotNull(contReviewTaskDto);

//            getPassMessage(auditVo.getObjectType(), auditVo.getObjId(), auditVo.getManId(), auditVo.getChekNode(), nowDate, ContractorConstant.ContWXContent.REVIEW_TASK);

        }


    }

    public void defaultUpdateStep(AuditVo auditVo,Date nowDate,String checkResult){
        String auditStatus=auditVo.getAuditStatus();
        String stepId = auditVo.getStepId();
        String chekNode=auditVo.getChekNode();
        String manId = auditVo.getManId();

        //1 更新审核步骤。若为最后一步则更新步骤表
        checkStepService.checkUpdate(nowDate,stepId, checkResult);

        // 2更新处理人
        checkManService.checkUpdate(nowDate,manId, CheckManStateEnum.PENDED.getKey(),chekNode,auditStatus);

    }

    /**
     * @param objType  对象类型 settle deal
     * @param objId    对象主键
     * @param manId    审核人
     * @param chekNode 审核意见
     * @param nowDate  审核时间
     * @return
     * @throws Exception
     */
    @Override
    public void getPassMessage(String objType, String objId,
                               String manId, String chekNode, Date nowDate, String content) throws Exception {

        ContReviewTaskDto contReviewTaskDto = taskService.selectByKey(objId);

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(contReviewTaskDto.getCreateId());


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(passTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的考评任务审批流程有新的结果！\n\n" );
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(content);
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", nowDate));
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setValue("" + deptName + "-" + userName + "【通过】");
        keyword3.setColor("#009900");


        WxMessageContent keyword4 = new WxMessageContent();
        keyword4.setValue(chekNode);
        keyword4.setColor("#00000");

        WxMessageContent remark = new WxMessageContent();
        remark.setValue("\n查看考评任务审批流程，点击下方详情进入！");
        remark.setColor("#0033CC");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setKeyword4(keyword4);
        data.setRemark(remark);
        vo.setData(data);

        //通过-通知承办人
        checkNoticeService.saveNotice(objId, objType, CheckNoticeTypeEnum.PASS.getKey(), user.getWxopenid(), passTemplateId, JSON.marshal(vo));
        return;

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.BACK.getKey());
        ContReviewTaskDto contReviewTaskDto = taskService.selectByKey(auditVo.getObjId());
        contReviewTaskDto.setTaskStatus(ContReviewTaskEnum.BACK.getKey());
        taskService.updateNotNull(contReviewTaskDto);

        //拒绝-通知承办人
//        getRefuseMessage(auditVo.getObjectType(), auditVo.getObjId(),
//                auditVo.getManId(), auditVo.getChekNode(),
//                nowDate, ContractorConstant.ContWXContent.REVIEW_TASK);


    }

    /**
     * @param objType   对象类型 settle deal
     * @param objId     对象主键
     * @param manUserId 审核人
     * @param chekNode  审核意见
     * @param nowDate   审核时间
     * @return
     * @throws Exception
     */
    @Override
    public void getRefuseMessage(String objType, String objId,
                                 String manUserId, String chekNode,
                                 Date nowDate, String content) throws Exception {
        ContReviewTaskDto reviewTaskDto = taskService.selectByKey(objId);

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manUserId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(reviewTaskDto.getCreateId());

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(refuseTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的考评任务审批流程有新的结果！\n\n" );
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(content);
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", nowDate));
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setValue("" + deptName + "-" + userName + "【驳回】");
        keyword3.setColor("#FF0000");


        WxMessageContent keyword4 = new WxMessageContent();
        keyword4.setValue(chekNode);
        keyword4.setColor("#00000");

        WxMessageContent remark = new WxMessageContent();
        remark.setValue("\n查看考评任务审批流程，点击下方详情进入！");
        remark.setColor("#0033CC");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setKeyword4(keyword4);
        data.setRemark(remark);
        vo.setData(data);

        //拒绝-通知承办人
        checkNoticeService.saveNotice(objId, objType, CheckNoticeTypeEnum.REFUSE.getKey(), user.getWxopenid(), refuseTemplateId, JSON.marshal(vo));

        return;
    }
}

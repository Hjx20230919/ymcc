package cn.com.cnpc.cpoa.service.constractor.audit;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.enums.contractor.ContProjectStateEnum;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.wx.WxMessageContent;
import cn.com.cnpc.cpoa.vo.wx.WxMessageData;
import cn.com.cnpc.cpoa.vo.wx.WxMessageVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 7:52
 * @Description:
 */
@Service
public class ContProjectAuditService extends ConstractorAuditService {


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initActiviti(String userId, String objId) throws Exception {
        int i=0;
        List<String> userIds = new ArrayList<>();
        List<String> sepname = new ArrayList<>();

        Map<String, Object> initMap = new HashMap<>();

        Date nowDate = DateUtils.getNowDate();
        SysDeptDto ownerDept = activitiService.getOwnerDept(userId);

        BizSysConfigDto proSplitUser = activitiService.getPrjSysConfig(Constants.PROSPLITUSER);
        BizSysConfigDto contSplitUser = activitiService.getPrjSysConfig(Constants.CONTSPLITUSER);

        String deptManager = ownerDept.getDeptManager();
        //1 获取项目审核 发起人信息
        BizCheckStepDto checkStep1 = activitiService.getCheckStep(nowDate, "0", launchMsg, userId,
                CheckStepStateEnum.PASS.getKey(), objId, CheckTypeEnum.CONTPROJECT.getKey(),userId);
        checkStep1.setStepBeginAt(nowDate);
        checkStep1.setStepEndAt(nowDate);

        BizCheckManDto checkMan1 = activitiService.getCheckMan(nowDate, checkStep1.getStepId(), userId, CheckManStateEnum.PENDED.getKey(),
                passMsg, CheckManResultEnum.PASS.getKey());


        initMap.put("checkStep1", checkStep1);
        initMap.put("checkMan1", checkMan1);
        BizCheckManDto checkMan2 =null;
        BizCheckStepDto checkStep2 =null;
        //2 获取项目审核 部门负责人信息
        if (StringUtils.isNotEmpty(deptManager)) {
            checkStep2 = activitiService.getCheckStep(nowDate, "1", deptMsg, userId,
                    null, objId, CheckTypeEnum.CONTPROJECT.getKey(),deptManager);
            checkStep2.setStepBeginAt(nowDate);

            checkMan2 = activitiService.getCheckMan(nowDate, checkStep2.getStepId(), deptManager, CheckManStateEnum.PENDING.getKey(), null, null);




            SysUserDto userDto = userService.selectByKey(deptManager);
            getPendingMessage(ContractorConstant.AuditService.PROJECTAUDITSERVICE, objId, userDto.getWxopenid()
                    , nowDate, ContractorConstant.ContWXContent.PROJECTCONTENT);
        }

        //3 系统管理员审核（叶剑眉）
        BizCheckStepDto checkStep3 = activitiService.getCheckStep(nowDate, "2", sysMsg, userId,
                null, objId, CheckTypeEnum.CONTPROJECT.getKey(), proSplitUser.getCfgValue());

        BizCheckManDto checkMan3 = activitiService.getCheckMan(nowDate, checkStep3.getStepId(), proSplitUser.getCfgValue(), CheckManStateEnum.PENDING.getKey(), null, null);
        userIds.add(checkMan2.getUserId());
        userIds.add(checkMan3.getUserId());
        sepname.add(checkStep2.getStepName());
        sepname.add(checkStep3.getStepName());
        if (stepnamejudge(sepname)||judge(userIds)){
            if (stepnamejudge(sepname)&&judge(userIds)){
                initMap.put("checkStep2", checkStep2);
                initMap.put("checkMan2", checkMan2);
            }
            userIds.clear();
            sepname.clear();
            initMap.put("checkStep3", checkStep3);
            initMap.put("checkMan3", checkMan3);
        }
        if (StringUtils.isEmpty(deptManager)) {
            SysUserDto userDto = userService.selectByKey(proSplitUser.getCfgValue());
            getPendingMessage(ContractorConstant.AuditService.PROJECTAUDITSERVICE, objId, userDto.getWxopenid()
                    , nowDate, ContractorConstant.ContWXContent.PROJECTCONTENT);
        }


        //4 承包商管理部门审批（罗宇）
        BizCheckStepDto checkStep4 = activitiService.getCheckStep(nowDate, "3", sysMsg, userId,
                null, objId, CheckTypeEnum.CONTPROJECT.getKey(),contSplitUser.getCfgValue());

        BizCheckManDto checkMan4 = activitiService.getCheckMan(nowDate, checkStep4.getStepId(), contSplitUser.getCfgValue(), CheckManStateEnum.PENDING.getKey(), null, null);
        userIds.add(checkMan2.getUserId());
        userIds.add(checkMan3.getUserId());
        userIds.add(checkMan4.getUserId());
        sepname.add(checkStep2.getStepName());
        sepname.add(checkStep3.getStepName());
        sepname.add(checkStep4.getStepName());
        if (stepnamejudge(sepname)||judge(userIds)){
            if (stepnamejudge(sepname)&&judge(userIds)){
                initMap.put("checkStep2", checkStep2);
                initMap.put("checkMan2", checkMan2);
            }
            userIds.clear();
            sepname.clear();
            initMap.put("checkStep4", checkStep4);
            initMap.put("checkMan4", checkMan4);

        }






        activitiService.saveInitMap(initMap);
    }
    public boolean judge(List<String> userIds){
        Set<String> uniqueUserIds = new HashSet<>(userIds);

        // 如果集合大小相同，说明所有userid都是唯一的
        return userIds.size() == uniqueUserIds.size();
    }
    public boolean stepnamejudge(List<String> stepname){
        Set<String> uniqueUserIds = new HashSet<>(stepname);

        // 如果集合大小相同，说明所有userid都是唯一的
        return stepname.size() == uniqueUserIds.size();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void passActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());

        BizContProjectDto contProjectDto = contProjectService.selectByKey(auditVo.getObjId());
        contProjectDto.setProjStateAt(nowDate);

        //3 最后一步则要新增 准入申请等信息
        if (activitiService.isLastStep(auditVo)) {
            Map<String, Object> proApplyEntity = contProjectService.getProApplyEntity(contProjectDto);
            contProjectService.saveProApplyEntity(proApplyEntity);
            contProjectDto.setProjState(ContProjectStateEnum.DOWN.getKey());
            //新增初始化资质信息
            BizContAccessDto contAccess = (BizContAccessDto) proApplyEntity.get("contAccess");
            Map<String, Object> params = new HashMap<>();
            params.put("acceId", contAccess.getAcceId());
            contCreditTiService.getDataFromCreditTi(params);

            getPassMessage(auditVo.getObjectType(), auditVo.getObjId(), auditVo.getManId(), auditVo.getChekNode(), nowDate, ContractorConstant.ContWXContent.PROJECTCONTENT);

        } else {
            //如果不是最后一步，且都是审核完成了。则更新下次合同的开始时间
            updateNextStep(auditVo, nowDate);

        }
        contProjectService.updateNotNull(contProjectDto);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void backActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.BACK.getKey());


        BizContProjectDto contProjectDto = contProjectService.selectByKey(auditVo.getObjId());
        //contProjectService.updateNotNull(contProjectDto);
        //3 退回项目
        contProjectDto.setProjState(ContProjectStateEnum.BACK.getKey());
        contProjectDto.setProjStateAt(nowDate);
        contProjectService.updateNotNull(contProjectDto);

        //拒绝-通知承办人
        getRefuseMessage(auditVo.getObjectType(), auditVo.getObjId(),
                auditVo.getManId(), auditVo.getChekNode(),
                nowDate, ContractorConstant.ContWXContent.PROJECTCONTENT);


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

        BizContProjectDto contProjectDto = contProjectService.selectByKey(objId);

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(contProjectDto.getOwnerId());


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(passTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的项目准入审批流程有新的结果！\n\n名称：" + contProjectDto.getProjContName());
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
        remark.setValue("\n查看项目准入审批流程，点击下方详情进入！");
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


        BizContProjectDto contProjectDto = contProjectService.selectByKey(objId);

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manUserId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(contProjectDto.getOwnerId());

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(refuseTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的项目准入审批流程有新的结果！\n\n名称：" + contProjectDto.getProjContName());
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
        remark.setValue("\n查看项目准入审批流程，点击下方详情进入！");
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


    /**
     * 获取待处通知
     *
     * @return
     * @throws Exception
     */
    @Override
    public void getPendingMessage(String objType, String objId, String openId, Date nowDate, String content) throws Exception {
        BizContProjectDto contProjectDto = contProjectService.selectByKey(objId);


        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fcont%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(pendTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("您有新的项目准入流程申请需要审批！");
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(content);
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(contProjectDto.getProjContName());
//        keyword2.setValue(""+contName+"公司\n"+"项目准入相对人："+dealName+"项目项目准入");
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", nowDate));
        keyword3.setColor("#000000");


        WxMessageContent remark = new WxMessageContent();
        remark.setValue("\n查看项目准入审批流程，点击下方详情进入！");
        remark.setColor("#0033CC");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setRemark(remark);
        vo.setData(data);

        checkNoticeService.saveNotice(objId, objType, CheckNoticeTypeEnum.PENDING.getKey(), openId, pendTemplateId, JSON.marshal(vo));


        return;
    }

}

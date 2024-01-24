package cn.com.cnpc.cpoa.service.project.audit;

import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.domain.BizCheckManDto;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.project.*;
import cn.com.cnpc.cpoa.enums.CheckManResultEnum;
import cn.com.cnpc.cpoa.enums.CheckNoticeTypeEnum;
import cn.com.cnpc.cpoa.enums.project.*;
import cn.com.cnpc.cpoa.service.project.ProjSelContAttachService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.wx.WxMessageContent;
import cn.com.cnpc.cpoa.vo.wx.WxMessageData;
import cn.com.cnpc.cpoa.vo.wx.WxMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/12/5 8:31
 * @Description:
 */
@Service
public class ProjSelContAuditService extends ProjectAuditService {

    @Autowired
    ProjSelContAuditService projSelContAuditService;

    @Autowired
    ProjSelContAttachService projSelContAttachService;

    @Override
    public void initActiviti(String userId, String objId) throws Exception {
        BizProjSelContDto projSelContDto = projSelContService.selectByKey(objId);
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projSelContDto.getProjId());
        //根据金额和选商方式获取当前流程map
        Map<String, String> activitiMap = getServiceMap(projProjectDto.getSelContType(), projProjectDto.getDealValue());
        //更具不同的审核流程的项目阶段获取 步骤信息
        initStepActiviti(userId, objId, ProjPhaseEnum.SELCONT.getKey(), activitiMap);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());

        BizProjSelContDto projSelContDto = projSelContService.selectByKey(auditVo.getObjId());

        //3 同级操作是否还有未处理的
        if (!activitiService.hasNoDealStep(auditVo)) {
            //最后一步则要新增 准入申请等信息
            if (activitiService.isLastStep(auditVo)) {
                projSelContDto.setProjStatus(ProjSelContStatusEnum.DONE.getKey());
                projSelContDto.setProjPhase(ProjPhaseEnum.PURCHASE.getKey());

                BizProjProjectDto projProjectDto = projProjectService.selectByKey(projSelContDto.getProjId());
                projProjectDto.setProjId(projSelContDto.getProjId());
                projProjectDto.setProjPhase(ProjPhaseEnum.PURCHASE.getKey());
                projProjectService.updateNotNull(projProjectDto);

                //新增一条待采购计划结果
                BizProjPurcPlanDto projPurcPlanDto = new BizProjPurcPlanDto();
                projPurcPlanDto.setProjStatus(ProPlanStatusEnum.WAITPLAN.getKey());
                projPurcPlanDto.setPlanId(StringUtils.getUuid32());
                projPurcPlanDto.setCreateAt(DateUtils.getNowDate());
                projPurcPlanDto.setProjPhase(ProjPhaseEnum.PURCHASE.getKey());
                projPurcPlanDto.setProjId(projSelContDto.getProjId());
                projPurcPlanDto.setSelContId(projSelContDto.getSelContId());
                projPurcPlanDto.setPlanNo(projPurcPlanService.getDealNo(ProjectConstant.ProjPhaseType.CGFA));
                projPurcPlanService.save(projPurcPlanDto);

                getPassMessage(auditVo.getObjectType(), auditVo.getObjId(), auditVo.getManId(), auditVo.getChekNode(), nowDate,
                        ProjectConstant.ContWXContent.SELCONTCONTENT);

            } else {
                updateNextStep(auditVo, nowDate);
            }
        }
        projSelContService.updateNotNull(projSelContDto);

    }
//@Override
//@Transactional(rollbackFor = Exception.class)
//public void passActiviti(AuditVo auditVo) throws Exception {
//    Date nowDate = DateUtils.getNowDate();
//
//    defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());
//
//    BizProjSelContDto projSelContDto = projSelContService.selectByKey(auditVo.getObjId());
//
//    //3 同级操作是否还有未处理的
//    if (!activitiService.hasNoDealStep(auditVo)) {
//        //最后一步则要新增 准入申请等信息
//        if (activitiService.isLastStep(auditVo)) {
//            projSelContDto.setProjStatus(ProjSelContStatusEnum.DONE.getKey());
//            projSelContDto.setProjPhase(ProjPhaseEnum.REPURCHASE.getKey());
//
//            BizProjProjectDto projProjectDto = projProjectService.selectByKey(projSelContDto.getProjId());
//            projProjectDto.setProjId(projSelContDto.getProjId());
//            projProjectDto.setProjPhase(ProjPhaseEnum.REPURCHASE.getKey());
//            projProjectService.updateNotNull(projProjectDto);
//
//            //新增待采购结果记录
//            BizProjPurcResultDto projPurcResultDto = new BizProjPurcResultDto();
//            projPurcResultDto.setProjStatus(ProResultStatusEnum.WAITRESULT.getKey());
//            projPurcResultDto.setResultId(StringUtils.getUuid32());
//            projPurcResultDto.setCreateAt(DateUtils.getNowDate());
//            projPurcResultDto.setProjPhase(ProjPhaseEnum.REPURCHASE.getKey());
//            projPurcResultDto.setProjId(projProjectDto.getProjId());
//            projPurcResultDto.setSelContId(projSelContDto.getSelContId());
////                projPurcResultDto.setPlanId(projPurcPlanDto.getPlanId());
//            projPurcResultDto.setPlanNo(projPurcResultService.getDealNo(ProjectConstant.ProjPhaseType.CGJG));
//
//            projPurcResultService.save(projPurcResultDto);
//
//            getPassMessage(auditVo.getObjectType(), auditVo.getObjId(), auditVo.getManId(), auditVo.getChekNode(), nowDate,
//                    ProjectConstant.ContWXContent.PURCHASECONTENT);
//
//        } else {
//            updateNextStep(auditVo, nowDate);
//        }
//    }
//    projSelContService.updateNotNull(projSelContDto);
//
//}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.BACK.getKey());

        //退回项目
        BizProjSelContDto projSelContDto = projSelContService.selectByKey(auditVo.getObjId());
        projSelContDto.setProjStatus(ProProjectStatusEnum.BACK.getKey());
        projSelContService.updateNotNull(projSelContDto);

        //拒绝-通知承办人
        getRefuseMessage(auditVo.getObjectType(), auditVo.getObjId(),
                auditVo.getManId(), auditVo.getChekNode(),
                nowDate, ProjectConstant.ContWXContent.SELCONTCONTENT);
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
        BizProjSelContDto projSelContDto = projSelContService.selectByKey(objId);
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projSelContDto.getProjId());

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewselcont%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(projSelContDto.getCreateId());


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(passTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的项目选商审批流程有新的结果！\n\n名称：" + projProjectDto.getProjName());
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
        remark.setValue("\n查看项目选商审批流程，点击下方详情进入！");
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


        BizProjSelContDto projSelContDto = projSelContService.selectByKey(objId);
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projSelContDto.getProjId());

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewselcont%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manUserId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(projSelContDto.getCreateId());

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(refuseTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的项目选商审批流程有新的结果！\n\n名称：" + projProjectDto.getProjName());
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
        remark.setValue("\n查看项目选商审批流程，点击下方详情进入！");
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
        BizProjSelContDto projSelContDto = projSelContService.selectByKey(objId);
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projSelContDto.getProjId());

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(pendTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("您有新的项目选商流程申请需要审批！");
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(content);
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(projProjectDto.getProjName());
//        keyword2.setValue(""+contName+"公司\n"+"项目准入相对人："+dealName+"项目项目准入");
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", nowDate));
        keyword3.setColor("#000000");


        WxMessageContent remark = new WxMessageContent();
        remark.setValue("\n查看项目选商审批流程，点击下方详情进入！");
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

    @Override
    public void deleteBack(String id) {
        Map<String, List> deleteList = getDeleteActiviti(id);
        deleteList.putAll(getDeleteFile(id));

        //重新新增待选商记录
        BizProjSelContDto projSelContDto = new BizProjSelContDto();
        projSelContDto.setSelContId(StringUtils.getUuid32());
        projSelContDto.setProjId(projSelContService.selectByKey(id).getProjId());
        projSelContDto.setProjStatus(ProjSelContStatusEnum.WAITSELCONT.getKey());
        projSelContDto.setProjPhase(ProjPhaseEnum.SELCONT.getKey());
        projSelContDto.setCreateAt(DateUtils.getNowDate());

        projSelContAuditService.deleteBackChain(id,projSelContDto, deleteList.get("manIds"), deleteList.get("stepIds"),
                deleteList.get("attachIds"), getDeleteAttachAndIds(id), getDeleteProjContList(id));
    }

    /**
     * 0 审核人 1 审核步骤 2 附件 3 中间表 4 选商列表
     *
     * @param deleteList
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBackChain(String selContId,  BizProjSelContDto projSelContDto,List<Object>... deleteList) {

        List<Object> manIds = deleteList[0];
        List<Object> stepIds = deleteList[1];
        List<Object> attachIds = deleteList[2];
        List<Object> selContAttachIds = deleteList[3];
        List<Object> contListIds = deleteList[4];

        projSelContService.delete(selContId);
        checkStepService.deleteList(stepIds);
        checkManService.deleteList(manIds);
        for (Object attachId : attachIds) {
            attachService.deleteById(String.valueOf(attachId),"");
        }
        projSelContAttachService.deleteList(selContAttachIds);

        projContListService.deleteList(contListIds);

        projSelContService.save(projSelContDto);

    }


    private List<Object> getDeleteAttachAndIds(String selContId) {
        List<Object> selContAttachIds = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("selContId", selContId);
        List<BizProjSelContAttachDto> projSelContAttachDtos = projSelContAttachService.selectProjSelContAttachDto(params);

        for (BizProjSelContAttachDto projSelContAttachDto : projSelContAttachDtos) {
            selContAttachIds.add(projSelContAttachDto.getId());
        }
        return selContAttachIds;
    }

}

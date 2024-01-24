package cn.com.cnpc.cpoa.service.project.audit;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.project.*;
import cn.com.cnpc.cpoa.enums.CheckManResultEnum;
import cn.com.cnpc.cpoa.enums.CheckManStateEnum;
import cn.com.cnpc.cpoa.enums.CheckNoticeTypeEnum;
import cn.com.cnpc.cpoa.enums.project.*;
import cn.com.cnpc.cpoa.service.project.ProjProjectAttachService;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
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
public class ProjProjectAuditService extends ProjectAuditService {


    @Autowired
    ProjProjectService projProjectService;
    @Autowired
    ProjProjectAttachService projProjectAttachService;
    @Autowired
    ProjProjectAuditService projProjectAuditService;

    @Override
    public void initActiviti(String userId, String objId) throws Exception {
        //立项流程 走的是合同分发那一套
        Map<String, Object> initMap = getDefualtStepMap(userId, objId, ProjPhaseEnum.PROPROJECT.getKey(), DateUtils.getNowDate());
        int stepNo = (int) initMap.get("stepNo");

        //3 获取项目审核 企管信息
        Date nowDate = DateUtils.getNowDate();
        BizSysConfigDto prjSysConfig = activitiService.getPrjSysConfig(Constants.PROSPLITUSER);

        //1 新建审核步骤
        BizCheckStepDto checkStepDto = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), sysMsg, userId,
                null, objId, ProjPhaseEnum.PROPROJECT.getKey(),prjSysConfig.getCfgValue());
        checkStepDto.setStepSplit(1);
        //2 新增处理人
        BizCheckManDto checkManDto = activitiService.getCheckMan(nowDate, checkStepDto.getStepId(), prjSysConfig.getCfgValue(), CheckManStateEnum.PENDING.getKey(),
                null, null);

        initMap.put("checkStep" + stepNo, checkStepDto);
        initMap.put("checkMan" + stepNo, checkManDto);
        stepNo++;
        //log.info("initMap"+JSONObject.toJSONString(initMap));
        activitiService.saveProInitMap(initMap, stepNo);
    }

//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void passActiviti(AuditVo auditVo) throws Exception {
//        Date nowDate = DateUtils.getNowDate();
//
//        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());
//
//        BizProjProjectDto projProjectDto = projProjectService.selectByKey(auditVo.getObjId());
//
//        //3 同级操作是否还有未处理的
//        if (!activitiService.hasNoDealStep(auditVo)) {
//            //最后一步则要新增 准入申请等信息
//            if (activitiService.isLastStep(auditVo)) {
//                projProjectDto.setProjStatus(ProProjectStatusEnum.DONE.getKey());
//                /**
//                 *  立项竞争性谈判 进入选商阶段
//                 *  可不招标 单一来源 到采购结果
//                 *  公开招标 直接结束
//                 *
//                 */
//                if (SelContTypeEnum.BUILDPROJECTNEGOTIATION.getKey().equals(projProjectDto.getSelContType())) {
//                    //更新状态
//                    projProjectDto.setProjPhase(ProjPhaseEnum.SELCONT.getKey());
//
//                    //新增待选商记录
//                    BizProjSelContDto projSelContDto = new BizProjSelContDto();
//                    projSelContDto.setSelContId(StringUtils.getUuid32());
//                    projSelContDto.setProjId(auditVo.getObjId());
//                    projSelContDto.setProjStatus(ProjSelContStatusEnum.WAITSELCONT.getKey());
//                    projSelContDto.setProjPhase(ProjPhaseEnum.SELCONT.getKey());
//                    projSelContDto.setCreateAt(DateUtils.getNowDate());
//
//                    projSelContService.save(projSelContDto);
//                } else if (SelContTypeEnum.NOTENDER.getKey().equals(projProjectDto.getSelContType())
//                        || SelContTypeEnum.BUILDPROJECTSINGLE.getKey().equals(projProjectDto.getSelContType())
//                        || SelContTypeEnum.BUILDPROJECTINSIDE.getKey().equals(projProjectDto.getSelContType())) {
//                    //更新状态
//                    projProjectDto.setProjPhase(ProjPhaseEnum.REPURCHASE.getKey());
//
//                    //新增一条待采购计划结果
////                    BizProjPurcResultDto projPurcPlanDto = new BizProjPurcResultDto();
////                    projPurcPlanDto.setProjStatus(ProPlanStatusEnum.WAITPLAN.getKey());
////                    projPurcPlanDto.setPlanId(StringUtils.getUuid32());
////                    projPurcPlanDto.setCreateAt(DateUtils.getNowDate());
////                    projPurcPlanDto.setProjPhase(ProjPhaseEnum.REPURCHASE.getKey());
////                    projPurcPlanDto.setProjId(auditVo.getObjId());
////                    //   projPurcPlanDto.setSelContId(projSelContDto.getSelContId());
////                    projPurcPlanDto.setPlanNo(projPurcResultService.getDealNo(ProjectConstant.ProjPhaseType.CGFA));
////                    projPurcResultService.save(projPurcPlanDto);
//                    BizProjPurcResultDto projPurcResultDto = new BizProjPurcResultDto();
//                    projPurcResultDto.setProjStatus(ProResultStatusEnum.WAITRESULT.getKey());
//                    projPurcResultDto.setResultId(StringUtils.getUuid32());
//                    projPurcResultDto.setCreateAt(DateUtils.getNowDate());
//                    projPurcResultDto.setProjPhase(ProjPhaseEnum.REPURCHASE.getKey());
//                    projPurcResultDto.setProjId(auditVo.getObjId());
////                    projPurcResultDto.setPlanId(projPurcPlanDto.getPlanId());
//                    projPurcResultDto.setPlanNo(projPurcResultService.getDealNo(ProjectConstant.ProjPhaseType.CGJG));
//
//                    projPurcResultService.save(projPurcResultDto);
//
//                } else {
//                    projProjectDto.setProjPhase(ProjPhaseEnum.COMPLETE.getKey());
//                }
//
//                getPassMessage(auditVo.getObjectType(), auditVo.getObjId(), auditVo.getManId(), auditVo.getChekNode(), nowDate, ProjectConstant.ContWXContent.PROPROJECTCONTENT);
//
//            } else {
//                updateNextStep(auditVo, nowDate);
//            }
//        }
//        projProjectService.updateNotNull(projProjectDto);
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());

        BizProjProjectDto projProjectDto = projProjectService.selectByKey(auditVo.getObjId());

        //3 同级操作是否还有未处理的
        if (!activitiService.hasNoDealStep(auditVo)) {
            //最后一步则要新增 准入申请等信息
            if (activitiService.isLastStep(auditVo)) {
                projProjectDto.setProjStatus(ProProjectStatusEnum.DONE.getKey());
                /**
                 *  立项竞争性谈判 进入选商阶段
                 *  可不招标 单一来源 到采购结果
                 *  公开招标 直接结束
                 *
                 */
                if (SelContTypeEnum.BUILDPROJECTNEGOTIATION.getKey().equals(projProjectDto.getSelContType())) {
                    //更新状态
                    projProjectDto.setProjPhase(ProjPhaseEnum.SELCONT.getKey());

                    //新增待选商记录
                    BizProjSelContDto projSelContDto = new BizProjSelContDto();
                    projSelContDto.setSelContId(StringUtils.getUuid32());
                    projSelContDto.setProjId(auditVo.getObjId());
                    projSelContDto.setProjStatus(ProjSelContStatusEnum.WAITSELCONT.getKey());
                    projSelContDto.setProjPhase(ProjPhaseEnum.SELCONT.getKey());
                    projSelContDto.setCreateAt(DateUtils.getNowDate());

                    projSelContService.save(projSelContDto);
                } else if (SelContTypeEnum.NOTENDER.getKey().equals(projProjectDto.getSelContType())
                        || SelContTypeEnum.BUILDPROJECTSINGLE.getKey().equals(projProjectDto.getSelContType())
                        || SelContTypeEnum.BUILDPROJECTINSIDE.getKey().equals(projProjectDto.getSelContType())) {
                    //更新状态
                    projProjectDto.setProjPhase(ProjPhaseEnum.PURCHASE.getKey());

                    //新增一条待采购计划结果
                    BizProjPurcPlanDto projPurcPlanDto = new BizProjPurcPlanDto();
                    projPurcPlanDto.setProjStatus(ProPlanStatusEnum.WAITPLAN.getKey());
                    projPurcPlanDto.setPlanId(StringUtils.getUuid32());
                    projPurcPlanDto.setCreateAt(DateUtils.getNowDate());
                    projPurcPlanDto.setProjPhase(ProjPhaseEnum.PURCHASE.getKey());
                    projPurcPlanDto.setProjId(auditVo.getObjId());
                    //   projPurcPlanDto.setSelContId(projSelContDto.getSelContId());
                    projPurcPlanDto.setPlanNo(projPurcPlanService.getDealNo(ProjectConstant.ProjPhaseType.CGFA));
                    projPurcPlanService.save(projPurcPlanDto);

                } else {
                    projProjectDto.setProjPhase(ProjPhaseEnum.COMPLETE.getKey());
                }

                getPassMessage(auditVo.getObjectType(), auditVo.getObjId(), auditVo.getManId(), auditVo.getChekNode(), nowDate, ProjectConstant.ContWXContent.PROPROJECTCONTENT);

            } else {
                updateNextStep(auditVo, nowDate);
            }
        }
        projProjectService.updateNotNull(projProjectDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.BACK.getKey());

        //退回项目
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(auditVo.getObjId());
        projProjectDto.setProjStatus(ProProjectStatusEnum.BACK.getKey());
        projProjectService.updateNotNull(projProjectDto);

        //拒绝-通知承办人
        getRefuseMessage(auditVo.getObjectType(), auditVo.getObjId(),
                auditVo.getManId(), auditVo.getChekNode(),
                nowDate, ProjectConstant.ContWXContent.PROPROJECTCONTENT);
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

        BizProjProjectDto projProjectDto = projProjectService.selectByKey(objId);

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewproproject%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(projProjectDto.getCreateId());


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(passTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的项目立项审批流程有新的结果！\n\n名称：" + projProjectDto.getProjName());
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
        remark.setValue("\n查看项目立项审批流程，点击下方详情进入！");
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


        BizProjProjectDto projProjectDto = projProjectService.selectByKey(objId);

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewproproject%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manUserId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(projProjectDto.getCreateId());

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(refuseTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的项目立项审批流程有新的结果！\n\n名称：" + projProjectDto.getProjName());
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
        remark.setValue("\n查看项目立项审批流程，点击下方详情进入！");
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
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(objId);

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(pendTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("您有新的项目立项流程申请需要审批！");
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
        remark.setValue("\n查看项目立项审批流程，点击下方详情进入！");
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

        projProjectAuditService.deleteBackChain(id, deleteList.get("manIds"), deleteList.get("stepIds"),
                deleteList.get("attachIds"), getDeleteAttachAndIds(id), getDeleteProjContList(id));
    }

    /**
     * 0 审核人 1 审核步骤 2 附件 3 中间表 4 选商列表
     *
     * @param deleteList
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBackChain(String projId, List<Object>... deleteList) {

        List<Object> manIds = deleteList[0];
        List<Object> stepIds = deleteList[1];
        List<Object> attachIds = deleteList[2];
        List<Object> projProjectAttachIds = deleteList[3];
        List<Object> contListIds = deleteList[4];

        projProjectService.delete(projId);
        checkStepService.deleteList(stepIds);
        checkManService.deleteList(manIds);
        for (Object attachId : attachIds) {
            attachService.deleteById(String.valueOf(attachId),"");
        }
        projProjectAttachService.deleteList(projProjectAttachIds);

        projContListService.deleteList(contListIds);

    }


    private List<Object> getDeleteAttachAndIds(String projId) {
        List<Object> projProjectAttachIds = new ArrayList<>();

        Map<String, Object> params = new HashMap<>();
        params.put("projId", projId);
        List<BizProjProjectAttachDto> projProjectAttachDtos = projProjectAttachService.selectProjProjectAttachDto(params);
        for (BizProjProjectAttachDto projProjectAttachDto : projProjectAttachDtos) {
            projProjectAttachIds.add(projProjectAttachDto.getId());
        }
        return projProjectAttachIds;
    }

}

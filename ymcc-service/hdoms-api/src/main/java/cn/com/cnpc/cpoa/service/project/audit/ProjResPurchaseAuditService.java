package cn.com.cnpc.cpoa.service.project.audit;

import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.domain.BizCheckManDto;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.project.*;
import cn.com.cnpc.cpoa.enums.CheckManResultEnum;
import cn.com.cnpc.cpoa.enums.CheckNoticeTypeEnum;
import cn.com.cnpc.cpoa.enums.project.ProProjectStatusEnum;
import cn.com.cnpc.cpoa.enums.project.ProResultStatusEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.service.project.ProjProjectService;
import cn.com.cnpc.cpoa.service.project.ProjPurcResultAttachService;
import cn.com.cnpc.cpoa.service.project.ProjResultListService;
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
public class ProjResPurchaseAuditService extends ProjectAuditService {


    @Autowired
    ProjResultListService projResultListService;

    @Autowired
    ProjPurcResultAttachService projPurcResultAttachService;

    @Autowired
    private ProjProjectService projProjectService;

    @Override
    public void initActiviti(String userId, String objId) throws Exception {
        BizProjPurcResultDto projPurcResultDto = projPurcResultService.selectByKey(objId);
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projPurcResultDto.getProjId());
        //根据金额和选商方式获取当前流程map
        Map<String, String> activitiMap = getServiceMap(projProjectDto.getSelContType(), projProjectDto.getDealValue());
        //更具不同的审核流程的项目阶段获取 步骤信息
        initStepActiviti(userId, objId, ProjPhaseEnum.REPURCHASE.getKey(), activitiMap);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void passActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());

        BizProjPurcResultDto projPurcResultDto = projPurcResultService.selectByKey(auditVo.getObjId());

        //3 同级操作是否还有未处理的
        if (!activitiService.hasNoDealStep(auditVo)) {
            //最后一步则要新增 准入申请等信息
            if (activitiService.isLastStep(auditVo)) {
                projPurcResultDto.setProjStatus(ProProjectStatusEnum.DONE.getKey());
                projPurcResultDto.setProjPhase(ProjPhaseEnum.COMPLETE.getKey());

//                BizProjPurcPlanDto projPurcPlanDto = projPurcPlanService.selectByKey(projPurcResultDto.getPlanId());
//                projPurcPlanDto.setProjPhase(ProjPhaseEnum.COMPLETE.getKey());
//                projPurcPlanService.updateNotNull(projPurcPlanDto);

                BizProjProjectDto projProjectDto = projProjectService.selectByKey(projPurcResultDto.getProjId());
                projProjectDto.setProjPhase(ProjPhaseEnum.COMPLETE.getKey());
                projProjectService.updateNotNull(projProjectDto);

                if (StringUtils.isNotEmpty(projPurcResultDto.getSelContId())) {
                    BizProjSelContDto projSelContDto = projSelContService.selectByKey(projPurcResultDto.getSelContId());
                    projSelContDto.setProjPhase(ProjPhaseEnum.COMPLETE.getKey());
                    projSelContService.updateNotNull(projSelContDto);
                }
                getPassMessage(auditVo.getObjectType(), auditVo.getObjId(), auditVo.getManId(), auditVo.getChekNode(), nowDate,
                        ProjectConstant.ContWXContent.REPURCHASECONTENT);
            } else {
                updateNextStep(auditVo, nowDate);
            }
        }
        projPurcResultService.updateNotNull(projPurcResultDto);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void backActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.BACK.getKey());

        //退回项目
        BizProjPurcResultDto projPurcResultDto = projPurcResultService.selectByKey(auditVo.getObjId());
        projPurcResultDto.setProjStatus(ProProjectStatusEnum.BACK.getKey());
        projPurcResultService.updateNotNull(projPurcResultDto);


        //拒绝-通知承办人
        getRefuseMessage(auditVo.getObjectType(), auditVo.getObjId(),
                auditVo.getManId(), auditVo.getChekNode(),
                nowDate, ProjectConstant.ContWXContent.REPURCHASECONTENT);
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
        BizProjPurcResultDto projPurcResultDto = projPurcResultService.selectByKey(objId);
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projPurcResultDto.getProjId());

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewpurchaseresult%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(projPurcResultDto.getCreateId());


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(passTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的项目采购结果审批流程有新的结果！\n\n名称：" + projProjectDto.getProjName());
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
        remark.setValue("\n查看项目采购结果审批流程，点击下方详情进入！");
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


        BizProjPurcResultDto projPurcResultDto = projPurcResultService.selectByKey(objId);
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projPurcResultDto.getProjId());

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewpurchaseresult%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manUserId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(projPurcResultDto.getCreateId());

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(refuseTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的项目采购结果审批流程有新的结果！\n\n名称：" + projProjectDto.getProjName());
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
        remark.setValue("\n查看项目采购结果审批流程，点击下方详情进入！");
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
        BizProjPurcResultDto projPurcResultDto = projPurcResultService.selectByKey(objId);
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projPurcResultDto.getProjId());

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(pendTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("您有新的项目采购结果流程申请需要审批！");
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
        remark.setValue("\n查看项目采购结果审批流程，点击下方详情进入！");
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

        deleteList.putAll(getOtherDeleteInfo(id));

        //新增待采购结果记录
        BizProjPurcResultDto projPurcResultDto1 = projPurcResultService.selectByKey(id);
        BizProjPurcResultDto projPurcResultDto = new BizProjPurcResultDto();
        projPurcResultDto.setProjStatus(ProResultStatusEnum.WAITRESULT.getKey());
        projPurcResultDto.setResultId(StringUtils.getUuid32());
        projPurcResultDto.setCreateAt(DateUtils.getNowDate());
        projPurcResultDto.setProjPhase(ProjPhaseEnum.REPURCHASE.getKey());
        projPurcResultDto.setProjId(projPurcResultDto1.getProjId());
        projPurcResultDto.setPlanId(projPurcResultDto1.getPlanId());
        projPurcResultDto.setPlanNo(projPurcResultService.getDealNo(ProjectConstant.ProjPhaseType.CGJG));

        projResPurchaseAuditService.deleteBackChain(id,projPurcResultDto, deleteList.get("manIds"), deleteList.get("stepIds"),
                deleteList.get("attachIds"), getDeleteAttachAndIds(id), deleteList.get("resListKeys"));
    }

    private Map<String, List> getOtherDeleteInfo(String id) {
        Map<String, List> listMap = new HashMap<>();

        Map<String, Object> params = new HashMap<>();
        params.put("resultId", id);
        List<BizProjResultListDto> resultListDtos = projResultListService.selectResultList(params);
        List<Object> resListKeys = new ArrayList<>();
        for (BizProjResultListDto projResultListDto : resultListDtos) {
            resListKeys.add(projResultListDto.getResultListId());
        }

        listMap.put("resListKeys", resListKeys);
        return listMap;
    }

    /**
     * 0 审核人 1 审核步骤 2 附件 3 中间表 4 计划明细
     *
     * @param deleteList
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteBackChain(String resultId,BizProjPurcResultDto projPurcResultDto, List<Object>... deleteList) {

        List<Object> manIds = deleteList[0];
        List<Object> stepIds = deleteList[1];
        List<Object> attachIds = deleteList[2];
        List<Object> projPurcResultAttachIds = deleteList[3];
        List<Object> resListKeys = deleteList[4];

        projPurcResultService.delete(resultId);
        checkStepService.deleteList(stepIds);
        checkManService.deleteList(manIds);
        for (Object attachId : attachIds) {
            attachService.deleteById(String.valueOf(attachId),"");
        }

        projPurcResultAttachService.deleteList(projPurcResultAttachIds);

        projResultListService.deleteList(resListKeys);

        projPurcResultService.save(projPurcResultDto);
    }


    private List<Object> getDeleteAttachAndIds(String resultId) {
        List<Object> projPurcResultAttachIds = new ArrayList<>();

        Map<String, Object> param = new HashMap<>();
        param.put("resultId", resultId);
        List<BizProjPurcResultAttachDto> projPurcResultAttachDtos = projPurcResultAttachService.selectProjPurcResultAttachDto(param);

        for (BizProjPurcResultAttachDto projPurcResultAttachDto : projPurcResultAttachDtos) {
            projPurcResultAttachIds.add(projPurcResultAttachDto.getId());
        }
        return projPurcResultAttachIds;
    }


}

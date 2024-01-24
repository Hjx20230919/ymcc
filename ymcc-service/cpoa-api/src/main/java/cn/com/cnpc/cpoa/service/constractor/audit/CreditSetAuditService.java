package cn.com.cnpc.cpoa.service.constractor.audit;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditSetDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.enums.contractor.CreditStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.SetStateEnum;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.wx.WxMessageContent;
import cn.com.cnpc.cpoa.vo.wx.WxMessageData;
import cn.com.cnpc.cpoa.vo.wx.WxMessageVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/20 19:33
 * @Description:
 */
@Service
public class CreditSetAuditService extends ConstractorAuditService {

    private static Logger logger = LoggerFactory.getLogger(CreditSetAuditService.class);

    @Transactional(rollbackFor = Exception.class)
    @Override
    public synchronized void initActiviti(String userId, String objId) throws Exception {

        BizContCreditSetDto contCreditSetDto = contCreditSetService.selectByKey(objId);
        String setState = contCreditSetDto.getSetState();
        //logger.info("SetStateEnum.AUDITING.getKey()"+SetStateEnum.AUDITING.getKey()+"setState"+setState);
        if(SetStateEnum.AUDITING.getKey().equals(setState)){
            throw new AppException("当前流程已提交，请勿重复操作");
        }


        Map<String, Object> initMap = new HashMap<>();

        String cfgCode = Constants.PROSPLITUSER;
        Date nowDate = DateUtils.getNowDate();
        //SysDeptDto ownerDept = activitiService.getOwnerDept(userId);
        BizSysConfigDto prjSysConfig = activitiService.getPrjSysConfig(cfgCode);
        //String deptManager = ownerDept.getDeptManager();

        //1 获取项目审核 发起人信息
        if (StringUtils.isNotEmpty(userId) && !ContractorConstant.UPLOAD_USER.equals(userId)) {
            BizCheckStepDto checkStep1 = activitiService.getCheckStep(nowDate, "0", launchMsg, userId,
                    CheckStepStateEnum.PASS.getKey(), objId, CheckTypeEnum.CREDITSET.getKey(), userId);
            checkStep1.setStepBeginAt(nowDate);
            checkStep1.setStepEndAt(nowDate);

            BizCheckManDto checkMan1 = activitiService.getCheckMan(nowDate, checkStep1.getStepId(), userId, CheckManStateEnum.PENDED.getKey(),
                    passMsg, CheckManResultEnum.PASS.getKey());
            initMap.put("checkStep1", checkStep1);
            initMap.put("checkMan1", checkMan1);
        } else {
            userId = prjSysConfig.getCfgValue();
        }

        //2 获取项目审核 部门负责人信息
//        if (StringUtils.isNotEmpty(deptManager)) {
//            BizCheckStepDto checkStep2 = activitiService.getCheckStep(nowDate, "1", deptMsg, userId,
//                    null, objId, CheckTypeEnum.CREDITSET.getKey());
//            checkStep2.setStepBeginAt(nowDate);
//
//            BizCheckManDto checkMan2 = activitiService.getCheckMan(nowDate, checkStep2.getStepId(), deptManager, CheckManStateEnum.PENDING.getKey(), null, null);
//
//            initMap.put("checkStep2", checkStep2);
//            initMap.put("checkMan2", checkMan2);
//
//            SysUserDto userDto = userService.selectByKey(deptManager);

//        }

        //3 获取项目审核 企管信息     20200112: 资质变更直接到承包商管理员
        BizCheckStepDto checkStep3 = activitiService.getCheckStep(nowDate, "1", sysMsg, userId,
                null, objId, CheckTypeEnum.CREDITSET.getKey(), prjSysConfig.getCfgValue());
        checkStep3.setStepSplit(1);
        checkStep3.setStepBeginAt(nowDate);

        BizCheckManDto checkMan3 = activitiService.getCheckMan(nowDate, checkStep3.getStepId(), prjSysConfig.getCfgValue(), CheckManStateEnum.PENDING.getKey(), null, null);
        SysUserDto userDto = userService.selectByKey(prjSysConfig.getCfgValue());
        getPendingMessage(ContractorConstant.AuditService.CREDITSETSERVIVCE, objId, userDto.getWxopenid()
                , nowDate, ContractorConstant.ContWXContent.CREDITSETSCONTENT);
//        if (StringUtils.isEmpty(deptManager)) {
//            SysUserDto userDto = userService.selectByKey(prjSysConfig.getCfgValue());
//            getPendingMessage(ContractorConstant.AuditService.CREDITSETSERVIVCE, objId, userDto.getWxopenid()
//                    , nowDate, ContractorConstant.contWXContent.CREDITSETSCONTENT);
//        }


        initMap.put("checkStep3", checkStep3);
        initMap.put("checkMan3", checkMan3);

        activitiService.saveInitMap(initMap);
    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void passActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());

        BizContCreditSetDto contCreditSetDto = contCreditSetService.selectByKey(auditVo.getObjId());
        contCreditSetDto.setSetStateAt(DateUtils.getNowDate());

        //3 最后一步则要新增 准入申请等信息
        if (!activitiService.hasNoDealStep(auditVo)) {
            if (activitiService.isLastStep(auditVo)) {
                contCreditSetDto.setSetState(SetStateEnum.DONE.getKey());
                // contCreditAttachService.passCreditAttach(auditVo.getObjId());
                //TODO 若当前承包商下所有的资质有效时间都已超过当前时间，则更新当前项目状态为使用中
                BizContAccessDto contAccessDto = contAccessService.selectByKey(contCreditSetDto.getAcceId());
                //如果是导入的承包商，则需要更新资质为生效
                if (StringUtils.isEmpty(contAccessDto.getOwnerId())) {
                    //更新资质为 生效
                    contCreditService.updateContCreditByAcceId(contAccessDto.getAcceId(), CreditStateEnum.VALID.getKey());
                }


                getPassMessage(auditVo.getObjectType(), auditVo.getObjId(), auditVo.getManId(), auditVo.getChekNode(), nowDate, ContractorConstant.ContWXContent.CREDITSETSCONTENT);

            } else {
                updateNextStep(auditVo, nowDate);
            }
        }
        contCreditSetService.updateNotNull(contCreditSetDto);

    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void backActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.BACK.getKey());

        //退回项目
        BizContCreditSetDto contCreditSetDto = contCreditSetService.selectByKey(auditVo.getObjId());
        contCreditSetDto.setSetStateAt(DateUtils.getNowDate());
        contCreditSetDto.setSetState(SetStateEnum.BACK.getKey());
        contCreditSetService.updateNotNull(contCreditSetDto);

        // contCreditAttachService.backCreditAttach(auditVo.getObjId());

        //拒绝-通知承办人
        getRefuseMessage(auditVo.getObjectType(), auditVo.getObjId(),
                auditVo.getManId(), auditVo.getChekNode(),
                nowDate, ContractorConstant.ContWXContent.CREDITSETSCONTENT);
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

        BizContCreditSetDto contCreditSetDto = contCreditSetService.selectByKey(objId);
        BizContAccessDto contAccessDto = contAccessService.selectByKey(contCreditSetDto.getAcceId());

        BizContProjectDto contProjectDto = contProjectService.selectByKey(contAccessDto.getProjId());

        BizSysConfigDto proPurchaseUser = activitiService.getPrjSysConfig(Constants.PROSPLITUSER);

        String ownerId = proPurchaseUser.getCfgValue();

        if (StringUtils.isNotEmpty(ownerId)) {
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewcont%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

            BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
            SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());

            String deptId = userDto.getDeptId();
            SysDeptDto deptDto = deptService.selectByKey(deptId);
            String deptName = deptDto.getDeptName();
            String userName = userDto.getUserName();

            SysUserDto user = userService.selectByKey(ownerId);

            WxMessageVo vo = new WxMessageVo();
            vo.setTouser(user.getWxopenid()); //承办人

            vo.setTemplate_id(passTemplateId);
            vo.setUrl(url);
            WxMessageData data = new WxMessageData();

            WxMessageContent first = new WxMessageContent();
            first.setValue("你提交的资质变更审批流程有新的结果！\n\n名称：" + contProjectDto.getProjContName());
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
            remark.setValue("\n查看资质变更审批流程，点击下方详情进入！");
            remark.setColor("#0033CC");

            data.setFirst(first);
            data.setKeyword1(keyword1);
            data.setKeyword2(keyword2);
            data.setKeyword3(keyword3);
            data.setKeyword4(keyword4);
            data.setRemark(remark);
            vo.setData(data);

            checkNoticeService.saveNotice(objId, objType, CheckNoticeTypeEnum.PASS.getKey(), userDto.getWxopenid(), passTemplateId, JSON.marshal(vo));
        }


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


        BizContCreditSetDto contCreditSetDto = contCreditSetService.selectByKey(objId);
        BizContAccessDto contAccessDto = contAccessService.selectByKey(contCreditSetDto.getAcceId());

        BizContProjectDto contProjectDto = contProjectService.selectByKey(contAccessDto.getProjId());
        BizSysConfigDto proPurchaseUser = activitiService.getPrjSysConfig(Constants.PROSPLITUSER);

        String ownerId = proPurchaseUser.getCfgValue();

        if (StringUtils.isNotEmpty(ownerId)) {
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewcont%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

            BizCheckManDto bizCheckManDto = checkManService.selectByKey(manUserId);
            SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
            String deptId = userDto.getDeptId();
            SysDeptDto deptDto = deptService.selectByKey(deptId);
            String deptName = deptDto.getDeptName();
            String userName = userDto.getUserName();

            //承办人
            SysUserDto user = userService.selectByKey(ownerId);

            WxMessageVo vo = new WxMessageVo();
            vo.setTouser(user.getWxopenid());
            vo.setTemplate_id(refuseTemplateId);
            vo.setUrl(url);
            WxMessageData data = new WxMessageData();

            WxMessageContent first = new WxMessageContent();
            first.setValue("你提交的资质变更审批流程有新的结果！\n\n名称：" + contProjectDto.getProjContName());
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
            remark.setValue("\n查看资质变更审批流程，点击下方详情进入！");
            remark.setColor("#0033CC");

            data.setFirst(first);
            data.setKeyword1(keyword1);
            data.setKeyword2(keyword2);
            data.setKeyword3(keyword3);
            data.setKeyword4(keyword4);
            data.setRemark(remark);
            vo.setData(data);

            checkNoticeService.saveNotice(objId, objType, CheckNoticeTypeEnum.REFUSE.getKey(), user.getWxopenid(), refuseTemplateId, JSON.marshal(vo));
            return;
        }


    }


    /**
     * 获取待处通知
     *
     * @return
     * @throws Exception
     */
    @Override
    public void getPendingMessage(String objType, String objId, String openId, Date nowDate, String content) throws Exception {
        BizContCreditSetDto contCreditSetDto = contCreditSetService.selectByKey(objId);
        BizContAccessDto contAccessDto = contAccessService.selectByKey(contCreditSetDto.getAcceId());

        BizContProjectDto contProjectDto = contProjectService.selectByKey(contAccessDto.getProjId());

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fcont%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(pendTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("您有新的资质变更流程申请需要审批！");
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(content);
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(contProjectDto.getProjContName());
//        keyword2.setValue(""+contName+"公司\n"+"资质变更相对人："+dealName+"资质变更");
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", nowDate));
        keyword3.setColor("#000000");


        WxMessageContent remark = new WxMessageContent();
        remark.setValue("\n查看资质变更审批流程，点击下方详情进入！");
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

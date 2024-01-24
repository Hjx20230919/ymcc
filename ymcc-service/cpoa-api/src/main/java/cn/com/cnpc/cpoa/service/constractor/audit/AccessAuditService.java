package cn.com.cnpc.cpoa.service.constractor.audit;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.enums.CheckManResultEnum;
import cn.com.cnpc.cpoa.enums.CheckManStateEnum;
import cn.com.cnpc.cpoa.enums.CheckNoticeTypeEnum;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.*;
import cn.com.cnpc.cpoa.service.constractor.ContLogService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.SendMailUtil;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.contractor.ContLogVo;
import cn.com.cnpc.cpoa.vo.wx.WxMessageContent;
import cn.com.cnpc.cpoa.vo.wx.WxMessageData;
import cn.com.cnpc.cpoa.vo.wx.WxMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class AccessAuditService extends ConstractorAuditService {


    @Autowired
    ContLogService contLogService;

    @Value("${access.prefix}")
    private String acceePrefix;


    static String SUBJECT = "您的准入申请已通过，请您及时处理"; // 邮件标题

    static String SUBJECTBACK = "您的准入申请已被退回，请您及时处理"; // 邮件标题

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void initActiviti(String userId, String objId) throws Exception {
        Map<String, Object> initMap = new HashMap<>();

        String cfgCode = Constants.PROSPLITUSER;
        Date nowDate = DateUtils.getNowDate();
        //       SysDeptDto ownerDept = activitiService.getOwnerDept(userId);
        BizSysConfigDto prjSysConfig = activitiService.getPrjSysConfig(cfgCode);
//        String deptManager = ownerDept.getDeptManager();
        //1 获取项目审核 发起人信息
//        BizCheckStepDto checkStep1 = activitiService.getCheckStep(nowDate, "0", launchMsg, userId,
//                null, objId, CheckTypeEnum.ACCESS.getKey());
//        checkStep1.setStepBeginAt(nowDate);
//
//        BizCheckManDto checkMan1 = activitiService.getCheckMan(nowDate, checkStep1.getStepId(), userId, CheckManStateEnum.PENDING.getKey(),
//                null, null);

        //待审批--承办人


        //2 获取项目审核 部门负责人信息
//        if (StringUtils.isNotEmpty(deptManager)) {
//            BizCheckStepDto checkStep2 = activitiService.getCheckStep(nowDate, "1", deptMsg, userId,
//                    null, objId, CheckTypeEnum.ACCESS.getKey());
//            checkStep2.setStepBeginAt(nowDate);
//
//            BizCheckManDto checkMan2 = activitiService.getCheckMan(nowDate, checkStep2.getStepId(), deptManager, CheckManStateEnum.PENDING.getKey(),
//                    null, null);
//
//            initMap.put("checkStep2", checkStep2);
//            initMap.put("checkMan2", checkMan2);
//
//        }

        //3 获取项目审核 企管信息
        BizCheckStepDto checkStep3 = activitiService.getCheckStep(nowDate, "0", sysMsg, userId,
                null, objId, CheckTypeEnum.ACCESS.getKey(),prjSysConfig.getCfgValue());
        checkStep3.setStepSplit(1);

        BizCheckManDto checkMan3 = activitiService.getCheckMan(nowDate, checkStep3.getStepId(), prjSysConfig.getCfgValue(), CheckManStateEnum.PENDING.getKey(), null, null);

        SysUserDto userDto = userService.selectByKey(prjSysConfig.getCfgValue());
        getPendingMessage(ContractorConstant.AuditService.ACESSAUDITSERVICE, objId, userDto.getWxopenid()
                , nowDate, ContractorConstant.ContWXContent.ACESSAUDITCONTENT);
        //initMap.put("checkStep1", checkStep1);
        initMap.put("checkStep3", checkStep3);
        //initMap.put("checkMan1", checkMan1);
        initMap.put("checkMan3", checkMan3);

        activitiService.saveInitMap(initMap);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void passActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.PASS.getKey());

        BizContAccessDto accessDto = contAccessService.selectByKey(auditVo.getObjId());
        accessDto.setAcceStateAt(nowDate);


        //3 同级操作是否还有未处理的
        if (!activitiService.hasNoDealStep(auditVo)) {
            //最后一步则要新增 准入申请等信息
            if (activitiService.isLastStep(auditVo)) {
                accessDto.setAcceState(AcceStateEnum.DONE.getKey());

                BizContContractorDto contractorDto = contContractorService.selectByKey(accessDto.getContId());

                //更新填报时间为填报完成
                BizContContractorDto contContractorDto = new BizContContractorDto();
                contContractorDto.setContId(accessDto.getContId());
                contContractorDto.setContState(ContractorStateEnum.FILLCOMPELTE.getKey());
                contContractorDto.setContStateAt(DateUtils.getNowDate());
                contContractorDto.setAcceStateAt(DateUtils.getNowDate());
                //临时准入-设置冻结时间
                if (AccessTypeEnum.TEMPORARY.getValue().equals(contractorDto.getAccessLevel())) {
                    String cfgCode = ContractorConstant.CONT_TEMP_FREEZE_DATE;
                    BizSysConfigDto prjSysConfig = activitiService.getPrjSysConfig(cfgCode);
                    contContractorDto.setContTempFreezeDate(DateUtils.getCurrentAddDays(DateUtils.getNowDate(), Integer.valueOf(prjSysConfig.getCfgValue())));
                }
                contContractorService.updateNotNull(contContractorDto);

                //更新资质为 生效
                contCreditService.updateContCreditByAcceId(accessDto.getAcceId(), CreditStateEnum.VALID.getKey());

                //记录准入信息
                ContLogVo vo = new ContLogVo();
                vo.setContId(accessDto.getContId());
                vo.setLogObj(ContLogObjEnum.APPLYPROJ.getKey());
                vo.setLogDesc("项目正式准入");
                contLogService.addContLogOne(accessDto.getOwnerId(), vo);

                //发送邮件
                String[] TOS = new String[]{contractorDto.getLinkMail()};
                SendMailUtil.threadSendMail(getMailMessage(contractorDto, accessDto, acceePrefix),SUBJECT, TOS);

                //微信推送
                getPassMessage(auditVo.getObjectType(), auditVo.getObjId(), auditVo.getManId(), auditVo.getChekNode(), nowDate, ContractorConstant.ContWXContent.ACESSAUDITCONTENT);

            } else {
                updateNextStep(auditVo, nowDate);
            }
        }

        contAccessService.updateNotNull(accessDto);

    }

    private String getMailMessage(BizContContractorDto contContractorDto, BizContAccessDto accessDto, String acceePrefix) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(contContractorDto.getContName());
        sbf.append("公司 你好！\n");
        sbf.append("    你申请办理安检院院准入（或临时准入）已获同意。推荐使用谷歌公司的Chrome浏览器或360浏览器（需要设置为极速模式）打开以下准入地址，正确填写准入码，上传贵公司准\n");
        sbf.append("入资料办理准入。\n\n");
        sbf.append("准入地址：");
        sbf.append(acceePrefix);
        sbf.append("/login/access/");
        sbf.append(accessDto.getAcceId());
        sbf.append("\n\n");
        sbf.append("准入码: ");
        sbf.append(accessDto.getAcceCode());
        sbf.append("\n\n");
        sbf.append("业务咨询人：叶剑眉     0838-5150017");
        return sbf.toString();

    }


    private String getBackMailMessage(BizContContractorDto contContractorDto, BizContAccessDto accessDto, String acceePrefix, String chekNode) {
        StringBuffer sbf = new StringBuffer();
        sbf.append(contContractorDto.getContName());
        sbf.append("公司 你好！\n");
        sbf.append("    你申请办理安检院院准入（或临时准入）被退回，请修改后重新提交。\n\n");
        sbf.append("退回原因：");
        sbf.append(chekNode);

        sbf.append("准入地址：");
        sbf.append(acceePrefix);
        sbf.append("/login/access/");
        sbf.append(accessDto.getAcceId());
        sbf.append("\n\n");

        sbf.append("准入码: ");
        sbf.append(accessDto.getAcceCode());
        sbf.append("\n\n");

        sbf.append("业务咨询人：叶剑眉     0838-5150017");
        return sbf.toString();

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public void backActiviti(AuditVo auditVo) throws Exception {
        Date nowDate = DateUtils.getNowDate();

        defaultUpdateStep(auditVo, nowDate, CheckManResultEnum.BACK.getKey());

        //退回项目
        BizContAccessDto accessDto = contAccessService.selectByKey(auditVo.getObjId());
        accessDto.setAcceStateAt(nowDate);
        accessDto.setAcceState(AcceStateEnum.BACK.getKey());
        contAccessService.updateNotNull(accessDto);

        BizContContractorDto contractorDto = contContractorService.selectByKey(accessDto.getContId());

        //发送邮件

        String[] TOS = new String[]{contractorDto.getLinkMail()};
        SendMailUtil.threadSendMail(getBackMailMessage(contractorDto, accessDto, acceePrefix,auditVo.getChekNode()),SUBJECTBACK, TOS);

        //拒绝-通知承办人
        getRefuseMessage(auditVo.getObjectType(), auditVo.getObjId(),
                auditVo.getManId(), auditVo.getChekNode(),
                nowDate, ContractorConstant.ContWXContent.ACESSAUDITCONTENT);

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
        BizContAccessDto contAccessDto = contAccessService.selectByKey(objId);

        BizContProjectDto contProjectDto = contProjectService.selectByKey(contAccessDto.getProjId());

        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewaccess%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(contAccessDto.getOwnerId());


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(passTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的正式准入审批流程有新的结果！\n\n名称：" + contProjectDto.getProjContName());
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
        remark.setValue("\n查看正式准入审批流程，点击下方详情进入！");
        remark.setColor("#0033CC");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setKeyword4(keyword4);
        data.setRemark(remark);
        vo.setData(data);

        checkNoticeService.saveNotice(objId, objType, CheckNoticeTypeEnum.PASS.getKey(), userDto.getWxopenid(), passTemplateId, JSON.marshal(vo));
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


        BizContAccessDto contAccessDto = contAccessService.selectByKey(objId);

        BizContProjectDto contProjectDto = contProjectService.selectByKey(contAccessDto.getProjId());


        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewaccess%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";

        BizCheckManDto bizCheckManDto = checkManService.selectByKey(manUserId);
        SysUserDto userDto = userService.selectByKey(bizCheckManDto.getUserId());
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        //承办人
        SysUserDto user = userService.selectByKey(contAccessDto.getOwnerId());


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(user.getWxopenid());
        vo.setTemplate_id(refuseTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("你提交的正式准入审批流程有新的结果！\n\n名称：" + contProjectDto.getProjContName());
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
        remark.setValue("\n查看正式准入审批流程，点击下方详情进入！");
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


    /**
     * 获取待处通知
     *
     * @return
     * @throws Exception
     */
    @Override
    public void getPendingMessage(String objType, String objId, String openId, Date nowDate, String content) throws Exception {
        BizContAccessDto contAccessDto = contAccessService.selectByKey(objId);

        BizContProjectDto contProjectDto = contProjectService.selectByKey(contAccessDto.getProjId());


        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fcont%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(pendTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue("您有新的正式准入流程申请需要审批！");
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(content);
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(contProjectDto.getProjContName());
//        keyword2.setValue(""+contName+"公司\n"+"正式准入相对人："+dealName+"项目正式准入");
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", nowDate));
        keyword3.setColor("#000000");


        WxMessageContent remark = new WxMessageContent();
        remark.setValue("\n查看正式准入审批流程，点击下方详情进入！");
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

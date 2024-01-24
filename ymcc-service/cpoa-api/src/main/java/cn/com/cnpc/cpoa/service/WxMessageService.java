package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.bid.BidProjectDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.enums.CheckNoticeSendSateEnum;
import cn.com.cnpc.cpoa.enums.CheckNoticeTypeEnum;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizCheckNoticeDtoMapper;
import cn.com.cnpc.cpoa.po.bid.BidProjectPo;
import cn.com.cnpc.cpoa.service.bid.BidProjectService;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsService;
import cn.com.cnpc.cpoa.service.prodsys.BizProjectService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.WxUtils;
import cn.com.cnpc.cpoa.vo.wx.WxMessageContent;
import cn.com.cnpc.cpoa.vo.wx.WxMessageData;
import cn.com.cnpc.cpoa.vo.wx.WxMessageVo;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 10:53
 * @Description:推送消息
 */
@Service
public class WxMessageService {
    private static final Logger logger = LoggerFactory.getLogger(WxMessageService.class);
    /**
     * 推送消息
     */
    String SEND_MSG_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=%s";

    @Autowired
    BizCheckNoticeDtoMapper bzCheckNoticeDtoMapper;

    @Autowired
    private BizDealHdService bizDealHdService;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    DealService dealService;
    @Autowired
    SettlementService settlementService;
    @Autowired
    ContractorService contractorService;
    @Autowired
    DeptService deptService;
    @Autowired
    UserService userService;
    @Autowired
    CheckNoticeService checkNoticeService;
    @Autowired
    CheckManService checkManService;
    @Autowired
    protected WxMpConfigStorage wxMpConfigStorage;

    @Autowired
    BizDealPsService dealPsService;

    @Autowired
    BizProjectService bizProjectService;

    @Autowired
    ConstractorAuditService constractorAuditService;

    @Autowired
    ProjectAuditService projectAuditService;

    @Autowired
    private BidProjectService bidProjectService;

    @Value("${templateId.passTemplateId}")
    private String passTemplateId;

    @Value("${templateId.refuseTemplateId}")
    private String refuseTemplateId;

    @Value("${templateId.pendTemplateId}")
    private String pendTemplateId;

    @Value("${templateId.templateUrl}")
    private String templateUrl;

    public static final Map<String, String> wxContentMap = new HashMap<>();

    static {
        wxContentMap.put(CheckTypeEnum.DEAL.getKey(), "合同-立项审核");
        wxContentMap.put(CheckTypeEnum.SETTLE.getKey(), "合同-结算审核");
        wxContentMap.put(CheckTypeEnum.CONTPROJECT.getKey(), ContractorConstant.ContWXContent.PROJECTCONTENT);
        wxContentMap.put(CheckTypeEnum.ACCESS.getKey(), ContractorConstant.ContWXContent.ACESSAUDITCONTENT);
        wxContentMap.put(CheckTypeEnum.CREDITSET.getKey(), ContractorConstant.ContWXContent.CREDITSETSCONTENT);
        wxContentMap.put("dealps", "提前开工审核");
        wxContentMap.put(CheckTypeEnum.INSTRUCTION.getKey(), CheckTypeEnum.INSTRUCTION.getValue());
        wxContentMap.put(CheckTypeEnum.DELEGATE.getKey(), CheckTypeEnum.DELEGATE.getValue());
        wxContentMap.put(CheckTypeEnum.MULTIPROJECT.getKey(), CheckTypeEnum.MULTIPROJECT.getValue());
        wxContentMap.put(CheckTypeEnum.PROPROJECT.getKey(), ProjectConstant.ContWXContent.PROPROJECTCONTENT);
        wxContentMap.put(CheckTypeEnum.SELCONT.getKey(), ProjectConstant.ContWXContent.SELCONTCONTENT);
        wxContentMap.put(CheckTypeEnum.PURCHASE.getKey(), ProjectConstant.ContWXContent.PURCHASECONTENT);
        wxContentMap.put(CheckTypeEnum.REPURCHASE.getKey(), ProjectConstant.ContWXContent.REPURCHASECONTENT);

    }

    public List<BizCheckNoticeDto> getSendMessage() {
        return bzCheckNoticeDtoMapper.getSendMessage();
    }


    public void pushRealTimeMessage() {
        //1 先更新已经审核的，为发送成功，无需再发送
        //List<BizCheckNoticeDto> penedMessage = bzCheckNoticeDtoMapper.getPenedMessage();


        //2 查询需要发送的消息（待发送或者失败三次以内的）
        List<BizCheckNoticeDto> checkNoticeDtos = getSendMessage();
        for (BizCheckNoticeDto checkNoticeDto : checkNoticeDtos) {
            try {
                String noticeUserId = checkNoticeDto.getNoticeUserId();
                if (StringUtils.isEmpty(noticeUserId)) {
                    checkNoticeDto.setNoticeSendState(CheckNoticeSendSateEnum.FAIL.getKey());
                    checkNoticeDto.setNoticeSendRetry(3);
                    checkNoticeDto.setNoticeSendTime(DateUtils.getNowDate());
                    checkNoticeDto.setNoticeNote("发送失败,用户未绑定");
                    checkNoticeService.updateNotNull(checkNoticeDto);
                    continue;
                }
                boolean push = push(checkNoticeDto.getNoticeSendContent());
                if (push) {
                    checkNoticeDto.setNoticeSendState(CheckNoticeSendSateEnum.SUCCESS.getKey());
                    checkNoticeDto.setNoticeSendTime(DateUtils.getNowDate());
                    checkNoticeDto.setNoticeNote("发送成功");
                    checkNoticeService.updateNotNull(checkNoticeDto);
                } else {
                    checkNoticeDto.setNoticeSendState(CheckNoticeSendSateEnum.FAIL.getKey());
                    checkNoticeDto.setNoticeSendRetry(checkNoticeDto.getNoticeSendRetry() + 1);
                    checkNoticeDto.setNoticeSendTime(DateUtils.getNowDate());
                    checkNoticeDto.setNoticeNote("发送失败");
                    checkNoticeService.updateNotNull(checkNoticeDto);
                }
            } catch (Exception e) {
                checkNoticeDto.setNoticeSendState(CheckNoticeSendSateEnum.FAIL.getKey());
                checkNoticeDto.setNoticeSendRetry(checkNoticeDto.getNoticeSendRetry() + 1);
                checkNoticeDto.setNoticeSendTime(DateUtils.getNowDate());
                checkNoticeDto.setNoticeNote("发送失败，系统网络故障" + e.getMessage());
                checkNoticeService.updateNotNull(checkNoticeDto);
            }
        }
    }


    public void push9TimeMessage() {
        try {
            //查询隔天未审批记录
            List<BizCheckNoticeDto> bizCheckNoticeDtos = bzCheckNoticeDtoMapper.selectNextday();
            bizCheckNoticeDtos.addAll(bzCheckNoticeDtoMapper.selectContNextday());
            bizCheckNoticeDtos.addAll(bzCheckNoticeDtoMapper.selectProNextday());
            for (BizCheckNoticeDto bizCheckNoticeDto : bizCheckNoticeDtos) {
                String noticeObjId = bizCheckNoticeDto.getNoticeObjId();
                String noticeObjType = bizCheckNoticeDto.getNoticeObjType();
                String manId = bizCheckNoticeDto.getManId();
                BizCheckManDto bizCheckManDto = checkManService.selectByKey(manId);
                String userId = bizCheckManDto.getUserId();
                SysUserDto userDto = userService.selectByKey(userId);

                if(noticeObjType.equals(CheckTypeEnum.CONTPROJECT.getKey())||noticeObjType.equals(CheckTypeEnum.ACCESS.getKey())||noticeObjType.equals(CheckTypeEnum.CREDITSET.getKey())){
                    ConstractorAuditService auditService = constractorAuditService.getAuditService(noticeObjType);
                    auditService.getPendingMessage(noticeObjType,noticeObjId,userDto.getWxopenid(),bizCheckNoticeDto.getNoticeSendTime(), wxContentMap.get(noticeObjType));
                }else if(noticeObjType.equals(CheckTypeEnum.PROPROJECT.getKey())||noticeObjType.equals(CheckTypeEnum.SELCONT.getKey())||noticeObjType.equals(CheckTypeEnum.PURCHASE.getKey())||noticeObjType.equals(CheckTypeEnum.REPURCHASE.getKey())){
                    ProjectAuditService auditService = projectAuditService.getAuditService(noticeObjType);
                    auditService.getPendingMessage(noticeObjType,noticeObjId,userDto.getWxopenid(),bizCheckNoticeDto.getNoticeSendTime(), wxContentMap.get(noticeObjType));
                }else{
                    String pendingMessage = getPendingMessage(noticeObjType, noticeObjId, userDto.getWxopenid(), bizCheckNoticeDto.getNoticeSendTime(), wxContentMap.get(noticeObjType));
                    checkNoticeService.saveNotice(noticeObjId, noticeObjType, CheckNoticeTypeEnum.PENDING.getKey(), userDto.getWxopenid(), pendTemplateId, pendingMessage);
                }


            }
        } catch (Exception e) {
            logger.info("每日九点执行推送隔日未审批微信消息任务出错{}", e.getMessage());
            e.printStackTrace();
        }


    }

    public static final String message="http://119.4.40.87:8011/wechat-api/wx/message";

    public  boolean push(String massage) throws Exception {
        try {
            logger.info("调用推送消息：加密前的消息为{}", massage);
            String encodeToString = Base64.encode(massage.getBytes());
            String res = WxUtils.sendGet(message, "message="+encodeToString);
            logger.info("调用推送消息：加密后的消息为{}", encodeToString);
            if(StringUtils.isEmpty(res)){
                return false;
            }
            AppMessage appMessage= JSONObject.parseObject(res, AppMessage.class);
            logger.info("调用推送消息：返回jsonObject为{}",JSON.marshal(appMessage));
            Boolean errcode =(Boolean) appMessage.getResult();
            if (null != errcode) {
                if (errcode) {
                    return true;
                } else {
                    logger.error("调用返回失败");
                    return false;
                }
            }
        }catch (Exception e){
            logger.error("调用出错了"+e.getMessage(),e);
           //pushForce(massage);
        }
        return false;

    }




    public boolean pushForce(String massage) throws Exception {
        try {
            String res = HttpUtil.get(message+"?message="+massage);
            AppMessage appMessage= JSONObject.parseObject(res, AppMessage.class);
            logger.info("调用推送消息：返回jsonObject为{}",JSON.marshal(appMessage));
            Boolean errcode =(Boolean) appMessage.getResult();
            if (null != errcode) {
                if (errcode) {
                    return true;
                } else {
                    return false;
                }
            }
        }catch (Exception e){
            logger.error("调用出错了"+e.getMessage(),e);
            return false;
        }

        return false;
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
    public String getPassMessage(String objType, String objId, String openId, String manId, String chekNode, Date nowDate, String content) throws Exception {

        String dealName = null;
        String contName = null;
        String url = null;
        String targetType = "合同";
        String contKey = "合同相对人";

        if ("deal".equals(objType)) {
            BizDealDto dealDto = dealService.selectByKey(objId);
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewdeal%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.DEALPS.getKey().equals(objType)) {
            targetType = "提前开工";
            BizDealPsDto dealDto = dealPsService.selectByKey(objId);
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewdealps%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.INSTRUCTION.getKey().equals(objType)) {
            targetType = "指令划拨项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.DELEGATE.getKey().equals(objType)) {
            targetType = "代签项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.MULTIPROJECT.getKey().equals(objType)) {
            targetType = "合同拆分多项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else {
            BizSettlementDto settlementDto = settlementService.selectByKey(objId);
            BizDealDto dealDto = dealService.selectByKey(settlementDto.getDealId());
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewsettle%2f" + settlementDto.getDealId() + "%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        }

        SysUserDto userDto = userService.selectByKey(manId);
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(passTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
//        first.setValue("你提交的合同审批流程有新的结果！\n\n名称：" + dealName + "\n合同相对人：" + contName);
        first.setValue(String.format("你提交的%s审批流程有新的结果！\n\n名称：%s\n%s：%s", targetType, dealName, contKey, contName));
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
//        remark.setValue("\n查看合同审批流程，点击下方详情进入！");
        remark.setValue(String.format("\n查看%s审批流程，点击下方详情进入！", targetType));
        remark.setColor("#0033CC");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setKeyword4(keyword4);
        data.setRemark(remark);
        vo.setData(data);
        return JSON.marshal(vo);
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
    public String getRefuseMessage(String objType, String objId, String openId, String manUserId, String chekNode, Date nowDate, String content) throws Exception {

        String dealName = null;
        String contName = null;
        String url = null;
        String targetType = "合同";
        String contKey = "合同相对人";
        if ("deal".equals(objType)) {
            BizDealDto dealDto = dealService.selectByKey(objId);
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewdeal%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.DEALPS.getKey().equals(objType)) {
            targetType = "提前开工";
            BizDealPsDto dealDto = dealPsService.selectByKey(objId);
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewdealps%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.INSTRUCTION.getKey().equals(objType)) {
            targetType = "指令划拨项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.DELEGATE.getKey().equals(objType)) {
            targetType = "代签项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.MULTIPROJECT.getKey().equals(objType)) {
            targetType = "合同拆分多项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else {
            BizSettlementDto settlementDto = settlementService.selectByKey(objId);
            BizDealDto dealDto = dealService.selectByKey(settlementDto.getDealId());
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fpreviewsettle%2f" + settlementDto.getDealId() + "%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        }

        SysUserDto userDto = userService.selectByKey(manUserId);
        String deptId = userDto.getDeptId();
        SysDeptDto deptDto = deptService.selectByKey(deptId);
        String deptName = deptDto.getDeptName();
        String userName = userDto.getUserName();

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(refuseTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
//        first.setValue("你提交的合同审批流程有新的结果！\n\n名称：" + dealName + "\n合同相对人：" + contName);
        first.setValue(String.format("你提交的%s审批流程有新的结果！\n\n名称：%s\n%s：%s", targetType, dealName, contKey, contName));
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
//        remark.setValue("\n查看合同审批流程，点击下方详情进入！");
        remark.setValue(String.format("\n查看%s审批流程，点击下方详情进入！", targetType));
        remark.setColor("#0033CC");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setKeyword4(keyword4);
        data.setRemark(remark);
        vo.setData(data);
        return JSON.marshal(vo);
    }

    /**
     * 获取待处通知
     *
     * @return
     * @throws Exception
     */
    public String getPendingMessage2(String objType, String objId, String openId, Date nowDate, String content) throws Exception {
        String dealName = null;
        String contName = null;
        String url = null;
        String targetType = "合同";
        String contKey = "合同相对人";
        if ("deal".equals(objType)) {
            BizDealDto dealDto = dealService.selectByKey(objId);
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fdeal%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.DEALPS.getKey().equals(objType)) {
            targetType = "提前开工";
            BizDealPsDto dealDto = dealPsService.selectByKey(objId);
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fdealps%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        }else if (CheckTypeEnum.HD.getKey().equals(objType)) {
//            BizDealHdDto bizDealHdDto = bizDealHdService.selectByKey(objId);
//            String contractId = bizDealHdDto.getContractId();
//            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
//            dealName = bizDealHdDto.getDealName();
//            contName = contractorDto.getContName();
//            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.INSTRUCTION.getKey().equals(objType)) {
            targetType = "指令划拨项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.DELEGATE.getKey().equals(objType)) {
            targetType = "代签项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.MULTIPROJECT.getKey().equals(objType)) {
            targetType = "合同拆分多项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if(CheckTypeEnum.SETTLE.getKey().equals(objType)){
            BizSettlementDto settlementDto = settlementService.selectByKey(objId);
            BizDealDto dealDto = dealService.selectByKey(settlementDto.getDealId());
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fsettle%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        }

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(pendTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue(String.format("您有新的%s流程申请需要审批！", targetType));
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(content);
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
//        keyword2.setValue(dealName + "\n合同相对人：" + contName);
        keyword2.setValue(String.format("%s\n%s：%s", dealName, contKey, contName));
//        keyword2.setValue(""+contName+"公司\n"+"合同相对人："+dealName+"项目合同");
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", nowDate));
        keyword3.setColor("#000000");


        WxMessageContent remark = new WxMessageContent();
        remark.setValue(String.format("\n查看%s审批流程，点击下方详情进入！", targetType));
        remark.setColor("#0033CC");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setRemark(remark);
        vo.setData(data);
        return JSON.marshal(vo);
    }
    public String getPendingMessage(String objType, String objId, String openId, Date nowDate, String content) throws Exception {
        String dealName = null;
        String contName = null;
        String url = null;
        String targetType = "合同";
        String contKey = "合同相对人";
        if ("deal".equals(objType)) {
            BizDealDto dealDto = dealService.selectByKey(objId);
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fdeal%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.DEALPS.getKey().equals(objType)) {
            targetType = "提前开工";
            BizDealPsDto dealDto = dealPsService.selectByKey(objId);
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fdealps%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.INSTRUCTION.getKey().equals(objType)) {
            targetType = "指令划拨项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.DELEGATE.getKey().equals(objType)) {
            targetType = "代签项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if (CheckTypeEnum.MULTIPROJECT.getKey().equals(objType)) {
            targetType = "合同拆分多项目开工";
            contKey = "客户";
            BizProjectDto projDto = bizProjectService.selectByKey(objId);
            String contractId = projDto.getClientId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = projDto.getContractName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fpreviewbizproj%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        } else if(CheckTypeEnum.SETTLE.getKey().equals(objType)){
            BizSettlementDto settlementDto = settlementService.selectByKey(objId);
            BizDealDto dealDto = dealService.selectByKey(settlementDto.getDealId());
            String contractId = dealDto.getContractId();
            BizContractorDto contractorDto = contractorService.selectByKey(contractId);
            dealName = dealDto.getDealName();
            contName = contractorDto.getContName();
            url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fsettle%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";
        }

        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(pendTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue(String.format("您有新的%s流程申请需要审批！", targetType));
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(content);
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
//        keyword2.setValue(dealName + "\n合同相对人：" + contName);
        keyword2.setValue(String.format("%s\n%s：%s", dealName, contKey, contName));
//        keyword2.setValue(""+contName+"公司\n"+"合同相对人："+dealName+"项目合同");
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", nowDate));
        keyword3.setColor("#000000");


        WxMessageContent remark = new WxMessageContent();
        remark.setValue(String.format("\n查看%s审批流程，点击下方详情进入！", targetType));
        remark.setColor("#0033CC");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setRemark(remark);
        vo.setData(data);
        return JSON.marshal(vo);
    }

    /**
     * 获取待处通知
     *
     * @return
     * @throws Exception
     */
    public String getBidProjectMessage( String objId, String openId, Date nowDate, String content) throws Exception {
        String url = null;
        String targetType = "招标项目";

        BidProjectPo bidProjectPo = bidProjectService.selectBidProjectDetail(objId);
        String projName = bidProjectPo.getProjName();
        String projNo = bidProjectPo.getProjNo();
        url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + wxMpConfigStorage.getAppId() + "&redirect_uri=http%3a%2f%2f" + templateUrl + "%2fapp%2fhome%2fdeal%2f" + objId + "&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect";


        WxMessageVo vo = new WxMessageVo();
        vo.setTouser(openId);
        vo.setTemplate_id(pendTemplateId);
        vo.setUrl(url);
        WxMessageData data = new WxMessageData();

        WxMessageContent first = new WxMessageContent();
        first.setValue(String.format("您有新的%s流程申请需要审批！", targetType));
        first.setColor("#00000");

        WxMessageContent keyword1 = new WxMessageContent();
        keyword1.setValue(content);
        keyword1.setColor("#00000");

        WxMessageContent keyword2 = new WxMessageContent();
        keyword2.setValue(String.format("%s\n%s：%s", projName, "项目编号", projNo));
        keyword2.setColor("#00000");

        WxMessageContent keyword3 = new WxMessageContent();
        keyword3.setValue(DateUtils.parseDateToStr("yyyy年MM月dd日 HH时mm分ss秒", nowDate));
        keyword3.setColor("#000000");


        WxMessageContent remark = new WxMessageContent();
        remark.setValue(String.format("\n查看%s审批流程，点击下方详情进入！", targetType));
        remark.setColor("#0033CC");

        data.setFirst(first);
        data.setKeyword1(keyword1);
        data.setKeyword2(keyword2);
        data.setKeyword3(keyword3);
        data.setRemark(remark);
        vo.setData(data);
        return JSON.marshal(vo);
    }

    public static void main(String[] args) throws Exception{
        //push("{asas\n}");

        String massage = "{\n" +
                "  \"touser\" : \"oHRZi5pZBRyoB1-aBJDXrh3TpO_U\",\n" +
                "  \"template_id\" : \"F31QpBiXxTSnA9tB9YzAbmRgtNVfeNIrmDDZ6HqTL0I\",\n" +
                "  \"url\" : \"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx9d7e6d005eb28ea6&redirect_uri=http%3a%2f%2fcpoa0.zhinglink.com%2fapp%2fhome%2fsettle%2f7035775f776947919a7c427894db5d15&response_type=code&scope=snsapi_base&state=STATE&connect_redirect=1#wechat_redirect\",\n" +
                "  \"data\" : {\n" +
                "    \"first\" : {\n" +
                "      \"value\" : \"您有新的合同流程申请需要审批！\",\n" +
                "      \"color\" : \"#00000\"\n" +
                "    },\n" +
                "    \"keyword1\" : {\n" +
                "      \"value\" : \"合同-结算审批\",\n" +
                "      \"color\" : \"#00000\"\n" +
                "    },\n" +
                "    \"keyword2\" : {\n" +
                "      \"value\" : \"2021年气井复合增产工艺技术服务内部责任书\\n合同相对人：中国石油集团川庆钻探工程有限公司苏里格项目经理部\",\n" +
                "      \"color\" : \"#00000\"\n" +
                "    },\n" +
                "    \"keyword3\" : {\n" +
                "      \"value\" : \"2021年11月09日 15时50分27秒\",\n" +
                "      \"color\" : \"#000000\"\n" +
                "    },\n" +
                "    \"keyword4\" : null,\n" +
                "    \"remark\" : {\n" +
                "      \"value\" : \"\\n查看合同审批流程，点击下方详情进入！\",\n" +
                "      \"color\" : \"#0033CC\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        String encodeToString = Base64.encode(massage.getBytes("UTF-8"));
        System.out.println("encodeToString:"+encodeToString);
    }
}

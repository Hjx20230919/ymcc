package cn.com.cnpc.cpoa.service.constractor.audit;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizCheckStepDto;
import cn.com.cnpc.cpoa.enums.CheckManStateEnum;
import cn.com.cnpc.cpoa.po.contractor.ContProjectPo;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.service.constractor.*;
import cn.com.cnpc.cpoa.vo.AuditVo;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/10/13 7:51
 * @Description:
 */
@Service
public class ConstractorAuditService {

    public  final static String launchMsg="发起";
    public  final static String deptMsg="部门审核";
    public  final static String sysMsg="企管审核";
    public  final static String passMsg=" 通过";
    public final static String ministerMsg = "部长审核";
    public final static String organMsg = "机关审核";
    public final static String userName = "沈栩锐";

    @Autowired
    ContReviewTaskService taskService;

    @Autowired
    ContProjectAuditService contProjectAuditService;

    @Autowired
    AccessAuditService accessAuditService;

    @Autowired
    CreditSetAuditService creditSetAuditService;

    @Autowired
    ContReviewTaskAuditService reviewTaskAuditService;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    CheckManService checkManService;

    @Autowired
    CheckStepService checkStepService;

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    protected WxMpConfigStorage wxMpConfigStorage;

    @Autowired
    DeptService deptService;
    @Autowired
    UserService userService;

    @Autowired
    CheckNoticeService checkNoticeService;

    @Autowired
    ContCreditSetService contCreditSetService;

    @Autowired
    ContCreditAttachService contCreditAttachService;


    @Autowired
    ContAccessService contAccessService;

    @Autowired
    ContCreditService contCreditService;


    @Autowired
    ContProjectService contProjectService;

    @Autowired
    ContCreditTiService contCreditTiService;

    @Autowired
    ContReviewOfficeDetailAuditService officeDetailAuditService;

    @Autowired
    ContReviewOfficeDetailService officeDetailService;



    @Value("${templateId.passTemplateId}")
    protected   String passTemplateId;

    @Value("${templateId.refuseTemplateId}")
    protected  String refuseTemplateId;

    @Value("${templateId.pendTemplateId}")
    protected  String pendTemplateId;

    @Value("${templateId.templateUrl}")
    protected  String templateUrl;

    public void initActiviti(String userId,String objId) throws Exception{

    }

    public void passActiviti(AuditVo auditVo) throws Exception{

    }

    public void backActiviti(AuditVo auditVo)throws Exception{

    }


    /**
     *
     * @param objType  对象类型 settle deal
     * @param objId 对象主键
     * @param manId 审核人
     * @param chekNode 审核意见
     * @param nowDate 审核时间
     * @return
     * @throws Exception
     */
    public  void getPassMessage(String objType,String objId,
                                  String manId,String chekNode,Date nowDate,String content) throws Exception {


      return ;
    }

    /**
     *
     * @param objType  对象类型 settle deal
     * @param objId 对象主键
     * @param manUserId 审核人
     * @param chekNode 审核意见
     * @param nowDate 审核时间
     * @return
     * @throws Exception
     */
    public  void getRefuseMessage(String objType,String objId,
                                    String manUserId,String chekNode,
                                    Date nowDate,String content) throws Exception {

        return;
    }


    /**
     * 获取待处通知
     *
     * @return
     * @throws Exception
     */
    public  void getPendingMessage(String objType,String objId,String openId,Date nowDate,String content) throws Exception {

        return ;
    }

    public ConstractorAuditService getAuditService(String type) {
        ConstractorAuditService constractorAuditService;
        switch(type){
            case ContractorConstant.AuditService.PROJECTAUDITSERVICE :
                constractorAuditService= contProjectAuditService;
                break;
            case ContractorConstant.AuditService.ACESSAUDITSERVICE :
                constractorAuditService=accessAuditService;
                break;
            case ContractorConstant.AuditService.CREDITSETSERVIVCE :
                constractorAuditService=creditSetAuditService;
                break;
            case ContractorConstant.AuditService.REVIEW_TASK :
                constractorAuditService=reviewTaskAuditService;
                break;
            case ContractorConstant.AuditService.OFFICE_REVIEW_TASK :
                constractorAuditService=officeDetailAuditService;
                break;
            default :
                throw new AppException("当前操作对象不存在，请确认后再试");
        }

        return constractorAuditService;
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

    public void updateNextStep(AuditVo auditVo,Date nowDate) throws Exception{
        //如果不是最后一步，且都是审核完成了。则更新下次合同的开始时间
        Map<String,Object> par=new HashMap<>();
        String objId = auditVo.getObjId();

        par.put("objId", objId);
        par.put("stepNo",Integer.valueOf(auditVo.getStepNo())+1);
        List<BizCheckStepDto> bizCheckStepDtos = checkStepService.selectList(par);
        for (BizCheckStepDto bizCheckStepDto:bizCheckStepDtos) {
            bizCheckStepDto.setStepBeginAt(nowDate);
            checkStepService.updateNotNull(bizCheckStepDto);
        }

        //推送微信
        List<String> list=new ArrayList<>();
        list.add(objId);

        List<ContProjectPo> pos = contProjectService.selectUserNameListWx(list);
        for (ContProjectPo contProjectPo:pos) {
            String wxopenid = userService.selectByKey(contProjectPo.getUserId()).getWxopenid();
            //推送消息
            getPendingMessage(auditVo.getObjectType(), objId,wxopenid
                    ,nowDate,ContractorConstant.ContWXContent.contMap.get(auditVo.getObjectType()));
        }

    }

}

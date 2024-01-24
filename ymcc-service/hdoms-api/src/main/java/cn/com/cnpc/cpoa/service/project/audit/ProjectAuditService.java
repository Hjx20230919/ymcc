package cn.com.cnpc.cpoa.service.project.audit;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.domain.project.BizProjContListDto;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.enums.prodsys.ContractStateEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.enums.project.SelContTypeEnum;
import cn.com.cnpc.cpoa.mapper.prodsys.BizDealPsDtoMapper;
import cn.com.cnpc.cpoa.po.project.ProActivitiItemPo;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.service.constractor.ContProjectService;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsService;
import cn.com.cnpc.cpoa.service.project.*;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/12/5 8:31
 * @Description:
 */
@Service
public class ProjectAuditService {

    private final BigDecimal fifty = new BigDecimal(500000);

    private final BigDecimal oneHundred = new BigDecimal(1000000);

    public final static String launchMsg = "发起";
    public final static String deptMsg = "部门审核";
    public final static String sysMsg = "企管审核";
    public final static String passMsg = " 通过";

    @Autowired
    ProjProjectAuditService projProjectAuditService;

    @Autowired
    ProjSelContAuditService projSelContAuditService;

    @Autowired
    ProjPurchaseAuditService projPurchaseAuditService;

    @Autowired
    ProjResPurchaseAuditService projResPurchaseAuditService;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    CheckTiService checkTiService;

    @Autowired
    CheckManService checkManService;

    @Autowired
    CheckStepService checkStepService;

    @Autowired
    ConstractorAuditService constractorAuditService;

    @Autowired
    CheckNoticeService checkNoticeService;

    @Autowired
    ProjProjectService projProjectService;

    @Autowired
    ProjSelContService projSelContService;

    @Autowired
    ProjPurcPlanService projPurcPlanService;

    @Autowired
    ProjPurcResultService projPurcResultService;

    @Autowired
    DeptService deptService;

    @Autowired
    UserService userService;

    @Autowired
    ContProjectService contProjectService;

    @Autowired
    protected WxMpConfigStorage wxMpConfigStorage;

    @Autowired
    ProjContListService projContListService;

    @Autowired
    AttachService attachService;

    @Autowired
    BizDealPsService dealService;

    @Autowired
    WxMessageService wxMessageService;

    @Autowired
    private BizDealPsDtoMapper bizDealDtoMapper;

    @Value("${templateId.passTemplateId}")
    protected String passTemplateId;

    @Value("${templateId.refuseTemplateId}")
    protected String refuseTemplateId;

    @Value("${templateId.pendTemplateId}")
    protected String pendTemplateId;

    @Value("${templateId.templateUrl}")
    protected String templateUrl;


    public void initActiviti(String userId, String objId) throws Exception {


    }

    public void passActiviti(AuditVo auditVo) throws Exception {


    }

    public void backActiviti(AuditVo auditVo) throws Exception {

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
    public void getPassMessage(String objType, String objId,
                               String manId, String chekNode, Date nowDate, String content) throws Exception {


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
    public void getRefuseMessage(String objType, String objId,
                                 String manUserId, String chekNode,
                                 Date nowDate, String content) throws Exception {

        return;
    }


    /**
     * 获取待处通知
     *
     * @return
     * @throws Exception
     */
    public void getPendingMessage(String objType, String objId, String openId, Date nowDate, String content) throws Exception {

        return;
    }


    public void defaultUpdateStep(AuditVo auditVo, Date nowDate, String checkResult) {
        constractorAuditService.defaultUpdateStep(auditVo, nowDate, checkResult);
    }

    public void updateNextStep(AuditVo auditVo, Date nowDate) throws Exception {
        //如果不是最后一步，且都是审核完成了。则更新下次合同的开始时间
        Map<String, Object> par = new HashMap<>();
        String objId = auditVo.getObjId();

        par.put("objId", objId);
        par.put("stepNo", Integer.valueOf(auditVo.getStepNo()) + 1);
        List<BizCheckStepDto> bizCheckStepDtos = checkStepService.selectList(par);
        for (BizCheckStepDto bizCheckStepDto : bizCheckStepDtos) {
            bizCheckStepDto.setStepBeginAt(nowDate);
            checkStepService.updateNotNull(bizCheckStepDto);
        }
        //推送微信
        List<String> list = new ArrayList<>();
        list.add(objId);

        List<ProActivitiItemPo> pos = projProjectService.selectUserNameListWx(list);
        for (ProActivitiItemPo proActivitiItemPo : pos) {
            String wxopenid = userService.selectByKey(proActivitiItemPo.getUserId()).getWxopenid();
            //推送消息
            getPendingMessage(auditVo.getObjectType(), objId, wxopenid
                    , nowDate, ProjectConstant.ContWXContent.projMap.get(auditVo.getObjectType()));
        }
    }


    public void initStepActiviti(String userId, String objId, String objType, Map<String, String> stepTmplMap) throws Exception {
        Date nowDate = DateUtils.getNowDate();
        // 获取初始化步骤
        Map<String, Object> initMap = getDefualtStepMap(userId, objId, objType, nowDate);
        int stepNo = (int) initMap.get("stepNo");

        //根据模板获取剩余步骤
        Map<String, Object> param = new HashMap<>();
        param.put("tmplCode", stepTmplMap.get((objType)));
        List<BizCheckTiDto> checkTiDtos = checkTiService.selectOwnerCheckTi(param);
        for (BizCheckTiDto checkTiDto : checkTiDtos) {
            //1 新建审核步骤
            BizCheckStepDto checkStepDto = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), checkTiDto.getDeptName(), userId,
                    null, objId, objType,checkTiDto.getUserId());

            //2 新增处理人
            BizCheckManDto checkManDto = activitiService.getCheckMan(nowDate, checkStepDto.getStepId(), checkTiDto.getUserId(), CheckManStateEnum.PENDING.getKey(),
                    null, null);

            initMap.put("checkStep" + stepNo, checkStepDto);
            initMap.put("checkMan" + stepNo, checkManDto);
            stepNo++;
        }

        activitiService.saveProInitMap(initMap, stepNo);
    }


    /**
     * 获取初始化步骤--当前审核人 当前审核人部门
     *
     * @param userId  当前操作人员
     * @param objId
     * @param objType
     */
    public Map<String, Object> getDefualtStepMap(String userId, String objId, String objType, Date nowDate) throws Exception {
        int stepNo = 0;
        Map<String, Object> initMap = new HashMap<>();

        SysDeptDto ownerDept = activitiService.getOwnerDept(userId);
        String deptManager = ownerDept.getDeptManager();
        //1 获取项目审核 发起人信息
        BizCheckStepDto checkStep0 = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), launchMsg, userId,
                CheckStepStateEnum.PASS.getKey(), objId, objType,userId);
        checkStep0.setStepBeginAt(nowDate);
        checkStep0.setStepEndAt(nowDate);

        BizCheckManDto checkMan0 = activitiService.getCheckMan(nowDate, checkStep0.getStepId(), userId, CheckManStateEnum.PENDED.getKey(),
                passMsg, CheckManResultEnum.PASS.getKey());

        initMap.put("checkStep" + stepNo, checkStep0);
        initMap.put("checkMan" + stepNo, checkMan0);
        stepNo++;

        //选商新增叶剑眉
        if(ProjPhaseEnum.SELCONT.getKey().equals(objType)){
            BizSysConfigDto proSplitUser = activitiService.getPrjSysConfig(Constants.PROSPLITUSER);
            BizCheckStepDto checkStep1 = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), deptMsg, userId,
                    null, objId, objType,proSplitUser.getCfgValue());
            checkStep1.setStepBeginAt(nowDate);

            BizCheckManDto checkMan1 = activitiService.getCheckMan(nowDate, checkStep1.getStepId(), proSplitUser.getCfgValue(), CheckManStateEnum.PENDING.getKey(), null, null);

            SysUserDto userDto = userService.selectByKey(proSplitUser.getCfgValue());

            ProjectAuditService auditService = getAuditService(objType);
            auditService.getPendingMessage(objType, objId, userDto.getWxopenid()
                    , nowDate, ProjectConstant.ContWXContent.projMap.get(objType));

            initMap.put("checkStep" + stepNo, checkStep1);
            initMap.put("checkMan" + stepNo, checkMan1);
            stepNo++;
        }

        //2 获取项目审核 部门负责人信息
        if (StringUtils.isNotEmpty(deptManager)) {
            BizCheckStepDto checkStep1 = activitiService.getCheckStep(nowDate, String.valueOf(stepNo), deptMsg, userId,
                    null, objId, objType,deptManager);
            BizCheckManDto checkMan1 = activitiService.getCheckMan(nowDate, checkStep1.getStepId(), deptManager, CheckManStateEnum.PENDING.getKey(), null, null);

            if(!ProjPhaseEnum.SELCONT.getKey().equals(objType)) {
                checkStep1.setStepBeginAt(nowDate);

                SysUserDto userDto = userService.selectByKey(deptManager);

                ProjectAuditService auditService = getAuditService(objType);
                auditService.getPendingMessage(objType, objId, userDto.getWxopenid()
                        , nowDate, ProjectConstant.ContWXContent.projMap.get(objType));
            }

            initMap.put("checkStep" + stepNo, checkStep1);
            initMap.put("checkMan" + stepNo, checkMan1);
            stepNo++;
        }
        initMap.put("stepNo", stepNo);
        return initMap;

    }


    /**
     * 获取具体的实现类
     *
     * @param type
     * @return
     */
    public ProjectAuditService getAuditService(String type) {
        ProjectAuditService projectAuditService;
        switch (type) {
            case ProjectConstant.AuditService.PROPROJECTSERVICE:
                projectAuditService = projProjectAuditService;
                break;
            case ProjectConstant.AuditService.SELCONTSERVICE:
                projectAuditService = projSelContAuditService;
                break;
            case ProjectConstant.AuditService.PURCHASESERVICE:
                projectAuditService = projPurchaseAuditService;
                break;
            case ProjectConstant.AuditService.REPURCHASESERVICE:
                projectAuditService = projResPurchaseAuditService;
                break;
            default:
                throw new AppException("当前操作对象不存在，请确认后再试");
        }

        return projectAuditService;
    }


    /**
     * 根据合同金额和招商方式 返回实现类
     *
     * @param selContType 招商方式
     * @return
     */
    public Map<String, String> getServiceMap(String selContType, BigDecimal dealValue) {

        if (StringUtils.equals(selContType, SelContTypeEnum.BUILDPROJECTSINGLE.getKey()) || StringUtils.equals(selContType, SelContTypeEnum.BUILDPROJECTINSIDE.getKey())) {
            return ProjectConstant.AuditService.buildProProjectSingleMap;
        } else if (StringUtils.equals(selContType, SelContTypeEnum.BUILDPROJECTNEGOTIATION.getKey())) {
            //竞争性谈判金额小于50
            if (dealValue.compareTo(fifty) < 0) {
                return ProjectConstant.AuditService.buildProProjectNegotiationMap;
            } else {
                //竞争性谈判金额大于50
                return ProjectConstant.AuditService.buildProProjectNegotiation50Map;
            }
        } else if (StringUtils.equals(selContType, SelContTypeEnum.OPENTENDER.getKey())) {
            return ProjectConstant.AuditService.tenderingMap;
        } else if (StringUtils.equals(selContType, SelContTypeEnum.NOTENDER.getKey())) {
            return ProjectConstant.AuditService.noTenderingMap;
        } else {
            throw new AppException("操作对象不存在!");
        }

    }


    /**
     * 删除回退流程
     * @param id
     */
    public void deleteBack(String id) {

    }


    public Map<String,List> getDeleteActiviti(String id){
        Map<String,List> listMap=new HashMap<>();

        List<String> stepIds=new ArrayList<>();
        List<String> manIds=new ArrayList<>();

        //查询要删除的步骤
        Map<String,Object> param1 =new HashMap<>();
        param1.put("objId",id);
        List<BizCheckStepDto> checkStepDtos = checkStepService.selectList(param1);

        for (BizCheckStepDto checkStepDto: checkStepDtos ) {
            String stepId = checkStepDto.getStepId();
            stepIds.add(stepId);

            Map<String,Object> param2 =new HashMap<>();
            param2.put("stepId", stepId);

            //查询要删除的人员
            List<BizCheckManDto> bizCheckManDtos = checkManService.selectList(param2);
            for (BizCheckManDto manDto:bizCheckManDtos) {
                manIds.add(manDto.getManId());
            }
        }

        listMap.put("stepIds",stepIds);
        listMap.put("manIds",manIds);

        return listMap;
    }

    public Map<String,List> getDeleteFile(String id){
        Map<String,List> listMap=new HashMap<>();

        List<String> attachIds=new ArrayList<>();

        //查询要删除的附件
        Map<String,Object> param3 =new HashMap<>();
        param3.put("ownerId",id);
        List<BizAttachDto> bizAttachDtos = attachService.selectList(param3);
        for (BizAttachDto attachDto:bizAttachDtos) {
            attachIds.add(attachDto.getAttachId());
        }



        listMap.put("attachIds",attachIds);
        return listMap;
    }

    public List<Object> getDeleteProjContList(String id){
        //删除选商列表
        Map<String, Object> param = new HashMap<>();
        param.put("selContId", id);
        List<BizProjContListDto> removeContList = projContListService.selectProjContList(param);
        List<Object> contListKey = new ArrayList<>();
        for (BizProjContListDto projContListDto : removeContList) {
            contListKey.add(projContListDto.getContListId());
        }
        return contListKey;
    }



}

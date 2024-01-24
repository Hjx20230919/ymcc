package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcPlanDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcResultDto;
import cn.com.cnpc.cpoa.domain.project.BizProjSelContDto;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.mapper.BackLogDtoMapper;
import cn.com.cnpc.cpoa.po.BackLogPo;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.MyProcessPo;
import cn.com.cnpc.cpoa.po.project.ProAllIdPo;
import cn.com.cnpc.cpoa.service.bid.BidProjectService;
import cn.com.cnpc.cpoa.service.constractor.ContAccessService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsApprovalService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsCheckStepService;
import cn.com.cnpc.cpoa.service.prodsys.BizProjectCheckStepService;
import cn.com.cnpc.cpoa.service.project.ProjPurcPlanService;
import cn.com.cnpc.cpoa.service.project.ProjPurcResultService;
import cn.com.cnpc.cpoa.service.project.ProjSelContService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.BackLogVo;
import cn.com.cnpc.cpoa.vo.MyProcessVo;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Yuxq
 * @version 1.0
 * @description:   待办项处理
 * @date 2022/4/29 15:18
 */
@Service
public class BackLogService {

    @Autowired
    private BackLogDtoMapper backLogDtoMapper;

    @Autowired
    private BizCheckStepService diyCheckStepService;

    @Autowired
    private BidProjectService projectService;

    @Autowired
    private ProjSelContService selContService;

    @Autowired
    private ProjPurcPlanService planService;

    @Autowired
    private ProjPurcResultService resultService;

    @Autowired
    private CheckStepService stepService;

    @Autowired
    private BizDealPsCheckStepService diyDealPsCheckStepService;

    @Autowired
    private BizProjectCheckStepService diyBizProjectCheckStepService;

    @Autowired
    private ContAccessService contAccessService;
    /**
     * 查询待办项
     * @param pageNum    当前页
     * @param pageSize  分页条数
     * @param status    待办项状态，auditing待审核，audited已审核
     * @param hashMap   查询条件
     * @return  HashMap
     */
    public HashMap<String,Object> selectBackLog(String status, int pageNum,int pageSize,HashMap<String, Object> hashMap) {
        String sessionUserId = ServletUtils.getSessionUserId();
        hashMap.put("userId",sessionUserId);
        PageHelper.startPage(pageNum,pageSize);
        List<BackLogPo> backLogPos;
        //待审核
        if (ContractorConstant.AuditType.AUDITING.equals(status)) {
            backLogPos = backLogDtoMapper.selectBackLogByUserId(hashMap);
        }else {
            backLogPos = backLogDtoMapper.selectAuditByUserId(hashMap);
        }
        //查询发起部门
        backLogPos.forEach(p -> {
            HashMap<String, Object> map = new HashMap<>(4);
            if (p.getCheckObjType().equals("access")) {
                BizContAccessDto contAccessDto = contAccessService.selectByKey(p.getCheckObjId());
                map.put("checkObjId",contAccessDto.getProjId());
                map.put("checkObjType","contProject");
            } else {
                map.put("checkObjId",p.getCheckObjId());
                map.put("checkObjType",p.getCheckObjType());
            }
            List<CheckStepPo> checkStepPos = stepService.selectStartDeptName(map);
            if (checkStepPos.size() > 0) {
                p.setDeptName(checkStepPos.get(0).getDeptName());
            }
        });
        HashMap<String, Object> dataMap = new HashMap<>(4);
        dataMap.put("data",backLogPos);
        dataMap.put("total",new PageInfo<>(backLogPos).getTotal());
        return dataMap;
    }

    /**
     * 查询流程中事项
     * @param pageNum
     * @param pageSize
     * @param hashMap
     * @return  HashMap
     */
    public HashMap<String, Object> selectMyProcessByUserId(int pageNum, int pageSize, HashMap<String, Object> hashMap) {
        PageHelper.startPage(pageNum,pageSize);
        List<MyProcessPo> myProcessPos = backLogDtoMapper.selectMyProcessByUserId(hashMap);
        //查询发起部门
        myProcessPos.forEach(p -> {
            HashMap<String, Object> map = new HashMap<>(4);
            if (p.getCheckObjType().equals("access")) {
                BizContAccessDto contAccessDto = contAccessService.selectByKey(p.getCheckObjId());
                map.put("checkObjId",contAccessDto.getProjId());
                map.put("checkObjType","contProject");
            } else {
                map.put("checkObjId",p.getCheckObjId());
                map.put("checkObjType",p.getCheckObjType());
            }
            List<CheckStepPo> checkStepPos = stepService.selectStartDeptName(map);
            if (checkStepPos.size() > 0) {
                p.setDeptName(checkStepPos.get(0).getDeptName());
            }
        });
        HashMap<String, Object> dataMap = new HashMap<>(4);
        dataMap.put("data",myProcessPos);
        dataMap.put("total",new PageInfo<>(myProcessPos).getTotal());
        return dataMap;
    }

    /**
     *
     * @param checkObjType
     * @param checkObjId
     * @return
     */
    public AppMessage selectDetailByType(String checkObjType, String checkObjId) {
        List<CheckStepPo> checkStepPoList;
        String userId = ServletUtils.getSessionUserId();
        if (checkObjType.equals(CheckTypeEnum.DEAL.getKey())) {
            checkStepPoList = diyCheckStepService.selectDetails(checkObjId, "", userId);
        } else if (checkObjType.equals(CheckTypeEnum.SETTLE.getKey())) {
            checkStepPoList = diyCheckStepService.selectDetails("", checkObjId, userId);
        }else {
            checkStepPoList = diyCheckStepService.selectDetails(checkObjId);
        }
        return AppMessage.success(checkStepPoList,"审核详情查询成功");
    }

    /**
     * 待办事项审批操作
     * @param auditVo
     * @return
     */
    public AppMessage backLogApproval(AuditVo auditVo) throws Exception {
        boolean isSuccess = false;
        if (CheckTypeEnum.DEAL.getKey().equals(auditVo.getObjectType()) || CheckTypeEnum.SETTLE.getKey().equals(auditVo.getObjectType())) {
            isSuccess = diyCheckStepService.audit(auditVo);
        } else if (CheckTypeEnum.CONTPROJECT.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.ACCESS.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.CREDITSET.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.TASK.getKey().equals(auditVo.getObjectType())) {
            isSuccess = diyCheckStepService.auditContractor(auditVo);
        } else if (CheckTypeEnum.PROPROJECT.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.SELCONT.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.PURCHASE.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.REPURCHASE.getKey().equals(auditVo.getObjectType())) {
            isSuccess = diyCheckStepService.auditProject(auditVo);
        } else if (CheckTypeEnum.DEALPS.getKey().equals(auditVo.getObjectType())) {
            isSuccess = diyDealPsCheckStepService.audit(auditVo);
        } else if (CheckTypeEnum.INSTRUCTION.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.DELEGATE.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.MULTIPROJECT.getKey().equals(auditVo.getObjectType())) {
            auditVo.setDealId(auditVo.getObjId());
            isSuccess = diyBizProjectCheckStepService.audit(auditVo);
        } else {
            isSuccess = projectService.audit(auditVo);
        }

        if (!isSuccess) {
            AppMessage.error("审核失败");
        }

        return AppMessage.success(isSuccess, "审核成功！");
    }

    public AppMessage getAllProjId(String checkObjId, String checkObjType) {
        CheckTypeEnum checkTypeEnum = CheckTypeEnum.getEnumViaKey(checkObjType);
        ProAllIdPo proAllIdPo = null;
        switch (checkTypeEnum) {
            case PROPROJECT:
                proAllIdPo = getProj(checkObjId);
                break;
            case SELCONT:
                proAllIdPo = getSelCont(checkObjId);
                break;
            case PURCHASE:
                proAllIdPo = getPlan(checkObjId);
                break;
            case REPURCHASE:
                proAllIdPo = getResult(checkObjId);
                break;
            default:
                break;
        }
        return AppMessage.result(proAllIdPo);
    }

    private ProAllIdPo getResult(String checkObjId) {
        BizProjPurcResultDto resultDto = resultService.selectByKey(checkObjId);
        BizProjPurcPlanDto planDto = planService.selectByKey(resultDto.getPlanId());
        ProAllIdPo proAllIdPo = new ProAllIdPo();
        proAllIdPo.setProjId(planDto.getProjId());
        proAllIdPo.setSelContId(planDto.getSelContId());
        proAllIdPo.setPlanId(planDto.getPlanId());
        proAllIdPo.setResultId(checkObjId);
        return proAllIdPo;
    }

    private ProAllIdPo getPlan(String checkObjId) {
        BizProjPurcPlanDto planDto = planService.selectByKey(checkObjId);
        ProAllIdPo proAllIdPo = new ProAllIdPo();
        proAllIdPo.setProjId(planDto.getProjId());
        proAllIdPo.setSelContId(planDto.getSelContId());
        proAllIdPo.setPlanId(checkObjId);
        return proAllIdPo;
    }

    private ProAllIdPo getSelCont(String checkObjId) {
        BizProjSelContDto selContDto = selContService.selectByKey(checkObjId);
        ProAllIdPo proAllIdPo = new ProAllIdPo();
        proAllIdPo.setProjId(selContDto.getProjId());
        proAllIdPo.setSelContId(checkObjId);
        return proAllIdPo;
    }

    private ProAllIdPo getProj(String checkObjId) {
        ProAllIdPo proAllIdPo = new ProAllIdPo();
        proAllIdPo.setProjId(checkObjId);
        return proAllIdPo;
    }
}

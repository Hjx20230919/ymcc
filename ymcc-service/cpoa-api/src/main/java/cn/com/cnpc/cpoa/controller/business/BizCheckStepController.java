package cn.com.cnpc.cpoa.controller.business;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.bid.BidProjectService;
import cn.com.cnpc.cpoa.service.prodsys.BizDealPsCheckStepService;
import cn.com.cnpc.cpoa.service.prodsys.BizProjectCheckStepService;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.DiyStepParamVo;
import cn.com.cnpc.cpoa.web.base.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 18:11
 * @Description: 自定义审核流程
 */
@RestController
@RequestMapping("/step")
public class BizCheckStepController extends BaseController {

    @Autowired
    private BizCheckStepService diyCheckStepService;

    @Autowired
    private BizDealPsCheckStepService diyDealPsCheckStepService;

    @Autowired
    private BizProjectCheckStepService diyBizProjectCheckStepService;

    @Autowired
    private BidProjectService projectService;

    @Log(logContent = "新增自定义审核流程", logModule = LogModule.STEP, logType = LogType.OPERATION)
    @RequestMapping(value = "diy", method = POST)
    public AppMessage add(@RequestBody AuditVo auditVo) throws Exception{
        String userId = ServletUtils.getSessionUserId();
        List<DiyStepParamVo> diyStepParamVos = auditVo.getDiyStepParamVo();
        if (CheckTypeEnum.INSTRUCTION.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.DELEGATE.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.MULTIPROJECT.getKey().equals(auditVo.getObjectType())) {
            auditVo.setDealId(auditVo.getObjId());
        }
        for (DiyStepParamVo diyStepParamVo : diyStepParamVos) {
            if (auditVo.getObjectType().equals("deal") || auditVo.getObjectType().equals("dealps") || auditVo.getObjectType().equals("instruction")) {
                if(auditVo.getDealId() != null) {
                    diyStepParamVo.setCheckObjId(auditVo.getDealId());
                } else {
                    diyStepParamVo.setCheckObjId(auditVo.getObjId());
                }
            } else if (auditVo.getObjectType().equals("settle")) {
                if (auditVo.getSettleId() != null) {
                    diyStepParamVo.setCheckObjId(auditVo.getSettleId());
                } else {
                    diyStepParamVo.setCheckObjId(auditVo.getObjId());
                }
            } else {
                diyStepParamVo.setCheckObjId(auditVo.getObjId());
            }
        }
        if (CheckTypeEnum.DEALPS.getKey().equals(auditVo.getObjectType())) {
            if (diyDealPsCheckStepService.saveDiyCheckStep(auditVo, userId)) {
                return AppMessage.result("新增自定义审核流程成功");
            }
        } else if (CheckTypeEnum.INSTRUCTION.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.DELEGATE.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.MULTIPROJECT.getKey().equals(auditVo.getObjectType())) {
            if (diyBizProjectCheckStepService.saveDiyCheckStep(auditVo, userId)) {
                return AppMessage.result("新增自定义审核流程成功");
            }
        } else if (CheckTypeEnum.BIDPROJECT.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.CERTIBORROW.getKey().equals(auditVo.getObjectType())) {
            if(projectService.saveProDiyCheckStep(auditVo,userId)){
                return AppMessage.result("新增自定义审核流程成功");
            }
        }else if (CheckTypeEnum.PROPROJECT.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.SELCONT.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.PURCHASE.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.REPURCHASE.getKey().equals(auditVo.getObjectType())) {
            if(diyCheckStepService.saveProDiyCheckStep(auditVo,userId)){
                return AppMessage.result("新增自定义审核流程成功");
            }
        } else if (CheckTypeEnum.ACCESS.getKey().equals(auditVo.getObjectType())
                ||CheckTypeEnum.CREDITSET.getKey().equals(auditVo.getObjectType())) {
            if(diyCheckStepService.saveContDiyCheckStep(auditVo,userId)){
                return AppMessage.result("新增自定义审核流程成功");
            }
        } else {
            if (diyCheckStepService.saveDiyCheckStep(auditVo, userId)) {
                return AppMessage.result("新增自定义审核流程成功");
            }
        }
        return AppMessage.error("新增自定义审核流程失败");
    }

    /**
     * 查询审核详情
     *
     * @param dealId
     * @param settleId
     * @return
     */
    @RequestMapping(value = "/details", method = RequestMethod.GET)
    public AppMessage queryDetails(@RequestParam(value = "dealId", defaultValue = "") String dealId,
                                   @RequestParam(value = "settleId", defaultValue = "") String settleId) {
        String userId = ServletUtils.getSessionUserId();
        List<CheckStepPo> checkStepPoList = diyCheckStepService.selectDetails(dealId, settleId, userId);
        return AppMessage.success(checkStepPoList, "查询审核详情成功！");
    }


    /**
     * 审核操作
     *
     * @return
     */
    @Log(logContent = "审核操作", logModule = LogModule.STEP, logType = LogType.OPERATION)
    @RequestMapping(value = "/audit", method = RequestMethod.POST)
    public AppMessage audit(@RequestBody AuditVo auditVo) {
        boolean isSuccess = false;
        if (CheckTypeEnum.DEALPS.getKey().equals(auditVo.getObjectType())) {
            isSuccess = diyDealPsCheckStepService.audit(auditVo);
        } else if (CheckTypeEnum.INSTRUCTION.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.DELEGATE.getKey().equals(auditVo.getObjectType())
                || CheckTypeEnum.MULTIPROJECT.getKey().equals(auditVo.getObjectType())) {
            isSuccess = diyBizProjectCheckStepService.audit(auditVo);
        } else {
            isSuccess = diyCheckStepService.audit(auditVo);
        }

        if (!isSuccess) {
            AppMessage.error("审核失败");
        }

        return AppMessage.success(isSuccess, "审核成功！");
    }


}

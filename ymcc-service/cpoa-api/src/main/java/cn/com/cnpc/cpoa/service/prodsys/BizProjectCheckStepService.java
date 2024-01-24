package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.enums.CheckStepStateEnum;
import cn.com.cnpc.cpoa.service.CheckManService;
import cn.com.cnpc.cpoa.service.CheckStepService;
import cn.com.cnpc.cpoa.vo.AuditVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * <>
 *
 * @author wangjun
 * @create 06/04/2020 19:56
 * @since 1.0.0
 */
@Service
public class BizProjectCheckStepService {

    @Autowired
    private CheckStepService checkStepService;

    @Autowired
    private BizProjectApprovalService projectApprovalService;

    @Autowired
    CheckManService checkManService;

    @Autowired
    BizProjectService projectService;

    @Transactional
    public boolean saveDiyCheckStep(AuditVo auditVo, String userId) {

        return projectApprovalService.buildDiyActiviti(auditVo, userId);
    }

    public String selectMaxStepNo(String objId) {
        Map<String, Object> param = new HashMap<>();
        param.put("objId", objId);
        String stepNo = checkStepService.selectMaxStepNo(param);
        return stepNo;
    }


    public boolean audit(AuditVo auditVo) {
        synchronized (this) {
            String auditStatus = auditVo.getAuditStatus();
            String contractId = auditVo.getDealId();
            BizProjectDto projectDto = projectService.selectByKey(contractId);
            if (projectDto == null) {
                throw new AppException("当前项目开工不存在");
            }
            if (CheckStepStateEnum.PASS.getKey().equalsIgnoreCase(auditStatus)) {
                //通过
                projectApprovalService.passActiviti(auditVo);
            } else if (CheckStepStateEnum.REFUSE.getKey().equalsIgnoreCase(auditStatus)) {
                //拒绝
                projectApprovalService.backActiviti(auditVo);
            } else {
                throw new AppException("审核出错！审核状态不正确");
            }
        }
        return true;
    }


}

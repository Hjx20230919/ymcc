package cn.com.cnpc.cpoa.vo.project;

import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.AuditVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/13 21:36
 * @Description:
 */
@Data
public class ProjPurcPlanVo {

    /**
     * 采购方案ID
     */
    private String planId;

    /**
     * 拟签订合同名称
     */
    private String dealName;

    private BigDecimal dealValue;

    /**
     * 立项项目ID
     */
    private String projId;
    private String projName;
    /**
     * 选商审批表ID
     */
    private String selContId;

    /**
     * 编号
     */
    private String planNo;

    /**
     * 金额单位
     */
    private String amountUnit;

    /**
     * 经办人
     */
    private String createId;
    private String userName;


    /**
     * 经办部门
     */
    private String ownerDeptId;
    private String deptName;


    /**
     * 项目状态
     */
    private String projStatus;

    /**
     * 采购方案ID
     */
    private String projPhase;

    /**
     * 创建时间
     */
    private String createAt;

    /**
     * 采购方案说明
     */
    private String planNotes;

    private List<AttachVo> attachVos;

    List<ProjPlanListVo> projPlanListVos;

    private String resultId;

    private AuditVo auditVo;

    /**
     * 邀请服务商理由
     */
    private String recomContReason;

}

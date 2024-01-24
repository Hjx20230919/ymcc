package cn.com.cnpc.cpoa.vo.project;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:37
 * @Description:
 */
@Data
public class ProjProjectPlanVo {
    /**
     * 选商审批表ID
     */
    private String planId;

    private String selContId;

    private String userName;

    private String deptName;

    private String selContType;

    /**
     * 工程/服务名称
     */
    private String projName;
    private String projId;
    /**
     * 预计合同金额
     */
    private BigDecimal dealValue;

    private String dealNo;

    /**
     * 拟签订合同名称
     */
    private String dealName;


    /**
     * 项目状态
     */
    private String projStatus;

    /**
     * 项目阶段
     */
    private String projPhase;

    /**
     * 创建时间
     */
    private String createAt;

    private Integer attachNum;

    private String contQlyReq;

    private String planNotes;


    private String amountUnit;

    private String planNo;

    private List<String> curAuditUser;

    private String payType;

    private String resultId;

    /**
     * 1为冻结，0为解冻
     */
    @Transient
    private String freezeStatus;

}

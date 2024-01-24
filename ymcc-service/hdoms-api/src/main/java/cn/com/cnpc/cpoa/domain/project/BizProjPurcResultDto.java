package cn.com.cnpc.cpoa.domain.project;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/12/9 11:03
 * @Description:
 */
@Data
@Table(name = "T_PROJ_PURC_RESULT")
public class BizProjPurcResultDto {



    @Id
    @Column(name = "RESULT_ID")
    private String resultId;

    /**
     * 采购方案ID
     */
    @Column(name = "PLAN_ID")
    private String planId;


    /**
     * 选商审批表ID
     */
    @Column(name = "SEL_CONT_ID")
    private String selContId;

    /**
     * 立项项目ID
     */
    @Column(name = "PROJ_ID")
    private String projId;


    /**
     * 编号
     */
    @Column(name = "PLAN_NO")
    private String planNo;

    /**
     * 金额单位
     */
    @Column(name = "AMOUNT_UNIT")
    private String amountUnit;

    /**
     * 经办人
     */
    @Column(name = "CREATE_ID")
    private String createId;

    /**
     * 经办部门
     */
    @Column(name = "OWNER_DEPT_ID")
    private String ownerDeptId;

    /**
     * 项目状态
     */
    @Column(name = "PROJ_STATUS")
    private String projStatus;

    /**
     * 采购方案ID
     */
    @Column(name = "PROJ_PHASE")
    private String projPhase;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_AT")
    private Date createAt;

    /**
     * 采购方案说明
     */
    @Column(name = "RESULT_NOTES")
    private String resultNotes;

    @Transient
    private BigDecimal dealValue;

}

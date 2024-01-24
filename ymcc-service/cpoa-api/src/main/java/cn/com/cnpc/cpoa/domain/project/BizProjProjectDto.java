package cn.com.cnpc.cpoa.domain.project;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 19:09
 * @Description:o'p
 */
@Data
@Table(name = "T_PROJ_PROJECT")
public class BizProjProjectDto {

    /**
     * 立项项目ID
     */
    @Id
    @Column(name = "PROJ_ID")
    private String projId;

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
     * 工程/服务名称
     */
    @Column(name = "PROJ_NAME")
    private String projName;

    /**
     * 预计合同金额
     */
    @Column(name = "DEAL_VALUE")
    private BigDecimal dealValue;

    /**
     * 拟签订合同名称
     */
    @Column(name = "DEAL_NAME")
    private String dealName;


    /**
     * 编号
     */
    @Column(name = "DEAL_NO")
    private String dealNo;

    /**
     * 我方签约单位
     */
    @Column(name = "DEAL_CONTRACT")
    private String dealContract;

    /**
     * 资金支出渠道
     */
    @Column(name = "PAY_TYPE")
    private String payType;

    /**
     * 选商方式
     */
    @Column(name = "SEL_CONT_TYPE")
    private String selContType;

    /**
     * 对应收入情况
     */
    @Column(name = "INCOME_INFO")
    private String incomeInfo;

    /**
     * 服务合同立项原因描述
     */
    @Column(name = "APPL_DESC")
    private String applDesc;

    /**
     * 承包商资质要求
     */
    @Column(name = "CONT_QLY_REQ")
    private String contQlyReq;

    /**
     * 承包商技术服务人员要求
     */
    @Column(name = "CONT_SVR_REQ")
    private String contSvrReq;

    /**
     * 预计合同金额测算情况
     */
    @Column(name = "DEAL_VALUE_MEASURE")
    private String dealValueMeasure;

    /**
     * 项目状态
     */
    @Column(name = "PROJ_STATUS")
    private String projStatus;

    /**
     * 项目阶段
     */
    @Column(name = "PROJ_PHASE")
    private String projPhase;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_AT")
    private Date createAt;

    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;

    /**
     * 立项理由
     */
    @Column(name = "PROJ_NOTES")
    private String projNotes;


    @Column(name = "RESPONSIBILITY_MAN")
    private String responsibilityMan;

    @Column(name = "RESPONSIBILITY_PHONE")
    private String responsibilityPhone;

    @Column(name = "PROJ_PLAN_NO")
    private String projPlanNo;


    @Transient
    private String userName;

    @Transient
    private String selContId;

    /**
     * 承包商参与项目状态
     */
    @Transient
    private String joinStatus;

    /**
     * 参与项目承包商名称
     */
    @Transient
    private String joinName;

    /**
     * 承包商中标项目状态
     */
    @Transient
    private String bidStatus;

    /**
     * 中标承包商名称
     */
    @Transient
    private String bidName;


    /**
     * 1为冻结，0为解冻
     */
    @Transient
    private String freezeStatus;

    @Transient
    private String deptName;

    @Transient
    private List<String> curAuditUser;

    @Transient
    private Integer attachNum;

    @Transient
    private String planId;

    @Transient
    private String resultId;

    @Transient
    private String resultNotes;

    @Transient
    private String planNotes;


    @Transient
    private String amountUnit;

    @Transient
    private String planNo;

    @Transient
    private String contId;

    /***公开招标*/
    @Transient
    private String otenderId;
    /**
     * 工程项目/物资采购/服务
     */


    @Transient
    private String otenderProjType;
    @Transient
    private String otenderPlanNo;

    /**
     * 一类 二类 三类 四类
     */
    @Transient
    private String otenderType;

    /**
     * 公开招标/邀请招标
     */
    @Transient
    private String otenderTenderType;

    @Transient
    private Date otenderStartDate;

    @Transient
    private Date otenderEndDate;

    @Transient
    private String otenderValutionType;

    @Transient
    private String otenderModality;

    @Transient
    private String otenderSupervise;

    @Transient
    private String otenderContent;

    @Transient
    private String otenderCommittee;

    @Transient
    private String otenderQualifications;


    /**可不招标**/
    @Transient
    private String ntenderId;

    /**
     * 工程项目/物资采购/服务
     */
    @Transient
    private String ntenderProjType;

    @Transient
    private String ntenderPlanNo;

    /**
     * 一类 二类 三类 四类
     */
    @Transient
    private String ntenderType;

    @Transient
    private String ntenderPurchaseType;

    @Transient
    private Date ntenderStartDate;

    @Transient
    private Date ntenderEndDate;

    @Transient
    private String ntenderValutionType;

    @Transient
    private String ntenderGroup;

    @Transient
    private String ntenderSummary;

    @Transient
    private String ntenderReason;

    @Transient
    private String ntenderSupervise;


}

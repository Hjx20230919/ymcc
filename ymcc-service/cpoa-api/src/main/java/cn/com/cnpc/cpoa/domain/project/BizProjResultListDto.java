package cn.com.cnpc.cpoa.domain.project;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:04
 * @Description:
 */
@Data
@Table(name = "T_PROJ_RESULT_LIST")
public class BizProjResultListDto {


    /**
     * 采购计划明细ID
     */
    @Id
    @Column(name = "RESULT_LIST_ID")
    private String resultListId;

    /**
     * 采购方案ID
     */
    @Column(name = "RESULT_ID")
    private String resultId;

    /**
     * 拟签合同名称
     */
    @Column(name = "DEAL_NAME")
    private String dealName;

    /**
     * 服务内容
     */
    @Column(name = "SERVICE_CONTEXT")
    private String serviceContext;

    /**
     * 计费内容
     */
    @Column(name = "BILL_CONTEXT")
    private String billContext;

    /**
     * 计价方式
     */
    @Column(name = "VALUATION_CONTEXT")
    private String valuationContext;

    /**
     * 单价
     */
    @Column(name = "UNIT_PRICE")
    private BigDecimal unitPrice;

    /**
     * 控制总价
     */
    @Column(name = "LIMIT_TOTAL_PRICE")
    private BigDecimal limitTotalPrice;

    /**
     * 服务地点
     */
    @Column(name = "SERVICE_PLACE")
    private String servicePlace;

    /**
     * 邀请服务商
     */
    @Column(name = "CONT_ID")
    private String contId;

    @Transient
    private String contName;
    /**
     * 推荐服务商理由
     */
    @Column(name = "RECOM_CONT_REASON")
    private String recomContReason;

    /**
     * 采购方式
     */
    @Column(name = "SEL_CONT_TYPE")
    private String selContType;

    /**
     * 资金渠道
     */
    @Column(name = "PAY_TYPE")
    private String payType;

    /**
     * 计划来源
     */
    @Column(name = "PLAN_SOURCE")
    private String planSource;

    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;

    @Column(name = "COUNT")
    private Float count;
}

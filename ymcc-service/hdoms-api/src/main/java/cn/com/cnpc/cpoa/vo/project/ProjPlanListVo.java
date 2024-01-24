package cn.com.cnpc.cpoa.vo.project;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:04
 * @Description:
 */
@Data
public class ProjPlanListVo {


    /**
     * 采购计划明细ID
     */
    private String planListId;

    /**
     * 采购方案ID
     */
    private String planId;

    /**
     * 拟签合同名称
     */
    private String dealName;

    /**
     * 服务内容
     */
    private String serviceContext;

    /**
     * 计费内容
     */
    private String billContext;

    /**
     * 计价方式
     */
    private String valuationContext;

    /**
     * 预计单价
     */
    private BigDecimal estUnitPrice;

    /**
     * 预计总价
     */
    private BigDecimal estTotalPrice;

    /**
     * 服务地点
     */
    private String servicePlace;


    /**
     * 推荐服务商理由
     */
    private String recomContReason;

    /**
     * 采购方式
     */
    private String selContType;

    /**
     * 资金渠道
     */
    private String payType;

    /**
     * 计划来源
     */
    private String planSource;

    /**
     * 备注
     */
    private String notes;

    private List<ProjPlanContVo> projPlanContVos;

    private Float count;
}

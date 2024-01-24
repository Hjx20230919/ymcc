package cn.com.cnpc.cpoa.vo.project;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:04
 * @Description:
 */
@Data
public class ProjRsultListVo {



    /**
     * 采购计划明细ID
     */
    private String resultListId;

    /**
     * 采购方案ID
     */
    private String resultId;

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
     * 单价
     */
    private BigDecimal estUnitPrice;

    /**
     * 控制总价
     */
    private BigDecimal estTotalPrice;

    /**
     * 服务地点
     */
    private String servicePlace;

    /**
     * 邀请服务商
     */
    private String contId;
    private String contName;
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

    private Float count;

}

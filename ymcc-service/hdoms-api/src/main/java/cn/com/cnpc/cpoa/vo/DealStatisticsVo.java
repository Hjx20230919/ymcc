package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: 17742856263
 * @Date: 2020/7/5 11:07
 * @Description:
 */
@Data
public class DealStatisticsVo {

    private String statId;

    /**
     * ("XS","线上合同"),("NZ","内责书"),("TH","3万以下"),("XX","线下合同")
     */
    private String statType;

    private Integer statOrder;

    private String dealIncome;

    private String deptId;

    private String dealId;

    private String dealContract;

    private String dealReportNo;

    private String dealName;

    private String contName;
    private String contId;
    private String dealStart;

    private String dealEnd;

    private BigDecimal dealValue;

    private BigDecimal dealSettleLast;

    private BigDecimal dealSettleNow;

    private BigDecimal dealSettle;

    private BigDecimal dealSettleProgress;

    private BigDecimal settleLast;

    private BigDecimal settleNow;

    private BigDecimal settle;

    private BigDecimal settleProgress;

    private BigDecimal notSettleLast;

    private BigDecimal notSettle;

    private String marketDist;

    private String note;

    private String createTime;

    private String updateTime;

    private String createId;

    private String deptName;


    private String dealSettleWay;

    private String isDealSettleDone;

    private String settleWay;

    private String isSettleDone;

    private BigDecimal expectIncomeNow;

    private BigDecimal expectIncomeHalf;

    private String changesReason;
    private String changeAnalysis;

    private BigDecimal expectSettleNow;
    private String dealNo;
}

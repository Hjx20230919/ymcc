package cn.com.cnpc.cpoa.po;

import lombok.Data;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-27  09:05
 * @Description:
 * @Version: 1.0
 */
@Data
public class BizDealStatisticsHisPo {

    private String statId;

    /**
     * ("XS","线上合同"),("NZ","内责书"),("TH","3万以下"),("XX","线下合同")
     */
    private String statType;

    private Integer statOrder;

    private String dealId;

    private String dealNo;

    private String dealIncome;

    /**
     * YYYY-MM
     */
    private String yearMonh;

    private String deptId;

    private String dealContract;

    private String dealReportNo;

    private String dealName;

    private String contId;

    private String contName;

    private String dealStart;

    private String dealEnd;

    private BigDecimal dealValue;

    private BigDecimal settleProgress;

    private BigDecimal settle;

    private BigDecimal settleNow;

    private BigDecimal settleLast;

    private BigDecimal dealSettleProgress;

    private BigDecimal dealSettle;

    private BigDecimal dealSettleNow;

    private BigDecimal dealSettleLast;

    private String dealSettleWay;

    private String isDealSettleDone;

    private String settleWay;

    private String isSettleDone;

    private BigDecimal notSettleLast;

    private BigDecimal notSettle;

    private String marketDist;

    private String createTime;

    private String updateTime;

    private String createId;

    private BigDecimal expectIncomeNow;

    private BigDecimal expectIncomeHalf;

    private BigDecimal expectSettleNow;

    private String note;

    private String changesReason;

    private String changeAnalysis;


    private String deptName;
}

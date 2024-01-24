package cn.com.cnpc.cpoa.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class DealDataDto {
    private String dealId;
    private String dealNo;
    private String projectNo;
    private String dealName;
    private BigDecimal dealValue;
    private String categoryId;
    private Date dealSignTime;
    private String deptId;
    private String contractId;
    private String dealIncome;
    private String dealFunds;
    private String dealReportNo;
    private String dealContract;
    private String dealDispute;
    private String userId;
    private Date dealStart;
    private Date dealEnd;
    private String dealSelection;
    private BigDecimal dealSettlement;
    private Date settleDate;
    private String dealNotes;
    private Date createdAt;
    private String dealCurrency;
    private String subtypeId;
    private String dealStatus;
    private String dealType;
    private String paymentType;
    private String paymentReq;
    private BigDecimal dealValueAfter;
    private BigDecimal dealValueBefore;
    private Integer haveTax;
    private Integer taxRate;
    private String changeDesc;
    private Date placedTime;
    private String dealBiz;
    private String workZone;
    private String companyType;
    private String contractType;
    private String marketType;
    private String contName;
}

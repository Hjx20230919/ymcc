package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import javax.persistence.Column;
import java.util.Date;
@Data
public class DealexVo {
    private String dealId;

    private String dealNo;

    private String projectNo;

    private String dealName;

    private Double dealValue;

    private String categoryId;

    private Date dealSignTime;

    private String deptId;

    private String contractId;

    /**
     * INCOME   收入
     OUTCOME  支出
     NONE  不涉及
     */
    private String dealIncome;

    private String dealFunds;

    private String dealReportNo;

    /**
     * 固定：
     川庆钻探工程公司安全环保质量监督检测研究院
     四川科特检测技术有限公司
     */

    private String dealContract;


    private String dealDispute;


    private String userId;

    private Date dealStart;

    private Date dealEnd;

    private String dealSelection;

    private Double dealSettlement;

    private Date settleDate;

    private String dealNotes;

    private Date createAt;

    private String dealCurrency;

    /**
     * 业务类型
     */
    private String subtypeId;

    /**
     * 以下状态：

     立项：【草稿  、退回、 立项审核中】
     履行：【履行中】
     变更：【变更审核中】
     归档：【归档中、归档完毕】
     */
    private String dealStatus;

    /**
     * 集团合同    TJ
     内责书        NZ
     3万以下      TH
     线下合同     XX
     */
    private String dealType;

    private String paymentType;

    private String paymentReq;

    private Double dealValueAfter;

    private Double dealValueBefore;

    private Integer haveTax;

    private Integer taxRate;

    private String changeDesc;

    private Date placedTime;
}

package cn.com.cnpc.cpoa.domain;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "V_DEAL_STTLE")
public class VDealSttleDto {
    @Column(name = "DEAL_ID")
    private String dealId;

    @Column(name = "DEAL_NO")
    private String dealNo;

    @Column(name = "DEAL_NAME")
    private String dealName;

    @Column(name = "DEAL_VALUE")
    private BigDecimal dealValue;

    @Column(name = "CATEGORY_ID")
    private String categoryId;

    @Column(name = "DEAL_SIGN_TIME")
    private Date dealSignTime;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "CONTRACT_ID")
    private String contractId;

    /**
     * INCOME   收入
            OUTCOME  支出
            NONE  不涉及
     */
    @Column(name = "DEAL_INCOME")
    private String dealIncome;

    @Column(name = "DEAL_FUNDS")
    private String dealFunds;

    @Column(name = "DEAL_REPORT_NO")
    private String dealReportNo;

    /**
     * 固定：
            川庆钻探工程公司安全环保质量监督检测研究院
            四川科特检测技术有限公司
     */
    @Column(name = "DEAL_CONTRACT")
    private String dealContract;

    @Column(name = "DEAL_DISPUTE")
    private String dealDispute;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "DEAL_START")
    private Date dealStart;

    @Column(name = "DEAL_END")
    private Date dealEnd;

    @Column(name = "DEAL_SELECTION")
    private String dealSelection;

    @Column(name = "DEAL_SETTLEMENT")
    private BigDecimal dealSettlement;

    @Column(name = "SETTLE_DATE")
    private Date settleDate;

    @Column(name = "DEAL_NOTES")
    private String dealNotes;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "DEAL_CURRENCY")
    private String dealCurrency;

    /**
     * 业务类型
     */
    @Column(name = "SUBTYPE_ID")
    private String subtypeId;

    /**
     * 以下状态：
            
            立项：【草稿  、退回、 立项审核中】  
            履行：【履行中】
            变更：【变更审核中】  
            归档：【归档中、归档完毕】
     */
    @Column(name = "DEAL_STATUS")
    private String dealStatus;

    /**
     * 集团合同    TJ
            内责书        NZ
            3万以下      TH
            线下合同     XX
     */
    @Column(name = "DEAL_TYPE")
    private String dealType;

    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    @Column(name = "PAYMENT_REQ")
    private String paymentReq;

    @Column(name = "DEAL_VALUE_AFTER")
    private BigDecimal dealValueAfter;

    @Column(name = "DEAL_VALUE_BEFORE")
    private BigDecimal dealValueBefore;

    @Column(name = "HAVE_TAX")
    private Integer haveTax;

    @Column(name = "TAX_RATE")
    private Integer taxRate;

    /**
     * 签定年
     */
    private Integer signYear;

    /**
     * 签定年结算
     */
    private BigDecimal signYearSettle;

    /**
     * 往年结算
     */
    private BigDecimal settleLast;

    /**
     * 本年结算
     */
    private BigDecimal settleNow;

    /**
     * 合同历史
     */
    private String dealHis;

    /**
     * @return DEAL_ID
     */
    public String getDealId() {
        return dealId;
    }

    /**
     * @param dealId
     */
    public void setDealId(String dealId) {
        this.dealId = dealId == null ? null : dealId.trim();
    }

    /**
     * @return DEAL_NO
     */
    public String getDealNo() {
        return dealNo;
    }

    /**
     * @param dealNo
     */
    public void setDealNo(String dealNo) {
        this.dealNo = dealNo == null ? null : dealNo.trim();
    }

    /**
     * @return DEAL_NAME
     */
    public String getDealName() {
        return dealName;
    }

    /**
     * @param dealName
     */
    public void setDealName(String dealName) {
        this.dealName = dealName == null ? null : dealName.trim();
    }

    /**
     * @return DEAL_VALUE
     */
    public BigDecimal getDealValue() {
        return dealValue;
    }

    /**
     * @param dealValue
     */
    public void setDealValue(BigDecimal dealValue) {
        this.dealValue = dealValue;
    }

    /**
     * @return CATEGORY_ID
     */
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * @param categoryId
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId == null ? null : categoryId.trim();
    }

    /**
     * @return DEAL_SIGN_TIME
     */
    public Date getDealSignTime() {
        return dealSignTime;
    }

    /**
     * @param dealSignTime
     */
    public void setDealSignTime(Date dealSignTime) {
        this.dealSignTime = dealSignTime;
    }

    /**
     * @return DEPT_ID
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
    }

    /**
     * @return CONTRACT_ID
     */
    public String getContractId() {
        return contractId;
    }

    /**
     * @param contractId
     */
    public void setContractId(String contractId) {
        this.contractId = contractId == null ? null : contractId.trim();
    }

    /**
     * 获取INCOME   收入
            OUTCOME  支出
            NONE  不涉及
     *
     * @return DEAL_INCOME - INCOME   收入
            OUTCOME  支出
            NONE  不涉及
     */
    public String getDealIncome() {
        return dealIncome;
    }

    /**
     * 设置INCOME   收入
            OUTCOME  支出
            NONE  不涉及
     *
     * @param dealIncome INCOME   收入
            OUTCOME  支出
            NONE  不涉及
     */
    public void setDealIncome(String dealIncome) {
        this.dealIncome = dealIncome == null ? null : dealIncome.trim();
    }

    /**
     * @return DEAL_FUNDS
     */
    public String getDealFunds() {
        return dealFunds;
    }

    /**
     * @param dealFunds
     */
    public void setDealFunds(String dealFunds) {
        this.dealFunds = dealFunds == null ? null : dealFunds.trim();
    }

    /**
     * @return DEAL_REPORT_NO
     */
    public String getDealReportNo() {
        return dealReportNo;
    }

    /**
     * @param dealReportNo
     */
    public void setDealReportNo(String dealReportNo) {
        this.dealReportNo = dealReportNo == null ? null : dealReportNo.trim();
    }

    /**
     * 获取固定：
            川庆钻探工程公司安全环保质量监督检测研究院
            四川科特检测技术有限公司
     *
     * @return DEAL_CONTRACT - 固定：
            川庆钻探工程公司安全环保质量监督检测研究院
            四川科特检测技术有限公司
     */
    public String getDealContract() {
        return dealContract;
    }

    /**
     * 设置固定：
            川庆钻探工程公司安全环保质量监督检测研究院
            四川科特检测技术有限公司
     *
     * @param dealContract 固定：
            川庆钻探工程公司安全环保质量监督检测研究院
            四川科特检测技术有限公司
     */
    public void setDealContract(String dealContract) {
        this.dealContract = dealContract == null ? null : dealContract.trim();
    }

    /**
     * @return DEAL_DISPUTE
     */
    public String getDealDispute() {
        return dealDispute;
    }

    /**
     * @param dealDispute
     */
    public void setDealDispute(String dealDispute) {
        this.dealDispute = dealDispute == null ? null : dealDispute.trim();
    }

    /**
     * @return USER_ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * @return DEAL_START
     */
    public Date getDealStart() {
        return dealStart;
    }

    /**
     * @param dealStart
     */
    public void setDealStart(Date dealStart) {
        this.dealStart = dealStart;
    }

    /**
     * @return DEAL_END
     */
    public Date getDealEnd() {
        return dealEnd;
    }

    /**
     * @param dealEnd
     */
    public void setDealEnd(Date dealEnd) {
        this.dealEnd = dealEnd;
    }

    /**
     * @return DEAL_SELECTION
     */
    public String getDealSelection() {
        return dealSelection;
    }

    /**
     * @param dealSelection
     */
    public void setDealSelection(String dealSelection) {
        this.dealSelection = dealSelection == null ? null : dealSelection.trim();
    }

    /**
     * @return DEAL_SETTLEMENT
     */
    public BigDecimal getDealSettlement() {
        return dealSettlement;
    }

    /**
     * @param dealSettlement
     */
    public void setDealSettlement(BigDecimal dealSettlement) {
        this.dealSettlement = dealSettlement;
    }

    /**
     * @return SETTLE_DATE
     */
    public Date getSettleDate() {
        return settleDate;
    }

    /**
     * @param settleDate
     */
    public void setSettleDate(Date settleDate) {
        this.settleDate = settleDate;
    }

    /**
     * @return DEAL_NOTES
     */
    public String getDealNotes() {
        return dealNotes;
    }

    /**
     * @param dealNotes
     */
    public void setDealNotes(String dealNotes) {
        this.dealNotes = dealNotes == null ? null : dealNotes.trim();
    }

    /**
     * @return CREATE_AT
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * @return DEAL_CURRENCY
     */
    public String getDealCurrency() {
        return dealCurrency;
    }

    /**
     * @param dealCurrency
     */
    public void setDealCurrency(String dealCurrency) {
        this.dealCurrency = dealCurrency == null ? null : dealCurrency.trim();
    }

    /**
     * 获取业务类型
     *
     * @return SUBTYPE_ID - 业务类型
     */
    public String getSubtypeId() {
        return subtypeId;
    }

    /**
     * 设置业务类型
     *
     * @param subtypeId 业务类型
     */
    public void setSubtypeId(String subtypeId) {
        this.subtypeId = subtypeId == null ? null : subtypeId.trim();
    }

    /**
     * 获取以下状态：
            
            立项：【草稿  、退回、 立项审核中】  
            履行：【履行中】
            变更：【变更审核中】  
            归档：【归档中、归档完毕】
     *
     * @return DEAL_STATUS - 以下状态：
            
            立项：【草稿  、退回、 立项审核中】  
            履行：【履行中】
            变更：【变更审核中】  
            归档：【归档中、归档完毕】
     */
    public String getDealStatus() {
        return dealStatus;
    }

    /**
     * 设置以下状态：
            
            立项：【草稿  、退回、 立项审核中】  
            履行：【履行中】
            变更：【变更审核中】  
            归档：【归档中、归档完毕】
     *
     * @param dealStatus 以下状态：
            
            立项：【草稿  、退回、 立项审核中】  
            履行：【履行中】
            变更：【变更审核中】  
            归档：【归档中、归档完毕】
     */
    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus == null ? null : dealStatus.trim();
    }

    /**
     * 获取集团合同    TJ
            内责书        NZ
            3万以下      TH
            线下合同     XX
     *
     * @return DEAL_TYPE - 集团合同    TJ
            内责书        NZ
            3万以下      TH
            线下合同     XX
     */
    public String getDealType() {
        return dealType;
    }

    /**
     * 设置集团合同    TJ
            内责书        NZ
            3万以下      TH
            线下合同     XX
     *
     * @param dealType 集团合同    TJ
            内责书        NZ
            3万以下      TH
            线下合同     XX
     */
    public void setDealType(String dealType) {
        this.dealType = dealType == null ? null : dealType.trim();
    }

    /**
     * @return PAYMENT_TYPE
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType == null ? null : paymentType.trim();
    }

    /**
     * @return PAYMENT_REQ
     */
    public String getPaymentReq() {
        return paymentReq;
    }

    /**
     * @param paymentReq
     */
    public void setPaymentReq(String paymentReq) {
        this.paymentReq = paymentReq == null ? null : paymentReq.trim();
    }

    /**
     * @return DEAL_VALUE_AFTER
     */
    public BigDecimal getDealValueAfter() {
        return dealValueAfter;
    }

    /**
     * @param dealValueAfter
     */
    public void setDealValueAfter(BigDecimal dealValueAfter) {
        this.dealValueAfter = dealValueAfter;
    }

    /**
     * @return DEAL_VALUE_BEFORE
     */
    public BigDecimal getDealValueBefore() {
        return dealValueBefore;
    }

    /**
     * @param dealValueBefore
     */
    public void setDealValueBefore(BigDecimal dealValueBefore) {
        this.dealValueBefore = dealValueBefore;
    }

    /**
     * @return HAVE_TAX
     */
    public Integer getHaveTax() {
        return haveTax;
    }

    /**
     * @param haveTax
     */
    public void setHaveTax(Integer haveTax) {
        this.haveTax = haveTax;
    }

    /**
     * @return TAX_RATE
     */
    public Integer getTaxRate() {
        return taxRate;
    }

    /**
     * @param taxRate
     */
    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }

    public Integer getSignYear() {
        return signYear;
    }

    public void setSignYear(Integer signYear) {
        this.signYear = signYear;
    }

    public BigDecimal getSignYearSettle() {
        return signYearSettle;
    }

    public void setSignYearSettle(BigDecimal signYearSettle) {
        this.signYearSettle = signYearSettle;
    }

    public BigDecimal getSettleLast() {
        return settleLast;
    }

    public void setSettleLast(BigDecimal settleLast) {
        this.settleLast = settleLast;
    }

    public BigDecimal getSettleNow() {
        return settleNow;
    }

    public void setSettleNow(BigDecimal settleNow) {
        this.settleNow = settleNow;
    }

    public String getDealHis() {
        return dealHis;
    }

    public void setDealHis(String dealHis) {
        this.dealHis = dealHis;
    }
}
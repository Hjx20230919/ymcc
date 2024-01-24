package cn.com.cnpc.cpoa.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
@Data
@Table(name = "T_BIZ_DEAL_STATISTICS_HIS")
public class BizDealStatisticsHisDto {
    @Column(name = "STAT_ID")
    private String statId;

    /**
     * ("XS","线上合同"),("NZ","内责书"),("TH","3万以下"),("XX","线下合同")
     */
    @Column(name = "STAT_TYPE")
    private String statType;

    @Column(name = "STAT_ORDER")
    private Integer statOrder;

    @Column(name = "DEAL_ID")
    private String dealId;

    @Column(name = "DEAL_NO")
    private String dealNo;

    @Column(name = "DEAL_INCOME")
    private String dealIncome;

    /**
     * YYYY-MM
     */
    @Column(name = "YEAR_MONH")
    private String yearMonh;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "DEAL_CONTRACT")
    private String dealContract;

    @Column(name = "DEAL_REPORT_NO")
    private String dealReportNo;

    @Column(name = "DEAL_NAME")
    private String dealName;

    @Column(name = "CONT_ID")
    private String contId;

    @Column(name = "CONT_NAME")
    private String contName;

    @Column(name = "DEAL_START")
    private Date dealStart;

    @Column(name = "DEAL_END")
    private Date dealEnd;

    @Column(name = "DEAL_VALUE")
    private BigDecimal dealValue;

    @Column(name = "SETTLE_PROGRESS")
    private BigDecimal settleProgress;

    @Column(name = "SETTLE")
    private BigDecimal settle;

    @Column(name = "SETTLE_NOW")
    private BigDecimal settleNow;

    @Column(name = "SETTLE_LAST")
    private BigDecimal settleLast;

    @Column(name = "DEAL_SETTLE_PROGRESS")
    private BigDecimal dealSettleProgress;

    @Column(name = "DEAL_SETTLE")
    private BigDecimal dealSettle;

    @Column(name = "DEAL_SETTLE_NOW")
    private BigDecimal dealSettleNow;

    @Column(name = "DEAL_SETTLE_LAST")
    private BigDecimal dealSettleLast;

    @Column(name = "DEAL_SETTLE_WAY")
    private String dealSettleWay;

    @Column(name = "IS_DEAL_SETTLE_DONE")
    private String isDealSettleDone;

    @Column(name = "SETTLE_WAY")
    private String settleWay;

    @Column(name = "IS_SETTLE_DONE")
    private String isSettleDone;

    @Column(name = "NOT_SETTLE_LAST")
    private BigDecimal notSettleLast;

    @Column(name = "NOT_SETTLE")
    private BigDecimal notSettle;

    @Column(name = "MARKET_DIST")
    private String marketDist;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "EXPECT_INCOME_NOW")
    private BigDecimal expectIncomeNow;

    @Column(name = "EXPECT_INCOME_HALF")
    private BigDecimal expectIncomeHalf;

    @Column(name = "EXPECT_SETTLE_NOW")
    private BigDecimal expectSettleNow;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "CHANGES_REASON")
    private String changesReason;

    @Column(name = "CHANGE_ANALYSIS")
    private String changeAnalysis;

    @Transient
    private String deptName;

    /**
     * @return STAT_ID
     */
    public String getStatId() {
        return statId;
    }

    /**
     * @param statId
     */
    public void setStatId(String statId) {
        this.statId = statId == null ? null : statId.trim();
    }

    /**
     * 获取("XS","线上合同"),("NZ","内责书"),("TH","3万以下"),("XX","线下合同")
     *
     * @return STAT_TYPE - ("XS","线上合同"),("NZ","内责书"),("TH","3万以下"),("XX","线下合同")
     */
    public String getStatType() {
        return statType;
    }

    /**
     * 设置("XS","线上合同"),("NZ","内责书"),("TH","3万以下"),("XX","线下合同")
     *
     * @param statType ("XS","线上合同"),("NZ","内责书"),("TH","3万以下"),("XX","线下合同")
     */
    public void setStatType(String statType) {
        this.statType = statType == null ? null : statType.trim();
    }

    /**
     * @return STAT_ORDER
     */
    public Integer getStatOrder() {
        return statOrder;
    }

    /**
     * @param statOrder
     */
    public void setStatOrder(Integer statOrder) {
        this.statOrder = statOrder;
    }

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
     * @return DEAL_INCOME
     */
    public String getDealIncome() {
        return dealIncome;
    }

    /**
     * @param dealIncome
     */
    public void setDealIncome(String dealIncome) {
        this.dealIncome = dealIncome == null ? null : dealIncome.trim();
    }

    /**
     * 获取YYYY-MM
     *
     * @return YEAR_MONH - YYYY-MM
     */
    public String getYearMonh() {
        return yearMonh;
    }

    /**
     * 设置YYYY-MM
     *
     * @param yearMonh YYYY-MM
     */
    public void setYearMonh(String yearMonh) {
        this.yearMonh = yearMonh == null ? null : yearMonh.trim();
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
     * @return DEAL_CONTRACT
     */
    public String getDealContract() {
        return dealContract;
    }

    /**
     * @param dealContract
     */
    public void setDealContract(String dealContract) {
        this.dealContract = dealContract == null ? null : dealContract.trim();
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
     * @return CONT_ID
     */
    public String getContId() {
        return contId;
    }

    /**
     * @param contId
     */
    public void setContId(String contId) {
        this.contId = contId == null ? null : contId.trim();
    }

    /**
     * @return CONT_NAME
     */
    public String getContName() {
        return contName;
    }

    /**
     * @param contName
     */
    public void setContName(String contName) {
        this.contName = contName == null ? null : contName.trim();
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
     * @return SETTLE_PROGRESS
     */
    public BigDecimal getSettleProgress() {
        return settleProgress;
    }

    /**
     * @param settleProgress
     */
    public void setSettleProgress(BigDecimal settleProgress) {
        this.settleProgress = settleProgress;
    }

    /**
     * @return SETTLE
     */
    public BigDecimal getSettle() {
        return settle;
    }

    /**
     * @param settle
     */
    public void setSettle(BigDecimal settle) {
        this.settle = settle;
    }

    /**
     * @return SETTLE_NOW
     */
    public BigDecimal getSettleNow() {
        return settleNow;
    }

    /**
     * @param settleNow
     */
    public void setSettleNow(BigDecimal settleNow) {
        this.settleNow = settleNow;
    }

    /**
     * @return SETTLE_LAST
     */
    public BigDecimal getSettleLast() {
        return settleLast;
    }

    /**
     * @param settleLast
     */
    public void setSettleLast(BigDecimal settleLast) {
        this.settleLast = settleLast;
    }

    /**
     * @return DEAL_SETTLE_PROGRESS
     */
    public BigDecimal getDealSettleProgress() {
        return dealSettleProgress;
    }

    /**
     * @param dealSettleProgress
     */
    public void setDealSettleProgress(BigDecimal dealSettleProgress) {
        this.dealSettleProgress = dealSettleProgress;
    }

    /**
     * @return DEAL_SETTLE
     */
    public BigDecimal getDealSettle() {
        return dealSettle;
    }

    /**
     * @param dealSettle
     */
    public void setDealSettle(BigDecimal dealSettle) {
        this.dealSettle = dealSettle;
    }

    /**
     * @return DEAL_SETTLE_NOW
     */
    public BigDecimal getDealSettleNow() {
        return dealSettleNow;
    }

    /**
     * @param dealSettleNow
     */
    public void setDealSettleNow(BigDecimal dealSettleNow) {
        this.dealSettleNow = dealSettleNow;
    }

    /**
     * @return DEAL_SETTLE_LAST
     */
    public BigDecimal getDealSettleLast() {
        return dealSettleLast;
    }

    /**
     * @param dealSettleLast
     */
    public void setDealSettleLast(BigDecimal dealSettleLast) {
        this.dealSettleLast = dealSettleLast;
    }

    /**
     * @return DEAL_SETTLE_WAY
     */
    public String getDealSettleWay() {
        return dealSettleWay;
    }

    /**
     * @param dealSettleWay
     */
    public void setDealSettleWay(String dealSettleWay) {
        this.dealSettleWay = dealSettleWay == null ? null : dealSettleWay.trim();
    }

    /**
     * @return IS_DEAL_SETTLE_DONE
     */
    public String getIsDealSettleDone() {
        return isDealSettleDone;
    }

    /**
     * @param isDealSettleDone
     */
    public void setIsDealSettleDone(String isDealSettleDone) {
        this.isDealSettleDone = isDealSettleDone == null ? null : isDealSettleDone.trim();
    }

    /**
     * @return SETTLE_WAY
     */
    public String getSettleWay() {
        return settleWay;
    }

    /**
     * @param settleWay
     */
    public void setSettleWay(String settleWay) {
        this.settleWay = settleWay == null ? null : settleWay.trim();
    }

    /**
     * @return IS_SETTLE_DONE
     */
    public String getIsSettleDone() {
        return isSettleDone;
    }

    /**
     * @param isSettleDone
     */
    public void setIsSettleDone(String isSettleDone) {
        this.isSettleDone = isSettleDone == null ? null : isSettleDone.trim();
    }

    /**
     * @return NOT_SETTLE_LAST
     */
    public BigDecimal getNotSettleLast() {
        return notSettleLast;
    }

    /**
     * @param notSettleLast
     */
    public void setNotSettleLast(BigDecimal notSettleLast) {
        this.notSettleLast = notSettleLast;
    }

    /**
     * @return NOT_SETTLE
     */
    public BigDecimal getNotSettle() {
        return notSettle;
    }

    /**
     * @param notSettle
     */
    public void setNotSettle(BigDecimal notSettle) {
        this.notSettle = notSettle;
    }

    /**
     * @return MARKET_DIST
     */
    public String getMarketDist() {
        return marketDist;
    }

    /**
     * @param marketDist
     */
    public void setMarketDist(String marketDist) {
        this.marketDist = marketDist == null ? null : marketDist.trim();
    }

    /**
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return UPDATE_TIME
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return CREATE_ID
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * @param createId
     */
    public void setCreateId(String createId) {
        this.createId = createId == null ? null : createId.trim();
    }

    /**
     * @return EXPECT_INCOME_NOW
     */
    public BigDecimal getExpectIncomeNow() {
        return expectIncomeNow;
    }

    /**
     * @param expectIncomeNow
     */
    public void setExpectIncomeNow(BigDecimal expectIncomeNow) {
        this.expectIncomeNow = expectIncomeNow;
    }

    /**
     * @return EXPECT_INCOME_HALF
     */
    public BigDecimal getExpectIncomeHalf() {
        return expectIncomeHalf;
    }

    /**
     * @param expectIncomeHalf
     */
    public void setExpectIncomeHalf(BigDecimal expectIncomeHalf) {
        this.expectIncomeHalf = expectIncomeHalf;
    }

    /**
     * @return EXPECT_SETTLE_NOW
     */
    public BigDecimal getExpectSettleNow() {
        return expectSettleNow;
    }

    /**
     * @param expectSettleNow
     */
    public void setExpectSettleNow(BigDecimal expectSettleNow) {
        this.expectSettleNow = expectSettleNow;
    }

    /**
     * @return NOTE
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note
     */
    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

    /**
     * @return CHANGES_REASON
     */
    public String getChangesReason() {
        return changesReason;
    }

    /**
     * @param changesReason
     */
    public void setChangesReason(String changesReason) {
        this.changesReason = changesReason == null ? null : changesReason.trim();
    }

    /**
     * @return CHANGE_ANALYSIS
     */
    public String getChangeAnalysis() {
        return changeAnalysis;
    }

    /**
     * @param changeAnalysis
     */
    public void setChangeAnalysis(String changeAnalysis) {
        this.changeAnalysis = changeAnalysis == null ? null : changeAnalysis.trim();
    }
}
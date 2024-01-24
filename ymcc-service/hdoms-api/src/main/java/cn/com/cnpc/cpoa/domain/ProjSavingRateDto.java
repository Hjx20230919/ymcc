package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "T_PROJ_SAVING_RATE")
public class ProjSavingRateDto {
    @Id
    @Column(name = "SAVING_RATE_ID")
    private String savingRateId;

    @Column(name = "CALC_YEAR")
    private String calcYear;

    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "DEAL_ID")
    private String dealId;

    @Column(name = "SAVING_RATE")
    private Float savingRate;

    @Column(name = "TAX_RATE")
    private Integer taxRate;

    @Column(name = "DEAL_VALUE_IN_RATE")
    private Double dealValueInRate;

    @Column(name = "DEAL_VALUE_EX_RATE")
    private Double dealValueExRate;

    @Column(name = "DEAL_SETTLE_M1")
    private Double dealSettleM1;

    @Column(name = "DEAL_SETTLE_M2")
    private Double dealSettleM2;

    @Column(name = "DEAL_SETTLE_M3")
    private Double dealSettleM3;

    @Column(name = "DEAL_SETTLE_M4")
    private Double dealSettleM4;

    @Column(name = "DEAL_SETTLE_M5")
    private Double dealSettleM5;

    @Column(name = "DEAL_SETTLE_M6")
    private Double dealSettleM6;

    @Column(name = "DEAL_SETTLE_M7")
    private Double dealSettleM7;

    @Column(name = "DEAL_SETTLE_M8")
    private Double dealSettleM8;

    @Column(name = "DEAL_SETTLE_M9")
    private Double dealSettleM9;

    @Column(name = "DEAL_SETTLE_M10")
    private Double dealSettleM10;

    @Column(name = "DEAL_SETTLE_M11")
    private Double dealSettleM11;

    @Column(name = "DEAL_SETTLE_M12")
    private Double dealSettleM12;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return SAVING_RATE_ID
     */
    public String getSavingRateId() {
        return savingRateId;
    }

    /**
     * @param savingRateId
     */
    public void setSavingRateId(String savingRateId) {
        this.savingRateId = savingRateId == null ? null : savingRateId.trim();
    }

    /**
     * @return CALC_YEAR
     */
    public String getCalcYear() {
        return calcYear;
    }

    /**
     * @param calcYear
     */
    public void setCalcYear(String calcYear) {
        this.calcYear = calcYear;
    }

    /**
     * @return PROJ_ID
     */
    public String getProjId() {
        return projId;
    }

    /**
     * @param projId
     */
    public void setProjId(String projId) {
        this.projId = projId == null ? null : projId.trim();
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
     * @return SAVING_RATE
     */
    public Float getSavingRate() {
        return savingRate;
    }

    /**
     * @param savingRate
     */
    public void setSavingRate(Float savingRate) {
        this.savingRate = savingRate;
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

    /**
     * @return DEAL_VALUE_IN_RATE
     */
    public Double getDealValueInRate() {
        return dealValueInRate;
    }

    /**
     * @param dealValueInRate
     */
    public void setDealValueInRate(Double dealValueInRate) {
        this.dealValueInRate = dealValueInRate;
    }

    /**
     * @return DEAL_VALUE_EX_RATE
     */
    public Double getDealValueExRate() {
        return dealValueExRate;
    }

    /**
     * @param dealValueExRate
     */
    public void setDealValueExRate(Double dealValueExRate) {
        this.dealValueExRate = dealValueExRate;
    }

    /**
     * @return DEAL_SETTLE_M1
     */
    public Double getDealSettleM1() {
        return dealSettleM1;
    }

    /**
     * @param dealSettleM1
     */
    public void setDealSettleM1(Double dealSettleM1) {
        this.dealSettleM1 = dealSettleM1;
    }

    /**
     * @return DEAL_SETTLE_M2
     */
    public Double getDealSettleM2() {
        return dealSettleM2;
    }

    /**
     * @param dealSettleM2
     */
    public void setDealSettleM2(Double dealSettleM2) {
        this.dealSettleM2 = dealSettleM2;
    }

    /**
     * @return DEAL_SETTLE_M3
     */
    public Double getDealSettleM3() {
        return dealSettleM3;
    }

    /**
     * @param dealSettleM3
     */
    public void setDealSettleM3(Double dealSettleM3) {
        this.dealSettleM3 = dealSettleM3;
    }

    /**
     * @return DEAL_SETTLE_M4
     */
    public Double getDealSettleM4() {
        return dealSettleM4;
    }

    /**
     * @param dealSettleM4
     */
    public void setDealSettleM4(Double dealSettleM4) {
        this.dealSettleM4 = dealSettleM4;
    }

    /**
     * @return DEAL_SETTLE_M5
     */
    public Double getDealSettleM5() {
        return dealSettleM5;
    }

    /**
     * @param dealSettleM5
     */
    public void setDealSettleM5(Double dealSettleM5) {
        this.dealSettleM5 = dealSettleM5;
    }

    /**
     * @return DEAL_SETTLE_M6
     */
    public Double getDealSettleM6() {
        return dealSettleM6;
    }

    /**
     * @param dealSettleM6
     */
    public void setDealSettleM6(Double dealSettleM6) {
        this.dealSettleM6 = dealSettleM6;
    }

    /**
     * @return DEAL_SETTLE_M7
     */
    public Double getDealSettleM7() {
        return dealSettleM7;
    }

    /**
     * @param dealSettleM7
     */
    public void setDealSettleM7(Double dealSettleM7) {
        this.dealSettleM7 = dealSettleM7;
    }

    /**
     * @return DEAL_SETTLE_M8
     */
    public Double getDealSettleM8() {
        return dealSettleM8;
    }

    /**
     * @param dealSettleM8
     */
    public void setDealSettleM8(Double dealSettleM8) {
        this.dealSettleM8 = dealSettleM8;
    }

    /**
     * @return DEAL_SETTLE_M9
     */
    public Double getDealSettleM9() {
        return dealSettleM9;
    }

    /**
     * @param dealSettleM9
     */
    public void setDealSettleM9(Double dealSettleM9) {
        this.dealSettleM9 = dealSettleM9;
    }

    /**
     * @return DEAL_SETTLE_M10
     */
    public Double getDealSettleM10() {
        return dealSettleM10;
    }

    /**
     * @param dealSettleM10
     */
    public void setDealSettleM10(Double dealSettleM10) {
        this.dealSettleM10 = dealSettleM10;
    }

    /**
     * @return DEAL_SETTLE_M11
     */
    public Double getDealSettleM11() {
        return dealSettleM11;
    }

    /**
     * @param dealSettleM11
     */
    public void setDealSettleM11(Double dealSettleM11) {
        this.dealSettleM11 = dealSettleM11;
    }

    /**
     * @return DEAL_SETTLE_M12
     */
    public Double getDealSettleM12() {
        return dealSettleM12;
    }

    /**
     * @param dealSettleM12
     */
    public void setDealSettleM12(Double dealSettleM12) {
        this.dealSettleM12 = dealSettleM12;
    }

    /**
     * @return NOTES
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }
}
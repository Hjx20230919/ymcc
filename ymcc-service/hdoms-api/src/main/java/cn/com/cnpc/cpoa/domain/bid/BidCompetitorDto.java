package cn.com.cnpc.cpoa.domain.bid;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_BID_COMPETITOR")
public class BidCompetitorDto {
    @Id
    @Column(name = "COMPETITOR_ID")
    private String competitorId;

    @Column(name = "COMP_NAME")
    private String compName;

    @Column(name = "COPM_ADDR")
    private String copmAddr;

    @Column(name = "COMP_TYPE")
    private String compType;

    @Column(name = "REG_AMOUNT")
    private String regAmount;

    @Column(name = "COMP_SCALE")
    private String compScale;

    @Column(name = "MAIN_ADV")
    private String mainAdv;

    @Column(name = "CUR_PERF")
    private String curPerf;

    @Column(name = "COMP_ANAL")
    private String compAnal;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return COMPETITOR_ID
     */
    public String getCompetitorId() {
        return competitorId;
    }

    /**
     * @param competitorId
     */
    public void setCompetitorId(String competitorId) {
        this.competitorId = competitorId == null ? null : competitorId.trim();
    }

    /**
     * @return COMP_NAME
     */
    public String getCompName() {
        return compName;
    }

    /**
     * @param compName
     */
    public void setCompName(String compName) {
        this.compName = compName == null ? null : compName.trim();
    }

    /**
     * @return COPM_ADDR
     */
    public String getCopmAddr() {
        return copmAddr;
    }

    /**
     * @param copmAddr
     */
    public void setCopmAddr(String copmAddr) {
        this.copmAddr = copmAddr == null ? null : copmAddr.trim();
    }

    /**
     * @return COMP_TYPE
     */
    public String getCompType() {
        return compType;
    }

    /**
     * @param compType
     */
    public void setCompType(String compType) {
        this.compType = compType == null ? null : compType.trim();
    }

    /**
     * @return REG_AMOUNT
     */
    public String getRegAmount() {
        return regAmount;
    }

    /**
     * @param regAmount
     */
    public void setRegAmount(String regAmount) {
        this.regAmount = regAmount == null ? null : regAmount.trim();
    }

    /**
     * @return COMP_SCALE
     */
    public String getCompScale() {
        return compScale;
    }

    /**
     * @param compScale
     */
    public void setCompScale(String compScale) {
        this.compScale = compScale == null ? null : compScale.trim();
    }

    /**
     * @return MAIN_ADV
     */
    public String getMainAdv() {
        return mainAdv;
    }

    /**
     * @param mainAdv
     */
    public void setMainAdv(String mainAdv) {
        this.mainAdv = mainAdv == null ? null : mainAdv.trim();
    }

    /**
     * @return CUR_PERF
     */
    public String getCurPerf() {
        return curPerf;
    }

    /**
     * @param curPerf
     */
    public void setCurPerf(String curPerf) {
        this.curPerf = curPerf == null ? null : curPerf.trim();
    }

    /**
     * @return COMP_ANAL
     */
    public String getCompAnal() {
        return compAnal;
    }

    /**
     * @param compAnal
     */
    public void setCompAnal(String compAnal) {
        this.compAnal = compAnal == null ? null : compAnal.trim();
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
     * @return CREATOR
     */
    public String getCreator() {
        return creator;
    }

    /**
     * @param creator
     */
    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
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
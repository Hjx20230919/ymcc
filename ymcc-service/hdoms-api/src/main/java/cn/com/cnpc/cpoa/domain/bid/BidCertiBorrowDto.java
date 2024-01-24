package cn.com.cnpc.cpoa.domain.bid;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_BID_CERTI_BORROW")
public class BidCertiBorrowDto {
    @Id
    @Column(name = "CERTI_BORROW_ID")
    private String certiBorrowId;

    @Column(name = "BID_PROJ_ID")
    private String bidProjId;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "BORROW_MAN")
    private String borrowMan;

    @Column(name = "BORROW_REASON")
    private String borrowReason;

    @Column(name = "BORROW_START_AT")
    private Date borrowStartAt;

    @Column(name = "BORROW_END_AT")
    private Date borrowEndAt;

    @Column(name = "WATERMARK_CONTENT")
    private String watermarkContent;

    /**
     * inReview 审核中,audited 审核已完成
     */
    @Column(name = "CERTI_BORROW_STATUS")
    private String certiBorrowStatus;

    @Column(name = "CHECK_MAN")
    private String checkMan;

    /**
     * 不同意   Disagree
            同意       Agree
     */
    @Column(name = "CHECK_STATUS")
    private String checkStatus;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return CERTI_BORROW_ID
     */
    public String getCertiBorrowId() {
        return certiBorrowId;
    }

    /**
     * @param certiBorrowId
     */
    public void setCertiBorrowId(String certiBorrowId) {
        this.certiBorrowId = certiBorrowId == null ? null : certiBorrowId.trim();
    }

    /**
     * @return BID_PROJ_ID
     */
    public String getBidProjId() {
        return bidProjId;
    }

    /**
     * @param bidProjId
     */
    public void setBidProjId(String bidProjId) {
        this.bidProjId = bidProjId == null ? null : bidProjId.trim();
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
     * @return BORROW_MAN
     */
    public String getBorrowMan() {
        return borrowMan;
    }

    /**
     * @param borrowMan
     */
    public void setBorrowMan(String borrowMan) {
        this.borrowMan = borrowMan == null ? null : borrowMan.trim();
    }

    /**
     * @return BORROW_REASON
     */
    public String getBorrowReason() {
        return borrowReason;
    }

    /**
     * @param borrowReason
     */
    public void setBorrowReason(String borrowReason) {
        this.borrowReason = borrowReason == null ? null : borrowReason.trim();
    }

    /**
     * @return BORROW_START_AT
     */
    public Date getBorrowStartAt() {
        return borrowStartAt;
    }

    /**
     * @param borrowStartAt
     */
    public void setBorrowStartAt(Date borrowStartAt) {
        this.borrowStartAt = borrowStartAt;
    }

    /**
     * @return BORROW_END_AT
     */
    public Date getBorrowEndAt() {
        return borrowEndAt;
    }

    /**
     * @param borrowEndAt
     */
    public void setBorrowEndAt(Date borrowEndAt) {
        this.borrowEndAt = borrowEndAt;
    }

    /**
     * @return WATERMARK_CONTENT
     */
    public String getWatermarkContent() {
        return watermarkContent;
    }

    /**
     * @param watermarkContent
     */
    public void setWatermarkContent(String watermarkContent) {
        this.watermarkContent = watermarkContent == null ? null : watermarkContent.trim();
    }

    /**
     * @return CHECK_MAN
     */
    public String getCheckMan() {
        return checkMan;
    }

    /**
     * @param checkMan
     */
    public void setCheckMan(String checkMan) {
        this.checkMan = checkMan == null ? null : checkMan.trim();
    }

    /**
     * 获取不同意   Disagree
            同意       Agree
     *
     * @return CHECK_STATUS - 不同意   Disagree
            同意       Agree
     */
    public String getCheckStatus() {
        return checkStatus;
    }

    /**
     * 设置不同意   Disagree
            同意       Agree
     *
     * @param checkStatus 不同意   Disagree
            同意       Agree
     */
    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus == null ? null : checkStatus.trim();
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

    public String getCertiBorrowStatus() {
        return certiBorrowStatus;
    }

    public void setCertiBorrowStatus(String certiBorrowStatus) {
        this.certiBorrowStatus = certiBorrowStatus;
    }
}
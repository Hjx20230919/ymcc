package cn.com.cnpc.cpoa.domain.bid;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_BID_BIDDING_DEPT")
public class BidBiddingDeptDto {
    @Id
    @Column(name = "BIDDING_DEPT_ID")
    private String biddingDeptId;

    @Column(name = "BIDDING_ID")
    private String biddingId;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "DEPT_DESC")
    private String deptDesc;

    /**
     * 1、待确认     Confirming
            2、参与投标 Bidding
            3、放弃投标 UnBidding
     */
    @Column(name = "CONFIRM_BID")
    private String confirmBid;

    @Column(name = "CONFIRM_MAN")
    private String confirmMan;

    @Column(name = "CONFIRM_AT")
    private Date confirmAt;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return BIDDING_DEPT_ID
     */
    public String getBiddingDeptId() {
        return biddingDeptId;
    }

    /**
     * @param biddingDeptId
     */
    public void setBiddingDeptId(String biddingDeptId) {
        this.biddingDeptId = biddingDeptId == null ? null : biddingDeptId.trim();
    }

    /**
     * @return BIDDING_ID
     */
    public String getBiddingId() {
        return biddingId;
    }

    /**
     * @param biddingId
     */
    public void setBiddingId(String biddingId) {
        this.biddingId = biddingId == null ? null : biddingId.trim();
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
     * @return DEPT_DESC
     */
    public String getDeptDesc() {
        return deptDesc;
    }

    /**
     * @param deptDesc
     */
    public void setDeptDesc(String deptDesc) {
        this.deptDesc = deptDesc == null ? null : deptDesc.trim();
    }

    /**
     * 获取1、待确认     Confirming
            2、参与投标 Bidding
            3、放弃投标 UnBidding
     *
     * @return CONFIRM_BID - 1、待确认     Confirming
            2、参与投标 Bidding
            3、放弃投标 UnBidding
     */
    public String getConfirmBid() {
        return confirmBid;
    }

    /**
     * 设置1、待确认     Confirming
            2、参与投标 Bidding
            3、放弃投标 UnBidding
     *
     * @param confirmBid 1、待确认     Confirming
            2、参与投标 Bidding
            3、放弃投标 UnBidding
     */
    public void setConfirmBid(String confirmBid) {
        this.confirmBid = confirmBid == null ? null : confirmBid.trim();
    }

    /**
     * @return CONFIRM_MAN
     */
    public String getConfirmMan() {
        return confirmMan;
    }

    /**
     * @param confirmMan
     */
    public void setConfirmMan(String confirmMan) {
        this.confirmMan = confirmMan == null ? null : confirmMan.trim();
    }

    /**
     * @return CONFIRM_AT
     */
    public Date getConfirmAt() {
        return confirmAt;
    }

    /**
     * @param confirmAt
     */
    public void setConfirmAt(Date confirmAt) {
        this.confirmAt = confirmAt;
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
package cn.com.cnpc.cpoa.domain.bid;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_BID_COMP_INFO_BORROW_LIST")
public class BidCompInfoBorrowListDto {
    @Id
    @Column(name = "COMP_INFO_BORROW_LIST_ID")
    private String compInfoBorrowListId;

    @Column(name = "CERTI_BORROW_ID")
    private String certiBorrowId;

    @Column(name = "COMP_INFO_TYPE")
    private String compInfoType;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "COMP_INFO_REQ")
    private String compInfoReq;

    @Column(name = "LAST_AT")
    private Date lastAt;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return COMP_INFO_BORROW_LIST_ID
     */
    public String getCompInfoBorrowListId() {
        return compInfoBorrowListId;
    }

    /**
     * @param compInfoBorrowListId
     */
    public void setCompInfoBorrowListId(String compInfoBorrowListId) {
        this.compInfoBorrowListId = compInfoBorrowListId == null ? null : compInfoBorrowListId.trim();
    }

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
     * @return COMP_INFO_TYPE
     */
    public String getCompInfoType() {
        return compInfoType;
    }

    /**
     * @param compInfoType
     */
    public void setCompInfoType(String compInfoType) {
        this.compInfoType = compInfoType == null ? null : compInfoType.trim();
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
     * @return COMP_INFO_REQ
     */
    public String getCompInfoReq() {
        return compInfoReq;
    }

    /**
     * @param compInfoReq
     */
    public void setCompInfoReq(String compInfoReq) {
        this.compInfoReq = compInfoReq == null ? null : compInfoReq.trim();
    }

    /**
     * @return LAST_AT
     */
    public Date getLastAt() {
        return lastAt;
    }

    /**
     * @param lastAt
     */
    public void setLastAt(Date lastAt) {
        this.lastAt = lastAt;
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
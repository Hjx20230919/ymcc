package cn.com.cnpc.cpoa.domain.bid;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_BID_BIZ_OPT_TRACK")
public class BidBizOptTrackDto {
    @Id
    @Column(name = "BIZ_OPT_TRACK_ID")
    private String bizOptTrackId;

    @Column(name = "BIZ_OPT_ID")
    private String bizOptId;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "TRACK_DESC")
    private String trackDesc;

    @Column(name = "RESERVE1")
    private String reserve1;

    @Column(name = "RESERVE2")
    private String reserve2;

    @Column(name = "RESERVE3")
    private String reserve3;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return BIZ_OPT_TRACK_ID
     */
    public String getBizOptTrackId() {
        return bizOptTrackId;
    }

    /**
     * @param bizOptTrackId
     */
    public void setBizOptTrackId(String bizOptTrackId) {
        this.bizOptTrackId = bizOptTrackId == null ? null : bizOptTrackId.trim();
    }

    /**
     * @return BIZ_OPT_ID
     */
    public String getBizOptId() {
        return bizOptId;
    }

    /**
     * @param bizOptId
     */
    public void setBizOptId(String bizOptId) {
        this.bizOptId = bizOptId == null ? null : bizOptId.trim();
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
     * @return TRACK_DESC
     */
    public String getTrackDesc() {
        return trackDesc;
    }

    /**
     * @param trackDesc
     */
    public void setTrackDesc(String trackDesc) {
        this.trackDesc = trackDesc == null ? null : trackDesc.trim();
    }

    /**
     * @return RESERVE1
     */
    public String getReserve1() {
        return reserve1;
    }

    /**
     * @param reserve1
     */
    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1 == null ? null : reserve1.trim();
    }

    /**
     * @return RESERVE2
     */
    public String getReserve2() {
        return reserve2;
    }

    /**
     * @param reserve2
     */
    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2 == null ? null : reserve2.trim();
    }

    /**
     * @return RESERVE3
     */
    public String getReserve3() {
        return reserve3;
    }

    /**
     * @param reserve3
     */
    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3 == null ? null : reserve3.trim();
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
package cn.com.cnpc.cpoa.domain.bid;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_BID_BIZ_OPT")
public class BidBizOptDto {
    @Id
    @Column(name = "BIZ_OPT_ID")
    private String bizOptId;

    @Column(name = "OPT_NAME")
    private String optName;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "CREATOR")
    private String creator;

    @Column(name = "CREATE_AT")
    private Date createAt;

    @Column(name = "CONT_NAME")
    private String contName;

    @Column(name = "BIZ_OPT_DESC")
    private String bizOptDesc;

    @Column(name = "NEXT_REMIND_AT")
    private Date nextRemindAt;

    @Column(name = "REMIND_CTX")
    private String remindCtx;

    /**
     * 进行中  Ongoing
            关闭     Closed
             
     */
    @Column(name = "BIZ_OPT_STATUS")
    private String bizOptStatus;

    @Column(name = "RESERVE1")
    private String reserve1;

    @Column(name = "RESERVE2")
    private String reserve2;

    @Column(name = "RESERVE3")
    private String reserve3;

    @Column(name = "NOTES")
    private String notes;

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
     * @return DEPT
     */
    public String getDept() {
        return deptId;
    }

    /**
     * @param deptId
     */
    public void setDept(String deptId) {
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
     * @return BIZ_OPT_DESC
     */
    public String getBizOptDesc() {
        return bizOptDesc;
    }

    /**
     * @param bizOptDesc
     */
    public void setBizOptDesc(String bizOptDesc) {
        this.bizOptDesc = bizOptDesc == null ? null : bizOptDesc.trim();
    }

    /**
     * @return NEXT_REMIND_AT
     */
    public Date getNextRemindAt() {
        return nextRemindAt;
    }

    /**
     * @param nextRemindAt
     */
    public void setNextRemindAt(Date nextRemindAt) {
        this.nextRemindAt = nextRemindAt;
    }

    /**
     * @return REMIND_CTX
     */
    public String getRemindCtx() {
        return remindCtx;
    }

    /**
     * @param remindCtx
     */
    public void setRemindCtx(String remindCtx) {
        this.remindCtx = remindCtx == null ? null : remindCtx.trim();
    }

    /**
     * 获取进行中  Ongoing
            关闭     Closed
             
     *
     * @return BIZ_OPT_STATUS - 进行中  Ongoing
            关闭     Closed
             
     */
    public String getBizOptStatus() {
        return bizOptStatus;
    }

    /**
     * 设置进行中  Ongoing
            关闭     Closed
             
     *
     * @param bizOptStatus 进行中  Ongoing
            关闭     Closed
             
     */
    public void setBizOptStatus(String bizOptStatus) {
        this.bizOptStatus = bizOptStatus == null ? null : bizOptStatus.trim();
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
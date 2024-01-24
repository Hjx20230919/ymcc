package cn.com.cnpc.cpoa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_PROJ_CHECK_LOG")
public class ProjCheckLogDto {
    @Column(name = "CHECK_LOG_ID")
    private String checkLogId;

    @Column(name = "CHECK_OBJ_ID")
    private String checkObjId;

    /**
     * ("deal","合同"),("settle","结算")
     */
    @Column(name = "CHECK_OBJ_TYPE")
    private String checkObjType;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "CREATE_AT")
    private Date createAt;

    /**
     * 文本记录，记录采购方案内容，采购方案本身+采购列表
     */
    @Column(name = "PLAN_LOG")
    private String planLog;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return CHECK_LOG_ID
     */
    public String getCheckLogId() {
        return checkLogId;
    }

    /**
     * @param checkLogId
     */
    public void setCheckLogId(String checkLogId) {
        this.checkLogId = checkLogId == null ? null : checkLogId.trim();
    }

    /**
     * @return CHECK_OBJ_ID
     */
    public String getCheckObjId() {
        return checkObjId;
    }

    /**
     * @param checkObjId
     */
    public void setCheckObjId(String checkObjId) {
        this.checkObjId = checkObjId == null ? null : checkObjId.trim();
    }

    /**
     * 获取("deal","合同"),("settle","结算")
     *
     * @return CHECK_OBJ_TYPE - ("deal","合同"),("settle","结算")
     */
    public String getCheckObjType() {
        return checkObjType;
    }

    /**
     * 设置("deal","合同"),("settle","结算")
     *
     * @param checkObjType ("deal","合同"),("settle","结算")
     */
    public void setCheckObjType(String checkObjType) {
        this.checkObjType = checkObjType == null ? null : checkObjType.trim();
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
     * 获取文本记录，记录采购方案内容，采购方案本身+采购列表
     *
     * @return PLAN_LOG - 文本记录，记录采购方案内容，采购方案本身+采购列表
     */
    public String getPlanLog() {
        return planLog;
    }

    /**
     * 设置文本记录，记录采购方案内容，采购方案本身+采购列表
     *
     * @param planLog 文本记录，记录采购方案内容，采购方案本身+采购列表
     */
    public void setPlanLog(String planLog) {
        this.planLog = planLog == null ? null : planLog.trim();
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
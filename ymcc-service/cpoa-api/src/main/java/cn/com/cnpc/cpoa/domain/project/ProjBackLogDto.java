package cn.com.cnpc.cpoa.domain.project;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_PROJ_BACK_LOG")
public class ProjBackLogDto {
    /**
     * 返回日志主键ID
     */
    @Id
    @Column(name = "BACK_LOG_ID")
    private String backLogId;

    /**
     * 立项项目ID
     */
    @Column(name = "PROJ_ID")
    private String projId;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_AT")
    private Date createAt;

    /**
     * 采购方案日志
     */
    @Column(name = "PLAN_LOG")
    private String planLog;

    /**
     * 选商日志
     */
    @Column(name = "SEL_CONT_LOG")
    private String selContLog;

    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;

    /**
     * 经办人
     */
    @Column(name = "CREATE_ID")
    private String createId;

    /**
     * 获取返回日志主键ID
     *
     * @return BACK_LOG_ID - 返回日志主键ID
     */
    public String getBackLogId() {
        return backLogId;
    }

    /**
     * 设置返回日志主键ID
     *
     * @param backLogId 返回日志主键ID
     */
    public void setBackLogId(String backLogId) {
        this.backLogId = backLogId == null ? null : backLogId.trim();
    }

    /**
     * 获取立项项目ID
     *
     * @return PROJ_ID - 立项项目ID
     */
    public String getProjId() {
        return projId;
    }

    /**
     * 设置立项项目ID
     *
     * @param projId 立项项目ID
     */
    public void setProjId(String projId) {
        this.projId = projId == null ? null : projId.trim();
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_AT - 创建时间
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * 设置创建时间
     *
     * @param createAt 创建时间
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取采购方案日志
     *
     * @return PLAN_LOG - 采购方案日志
     */
    public String getPlanLog() {
        return planLog;
    }

    /**
     * 设置采购方案日志
     *
     * @param planLog 采购方案日志
     */
    public void setPlanLog(String planLog) {
        this.planLog = planLog == null ? null : planLog.trim();
    }

    /**
     * 获取选商日志
     *
     * @return SEL_CONT_LOG - 选商日志
     */
    public String getSelContLog() {
        return selContLog;
    }

    /**
     * 设置选商日志
     *
     * @param selContLog 选商日志
     */
    public void setSelContLog(String selContLog) {
        this.selContLog = selContLog == null ? null : selContLog.trim();
    }

    /**
     * 获取备注
     *
     * @return NOTES - 备注
     */
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注
     *
     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }

    /**
     * 获取经办人
     *
     * @return CREATE_ID - 经办人
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置经办人
     *
     * @param createId 经办人
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }
}
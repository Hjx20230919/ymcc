package cn.com.cnpc.cpoa.domain.contractor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_CONT_PROJECT_HIS")
public class ContProjectHisDto {
    @Id
    @Column(name = "PROJ_ID")
    private String projId;

    @Column(name = "PROJ_CONT_CODE")
    private String projContCode;

    @Column(name = "PROJ_CONT_NAME")
    private String projContName;

    /**
     * 正式准入，临时准入
     */
    @Column(name = "PROJ_ACCESS_TYPE")
    private String projAccessType;

    @Column(name = "PROJ_CONTENT")
    private String projContent;

    @Column(name = "PROJ_AT")
    private Date projAt;

    @Column(name = "PROJ_USER")
    private String projUser;

    /**
     * 草稿、审核中、退回、审核完成
     */
    @Column(name = "PROJ_STATE")
    private String projState;

    @Column(name = "PROJ_STATE_AT")
    private Date projStateAt;

    @Column(name = "OWNER_ID")
    private String ownerId;

    @Column(name = "OWNER_DEPT_ID")
    private String ownerDeptId;

    @Column(name = "PROJ_APPLY_REASON")
    private String projApplyReason;

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
     * @return PROJ_CONT_CODE
     */
    public String getProjContCode() {
        return projContCode;
    }

    /**
     * @param projContCode
     */
    public void setProjContCode(String projContCode) {
        this.projContCode = projContCode == null ? null : projContCode.trim();
    }

    /**
     * @return PROJ_CONT_NAME
     */
    public String getProjContName() {
        return projContName;
    }

    /**
     * @param projContName
     */
    public void setProjContName(String projContName) {
        this.projContName = projContName == null ? null : projContName.trim();
    }

    /**
     * 获取正式准入，临时准入
     *
     * @return PROJ_ACCESS_TYPE - 正式准入，临时准入
     */
    public String getProjAccessType() {
        return projAccessType;
    }

    /**
     * 设置正式准入，临时准入
     *
     * @param projAccessType 正式准入，临时准入
     */
    public void setProjAccessType(String projAccessType) {
        this.projAccessType = projAccessType == null ? null : projAccessType.trim();
    }

    /**
     * @return PROJ_CONTENT
     */
    public String getProjContent() {
        return projContent;
    }

    /**
     * @param projContent
     */
    public void setProjContent(String projContent) {
        this.projContent = projContent == null ? null : projContent.trim();
    }

    /**
     * @return PROJ_AT
     */
    public Date getProjAt() {
        return projAt;
    }

    /**
     * @param projAt
     */
    public void setProjAt(Date projAt) {
        this.projAt = projAt;
    }

    /**
     * @return PROJ_USER
     */
    public String getProjUser() {
        return projUser;
    }

    /**
     * @param projUser
     */
    public void setProjUser(String projUser) {
        this.projUser = projUser == null ? null : projUser.trim();
    }

    /**
     * 获取草稿、审核中、退回、审核完成
     *
     * @return PROJ_STATE - 草稿、审核中、退回、审核完成
     */
    public String getProjState() {
        return projState;
    }

    /**
     * 设置草稿、审核中、退回、审核完成
     *
     * @param projState 草稿、审核中、退回、审核完成
     */
    public void setProjState(String projState) {
        this.projState = projState == null ? null : projState.trim();
    }

    /**
     * @return PROJ_STATE_AT
     */
    public Date getProjStateAt() {
        return projStateAt;
    }

    /**
     * @param projStateAt
     */
    public void setProjStateAt(Date projStateAt) {
        this.projStateAt = projStateAt;
    }

    /**
     * @return OWNER_ID
     */
    public String getOwnerId() {
        return ownerId;
    }

    /**
     * @param ownerId
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId == null ? null : ownerId.trim();
    }

    /**
     * @return OWNER_DEPT_ID
     */
    public String getOwnerDeptId() {
        return ownerDeptId;
    }

    /**
     * @param ownerDeptId
     */
    public void setOwnerDeptId(String ownerDeptId) {
        this.ownerDeptId = ownerDeptId == null ? null : ownerDeptId.trim();
    }

    /**
     * @return PROJ_APPLY_REASON
     */
    public String getProjApplyReason() {
        return projApplyReason;
    }

    /**
     * @param projApplyReason
     */
    public void setProjApplyReason(String projApplyReason) {
        this.projApplyReason = projApplyReason == null ? null : projApplyReason.trim();
    }
}
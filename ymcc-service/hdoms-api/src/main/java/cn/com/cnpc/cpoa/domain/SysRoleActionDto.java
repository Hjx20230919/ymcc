package cn.com.cnpc.cpoa.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_SYS_ROLE_ACTIONS")
public class SysRoleActionDto {
    @Id
    @Column(name = "ROLE_ACTION_ID")
    private String roleActionId;

    @Column(name = "ACTION_ID")
    private String actionId;

    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return ROLE_ACTION_ID
     */
    public String getRoleActionId() {
        return roleActionId;
    }

    /**
     * @param roleActionId
     */
    public void setRoleActionId(String roleActionId) {
        this.roleActionId = roleActionId == null ? null : roleActionId.trim();
    }

    /**
     * @return ACTION_ID
     */
    public String getActionId() {
        return actionId;
    }

    /**
     * @param actionId
     */
    public void setActionId(String actionId) {
        this.actionId = actionId == null ? null : actionId.trim();
    }

    /**
     * @return ROLE_ID
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId == null ? null : roleId.trim();
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
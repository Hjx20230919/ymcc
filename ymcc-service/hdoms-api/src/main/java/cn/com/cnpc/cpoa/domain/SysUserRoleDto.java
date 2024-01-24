package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "T_SYS_USER_ROLES")
public class SysUserRoleDto {
    @Id
    @Column(name = "USER_ACTION_ID")
    private String userActionId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "ROLE_ID")
    private String roleId;

    /**
     * @return USER_ACTION_ID
     */
    public String getUserActionId() {
        return userActionId;
    }

    /**
     * @param userActionId
     */
    public void setUserActionId(String userActionId) {
        this.userActionId = userActionId == null ? null : userActionId.trim();
    }

    /**
     * @return USER_ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
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
}
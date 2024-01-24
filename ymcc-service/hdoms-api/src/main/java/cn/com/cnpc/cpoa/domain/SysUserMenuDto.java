package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 15:24
 * @Description: 用户权限表
 */
@Table(name = "T_SYS_USER_ACTIONS")
public class SysUserMenuDto {

    /**
     * 用户权限标识
     */
    @Id
    @Column(name = "USER_ACTION_ID")
    private String userActionId;

    /**
     * 菜单权限ID
     */
    @Column(name = "ACTION_ID")
    private String actionId;

    /**
     * 用户ID
     */
    @Column(name = "USER_ID")
    private String userId;

    public String getUserActionId() {
        return userActionId;
    }

    public void setUserActionId(String userActionId) {
        this.userActionId = userActionId;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

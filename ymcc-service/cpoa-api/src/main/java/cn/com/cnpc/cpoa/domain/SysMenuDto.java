package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 11:49
 * @Description: 菜单传输对象
 */
@Table(name = "T_SYS_ACTIONS")
public class SysMenuDto {

    /**
     * 菜单标识
     */
    @Id
    @Column(name = "ACTION_ID")
    private String actionId;

    /**
     * 权限名称
     */
    @Column(name = "NAME")
    private String name;

    /**
     * 类型
     */
    @Column(name = "TYPE")
    private String type;

    /**
     * 父action_id
     */
    @Column(name = "PARENT_ACTION_ID")
    private String parentActionId;

    /**
     * Action URL
     */
    @Column(name = "ACTION_URL")
    private String actionUrl;

    /**
     * Action_icon
     */
    @Column(name = "ACTION_ICON")
    private String actionIcon;

    /**
     * 序号
     */
    @Column(name = "SEQ")
    private Integer seq;

    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;


    @Column(name = "PATH")
    private String path;

    @Column(name = "REDIRECT")
    private String redirect;

    @Column(name = "COMPONENT")
    private String component;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "ICON")
    private String icon;

    @Column(name = "SYSKIND")
    private String syskind;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSyskind() {
        return syskind;
    }

    public void setSyskind(String syskind) {
        this.syskind = syskind;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getParentActionId() {
        return parentActionId;
    }

    public void setParentActionId(String parentActionId) {
        this.parentActionId = parentActionId;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public String getActionIcon() {
        return actionIcon;
    }

    public void setActionIcon(String actionIcon) {
        this.actionIcon = actionIcon;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

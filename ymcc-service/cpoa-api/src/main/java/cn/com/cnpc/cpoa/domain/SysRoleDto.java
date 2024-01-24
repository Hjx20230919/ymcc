package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "T_SYS_ROLE")
public class SysRoleDto {
    @Id
    @Column(name = "ROLE_ID")
    private String roleId;

    @Column(name = "NAME")
    private String name;

    /**
     * 0 系统默认的角色，不能删除
            1 自定义角色
     */
    @Column(name = "TYPE")
    private String type;

    @Column(name = "NOTES")
    private String notes;

//    /**
//     * all  全部权限
//            deptAndChild  部门及部门以下
//     */
//    @Column(name = "DATA_SCOPE")
//    private String dataScope;

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
     * @return NAME
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * 获取0 系统默认的角色，不能删除
            1 自定义角色
     *
     * @return TYPE - 0 系统默认的角色，不能删除
            1 自定义角色
     */
    public String getType() {
        return type;
    }

    /**
     * 设置0 系统默认的角色，不能删除
            1 自定义角色
     *
     * @param type 0 系统默认的角色，不能删除
            1 自定义角色
     */
    public void setType(String type) {
        this.type = type == null ? null : type.trim();
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
//
//    /**
//     * 获取all  全部权限
//            deptAndChild  部门及部门以下
//     *
//     * @return DATA_SCOPE - all  全部权限
//            deptAndChild  部门及部门以下
//     */
//    public String getDataScope() {
//        return dataScope;
//    }
//
//    /**
//     * 设置all  全部权限
//            deptAndChild  部门及部门以下
//     *
//     * @param dataScope all  全部权限
//            deptAndChild  部门及部门以下
//     */
//    public void setDataScope(String dataScope) {
//        this.dataScope = dataScope == null ? null : dataScope.trim();
//    }
}
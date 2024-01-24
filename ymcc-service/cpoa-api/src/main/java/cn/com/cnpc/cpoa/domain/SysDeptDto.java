package cn.com.cnpc.cpoa.domain;

import cn.com.cnpc.cpoa.enums.DeptBaseEnum;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 16:32
 * @Description: 部门
 */
@Table(name = "T_SYS_DEPT")
public class SysDeptDto {

    /**
     * 部门标识
     */
    @Id
    @Column(name = "DEPT_ID")
    private String deptId;

    /**
     * 部门名称
     */
    @Column(name = "DEPT_NAME")
    private String deptName;

    /**
     * 部门简称(院内)
     */
    @Column(name = "ALIAS_NAME1")
    private String aliasName1;

    /**
     * 部门简称(院外)
     */
    @Column(name = "ALIAS_NAME2")
    private String aliasName2;

    /**
     * 部门属地('枚举值：
     机关 ： Office
     基层：  Basic
     )
     */
    @Column(name = "DEPT_BASE")
    private String deptBase;

    /**
     * 部门负责人
     */
    @Column(name = "DEPT_MANAGER")
    private String deptManager;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 是否是企管部 (默认为  否   0)
     */
    @Column(name = "IS_EMP")
    private Integer isEmp;

    /**
     * 全称
     */
    @Column(name = "ALIAS_NAME3")
    private  String aliasName3;

    @Transient
    private String deptManagerName;

    public String getAliasName3() {
        return aliasName3;
    }

    public void setAliasName3(String aliasName3) {
        this.aliasName3 = aliasName3;
    }

    public String getDeptManagerName() {
        return deptManagerName;
    }

    public void setDeptManagerName(String deptManagerName) {
        this.deptManagerName = deptManagerName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAliasName1() {
        return aliasName1;
    }

    public void setAliasName1(String aliasName1) {
        this.aliasName1 = aliasName1;
    }

    public String getAliasName2() {
        return aliasName2;
    }

    public void setAliasName2(String aliasName2) {
        this.aliasName2 = aliasName2;
    }

    public String getDeptBase() {
//        if("Office".equals(DeptBaseEnum.OFFICE.getKey())){
//            return DeptBaseEnum.OFFICE.getValue();
//        }else if("Basic".equals(DeptBaseEnum.BASIC.getKey())){
//            return DeptBaseEnum.BASIC.getValue();
//        }
        return deptBase;
    }

    public void setDeptBase(String deptBase) {
        this.deptBase = deptBase;
    }

    public String getDeptManager() {
        return deptManager;
    }

    public void setDeptManager(String deptManager) {
        this.deptManager = deptManager;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsEmp() {
        return isEmp;
    }

    public void setIsEmp(Integer isEmp) {
        this.isEmp = isEmp;
    }
}

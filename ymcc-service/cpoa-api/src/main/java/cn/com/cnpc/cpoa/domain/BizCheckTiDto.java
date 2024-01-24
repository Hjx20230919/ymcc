package cn.com.cnpc.cpoa.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @Author: 17742856263
 * @Date: 2019/4/20 8:22
 * @Description:审批流程模板项
 */
@Data
@Table(name = "T_BIZ_CHECK_TI")
public class BizCheckTiDto {

    /**
     * 模板项标识
     */
    @Id
    @Column(name = "ITME_ID")
    private String itmeId;

    /**
     * 审核步骤
     */
    @Column(name = "CHECK_NO")
    private String checkNo;

    /**
     * 审核步骤
     */
    @Column(name = "CHECK_NAME")
    private String checkName;

    /**
     * 审核部门
     */
    @Column(name = "CHECK_DEPT")
    private String checkDept;


    /**
     * 审核人员
     */
    @Column(name = "CHECK_MAN")
    private String checkMan;


    /**
     * 模板标识
     */
    @Column(name = "TMPL_ID")
    private String tmplId;

    /**
     * 模板标识
     */
    @Column(name = "ITME_RULE")
    private String itmeRule;


    @Transient
    private String deptName;

    @Transient
    private String userId;

    @Transient
    private String userName;


}

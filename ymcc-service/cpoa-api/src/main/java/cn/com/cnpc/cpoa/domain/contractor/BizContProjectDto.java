package cn.com.cnpc.cpoa.domain.contractor;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/9 20:18
 * @Description:
 */
@Data
@Table(name = "T_CONT_PROJECT")
public class BizContProjectDto {

    /**
     * 项目申请标识
     */
    @Id
    @Column(name = "PROJ_ID")
    private String projId;

    /**
     * 承包商组织机构代码
     */
    @Column(name = "PROJ_CONT_CODE")
    private String projContCode;

    /**
     * 承包商名称
     */
    @Column(name = "PROJ_CONT_NAME")
    private String projContName;

    /**
     * 承包商联系人
     */
    @Column(name = "PROJ_CONT_LINKMAN")
    private String projContLinkman;

    /**
     * 承包商联系电话
     */
    @Column(name = "PROJ_CONT_PHONE")
    private String projContPhone;

    /**
     * 承包商联系邮箱
     */
    @Column(name = "PROJ_CONT_MAIL")
    private String projContMail;


    /**
     * 准入申请类型(正式准入，临时准入)
     */
    @Column(name = "PROJ_ACCESS_TYPE")
    private String projAccessType;

    /**
     * 项目申请内容
     */
    @Column(name = "PROJ_CONTENT")
    private String projContent;

    /**
     * 项目申请时间
     */
    @Column(name = "PROJ_AT")
    private Date projAt;

    /**
     * 项目申请状态(草稿、审核中、退回、审核完成)
     */
    @Column(name = "PROJ_STATE")
    private String projState;

    /**
     * 项目申请状态时间
     */
    @Column(name = "PROJ_STATE_AT")
    private Date projStateAt;

    /**
     * 经办人
     */
    @Column(name = "OWNER_ID")
    private String ownerId;

    /**
     * 经办部门
     */
    @Column(name = "OWNER_DEPT_ID")
    private String ownerDeptId;


    /**
     * 准入申请原因
     */
    @Column(name = "PROJ_APPLY_REASON")
    private String projApplyReason;



}

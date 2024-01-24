package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/26 10:24
 * @Description:资质维护实体
 */
@Data
public class ContCreditMaintainPo {

    private String setId;
    private String acceId;
    private String contName;
    private String contOrgNo;
    private String corporate;
    private String linkMobile;
    private String ownerId;
    private String ownerDeptId;
    private String userName;
    private String deptName;
    private String contState;
    private String content;
    private Date createAt;
    private Date setFillTime;
    private String setCode;
    private String isOutTime;
    private String acceState;
    private String setState;
    private String projContent;
    private String accessLevel;
    private String contFreezeState;

    /**
     * 项目申请标识
     */
    private String projId;

    /**
     * 承包商组织机构代码
     */
    private String projContCode;

    /**
     * 承包商名称
     */
    private String projContName;

    /**
     * 承包商联系人
     */
    private String projContLinkman;

    /**
     * 承包商联系电话
     */
    private String projContPhone;

    /**
     * 承包商联系邮箱
     */
    private String projContMail;

    /**
     * 准入申请类型(正式准入，临时准入)
     */
    private String projAccessType;

    /**
     * 项目申请时间
     */
    private Date projAt ;

    /**
     * 项目申请状态(草稿、审核中、退回、审核完成)
     */
    private String projState ;

    /**
     * 项目申请状态时间
     */
    private Date projStateAt  ;

    private String projApplyReason;
}

package cn.com.cnpc.cpoa.vo.contractor;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/10/26 10:33
 * @Description:
 */
@Data
public class ContCreditMaintainVo {

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
    private String createAt;
    private String setFillTime;
    private String setCode;
    private String setUrl;
    //是否过期 0 否 1 是
    private String isOutTime;

    private String acceState;
    private String setState;
    private String setId;

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
    private String projAt ;

    /**
     * 项目申请状态(草稿、审核中、退回、审核完成)
     */
    private String projState ;

    /**
     * 项目申请状态时间
     */
    private String projStateAt  ;

    private String projApplyReason;
}

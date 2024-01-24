package cn.com.cnpc.cpoa.vo.contractor;

import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/10/9 20:38
 * @Description:
 */
@Data
public class ContProjectVo {
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
     * 是否是黑名单,1为黑名单，0为正常
     */
    private Integer isRelieve;

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
     * 项目申请内容
     */
    private String projContent;

    /**
     * 项目申请时间
     */
    private String projAt;

    /**
     * 项目申请状态(草稿、审核中、退回、审核完成)
     */
    private String projState ;

    /**
     * 项目申请状态时间
     */
    private String projStateAt;

    /**
     * 经办人
     */
    private String ownerId;

    /**
     * 经办部门
     */
    private String ownerDeptId;


    private String userName;

    private String deptName;

    private String acessCode;

    private String acessUrl;

    private String acceId;
    private String type;

    private String acceState;

    private String acceFillTime;

    private String objId;
    private String objType;
    private List<String> curAuditUser;

    private String projApplyReason;

    private String accessLevel;
}

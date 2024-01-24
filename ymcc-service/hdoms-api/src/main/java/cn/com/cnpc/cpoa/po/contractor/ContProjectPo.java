package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 19:04
 * @Description:
 */
@Data
public class ContProjectPo {

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
     * 项目申请内容
     */
    private String projContent;

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

    /**
     * 经办人
     */
    private String ownerId;

    /**
     * 经办部门
     */
    private String ownerDeptId;

    private String userId;
    private String userName;

    private String deptName;

    private String acessCode;

    private String acceId;
    private String acceState;

    private String objId;
    private String objType;

    private Date acceFillTime;

    private List<String> curAuditUser;

    private String projApplyReason;

    private String accessLevel;

    /**
     * 是否是黑名单,1为黑名单，0为正常
     */
    private Integer isRelieve;

}

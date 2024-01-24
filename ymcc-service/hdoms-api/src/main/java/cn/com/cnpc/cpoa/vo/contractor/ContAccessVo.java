package cn.com.cnpc.cpoa.vo.contractor;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 20:01
 * @Description:
 */
@Data
public class ContAccessVo {
    /**
     * 准入申请标识
     */
    private String acceId;

    /**
     * 项目申请
     */
    private String projId;

    /**
     * 承包商标识
     */
    private String contId;

    /**
     * 准入申请创建时间
     */
    private String acceAt;

    /**
     * 准入申请状态(填报、审核中、退回、审核完毕、失效)
     */
    private String acceState;

    /**
     * 准入申请接入码
     */
    private String acceCode;

    /**
     * 准入申请填写限定时间
     */
    private String acceFillTime;

    /**
     * 准入申请状态时间
     */
    private String acceStateAt;

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
    private String projContent;
}

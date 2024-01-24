package cn.com.cnpc.cpoa.po;

import lombok.Data;

import java.util.Date;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO   待办项返回类
 * @date 2022/4/28 16:38
 */
@Data
public class BackLogPo {

    /**
     * 待办项ID
     */
    private String checkObjId;

    private String checkObjType;

    private String settleId;

    /**
     * 承办部门
     */
    private String deptName;

    /**
     * 待办项审批人
     */
    private String userName;

    /**
     * 创建时间
     */
    private String checkTime;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 审核类型
     */
    private String type;
    private String projId;
    private String selContId;
    private String resultId;
    private String planId;
    private String dealId;
    private String accessId;
    /**
     * 流程类型
     */
    private String processType;

    private String selContType;

}

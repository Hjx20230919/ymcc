package cn.com.cnpc.cpoa.po;

import lombok.Data;

import java.util.Date;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/5/5 11:03
 */
@Data
public class MyProcessPo {

    /**
     * 流程ID
     */
    private String checkObjId;

    /**
     * userID
     */
    private String userId;

    /**
     * 当前流程分类
     */
    private String checkObjType;

    /**
     * 流程分类名称
     */
    private String checkObjName;

    /**
     * 流程类型
     */
    private String type;

    /**
     * 流程名称
     */
    private String name;

    /**
     * 当前状态
     */
    private String status;

    private String accessId;

    private String settleId;

    /**
     * 承办部门
     */
    private String deptName;

    /**
     * 承办人
     */
    private String userName;

    /**
     * 创建时间
     */
    private String checkTime;

    /**
     *标识ID
     */
    private String projId;
    private String dealId;
    private String selContId;
    private String planId;
    private String resultId;

    /**
     * 流程类型
     */
    private String processType;
}

package cn.com.cnpc.cpoa.vo;

import lombok.Data;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/5/5 15:53
 */
@Data
public class MyProcessVo {
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

}

package cn.com.cnpc.cpoa.vo;

import lombok.Data;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/5/5 15:52
 */
@Data
public class BackLogVo {
    /**
     * 待办项ID
     */
    private String checkObjId;

    /**
     * 待办类型
     */
    private String checkObjType;

    /**
     * 待办类型名称
     */
    private String checkObjName;

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

    private String name;
    private String type;
    private String projId;
    private String selContId;
    private String resultId;
    private String planId;
}

package cn.com.cnpc.cpoa.po;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/14 23:31
 * @Description: 审核步骤查询详情
 */
@Data
public class CheckStepPo {

    private String userName;

    private String deptName;

    private Integer stepNo;

    private Date checkTime;

    private String checkNode;

    private String stepId;

    private String manId;

    //否是是企管部门  1 是 0 不是
    private String isEmp;

    private String checkResult;

    private String userId;

    //是否是当前步骤  1 是 0 不是
    private String isCurrentStep;

    private Integer stepSplit;

    //步骤名称
    private String stepName;

    //委托人id
    private String entrustUserId;

    //处理状态
    private String checkState;


    private String checkObjId;

    private String checkObjType;

}

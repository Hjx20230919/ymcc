package cn.com.cnpc.cpoa.domain;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 15:44
 * @Description: 审批流程步骤
 */
@Data
@Table(name = "T_BIZ_CHECK_STEP")
public class BizCheckStepDto {

    /**
     *步骤标识
     */
    @Id
    @Column(name = "STEP_ID")
    private String stepId;

    /**
     *步骤序号
     */
    @Column(name = "STEP_NO")
    private Integer stepNo;

    /**
     *步骤名称
     */
    @Column(name = "STEP_NAME")
    private String stepName;

    /**
     *步骤创建时间
     */
    @Column(name = "STEP_CREATE_AT")
    private Date stepCreateAt;

    /**
     *步骤创建人
     */
    @Column(name = "STEP_CREATE_USER")
    private String stepCreateUser;

    /**
     *步骤状态
     */
    @Column(name = "STEP_STATE")
    private String stepState;

    /**
     *步骤开始时间
     */
    @Column(name = "STEP_BEGIN_AT")
    private Date stepBeginAt;

    /**
     *步骤结束时间
     */
    @Column(name = "STEP_END_AT")
    private Date stepEndAt;

    /**
     *审批主体标识
     */
    @Column(name = "CHECK_OBJ_ID")
    private String checkObjId;

    /**
     *审批主体类型
     */
    @Column(name = "CHECK_OBJ_TYPE")
    private String checkObjType;


    /**
     *企管部自定义标志 1 可制定 2 不可制定
     */
    @Column(name = "STEP_SPLIT")
    private Integer stepSplit;


    @Column(name = "CHECK_DEPT_ID")
    private String checkDeptId;


}

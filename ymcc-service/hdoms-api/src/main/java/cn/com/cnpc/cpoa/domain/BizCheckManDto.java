package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 15:50
 * @Description: 审批流程步骤处理人
 */
@Table(name = "T_BIZ_CHECK_MAN")
public class BizCheckManDto {

    /**
     *处理人标识
     */
    @Id
    @Column(name = "MAN_ID")
    private String manId;

    /**
     *步骤序号
     */
    @Column(name = "STEP_ID")
    private String stepId;

    /**
     *用户标识
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     *处理状态
     */
    @Column(name = "CHECK_STATE")
    private String checkState;

    /**
     *处理时间
     */
    @Column(name = "CHECK_TIME")
    private Date checkTime;

    /**
     *处理意见
     */
    @Column(name = "CHECK_NODE")
    private String checkNode;

    /**
     *处理结果
     */
    @Column(name = "CHECK_RESULT")
    private String checkResult;

    public String getManId() {
        return manId;
    }

    public void setManId(String manId) {
        this.manId = manId;
    }

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCheckState() {
        return checkState;
    }

    public void setCheckState(String checkState) {
        this.checkState = checkState;
    }

    public Date getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(Date checkTime) {
        this.checkTime = checkTime;
    }

    public String getCheckNode() {
        return checkNode;
    }

    public void setCheckNode(String checkNode) {
        this.checkNode = checkNode;
    }

    public String getCheckResult() {
        return checkResult;
    }

    public void setCheckResult(String checkResult) {
        this.checkResult = checkResult;
    }
}

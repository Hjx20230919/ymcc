package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 19:20
 * @Description:准入申请
 */
@Table(name = "T_CONT_ACCESS")
public class BizContAccessDto {

    /**
     * 准入申请标识
     */
    @Id
    @Column(name = "ACCE_ID")
    private String acceId;

    /**
     * 项目申请
     */
    @Column(name = "PROJ_ID")
    private String projId;

    /**
     * 承包商标识
     */
    @Column(name = "CONT_ID")
    private String contId;

    /**
     * 准入申请创建时间
     */
    @Column(name = "ACCE_AT")
    private Date acceAt;

    /**
     * 准入申请状态(填报、审核中、退回、审核完毕、失效)
     */
    @Column(name = "ACCE_STATE")
    private String acceState;

    /**
     * 准入申请接入码
     */
    @Column(name = "ACCE_CODE")
    private String acceCode;

    /**
     * 准入申请填写限定时间
     */
    @Column(name = "ACCE_FILL_TIME")
    private Date acceFillTime;

    /**
     * 准入申请状态时间
     */
    @Column(name = "ACCE_STATE_AT")
    private Date acceStateAt;

    /**
     * 经办人
     */
    @Column(name = "OWNER_ID")
    private String ownerId;

    /**
     * 经办部门
     */
    @Column(name = "OWNER_DEPT_ID")
    private String ownerDeptId;

    public String getAcceId() {
        return acceId;
    }

    public void setAcceId(String acceId) {
        this.acceId = acceId;
    }

    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }



    public String getAcceState() {
        return acceState;
    }

    public void setAcceState(String acceState) {
        this.acceState = acceState;
    }

    public String getAcceCode() {
        return acceCode;
    }

    public void setAcceCode(String acceCode) {
        this.acceCode = acceCode;
    }

    public Date getAcceAt() {
        return acceAt;
    }

    public void setAcceAt(Date acceAt) {
        this.acceAt = acceAt;
    }

    public Date getAcceFillTime() {
        return acceFillTime;
    }

    public void setAcceFillTime(Date acceFillTime) {
        this.acceFillTime = acceFillTime;
    }

    public Date getAcceStateAt() {
        return acceStateAt;
    }

    public void setAcceStateAt(Date acceStateAt) {
        this.acceStateAt = acceStateAt;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerDeptId() {
        return ownerDeptId;
    }

    public void setOwnerDeptId(String ownerDeptId) {
        this.ownerDeptId = ownerDeptId;
    }
}

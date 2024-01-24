package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/21 18:44
 * @Description:资质变更
 */
@Table(name="T_CONT_CREDIT_SET")
public class BizContCreditSetDto {

    /**
     * 变更标识
     */
    @Id
    @Column(name = "SET_ID")
    private String setId;


    /**
     * 准入申请标识
     */
    @Column(name = "ACCE_ID")
    private String acceId;

    /**
     * 变更创建时间
     */
    @Column(name = "SET_CREATE_AT")
    private Date setCreateAt;

    /**
     * 变更创建原因
     */
    @Column(name = "SET_CREATE_REASON")
    private String setCreateReason;

    /**
     * 变更状态
     */
    @Column(name = "SET_STATE")
    private String setState;

    /**
     * 变更状态时间
     */
    @Column(name = "SET_STATE_AT")
    private Date setStateAt;

    /**
     * 变更填写限定时间
     */
    @Column(name = "SET_FILL_TIME")
    private Date setFillTime;

    /**
     * 变更接入码
     */
    @Column(name = "SET_CODE")
    private String setCode;

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getAcceId() {
        return acceId;
    }

    public void setAcceId(String acceId) {
        this.acceId = acceId;
    }

    public Date getSetCreateAt() {
        return setCreateAt;
    }

    public void setSetCreateAt(Date setCreateAt) {
        this.setCreateAt = setCreateAt;
    }

    public String getSetCreateReason() {
        return setCreateReason;
    }

    public void setSetCreateReason(String setCreateReason) {
        this.setCreateReason = setCreateReason;
    }

    public String getSetState() {
        return setState;
    }

    public void setSetState(String setState) {
        this.setState = setState;
    }

    public Date getSetStateAt() {
        return setStateAt;
    }

    public void setSetStateAt(Date setStateAt) {
        this.setStateAt = setStateAt;
    }

    public Date getSetFillTime() {
        return setFillTime;
    }

    public void setSetFillTime(Date setFillTime) {
        this.setFillTime = setFillTime;
    }

    public String getSetCode() {
        return setCode;
    }

    public void setSetCode(String setCode) {
        this.setCode = setCode;
    }
}

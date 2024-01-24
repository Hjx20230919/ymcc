package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 20:35
 * @Description:资质附件
 */
@Table(name = "T_CONT_CREDIT_ATTACH")
public class BizContCreditAttachDto {

    /**
     * 标识
     */
    @Id
    @Column(name = "ID")
    private String id;

    /*
     * 资质标识
     */
    @Column(name = "CREDIT_ID")
    private String creditId;

    /**
     * 附件标识
     */
    @Column(name = "ATTACH_ID")
    private String attachId;

    /**
     * 附件状态
     */
    @Column(name = "ATTACH_STATE")
    private String attachState;

    /**
     * 状态时间
     */
    @Column(name = "STATE_AT")
    private Date stateAt;

    /**
     * 变更标识
     */
    @Column(name = "SET_ID")
    private String setId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getAttachState() {
        return attachState;
    }

    public void setAttachState(String attachState) {
        this.attachState = attachState;
    }

    public Date getStateAt() {
        return stateAt;
    }

    public void setStateAt(Date stateAt) {
        this.stateAt = stateAt;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }
}

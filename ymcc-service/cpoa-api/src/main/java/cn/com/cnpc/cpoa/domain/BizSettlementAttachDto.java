package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 10:02
 * @Description: 结算附件中间
 */
@Table(name = "T_BIZ_SETTLEMENT_ATTACH")
public class BizSettlementAttachDto {

    /**
     * 结算附件标识
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 结算标识
     */
    @Column(name = "SETTLE_ID")
    private String settleId;

    /**
     * 附件标识
     */
    @Column(name = "ATTACH_ID")
    private String attachId;

    @Transient
    private String deptName;

    @Transient
    private String contName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSettleId() {
        return settleId;
    }

    public void setSettleId(String settleId) {
        this.settleId = settleId;
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }
}

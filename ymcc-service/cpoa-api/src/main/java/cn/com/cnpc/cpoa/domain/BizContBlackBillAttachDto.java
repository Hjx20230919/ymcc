package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "T_CONT_BLACKBILL_ATTACH")
public class BizContBlackBillAttachDto {
    @Id
    @Column(name = "BLACKBILL_ATTACH_ID")
    private String blackbillAttachId;

    @Column(name = "BLACKLIST_ID")
    private String blacklistId;

    @Column(name = "ATTACH_ID")
    private String attachId;

    /**
     * @return BLACKBILL_ATTACH_ID
     */
    public String getBlackbillAttachId() {
        return blackbillAttachId;
    }

    /**
     * @param blackbillAttachId
     */
    public void setBlackbillAttachId(String blackbillAttachId) {
        this.blackbillAttachId = blackbillAttachId == null ? null : blackbillAttachId.trim();
    }

    /**
     * @return BLACKLIST_ID
     */
    public String getBlacklistId() {
        return blacklistId;
    }

    /**
     * @param blacklistId
     */
    public void setBlacklistId(String blacklistId) {
        this.blacklistId = blacklistId == null ? null : blacklistId.trim();
    }

    /**
     * @return ATTACH_ID
     */
    public String getAttachId() {
        return attachId;
    }

    /**
     * @param attachId
     */
    public void setAttachId(String attachId) {
        this.attachId = attachId == null ? null : attachId.trim();
    }
}
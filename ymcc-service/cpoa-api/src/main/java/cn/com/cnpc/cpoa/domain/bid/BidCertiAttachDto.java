package cn.com.cnpc.cpoa.domain.bid;

import javax.persistence.*;

@Table(name = "T_BID_CERTI_ATTACH")
public class BidCertiAttachDto {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "USER_CERTI_ID")
    private String userCertiId;

    @Column(name = "ATTACH_ID")
    private String attachId;

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * @return USER_CERTI_ID
     */
    public String getUserCertiId() {
        return userCertiId;
    }

    /**
     * @param userCertiId
     */
    public void setUserCertiId(String userCertiId) {
        this.userCertiId = userCertiId == null ? null : userCertiId.trim();
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
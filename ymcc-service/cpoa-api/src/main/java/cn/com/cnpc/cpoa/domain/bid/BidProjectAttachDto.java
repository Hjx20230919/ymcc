package cn.com.cnpc.cpoa.domain.bid;

import javax.persistence.*;

@Table(name = "T_BID_PROJECT_ATTACH")
public class BidProjectAttachDto {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "BID_PROJ_ID")
    private String bidProjId;

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
     * @return BID_PROJ_ID
     */
    public String getBidProjId() {
        return bidProjId;
    }

    /**
     * @param bidProjId
     */
    public void setBidProjId(String bidProjId) {
        this.bidProjId = bidProjId == null ? null : bidProjId.trim();
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
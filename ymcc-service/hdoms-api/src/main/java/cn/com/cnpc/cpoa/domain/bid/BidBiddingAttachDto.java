package cn.com.cnpc.cpoa.domain.bid;

import javax.persistence.*;

@Table(name = "T_BID_BIDDING_ATTACH")
public class BidBiddingAttachDto {
    @Column(name = "ID")
    private String id;

    @Column(name = "BIDDING_ID")
    private String biddingId;

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
     * @return BIDDING_ID
     */
    public String getBiddingId() {
        return biddingId;
    }

    /**
     * @param biddingId
     */
    public void setBiddingId(String biddingId) {
        this.biddingId = biddingId == null ? null : biddingId.trim();
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
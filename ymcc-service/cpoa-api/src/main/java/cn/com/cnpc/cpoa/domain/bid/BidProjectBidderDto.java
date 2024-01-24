package cn.com.cnpc.cpoa.domain.bid;

import lombok.Data;

import javax.persistence.*;
@Data
@Table(name = "T_BID_PROJECT_BIDDER")
public class BidProjectBidderDto {
    @Id
    @Column(name = "PROJECT_BIDDER_ID")
    private String projectBidderId;

    @Column(name = "BID_PROJ_ID")
    private String bidProjId;

    @Column(name = "COMPETITOR_ID")
    private String competitorId;

    @Column(name = "RESERVE1")
    private String reserve1;

    @Column(name = "RESERVE2")
    private String reserve2;

    @Column(name = "RESERVE3")
    private String reserve3;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return PROJECT_BIDDER_ID
     */
    public String getProjectBidderId() {
        return projectBidderId;
    }

    /**
     * @param projectBidderId
     */
    public void setProjectBidderId(String projectBidderId) {
        this.projectBidderId = projectBidderId == null ? null : projectBidderId.trim();
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
     * @return COMPETITOR_ID
     */
    public String getCompetitorId() {
        return competitorId;
    }

    /**
     * @param competitorId
     */
    public void setCompetitorId(String competitorId) {
        this.competitorId = competitorId == null ? null : competitorId.trim();
    }

    /**
     * @return RESERVE1
     */
    public String getReserve1() {
        return reserve1;
    }

    /**
     * @param reserve1
     */
    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1 == null ? null : reserve1.trim();
    }

    /**
     * @return RESERVE2
     */
    public String getReserve2() {
        return reserve2;
    }

    /**
     * @param reserve2
     */
    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2 == null ? null : reserve2.trim();
    }

    /**
     * @return RESERVE3
     */
    public String getReserve3() {
        return reserve3;
    }

    /**
     * @param reserve3
     */
    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3 == null ? null : reserve3.trim();
    }

    /**
     * @return NOTES
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes
     */
    public void setNotes(String notes) {
        this.notes = notes == null ? null : notes.trim();
    }
}
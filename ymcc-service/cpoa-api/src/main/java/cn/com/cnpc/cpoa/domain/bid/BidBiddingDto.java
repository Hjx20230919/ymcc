package cn.com.cnpc.cpoa.domain.bid;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "T_BID_BIDDING")
public class BidBiddingDto {
    @Id
    @Column(name = "BIDDING_ID")
    private String biddingId;

    @Column(name = "PROJ_NO")
    private String projNo;

    @Column(name = "PROJ_NAME")
    private String projName;

    @Column(name = "NOTICE_NAME")
    private String noticeName;

    @Column(name = "PUBLISH_AT")
    private Date publishAt;

    @Column(name = "BIDDING_CONDITIONS")
    private String biddingConditions;

    @Column(name = "PROJ_DESC")
    private String projDesc;

    @Column(name = "BIDDER_QUALIFICATION")
    private String bidderQualification;

    @Column(name = "BIDDER_DOC_INFO")
    private String bidderDocInfo;

    @Column(name = "POST_BID_DOC_INFO")
    private String postBidDocInfo;

    @Column(name = "PUBLISH_MEDIA")
    private String publishMedia;

    @Column(name = "LINK_INFO")
    private String linkInfo;

    @Column(name = "OTHER_INFO")
    private String otherInfo;

    @Column(name = "BIDDER_FILE_AMOUNT")
    private Float bidderFileAmount;

    @Column(name = "GET_BID_DOC_START_AT")
    private Date getBidDocStartAt;

    @Column(name = "GET_BID_DOC_END_AT")
    private Date getBidDocEndAt;

    @Column(name = "GUARANTEE_AMOUNT")
    private Long guaranteeAmount;

    @Column(name = "GA_ACTUAL_AT")
    private Date gaActualAt;

    @Column(name = "POST_BID_DOC_END_AT")
    private Date postBidDocEndAt;

    @Column(name = "BID_AMOUNT")
    private Float bidAmount;

    @Column(name = "BID_OPEN_AT")
    private Date bidOpenAt;

    @Column(name = "CONTACT_METHOD")
    private String contactMethod;

    @Column(name = "SOURCE_URL")
    private String sourceUrl;

    @Column(name = "CRAWL_AT")
    private Date crawlAt;

    /**
     * Net   爬取，New  手动
     */
    @Column(name = "CRAWL_METHOD")
    private String crawlMethod;

    /**
     * 忽略  Ignore
            放弃  GiveUp
            分配  Distribution
            退回  Back
            投标  Bidding
            
     */
    @Column(name = "BIDDING_STATUS")
    private String biddingStatus;

    @Column(name = "KEY_WORD")
    private String keyWord;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "RESERVE1")
    private String reserve1;

    @Column(name = "RESERVE2")
    private String reserve2;

    @Column(name = "RESERVE3")
    private String reserve3;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "SOURCE_HTML")
    private String sourceHtml;

    public String getCrawlMethod() {
        return crawlMethod;
    }

    public void setCrawlMethod(String crawlMethod) {
        this.crawlMethod = crawlMethod;
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
     * @return PROJ_NO
     */
    public String getProjNo() {
        return projNo;
    }

    /**
     * @param projNo
     */
    public void setProjNo(String projNo) {
        this.projNo = projNo == null ? null : projNo.trim();
    }

    /**
     * @return PROJ_NAME
     */
    public String getProjName() {
        return projName;
    }

    /**
     * @param projName
     */
    public void setProjName(String projName) {
        this.projName = projName == null ? null : projName.trim();
    }

    /**
     * @return NOTICE_NAME
     */
    public String getNoticeName() {
        return noticeName;
    }

    /**
     * @param noticeName
     */
    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName == null ? null : noticeName.trim();
    }

    /**
     * @return PUBLISH_AT
     */
    public Date getPublishAt() {
        return publishAt;
    }

    /**
     * @param publishAt
     */
    public void setPublishAt(Date publishAt) {
        this.publishAt = publishAt;
    }

    /**
     * @return BIDDING_CONDITIONS
     */
    public String getBiddingConditions() {
        return biddingConditions;
    }

    /**
     * @param biddingConditions
     */
    public void setBiddingConditions(String biddingConditions) {
        this.biddingConditions = biddingConditions == null ? null : biddingConditions.trim();
    }

    /**
     * @return PROJ_DESC
     */
    public String getProjDesc() {
        return projDesc;
    }

    /**
     * @param projDesc
     */
    public void setProjDesc(String projDesc) {
        this.projDesc = projDesc == null ? null : projDesc.trim();
    }

    /**
     * @return BIDDER_QUALIFICATION
     */
    public String getBidderQualification() {
        return bidderQualification;
    }

    /**
     * @param bidderQualification
     */
    public void setBidderQualification(String bidderQualification) {
        this.bidderQualification = bidderQualification == null ? null : bidderQualification.trim();
    }

    /**
     * @return BIDDER_DOC_INFO
     */
    public String getBidderDocInfo() {
        return bidderDocInfo;
    }

    /**
     * @param bidderDocInfo
     */
    public void setBidderDocInfo(String bidderDocInfo) {
        this.bidderDocInfo = bidderDocInfo == null ? null : bidderDocInfo.trim();
    }

    /**
     * @return POST_BID_DOC_INFO
     */
    public String getPostBidDocInfo() {
        return postBidDocInfo;
    }

    /**
     * @param postBidDocInfo
     */
    public void setPostBidDocInfo(String postBidDocInfo) {
        this.postBidDocInfo = postBidDocInfo == null ? null : postBidDocInfo.trim();
    }

    /**
     * @return PUBLISH_MEDIA
     */
    public String getPublishMedia() {
        return publishMedia;
    }

    /**
     * @param publishMedia
     */
    public void setPublishMedia(String publishMedia) {
        this.publishMedia = publishMedia == null ? null : publishMedia.trim();
    }

    /**
     * @return LINK_INFO
     */
    public String getLinkInfo() {
        return linkInfo;
    }

    /**
     * @param linkInfo
     */
    public void setLinkInfo(String linkInfo) {
        this.linkInfo = linkInfo == null ? null : linkInfo.trim();
    }

    /**
     * @return OTHER_INFO
     */
    public String getOtherInfo() {
        return otherInfo;
    }

    /**
     * @param otherInfo
     */
    public void setOtherInfo(String otherInfo) {
        this.otherInfo = otherInfo == null ? null : otherInfo.trim();
    }

    /**
     * @return BIDDER_FILE_AMOUNT
     */
    public Float getBidderFileAmount() {
        return bidderFileAmount;
    }

    /**
     * @param bidderFileAmount
     */
    public void setBidderFileAmount(Float bidderFileAmount) {
        this.bidderFileAmount = bidderFileAmount;
    }

    /**
     * @return GET_BID_DOC_START_AT
     */
    public Date getGetBidDocStartAt() {
        return getBidDocStartAt;
    }

    /**
     * @param getBidDocStartAt
     */
    public void setGetBidDocStartAt(Date getBidDocStartAt) {
        this.getBidDocStartAt = getBidDocStartAt;
    }

    /**
     * @return GET_BID_DOC_END_AT
     */
    public Date getGetBidDocEndAt() {
        return getBidDocEndAt;
    }

    /**
     * @param getBidDocEndAt
     */
    public void setGetBidDocEndAt(Date getBidDocEndAt) {
        this.getBidDocEndAt = getBidDocEndAt;
    }

    /**
     * @return GUARANTEE_AMOUNT
     */
    public Long getGuaranteeAmount() {
        return guaranteeAmount;
    }

    /**
     * @param guaranteeAmount
     */
    public void setGuaranteeAmount(Long guaranteeAmount) {
        this.guaranteeAmount = guaranteeAmount;
    }

    /**
     * @return GA_ACTUAL_AT
     */
    public Date getGaActualAt() {
        return gaActualAt;
    }

    /**
     * @param gaActualAt
     */
    public void setGaActualAt(Date gaActualAt) {
        this.gaActualAt = gaActualAt;
    }

    /**
     * @return POST_BID_DOC_END_AT
     */
    public Date getPostBidDocEndAt() {
        return postBidDocEndAt;
    }

    /**
     * @param postBidDocEndAt
     */
    public void setPostBidDocEndAt(Date postBidDocEndAt) {
        this.postBidDocEndAt = postBidDocEndAt;
    }

    /**
     * @return BID_AMOUNT
     */
    public Float getBidAmount() {
        return bidAmount;
    }

    /**
     * @param bidAmount
     */
    public void setBidAmount(Float bidAmount) {
        this.bidAmount = bidAmount;
    }

    /**
     * @return BID_OPEN_AT
     */
    public Date getBidOpenAt() {
        return bidOpenAt;
    }

    /**
     * @param bidOpenAt
     */
    public void setBidOpenAt(Date bidOpenAt) {
        this.bidOpenAt = bidOpenAt;
    }

    /**
     * @return CONTACT_METHOD
     */
    public String getContactMethod() {
        return contactMethod;
    }

    /**
     * @param contactMethod
     */
    public void setContactMethod(String contactMethod) {
        this.contactMethod = contactMethod == null ? null : contactMethod.trim();
    }

    /**
     * @return SOURCE_URL
     */
    public String getSourceUrl() {
        return sourceUrl;
    }

    /**
     * @param sourceUrl
     */
    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl == null ? null : sourceUrl.trim();
    }

    /**
     * @return CRAWL_AT
     */
    public Date getCrawlAt() {
        return crawlAt;
    }

    /**
     * @param crawlAt
     */
    public void setCrawlAt(Date crawlAt) {
        this.crawlAt = crawlAt;
    }

    /**
     * 获取忽略  Ignore
            放弃  GiveUp
            分配  Distribution
            退回  Back
            投标  Bidding
            
     *
     * @return BIDDING_STATUS - 忽略  Ignore
            放弃  GiveUp
            分配  Distribution
            退回  Back
            投标  Bidding
            
     */
    public String getBiddingStatus() {
        return biddingStatus;
    }

    /**
     * 设置忽略  Ignore
            放弃  GiveUp
            分配  Distribution
            退回  Back
            投标  Bidding
            
     *
     * @param biddingStatus 忽略  Ignore
            放弃  GiveUp
            分配  Distribution
            退回  Back
            投标  Bidding
            
     */
    public void setBiddingStatus(String biddingStatus) {
        this.biddingStatus = biddingStatus == null ? null : biddingStatus.trim();
    }

    /**
     * @return KEY_WORD
     */
    public String getKeyWord() {
        return keyWord;
    }

    /**
     * @param keyWord
     */
    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord == null ? null : keyWord.trim();
    }

    /**
     * @return DEPT_ID
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
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

    /**
     * @return SOURCE_HTML
     */
    public String getSourceHtml() {
        return sourceHtml;
    }

    /**
     * @param sourceHtml
     */
    public void setSourceHtml(String sourceHtml) {
        this.sourceHtml = sourceHtml == null ? null : sourceHtml.trim();
    }
}
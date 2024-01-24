package cn.com.cnpc.cpoa.domain.bid;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_BID_PROJECT")
public class BidProjectDto {
    @Id
    @Column(name = "BID_PROJ_ID")
    private String bidProjId;

    @Column(name = "BIDDING_ID")
    private String biddingId;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "CREATE_ID")
    private String createId;

    /**
     * Yes  借用
            No   不借用
     */
    @Column(name = "IS_BORROW_UKEY")
    private Integer isBorrowUkey;

    /**
     * AJY   安检院
            KT     科特
     */
    @Column(name = "UKEY_TYPE")
    private String ukeyType;

    /**
     * Yes  归还
            No  未归还
     */
    @Column(name = "IS_RETURN")
    private String isReturn;

    /**
     * 支付方式，Company  公司，Individual  个人
     */
    @Column(name = "PAYMENT_METHOD")
    private String paymentMethod;

    /**
     * 支付单位：四川科特  安检院
     */
    @Column(name = "PAY_COMPANY")
    private String payCompany;

    @Column(name = "BORROW_UKEY_AT")
    private Date borrowUkeyAt;

    @Column(name = "RETURN_UKEY_AT")
    private Date returnUkeyAt;

    @Column(name = "GET_BID_DOC_END_AT")
    private Date getBidDocEndAt;

    @Column(name = "IS_GET_BID_DOC")
    private String isGetBidDoc;

    @Column(name = "GET_BID_DOC_ACTUAL_AT")
    private Date getBidDocActualAt;

    @Column(name = "GUARANTEE_AMOUNT")
    private Long guaranteeAmount;

    @Column(name = "GA_END_AT")
    private Date gaEndAt;

    @Column(name = "IS_GUARANTEE_AMOUNT")
    private String isGuaranteeAmount;

    @Column(name = "GA_ACTUAL_AT")
    private Date gaActualAt;

    @Column(name = "UPLOAD_PLAN_AT")
    private Date uploadPlanAt;

    @Column(name = "IS_UPLOAD_PLAN")
    private String isUploadPlan;

    @Column(name = "UPLOAD_ACTUAL_AT")
    private Date uploadActualAt;

    @Column(name = "POST_BID_DOC_END_AT")
    private Date postBidDocEndAt;

    @Column(name = "IS_POST_BID_DOC")
    private String isPostBidDoc;

    @Column(name = "POST_BID_DOC_ACTAUL_AT")
    private Date postBidDocActaulAt;

    /**
     * 0 是
     * 1 否
     */
    @Column(name = "IS_BID")
    private Integer isBid;

    @Column(name = "UPLOAD_BID_NOTICE_AT")
    private Date uploadBidNoticeAt;

    @Column(name = "UN_BID_DESC")
    private String unBidDesc;

    @Column(name = "BID_PROJ_DESC")
    private String bidProjDesc;

    /**
     * 1、投标准备   BidPrepare
            2、投标审核   BidAuditing
            3、退回          Back
            4、投标完成   Complete
            
            
     */
    @Column(name = "PROJ_STATUS")
    private String projStatus;

    @Column(name = "RESERVE1")
    private String reserve1;

    @Column(name = "RESERVE2")
    private String reserve2;

    @Column(name = "RESERVE3")
    private String reserve3;

    @Column(name = "NOTES")
    private String notes;

    @Column(name = "IS_JOINT_CHECKUP")
    private Integer isJointCheckup;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Integer getIsJointCheckup() {
        return isJointCheckup;
    }

    public void setIsJointCheckup(Integer isJointCheckup) {
        this.isJointCheckup = isJointCheckup;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }

    public Date getBorrowUkeyAt() {
        return borrowUkeyAt;
    }

    public void setBorrowUkeyAt(Date borrowUkeyAt) {
        this.borrowUkeyAt = borrowUkeyAt;
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
     * 获取Yes  借用
            No   不借用
     *
     * @return IS_BORROW_UKEY - Yes  借用
            No   不借用
     */
    public Integer getIsBorrowUkey() {
        return isBorrowUkey;
    }

    /**
     * 设置Yes  借用
            No   不借用
     *
     * @param isBorrowUkey Yes  借用
            No   不借用
     */
    public void setIsBorrowUkey(Integer isBorrowUkey) {
        this.isBorrowUkey = isBorrowUkey;
    }

    /**
     * 获取AJY   安检院
            KT     科特
     *
     * @return UKEY_TYPE - AJY   安检院
            KT     科特
     */
    public String getUkeyType() {
        return ukeyType;
    }

    /**
     * 设置AJY   安检院
            KT     科特
     *
     * @param ukeyType AJY   安检院
            KT     科特
     */
    public void setUkeyType(String ukeyType) {
        this.ukeyType = ukeyType == null ? null : ukeyType.trim();
    }

    /**
     * 获取Yes  归还
            No  未归还
     *
     * @return IS_RETURN - Yes  归还
            No  未归还
     */
    public String getIsReturn() {
        return isReturn;
    }

    /**
     * 设置Yes  归还
            No  未归还
     *
     * @param isReturn Yes  归还
            No  未归还
     */
    public void setIsReturn(String isReturn) {
        this.isReturn = isReturn == null ? null : isReturn.trim();
    }

    /**
     * @return RETURN_UKEY_AT
     */
    public Date getReturnUkeyAt() {
        return returnUkeyAt;
    }

    /**
     * @param returnUkeyAt
     */
    public void setReturnUkeyAt(Date returnUkeyAt) {
        this.returnUkeyAt = returnUkeyAt;
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
     * @return IS_GET_BID_DOC
     */
    public String getIsGetBidDoc() {
        return isGetBidDoc;
    }

    /**
     * @param isGetBidDoc
     */
    public void setIsGetBidDoc(String isGetBidDoc) {
        this.isGetBidDoc = isGetBidDoc == null ? null : isGetBidDoc.trim();
    }

    /**
     * @return GET_BID_DOC_ACTUAL_AT
     */
    public Date getGetBidDocActualAt() {
        return getBidDocActualAt;
    }

    /**
     * @param getBidDocActualAt
     */
    public void setGetBidDocActualAt(Date getBidDocActualAt) {
        this.getBidDocActualAt = getBidDocActualAt == null ? null : getBidDocActualAt;
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
     * @return GA_END_AT
     */
    public Date getGaEndAt() {
        return gaEndAt;
    }

    /**
     * @param gaEndAt
     */
    public void setGaEndAt(Date gaEndAt) {
        this.gaEndAt = gaEndAt;
    }

    /**
     * @return IS_GUARANTEE_AMOUNT
     */
    public String getIsGuaranteeAmount() {
        return isGuaranteeAmount;
    }

    /**
     * @param isGuaranteeAmount
     */
    public void setIsGuaranteeAmount(String isGuaranteeAmount) {
        this.isGuaranteeAmount = isGuaranteeAmount == null ? null : isGuaranteeAmount.trim();
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
        this.gaActualAt = gaActualAt == null ? null : gaActualAt;
    }

    /**
     * @return UPLOAD_PLAN_AT
     */
    public Date getUploadPlanAt() {
        return uploadPlanAt;
    }

    /**
     * @param uploadPlanAt
     */
    public void setUploadPlanAt(Date uploadPlanAt) {
        this.uploadPlanAt = uploadPlanAt;
    }

    /**
     * @return IS_UPLOAD_PLAN
     */
    public String getIsUploadPlan() {
        return isUploadPlan;
    }

    /**
     * @param isUploadPlan
     */
    public void setIsUploadPlan(String isUploadPlan) {
        this.isUploadPlan = isUploadPlan == null ? null : isUploadPlan.trim();
    }

    /**
     * @return UPLOAD_ACTUAL_AT
     */
    public Date getUploadActualAt() {
        return uploadActualAt;
    }

    /**
     * @param uploadActualAt
     */
    public void setUploadActualAt(Date uploadActualAt) {
        this.uploadActualAt = uploadActualAt == null ? null : uploadActualAt;
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
     * @return IS_POST_BID_DOC
     */
    public String getIsPostBidDoc() {
        return isPostBidDoc;
    }

    /**
     * @param isPostBidDoc
     */
    public void setIsPostBidDoc(String isPostBidDoc) {
        this.isPostBidDoc = isPostBidDoc == null ? null : isPostBidDoc.trim();
    }

    /**
     * @return POST_BID_DOC_ACTAUL_AT
     */
    public Date getPostBidDocActaulAt() {
        return postBidDocActaulAt;
    }

    /**
     * @param postBidDocActaulAt
     */
    public void setPostBidDocActaulAt(Date postBidDocActaulAt) {
        this.postBidDocActaulAt = postBidDocActaulAt == null ? null : postBidDocActaulAt;
    }

    /**
     * @return IS_BID
     */
    public Integer getIsBid() {
        return isBid;
    }

    /**
     * @param isBid
     */
    public void setIsBid(Integer isBid) {
        this.isBid = isBid;
    }

    /**
     * @return UPLOAD_BID_NOTICE_AT
     */
    public Date getUploadBidNoticeAt() {
        return uploadBidNoticeAt;
    }

    /**
     * @param uploadBidNoticeAt
     */
    public void setUploadBidNoticeAt(Date uploadBidNoticeAt) {
        this.uploadBidNoticeAt = uploadBidNoticeAt;
    }

    /**
     * @return UN_BID_DESC
     */
    public String getUnBidDesc() {
        return unBidDesc;
    }

    /**
     * @param unBidDesc
     */
    public void setUnBidDesc(String unBidDesc) {
        this.unBidDesc = unBidDesc == null ? null : unBidDesc.trim();
    }

    /**
     * @return BID_PROJ_DESC
     */
    public String getBidProjDesc() {
        return bidProjDesc;
    }

    /**
     * @param bidProjDesc
     */
    public void setBidProjDesc(String bidProjDesc) {
        this.bidProjDesc = bidProjDesc == null ? null : bidProjDesc.trim();
    }

    /**
     * 获取1、投标准备   BidPrepare
            2、投标审核   BidAuditing
            3、退回          Back
            4、投标完成   Complete
            
            
     *
     * @return PROJ_STATUS - 1、投标准备   BidPrepare
            2、投标审核   BidAuditing
            3、退回          Back
            4、投标完成   Complete
            
            
     */
    public String getProjStatus() {
        return projStatus;
    }

    /**
     * 设置1、投标准备   BidPrepare
            2、投标审核   BidAuditing
            3、退回          Back
            4、投标完成   Complete
            
            
     *
     * @param projStatus 1、投标准备   BidPrepare
            2、投标审核   BidAuditing
            3、退回          Back
            4、投标完成   Complete
            
            
     */
    public void setProjStatus(String projStatus) {
        this.projStatus = projStatus == null ? null : projStatus.trim();
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

    public String getPayCompany() {
        return payCompany;
    }

    public void setPayCompany(String payCompany) {
        this.payCompany = payCompany;
    }
}
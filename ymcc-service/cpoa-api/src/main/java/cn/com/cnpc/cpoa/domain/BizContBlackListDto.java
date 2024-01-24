package cn.com.cnpc.cpoa.domain;

import cn.com.cnpc.cpoa.common.annotation.Excel;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_CONT_BLACKLIST")
public class BizContBlackListDto {
    @Id
    @Column(name = "BLACKLIST_ID")
    private String blacklistId;

    @Excel(name = "承包商名称")
    @Column(name = "CONT_NAME")
    private String contName;
    @Excel(name = "工商注册号")
    @Column(name = "CONT_IAC_NO")
    private String contIacNo;
    @Excel(name = "税务登记号")
    @Column(name = "CONT_TAX_NO")
    private String contTaxNo;
    @Excel(name = "组织机构代码")
    @Column(name = "CONT_ORG_NO")
    private String contOrgNo;
    @Excel(name = "法定代表人姓名")
    @Column(name = "CORPORATE")
    private String corporate;

    @Excel(name = "注册资本（万元）")
    @Column(name = "CONT_REG_CAPTIAL")
    private Long contRegCaptial;
    @Excel(name = "联系人姓名")
    @Column(name = "LINKMAN")
    private String linkman;
    @Excel(name = "联系人电话-移动")
    @Column(name = "LINK_MOBILE")
    private String linkMobile;
    @Excel(name = "联系人电话-固定")
    @Column(name = "LINK_PHONE")
    private String linkPhone;

    @Column(name = "LINK_COMPANY")
    private String linkCompany;

    @Column(name = "COM_TYPE")
    private String comType;

    @Excel(name = "成立时间")
    @Column(name = "SETUP_TIME")
    private Date setupTime;

    @Column(name = "CONT_BANK")
    private String contBank;

    @Column(name = "CONT_ACCOUNT")
    private String contAccount;

    @Column(name = "LINK_FAX")
    private String linkFax;

    @Column(name = "LINK_MAIL")
    private String linkMail;

    @Column(name = "CONT_SITE_URL")
    private String contSiteUrl;
    @Excel(name = "准入级别")
    @Column(name = "ACCESS_LEVEL")
    private String accessLevel;
    @Excel(name = "拉黑原因")
    @Column(name = "BLACK_REASON")
    private String blackReason;
    @Excel(name = "拉黑时间")
    @Column(name = "BLACK_AT")
    private Date blackAt;
    @Excel(name = "拉黑人员")
    @Column(name = "BLACK_MAN")
    private String blackMan;

    @Column(name = "IS_RELIEVE")
    private Integer isRelieve;

    @Column(name = "RELIEVE_REASON")
    private String relieveReason;

    @Column(name = "RELIEVE_AT")
    private Date relieveAt;

    @Column(name = "RELIEVE_MAN")
    private String relieveMan;

    @Column(name = "NOTES")
    private String notes;

    @Excel(name = "地址-详情")
    @Column(name = "CONT_ADDR_DETAIL")
    private String contAddrDetail;

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
     * @return CONT_NAME
     */
    public String getContName() {
        return contName;
    }

    /**
     * @param contName
     */
    public void setContName(String contName) {
        this.contName = contName == null ? null : contName.trim();
    }

    /**
     * @return CONT_IAC_NO
     */
    public String getContIacNo() {
        return contIacNo;
    }

    /**
     * @param contIacNo
     */
    public void setContIacNo(String contIacNo) {
        this.contIacNo = contIacNo == null ? null : contIacNo.trim();
    }

    /**
     * @return CONT_TAX_NO
     */
    public String getContTaxNo() {
        return contTaxNo;
    }

    /**
     * @param contTaxNo
     */
    public void setContTaxNo(String contTaxNo) {
        this.contTaxNo = contTaxNo == null ? null : contTaxNo.trim();
    }

    /**
     * @return CONT_ORG_NO
     */
    public String getContOrgNo() {
        return contOrgNo;
    }

    /**
     * @param contOrgNo
     */
    public void setContOrgNo(String contOrgNo) {
        this.contOrgNo = contOrgNo == null ? null : contOrgNo.trim();
    }

    /**
     * @return CORPORATE
     */
    public String getCorporate() {
        return corporate;
    }

    /**
     * @param corporate
     */
    public void setCorporate(String corporate) {
        this.corporate = corporate == null ? null : corporate.trim();
    }

    /**
     * @return CONT_REG_CAPTIAL
     */
    public Long getContRegCaptial() {
        return contRegCaptial;
    }

    /**
     * @param contRegCaptial
     */
    public void setContRegCaptial(Long contRegCaptial) {
        this.contRegCaptial = contRegCaptial;
    }

    /**
     * @return LINKMAN
     */
    public String getLinkman() {
        return linkman;
    }

    /**
     * @param linkman
     */
    public void setLinkman(String linkman) {
        this.linkman = linkman == null ? null : linkman.trim();
    }

    /**
     * @return LINK_MOBILE
     */
    public String getLinkMobile() {
        return linkMobile;
    }

    /**
     * @param linkMobile
     */
    public void setLinkMobile(String linkMobile) {
        this.linkMobile = linkMobile == null ? null : linkMobile.trim();
    }

    /**
     * @return LINK_PHONE
     */
    public String getLinkPhone() {
        return linkPhone;
    }

    /**
     * @param linkPhone
     */
    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone == null ? null : linkPhone.trim();
    }

    /**
     * @return LINK_COMPANY
     */
    public String getLinkCompany() {
        return linkCompany;
    }

    /**
     * @param linkCompany
     */
    public void setLinkCompany(String linkCompany) {
        this.linkCompany = linkCompany == null ? null : linkCompany.trim();
    }

    /**
     * @return COM_TYPE
     */
    public String getComType() {
        return comType;
    }

    /**
     * @param comType
     */
    public void setComType(String comType) {
        this.comType = comType == null ? null : comType.trim();
    }

    /**
     * @return SETUP_TIME
     */
    public Date getSetupTime() {
        return setupTime;
    }

    /**
     * @param setupTime
     */
    public void setSetupTime(Date setupTime) {
        this.setupTime = setupTime;
    }

    /**
     * @return CONT_BANK
     */
    public String getContBank() {
        return contBank;
    }

    /**
     * @param contBank
     */
    public void setContBank(String contBank) {
        this.contBank = contBank == null ? null : contBank.trim();
    }

    /**
     * @return CONT_ACCOUNT
     */
    public String getContAccount() {
        return contAccount;
    }

    /**
     * @param contAccount
     */
    public void setContAccount(String contAccount) {
        this.contAccount = contAccount == null ? null : contAccount.trim();
    }

    /**
     * @return LINK_FAX
     */
    public String getLinkFax() {
        return linkFax;
    }

    /**
     * @param linkFax
     */
    public void setLinkFax(String linkFax) {
        this.linkFax = linkFax == null ? null : linkFax.trim();
    }

    /**
     * @return LINK_MAIL
     */
    public String getLinkMail() {
        return linkMail;
    }

    /**
     * @param linkMail
     */
    public void setLinkMail(String linkMail) {
        this.linkMail = linkMail == null ? null : linkMail.trim();
    }

    /**
     * @return CONT_SITE_URL
     */
    public String getContSiteUrl() {
        return contSiteUrl;
    }

    /**
     * @param contSiteUrl
     */
    public void setContSiteUrl(String contSiteUrl) {
        this.contSiteUrl = contSiteUrl == null ? null : contSiteUrl.trim();
    }

    /**
     * @return ACCESS_LEVEL
     */
    public String getAccessLevel() {
        return accessLevel;
    }

    /**
     * @param accessLevel
     */
    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel == null ? null : accessLevel.trim();
    }

    /**
     * @return BLACK_REASON
     */
    public String getBlackReason() {
        return blackReason;
    }

    /**
     * @param blackReason
     */
    public void setBlackReason(String blackReason) {
        this.blackReason = blackReason == null ? null : blackReason.trim();
    }

    /**
     * @return BLACK_AT
     */
    public Date getBlackAt() {
        return blackAt;
    }

    /**
     * @param blackAt
     */
    public void setBlackAt(Date blackAt) {
        this.blackAt = blackAt;
    }

    /**
     * @return BLACK_MAN
     */
    public String getBlackMan() {
        return blackMan;
    }

    /**
     * @param blackMan
     */
    public void setBlackMan(String blackMan) {
        this.blackMan = blackMan == null ? null : blackMan.trim();
    }

    /**
     * @return IS_RELIEVE
     */
    public Integer getIsRelieve() {
        return isRelieve;
    }

    /**
     * @param isRelieve
     */
    public void setIsRelieve(Integer isRelieve) {
        this.isRelieve = isRelieve;
    }

    /**
     * @return RELIEVE_REASON
     */
    public String getRelieveReason() {
        return relieveReason;
    }

    /**
     * @param relieveReason
     */
    public void setRelieveReason(String relieveReason) {
        this.relieveReason = relieveReason == null ? null : relieveReason.trim();
    }

    /**
     * @return RELIEVE_AT
     */
    public Date getRelieveAt() {
        return relieveAt;
    }

    /**
     * @param relieveAt
     */
    public void setRelieveAt(Date relieveAt) {
        this.relieveAt = relieveAt;
    }

    /**
     * @return RELIEVE_MAN
     */
    public String getRelieveMan() {
        return relieveMan;
    }

    /**
     * @param relieveMan
     */
    public void setRelieveMan(String relieveMan) {
        this.relieveMan = relieveMan == null ? null : relieveMan.trim();
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
     * @return CONT_ADDR_DETAIL
     */
    public String getContAddrDetail() {
        return contAddrDetail;
    }

    /**
     * @param contAddrDetail
     */
    public void setContAddrDetail(String contAddrDetail) {
        this.contAddrDetail = contAddrDetail == null ? null : contAddrDetail.trim();
    }
}
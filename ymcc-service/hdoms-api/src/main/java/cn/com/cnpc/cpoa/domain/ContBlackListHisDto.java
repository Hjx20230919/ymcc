package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_CONT_BLACKLIST_HIS")
public class ContBlackListHisDto {
    @Id
    @Column(name = "BLACKLIST_HIS_ID")
    private String blacklistHisId;

    @Column(name = "BLACKLIST_ID")
    private String blacklistId;

    @Column(name = "CONT_NAME")
    private String contName;

    @Column(name = "CONT_IAC_NO")
    private String contIacNo;

    @Column(name = "CONT_TAX_NO")
    private String contTaxNo;

    @Column(name = "CONT_ORG_NO")
    private String contOrgNo;

    @Column(name = "ACCESS_LEVEL")
    private String accessLevel;

    @Column(name = "BLACK_REASON")
    private String blackReason;

    @Column(name = "BLACK_AT")
    private Date blackAt;

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

    /**
     * @return BLACKLIST_HIS_ID
     */
    public String getBlacklistHisId() {
        return blacklistHisId;
    }

    /**
     * @param blacklistHisId
     */
    public void setBlacklistHisId(String blacklistHisId) {
        this.blacklistHisId = blacklistHisId == null ? null : blacklistHisId.trim();
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
}
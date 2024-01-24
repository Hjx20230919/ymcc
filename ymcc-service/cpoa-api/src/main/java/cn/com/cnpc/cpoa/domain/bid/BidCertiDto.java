package cn.com.cnpc.cpoa.domain.bid;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_BID_CERTI")
public class BidCertiDto {
    @Id
    @Column(name = "USER_CERTI_ID")
    private String userCertiId;

    @Column(name = "COMPANY_NAME")
    private String companyName;

    /**
     * 高压电工
            低压电工
            防爆电器
            熔化焊接与热切割
            压力焊
            钎焊
            登高架设
            高处安装、
            维护、
            拆除
            其他
            .....可以自定义输入			
            
     */
    @Column(name = "CERTI_TYPE")
    private String certiType;

    @Column(name = "CERTI_NAME")
    private String certiName;

    @Column(name = "CERTI_CODE")
    private String certiCode;

    @Column(name = "CERTI_LEVEL")
    private String certiLevel;

    @Column(name = "ISSUE_DATE")
    private Date issueDate;

    @Column(name = "DUE_DATE")
    private Date dueDate;

    @Column(name = "CERTI_AUTH")
    private String certiAuth;

    @Column(name = "AUDIT_CYCLE")
    private Float auditCycle;

    @Column(name = "AUDIT_DATE_PRE")
    private Date auditDatePre;

    @Column(name = "AUDIT_DATE_NEXT")
    private Date auditDateNext;

    /**
     * 到期前多少天提醒
     */
    @Column(name = "ALART_DAYS")
    private Integer alartDays;

    @Column(name = "NOTES")
    private String notes;

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
     * @return COMPANY_NAME
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    /**
     * 获取高压电工
            低压电工
            防爆电器
            熔化焊接与热切割
            压力焊
            钎焊
            登高架设
            高处安装、
            维护、
            拆除
            其他
            .....可以自定义输入			
            
     *
     * @return CERTI_TYPE - 高压电工
            低压电工
            防爆电器
            熔化焊接与热切割
            压力焊
            钎焊
            登高架设
            高处安装、
            维护、
            拆除
            其他
            .....可以自定义输入			
            
     */
    public String getCertiType() {
        return certiType;
    }

    /**
     * 设置高压电工
            低压电工
            防爆电器
            熔化焊接与热切割
            压力焊
            钎焊
            登高架设
            高处安装、
            维护、
            拆除
            其他
            .....可以自定义输入			
            
     *
     * @param certiType 高压电工
            低压电工
            防爆电器
            熔化焊接与热切割
            压力焊
            钎焊
            登高架设
            高处安装、
            维护、
            拆除
            其他
            .....可以自定义输入			
            
     */
    public void setCertiType(String certiType) {
        this.certiType = certiType == null ? null : certiType.trim();
    }

    /**
     * @return CERTI_NAME
     */
    public String getCertiName() {
        return certiName;
    }

    /**
     * @param certiName
     */
    public void setCertiName(String certiName) {
        this.certiName = certiName == null ? null : certiName.trim();
    }

    /**
     * @return CERTI_CODE
     */
    public String getCertiCode() {
        return certiCode;
    }

    /**
     * @param certiCode
     */
    public void setCertiCode(String certiCode) {
        this.certiCode = certiCode == null ? null : certiCode.trim();
    }

    /**
     * @return CERTI_LEVEL
     */
    public String getCertiLevel() {
        return certiLevel;
    }

    /**
     * @param certiLevel
     */
    public void setCertiLevel(String certiLevel) {
        this.certiLevel = certiLevel == null ? null : certiLevel.trim();
    }

    /**
     * @return ISSUE_DATE
     */
    public Date getIssueDate() {
        return issueDate;
    }

    /**
     * @param issueDate
     */
    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    /**
     * @return DUE_DATE
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * @param dueDate
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * @return CERTI_AUTH
     */
    public String getCertiAuth() {
        return certiAuth;
    }

    /**
     * @param certiAuth
     */
    public void setCertiAuth(String certiAuth) {
        this.certiAuth = certiAuth == null ? null : certiAuth.trim();
    }

    /**
     * @return AUDIT_CYCLE
     */
    public Float getAuditCycle() {
        return auditCycle;
    }

    /**
     * @param auditCycle
     */
    public void setAuditCycle(Float auditCycle) {
        this.auditCycle = auditCycle;
    }

    /**
     * @return AUDIT_DATE_PRE
     */
    public Date getAuditDatePre() {
        return auditDatePre;
    }

    /**
     * @param auditDatePre
     */
    public void setAuditDatePre(Date auditDatePre) {
        this.auditDatePre = auditDatePre;
    }

    /**
     * @return AUDIT_DATE_NEXT
     */
    public Date getAuditDateNext() {
        return auditDateNext;
    }

    /**
     * @param auditDateNext
     */
    public void setAuditDateNext(Date auditDateNext) {
        this.auditDateNext = auditDateNext;
    }

    /**
     * 获取到期前多少天提醒
     *
     * @return ALART_DAYS - 到期前多少天提醒
     */
    public Integer getAlartDays() {
        return alartDays;
    }

    /**
     * 设置到期前多少天提醒
     *
     * @param alartDays 到期前多少天提醒
     */
    public void setAlartDays(Integer alartDays) {
        this.alartDays = alartDays;
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
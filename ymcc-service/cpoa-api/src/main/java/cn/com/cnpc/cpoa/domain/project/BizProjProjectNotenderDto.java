package cn.com.cnpc.cpoa.domain.project;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_PROJ_PROJECT_NOTENDER")
public class BizProjProjectNotenderDto {

    @Id
    @Column(name = "NTENDER_ID")
    private String ntenderId;

    @Column(name = "PROJ_ID")
    private String projId;

    /**
     * 默认为可不招标
     */
    @Column(name = "NTENDER_SEL_CONT_TYPE")
    private String ntenderSelContType;

    /**
     * 工程项目/物资采购/服务
     */
    @Column(name = "NTENDER_PROJ_TYPE")
    private String ntenderProjType;

    @Column(name = "NTENDER_PLAN_NO")
    private String ntenderPlanNo;

    /**
     * 一类 二类 三类 四类
     */
    @Column(name = "NTENDER_TYPE")
    private String ntenderType;

    @Column(name = "NTENDER_PURCHASE_TYPE")
    private String ntenderPurchaseType;

    @Column(name = "NTENDER_START_DATE")
    private Date ntenderStartDate;

    @Column(name = "NTENDER_END_DATE")
    private Date ntenderEndDate;

    @Column(name = "NTENDER_VALUTION_TYPE")
    private String ntenderValutionType;

    @Column(name = "NTENDER_GROUP")
    private String ntenderGroup;

    @Column(name = "NTENDER_SUMMARY")
    private String ntenderSummary;

    @Column(name = "NTENDER_REASON")
    private String ntenderReason;

    @Column(name = "NTENDER_SUPERVISE")
    private String ntenderSupervise;

    /**
     * @return NTENDER_ID
     */
    public String getNtenderId() {
        return ntenderId;
    }

    /**
     * @param ntenderId
     */
    public void setNtenderId(String ntenderId) {
        this.ntenderId = ntenderId == null ? null : ntenderId.trim();
    }

    /**
     * @return PROJ_ID
     */
    public String getProjId() {
        return projId;
    }

    /**
     * @param projId
     */
    public void setProjId(String projId) {
        this.projId = projId == null ? null : projId.trim();
    }

    /**
     * 获取默认为可不招标
     *
     * @return NTENDER_SEL_CONT_TYPE - 默认为可不招标
     */
    public String getNtenderSelContType() {
        return ntenderSelContType;
    }

    /**
     * 设置默认为可不招标
     *
     * @param ntenderSelContType 默认为可不招标
     */
    public void setNtenderSelContType(String ntenderSelContType) {
        this.ntenderSelContType = ntenderSelContType == null ? null : ntenderSelContType.trim();
    }

    /**
     * 获取工程项目/物资采购/服务
     *
     * @return NTENDER_PROJ_TYPE - 工程项目/物资采购/服务
     */
    public String getNtenderProjType() {
        return ntenderProjType;
    }

    /**
     * 设置工程项目/物资采购/服务
     *
     * @param ntenderProjType 工程项目/物资采购/服务
     */
    public void setNtenderProjType(String ntenderProjType) {
        this.ntenderProjType = ntenderProjType == null ? null : ntenderProjType.trim();
    }

    /**
     * @return NTENDER_PLAN_NO
     */
    public String getNtenderPlanNo() {
        return ntenderPlanNo;
    }

    /**
     * @param ntenderPlanNo
     */
    public void setNtenderPlanNo(String ntenderPlanNo) {
        this.ntenderPlanNo = ntenderPlanNo == null ? null : ntenderPlanNo.trim();
    }

    /**
     * 获取一类 二类 三类 四类
     *
     * @return NTENDER_TYPE - 一类 二类 三类 四类
     */
    public String getNtenderType() {
        return ntenderType;
    }

    /**
     * 设置一类 二类 三类 四类
     *
     * @param ntenderType 一类 二类 三类 四类
     */
    public void setNtenderType(String ntenderType) {
        this.ntenderType = ntenderType == null ? null : ntenderType.trim();
    }

    /**
     * @return NTENDER_PURCHASE_TYPE
     */
    public String getNtenderPurchaseType() {
        return ntenderPurchaseType;
    }

    /**
     * @param ntenderPurchaseType
     */
    public void setNtenderPurchaseType(String ntenderPurchaseType) {
        this.ntenderPurchaseType = ntenderPurchaseType == null ? null : ntenderPurchaseType.trim();
    }

    /**
     * @return NTENDER_START_DATE
     */
    public Date getNtenderStartDate() {
        return ntenderStartDate;
    }

    /**
     * @param ntenderStartDate
     */
    public void setNtenderStartDate(Date ntenderStartDate) {
        this.ntenderStartDate = ntenderStartDate;
    }

    /**
     * @return NTENDER_END_DATE
     */
    public Date getNtenderEndDate() {
        return ntenderEndDate;
    }

    /**
     * @param ntenderEndDate
     */
    public void setNtenderEndDate(Date ntenderEndDate) {
        this.ntenderEndDate = ntenderEndDate;
    }

    /**
     * @return NTENDER_VALUTION_TYPE
     */
    public String getNtenderValutionType() {
        return ntenderValutionType;
    }

    /**
     * @param ntenderValutionType
     */
    public void setNtenderValutionType(String ntenderValutionType) {
        this.ntenderValutionType = ntenderValutionType == null ? null : ntenderValutionType.trim();
    }

    /**
     * @return NTENDER_GROUP
     */
    public String getNtenderGroup() {
        return ntenderGroup;
    }

    /**
     * @param ntenderGroup
     */
    public void setNtenderGroup(String ntenderGroup) {
        this.ntenderGroup = ntenderGroup == null ? null : ntenderGroup.trim();
    }

    /**
     * @return NTENDER_SUMMARY
     */
    public String getNtenderSummary() {
        return ntenderSummary;
    }

    /**
     * @param ntenderSummary
     */
    public void setNtenderSummary(String ntenderSummary) {
        this.ntenderSummary = ntenderSummary == null ? null : ntenderSummary.trim();
    }

    /**
     * @return NTENDER_REASON
     */
    public String getNtenderReason() {
        return ntenderReason;
    }

    /**
     * @param ntenderReason
     */
    public void setNtenderReason(String ntenderReason) {
        this.ntenderReason = ntenderReason == null ? null : ntenderReason.trim();
    }

    /**
     * @return NTENDER_SUPERVISE
     */
    public String getNtenderSupervise() {
        return ntenderSupervise;
    }

    /**
     * @param ntenderSupervise
     */
    public void setNtenderSupervise(String ntenderSupervise) {
        this.ntenderSupervise = ntenderSupervise == null ? null : ntenderSupervise.trim();
    }
}
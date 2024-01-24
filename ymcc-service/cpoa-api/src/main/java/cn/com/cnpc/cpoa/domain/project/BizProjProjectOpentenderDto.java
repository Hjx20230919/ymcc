package cn.com.cnpc.cpoa.domain.project;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_PROJ_PROJECT_OPENTENDER")
public class BizProjProjectOpentenderDto {

    @Id
    @Column(name = "OTENDER_ID")
    private String otenderId;

    @Column(name = "PROJ_ID")
    private String projId;

    /**
     * 默认为公开招标
     */
    @Column(name = "OTENDER_SEL_CONT_TYPE")
    private String otenderSelContType;

    /**
     * 工程项目/物资采购/服务
     */
    @Column(name = "OTENDER_PROJ_TYPE")
    private String otenderProjType;

    @Column(name = "OTENDER_PLAN_NO")
    private String otenderPlanNo;

    /**
     * 一类 二类 三类 四类
     */
    @Column(name = "OTENDER_TYPE")
    private String otenderType;

    /**
     * 公开招标/邀请招标
     */
    @Column(name = "OTENDER_TENDER_TYPE")
    private String otenderTenderType;

    @Column(name = "OTENDER_START_DATE")
    private Date otenderStartDate;

    @Column(name = "OTENDER_END_DATE")
    private Date otenderEndDate;

    @Column(name = "OTENDER_VALUTION_TYPE")
    private String otenderValutionType;

    @Column(name = "OTENDER_MODALITY")
    private String otenderModality;

    @Column(name = "OTENDER_SUPERVISE")
    private String otenderSupervise;

    @Column(name = "OTENDER_CONTENT")
    private String otenderContent;

    @Column(name = "OTENDER_COMMITTEE")
    private String otenderCommittee;

    @Column(name = "OTENDER_QUALIFICATIONS")
    private String otenderQualifications;

    /**
     * @return OTENDER_ID
     */
    public String getOtenderId() {
        return otenderId;
    }

    /**
     * @param otenderId
     */
    public void setOtenderId(String otenderId) {
        this.otenderId = otenderId == null ? null : otenderId.trim();
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
     * 获取默认为公开招标
     *
     * @return OTENDER_SEL_CONT_TYPE - 默认为公开招标
     */
    public String getOtenderSelContType() {
        return otenderSelContType;
    }

    /**
     * 设置默认为公开招标
     *
     * @param otenderSelContType 默认为公开招标
     */
    public void setOtenderSelContType(String otenderSelContType) {
        this.otenderSelContType = otenderSelContType == null ? null : otenderSelContType.trim();
    }

    /**
     * 获取工程项目/物资采购/服务
     *
     * @return OTENDER_PROJ_TYPE - 工程项目/物资采购/服务
     */
    public String getOtenderProjType() {
        return otenderProjType;
    }

    /**
     * 设置工程项目/物资采购/服务
     *
     * @param otenderProjType 工程项目/物资采购/服务
     */
    public void setOtenderProjType(String otenderProjType) {
        this.otenderProjType = otenderProjType == null ? null : otenderProjType.trim();
    }

    /**
     * @return OTENDER_PLAN_NO
     */
    public String getOtenderPlanNo() {
        return otenderPlanNo;
    }

    /**
     * @param otenderPlanNo
     */
    public void setOtenderPlanNo(String otenderPlanNo) {
        this.otenderPlanNo = otenderPlanNo == null ? null : otenderPlanNo.trim();
    }

    /**
     * 获取一类 二类 三类 四类
     *
     * @return OTENDER_TYPE - 一类 二类 三类 四类
     */
    public String getOtenderType() {
        return otenderType;
    }

    /**
     * 设置一类 二类 三类 四类
     *
     * @param otenderType 一类 二类 三类 四类
     */
    public void setOtenderType(String otenderType) {
        this.otenderType = otenderType == null ? null : otenderType.trim();
    }

    /**
     * 获取公开招标/邀请招标
     *
     * @return OTENDER_TENDER_TYPE - 公开招标/邀请招标
     */
    public String getOtenderTenderType() {
        return otenderTenderType;
    }

    /**
     * 设置公开招标/邀请招标
     *
     * @param otenderTenderType 公开招标/邀请招标
     */
    public void setOtenderTenderType(String otenderTenderType) {
        this.otenderTenderType = otenderTenderType == null ? null : otenderTenderType.trim();
    }

    /**
     * @return OTENDER_START_DATE
     */
    public Date getOtenderStartDate() {
        return otenderStartDate;
    }

    /**
     * @param otenderStartDate
     */
    public void setOtenderStartDate(Date otenderStartDate) {
        this.otenderStartDate = otenderStartDate;
    }

    /**
     * @return OTENDER_END_DATE
     */
    public Date getOtenderEndDate() {
        return otenderEndDate;
    }

    /**
     * @param otenderEndDate
     */
    public void setOtenderEndDate(Date otenderEndDate) {
        this.otenderEndDate = otenderEndDate;
    }

    /**
     * @return OTENDER_VALUTION_TYPE
     */
    public String getOtenderValutionType() {
        return otenderValutionType;
    }

    /**
     * @param otenderValutionType
     */
    public void setOtenderValutionType(String otenderValutionType) {
        this.otenderValutionType = otenderValutionType == null ? null : otenderValutionType.trim();
    }

    /**
     * @return OTENDER_MODALITY
     */
    public String getOtenderModality() {
        return otenderModality;
    }

    /**
     * @param otenderModality
     */
    public void setOtenderModality(String otenderModality) {
        this.otenderModality = otenderModality == null ? null : otenderModality.trim();
    }

    /**
     * @return OTENDER_SUPERVISE
     */
    public String getOtenderSupervise() {
        return otenderSupervise;
    }

    /**
     * @param otenderSupervise
     */
    public void setOtenderSupervise(String otenderSupervise) {
        this.otenderSupervise = otenderSupervise == null ? null : otenderSupervise.trim();
    }

    /**
     * @return OTENDER_CONTENT
     */
    public String getOtenderContent() {
        return otenderContent;
    }

    /**
     * @param otenderContent
     */
    public void setOtenderContent(String otenderContent) {
        this.otenderContent = otenderContent == null ? null : otenderContent.trim();
    }

    /**
     * @return OTENDER_COMMITTEE
     */
    public String getOtenderCommittee() {
        return otenderCommittee;
    }

    /**
     * @param otenderCommittee
     */
    public void setOtenderCommittee(String otenderCommittee) {
        this.otenderCommittee = otenderCommittee == null ? null : otenderCommittee.trim();
    }

    /**
     * @return OTENDER_QUALIFICATIONS
     */
    public String getOtenderQualifications() {
        return otenderQualifications;
    }

    /**
     * @param otenderQualifications
     */
    public void setOtenderQualifications(String otenderQualifications) {
        this.otenderQualifications = otenderQualifications == null ? null : otenderQualifications.trim();
    }
}
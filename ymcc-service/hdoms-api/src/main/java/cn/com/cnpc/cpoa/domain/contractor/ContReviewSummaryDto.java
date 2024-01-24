package cn.com.cnpc.cpoa.domain.contractor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_CONT_REVIEW_SUMMARY")
public class ContReviewSummaryDto {
    @Id
    @Column(name = "REVIEW_SUMMARY_ID")
    private String reviewSummaryId;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "CREATE_AT")
    private Date createAt;

    /**
     * 待考评   draft
            考评中  reviewing
            考评完成  down
            
     */
    @Column(name = "TASK_STATUS")
    private String taskStatus;

    @Column(name = "REVIEW_YEAR")
    private String reviewYear;

    @Column(name = "CONT_ID")
    private String contId;

    @Column(name = "OWNER_DEPT_ID")
    private String ownerDeptId;

    @Column(name = "BASIC_SCORE")
    private Float basicScore;

    @Column(name = "QHSE_SCORE")
    private Float qhseScore;

    @Column(name = "PRODUCTION_SCORE")
    private Float productionScore;

    @Column(name = "OM_SCORE")
    private Float omScore;

    @Column(name = "FINANCIAL_SCORE")
    private Float financialScore;

    @Column(name = "TOTAL_SCORE")
    private Float totalScore;

    /**
     * 90以上     优秀
     */
    @Column(name = "EVAL_CONCLUSION")
    private String evalConclusion;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return REVIEW_SUMMARY_ID
     */
    public String getReviewSummaryId() {
        return reviewSummaryId;
    }

    /**
     * @param reviewSummaryId
     */
    public void setReviewSummaryId(String reviewSummaryId) {
        this.reviewSummaryId = reviewSummaryId == null ? null : reviewSummaryId.trim();
    }

    /**
     * @return CREATE_ID
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * @param createId
     */
    public void setCreateId(String createId) {
        this.createId = createId == null ? null : createId.trim();
    }

    /**
     * @return CREATE_AT
     */
    public Date getCreateAt() {
        return createAt;
    }

    /**
     * @param createAt
     */
    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    /**
     * 获取待考评   draft
            考评中  reviewing
            考评完成  down
            
     *
     * @return TASK_STATUS - 待考评   draft
            考评中  reviewing
            考评完成  down
            
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * 设置待考评   draft
            考评中  reviewing
            考评完成  down
            
     *
     * @param taskStatus 待考评   draft
            考评中  reviewing
            考评完成  down
            
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus == null ? null : taskStatus.trim();
    }

    /**
     * @return REVIEW_YEAR
     */
    public String getReviewYear() {
        return reviewYear;
    }

    /**
     * @param reviewYear
     */
    public void setReviewYear(String reviewYear) {
        this.reviewYear = reviewYear == null ? null : reviewYear.trim();
    }

    /**
     * @return CONT_ID
     */
    public String getContId() {
        return contId;
    }

    /**
     * @param contId
     */
    public void setContId(String contId) {
        this.contId = contId == null ? null : contId.trim();
    }

    /**
     * @return OWNER_DEPT_ID
     */
    public String getOwnerDeptId() {
        return ownerDeptId;
    }

    /**
     * @param ownerDeptId
     */
    public void setOwnerDeptId(String ownerDeptId) {
        this.ownerDeptId = ownerDeptId == null ? null : ownerDeptId.trim();
    }

    /**
     * @return BASIC_SCORE
     */
    public Float getBasicScore() {
        return basicScore;
    }

    /**
     * @param basicScore
     */
    public void setBasicScore(Float basicScore) {
        this.basicScore = basicScore;
    }

    /**
     * @return QHSE_SCORE
     */
    public Float getQhseScore() {
        return qhseScore;
    }

    /**
     * @param qhseScore
     */
    public void setQhseScore(Float qhseScore) {
        this.qhseScore = qhseScore;
    }

    /**
     * @return PRODUCTION_SCORE
     */
    public Float getProductionScore() {
        return productionScore;
    }

    /**
     * @param productionScore
     */
    public void setProductionScore(Float productionScore) {
        this.productionScore = productionScore;
    }

    /**
     * @return OM_SCORE
     */
    public Float getOmScore() {
        return omScore;
    }

    /**
     * @param omScore
     */
    public void setOmScore(Float omScore) {
        this.omScore = omScore;
    }

    /**
     * @return FINANCIAL_SCORE
     */
    public Float getFinancialScore() {
        return financialScore;
    }

    /**
     * @param financialScore
     */
    public void setFinancialScore(Float financialScore) {
        this.financialScore = financialScore;
    }

    /**
     * @return TOTAL_SCORE
     */
    public Float getTotalScore() {
        return totalScore;
    }

    /**
     * @param totalScore
     */
    public void setTotalScore(Float totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * 获取90以上     优秀
     *
     * @return EVAL_CONCLUSION - 90以上     优秀
     */
    public String getEvalConclusion() {
        return evalConclusion;
    }

    /**
     * 设置90以上     优秀
     *
     * @param evalConclusion 90以上     优秀
     */
    public void setEvalConclusion(String evalConclusion) {
        this.evalConclusion = evalConclusion == null ? null : evalConclusion.trim();
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
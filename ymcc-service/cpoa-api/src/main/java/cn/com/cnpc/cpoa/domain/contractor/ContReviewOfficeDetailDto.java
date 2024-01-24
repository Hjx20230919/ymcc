package cn.com.cnpc.cpoa.domain.contractor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_CONT_REVIEW_OFFICE_DETAIL")
public class ContReviewOfficeDetailDto {
    @Id
    @Column(name = "OFFICE_DETAIL_ID")
    private String officeDetailId;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "CREATE_AT")
    private Date createAt;

    /**
     * 待考评   draft
            考评中  reviewing
            审核中  buildAuditing
            退回   back
            考评完成  down
            
            
            ("draft","草稿"),("back","退回"),("buildAuditing","审核中"),("down","审核完毕")
     */
    @Column(name = "TASK_STATUS")
    private String taskStatus;

    @Column(name = "REVIEW_YEAR")
    private String reviewYear;

    @Column(name = "CONT_ID")
    private String contId;

    @Column(name = "OWNER_DEPT_ID")
    private String ownerDeptId;

    @Column(name = "TOTAL_SCORE")
    private Float totalScore;

    @Column(name = "PROPORTION")
    private Float proportion;

    @Column(name = "CONVERSION_SCORE")
    private Float conversionScore;

    @Column(name = "EVAL_CONCLUSION")
    private String evalConclusion;

    @Column(name = "EVAL_AT")
    private Date evalAt;

    @Column(name = "EVAL_MAN")
    private String evalMan;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return OFFICE_DETAIL_ID
     */
    public String getOfficeDetailId() {
        return officeDetailId;
    }

    /**
     * @param officeDetailId
     */
    public void setOfficeDetailId(String officeDetailId) {
        this.officeDetailId = officeDetailId == null ? null : officeDetailId.trim();
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
            审核中  buildAuditing
            退回   back
            考评完成  down
            
            
            ("draft","草稿"),("back","退回"),("buildAuditing","审核中"),("down","审核完毕")
     *
     * @return TASK_STATUS - 待考评   draft
            考评中  reviewing
            审核中  buildAuditing
            退回   back
            考评完成  down
            
            
            ("draft","草稿"),("back","退回"),("buildAuditing","审核中"),("down","审核完毕")
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * 设置待考评   draft
            考评中  reviewing
            审核中  buildAuditing
            退回   back
            考评完成  down
            
            
            ("draft","草稿"),("back","退回"),("buildAuditing","审核中"),("down","审核完毕")
     *
     * @param taskStatus 待考评   draft
            考评中  reviewing
            审核中  buildAuditing
            退回   back
            考评完成  down
            
            
            ("draft","草稿"),("back","退回"),("buildAuditing","审核中"),("down","审核完毕")
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
     * @return PROPORTION
     */
    public Float getProportion() {
        return proportion;
    }

    /**
     * @param proportion
     */
    public void setProportion(Float proportion) {
        this.proportion = proportion;
    }

    /**
     * @return CONVERSION_SCORE
     */
    public Float getConversionScore() {
        return conversionScore;
    }

    /**
     * @param conversionScore
     */
    public void setConversionScore(Float conversionScore) {
        this.conversionScore = conversionScore;
    }

    /**
     * @return EVAL_CONCLUSION
     */
    public String getEvalConclusion() {
        return evalConclusion;
    }

    /**
     * @param evalConclusion
     */
    public void setEvalConclusion(String evalConclusion) {
        this.evalConclusion = evalConclusion == null ? null : evalConclusion.trim();
    }

    /**
     * @return EVAL_AT
     */
    public Date getEvalAt() {
        return evalAt;
    }

    /**
     * @param evalAt
     */
    public void setEvalAt(Date evalAt) {
        this.evalAt = evalAt;
    }

    /**
     * @return EVAL_MAN
     */
    public String getEvalMan() {
        return evalMan;
    }

    /**
     * @param evalMan
     */
    public void setEvalMan(String evalMan) {
        this.evalMan = evalMan == null ? null : evalMan.trim();
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
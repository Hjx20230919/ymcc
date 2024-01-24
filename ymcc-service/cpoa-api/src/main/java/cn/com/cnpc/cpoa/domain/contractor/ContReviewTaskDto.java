package cn.com.cnpc.cpoa.domain.contractor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;
import javax.persistence.*;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_CONT_REVIEW_TASK")
public class ContReviewTaskDto {
    @Id
    @Column(name = "REVIEW_TASK_ID")
    private String reviewTaskId;

    @Column(name = "CREATE_ID")
    private String createId;

    @Column(name = "CREATE_AT")
    private Date createAt;

    /**
     * -- 待考评，考评任务表中，无对应的项目
            -- 下面三个状态都是考评单位内部操作，对项目来说，都叫“考评中”
            考评中  reviewing
            审核中  buildAuditing
            退回   back
            
            -- 基础单位提交完成，考评结算
            考评完成  down
            
            -- 未完成待考评项目，也要写入一个考评任务数据
            未完成待考评项目     wait_review
            
            -- 其他不考评项目，也要写入一个考评任务数据
            不考评     no_review
            
     */
    @Column(name = "TASK_STATUS")
    private String taskStatus;

    @Column(name = "REVIEW_YEAR")
    private String reviewYear;

    @Column(name = "CONT_ID")
    private String contId;

    @Column(name = "OWNER_DEPT_ID")
    private String ownerDeptId;

    @Column(name = "PROJ_ID")
    private String projId;

    /**
     * 默认是采购结果金额，管理员可以修改
     */
    @Column(name = "DEAL_VALUE")
    private Float dealValue;

    /**
     * 基层单位填写
     */
    @Column(name = "DEAL_FINISH_VALUE")
    private Float dealFinishValue;

    /**
     * 默认：准入类型
            
            问题以前数据，在选商时没有设置准入类型，问题：如果一个承包商有多个专业类别，就需要手动选择
     */
    @Column(name = "ACC_NAME")
    private String accName;

    /**
     * 也准入类别一致
     */
    @Column(name = "ACC_TYPE")
    private String accType;

    @Column(name = "FINISH_AT")
    private Date finishAt;

    @Column(name = "IE_DESC")
    private String ieDesc;

    @Column(name = "TOTAL_SCORE")
    private Float totalScore;

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
     * @return REVIEW_TASK_ID
     */
    public String getReviewTaskId() {
        return reviewTaskId;
    }

    /**
     * @param reviewTaskId
     */
    public void setReviewTaskId(String reviewTaskId) {
        this.reviewTaskId = reviewTaskId == null ? null : reviewTaskId.trim();
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
     * 获取-- 待考评，考评任务表中，无对应的项目
            -- 下面三个状态都是考评单位内部操作，对项目来说，都叫“考评中”
            考评中  reviewing
            审核中  buildAuditing
            退回   back
            
            -- 基础单位提交完成，考评结算
            考评完成  down
            
            -- 未完成待考评项目，也要写入一个考评任务数据
            未完成待考评项目     wait_review
            
            -- 其他不考评项目，也要写入一个考评任务数据
            不考评     no_review
            
     *
     * @return TASK_STATUS - -- 待考评，考评任务表中，无对应的项目
            -- 下面三个状态都是考评单位内部操作，对项目来说，都叫“考评中”
            考评中  reviewing
            审核中  buildAuditing
            退回   back
            
            -- 基础单位提交完成，考评结算
            考评完成  down
            
            -- 未完成待考评项目，也要写入一个考评任务数据
            未完成待考评项目     wait_review
            
            -- 其他不考评项目，也要写入一个考评任务数据
            不考评     no_review
            
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * 设置-- 待考评，考评任务表中，无对应的项目
            -- 下面三个状态都是考评单位内部操作，对项目来说，都叫“考评中”
            考评中  reviewing
            审核中  buildAuditing
            退回   back
            
            -- 基础单位提交完成，考评结算
            考评完成  down
            
            -- 未完成待考评项目，也要写入一个考评任务数据
            未完成待考评项目     wait_review
            
            -- 其他不考评项目，也要写入一个考评任务数据
            不考评     no_review
            
     *
     * @param taskStatus -- 待考评，考评任务表中，无对应的项目
            -- 下面三个状态都是考评单位内部操作，对项目来说，都叫“考评中”
            考评中  reviewing
            审核中  buildAuditing
            退回   back
            
            -- 基础单位提交完成，考评结算
            考评完成  down
            
            -- 未完成待考评项目，也要写入一个考评任务数据
            未完成待考评项目     wait_review
            
            -- 其他不考评项目，也要写入一个考评任务数据
            不考评     no_review
            
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
     * 获取默认是采购结果金额，管理员可以修改
     *
     * @return DEAL_VALUE - 默认是采购结果金额，管理员可以修改
     */
    public Float getDealValue() {
        return dealValue;
    }

    /**
     * 设置默认是采购结果金额，管理员可以修改
     *
     * @param dealValue 默认是采购结果金额，管理员可以修改
     */
    public void setDealValue(Float dealValue) {
        this.dealValue = dealValue;
    }

    /**
     * 获取基层单位填写
     *
     * @return DEAL_FINISH_VALUE - 基层单位填写
     */
    public Float getDealFinishValue() {
        return dealFinishValue;
    }

    /**
     * 设置基层单位填写
     *
     * @param dealFinishValue 基层单位填写
     */
    public void setDealFinishValue(Float dealFinishValue) {
        this.dealFinishValue = dealFinishValue;
    }

    /**
     * 获取默认：准入类型
            
            问题以前数据，在选商时没有设置准入类型，问题：如果一个承包商有多个专业类别，就需要手动选择
     *
     * @return ACC_NAME - 默认：准入类型
            
            问题以前数据，在选商时没有设置准入类型，问题：如果一个承包商有多个专业类别，就需要手动选择
     */
    public String getAccName() {
        return accName;
    }

    /**
     * 设置默认：准入类型
            
            问题以前数据，在选商时没有设置准入类型，问题：如果一个承包商有多个专业类别，就需要手动选择
     *
     * @param accName 默认：准入类型
            
            问题以前数据，在选商时没有设置准入类型，问题：如果一个承包商有多个专业类别，就需要手动选择
     */
    public void setAccName(String accName) {
        this.accName = accName == null ? null : accName.trim();
    }

    /**
     * 获取也准入类别一致
     *
     * @return ACC_TYPE - 也准入类别一致
     */
    public String getAccType() {
        return accType;
    }

    /**
     * 设置也准入类别一致
     *
     * @param accType 也准入类别一致
     */
    public void setAccType(String accType) {
        this.accType = accType == null ? null : accType.trim();
    }

    /**
     * @return FINISH_AT
     */
    public Date getFinishAt() {
        return finishAt;
    }

    /**
     * @param finishAt
     */
    public void setFinishAt(Date finishAt) {
        this.finishAt = finishAt;
    }

    /**
     * @return IE_DESC
     */
    public String getIeDesc() {
        return ieDesc;
    }

    /**
     * @param ieDesc
     */
    public void setIeDesc(String ieDesc) {
        this.ieDesc = ieDesc == null ? null : ieDesc.trim();
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
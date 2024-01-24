package cn.com.cnpc.cpoa.domain.contractor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "T_CONT_REVIEW_DETAIL")
public class ContReviewDetailDto {
    @Id
    @Column(name = "REVIEW_DETAIL_ID")
    private String reviewDetailId;

    @Column(name = "REVIEW_TASK_ID")
    private String reviewTaskId;

    /**
     * Basic  基本条件评价
            Achie  业绩评价
     */
    @Column(name = "REVIEW_DETAIL_TYPE")
    private String reviewDetailType;

    /**
     * 1.项目组织实施
            2.项目质量管理
            3.项目职业健康安全与环境管理
            4.项目成本管理
            5.项目进度管理
            6.项目分包管理
            7.信息资料管理
            8.诚信履约
            
     */
    @Column(name = "REVIEW_DETAIL_SUB_TYPE")
    private String reviewDetailSubType;

    @Column(name = "REVIEW_DETAIL_NO")
    private String reviewDetailNo;

    @Column(name = "REVIEW_DETAIL_CTX")
    private String reviewDetailCtx;

    /**
     * 是否，INT            0 否，1是
            分值，FLOAT      float
     */
    @Column(name = "DATA_TYPE")
    private String dataType;

    @Column(name = "REF_VALUE")
    private String refValue;

    @Column(name = "REVIEW_TI_VALUE")
    private Float reviewTiValue;

    @Column(name = "REVIEW_VALUE")
    private String reviewValue;

    @Column(name = "NOTES")
    private String notes;

    public Float getReviewTiValue() {
        return reviewTiValue;
    }

    public void setReviewTiValue(Float reviewTiValue) {
        this.reviewTiValue = reviewTiValue;
    }

    /**
     * @return REVIEW_DETAIL_ID
     */
    public String getReviewDetailId() {
        return reviewDetailId;
    }

    /**
     * @param reviewDetailId
     */
    public void setReviewDetailId(String reviewDetailId) {
        this.reviewDetailId = reviewDetailId == null ? null : reviewDetailId.trim();
    }

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
     * 获取Basic  基本条件评价
            Achie  业绩评价
     *
     * @return REVIEW_DETAIL_TYPE - Basic  基本条件评价
            Achie  业绩评价
     */
    public String getReviewDetailType() {
        return reviewDetailType;
    }

    /**
     * 设置Basic  基本条件评价
            Achie  业绩评价
     *
     * @param reviewDetailType Basic  基本条件评价
            Achie  业绩评价
     */
    public void setReviewDetailType(String reviewDetailType) {
        this.reviewDetailType = reviewDetailType == null ? null : reviewDetailType.trim();
    }

    /**
     * 获取1.项目组织实施
            2.项目质量管理
            3.项目职业健康安全与环境管理
            4.项目成本管理
            5.项目进度管理
            6.项目分包管理
            7.信息资料管理
            8.诚信履约
            
     *
     * @return REVIEW_DETAIL_SUB_TYPE - 1.项目组织实施
            2.项目质量管理
            3.项目职业健康安全与环境管理
            4.项目成本管理
            5.项目进度管理
            6.项目分包管理
            7.信息资料管理
            8.诚信履约
            
     */
    public String getReviewDetailSubType() {
        return reviewDetailSubType;
    }

    /**
     * 设置1.项目组织实施
            2.项目质量管理
            3.项目职业健康安全与环境管理
            4.项目成本管理
            5.项目进度管理
            6.项目分包管理
            7.信息资料管理
            8.诚信履约
            
     *
     * @param reviewDetailSubType 1.项目组织实施
            2.项目质量管理
            3.项目职业健康安全与环境管理
            4.项目成本管理
            5.项目进度管理
            6.项目分包管理
            7.信息资料管理
            8.诚信履约
            
     */
    public void setReviewDetailSubType(String reviewDetailSubType) {
        this.reviewDetailSubType = reviewDetailSubType == null ? null : reviewDetailSubType.trim();
    }

    /**
     * @return REVIEW_DETAIL_NO
     */
    public String getReviewDetailNo() {
        return reviewDetailNo;
    }

    /**
     * @param reviewDetailNo
     */
    public void setReviewDetailNo(String reviewDetailNo) {
        this.reviewDetailNo = reviewDetailNo == null ? null : reviewDetailNo.trim();
    }

    /**
     * @return REVIEW_DETAIL_CTX
     */
    public String getReviewDetailCtx() {
        return reviewDetailCtx;
    }

    /**
     * @param reviewDetailCtx
     */
    public void setReviewDetailCtx(String reviewDetailCtx) {
        this.reviewDetailCtx = reviewDetailCtx == null ? null : reviewDetailCtx.trim();
    }

    /**
     * 获取是否，INT            0 否，1是
            分值，FLOAT      float
     *
     * @return DATA_TYPE - 是否，INT            0 否，1是
            分值，FLOAT      float
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * 设置是否，INT            0 否，1是
            分值，FLOAT      float
     *
     * @param dataType 是否，INT            0 否，1是
            分值，FLOAT      float
     */
    public void setDataType(String dataType) {
        this.dataType = dataType == null ? null : dataType.trim();
    }

    /**
     * @return REF_VALUE
     */
    public String getRefValue() {
        return refValue;
    }

    /**
     * @param refValue
     */
    public void setRefValue(String refValue) {
        this.refValue = refValue == null ? null : refValue.trim();
    }

    /**
     * @return REVIEW_VALUE
     */
    public String getReviewValue() {
        return reviewValue;
    }

    /**
     * @param reviewValue
     */
    public void setReviewValue(String reviewValue) {
        this.reviewValue = reviewValue == null ? null : reviewValue.trim();
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
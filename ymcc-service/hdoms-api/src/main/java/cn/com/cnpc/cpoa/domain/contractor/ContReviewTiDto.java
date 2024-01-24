package cn.com.cnpc.cpoa.domain.contractor;

import cn.com.cnpc.cpoa.common.annotation.Excel;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "T_CONT_REVIEW_TI")
public class ContReviewTiDto implements Serializable {
    @Id
    @Column(name = "REVIEW_TI_ID")
    private String reviewTiId;

    /**
     * Basic  基本条件评价
            Achie  业绩评价
     */
    @Excel(name = "分类")
    @Column(name = "REVIEW_TYPE")
    private String reviewType;

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
    @Excel(name = "细类")
    @Column(name = "REVIEW_SUB_TYPE")
    private String reviewSubType;

    @Excel(name = "序号")
    @Column(name = "REVIEW_NO")
    private String reviewNo;

    @Excel(name = "评价项目/内容")
    @Column(name = "REVIEW_CTX")
    private String reviewCtx;

    @Excel(name = "参考分值")
    @Column(name = "REF_VALUE")
    private String refValue;

    @Excel(name = "分值")
    @Column(name = "REVIEW_TI_VALUE")
    private Float reviewTiValue;

    /**
     * 是否，INT            0 否，1是
            分值，FLOAT      float
     */
    @Column(name = "DATA_TYPE")
    private String dataType;

    @Excel(name = "备注")
    @Column(name = "NOTES")
    private String notes;

    public Float getReviewTiValue() {
        return reviewTiValue;
    }

    public void setReviewTiValue(Float reviewTiValue) {
        this.reviewTiValue = reviewTiValue;
    }

    /**
     * @return REVIEW_TI_ID
     */
    public String getReviewTiId() {
        return reviewTiId;
    }

    /**
     * @param reviewTiId
     */
    public void setReviewTiId(String reviewTiId) {
        this.reviewTiId = reviewTiId == null ? null : reviewTiId.trim();
    }

    /**
     * 获取Basic  基本条件评价
            Achie  业绩评价
     *
     * @return REVIEW_TYPE - Basic  基本条件评价
            Achie  业绩评价
     */
    public String getReviewType() {
        return reviewType;
    }

    /**
     * 设置Basic  基本条件评价
            Achie  业绩评价
     *
     * @param reviewType Basic  基本条件评价
            Achie  业绩评价
     */
    public void setReviewType(String reviewType) {
        this.reviewType = reviewType == null ? null : reviewType.trim();
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
     * @return REVIEW_SUB_TYPE - 1.项目组织实施
            2.项目质量管理
            3.项目职业健康安全与环境管理
            4.项目成本管理
            5.项目进度管理
            6.项目分包管理
            7.信息资料管理
            8.诚信履约
            
     */
    public String getReviewSubType() {
        return reviewSubType;
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
     * @param reviewSubType 1.项目组织实施
            2.项目质量管理
            3.项目职业健康安全与环境管理
            4.项目成本管理
            5.项目进度管理
            6.项目分包管理
            7.信息资料管理
            8.诚信履约
            
     */
    public void setReviewSubType(String reviewSubType) {
        this.reviewSubType = reviewSubType == null ? null : reviewSubType.trim();
    }

    /**
     * @return REVIEW_NO
     */
    public String getReviewNo() {
        return reviewNo;
    }

    /**
     * @param reviewNo
     */
    public void setReviewNo(String reviewNo) {
        this.reviewNo = reviewNo == null ? null : reviewNo.trim();
    }

    /**
     * @return REVIEW_CTX
     */
    public String getReviewCtx() {
        return reviewCtx;
    }

    /**
     * @param reviewCtx
     */
    public void setReviewCtx(String reviewCtx) {
        this.reviewCtx = reviewCtx == null ? null : reviewCtx.trim();
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
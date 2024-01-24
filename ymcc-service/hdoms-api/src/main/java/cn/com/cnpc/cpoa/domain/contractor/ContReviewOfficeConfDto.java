package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.*;

@Table(name = "T_CONT_REVIEW_OFFICE_CONF")
public class ContReviewOfficeConfDto {
    @Id
    @Column(name = "OFFICE_CONF_ID")
    private String officeConfId;

    @Column(name = "REVIEW_NO")
    private Integer reviewNo;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "PROPORTION")
    private Float proportion;

    @Column(name = "NOTES")
    private String notes;

    /**
     * @return OFFICE_CONF_ID
     */
    public String getOfficeConfId() {
        return officeConfId;
    }

    /**
     * @param officeConfId
     */
    public void setOfficeConfId(String officeConfId) {
        this.officeConfId = officeConfId == null ? null : officeConfId.trim();
    }

    /**
     * @return REVIEW_NO
     */
    public Integer getReviewNo() {
        return reviewNo;
    }

    /**
     * @param reviewNo
     */
    public void setReviewNo(Integer reviewNo) {
        this.reviewNo = reviewNo;
    }

    /**
     * @return DEPT_ID
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param deptId
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId == null ? null : deptId.trim();
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
package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.*;

@Table(name = "T_CONT_REVIEW_TASK_ATTACH")
public class ContReviewTaskAttachDto {
    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "ATTACH_ID")
    private String attachId;

    @Column(name = "REVIEW_TASK_ID")
    private String reviewTaskId;

    /**
     * @return ID
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * @return ATTACH_ID
     */
    public String getAttachId() {
        return attachId;
    }

    /**
     * @param attachId
     */
    public void setAttachId(String attachId) {
        this.attachId = attachId == null ? null : attachId.trim();
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
}
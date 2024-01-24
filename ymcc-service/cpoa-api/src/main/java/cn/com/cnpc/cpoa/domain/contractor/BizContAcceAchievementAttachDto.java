package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_CONT_ACCE_ACHIEVEMENT_ATTACH")
public class BizContAcceAchievementAttachDto {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "ACH_ID")
    private String achId;

    @Column(name = "ATTACH_ID")
    private String attachId;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "UPDATE_TIME")
    private Date updateTime;

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
     * @return ACH_ID
     */
    public String getAchId() {
        return achId;
    }

    /**
     * @param achId
     */
    public void setAchId(String achId) {
        this.achId = achId == null ? null : achId.trim();
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
     * @return CREATE_TIME
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return UPDATE_TIME
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
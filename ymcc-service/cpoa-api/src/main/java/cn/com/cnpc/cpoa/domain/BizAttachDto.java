package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/5 11:12
 * @Description: 附件实体
 */
@Table(name = "T_BIZ_ATTACH")
public class BizAttachDto {

    @Id
    @Column(name = "ATTACH_ID")
    private String attachId;

    @Column(name = "OWNER_ID")
    private String ownerId;

    @Column(name = "OWNER_TYPE")
    private String ownerType;


    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_TYPE")
    private String fileType;

    @Column(name = "FILE_URI")
    private String fileUri;

    @Column(name = "FILE_SIZE")
    private Double fileSize;

    @Column(name = "CREATE_TIME")
    private Date createTime;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private String userName;

    @Transient
    private String dealFileType;

    @Transient
    private String relativeUrl;

    public String getRelativeUrl() {
        return relativeUrl;
    }

    public void setRelativeUrl(String relativeUrl) {
        this.relativeUrl = relativeUrl;
    }

    public String getDealFileType() {
        return dealFileType;
    }

    public void setDealFileType(String dealFileType) {
        this.dealFileType = dealFileType;
    }

    @Transient
    private String fileSizeStr;

    @Transient
    private String setId;

    @Transient
    private String attachState;

    public String getAttachState() {
        return attachState;
    }

    public void setAttachState(String attachState) {
        this.attachState = attachState;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }


    public String getFileSizeStr() {
        return fileSizeStr;
    }

    public void setFileSizeStr(String fileSizeStr) {
        this.fileSizeStr = fileSizeStr;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUri() {
        return fileUri;
    }

    public void setFileUri(String fileUri) {
        this.fileUri = fileUri;
    }

    public Double getFileSize() {
        return fileSize;
    }

    public void setFileSize(Double fileSize) {
        this.fileSize = fileSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }
}

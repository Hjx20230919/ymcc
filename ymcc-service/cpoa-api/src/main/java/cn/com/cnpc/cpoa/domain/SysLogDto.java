package cn.com.cnpc.cpoa.domain;

import java.util.Date;
import javax.persistence.*;

@Table(name = "T_SYS_LOG")
public class SysLogDto {
    @Column(name = "LOG_ID")
    private String logId;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "LOG_TIME")
    private Date logTime;

    @Column(name = "LOG_MODULE")
    private String logModule;

    /**
     * 操作对象的ID
     */
    @Column(name = "LOG_OBJECT")
    private String logObject;

    @Column(name = "LOG_TYPE")
    private String logType;

    @Column(name = "LOG_CONTENT")
    private String logContent;

    @Transient
    private String userName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return LOG_ID
     */
    public String getLogId() {
        return logId;
    }

    /**
     * @param logId
     */
    public void setLogId(String logId) {
        this.logId = logId == null ? null : logId.trim();
    }

    /**
     * @return USER_ID
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * @return LOG_TIME
     */
    public Date getLogTime() {
        return logTime;
    }

    /**
     * @param logTime
     */
    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    /**
     * @return LOG_MODULE
     */
    public String getLogModule() {
        return logModule;
    }

    /**
     * @param logModule
     */
    public void setLogModule(String logModule) {
        this.logModule = logModule == null ? null : logModule.trim();
    }

    /**
     * 获取操作对象的ID
     *
     * @return LOG_OBJECT - 操作对象的ID
     */
    public String getLogObject() {
        return logObject;
    }

    /**
     * 设置操作对象的ID
     *
     * @param logObject 操作对象的ID
     */
    public void setLogObject(String logObject) {
        this.logObject = logObject == null ? null : logObject.trim();
    }

    /**
     * @return LOG_TYPE
     */
    public String getLogType() {
        return logType;
    }

    /**
     * @param logType
     */
    public void setLogType(String logType) {
        this.logType = logType == null ? null : logType.trim();
    }

    /**
     * @return LOG_CONTENT
     */
    public String getLogContent() {
        return logContent;
    }

    /**
     * @param logContent
     */
    public void setLogContent(String logContent) {
        this.logContent = logContent == null ? null : logContent.trim();
    }
}
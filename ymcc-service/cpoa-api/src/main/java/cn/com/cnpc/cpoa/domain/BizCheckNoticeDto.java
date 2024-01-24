package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/5/25 10:36
 * @Description:审批通知表
 */
@Table(name = "T_BIZ_CHECK_NOTICE")
public class BizCheckNoticeDto {

    /**
     *通知标识
     */
    @Id
    @Column(name = "NOTICE_ID")
    private String noticeId;

    /**
     *通知主体标识
     */
    @Column(name = "NOTICE_OBJ_ID")
    private String noticeObjId;

    /**
     *通知主体类型
     */
    @Column(name = "NOTICE_OBJ_TYPE")
    private String noticeObjType;

    /**
     *通知类型
     */
    @Column(name = "NOTICE_TYPE")
    private String noticeType;

    /**
     *通知用户
     */
    @Column(name = "NOTICE_USER_ID")
    private String noticeUserId;

    /**
     *通知创建时间
     */
    @Column(name = "NOTICE_CREATE_AT")
    private Date noticeCreateAt;

    /**
     *通知发送状态
     */
    @Column(name = "NOTICE_SEND_STATE")
    private String noticeSendState;

    /**
     *通知发送模板
     */
    @Column(name = "NOTICE_SEND_TEMP")
    private String noticeSendTemp;

    /**
     *知发送内容
     */
    @Column(name = "NOTICE_SEND_CONTENT")
    private String noticeSendContent;

    /**
     *通知发送时间
     */
    @Column(name = "NOTICE_SEND_TIME")
    private Date noticeSendTime;

    /**
     *通知发送重试次数
     */
    @Column(name = "NOTICE_SEND_RETRY")
    private Integer noticeSendRetry;



    /**
     *备注
     */
    @Column(name = "NOTICE_NOTE")
    private String noticeNote;

    @Transient
    private String stepId;
    @Transient
    private String manId;

    public String getStepId() {
        return stepId;
    }

    public void setStepId(String stepId) {
        this.stepId = stepId;
    }

    public String getManId() {
        return manId;
    }

    public void setManId(String manId) {
        this.manId = manId;
    }

    public String getNoticeId() {
        return noticeId;
    }

    public void setNoticeId(String noticeId) {
        this.noticeId = noticeId;
    }

    public String getNoticeObjId() {
        return noticeObjId;
    }

    public void setNoticeObjId(String noticeObjId) {
        this.noticeObjId = noticeObjId;
    }

    public String getNoticeObjType() {
        return noticeObjType;
    }

    public void setNoticeObjType(String noticeObjType) {
        this.noticeObjType = noticeObjType;
    }

    public String getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(String noticeType) {
        this.noticeType = noticeType;
    }

    public String getNoticeUserId() {
        return noticeUserId;
    }

    public void setNoticeUserId(String noticeUserId) {
        this.noticeUserId = noticeUserId;
    }

    public Date getNoticeCreateAt() {
        return noticeCreateAt;
    }

    public void setNoticeCreateAt(Date noticeCreateAt) {
        this.noticeCreateAt = noticeCreateAt;
    }

    public String getNoticeSendState() {
        return noticeSendState;
    }

    public void setNoticeSendState(String noticeSendState) {
        this.noticeSendState = noticeSendState;
    }

    public String getNoticeSendTemp() {
        return noticeSendTemp;
    }

    public void setNoticeSendTemp(String noticeSendTemp) {
        this.noticeSendTemp = noticeSendTemp;
    }

    public String getNoticeSendContent() {
        return noticeSendContent;
    }

    public void setNoticeSendContent(String noticeSendContent) {
        this.noticeSendContent = noticeSendContent;
    }

    public Date getNoticeSendTime() {
        return noticeSendTime;
    }

    public void setNoticeSendTime(Date noticeSendTime) {
        this.noticeSendTime = noticeSendTime;
    }

    public Integer getNoticeSendRetry() {
        return noticeSendRetry;
    }

    public void setNoticeSendRetry(Integer noticeSendRetry) {
        this.noticeSendRetry = noticeSendRetry;
    }

    public String getNoticeNote() {
        return noticeNote;
    }

    public void setNoticeNote(String noticeNote) {
        this.noticeNote = noticeNote;
    }
}

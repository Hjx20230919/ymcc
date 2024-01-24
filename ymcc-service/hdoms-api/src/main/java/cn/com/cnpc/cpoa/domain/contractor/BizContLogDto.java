package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:02
 * @Description:承包商记录
 */
@Table(name = "T_CONT_LOG")
public class BizContLogDto {

     /**
     * 记录标识
     */
    @Id
    @Column(name = "LOG_ID")
    private String logId;

    /**
     * 承包商标识
     */
    @Column(name = "CONT_ID")
    private String contId;

    /**
     * 记录对象(项目准入申请、冻结（冻结/解冻）、变更、考评（日常/年度）、年审、销项)
     */
    @Column(name = "LOG_OBJ")
    private String logObj;

    /**
     * 记录对象标识
     */
    @Column(name = "LOG_OBJ_ID")
    private String logObjId;

    /**
     * 记录说明
     */
    @Column(name = "LOG_DESC")
    private String logDesc;

    /**
     * 记录时间
     */
    @Column(name = "LOG_TIME")
    private Date logTime;

    /**
     * 记录人员
     */
    @Column(name = "LOG_USER")
    private String logUser;

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }

    public String getLogObj() {
        return logObj;
    }

    public void setLogObj(String logObj) {
        this.logObj = logObj;
    }

    public String getLogObjId() {
        return logObjId;
    }

    public void setLogObjId(String logObjId) {
        this.logObjId = logObjId;
    }

    public String getLogDesc() {
        return logDesc;
    }

    public void setLogDesc(String logDesc) {
        this.logDesc = logDesc;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getLogUser() {
        return logUser;
    }

    public void setLogUser(String logUser) {
        this.logUser = logUser;
    }
}

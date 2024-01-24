package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:07
 * @Description:承包商记录附件
 */
@Table(name = "T_CONT_LOG_ATTACH")
public class BizContLogAttachDto {


    /**
     * 记录附件标识
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 附件标识
     */
    @Column(name = "ATTACH_ID")
    private String attachId;

    /**
     * 记录标识
     */
    @Column(name = "LOG_ID")
    private String logId ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }
}

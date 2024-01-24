package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 10:02
 * @Description: 合同附件中间表
 */
@Table(name = "T_BIZ_DEAL_ATTACH")
public class BizDealAttachDto {

    /**
     * 合同附件标识
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 合同标识
     */
    @Column(name = "DEAL_ID")
    private String dealId;



    /**
     * 附件标识
     */
    @Column(name = "ATTACH_ID")
    private String attachId;

    /**
     * 附件类型
     */
    @Column(name = "DEAL_FILE_TYPE")
    private String dealFileType;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public String getAttachId() {
        return attachId;
    }

    public void setAttachId(String attachId) {
        this.attachId = attachId;
    }

    public String getDealFileType() {
        return dealFileType;
    }

    public void setDealFileType(String dealFileType) {
        this.dealFileType = dealFileType;
    }
}

package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 21:53
 * @Description: 合同子类(业务类型)
 */
@Table(name = "T_BIZ_SUBTYPE")
public class BizSubtypeDto {

    /**
     * 合同子类ID
     */
    @Id
    @Column(name = "SUBTYPE_ID")
    private String subtypeId;

    /**
     * 合同子类名称
     */
    @Column(name = "SUBTYPE_NAME")
    private String subtypeName;

    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;

    public String getSubtypeId() {
        return subtypeId;
    }

    public void setSubtypeId(String subtypeId) {
        this.subtypeId = subtypeId;
    }

    public String getSubtypeName() {
        return subtypeName;
    }

    public void setSubtypeName(String subtypeName) {
        this.subtypeName = subtypeName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

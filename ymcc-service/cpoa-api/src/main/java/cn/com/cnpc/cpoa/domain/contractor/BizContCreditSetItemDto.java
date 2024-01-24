package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/10/21 18:51
 * @Description:资质变更项
 */
@Table(name="T_CONT_CREDIT_SET_ITEM")
public class BizContCreditSetItemDto {


    /**
     * 变更标识
     */
    @Id
    @Column(name = "ITEM_ID")
    private String itemId;

    /**
     * 变更标识
     */
    @Column(name = "SET_ID")
    private String setId;

    /**
     * 变更标识
     */
    @Column(name = "CREDIT_ID")
    private String creditId;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getSetId() {
        return setId;
    }

    public void setSetId(String setId) {
        this.setId = setId;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }
}

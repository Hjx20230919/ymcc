package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/10/29 19:58
 * @Description:准入资质模板项
 */
@Table(name="T_CONT_CREDIT_TI")
public class BizContCreditTiDto {

    /**
     * 变更标识
     */
    @Id
    @Column(name = "ITEM_ID")
    private String itemId;

    /**
     * 模板项序号
     */
    @Column(name = "ITEM_NO")
    private Integer itemNo;


    /**
     * 资质项目名称
     */
    @Column(name = "ITEM_PROJ_NAME")
    private String itemProjName;


    /**
     * 是否必须
     */
    @Column(name = "ITEM_MUST")
    private String itemMust;


    /**
     * 模板标识
     */
    @Column(name = "TMPL_ID")
    private String tmplId;


    /**
     * 准入类型(正式准入:formal、临时准入:temp)
     */
    @Column(name = "ACCESS_TYPE")
    private String accessType;


    /**
     * 资质项目说明
     */
    @Column(name = "ITEM_PROJ_DESC")
    private String itemProjDesc;

    public String getItemProjDesc() {
        return itemProjDesc;
    }

    public void setItemProjDesc(String itemProjDesc) {
        this.itemProjDesc = itemProjDesc;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemProjName() {
        return itemProjName;
    }

    public void setItemProjName(String itemProjName) {
        this.itemProjName = itemProjName;
    }

    public String getItemMust() {
        return itemMust;
    }

    public void setItemMust(String itemMust) {
        this.itemMust = itemMust;
    }

    public String getTmplId() {
        return tmplId;
    }

    public void setTmplId(String tmplId) {
        this.tmplId = tmplId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }
}

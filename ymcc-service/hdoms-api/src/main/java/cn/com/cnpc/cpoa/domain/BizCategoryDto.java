package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 21:53
 * @Description: 合同子类(业务类型)
 */
@Table(name = "T_BIZ_CATEGORY")
public class BizCategoryDto {

    /**
     * 合同类别ID
     */
    @Id
    @Column(name = "CATEGORY_ID")
    private String categoryId;


    /**
     * 合同类别名称
     */
    @Column(name = "CATEGORY_NAME")
    private String categoryName;



    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;


    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}

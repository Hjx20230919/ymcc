package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import javax.persistence.Id;

/**
 * @Author: 17742856263
 * @Date: 2019/3/23 15:13
 * @Description:合同类别
 */
@Data
public class CategoryVo {

    /**
     * 合同类别ID
     */
    @Id
    private String categoryId;


    /**
     * 合同类别名称
     */
    private String categoryName;



    /**
     * 备注
     */
    private String notes;
}

package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * @Author: 17742856263
 * @Date: 2019/3/26 15:55
 * @Description:
 */
@Data
public class SubtypeVo {
    /**
     * 合同子类ID
     */
    private String subtypeId;

    /**
     * 合同子类名称
     */
    private String subtypeName;

    /**
     * 备注
     */
    private String notes;
}

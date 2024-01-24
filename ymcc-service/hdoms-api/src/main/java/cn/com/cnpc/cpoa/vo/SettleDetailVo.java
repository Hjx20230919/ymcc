package cn.com.cnpc.cpoa.vo;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/5/10 20:42
 * @Description:
 */
@Data
public class SettleDetailVo {

    /**
     * 项目标识
     */
    private String detailId;

    /**
     * 项目名称
     */
    private String detailName;


    /**
     * 规格型号
     */
    private String detailModel;

    /**
     * 单位
     */
    private String detailUnit;

    /**
     * 数量
     */
    private Integer detailTotal;

    /**
     * 单价（不含税）
     */
    private Double detailPrice;

    /**
     * 金额（价款）
     */
    private Double detailMoney;

    /**
     * 税率（%）
     */
    private Integer detailRate;

    /**
     * 税额
     */
    private Double detailTax;

    /**
     * 所属结算
     */
    private String settleId;

}

package cn.com.cnpc.cpoa.po;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/4/21 9:45
 * @Description:
 */
@Data
public class StatisticsOnePo {

    //类型
    private String dealType;
    //数量
    private Integer count;
    //合同总金额
    private Double dealValue;
    //累计已结算
    private Double dealSettle;
    //本年已结算
    private Double dealSettleNow;
    //未结算
    private Double receivables;
    //结算占比
    private Double revenueShare;



}

package cn.com.cnpc.cpoa.po;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/9/2 22:27
 * @Description:
 */
@Data
public class StatisticsFourPo {

    //年份
    @Excel(name = "年份")
    private String year;

    //合同数量
    @Excel(name = "合同数量")
    private Integer count;

    //合同金额
    @Excel(name = "合同金额")
    private Double dealValue;

    //累计结算金额
    @Excel(name = "累计结算金额")
    private Double dealSettle;

    //本年结算金额
    @Excel(name = "本年结算金额")
    private Double dealSettleNow;

    //未结算
    @Excel(name = "未结算")
    private Double receivables;

    //结算占比
    @Excel(name = "结算占比")
    private Double revenueShare;
}

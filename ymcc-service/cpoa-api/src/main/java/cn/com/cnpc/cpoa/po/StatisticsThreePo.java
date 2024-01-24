package cn.com.cnpc.cpoa.po;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/9/2 21:53
 * @Description:
 */
@Data
public class StatisticsThreePo {

    //年份
    @Excel(name = "签订年")
    private String year;
    //合同数量
    @Excel(name = "合同数量")
    private Integer count;
    //合同金额
    @Excel(name = "合同金额")
    private Double dealValue;
    //结算金额（本年）
    @Excel(name = "结算金额（签订年）")
    private Double settleNow;


}

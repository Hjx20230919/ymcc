package cn.com.cnpc.cpoa.po;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/9/2 22:54
 * @Description:
 */
@Data
public class StatisticsFivePo {

    //合同类型
    @Excel(name = "合同类型")
    private String dealType;
    //合同数量
    @Excel(name = "合同数量")
    private Integer count;
    //合同金额
    @Excel(name = "合同金额")
    private Double dealValue;
    //累计结算金额（包含跨年）
    @Excel(name = "累计结算金额（包含跨年）")
    private Double dealSettle;


}

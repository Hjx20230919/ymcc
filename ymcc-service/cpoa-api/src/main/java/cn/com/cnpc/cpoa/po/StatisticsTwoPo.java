package cn.com.cnpc.cpoa.po;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/4/21 9:45
 * @Description:
 */
@Data
public class StatisticsTwoPo {

//    //资金流向 INCOME   收入 OUTCOME  支出
//    private String dealIncome;

    //序号
    private int count;
    //单位名称
    private String deptName;

    //线上合同.数量
    private Integer xsCount;
    //线上合同.合同总额
    private Double xsValue;
    //线上合同.已收款总额
    private Double xsSettleNow;
    private Double xsSettle;
    private Double xsReceivables;
    private Double xsRevenueShare;

    //内责书.数量
    private Integer nzCount;
    //内责书.合同总额
    private Double nzValue;
    private Double nzSettle;
    //内责书.已收款总额
    private Double nzSettleNow;
    private Double nzReceivables;
    private Double nzRevenueShare;

    //三万以下.数量
    private Integer thCount;
    //三万以下.合同总额
    private Double thValue;
    private Double thSettle;
    //三万以下.已收款总额
    private Double thSettleNow;

    private Double thReceivables;
    private Double thRevenueShare;

    //线下合同.数量
    private Integer xxCount;
    //线下合同.合同总额
    private Double xxValue;
    //线下合同.累计已结算
    private Double xxSettle;
    //线下合同.本年已结算
    private Double xxSettleNow;
    //线下合同.未结算
    private Double xxReceivables;
    //线下合同.结算占比
    private Double xxRevenueShare;

    //收入合同总计.数量
    private Integer dealCount;
    //收入合同总计.合同总额
    private Double dealValue;
    private Double dealSettle;
    //收入合同总计.已收款总额
    private Double  dealSettleNow;

    private Double receivables;
    private Double revenueShare;

    private String deptId;




}

package cn.com.cnpc.cpoa.vo.prodsys.report;

import lombok.Data;

/**
 * @author: sirjaime
 * @create: 2020-11-01 10:52
 */
@Data
public class RptBaseVO {

    // @Excel(name = "往期金额")
    private double lastAmount;

    // @Excel(name = "本期金额")
    private double currentAmount;

    // @Excel(name = "增减")
    private double growth;

    // @Excel(name = "增减比")
    private float growthRatio;

    /**
     * 内部序列号，inner serial用于前端展示排序。
     * <p>
     * 如市场区域报表，区域+单位为排序对象，每一个排序对象用2位表示<br>
     * 区域	单位	新签	同期	增减值	增减比<br>
     * 川渝地区 川庆钻探     ins=1010<br>
     * 川渝地区 西南油气田   ins=1011<br>
     * 川渝地区 小计          ins=1012<br>
     * </p>
     */
    private int ins;

    /**
     * 报表中标识一条记录的pk
     */
    private String key;

}

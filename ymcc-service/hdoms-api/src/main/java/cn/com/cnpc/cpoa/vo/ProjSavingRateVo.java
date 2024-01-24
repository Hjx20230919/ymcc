package cn.com.cnpc.cpoa.vo;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.naming.Name;


/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-06  11:04
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProjSavingRateVo {
    private String savingRateId;

    @Excel(name = "年度")
    private String calcYear;

    @Excel(name = "项目编号")
    private String projNo;

    @Excel(name = "PROJ_ID")
    private String projId;

    @Excel(name = "DEAL_ID")
    private String dealId;

    @Excel(name = "合同名称")
    private String dealName;

    @Excel(name = "合同编号")
    private String dealNo;

    @Excel(name = "项目单位")
    private String deptName;

    @Excel(name = "合同相对人")
    private String contName;

    @Excel(name = "采购方式")
    private String selContType;

    @Excel(name = "合同结算状态")
    private String dealStatus;

    @Excel(name = "合同签订起止日期")
    private String dealStartEnd;

    @Excel(name = "资金节约率")
    private Float savingRate;

    @Excel(name = "税率")
    private Integer taxRate;

    @Excel(name = "合同金额")
    private Double dealValueInRate;

    @Excel(name = "不含税合同金额")
    private Double dealValueExRate;

    @Excel(name = "结算金额1")
    private Double dealSettleM1;

    @Excel(name = "节约金额1")
    private Double saveMoney1;

    @Excel(name = "结算金额2")
    private Double dealSettleM2;

    @Excel(name = "节约金额2")
    private Double saveMoney2;

    @Excel(name = "结算金额3")
    private Double dealSettleM3;

    @Excel(name = "节约金额3")
    private Double saveMoney3;

    @Excel(name = "结算金额4")
    private Double dealSettleM4;

    @Excel(name = "节约金额4")
    private Double saveMoney4;

    @Excel(name = "结算金额5")
    private Double dealSettleM5;

    @Excel(name = "节约金额5")
    private Double saveMoney5;

    @Excel(name = "结算金额6")
    private Double dealSettleM6;

    @Excel(name = "节约金额6")
    private Double saveMoney6;

    @Excel(name = "结算金额7")
    private Double dealSettleM7;

    @Excel(name = "节约金额7")
    private Double saveMoney7;

    @Excel(name = "结算金额8")
    private Double dealSettleM8;

    @Excel(name = "节约金额8")
    private Double saveMoney8;

    @Excel(name = "结算金额9")
    private Double dealSettleM9;

    @Excel(name = "节约金额9")
    private Double saveMoney9;

    @Excel(name = "结算金额10")
    private Double dealSettleM10;

    @Excel(name = "节约金额10")
    private Double saveMoney10;

    @Excel(name = "结算金额11")
    private Double dealSettleM11;

    @Excel(name = "节约金额11")
    private Double saveMoney11;

    @Excel(name = "结算金额12")
    private Double dealSettleM12;

    @Excel(name = "节约金额12")
    private Double saveMoney12;

    @Excel(name = "NOTES")
    private String notes;
}

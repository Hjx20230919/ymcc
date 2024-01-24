package cn.com.cnpc.cpoa.vo;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/5/9 20:42
 * @Description:流程中项目合同
 */
@Data
public class StatisticsDetailVo {

    @Excel(name = "合同名称")
    private String dealName;

    @Excel(name = "合同编码")
    private String dealNo;

    @Excel(name = "合同报审号")
    private String dealReportNo;

    @Excel(name = "合同签订时间")
    private Date dealSignTime;

    @Excel(name = "合同类型")
    private String dealType;

    @Excel(name = "合同状态")
    private String dealStatus;

    @Excel(name = "结算状态")
    private String settleStatus;

    @Excel(name = "资金流向")
    private String dealIncome;
    private String deptId;

    // private String settleAmount;
    private String dealId;

    @Excel(name = "承办单位")
    private String deptName;
    private String userId;

    @Excel(name = "承办人")
    private String userName;


    @Excel(name = "标的金额")
    private Double dealValue;

    @Excel(name = "往年结算")
    private Double settleHis ;

    @Excel(name = "本年结算")
    private Double settleNow;


    @Excel(name = "履行金额")
    private Double dealSettlement;

    @Excel(name = "履行期限(始)")
    private Date dealStart;

    @Excel(name = "履行期限(止)")
    private Date dealEnd;

    @Excel(name = "相对人")
    private String contName;

}

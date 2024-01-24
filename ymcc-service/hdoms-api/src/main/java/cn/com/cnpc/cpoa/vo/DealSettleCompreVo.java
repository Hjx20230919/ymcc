package cn.com.cnpc.cpoa.vo;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/9/8 9:59
 * @Description:
 */
@Data
public class DealSettleCompreVo {

    /**
     * 合同编号
     */
    @Excel(name = "合同编号")
    private String dealNo;

    private String dealId;

    /**
     * 合同名称
     */
    @Excel(name = "合同名称")
    private String dealName;

    /**
     * 合同名称
     */
    @Excel(name = "合同类型")
    private String dealType;


    @Excel(name = "结算状态")
    private String settleStatus;
    /**
     * 资金流向(
     * 'INCOME   收入
     OUTCOME  支出
     NONE  不涉及',
     * )
     */
    @Excel(name = "资金流向")
    private String dealIncome;


    /**
     * 承办部门
     */
    @Excel(name = "承办单位")
    private String deptName;

    private String deptId;


    /**
     * 承办人
     */
    @Excel(name = "承办人")
    private String userName;
    private String userId;

    @Excel(name = "结算金额")
    private Double settleAmount;

    @Excel(name = "上传时间")
    private Date createTime;


    @Excel(name = "结算时间")
    private Date downTime;


    /**
     * 结算标识
     */
    private String settleId;


    /**
     * 开户银行
     */
    private String settleBank;

    /**
     * 开户账号
     */
    private String settleAcount;

    /**
     * 应付款时间
     */
    private Date payableTime;

    /**
     * 财务实际付款金额
     */
    private Double actualPayAmount;

    /**
     * 付款日期
     */
    private Date actualPayTime;

    /**
     * 付款人
     */
    private String actualPayMan;

    /**
     * 财务付款说明
     */
    private String actualPayNotes;

    /**
     * 备注
     */
    private String notes;


    /**
     * 单位名称
     */
    private String contName2;

    /**
     * 纳税人识别号
     */
    private String orgNo;

    /**
     * 联系地址
     */
    private String contAddrss;

    /**
     * 联系电话
     */
    private String linkNum;


    private String stepNo;

    private List<String> userNames;

    private String contName;
}

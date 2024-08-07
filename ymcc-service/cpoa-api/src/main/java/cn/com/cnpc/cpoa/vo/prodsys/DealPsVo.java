package cn.com.cnpc.cpoa.vo.prodsys;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author: serJaime
 * @create: 2020-08-22 21:30
 */
@Data
public class DealPsVo {

    /**
     * 合同编号
     */
    // @Excel(name = "合同编号")
    private String dealNo;

    /**
     * 合同名称
     */
    @Excel(name = "合同名称")
    private String dealName;

    @Excel(name = "合同类型")
    private String dealType;

    @Excel(name = "合同状态")
    private String dealStatus;

    /**
     * 资金流向(
     * 'INCOME   收入
     * OUTCOME  支出
     * NONE  不涉及',
     * )
     */
    @Excel(name = "资金流向")
    private String dealIncome;

    //    /**
//     * 合同类别
//     */
//    @Excel(name = "合同类别")
    private String categoryName;

    private String categoryId;

    /**
     * 承办部门
     */
    @Excel(name = "承办部门")
    private String deptName;

    private String deptId;

    /**
     * 承办人
     */
    @Excel(name = "承办人")
    private String userName;
    private String userId;

    // @Excel(name = "结算状态")
    private String settleStatus;

    /**
     * 签订时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    // @Excel(name = "签订时间")
    private Date dealSignTime;

    private String contractId;

    /**
     * 资金渠道
     */
    // @Excel(name = "资金渠道")
    private String dealFunds;

    /**
     * 报审序号
     */
    // @Excel(name = "报审序号")
    private String dealReportNo;

    /**
     * 我方签约单位(
     * '固定：
     * 川庆钻探工程公司安全环保质量监督检测研究院
     * 四川科特检测技术有限公司',
     * )
     */
    // @Excel(name = "我方签约单位")
    private String dealContract;

    /**
     * 纠纷解决方式
     */
    // @Excel(name = "纠纷解决方式")
    private String dealDispute;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    /**
     * 标的金额
     */
    @Excel(name = "预估金额")
    private Double dealValue;

    /**
     * 履行期限(始)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预估履行期限(始)")
    private Date dealStart;

    /**
     * 履行期限(止)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "预估履行期限(止)")
    private Date dealEnd;

    /**
     * 相对人标识
     */
    @Excel(name = "相对人")
    private String contName;

    /**
     * 选商方式
     */
    // @Excel(name = "选商方式")
    private String dealSelection;

    /**
     * 履行金额
     */
    // @Excel(name = "履行金额")
    private Double dealSettlement;

    /**
     * 提交日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    // @Excel(name = "提交时间")
    private Date settleDate;

    /**
     * 合同标的
     */
    // @Excel(name = "合同标的")
    private String dealNotes;

    /**
     * 标的金额币种
     */
    // @Excel(name = "标的金额币种")
    private String dealCurrency;

    /**
     * 合同子类
     */
    private String subtypeId;

    /**
     * 付款性质
     */
    private String paymentType;

    /**
     * 合同约定的付款条件
     */
    private String paymentTeq;

    /**
     * 合同标的金额（变更后）
     */
    private Double dealValueAfter;

    /**
     * 合同标的金额（变更前）
     */
    private Double dealValueBefore;

    /**
     * 是否含税 是：1，否：0
     */
    private Integer haveTax;

    /**
     * 税率 1-99%
     */
    private Integer taxRate;

    private String projectNo;

    // 项目编号
    @Excel(name = "项目编号")
    private String contractNumber;
    private String bizProjectId;

    // 关联合同
    @Excel(name = "关联合同")
    private String joinedDealName;
    private String joinedDealId;
    private String changeDesc;

}

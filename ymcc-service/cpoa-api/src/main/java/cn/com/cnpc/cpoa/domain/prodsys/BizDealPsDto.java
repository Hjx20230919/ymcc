package cn.com.cnpc.cpoa.domain.prodsys;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Table(name = "T_BIZ_DEAL_PS")
public class BizDealPsDto {

    /**
     * 合同标识
     */
    @Id
    @Column(name = "DEAL_ID")
    private String dealId;

    /**
     * 合同编号
     */
    @Column(name = "DEAL_NO")
    private String dealNo;

    @Column(name = "PROJECT_NO")
    private String projectNo;
    /**
     * 合同名称
     */
    @Column(name = "DEAL_NAME")
    private String dealName;

    /**
     * 标的金额
     */
    @Column(name = "DEAL_VALUE")
    private Double dealValue;

    /**
     * 合同类别
     */
    @Column(name = "CATEGORY_ID")
    private String categoryId;

    /**
     * 签到时间
     */
    @Column(name = "DEAL_SIGN_TIME")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date dealSignTime;

    /**
     * 承办部门
     */
    @Column(name = "DEPT_ID")
    private String deptId;

    /**
     * 相对人标识
     */
    @Column(name = "CONTRACT_ID")
    private String contractId;

    /**
     * 资金流向(
     * 'INCOME   收入
     * OUTCOME  支出
     * NONE  不涉及',
     * )
     */
    @Column(name = "DEAL_INCOME")
    private String dealIncome;

    /**
     * 资金渠道
     */
    @Column(name = "DEAL_FUNDS")
    private String dealFunds;

    /**
     * 报审序号
     */
    @Column(name = "DEAL_REPORT_NO")
    private String dealReportNo;

    /**
     * 我方签约单位(
     * '固定：
     * 川庆钻探工程公司安全环保质量监督检测研究院
     * 四川科特检测技术有限公司',
     * )
     */
    @Column(name = "DEAL_CONTRACT")
    private String dealContract;

    /**
     * 纠纷解决方式
     */
    @Column(name = "DEAL_DISPUTE")
    private String dealDispute;

    /**
     * 承办人
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     * 履行期限(始)
     */
    @Column(name = "DEAL_START")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date dealStart;

    /**
     * 履行期限(止)
     */
    @Column(name = "DEAL_END")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date dealEnd;

    /**
     * 选商方式
     */
    @Column(name = "DEAL_SELECTION")
    private String dealSelection;

    /**
     * 履行金额
     */
    @Column(name = "DEAL_SETTLEMENT")
    private Double dealSettlement;

    /**
     * 提交日期
     */
    @Column(name = "SETTLE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date settleDate;

    /**
     * 合同标的
     */
    @Column(name = "DEAL_NOTES")
    private String dealNotes;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_AT")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date createAt;

    /**
     * 标的金额币种
     */
    @Column(name = "DEAL_CURRENCY")
    private String dealCurrency;

    /**
     * 合同子类
     */
    @Column(name = "SUBTYPE_ID")
    private String subtypeId;

    /**
     * 合同当前状态
     */
    @Column(name = "DEAL_STATUS")
    private String dealStatus;

    /**
     * 合同类型
     */
    @Column(name = "DEAL_TYPE")
    private String dealType;

    /**
     * 付款性质
     */
    @Column(name = "PAYMENT_TYPE")
    private String paymentType;

    /**
     * 合同约定的付款条件
     */
    @Column(name = "PAYMENT_REQ")
    private String paymentTeq;

    /**
     * 合同标的金额（变更后）
     */
    @Column(name = "DEAL_VALUE_AFTER")
    private Double dealValueAfter;

    /**
     * 合同标的金额（变更前）
     */
    @Column(name = "DEAL_VALUE_BEFORE")
    private Double dealValueBefore;

    /**
     * 是否含税 是：1，否：0
     */
    @Column(name = "HAVE_TAX")
    private Integer haveTax;

    /**
     * 税率 1-99%
     */
    @Column(name = "TAX_RATE")
    private Integer taxRate;

    //部门名称
    @Transient
    private String deptName;

    //合同类别名称
    @Transient
    private String categoryName;

    //合同子类名称
    @Transient
    private String subtypeName;

    //相对人名称
    @Transient
    private String contName;

    //用户名称
    @Transient
    private String userName;

    //用户名称
    @Transient
    private String stepNo;

    //当前审核人
    @Transient
    private List<String> userNames;


    //不为空则是正在审核
    @Transient
    private String flag;

    //相对人负责人
    @Transient
    private String dutyMan;

    //开户银行
    @Transient
    private String contBank;

    //开户账号
    @Transient
    private String contAccount;

    //当前结算状态
    @Transient
    private String settleStatus;

    @Transient
    private String outTime;

    @Transient
    private Double settleNow;

    @Transient
    private Double settleHis;

    @Transient
    private String projId;

    @Transient
    private String selContId;

    @Transient
    private String planId;

    @Transient
    private String resultId;

    // 项目编号
    @Transient
    private String contractNumber;
    @Transient
    private String bizProjectId;

    // 关联合同
    @Transient
    private String joinedDealName;
    @Transient
    private String joinedDealId;

}

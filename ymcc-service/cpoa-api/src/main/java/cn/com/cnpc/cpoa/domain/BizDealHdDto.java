package cn.com.cnpc.cpoa.domain;


import javax.persistence.*;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * TBizDealHd
 * @Author liuql
 * @Date 2023-07-13
 */

@Data
@Table(name = "T_BIZ_DEAL_HD")
public class BizDealHdDto {
	@Id
 	@Column(name = "DEAL_ID", length = 32 )
	private String dealId;

	@Column(name = "DEAL_BIZ")
	private String dealbiz;

 	@Column(name = "DEAL_NO", length = 100 )
	private String dealNo;

 	@Column(name = "PROJECT_NO", length = 100 )
	private String projectNo;

 	@Column(name = "DEAL_NAME", length = 255 )
	private String dealName;

 	@Column(name = "DEAL_VALUE")
	private Double dealValue;

 	@Column(name = "CATEGORY_ID", length = 32 )
	private String categoryId;

 	@Column(name = "DEAL_SIGN_TIME" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dealSignTime;

 	@Column(name = "DEPT_ID", length = 32 )
	private String deptId;

 	@Column(name = "CONTRACT_ID", length = 32 )
	private String contractId;

	/**
	 * INCOME   收入
            OUTCOME  支出
            NONE  不涉及
	 */
 	@Column(name = "DEAL_INCOME", length = 20 )
	private String dealIncome;

 	@Column(name = "DEAL_FUNDS", length = 100 )
	private String dealFunds;

 	@Column(name = "DEAL_REPORT_NO", length = 100 )
	private String dealReportNo;

	/**
	 * 固定：
            川庆钻探工程公司安全环保质量监督检测研究院
            四川科特检测技术有限公司
	 */
 	@Column(name = "DEAL_CONTRACT", length = 100 )
	private String dealContract;

 	@Column(name = "DEAL_DISPUTE", length = 100 )
	private String dealDispute;

 	@Column(name = "USER_ID", length = 32 )
	private String userId;

 	@Column(name = "DEAL_START" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dealStart;

 	@Column(name = "DEAL_END" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dealEnd;

 	@Column(name = "DEAL_SELECTION", length = 100 )
	private String dealSelection;

 	@Column(name = "DEAL_SETTLEMENT")
	private Double dealSettlement;

 	@Column(name = "SETTLE_DATE" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date settleDate;

 	@Column(name = "DEAL_NOTES", length = 500 )
	private String dealNotes;

 	@Column(name = "CREATE_AT" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createAt;

 	@Column(name = "DEAL_CURRENCY", length = 20 )
	private String dealCurrency;

	/**
	 * 业务类型
	 */
 	@Column(name = "SUBTYPE_ID", length = 32 )
	private String subtypeId;

	/**
	 * 以下状态：

            立项：【草稿  、退回、 立项审核中】
            履行：【履行中】
            变更：【变更审核中】
            归档：【归档中、归档完毕】
	 */
 	@Column(name = "DEAL_STATUS", length = 20 )
	private String dealStatus;

	/**
	 * 集团合同    TJ
            内责书        NZ
            3万以下      TH
            线下合同     XX
	 */
 	@Column(name = "DEAL_TYPE", length = 100 )
	private String dealType;

 	@Column(name = "PAYMENT_TYPE", length = 100 )
	private String paymentType;

 	@Column(name = "PAYMENT_REQ", length = 100 )
	private String paymentReq;

 	@Column(name = "DEAL_VALUE_AFTER")
	private Double dealValueAfter;

 	@Column(name = "DEAL_VALUE_BEFORE")
	private Double dealValueBefore;

 	@Column(name = "HAVE_TAX", length = 11 )
	private Integer haveTax;

 	@Column(name = "TAX_RATE", length = 11 )
	private Integer taxRate;

 	@Column(name = "CHANGE_DESC" )
	private String changeDesc;

 	@Column(name = "PLACED_TIME" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.TIMESTAMP)
	private Date placedTime;

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

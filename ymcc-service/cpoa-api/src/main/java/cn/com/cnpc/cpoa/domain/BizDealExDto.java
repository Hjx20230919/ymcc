package cn.com.cnpc.cpoa.domain;

import javax.persistence.*;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * TBizDealEx
 * @Author liuql
 * @Date 2023-07-12
 */


@Data
@Table(name = "T_BIZ_DEAL_EX")
public class BizDealExDto {
	@Id
 	@Column(name = "DEAL_ID")
	private String dealId;

//	@Column(name = "DEAL_BIZ")
//	private String dealbiz;

 	@Column(name = "DEAL_NO")
	private String dealNo;

 	@Column(name = "PROJECT_NO")
	private String projectNo;

 	@Column(name = "DEAL_NAME" )
	private String dealName;

 	@Column(name = "DEAL_VALUE" )
	private Double dealValue;

 	@Column(name = "CATEGORY_ID", length = 32 )
	private String categoryId;

 	@Column(name = "DEAL_SIGN_TIME" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.DATE)
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
	@Temporal(TemporalType.DATE)
	private Date dealStart;

 	@Column(name = "DEAL_END" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.DATE)
	private Date dealEnd;

 	@Column(name = "DEAL_SELECTION", length = 100 )
	private String dealSelection;

 	@Column(name = "DEAL_SETTLEMENT")
	private Double dealSettlement;

 	@Column(name = "SETTLE_DATE" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.DATE)
	private Date settleDate;

 	@Column(name = "DEAL_NOTES", length = 500 )
	private String dealNotes;

 	@Column(name = "CREATE_AT" )
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
	@Temporal(TemporalType.DATE)
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

 	@Column(name = "PAYMENT_TYPE" )
	private String paymentType;

 	@Column(name = "PAYMENT_REQ" )
	private String paymentReq;

 	@Column(name = "DEAL_VALUE_AFTER")
	private Double dealValueAfter;

 	@Column(name = "DEAL_VALUE_BEFORE" )
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
	@Temporal(TemporalType.DATE)
	private Date placedTime;

}

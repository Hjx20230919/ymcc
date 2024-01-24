package cn.com.cnpc.cpoa.domain;


import javax.persistence.*;
import java.io.Serializable;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * TBizDealZlhb
 * @Author liuql
 * @Date 2023-07-25
 */


@Data
@Table(name = "T_BIZ_DEAL_ZLHb")
public class BizDealZlhbDto  {

	private static final long serialVersionUID = 1L;
	@Id
 	@Column(name = "DEAL_ID", length = 100 )
	private String dealId;

 	@Column(name = "deal_type", length = 100 )
	private String dealType;

 	@Column(name = "DEAL_NO", length = 100 )
	private String dealNo;

 	@Column(name = "DEAL_NAME", length = 100 )
	private String dealName;

 	@Column(name = "DEAL_VALUE")
	private Double dealValue;

 	@Column(name = "DEAL_SIGN_TIME" )
	private Date dealSignTime;

 	@Column(name = "DEPT_ID", length = 100 )
	private String deptId;

 	@Column(name = "CONTRACT_ID", length = 100 )
	private String contractId;

 	@Column(name = "MARKET_TYPE", length = 100 )
	private String marketType;

 	@Column(name = "COMPANY_TYPE", length = 100 )
	private String companyType;

 	@Column(name = "CONTRACT_TYPE", length = 100 )
	private String contractType;

 	@Column(name = "WORK_ZONE", length = 100 )
	private String workZone;

 	@Column(name = "parent_name", length = 100 )
	private String parentName;

	/**
	 * 0<为空>\1检验检测\2QHSE\3服务保障
	 */
 	@Column(name = "deal_biz", length = 100 )
	private String dealBiz;

	@Transient
	private List<String> dealBizList;

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

	//29	SPARE1	保留字段1	NVARCHAR2(200)	200				保留字段1
	@Transient
	private String spare1;

	//30	SPARE2	保留字段2	NVARCHAR2(1024)	1024				保留字段2
	@Transient
	private String spare2;

	// 客户名称
	@Excel(name = "客户")
	@Transient
	private String clientName;

	// 申请名称
	@Transient
	private String applyName;

	// default deal.deptName
	@Excel(name = "承办单位")
	@Transient
	private String referUnitName;

	// name of responDepart
	@Excel(name = "负责科室")
	@Transient
	private String subDeptName;

//	@Excel(name = "关联合同")
//	@Transient
//	private String dealName;

	@Excel(name = "创建人")
	@Transient
	private String createUserName;

	// 项目编号
//	@Transient
//	private String contractNumber;
	@Transient
	private String bizProjectId;

	// 关联合同
	@Transient
	private String joinedDealName;
	@Transient
	private String joinedDealId;

}

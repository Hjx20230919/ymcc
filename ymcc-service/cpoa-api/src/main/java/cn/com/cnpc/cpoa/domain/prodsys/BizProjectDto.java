package cn.com.cnpc.cpoa.domain.prodsys;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * <项目信息表>
 *
 * @author anonymous
 * @create 11/02/2020 22:24
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_PROJECT")
public class BizProjectDto {
    //    1	CONTRACT_ID	项目ID	NVARCHAR2(120)	120		√	√	项目ID
    @Id
    @Column(name = "CONTRACT_ID")
    private String contractId;

    //2	CLIENT_ID	客户ID	NVARCHAR2(120)	120				客户ID
    @Column(name = "CLIENT_ID")
    private String clientId;

    //3	APPLY_ID	申请ID	NVARCHAR2(120)	120
    @Column(name = "APPLY_ID")
    private String applyId;

    //            4	CONTRACT_NAME	项目名称	NVARCHAR2(50)	50				项目名称
    @Excel(name = "项目名称")
    @Column(name = "CONTRACT_NAME")
    private String contractName;

    //5	REFER_UNIT	涉及单位	NVARCHAR2(50)	50				涉及单位
    // default deal.deptId 申请单位，更名为施工单位
    @Column(name = "REFER_UNIT")
    private String referUnit;

    //6	MARKET_TYPE	市场分类	NVARCHAR2(10)	10				市场分类：国内，国外
    @Excel(name = "市场分类")
    @Column(name = "MARKET_TYPE")
    private String marketType;

    //7	COMPANY_TYPE	公司集团	NVARCHAR2(10)	10				公司集团：川庆内部，集团内部，集团外部
    @Excel(name = "公司集团")
    @Column(name = "COMPANY_TYPE")
    private String companyType;

    //8	CONTRACT_TYPE	交易类型	NVARCHAR2(20)	20				交易类型：关联交易，非关联交易，内部责任书
    @Excel(name = "交易类型")
    @Column(name = "CONTRACT_TYPE")
    private String contractType;

    //9	WORK_ZONE	作业区域	NVARCHAR2(20)	20				作业区域：川渝地区、塔里木地区、其它地区、长庆地区、深圳、天津、上海、其它
    @Excel(name = "作业区域")
    @Column(name = "WORK_ZONE")
    private String workZone;

    //10	WORK_TYPE	项目类型	NVARCHAR2(20)	20				项目类型:常规，页岩气
    @Excel(name = "项目类型")
    @Column(name = "WORK_TYPE")
    private String workType;

    //11	CONTRACT_NUMBER	合同编号	NVARCHAR2(50)	50				合同编号
    @Excel(name = "合同编号")
    @Column(name = "CONTRACT_NUMBER")
    private String contractNumber;

    //12	CONTRACT_DESC	合同内容描述	NVARCHAR2(2000)	2000				合同内容描述
    @Column(name = "CONTRACT_DESC")
    private String contractDesc;

    //13	RESPON_DEPART	负责科室	NVARCHAR2(120)	120				负责科室
    // sub dept id
    @Column(name = "RESPON_DEPART")
    private String responDepart;

    //14	CONTRACT_BEGIN_DATE	项目开始时间	DATE					项目开始时间
    @Excel(name = "项目开始时间")
    @Column(name = "CONTRACT_BEGIN_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date contractBeginDate;

    //15	CONTRACT_PLAN_END_DATE	项目计划结束时间	DATE					项目计划结束时间
    @Excel(name = "项目计划结束时间")
    @Column(name = "CONTRACT_PLAN_END_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date contractPlanEndDate;

    //16	CONTRACT_END_DATE	项目结束时间	DATE					项目结束时间
    @Column(name = "CONTRACT_END_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @Temporal(TemporalType.DATE)
    private Date contractEndDate;

    //17	CONTRACT_PRICE	合同总金额	NUMBER(13,3)	13	3			合同总金额
    @Excel(name = "金额")
    @Column(name = "CONTRACT_PRICE")
    private Double contractPrice;

    //18	PAY_TYPE	合同结算方式	NVARCHAR2(20)	20				合同结算方式：整包合同，人天时，工作量
    @Excel(name = "结算方式")
    @Column(name = "PAY_TYPE")
    private String payType;

    //19	NOTIFY	注意事项	NVARCHAR2(2000)	2000				注意事项
    @Column(name = "NOTIFY")
    private String notify;

    //20	CONTRACT_STATE	项目状态	NVARCHAR2(32)	32
    @Excel(name = "状态")
    @Column(name = "CONTRACT_STATE")
    private String contractState;

    //            21	CREATE_USER	创建人	NVARCHAR2(120)	120				创建人
    @Column(name = "CREATE_USER")
    private String createUser;

    //22	CREATE_DATE	创建时间	DATE					创建时间
    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date createDate;

    //23	UPDATE_USER	修改者	NVARCHAR2(120)	120				修改者
    @Column(name = "UPDATE_USER")
    private String updateUser;

    //24	UPDATE_DATE	修改时间	DATE					修改时间
    @Column(name = "UPDATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date updateDate;

    //25	REMARK	备注	NVARCHAR2(1024)	1024				备注
    @Column(name = "REMARK")
    private String remark;

    //26	ROW_STATE	行状态	NVARCHAR2(32)	32				行状态
    @Column(name = "ROW_STATE")
    private String rowState;

    //27	sLOCKED_IF	是否锁定	NVARCHAR2(32)	32				是否锁定
    @Column(name = "LOCKED_IF")
    private Integer lockedIf;

    //28	AUDIT_IF	是否审核	NVARCHAR2(32)	32				是否审核
    @Column(name = "AUDIT_IF")
    private Integer auditIf;

    @Column(name = "DEAL_ID")
    private String dealId;

    // 同步状态 1-成功， 0-失败
    @Column(name = "SYNC_STATUS")
    private Integer syncStatus;

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

    @Excel(name = "关联合同")
    @Transient
    private String dealName;

    @Excel(name = "创建人")
    @Transient
    private String createUserName;

    // 合同签订单位
    @Column(name = "DEAL_CONTRACT_ID")
    private String dealContractId;

    // 合同签订单位名称
    @Excel(name = "合同签订单位")
    @Transient
    private String dealContractName;

    // 生产系统推送过来：项目状态
    @Column(name = "PRODSYS_STATUS")
    private String prodsysStatus;

    // 生产系统推送过来：项目执行天数
    @Column(name = "PRODSYS_EXEC_DAYS")
    private Float prodsysExecDays;

    // 生产系统推送过来：项目进度（总进度）
    @Column(name = "PRODSYS_PROGRESS")
    private Float prodsysProgress;

    // 生产系统推送过来：超期状态
    @Column(name = "PRODSYS_OVERDUE_STATUS")
    private String prodsysOverdueStatus;
}

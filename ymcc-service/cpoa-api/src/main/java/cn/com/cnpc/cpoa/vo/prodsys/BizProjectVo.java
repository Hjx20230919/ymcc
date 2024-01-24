package cn.com.cnpc.cpoa.vo.prodsys;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <>
 *
 * @author anonymous
 * @create 13/02/2020 23:21
 * @since 1.0.0
 */
@Data
public class BizProjectVo {
    private String contractId;

    //2	CLIENT_ID	客户ID	NVARCHAR2(120)	120				客户ID
    private String clientId;

    //3	APPLY_ID	申请ID	NVARCHAR2(120)	120
    private String applyId;

    //            4	CONTRACT_NAME	项目名称	NVARCHAR2(50)	50				项目名称
    private String contractName;

    //5	REFER_UNIT	涉及单位	NVARCHAR2(50)	50				涉及单位
    private String referUnit;

    //6	MARKET_TYPE	市场分类	NVARCHAR2(10)	10				市场分类：国内，国外
    private String marketType;

    //7	COMPANY_TYPE	公司集团	NVARCHAR2(10)	10				公司集团：川庆内部，集团内部，集团外部
    private String companyType;

    //8	CONTRACT_TYPE	交易类型	NVARCHAR2(20)	20				交易类型：关联交易，非关联交易，内部责任书
    private String contractType;

    //9	WORK_ZONE	作业区域	NVARCHAR2(20)	20				作业区域：川渝地区、塔里木地区、其它地区、长庆地区、深圳、天津、上海、其它
    private String workZone;

    //10	WORK_TYPE	项目类型	NVARCHAR2(20)	20				项目类型:常规，页岩气
    private String workType;

    //11	CONTRACT_NUMBER	合同编号	NVARCHAR2(50)	50				合同编号
    private String contractNumber;

    //12	CONTRACT_DESC	合同内容描述	NVARCHAR2(2000)	2000				合同内容描述
    private String contractDesc;

    //13	RESPON_DEPART	负责科室	NVARCHAR2(120)	120				负责科室
    // sub dept id
    private String responDepart;

    //14	CONTRACT_BEGIN_DATE	项目开始时间	DATE					项目开始时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date contractBeginDate;

    //15	CONTRACT_PLAN_END_DATE	项目计划结束时间	DATE					项目计划结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date contractPlanEndDate;

    //16	CONTRACT_END_DATE	项目结束时间	DATE					项目结束时间
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd")
    private Date contractEndDate;

    //17	CONTRACT_PRICE	合同总金额	NUMBER(13,3)	13	3			合同总金额
    private Double contractPrice;

    //18	PAY_TYPE	合同结算方式	NVARCHAR2(20)	20				合同结算方式：整包合同，人天时，工作量
    private String payType;

    //19	NOTIFY	注意事项	NVARCHAR2(2000)	2000				注意事项
    private String notify;

    //20	CONTRACT_STATE	项目状态	NVARCHAR2(32)	32
    private String contractState;

    //            21	CREATE_USER	创建人	NVARCHAR2(120)	120				创建人
    private String createUser;

    //22	CREATE_DATE	创建时间	DATE					创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    //23	UPDATE_USER	修改者	NVARCHAR2(120)	120				修改者
    private String updateUser;

    //24	UPDATE_DATE	修改时间	DATE					修改时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    //25	REMARK	备注	NVARCHAR2(1024)	1024				备注
    private String remark;

    //26	ROW_STATE	行状态	NVARCHAR2(32)	32				行状态
    private String rowState;

    //27	sLOCKED_IF	是否锁定	NVARCHAR2(32)	32				是否锁定
    private Integer lockedIf;

    //28	AUDIT_IF	是否审核	NVARCHAR2(32)	32				是否审核
    private Integer auditIf;

    private Integer syncStatus;

    //29	SPARE1	保留字段1	NVARCHAR2(200)	200				保留字段1
    private String spare1;

    //30	SPARE2	保留字段2	NVARCHAR2(1024)	1024				保留字段2
    private String spare2;

    // 客户名称
    private String clientName;

    // 申请名称
    private String applyName;

    private String dealId;

    private String referUnitName;

    private String subDeptName;

    private String dealName;

    private String dealContractId;

    private String dealContractName;
}

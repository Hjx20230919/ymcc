package cn.com.cnpc.cpoa.domain.prodsys;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * <项目业务关联表>
 *
 * @author anonymous
 * @create 11/02/2020 23:43
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_PROJECT_WORK")
public class BizProjectWorkDto {
    //    1	PW_LINK_ID	项目业务关联表	NVARCHAR2(120)	120		√	√	任务业务关联表
    @Id
    @Column(name = "PW_LINK_ID")
    private String pwLinkId;

    //2	CONTRACT_ID	项目ID	NVARCHAR2(120)	120				项目ID
    @Column(name = "CONTRACT_ID")
    private String contractId;

    //3	CLAS_ID	业务ID	NVARCHAR2(120)	120				业务ID
    @Column(name = "CLAS_ID")
    private String clasId;

    //4	CLAS_TYPE	业务型号	NVARCHAR2(50)	50				业务型号
    @Column(name = "CLAS_TYPE")
    private String clasType;

    //5	PLAN_WORK_NUM	计划工作量	NUMBER(10)	10				计划工作量
    @Column(name = "PLAN_WORK_NUM")
    private Float planWorkNum;

    //6	PLAN_DOC_NUM	计划报告数	NUMBER(10)	10				计划报告数
    @Column(name = "PLAN_DOC_NUM")
    private Integer planDocNum;

    //5	PLAN_WORK_NUM	初始化工作量	NUMBER(10)	10				初始化工作量
    @Column(name = "INIT_WORK_NUM")
    private Float initWorkNum;

    //6	PLAN_DOC_NUM	初始化报告数	NUMBER(10)	10				初始化报告数
    @Column(name = "INIT_DOC_NUM")
    private Integer initDocNum;

    //7	UNIT	单位	NVARCHAR2(20)	20				单位:台，人天，座，公里
    @Column(name = "UNIT")
    private String unit;

    //8	PRICE	结算金额	NUMBER(10,3)	10	3
    @Column(name = "PRICE")
    private Double price;

    //            9	CREATE_USER	创建人	NVARCHAR2(120)	120				创建人
    @Column(name = "CREATE_USER")
    private String createUser;

    //10	CREATE_DATE	创建时间	DATE					创建时间
    @Column(name = "CREATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date createDate;

    //11	UPDATE_USER	修改者	NVARCHAR2(120)	120				修改者
    @Column(name = "UPDATE_USER")
    private String updateUser;

    //12	UPDATE_DATE	修改时间	DATE					修改时间
    @Column(name = "UPDATE_DATE")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Temporal(TemporalType.DATE)
    private Date updateDate;

    //13	REMARK	备注	NVARCHAR2(1024)	1024				备注
    @Column(name = "REMARK")
    private String remark;

    //14	ROW_STATE	行状态	NVARCHAR2(32)	32				行状态
    @Column(name = "ROW_STATE")
    private String rowState;

    //15	sLOCKED_IF	是否锁定	NVARCHAR2(32)	32				是否锁定
    @Column(name = "LOCKED_IF")
    private Integer lockedIf;

    //16	AUDIT_IF	是否审核	NVARCHAR2(32)	32				是否审核
    @Column(name = "AUDIT_IF")
    private Integer auditIf;

    //计划工期
    @Column(name = "PLAN_WORK_TIME")
    private Float planWorkTime;

    @Transient
    private String workType1;

    @Transient
    private String workType2;

    @Transient
    private String servType;

    @Column(name = "FINISH_WORK_NUM")
    private Float finishWorkNum;
}

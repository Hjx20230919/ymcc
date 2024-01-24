/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * FileName: BizProjectWorkVo
 * Author:   wangjun
 * Date:     13/02/2020 23:22
 */
package cn.com.cnpc.cpoa.vo.prodsys;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <>
 *
 * @author anonymous
 * @create 13/02/2020 23:22
 * @since 1.0.0
 */
@Data
public class BizProjectWorkVo {
    //    1	PW_LINK_ID	项目业务关联表	NVARCHAR2(120)	120		√	√	任务业务关联表
    private String pwLinkId;

    //2	CONTRACT_ID	项目ID	NVARCHAR2(120)	120				项目ID
    private String contractId;

    //3	CLAS_ID	业务ID	NVARCHAR2(120)	120				业务ID
    private String clasId;

    //4	CLAS_TYPE	业务型号	NVARCHAR2(50)	50				业务型号
    private String clasType;

    //5	PLAN_WORK_NUM	计划工作量	NUMBER(10)	10				计划工作量
    private Integer planWorkNum;

    //6	PLAN_DOC_NUM	计划报告数	NUMBER(10)	10				计划报告数
    private Integer planDocNum;

    // 初始化工作量
    private Integer initWorkNum;

    // 初始化报告数
    private Integer initDocNum;

    //7	UNIT	单位	NVARCHAR2(20)	20				单位:台，人天，座，公里
    private String unit;

    //8	PRICE	结算金额	NUMBER(10,3)	10	3
    private Double price;

    //            9	CREATE_USER	创建人	NVARCHAR2(120)	120				创建人
    private String createUser;

    //10	CREATE_DATE	创建时间	DATE					创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    //11	UPDATE_USER	修改者	NVARCHAR2(120)	120				修改者
    private String updateUser;

    //12	UPDATE_DATE	修改时间	DATE					修改时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale = "zh", timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    //13	REMARK	备注	NVARCHAR2(1024)	1024				备注
    private String remark;

    //14	ROW_STATE	行状态	NVARCHAR2(32)	32				行状态
    private String rowState;

    //15	sLOCKED_IF	是否锁定	NVARCHAR2(32)	32				是否锁定
    private Integer lockedIf;

    //16	AUDIT_IF	是否审核	NVARCHAR2(32)	32				是否审核
    private Integer auditIf;
}

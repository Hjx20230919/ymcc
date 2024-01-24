package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <单位年度指标>
 *
 * @author anonymous
 * @create 19/04/2020 10:19
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_DEPT_TASK")
public class BizDeptTaskDto {
    //    ID	DEPT_TASK_ID	varchar(32)
//    年月	YEAR_MOMTH	varchar(30)
//    部门/单位	DEPT_ID	varchar(32)
//    市场分类	MARKET_TYPE	varchar(30)
//    公司集团	COMPANY_TYPE	varchar(30)
//    交易类型	CONTRACT_TYPE	varchar(30)
//    作业区域	WORK_ZONE	varchar(30)
//    项目类型	WORK_TYPE	varchar(30)
//    业务类型	BIZ_TYPE_ID	varchar(32)
//    金额	TASK_PRICE	decimal
//    备注	NOTES	varchar(200)
    @Id
    @Column(name = "DEPT_TASK_ID")
    private String deptTaskId;

    @Column(name = "YEAR_MOMTH")
    private String yearMonth;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "MARKET_TYPE")
    private String marketType;

    @Column(name = "COMPANY_TYPE")
    private String companyType;

    @Column(name = "CONTRACT_TYPE")
    private String contractType;

    @Column(name = "WORK_ZONE")
    private String workZone;

    @Column(name = "WORK_TYPE")
    private String workType;

    @Column(name = "BIZ_TYPE_ID")
    private String bizTypeId;

    @Column(name = "TASK_PRICE")
    private Double taskPrice;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private String deptName;

}

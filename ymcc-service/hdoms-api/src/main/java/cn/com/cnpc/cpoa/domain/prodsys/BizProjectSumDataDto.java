package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * <Project historical data>
 *
 * @author wangjun
 * @create 23/03/2020 08:42
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_PROJECT_SUMDATA")
public class BizProjectSumDataDto {
    @Id
    @Column(name = "PSD_ID")
    private String psdId;

    @Column(name = "YEAR_MONTH")
    private String yearMonth;

    @Column(name = "CLIENT_ID")
    private String clientId;

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

    @Column(name = "CONTRACT_PRICE")
    private Double contractPrice;

    @Column(name = "NOTES")
    private String notes;

    @Transient
    private String deptName;

    @Transient
    private String subDeptName;

    @Transient
    private String clientName;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Transient
    private String workType1;

    @Transient
    private String workType2;

    @Transient
    private String servType;
}

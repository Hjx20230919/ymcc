package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <>
 *
 * @author anonymous
 * @create 11/04/2020 23:53
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_WORK_TABLE")
public class BizWorkTableDto {
    @Id
    @Column(name = "CLAS_ID")
    private String clasId;

    @Column(name = "CLAS_TYPE")
    private String clasType;

    @Column(name = "WORK_TYPE1")
    private String workType1;

    @Column(name = "WORK_TYPE2")
    private String workType2;

    @Column(name = "DPT_ID")
    private String deptId;

    @Column(name = "UNIT_NAME")
    private String unitName;

    @Column(name = "DPT_NAME")
    private String dptName;

    @Column(name = "SERV_TYPE")
    private String servType;

    @Column(name = "UNIT")
    private String unit;

    @Column(name = "REMARK")
    private String remark;
}

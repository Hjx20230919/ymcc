package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <科室>
 *
 * @author anonymous
 * @create 28/02/2020 20:35
 * @since 1.0.0
 */
@Data
@Table(name = "T_SYS_DEPT_SUB")
public class SysSubDeptDto {

    @Id
    @Column(name = "SUB_DEPT_ID")
    private String subDeptId;

    @Column(name = "SUB_DEPT_NAME")
    private String subDeptName;

    @Column(name = "DEPT_ID")
    private String deptId;

    @Column(name = "DEPT_NAME")
    private String deptName;

    @Column(name = "REF_DEPT_ID")
    private String refDeptId;
}

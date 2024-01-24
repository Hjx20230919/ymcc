package cn.com.cnpc.cpoa.vo.prodsys.report;

import cn.com.cnpc.cpoa.utils.StringUtils;
import lombok.Data;

/**
 * @author: sirjaime
 * @create: 2020-11-01 10:03
 */
@Data
public class RptClientSubDeptVO extends RptClientDeptVO {
    // @Excel(name = "下级单位")
    private String subDeptName;

    // @Excel(name = "业务类型")
    private String clasType;

    @Override
    public String getKey() {
        String dept = StringUtils.isBlank(getDeptName()) ? "NA" : getDeptName();
        String subDept = StringUtils.isBlank(subDeptName) ? "NA" : subDeptName;
        String ct = StringUtils.isBlank(clasType) ? "NA" : clasType;
        return String.format("%s_%s_%s", dept, subDept, ct);
    }

    @Override
    public String toString() {
        return "RptClientSubDeptVO{" +
                "subDeptName='" + subDeptName + '\'' +
                ", clasType='" + clasType + '\'' +
                "} " + super.toString();
    }
}

package cn.com.cnpc.cpoa.vo.prodsys.report;

import cn.com.cnpc.cpoa.utils.StringUtils;
import lombok.Data;

/**
 * @author: sirjaime
 * @create: 2020-11-01 10:01
 */
@Data
public class RptContractTypeVO extends RptBaseVO {
    private String typeName1;
    private String typeName2;
    private String deptName;

    public String getKey() {
        String tn1 = StringUtils.isBlank(typeName1) ? "NA" : typeName1;
        String tn2 = StringUtils.isBlank(typeName2) ? "NA" : typeName2;
        String dn = StringUtils.isBlank(deptName) ? "NA" : deptName;
        return String.format("%s_%s_%s", tn1, tn2, dn);
    }

    @Override
    public String toString() {
        return "RptContractTypeVO{" +
                "typeName1='" + typeName1 + '\'' +
                ", typeName2='" + typeName2 + '\'' +
                ", deptName='" + deptName + '\'' +
                "} " + super.toString();
    }
}

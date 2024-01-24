package cn.com.cnpc.cpoa.vo.prodsys.report;

import lombok.Data;

/**
 * @author: sirjaime
 * @create: 2020-11-01 11:06
 */
@Data
public class RptClientDeptVO extends RptBaseVO {

    // @Excel(name = "单位名称")
    private String deptName;

    public String getKey() {
        return deptName;
    }

    @Override
    public String toString() {
        return "RptClientDeptVO{" +
                "deptName='" + deptName + '\'' +
                "} " + super.toString();
    }
}

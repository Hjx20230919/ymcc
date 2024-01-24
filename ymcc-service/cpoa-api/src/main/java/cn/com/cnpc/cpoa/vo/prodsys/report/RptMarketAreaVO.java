package cn.com.cnpc.cpoa.vo.prodsys.report;

import cn.com.cnpc.cpoa.utils.StringUtils;
import lombok.Data;

/**
 * 市场区域报表数据
 *
 * @author: sirjaime
 * @create: 2020-11-01 10:01
 */
@Data
public class RptMarketAreaVO extends RptBaseVO {

    // @Excel(name = "区域")
    private String workZone;

    private String deptName;

    /**
     * 构造唯一key
     *
     * @return
     */
    public String getKey() {
        String dept = StringUtils.isBlank(deptName) ? "NA" : deptName;
        return String.format("%s_%s", workZone, dept);
    }

    @Override
    public String toString() {
        return "RptMarketAreaVO{" +
                "workZone='" + workZone + '\'' +
                ", deptName='" + deptName + '\'' +
                "} " + super.toString();
    }

}

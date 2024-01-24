package cn.com.cnpc.cpoa.vo.prodsys.report;

import cn.com.cnpc.cpoa.utils.StringUtils;
import lombok.Data;

/**
 * @author: sirjaime
 * @create: 2020-11-01 10:00
 */
@Data
public class RptClasTypeVO extends RptMarketAreaVO {
    private String clasType;

    /**
     * 构造唯一key
     *
     * @return
     */
    public String getKey() {
        String workZone = StringUtils.isBlank(getWorkZone()) ? "NA" : getWorkZone();
        String deptName = StringUtils.isBlank(getDeptName()) ? "NA" : getDeptName();
        return String.format("%s_%s_%s", clasType, workZone, deptName);
    }

    @Override
    public String toString() {
        return "RptClasTypeVO{" +
                "clasType='" + clasType + '\'' +
                "} " + super.toString();
    }
}

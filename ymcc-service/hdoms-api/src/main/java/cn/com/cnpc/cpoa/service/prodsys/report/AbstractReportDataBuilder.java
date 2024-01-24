package cn.com.cnpc.cpoa.service.prodsys.report;

import cn.com.cnpc.cpoa.utils.BigDecimalUtils;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptBaseVO;

import java.util.List;

/**
 * @author: sirjaime
 * @create: 2020-11-01 14:29
 */
public abstract class AbstractReportDataBuilder<T> {

    public abstract List<T> merge(List<T> cur, List<T> last);

    protected void calc(RptBaseVO d) {
        Double growth = BigDecimalUtils.subtract(d.getCurrentAmount(), d.getLastAmount());
        d.setGrowth(growth);

        Float growthRatio = d.getLastAmount() == 0 || d.getCurrentAmount() == 0 ? 0 : BigDecimalUtils.divide(growth, d.getLastAmount()).floatValue();
        d.setGrowthRatio(growthRatio);
    }
}

package cn.com.cnpc.cpoa.service.prodsys.report;

import cn.com.cnpc.cpoa.utils.BigDecimalUtils;
import cn.com.cnpc.cpoa.vo.prodsys.report.RptBaseVO;

import java.util.List;

/**
 * @author: sirjaime
 * @create: 2020-11-01 10:21
 */
public interface FusionReportDataBuilder<T> {
    public List<T> build(List<T> cur, List<T> last);

    public static final Integer INS_MAX = 99;

    public static final String OTHER = "其他";

    public static final String NA = "NA";

    public static final String BLANK = "";

    public static final String TOTAL = "合计";

    public static final String SUB_TOTAL = "小计";

    public static final String AGGREGATE_TOTAL = "总计";

    default void calc(RptBaseVO d) {
        Double growth = BigDecimalUtils.subtract(d.getCurrentAmount(), d.getLastAmount());
        d.setGrowth(growth);

        Float growthRatio = d.getLastAmount() == 0 || d.getCurrentAmount() == 0 ? 0 : BigDecimalUtils.divide(growth, d.getLastAmount()).floatValue();
        d.setGrowthRatio(growthRatio);
    }

}

package cn.com.cnpc.cpoa.core.page;

import java.io.Serializable;
import java.util.List;

/**
 * 表格分页数据对象
 *
 */
public class TableDataInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    /** 总记录数 */
    private long total;
    /** 列表数据 */
    private List<?> rows;
    /** 消息状态码 */
    private String code;
    private double totalActualPayAmount; // 总实际支付金额
    private double totalSettlementAmount; // 总结算金额

    /**
     * 表格数据对象
     */
    public TableDataInfo()
    {
    }

    /**
     * 分页
     *
     * @param list 列表数据
     * @param total 总记录数
     */
    public TableDataInfo(List<?> list, int total)
    {
        this.rows = list;
        this.total = total;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List<?> getRows()
    {
        return rows;
    }

    public void setRows(List<?> rows)
    {
        this.rows = rows;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getTotalActualPayAmount() {
        return totalActualPayAmount;
    }

    public void setTotalActualPayAmount(double totalActualPayAmount) {
        this.totalActualPayAmount = totalActualPayAmount;
    }

    public double getTotalSettlementAmount() {
        return totalSettlementAmount;
    }

    public void setTotalSettlementAmount(double totalSettlementAmount) {
        this.totalSettlementAmount = totalSettlementAmount;
    }
}

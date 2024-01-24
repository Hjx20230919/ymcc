package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/5/10 20:27
 * @Description:合同结算项目
 */
@Table(name = "T_BIZ_SETTLEDETAIL")
public class BizSettleDetailDto {

    /**
     * 项目标识
     */
    @Id
    @Column(name = "DETAIL_ID")
    private String detailId;

    /**
     * 项目名称
     */
    @Column(name = "DETAIL_NAME")
    private String detailName;


    /**
     * 规格型号
     */
    @Column(name = "DETAIL_MODEL")
    private String detailModel;

    /**
     * 单位
     */
    @Column(name = "DETAIL_UNIT")
    private String detailUnit;

    /**
     * 数量
     */
    @Column(name = "DETAIL_TOTAL")
    private Integer detailTotal;

    /**
     * 单价（不含税）
     */
    @Column(name = "DETAIL_PRICE")
    private Double detailPrice;

    /**
     * 金额（价款）
     */
    @Column(name = "DETAIL_MONEY")
    private Double detailMoney;

    /**
     * 税率（%）
     */
    @Column(name = "DETAIL_RATE")
    private Integer detailRate;

    /**
     * 税额
     */
    @Column(name = "DETAIL_TAX")
    private Double detailTax;

    /**
     * 所属结算
     */
    @Column(name = "SETTLE_ID")
    private String settleId;


    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getDetailName() {
        return detailName;
    }

    public void setDetailName(String detailName) {
        this.detailName = detailName;
    }

    public String getDetailModel() {
        return detailModel;
    }

    public void setDetailModel(String detailModel) {
        this.detailModel = detailModel;
    }

    public String getDetailUnit() {
        return detailUnit;
    }

    public void setDetailUnit(String detailUnit) {
        this.detailUnit = detailUnit;
    }

    public Integer getDetailTotal() {
        return detailTotal;
    }

    public void setDetailTotal(Integer detailTotal) {
        this.detailTotal = detailTotal;
    }

    public Double getDetailPrice() {
        return detailPrice;
    }

    public void setDetailPrice(Double detailPrice) {
        this.detailPrice = detailPrice;
    }

    public Double getDetailMoney() {
        return detailMoney;
    }

    public void setDetailMoney(Double detailMoney) {
        this.detailMoney = detailMoney;
    }

    public Integer getDetailRate() {
        return detailRate;
    }

    public void setDetailRate(Integer detailRate) {
        this.detailRate = detailRate;
    }

    public Double getDetailTax() {
        return detailTax;
    }

    public void setDetailTax(Double detailTax) {
        this.detailTax = detailTax;
    }

    public String getSettleId() {
        return settleId;
    }

    public void setSettleId(String settleId) {
        this.settleId = settleId;
    }
}

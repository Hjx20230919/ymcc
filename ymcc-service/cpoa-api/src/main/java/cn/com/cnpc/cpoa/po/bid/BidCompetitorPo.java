package cn.com.cnpc.cpoa.po.bid;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  09:01
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidCompetitorPo {

    private String competitorId;

    @Excel(name = "公司名称")
    private String compName;

    @Excel(name = "公司位置")
    private String copmAddr;

    @Excel(name = "公司性质")
    private String compType;

    @Excel(name = "注册金额")
    private String regAmount;

    @Excel(name = "公司规模")
    private String compScale;

    private String mainAdv;

    private String curPerf;

    private String compAnal;

    private String createAt;

    private String creator;

    private String notes;
}

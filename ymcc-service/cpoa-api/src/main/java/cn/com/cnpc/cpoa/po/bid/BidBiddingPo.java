package cn.com.cnpc.cpoa.po.bid;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-01  11:03
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidBiddingPo {

    private String biddingId;

    @Excel(name = "招标项目编号")
    private String projNo;

    @Excel(name = "招标项目名称")
    private String projName;

    @Excel(name = "公告名称")
    private String noticeName;

    @Excel(name = "发布时间")
    private String publishAt;


    private String biddingConditions;

    private String projDesc;

    private String bidderQualification;

    private String bidderDocInfo;

    private String postBidDocInfo;

    private String publishMedia;

    private String linkInfo;

    private String otherInfo;

    @Excel(name = "招标文件金额（元）")
    private Float bidderFileAmount;

    @Excel(name = "招标文件获取时间(起)")
    private String getBidDocStartAt;

    @Excel(name = "招标文件获取时间(止)")
    private String getBidDocEndAt;

    @Excel(name = "保证金额（元）")
    private Long guaranteeAmount;

    @Excel(name = "缴纳保证金实际时间")
    private String gaActualAt;

    @Excel(name = "投标文件递交截止时间")
    private String postBidDocEndAt;

    @Excel(name = "计划投资金额（万元）")
    private Float bidAmount;

    @Excel(name = "开标时间")
    private String bidOpenAt;

    @Excel(name = "联系方式")
    private String contactMethod;

    private String sourceUrl;

    private String crawlAt;

    /**
     * Net   爬取，New  手动
     */
    private String crawlMethod;

    private String biddingStatus;

    private String keyWord;

    private String deptId;

    private String reserve1;

    private String reserve2;

    private String reserve3;

    private String notes;

    private String sourceHtml;

    private String stepId;

    private String manId;

    private String deptName;


}

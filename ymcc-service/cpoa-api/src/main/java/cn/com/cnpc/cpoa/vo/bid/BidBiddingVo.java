package cn.com.cnpc.cpoa.vo.bid;

import cn.com.cnpc.cpoa.vo.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-01  13:52
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidBiddingVo {
    private String biddingId;

    private String projNo;

    private String projName;

    private String noticeName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date publishAt;

    private String biddingConditions;

    private String projDesc;

    private String bidderQualification;

    private String bidderDocInfo;

    private String postBidDocInfo;

    private String publishMedia;

    private String linkInfo;

    private String otherInfo;

    private Float bidderFileAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date getBidDocStartAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date getBidDocEndAt;

    private Long guaranteeAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date gaActualAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date postBidDocEndAt;

    private Float bidAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date bidOpenAt;

    private String contactMethod;

    private String sourceUrl;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date crawlAt;

    private String biddingStatus;

    private String keyWord;

    private String deptId;

    private String reserve1;

    private String reserve2;

    private String reserve3;

    private String notes;

    private String sourceHtml;

    private List<AttachVo> attachVos;

}

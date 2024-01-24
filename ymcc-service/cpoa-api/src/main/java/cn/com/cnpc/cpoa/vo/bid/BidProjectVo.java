package cn.com.cnpc.cpoa.vo.bid;

import cn.com.cnpc.cpoa.vo.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-19  16:12
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidProjectVo {
    private String bidProjId;

    private String biddingId;

    private String deptId;

    /**
     * 1  借用
     0   不借用
     */
    private Integer isBorrowUkey;

    /**
     * AJY   安检院
     KT     科特
     */
    private String ukeyType;

    /**
     * Yes  归还
     No  未归还
     */
    private String isReturn;

    private String paymentMethod;

    private String payCompany;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date returnUkeyAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date borrowUkeyAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date getBidDocEndAt;

    private String isGetBidDoc;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date getBidDocActualAt;

    private Long guaranteeAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date gaEndAt;

    private String isGuaranteeAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date gaActualAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date uploadPlanAt;

    private String isUploadPlan;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date uploadActualAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date postBidDocEndAt;

    private String isPostBidDoc;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date postBidDocActaulAt;

    private Integer isBid;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date uploadBidNoticeAt;

    private String unBidDesc;

    private String bidProjDesc;

    /**
     * 1、投标准备   BidPrepare
     2、投标审核   BidAuditing
     3、退回          Back
     4、投标完成   Complete


     */
    private String projStatus;

    private String reserve1;

    private String reserve2;

    private String reserve3;

    private String notes;

    private List<AttachVo> attachVos;

    private String ownerType;

    /**
     * bidDocument 标书购买  guaranteeAmount  保证金额
     */
    private String type;

    private List<BidCompetitorVo> competitorVos;

}

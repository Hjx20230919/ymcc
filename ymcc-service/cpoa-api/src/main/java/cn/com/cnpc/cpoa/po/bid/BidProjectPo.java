package cn.com.cnpc.cpoa.po.bid;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-05  09:30
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidProjectPo {

    private String bidProjId;

    private String biddingId;

    private String deptId;

    private String createId;

    private String userName;

    /**
     * 1  借用
     0   不借用
     */
    private Integer isBorrowUkey;

    /**
     * AJY   安检院
     KT     科特
     */
    @Excel(name = "U-Key类型")
    private String ukeyType;

    /**
     * Yes  归还
     No  未归还
     */
    private String isReturn;

    private String paymentMethod;

    private String borrowUkeyAt;

    private String returnUkeyAt;

    private String getBidDocEndAt;

    private String isGetBidDoc;

    private String getBidDocActualAt;

    @Excel(name = "保证金额（元）")
    private Long guaranteeAmount;

    private String gaEndAt;

    private String isGuaranteeAmount;

    private String gaActualAt;

    private String uploadPlanAt;

    private String isUploadPlan;

    private String uploadActualAt;

    private String postBidDocEndAt;

    private String isPostBidDoc;

    private String postBidDocActaulAt;

    private Integer isBid;

    private String uploadBidNoticeAt;

    @Excel(name = "未中标说明")
    private String unBidDesc;

    @Excel(name = "投标总结")
    private String bidProjDesc;

    /**
     * 1、投标准备   BidPrepare
     2、投标审核   BidAuditing
     3、退回          Back
     4、投标完成   Complete


     */
    @Excel(name = "项目状态")
    private String projStatus;

    private String reserve1;

    private String reserve2;

    private String reserve3;

    private String notes;

    @Excel(name = "招标项目编号")
    private String projNo;

    @Excel(name = "招标项目名称")
    private String projName;

    @Excel(name = "开标时间")
    private String bidOpenAt;

    @Excel(name = "发布时间")
    private String publishAt;

    @Excel(name = "承办单位")
    private String deptName;

    private String objType;

    private String certiBorrowId;

    private String objId;

    @Excel(name = "计划投资金额")
    private Integer bidAmount;

    /**
     * 中标公司
     */
    private String bidName;

    private String borrowMan;

    private String borrowReason;

    private Integer isJointCheckup;

    /**
     * 标书购买
     */
    private int payBidFile;

    /**
     * 保证金
     */
    private int cashDeposit;

    /**
     * ukey
     */
    private int uKey;

    /**
     * 投标结果
     */
    private int bidResult;

    private String payCompany;


}

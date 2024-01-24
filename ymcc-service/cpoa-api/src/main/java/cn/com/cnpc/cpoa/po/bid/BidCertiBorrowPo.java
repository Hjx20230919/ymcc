package cn.com.cnpc.cpoa.po.bid;

import lombok.Data;

import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-21  08:52
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidCertiBorrowPo {

    private String objId;

    private String objType;

    private String certiBorrowId;

    private String bidProjId;

    private String deptId;

    private String borrowMan;

    private String borrowReason;

    private String borrowStartAt;

    private String borrowEndAt;

    private String watermarkContent;

    /**
     * inRevie 审核中,audited 审核已完成
     */
    private String certiBorrowStatus;

    private String checkMan;

    /**
     * 不同意   Disagree
     同意       Agree
     */
    private String checkStatus;

    private String notes;

    /**
     * 企业资质
     */
    private List<BidCertiPo> certiPos;

    /**
     * 企业资料
     */
    private List<BidCompInfoBorrowListPo> compInfoBorrowListVos;
}

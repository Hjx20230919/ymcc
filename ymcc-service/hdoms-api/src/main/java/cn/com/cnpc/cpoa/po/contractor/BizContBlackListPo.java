package cn.com.cnpc.cpoa.po.contractor;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-24  19:41
 * @Description:
 * @Version: 1.0
 */
@Data
public class BizContBlackListPo {

    private String blacklistId;

    @Excel(name = "承包商名称")
    private String contName;
    @Excel(name = "工商注册号")
    private String contIacNo;
    @Excel(name = "税务登记号")
    private String contTaxNo;
    @Excel(name = "组织机构代码")
    private String contOrgNo;
    @Excel(name = "法定代表人姓名")
    private String corporate;

    @Excel(name = "注册资本（万元）")
    private Long contRegCaptial;
    @Excel(name = "联系人姓名")
    private String linkman;
    @Excel(name = "联系人电话-移动")
    private String linkMobile;
    @Excel(name = "联系人电话-固定")
    private String linkPhone;

    private String linkCompany;

    private String comType;

    @Excel(name = "成立时间")
    private String setupTime;

    private String contBank;

    private String contAccount;

    private String linkFax;

    private String linkMail;

    private String contSiteUrl;
    @Excel(name = "准入级别")
    private String accessLevel;
    @Excel(name = "拉黑原因")
    private String blackReason;
    @Excel(name = "拉黑时间")
    private String blackAt;
    @Excel(name = "拉黑人员")
    private String blackMan;

    private Integer isRelieve;

    private String relieveReason;

    private String relieveAt;

    private String relieveMan;

    private String notes;

    @Excel(name = "地址-详情")
    private String contAddrDetail;
}

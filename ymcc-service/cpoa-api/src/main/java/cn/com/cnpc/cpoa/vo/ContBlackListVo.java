package cn.com.cnpc.cpoa.vo;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO   承包商黑名单
 * @date 2022/5/10 17:05
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ContBlackListVo {
    
    private String blacklistId;

    @Excel(name = "承包商名称")
    private String contName;

    @Excel(name = "组织机构代码")
    private String contOrgNo;

    @Excel(name = "准入级别")
    private String accessLevel;

    @Excel(name = "工商注册号")
    private String contIacNo;

    @Excel(name = "税务登记号")
    private String contTaxNo;

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

    @Excel(name = "联系人电话-公司")
    private String linkCompany;

    @Excel(name = "公司类型")
    private String comType;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Excel(name = "成立时间")
    private Date setupTime;

    @Excel(name = "开户银行")
    private String contBank;

    @Excel(name = "银行账号")
    private String contAccount;

    @Excel(name = "传真")
    private String linkFax;

    @Excel(name = "电子邮箱")
    private String linkMail;

    @Excel(name = "公司网址")
    private String contSiteUrl;

    @Excel(name = "拉黑原因")
    private String blackReason;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "拉黑时间")
    private Date blackAt;

    @Excel(name = "拉黑人员")
    private String blackMan;


    private Integer isRelieve;


    private String relieveReason;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date relieveAt;


    private String relieveMan;


    private String notes;

    @Excel(name = "地址-详情")
    private String contAddrDetail;

    private List<AttachVo> attachVos;
}

package cn.com.cnpc.cpoa.domain.contractor;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 19:44
 * @Description:承包商
 */
@Data
@Table(name = "T_CONT_CONTRACTOR")
public class BizContContractorDto {


    /**
     * 承包商标识
     */
    @Id
    @Column(name = "CONT_ID")
    private String contId;

    /**
     * 承包商名称
     */
    @Column(name = "CONT_NAME")
    private String contName;

    /**
     * 工商注册号
     */
    @Column(name = "CONT_IAC_NO")
    private String contIacNo;

    /**
     * 税务登记号
     */
    @Column(name = "CONT_TAX_NO")
    private String contTaxNo;

    /**
     * 组织机构代码
     */
    @Column(name = "CONT_ORG_NO")
    private String contOrgNo;

    /**
     * 邮编
     */
    @Column(name = "CONT_POSTCODE")
    private String contPostcode;

    /**
     * 地址-省
     */
    @Column(name = "CONT_ADDR_PROVINCE")
    private String contAddrProvince;

    /**
     * 地址-市
     */
    @Column(name = "CONT_ADDR_CITY")
    private String contAddrCity;

    /**
     * 地址-路
     */
    @Column(name = "CONT_ADDR_WAY")
    private String contAddrWay;

    /**
     * 地址-号
     */
    @Column(name = "CONT_ADDR_NUMBER")
    private String contAddrNumber;

    /**
     * 地址-详情
     */
    @Column(name = "CONT_ADDR_DETAIL")
    private String contAddrDetail;

    /**
     * 资质等级
     */
    @Column(name = "CERDIT_LEVEL")
    private String cerditLevel;

    /**
     * 质量管理体系认证情况及机构
     */
    @Column(name = "ISO_INFO")
    private String isoInfo;

    /**
     * HSE体系认证情况及机构
     */
    @Column(name = "HSE_INFO")
    private String hseInfo;

    /**
     * 特种行业许可证及编号
     */
    @Column(name = "TZHY_CODE")
    private String tzhyCode;

    /**
     * 安全生产许可证及编号
     */
    @Column(name = "AQSC_CODE")
    private String aqscCode;

    /**
     * 法定代表人姓名
     */
    @Column(name = "CORPORATE")
    private String corporate;

    /**
     * 获奖情况
     */
    @Column(name = "CONT_AWARDS")
    private String contAwards;

    /**
     * 注册资本（万元）
     */
    @Column(name = "CONT_REG_CAPTIAL")
    private String contRegCaptial;

    /**
     * 联系人姓名
     */
    @Column(name = "LINKMAN")
    private String linkman;

    /**
     * 联系人电话-移动
     */
    @Column(name = "LINK_MOBILE")
    private String linkMobile;

    /**
     * 联系人电话-固定
     */
    @Column(name = "LINK_PHONE")
    private String linkPhone;

    /**
     * 联系人电话-公司
     */
    @Column(name = "LINK_COMPANY")
    private String linkCompany;

    /**
     * 公司类型
     */
    @Column(name = "COM_TYPE")
    private String comType;

    /**
     * 成立时间
     */
    @Column(name = "SETUP_TIME")
    private Date setupTime;

    /**
     * 开户银行
     */
    @Column(name = "CONT_BANK")
    private String contBank;

    /**
     * 传真
     */
    @Column(name = "LINK_FAX")
    private String linkFax;

    /**
     * 银行账号
     */
    @Column(name = "CONT_ACCOUNT")
    private String contAccount;

    /**
     * 电子邮箱
     */
    @Column(name = "LINK_MAIL")
    private String linkMail;

    /**
     * 银行信用等级
     */
    @Column(name = "CONT_CREDIT_RATING")
    private String contCreditRating;

    /**
     * 公司网址
     */
    @Column(name = "CONT_SITE_URL")
    private String contSiteUrl;

    /**
     * 承包商状态
     */
    @Column(name = "CONT_STATE")
    private String contState;

    /**
     * 承包商冻结状态
     */
    @Column(name = "CONT_FREEZE_STATE")
    private String contFreezeState;

    /**
     *
     */
    @Column(name = "CONT_TEMP_FREEZE_DATE")
    private Date contTempFreezeDate;


    /**
     * 承包商状态时间
     */
    @Column(name = "CONT_STATE_AT")
    private Date contStateAt;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_AT")
    private Date createAt;

    /**
     * 经营范围
     */
    @Column(name = "CONT_SCOPE")
    private String contScope;


    /**
     * 年审时间
     */
    @Column(name = "CHECK_AT")
    private Date checkAt;


    /**
     * 年审结果
     */
    @Column(name = "CHECK_RESULT")
    private String checkResult;

    /**
     * 准入级别
     */
    @Column(name = "ACCESS_LEVEL")
    private String accessLevel;

    /**
     * 准入编码
     */
    @Column(name = "ACCESS_NO")
    private String accessNo;

    /**
     * 准入编码
     */
    @Column(name = "ACCE_STATE_AT")
    private Date acceStateAt;

    /**
     * 单位名称
     */
    @Column(name = "PART_NAME")
    private String partName;

}

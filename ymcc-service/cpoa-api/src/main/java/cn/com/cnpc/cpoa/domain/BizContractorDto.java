package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 21:53
 * @Description: 合同相对人
 */
@Table(name = "T_BIZ_CONTRACTOR")
public class BizContractorDto {

    /**
         * 相对人标识
     */
    @Id
    @Column(name = "CONT_ID")
    private String contId;

    /**
     * 相对人名称
     */
    @Column(name = "CONT_NAME")
    private String contName;

    /**
     * 相对人简称
     */
    @Column(name = "ALIAS_NAME")
    private String aliasName;


    /**
     * 合同相对人性质
     */
    @Column(name = "CONT_TYPE")
    private String contType;

    /**
     * 相对人地址
     */
    @Column(name = "CONT_ADDRSS")
    private String contAddrss;

    /**
     * 身份证号码
     */
    @Column(name = "ID_NO")
    private String idNo;

    /**
     * 组织机构代码
     */
    @Column(name = "ORG_NO")
    private String orgNo;

    /**
     * 负责人
     */
    @Column(name = "DUTY_MAN")
    private String dutyMan;


    /**
     * 注册资本
     */
    @Column(name = "REG_CAPTIAL")
    private String regCaptial;

    /**
     * 税务登记证号
     */
    @Column(name = "REG_CERT_NUM")
    private String regCertNum;

    /**
     * 传真
     */
    @Column(name = "FAX")
    private String fax;

    /**
     * 联系人
     */
    @Column(name = "LINKMAN")
    private String linkman;
    /**
     * 联系电话
     */
    @Column(name = "LINK_NUM")
    private String linkNum;


    /**
     * 开户银行
     */
    @Column(name = "CONT_BANK")
    private String contBank;

    /**
     * 开户账号
     */
    @Column(name = "CONT_ACCOUNT")
    private String contAccount;


    /**
     * 电子邮件
     */
    @Column(name = "LINK_MAIL")
    private String linkMail;

    /**
     * 经营范围
     */
    @Column(name = "BIZ_SCOPE")
    private String bizScope;


    /**
     * 入库时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 创建人
     */
    @Column(name = "CREATOR")
    private String creator;

    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;

    /**
     * 创建人
     */
    @Transient
    private String createName;

    //6	MARKET_TYPE	市场分类	NVARCHAR2(10)	10				市场分类：国内，国外
    @Column(name = "MARKET_TYPE")
    private String marketType;

    //7	COMPANY_TYPE	公司集团	NVARCHAR2(10)	10				公司集团：川庆内部，集团内部，集团外部
    @Column(name = "COMPANY_TYPE")
    private String companyType;

    //8	CONTRACT_TYPE	交易类型	NVARCHAR2(20)	20				交易类型：关联交易，非关联交易，内部责任书
    @Column(name = "CONTRACT_TYPE")
    private String contractType;

    //9	WORK_ZONE	作业区域	NVARCHAR2(20)	20				作业区域：川渝地区、塔里木地区、其它地区、长庆地区、深圳、天津、上海、其它
    @Column(name = "WORK_ZONE")
    private String workZone;

    // 所属单位
    @Column(name = "PARENT_NAME")
    private String parentName;

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getContType() {
        return contType;
    }

    public void setContType(String contType) {
        this.contType = contType;
    }

    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getRegCaptial() {
        return regCaptial;
    }

    public void setRegCaptial(String regCaptial) {
        this.regCaptial = regCaptial;
    }

    public String getRegCertNum() {
        return regCertNum;
    }

    public void setRegCertNum(String regCertNum) {
        this.regCertNum = regCertNum;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getLinkNum() {
        return linkNum;
    }

    public void setLinkNum(String linkNum) {
        this.linkNum = linkNum;
    }

    public String getLinkMail() {
        return linkMail;
    }

    public void setLinkMail(String linkMail) {
        this.linkMail = linkMail;
    }

    public String getBizScope() {
        return bizScope;
    }

    public void setBizScope(String bizScope) {
        this.bizScope = bizScope;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public String getAliasName() {
        return aliasName;
    }

    public void setAliasName(String aliasName) {
        this.aliasName = aliasName;
    }

    public String getDutyMan() {
        return dutyMan;
    }

    public void setDutyMan(String dutyMan) {
        this.dutyMan = dutyMan;
    }


    public String getContAddrss() {
        return contAddrss;
    }

    public void setContAddrss(String contAddrss) {
        this.contAddrss = contAddrss;
    }

    public String getContBank() {
        return contBank;
    }

    public void setContBank(String contBank) {
        this.contBank = contBank;
    }

    public String getContAccount() {
        return contAccount;
    }

    public void setContAccount(String contAccount) {
        this.contAccount = contAccount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getMarketType() {
        return marketType;
    }

    public void setMarketType(String marketType) {
        this.marketType = marketType;
    }

    public String getCompanyType() {
        return companyType;
    }

    public void setCompanyType(String companyType) {
        this.companyType = companyType;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getWorkZone() {
        return workZone;
    }

    public void setWorkZone(String workZone) {
        this.workZone = workZone;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}

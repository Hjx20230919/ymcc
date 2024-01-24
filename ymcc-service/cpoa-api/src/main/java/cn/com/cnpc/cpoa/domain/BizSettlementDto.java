package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/3/8 10:39
 * @Description: 结算传输
 */
@Table(name = "T_BIZ_SETTLEMENT")
public class BizSettlementDto {

    /**
     * 结算标识
     */
    @Id
    @Column(name = "SETTLE_ID")
    private String settleId;

    /**
     * 结算状态
     */
    @Column(name = "SETTLE_STATUS")
    private String settleStatus;

    /**
     * 部门标识
     */
    @Column(name = "DEPT_ID")
    private String deptId;

    /**
     * 承办人(上传人)
     */
    @Column(name = "USER_ID")
    private String userId;

    /**
     * 合同标识
     */
    @Column(name = "DEAL_ID")
    private String dealId;

    /**
     * 结算金额
     */
    @Column(name = "SETTLE_AMOUNT")
    private Double settleAmount;

    /**
     * 开户银行
     */
    @Column(name = "SETTLE_BANK")
    private String settleBank;

    /**
     * 开户账号
     */
    @Column(name = "SETTLE_ACOUNT")
    private String settleAcount;

    /**
     * 上传时间
     */
    @Column(name = "CREATE_TIME")
    private Date createTime;

    /**
     * 应付款时间
     */
    @Column(name = "PAYABLE_TIME")
    private Date payableTime;

    /**
     * 财务实际付款金额
     */
    @Column(name = "ACTUAL_PAY_AMOUNT")
    private Double actualPayAmount;

    /**
     * 付款日期
     */
    @Column(name = "ACTUAL_PAY_TIME")
    private Date actualPayTime;

    /**
     * 付款人
     */
    @Column(name = "ACTUAL_PAY_MAN")
    private String actualPayMan;

    /**
     * 财务付款说明
     */
    @Column(name = "ACTUAL_PAY_NOTES")
    private String actualPayNotes;

    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;


    /**
     * 单位名称
     */
    @Column(name = "CONT_NAME")
    private String contName2;

    /**
     * 纳税人识别号
     */
    @Column(name = "ORG_NO")
    private String orgNo;

    /**
     * 联系地址
     */
    @Column(name = "CONT_ADDRSS")
    private String contAddrss;

    /**
     * 联系电话
     */
    @Column(name = "LINK_NUM")
    private String linkNum;

    @Column(name = "DOWN_TIME")
    private Date downTime;

    //用户名
    @Transient
    private String userName;

    //部门
    @Transient
    private String deptName;
    //合同名
    @Transient
    private String dealName;

    @Transient
    private String stepNo;

    @Transient
    private List<String> userNames;

    @Transient
    private String contName;

    public String getContName2() {
        return contName2;
    }

    public void setContName2(String contName2) {
        this.contName2 = contName2;
    }

    public String getOrgNo() {
        return orgNo;
    }

    public void setOrgNo(String orgNo) {
        this.orgNo = orgNo;
    }

    public String getContAddrss() {
        return contAddrss;
    }

    public void setContAddrss(String contAddrss) {
        this.contAddrss = contAddrss;
    }

    public String getLinkNum() {
        return linkNum;
    }

    public void setLinkNum(String linkNum) {
        this.linkNum = linkNum;
    }

    public String getContName() {
        return contName;
    }

    public void setContName(String contName) {
        this.contName = contName;
    }

    public List<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(List<String> userNames) {
        this.userNames = userNames;
    }

    public String getStepNo() {
        return stepNo;
    }

    public void setStepNo(String stepNo) {
        this.stepNo = stepNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getSettleId() {
        return settleId;
    }

    public void setSettleId(String settleId) {
        this.settleId = settleId;
    }

    public String getSettleStatus() {
        return settleStatus;
    }

    public void setSettleStatus(String settleStatus) {
        this.settleStatus = settleStatus;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDealId() {
        return dealId;
    }

    public void setDealId(String dealId) {
        this.dealId = dealId;
    }

    public Double getSettleAmount() {
        return settleAmount;
    }

    public void setSettleAmount(Double settleAmount) {
        this.settleAmount = settleAmount;
    }

    public String getSettleBank() {
        return settleBank;
    }

    public void setSettleBank(String settleBank) {
        this.settleBank = settleBank;
    }

    public String getSettleAcount() {
        return settleAcount;
    }

    public void setSettleAcount(String settleAcount) {
        this.settleAcount = settleAcount;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getPayableTime() {
        return payableTime;
    }

    public void setPayableTime(Date payableTime) {
        this.payableTime = payableTime;
    }

    public Double getActualPayAmount() {
        return actualPayAmount;
    }

    public void setActualPayAmount(Double actualPayAmount) {
        this.actualPayAmount = actualPayAmount;
    }

    public Date getActualPayTime() {
        return actualPayTime;
    }

    public void setActualPayTime(Date actualPayTime) {
        this.actualPayTime = actualPayTime;
    }

    public String getActualPayMan() {
        return actualPayMan;
    }

    public void setActualPayMan(String actualPayMan) {
        this.actualPayMan = actualPayMan;
    }

    public String getActualPayNotes() {
        return actualPayNotes;
    }

    public void setActualPayNotes(String actualPayNotes) {
        this.actualPayNotes = actualPayNotes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDownTime() {
        return downTime;
    }

    public void setDownTime(Date downTime) {
        this.downTime = downTime;
    }
}

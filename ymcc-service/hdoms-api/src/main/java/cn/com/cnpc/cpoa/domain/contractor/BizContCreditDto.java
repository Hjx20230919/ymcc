package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:24
 * @Description:准入资质
 */
@Table(name="T_CONT_CREDIT")
public class BizContCreditDto {

; /**
     * 资质标识
     */
    @Id
    @Column(name = "CREDIT_ID")
    private String creditId;

    /**
     * 资质名称
     */
    @Column(name = "CREDIT_NAME")
    private String creditName;

    /**
     * 资质有效期起
     */
    @Column(name = "CREDIT_VALID_START")
    private Date creditValidStart;

    /**
     * 资质有效期止
     */
    @Column(name = "CREDIT_VALID_END")
    private Date creditValidEnd;

    /**
     * 资质说明
     */
    @Column(name = "CREDIT_DESC")
    private String creditDesc;

    /**
     * 资质序号
     */
    @Column(name = "CREDIT_NO")
    private Integer creditNo;

    /**
     * 所属准入
     */
    @Column(name = "ACCE_ID")
    private String acceId;

    /**
     * 模板项标识
     */
    @Column(name = "ITEM_ID")
    private String itemId;

    /**
     * 资质项目名称
     */
    @Column(name = "CREDIT_PROJ_NAME")
    private String creditProjName;

    /**
     * 资质状态
     */
    @Column(name = "CREDIT_STATE")
    private String creditState;


    @Transient
    private String isMust;

    @Transient
    private String itemProjDesc;

    @Transient
    private String creditTimeFlag;

    @Transient
    private String creditItemFlag;


    public String getCreditTimeFlag() {
        return creditTimeFlag;
    }

    public void setCreditTimeFlag(String creditTimeFlag) {
        this.creditTimeFlag = creditTimeFlag;
    }

    public String getCreditItemFlag() {
        return creditItemFlag;
    }

    public void setCreditItemFlag(String creditItemFlag) {
        this.creditItemFlag = creditItemFlag;
    }

    public String getItemProjDesc() {
        return itemProjDesc;
    }

    public void setItemProjDesc(String itemProjDesc) {
        this.itemProjDesc = itemProjDesc;
    }

    public String getIsMust() {
        return isMust;
    }

    public void setIsMust(String isMust) {
        this.isMust = isMust;
    }

    public String getCreditState() {
        return creditState;
    }

    public void setCreditState(String creditState) {
        this.creditState = creditState;
    }

    public String getCreditId() {
        return creditId;
    }

    public void setCreditId(String creditId) {
        this.creditId = creditId;
    }

    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }

    public Date getCreditValidStart() {
        return creditValidStart;
    }

    public void setCreditValidStart(Date creditValidStart) {
        this.creditValidStart = creditValidStart;
    }

    public Date getCreditValidEnd() {
        return creditValidEnd;
    }

    public void setCreditValidEnd(Date creditValidEnd) {
        this.creditValidEnd = creditValidEnd;
    }

    public String getCreditDesc() {
        return creditDesc;
    }

    public void setCreditDesc(String creditDesc) {
        this.creditDesc = creditDesc;
    }

    public Integer getCreditNo() {
        return creditNo;
    }

    public void setCreditNo(Integer creditNo) {
        this.creditNo = creditNo;
    }

    public String getAcceId() {
        return acceId;
    }

    public void setAcceId(String acceId) {
        this.acceId = acceId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getCreditProjName() {
        return creditProjName;
    }

    public void setCreditProjName(String creditProjName) {
        this.creditProjName = creditProjName;
    }
}

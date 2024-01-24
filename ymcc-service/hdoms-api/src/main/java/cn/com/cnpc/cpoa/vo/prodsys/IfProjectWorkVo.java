/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/5/17 16:32
 */
package cn.com.cnpc.cpoa.vo.prodsys;

/**
 * <>
 *
 * @author wangjun
 * @create 2020/5/17 16:32
 * @since 1.0.0
 */
public class IfProjectWorkVo {
//    PW_LINK_ID	文本	a4b31e1a-126e-487e-b4fe-2b296e124d25
//    CONTRACT_ID	文本	3ccd30935c9f48d680b21cce369d2053
//    CLAS_ID	文本	sadasdasd213123wdsasad422
//    CLAS_TYPE	文本	　
//    PLAN_WORK_NUM	数值	5
//    PLAN_DOC_NUM	数值	5
//    UNIT	文本	台
//    PRICE	数值	89999
//    SPARE1	数值	0
//    SPARE2	数值	0
//    PLAN_WORK_TIME	数值	30

    private String PW_LINK_ID;

    private String CONTRACT_ID;

    private String CLAS_ID;

    private String CLAS_TYPE;

    private Float PLAN_WORK_NUM;

    private Integer PLAN_DOC_NUM;

    private String UNIT;

    private Double PRICE;

    private Float SPARE1;

    private Integer SPARE2; // init doc num

    private Float PLAN_WORK_TIME;

    private Float FINISH_WORK_NUM; // 完成工作量

    public String getPW_LINK_ID() {
        return PW_LINK_ID;
    }

    public void setPW_LINK_ID(String PW_LINK_ID) {
        this.PW_LINK_ID = PW_LINK_ID;
    }

    public String getCONTRACT_ID() {
        return CONTRACT_ID;
    }

    public void setCONTRACT_ID(String CONTRACT_ID) {
        this.CONTRACT_ID = CONTRACT_ID;
    }

    public String getCLAS_ID() {
        return CLAS_ID;
    }

    public void setCLAS_ID(String CLAS_ID) {
        this.CLAS_ID = CLAS_ID;
    }

    public String getCLAS_TYPE() {
        return CLAS_TYPE;
    }

    public void setCLAS_TYPE(String CLAS_TYPE) {
        this.CLAS_TYPE = CLAS_TYPE;
    }

    public Float getPLAN_WORK_NUM() {
        return PLAN_WORK_NUM;
    }

    public void setPLAN_WORK_NUM(Float PLAN_WORK_NUM) {
        this.PLAN_WORK_NUM = PLAN_WORK_NUM;
    }

    public Integer getPLAN_DOC_NUM() {
        return PLAN_DOC_NUM;
    }

    public void setPLAN_DOC_NUM(Integer PLAN_DOC_NUM) {
        this.PLAN_DOC_NUM = PLAN_DOC_NUM;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public Double getPRICE() {
        return PRICE;
    }

    public void setPRICE(Double PRICE) {
        this.PRICE = PRICE;
    }

    public Float getSPARE1() {
        return SPARE1;
    }

    public void setSPARE1(Float SPARE1) {
        this.SPARE1 = SPARE1;
    }

    public Integer getSPARE2() {
        return SPARE2;
    }

    public void setSPARE2(Integer SPARE2) {
        this.SPARE2 = SPARE2;
    }

    public Float getPLAN_WORK_TIME() {
        return PLAN_WORK_TIME;
    }

    public void setPLAN_WORK_TIME(Float PLAN_WORK_TIME) {
        this.PLAN_WORK_TIME = PLAN_WORK_TIME;
    }

    public Float getFINISH_WORK_NUM() {
        return FINISH_WORK_NUM;
    }

    public void setFINISH_WORK_NUM(Float FINISH_WORK_NUM) {
        this.FINISH_WORK_NUM = FINISH_WORK_NUM;
    }

    @Override
    public String toString() {
        String s = "IfProjectWorkVo:{";
        s += "PW_LINK_ID:" + getPW_LINK_ID();
        s += ",CONTRACT_ID:" + getCONTRACT_ID();
        s += ",CLAS_ID:" + getCLAS_ID();
        s += ",CLAS_TYPE:" + getCLAS_TYPE();
        s += ",PLAN_WORK_NUM:" + getPLAN_WORK_NUM();
        s += ",PLAN_DOC_NUM:" + getPLAN_DOC_NUM();
        s += ",UNIT:" + getUNIT();
        s += ",PRICE:" + getPRICE();
        s += ",SPARE1:" + getSPARE1();
        s += ",SPARE2:" + getSPARE2();
        s += ",PLAN_WORK_TIME:" + getPLAN_WORK_TIME();
        s += ",FINISH_WORK_NUM:" + getFINISH_WORK_NUM();
        s += "}";
        return s;
    }
}

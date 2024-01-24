/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/5/17 16:32
 */
package cn.com.cnpc.cpoa.vo.prodsys;

import lombok.Data;

/**
 * <>
 *
 * @author wangjun
 * @create 2020/5/17 16:32
 * @since 1.0.0
 */
public class IfWorkTableVo {
//    CLAS_ID	文本	sadasdasd213123wdsasad432
//    DPT_ID	文本	924
//    UNIT_NAME	文本	石油钻采设备检测技术研究所
//    CLAS_TYPE	文本	仪器仪表检测
//    SERV_TYPE	文本	压力表、报警仪、电表等检测
//    UNIT	文本	只
//    REMARK	文本	钻机检测
//    WORK_TYPE1	文本	检验检测

    private String CLASS_ID;

    private String DPT_ID;

    private String UNIT_NAME;

    private String CLAS_TYPE;

    private String SERV_TYPE;

    private String UNIT;

    private String REMARK;

    private String WORK_TYPE1;

    public String getCLASS_ID() {
        return CLASS_ID;
    }

    public void setCLASS_ID(String CLASS_ID) {
        this.CLASS_ID = CLASS_ID;
    }

    public String getDPT_ID() {
        return DPT_ID;
    }

    public void setDPT_ID(String DPT_ID) {
        this.DPT_ID = DPT_ID;
    }

    public String getUNIT_NAME() {
        return UNIT_NAME;
    }

    public void setUNIT_NAME(String UNIT_NAME) {
        this.UNIT_NAME = UNIT_NAME;
    }

    public String getCLAS_TYPE() {
        return CLAS_TYPE;
    }

    public void setCLAS_TYPE(String CLAS_TYPE) {
        this.CLAS_TYPE = CLAS_TYPE;
    }

    public String getSERV_TYPE() {
        return SERV_TYPE;
    }

    public void setSERV_TYPE(String SERV_TYPE) {
        this.SERV_TYPE = SERV_TYPE;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getREMARK() {
        return REMARK;
    }

    public void setREMARK(String REMARK) {
        this.REMARK = REMARK;
    }

    public String getWORK_TYPE1() {
        return WORK_TYPE1;
    }

    public void setWORK_TYPE1(String WORK_TYPE1) {
        this.WORK_TYPE1 = WORK_TYPE1;
    }

    @Override
    public String toString() {
        String s = "IfWorkTableVo={";
        s += "CLAS_ID:" + getCLASS_ID();
        s += ",DPT_ID:" + getDPT_ID();
        s += ",UNIT_NAME:" + getUNIT_NAME();
        s += ",CLAS_TYPE:" + getCLAS_TYPE();
        s += ",SERV_TYPE:" + getSERV_TYPE();
        s += ",UNIT:" + getUNIT();
        s += ",REMARK:" + getREMARK();
        s += ",WORK_TYPE1:" + getWORK_TYPE1();
        s += "}";
        return s;
    }
}

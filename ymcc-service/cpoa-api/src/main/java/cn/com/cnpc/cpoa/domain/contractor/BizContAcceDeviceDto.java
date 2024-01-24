package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:15
 * @Description:准入申请设备明细
 */
@Table(name="T_CONT_ACCE_DEVICE")
public class BizContAcceDeviceDto {


    /**
     * 设备明细标识
     */
    @Id
    @Column(name = "DEV_ID")
    private String devId;

    /**
     * 准入标识
     */
    @Column(name = "ACCE_ID")
    private String acceId;

    /**
     * 设备明细序号
     */
    @Column(name = "DEV_NO")
    private Integer devNo;

    /**
     * 设备名称
     */
    @Column(name = "DEV_NAME")
    private String devName;

    /**
     * 设备型号规格
     */
    @Column(name = "DEV_TYPE")
    private String devType;

    /**
     * 设备数量
     */
    @Column(name = "DEV_TOTAL")
    private Integer devTotal;

    /**
     * 设备完好状况
     */
    @Column(name = "DEV_STATUS")
    private String devStatus;

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getAcceId() {
        return acceId;
    }

    public void setAcceId(String acceId) {
        this.acceId = acceId;
    }

    public Integer getDevNo() {
        return devNo;
    }

    public void setDevNo(Integer devNo) {
        this.devNo = devNo;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public Integer getDevTotal() {
        return devTotal;
    }

    public void setDevTotal(Integer devTotal) {
        this.devTotal = devTotal;
    }

    public String getDevStatus() {
        return devStatus;
    }

    public void setDevStatus(String devStatus) {
        this.devStatus = devStatus;
    }
}

package cn.com.cnpc.cpoa.vo.contractor.data;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 20:26
 * @Description:
 */
@Data
public class ContAcceDeviceData {


    /**
     * 设备明细标识
     */
    private String devId;

    /**
     * 准入标识
     */
    private String acceId;

    /**
     * 设备明细序号
     */
    private Integer devNo;

    /**
     * 设备名称
     */
    private String devName;

    /**
     * 设备型号规格
     */
    private String devType;

    /**
     * 设备数量
     */
    private Integer devTotal;

    /**
     * 设备完好状况
     */
    private String devStatus;
}

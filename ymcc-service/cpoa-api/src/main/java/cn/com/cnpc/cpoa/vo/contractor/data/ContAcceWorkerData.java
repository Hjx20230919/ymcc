package cn.com.cnpc.cpoa.vo.contractor.data;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 20:27
 * @Description:
 */
@Data
public class ContAcceWorkerData {
    /**
     * 人员明细标识
     */
    private String workerId;

    /**
     * 准入申请标识
     */
    private String acceId;

    /**
     * 人员明细序号
     */
    private Integer workerNo;

    /**
     * 人员明细姓名
     */
    private String workerName;

    /**
     * 人员明细性别
     */
    private String workerSex;

    /**
     * 人员明细年龄
     */
    private Integer workerAge;

    /**
     * 人员明细职称
     */
    private String workerStaff;

    /**
     * 人员明细学历
     */
    private String workerAcademic;

    /**
     * 人员明细专业
     */
    private String workerSpecial;
}

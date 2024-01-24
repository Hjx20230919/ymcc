package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 20:52
 * @Description:准入申请从业人员明细
 */
@Table(name ="T_CONT_ACCE_WORKER")
public class BizContAcceWorkerDto {

    /**
     * 人员明细标识
     */
    @Id
    @Column(name = "WORKER_ID")
    private String workerId;

    /**
     * 准入申请标识
     */
    @Column(name = "ACCE_ID")
    private String acceId;

    /**
     * 人员明细序号
     */
    @Column(name = "WORKER_NO")
    private Integer workerNo;

    /**
     * 人员明细姓名
     */
    @Column(name = "WORKER_NAME")
    private String workerName;

    /**
     * 人员明细性别
     */
    @Column(name = "WORKER_SEX")
    private String workerSex;

    /**
     * 人员明细年龄
     */
    @Column(name = "WORKER_AGE")
    private Integer workerAge;

    /**
     * 人员明细职称
     */
    @Column(name = "WORKER_STAFF")
    private String workerStaff;

    /**
     * 人员明细学历
     */
    @Column(name = "WORKER_ACADEMIC")
    private String workerAcademic;

    /**
     * 人员明细专业
     */
    @Column(name = "WORKER_SPECIAL")
    private String workerSpecial;

    public String getWorkerId() {
        return workerId;
    }

    public void setWorkerId(String workerId) {
        this.workerId = workerId;
    }

    public String getAcceId() {
        return acceId;
    }

    public void setAcceId(String acceId) {
        this.acceId = acceId;
    }

    public Integer getWorkerNo() {
        return workerNo;
    }

    public void setWorkerNo(Integer workerNo) {
        this.workerNo = workerNo;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getWorkerSex() {
        return workerSex;
    }

    public void setWorkerSex(String workerSex) {
        this.workerSex = workerSex;
    }

    public Integer getWorkerAge() {
        return workerAge;
    }

    public void setWorkerAge(Integer workerAge) {
        this.workerAge = workerAge;
    }

    public String getWorkerStaff() {
        return workerStaff;
    }

    public void setWorkerStaff(String workerStaff) {
        this.workerStaff = workerStaff;
    }

    public String getWorkerAcademic() {
        return workerAcademic;
    }

    public void setWorkerAcademic(String workerAcademic) {
        this.workerAcademic = workerAcademic;
    }

    public String getWorkerSpecial() {
        return workerSpecial;
    }

    public void setWorkerSpecial(String workerSpecial) {
        this.workerSpecial = workerSpecial;
    }
}

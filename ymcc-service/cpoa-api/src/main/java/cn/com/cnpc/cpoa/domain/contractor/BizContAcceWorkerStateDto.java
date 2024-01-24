package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_CONT_ACCE_WORKER_STATE")
public class BizContAcceWorkerStateDto {

    @Id
    @Column(name = "STATE_ID")
    private String stateId;

    @Column(name = "ACCE_ID")
    private String acceId;

    @Column(name = "WORKER_STATE_NO")
    private Integer workerStateNo;

    @Column(name = "WORKER_NAME")
    private String workerName;


    @Column(name = "CREDIT_NAME")
    private String creditName;


    @Column(name = "WORKER_ORG")
    private String workerOrg;

    @Column(name = "CREDIT_VALID_START")
    private Date creditValidStart;

    @Column(name = "CREDIT_VALID_END")
    private Date creditValidEnd;

    @Column(name = "WORKER_SACN")
    private String workerSacn;

    /**
     * @return STATE_ID
     */
    public String getStateId() {
        return stateId;
    }

    /**
     * @param stateId
     */
    public void setStateId(String stateId) {
        this.stateId = stateId == null ? null : stateId.trim();
    }

    /**
     * @return ACCE_ID
     */
    public String getAcceId() {
        return acceId;
    }

    /**
     * @param acceId
     */
    public void setAcceId(String acceId) {
        this.acceId = acceId == null ? null : acceId.trim();
    }

    /**
     * @return WORKER_STATE_NO
     */
    public Integer getWorkerStateNo() {
        return workerStateNo;
    }

    /**
     * @param workerStateNo
     */
    public void setWorkerStateNo(Integer workerStateNo) {
        this.workerStateNo = workerStateNo;
    }

    /**
     * @return WORKER_NAME
     */
    public String getWorkerName() {
        return workerName;
    }

    /**
     * @param workerName
     */
    public void setWorkerName(String workerName) {
        this.workerName = workerName == null ? null : workerName.trim();
    }

    /**
     * @return WORKER_ORG
     */
    public String getWorkerOrg() {
        return workerOrg;
    }

    /**
     * @param workerOrg
     */
    public void setWorkerOrg(String workerOrg) {
        this.workerOrg = workerOrg == null ? null : workerOrg.trim();
    }

    /**
     * @return CREDIT_VALID_START
     */
    public Date getCreditValidStart() {
        return creditValidStart;
    }

    /**
     * @param creditValidStart
     */
    public void setCreditValidStart(Date creditValidStart) {
        this.creditValidStart = creditValidStart;
    }

    /**
     * @return CREDIT_VALID_END
     */
    public Date getCreditValidEnd() {
        return creditValidEnd;
    }

    /**
     * @param creditValidEnd
     */
    public void setCreditValidEnd(Date creditValidEnd) {
        this.creditValidEnd = creditValidEnd;
    }

    /**
     * @return WORKER_SACN
     */
    public String getWorkerSacn() {
        return workerSacn;
    }

    /**
     * @param workerSacn
     */
    public void setWorkerSacn(String workerSacn) {
        this.workerSacn = workerSacn == null ? null : workerSacn.trim();
    }


    public String getCreditName() {
        return creditName;
    }

    public void setCreditName(String creditName) {
        this.creditName = creditName;
    }
}
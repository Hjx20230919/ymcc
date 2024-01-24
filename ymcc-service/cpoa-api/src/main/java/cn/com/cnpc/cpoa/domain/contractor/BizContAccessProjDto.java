package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 21:37
 * @Description:承包商准入项目
 */
@Table(name = "T_CONT_ACCESS_PROJ")
public class BizContAccessProjDto {

    /**
     * 准入项目标识
     */
    @Id
    @Column(name = "PROJ_ID")
    private String projId;

    /**
     * 准入申请标识
     */
    @Column(name = "ACCE_ID")
    private String acceId;

    /**
     * 承包商标识
     */
    @Column(name = "CONT_ID")
    private String contId;

    /**
     * 项目名称
     */
    @Column(name = "PROJ_NAME")
    private String projName;

    /**
     * 项目状态
     */
    @Column(name = "PROJ_STATE")
    private String projState;

    /**
     * 准入有效期
     */
    @Column(name = "ACCE_LIMIT_TIME")
    private Date acceLimitTime;


    /**
     * 准入类型
     */
    @Column(name = "PROJ_ACCESS_TYPE")
    private String projAccessType;


    public String getProjId() {
        return projId;
    }

    public void setProjId(String projId) {
        this.projId = projId;
    }

    public String getAcceId() {
        return acceId;
    }

    public void setAcceId(String acceId) {
        this.acceId = acceId;
    }

    public String getContId() {
        return contId;
    }

    public void setContId(String contId) {
        this.contId = contId;
    }

    public String getProjName() {
        return projName;
    }

    public void setProjName(String projName) {
        this.projName = projName;
    }

    public String getProjState() {
        return projState;
    }

    public void setProjState(String projState) {
        this.projState = projState;
    }

    public Date getAcceLimitTime() {
        return acceLimitTime;
    }

    public void setAcceLimitTime(Date acceLimitTime) {
        this.acceLimitTime = acceLimitTime;
    }

    public String getProjAccessType() {
        return projAccessType;
    }

    public void setProjAccessType(String projAccessType) {
        this.projAccessType = projAccessType;
    }
}

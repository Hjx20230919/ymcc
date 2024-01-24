package cn.com.cnpc.cpoa.domain.contractor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 20:57
 * @Description:准入申请准入范围
 */
@Table(name = "T_CONT_ACCE_SCOPE")
public class BizContAcceScopeDto {

    /**
     * 准入范围标识
     */
    @Id
    @Column(name = "SCOPE_ID")
    private String scopeId;

    /**
     * 准入申请标识
     */
    @Column(name = "ACCE_ID")
    private String acceId;

    /**
     * 准入范围序号
     */
    @Column(name = "SCOPE_NO")
    private Integer scopeNo;

    /**
     * 准入范围名称
     */
    @Column(name = "SCOPE_NAME")
    private String scopeName;

    /**
     * 准入范围资质等级
     */
    @Column(name = "SCOPE_CREDIT_LEVEL")
    private String scopeCreditLevel;

    public String getScopeId() {
        return scopeId;
    }

    public void setScopeId(String scopeId) {
        this.scopeId = scopeId;
    }

    public String getAcceId() {
        return acceId;
    }

    public void setAcceId(String acceId) {
        this.acceId = acceId;
    }

    public Integer getScopeNo() {
        return scopeNo;
    }

    public void setScopeNo(Integer scopeNo) {
        this.scopeNo = scopeNo;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public String getScopeCreditLevel() {
        return scopeCreditLevel;
    }

    public void setScopeCreditLevel(String scopeCreditLevel) {
        this.scopeCreditLevel = scopeCreditLevel;
    }
}

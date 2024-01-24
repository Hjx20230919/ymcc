package cn.com.cnpc.cpoa.vo.contractor.data;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 20:27
 * @Description:
 */
@Data
public class ContAcceScopeData {

    /**
     * 准入范围标识
     */
    private String scopeId;

    /**
     * 准入申请标识
     */
    private String acceId;

    /**
     * 准入范围序号
     */
    private Integer scopeNo;

    /**
     * 准入范围名称
     */
    private String scopeName;

    /**
     * 准入范围资质等级
     */
    private String scopeCreditLevel;
}

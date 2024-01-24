/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * FileName: BizDealPsJoinDto
 * Author:   wangjun
 * Date:     22/02/2020 20:06
 */
package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * <>
 *
 * @author anonymous
 * @create 22/02/2020 20:06
 * @since 1.0.0
 */
@Data
@Table(name = "T_BIZ_DEAL_PS_JOIN")
public class BizDealPsJoinDto {

    @Id
    @Column(name = "ID")
    private String id;

    /**
     * PS deal id
     */
    @Column(name = "DEAL_ID")
    private String dealId;

    /**
     * XS/NZ/TH/XX deal id
     */
    @Column(name = "DEAL_JOIN_ID")
    private String dealJoinId;
}

/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/7/19 9:14
 */
package cn.com.cnpc.cpoa.vo.prodsys;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * <>
 *
 * @author anonymous
 * @create 2020/7/19 9:14
 * @since 1.0.0
 */
@Data
public class RptClientDeptAnalysisVo {

    @Excel(name = "单位名称")
    private String deptName;

    @Excel(name = "往期金额")
    private Double lastAmount;

    @Excel(name = "本期金额")
    private Double currentAmount;

    @Excel(name = "增减")
    private Double growth;

    @Excel(name = "增减比")
    private Float growthRatio;

}

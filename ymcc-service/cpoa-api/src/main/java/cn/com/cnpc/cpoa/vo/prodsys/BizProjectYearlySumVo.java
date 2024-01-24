/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * Author:   wangjun
 * Date:     2020/6/26 20:53
 */
package cn.com.cnpc.cpoa.vo.prodsys;

import lombok.Data;

/**
 * <>
 *
 * @author wangjun
 * @create 2020/6/26 20:53
 * @since 1.0.0
 */
@Data
public class BizProjectYearlySumVo {
    private String deptId;
    private String deptName;
    private String year;

    private double totalKpi;
    private double deal; // 合同（关联交易 & 非关联交易）
    private double nz; //内责
    private double instruction; // 指令、划拨
    private double th; // 3万以下

    private double s1Kpi;
    private double s1Real;
    private double s1Ratio;
    private double s2Kpi;
    private double s2Real;
    private double s2Ratio;
    private double s3Kpi;
    private double s3Real;
    private double s3Ratio;
    private double s4Kpi;
    private double s4Real;
    private double s4Ratio;
}

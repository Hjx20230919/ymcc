package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

/**
 * <>
 *
 * @author wangjun
 * @create 10/05/2020 17:41
 * @since 1.0.0
 */
@Data
public class BizAnalysisDto {

    // workType, serviceType, etc...
    private String bizItem;

    private Double lastAmount;

    private Double currentAmount;

    private Double growth;

    private Float growthRatio;

}

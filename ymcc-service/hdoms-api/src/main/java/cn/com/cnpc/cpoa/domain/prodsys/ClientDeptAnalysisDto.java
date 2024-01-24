package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

/**
 * <analysis by contract type>
 * as a vo maybe persist in future
 * @author wangjun
 * @create 23/03/2020 09:00
 * @since 1.0.0
 */
@Data
public class ClientDeptAnalysisDto {

    private String deptName;

    private String subDeptName;

    private Double lastAmount;

    private Double currentAmount;

    private Double growth;

    private Float growthRatio;
}

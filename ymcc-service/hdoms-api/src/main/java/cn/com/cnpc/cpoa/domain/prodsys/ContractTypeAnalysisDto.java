package cn.com.cnpc.cpoa.domain.prodsys;

import lombok.Data;

import javax.persistence.Transient;

/**
 * <analysis by contract type>
 * as a vo maybe persist in future
 * @author wangjun
 * @create 23/03/2020 09:00
 * @since 1.0.0
 */
@Data
public class ContractTypeAnalysisDto {

    private String contractType;

    private Double lastAmount;

    private Double currentAmount;

    private Double growth;

    private Float growthRatio;
}

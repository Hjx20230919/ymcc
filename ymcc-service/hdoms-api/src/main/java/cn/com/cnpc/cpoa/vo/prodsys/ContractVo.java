package cn.com.cnpc.cpoa.vo.prodsys;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <>
 *
 * @author anonymous
 * @create 27/02/2020 11:35
 * @since 1.0.0
 */
@Data
public class ContractVo {

    private String CONTRACT_ID;
    private String CLIENT_ID;
    private String CONTRACT_NAME;
    private String REFER_UNIT;
    private String MARKET_TYPE;
    private String COMPANY_TYPE;
    private String CONTRACT_TYPE;
    private String WORK_ZONE;
    private String WORK_TYPE;
    private String CONTRACT_NUMBER;
    private String RESPON_DEPART;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date CONTRACT_BEGIN_DATE;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date CONTRACT_PLAN_END_DATE;

    private Double CONTRACT_PRICE;
    private String PAY_TYPE;
    private String CREATE_USER;
    private String DEAL_ID;

}

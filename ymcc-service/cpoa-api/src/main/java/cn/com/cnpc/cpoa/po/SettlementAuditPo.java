package cn.com.cnpc.cpoa.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/3/19 14:28
 * @Description: 结算审核实体
 */
@Data
public class SettlementAuditPo {

    private String dealType;
    private String settleStatus;
    private String dealIncome;
    private String deptId;
    private String dealValue;
    private String settleAmount;
    private String dealId;
    //已结清金额
    private String sumSettleAmount;
    private String contName;
    private String deptName;
    private String userId;
    private String userName;
    private String dealName;
    private String settleId;
    //当前可审核人
    private List<String> userNames;
    private String dealNo;


    private String dealStart;

    private String dealEnd;


}

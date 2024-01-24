package cn.com.cnpc.cpoa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;

@Data
public class RreceiptVo {

    private Integer id;

    private String dealId;

    /**
     * 收付款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date receiptTime;

    /**
     * 收付款金额
     */
    private String receiptAmount;

    /**
     * 字符方式
     */
    private String modepayment;

    /**
     * t_biz_settlement.id(外键1)
     */
    private String settlementId;
}

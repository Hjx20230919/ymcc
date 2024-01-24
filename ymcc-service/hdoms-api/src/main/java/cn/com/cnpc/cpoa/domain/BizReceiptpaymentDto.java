package cn.com.cnpc.cpoa.domain;

import javax.persistence.*;
import java.io.Serializable;
import lombok.Data;

import java.util.Date;

/**
 * TBizReceiptpayment
 * @Author liuql
 * @Date 2023-07-31
 */
@Data
@Table(name = "T_BIZ_RECEIPTPAYMENT")
public class BizReceiptpaymentDto {

	@Id
 	@Column(name = "ID", length = 11 )
	private Integer id;

 	@Column(name = "DEAL_ID", length = 255 )
	private String dealId;

	/**
	 * 收付款时间
	 */
 	@Column(name = "RECEIPT_TIME" )
	private Date receiptTime;

	/**
	 * 收付款金额
	 */
 	@Column(name = "RECEIPT_AMOUNT", length = 255 )
	private String receiptAmount;

	/**
	 * 字符方式
	 */
 	@Column(name = "MODEPAYMENT", length = 255 )
	private String modepayment;

	/**
	 * t_biz_settlement.id(外键1)
	 */
 	@Column(name = "SETTLEMENT_ID", length = 255 )
	private String settlementId;
	/**
	 * 里程碑
	 */
 	@Transient
	private String milestone;
	/**
	 * 结算时间
	 */
	@Transient
	private Date createTime;

}

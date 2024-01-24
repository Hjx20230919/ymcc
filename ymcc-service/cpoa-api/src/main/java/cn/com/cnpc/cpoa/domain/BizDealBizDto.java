package cn.com.cnpc.cpoa.domain;

import javax.persistence.Column;

import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

/**
 * TBizDealBiz
 * @Author liuql
 * @Date 2023-07-27
 */

@Data
@Table(name = "T_BIZ_DEAL_BIZ")

public class BizDealBizDto {

	private static final long serialVersionUID = 1L;
	@Id
 	@Column(name = "DEAL_ID", length = 32 )
	private String dealId;

 	@Column(name = "DEAL_NO", length = 100 )
	private String dealNo;

 	@Column(name = "DEAL_BIZ", length = 100 )
	private String dealBiz;


}

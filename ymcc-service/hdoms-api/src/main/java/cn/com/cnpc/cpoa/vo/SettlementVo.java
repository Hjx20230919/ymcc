package cn.com.cnpc.cpoa.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/8 10:58
 * @Description: 结算实体
 */
@Data
public class SettlementVo {

    /**
     * 结算状态
     */
    private String settleStatus;
    /**
     * 里程碑
     */
    private String milestone;

    /**
     * 部门标识
     */
    private String deptId;

    /**
     * 承办人(上传人)
     */
    private String userId;

    /**
     * 合同标识
     */
    private String dealId;

    /**
     * 结算金额
     */
    private Double settleAmount;

    /**
     * 开户银行
     */
    private String settleBank;

    /**
     * 开户账号
     */
    private String settleAcount;

    /**
     * 上传时间
     */
    private Date createTime;

    /**
     * 应付款时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date payableTime;

    /**
     * 财务实际付款金额
     */
    private Double actualPayAmount;

    /**
     * 付款日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date actualPayTime;

    /**
     * 付款人
     */
    private String actualPayMan;

    /**
     * 财务付款说明
     */
    private String actualPayNotes;

    /**
     * 备注
     */
    private String notes;

    /**
     * 单位名称
     */
    private String contName2;

    /**
     * 纳税人识别号
     */
    private String orgNo;

    /**
     * 联系地址
     */
    private String contAddrss;

    /**
     * 联系电话
     */
    private String linkNum;
}

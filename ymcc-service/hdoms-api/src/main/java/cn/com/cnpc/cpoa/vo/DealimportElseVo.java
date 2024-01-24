package cn.com.cnpc.cpoa.vo;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
public class DealimportElseVo {
    /**
     * 合同名称
     */
    @Excel(name = "合同名称")
    private String dealName;
    /**
     * 合同编号
     */
    @Excel(name = "合同编号")
    private String dealNo;
    /**
     * 相对人标识，合同相对人
     */
    @Excel(name = "合作单位")
    private String contName;
    /**
     * 我方签订单位(
     * '四川宏大安全技术服务有限公司
     * )
     */
    @Excel(name = "签订单位")
    private String dealContract;

    /**
     * 合作单位社会性质
     */
    @Excel(name = "合作单位性质")
    private String subtypeId;


    /**
     * 选商方式
     */
    @Excel(name = "选商方式")
    private String dealSelection;

    /**
     * 合同标的
     */
    @Excel(name = "合同标的")
    private String dealNotes;

    /**
     * 标的金额
     */
    @Excel(name = "合同金额")
    private Double dealValue;

    /**
     * 是否暂定
     */
    @Excel(name = "是否暂定")
    private String istentative;

    /**
     * 履行金额
     */
    @Excel(name = "执行金额")
    private Double dealSettlement;
    /**
     * 履行金额
     */
    @Excel(name = "已付金额")
    private Double dealSettlements;
    /**
     * 承办人
     */
    @Excel(name = "经办人")
    private String userName;
    /**
     * 联系方式
     */
    @Excel(name = "联系方式")
    private String phone;

    /**
     * 是否完成
     */
    @Excel(name = "是否完成")
    private String isyn;

    /**
     * 签订时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Excel(name = "签订日期")
    private Date dealSignTime;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date createAt;

    /**日
     * 合同类别
     */
    @Excel(name = "合同类型")
    private String categoryName;
    /**日
     * 签约依据
     */
    @Excel(name = "签约依据")
    private String contract;
    /**
     * 备注
     */
    @Excel(name = "备注")
    private String remark;

    /**
     * 履行期限(始)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Excel(name = "履行开始日期")
    private Date dealStart;

    /**
     * 履行期限(止)
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    @Excel(name = "履行结束日期")
    private Date dealEnd;

    /**
     * 承办部门
     */
    private String deptName;
    private String deptId;
    private String contractId;
    private String userId;
    private String categoryId;
    private String frameDeal;
    /**
     * 资金流向(
     * 'INCOME   收入
     OUTCOME  支出
     NONE  不涉及',
     * )
     */

    private String dealIncome;
    private String dealReportNo;




}

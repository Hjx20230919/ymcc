package cn.com.cnpc.cpoa.po;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/5/9 20:42
 * @Description:流程中项目合同
 */
@Data
public class ActivitiItemDealPo {

    @Excel(name = "合同名称")
    private String dealName;

    @Excel(name = "合同类型")
    private String dealType;

    @Excel(name = "当前状态")
    private String dealStatus;

    @Excel(name = "资金流向")
    private String dealIncome;
    private String deptId;

   // private String settleAmount;
    private String dealId;

    @Excel(name = "承办单位")
    private String deptName;
    private String userId;

    @Excel(name = "承办人")
    private String userName;
    @Excel(name = "标的金额")
    private Double dealValue;

    @Excel(name = "履行期限(始)")
    private Date dealStart;

    @Excel(name = "履行期限(止)")
    private Date dealEnd;

    @Excel(name = "相对人")
    private String contName;

    private String dealNo;

}

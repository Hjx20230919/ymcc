package cn.com.cnpc.cpoa.vo;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/8 10:09
 * @Description: 合同实体
 */
@Data
public class DealOutTimeVo {

    @Excel(name = "超时天数")
    private String outTime;

    @Excel(name = "合同名称")
    private String dealName;

    @Excel(name = "合同类型")
    private String dealType;

    @Excel(name = "当前状态")
    private String dealStatus;

    @Excel(name = "资金流向")
    private String dealIncome;

    @Excel(name = "承办单位")
    private String deptName;

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




}

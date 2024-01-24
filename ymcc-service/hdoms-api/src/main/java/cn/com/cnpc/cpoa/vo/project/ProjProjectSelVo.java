package cn.com.cnpc.cpoa.vo.project;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:37
 * @Description:
 */
@Data
public class ProjProjectSelVo {

    @Excel(name = "项目名称")
    private String projName;
    @Excel(name = "选商方式")
    private String selContType;
    /**
     * 预计合同金额
     */
    @Excel(name = "预估金额")
    private BigDecimal dealValue;

    /**
     * 拟签订合同名称
     */
    @Excel(name = "拟签订合同名称")
    private String dealName;

    /**
     * 项目状态
     */
    @Excel(name = "项目状态")
    private String projStatus;


    /**
     * 项目阶段
     */
    @Excel(name = "项目阶段")
    private String projPhase;
    /**
     * 选商审批表ID
     */
    private String selContId;


    @Excel(name = "经办人")
    private String userName;

    @Excel(name = "经办部门")
    private String deptName;

    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    private String createAt;

    /**
     * 工程/服务名称
     */

    private String projId;



    private String notes;

    private String dealNo;



    private Integer attachNum;

    private String contQlyReq;

    private List<String> curAuditUser;

    private String planId;
    private String resultId;
}

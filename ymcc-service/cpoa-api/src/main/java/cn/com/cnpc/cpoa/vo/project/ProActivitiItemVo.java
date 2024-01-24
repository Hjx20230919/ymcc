package cn.com.cnpc.cpoa.vo.project;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import cn.com.cnpc.cpoa.vo.AttachVo;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 19:30
 * @Description:
 */
@Data
public class ProActivitiItemVo {


    /**
     * 工程/服务名称
     */
    @Excel(name = "工程/服务名称")
    private String projName;

    /**
     * 选商方式
     */
    @Excel(name = "选商方式")
    private String selContType;

    /**
     * 预计合同金额
     */
    @Excel(name = "预计合同金额")
    private BigDecimal dealValue;

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

    private List<String> curAuditUser;

    @Excel(name = "经办人")
    private String userName;

    @Excel(name = "经办部门")
    private String deptName;


    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    private String createAt;

    private String objId;

    private String objType;

    private String projId;

    private String selContId;

    private String planId;

    private String resultId;
}

package cn.com.cnpc.cpoa.vo.project;

import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:37
 * @Description:
 */
@Data
public class ProjProjectResultVo {

    /**
     * 选商审批表ID
     */
    private String resultId;

    private String planId;

    private String userName;

    private String deptName;

    private String selContType;

    /**
     * 工程/服务名称
     */
    private String projName;
    private String projId;
    /**
     * 预计合同金额
     */
    private BigDecimal dealValue;

    /**
     * 拟签订合同名称
     */
    private String dealName;


    /**
     * 项目状态
     */
    private String projStatus;

    /**
     * 项目阶段
     */
    private String projPhase;

    /**
     * 创建时间
     */
    private String createAt;

    private Integer attachNum;

    private String contQlyReq;

    private String resultNotes;

    private String amountUnit;

    private String planNo;

    private String selContId;

    private String dealNo;

    private List<String> curAuditUser;

    private String contId;

    /**
     * 1为隐藏，0为显示
     */
    private String freezeStatus;


}

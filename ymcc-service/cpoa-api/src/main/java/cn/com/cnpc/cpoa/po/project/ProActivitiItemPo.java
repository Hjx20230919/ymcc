package cn.com.cnpc.cpoa.po.project;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/5 17:47
 * @Description:
 */
@Data
public class ProActivitiItemPo {


    /**
     * 工程/服务名称
     */
    private String projName;

    /**
     * 选商方式
     */
    private String selContType;

    /**
     * 预计合同金额
     */
    private BigDecimal dealValue;

    /**
     * 项目状态
     */
    private String projStatus;

    /**
     * 项目阶段
     */
    private String projPhase;

    private List<String> curAuditUser;

    private String userName;

    private String deptName;


    /**
     * 创建时间
     */
    private Date createAt;

    private String objId;

    private String objType;

    private String userId;

    private String projId;

    private String selContId;

    private String planId;

    private String resultId;


}

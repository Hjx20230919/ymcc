package cn.com.cnpc.cpoa.vo.project;

import cn.com.cnpc.cpoa.vo.AttachVo;
import lombok.Data;

import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:22
 * @Description:
 */
@Data
public class ProjSelContVo {

    /**
     * 选商审批表ID
     */
    private String selContId;

    /**
     * 拟签订合同名称
     */
    private String dealName;

    /**
     * 立项申请ID
     */
    private String projId;

    /**
     * 附件份数
     */
    private Integer attachNum;

    /**
     * 经办人
     */
    private String createId;
    private String userName;

    /**
     * 经办部门
     */
    private String ownerDeptId;
    private String deptName;

    /**
     * 服务商资质要求简要描述
     */
    private String contQlyReq;

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

    /**
     * 备注
     */
    private String notes;

    private List<AttachVo> attachVos;

    private List<ProjContListVo> projContListVos;

    private String dealContract;
    private String selContType;
    private String projName;
}

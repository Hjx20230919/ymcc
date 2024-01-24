package cn.com.cnpc.cpoa.domain.project;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:01
 * @Description:选商审批表
 */
@Data
@Table(name = "T_PROJ_SEL_CONT")
public class BizProjSelContDto {

    /**
     * 选商审批表ID
     */
    @Id
    @Column(name = "SEL_CONT_ID")
    private String selContId;

    /**
     * 立项申请ID
     */
    @Column(name = "PROJ_ID")
    private String projId;

    /**
     * 附件份数
     */
    @Column(name = "ATTACH_NUM")
    private Integer attachNum;

    /**
     * 经办人
     */
    @Column(name = "CREATE_ID")
    private String createId;

    /**
     * 经办部门
     */
    @Column(name = "OWNER_DEPT_ID")
    private String ownerDeptId;

    /**
     * 服务商资质要求简要描述
     */
    @Column(name = "CONT_QLY_REQ")
    private String contQlyReq;

    /**
     * 项目状态
     */
    @Column(name = "PROJ_STATUS")
    private String projStatus;

    /**
     * 项目阶段
     */
    @Column(name = "PROJ_PHASE")
    private String projPhase;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_AT")
    private Date createAt;

    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;

    /**
     * 工程服务名称
     */
    @Transient
    private String projName;


}

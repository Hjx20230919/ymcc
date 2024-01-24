package cn.com.cnpc.cpoa.domain.project;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/12/9 11:03
 * @Description:
 */
@Data
@Table(name = "T_PROJ_PURC_PLAN_ATTACH")
public class BizProjPurcPlanAttachDto {


    /**
     * id
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 项目id
     */
    @Column(name = "PLAN_ID")
    private String planId;

    /**
     * 附件id
     */
    @Column(name = "ATTACH_ID")
    private String attachId;


}

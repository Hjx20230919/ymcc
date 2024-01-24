package cn.com.cnpc.cpoa.domain.project;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 19:09
 * @Description:
 */
@Data
@Table(name = "T_PROJ_SEL_CONT_ATTACH")
public class BizProjSelContAttachDto {

    /**
     * id
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 项目id
     */
    @Column(name = "SEL_CONT_ID")
    private String selContId;

    /**
     * 附件id
     */
    @Column(name = "ATTACH_ID")
    private String attachId;

}

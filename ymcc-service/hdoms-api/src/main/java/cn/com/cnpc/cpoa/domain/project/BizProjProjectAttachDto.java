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
@Table(name = "T_PROJ_PROJECT_ATTACH")
public class BizProjProjectAttachDto {

    /**
     * id
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 项目id
     */
    @Column(name = "PROJ_ID")
    private String projId;

    /**
     * 附件id
     */
    @Column(name = "ATTACH_ID")
    private String attachId;

}

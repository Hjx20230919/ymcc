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
@Table(name = "T_PROJ_PURC_RESULT_ATTACH")
public class BizProjPurcResultAttachDto {


    /**
     * id
     */
    @Id
    @Column(name = "ID")
    private String id;

    /**
     * 项目id
     */
    @Column(name = "RESULT_ID")
    private String resultId;

    /**
     * 附件id
     */
    @Column(name = "ATTACH_ID")
    private String attachId;


}

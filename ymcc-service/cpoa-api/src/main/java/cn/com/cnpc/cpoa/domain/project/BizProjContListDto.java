package cn.com.cnpc.cpoa.domain.project;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @Author: 17742856263
 * @Date: 2019/12/9 11:03
 * @Description:
 */
@Data
@Table(name = "T_PROJ_CONT_LIST")
public class BizProjContListDto {


    /**
     * 选商表ID
     */
    @Id
    @Column(name = "CONT_LIST_ID")
    private String contListId;

    /**
     * 选商审批表ID
     */
    @Column(name = "SEL_CONT_ID")
    private String selContId;

    /**
     * 承包商标识
     */
    @Column(name = "CONT_ID")
    private String contId;

    /**
     * 准入类型
     */
    @Column(name = "ACCESS_TYPE")
    private String accessType;

    /**
     * 准入范围
     */
    @Column(name = "ACCESS_SCOPE")
    private String accessScope;

    /**
     * 备注
     */
    @Column(name = "NOTES")
    private String notes;

    @Transient
    private String contName;

    @Transient
    private String accessLevel;


}

package cn.com.cnpc.cpoa.vo.project;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/12/9 11:11
 * @Description:
 */
@Data
public class ProjContListVo {


    /**
     * 选商表ID
     */
    private String contListId;

    /**
     * 选商审批表ID
     */
    private String selContId;

    /**
     * 承包商标识
     */
    private String contId;

    /**
     * 承包商名称
     */
    private String contName;

    /**
     * 准入类型
     */
    private String accessType;

    /**
     * 准入范围
     */
    private String accessScope;

    /**
     * 备注
     */
    private String notes;

    private String accessLevel;
}

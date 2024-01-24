package cn.com.cnpc.cpoa.vo.contractor.data;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/10/20 11:22
 * @Description:
 */
@Data
public class ContAccessProjData {

    /**
     * 项目名称
     */
    private String projName;


    /**
     * 是否销项（0 否 1 是）
     */
    private String isDeleteProj;
    /**
     * 选中标志（0 不选中 1 选中）
     */
    private String isCheck;


    private String projContent;

    private String projAccessType;

}

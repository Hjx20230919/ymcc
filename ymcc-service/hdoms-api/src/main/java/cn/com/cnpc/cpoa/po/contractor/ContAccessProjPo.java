package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/20 10:25
 * @Description:
 */
@Data
public class ContAccessProjPo {

    /**
     * 准入项目标识
     */
    private String projId;

    /**
     * 准入申请标识
     */
    private String acceId;

    /**
     * 承包商标识
     */
    private String contId;

    /**
     * 项目名称
     */
    private String projName;

    /**
     * 项目状态
     */
    private String projState;

    /**s
     * 准入申请标识
     */
    private Date acceLimitTime;

    private String contName;

    private String projContent;

    private String projAccessType;

}
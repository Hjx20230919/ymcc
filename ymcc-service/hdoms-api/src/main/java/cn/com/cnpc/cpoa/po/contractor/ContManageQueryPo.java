package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/20 8:29
 * @Description:
 */
@Data
public class ContManageQueryPo {

    /**
     * 承包商名称
     */
    private String contName;

    /**
     * 承包商组织机构代码
     */
    private String contOrgNo;

    /**
     * 法定代表人姓名
     */
    private String corporate;

    /**
     * 承包商联系电话
     */
    private String linkMobile;

    private String userName;

    private String deptName;

    /**
     * 承包商状态
     */
    private String contState;


    private String content;

    private Date acessDate;

    private String contId;

    /**
     * 承包商冻结状态
     */
    private String contFreezeState;

    //全量专业类别
    private String projContent;

    private String projAccessType;
    private String checkResult;
    private Date checkAt;
    private String accessNo;
    private String accessLevel;
    private Date acceStateAt;
    private String deptId;
    private String projId;
    private String scopeName;

    /**
     * 是否是黑名单,1为黑名单，0为正常
     */
    private Integer isRelieve;

}

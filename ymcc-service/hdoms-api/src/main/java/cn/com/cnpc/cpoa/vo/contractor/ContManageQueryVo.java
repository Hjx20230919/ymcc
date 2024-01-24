package cn.com.cnpc.cpoa.vo.contractor;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 9:14
 * @Description:综合管理查询实体
 */
@Data
public class ContManageQueryVo {


    private String contId;
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

    /**
     * 是否是黑名单,1为黑名单，0为正常
     */
    private Integer isRelieve;


    private String content;


    private String acessDate;

    /**
     * 承包商冻结状态
     */
    private String contFreezeState;
//    private String acessCode;
//
//    private String acessUrl;

    private String projAccessType;
    private String checkResult;
    private String checkAt;

    //上次审核时间
    private String preCheckAt;

    private String accessNo;
    private String accessLevel;

    private String deptId;

    private String projId;

    private  String scopeName;



}

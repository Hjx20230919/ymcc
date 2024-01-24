package cn.com.cnpc.cpoa.vo;

import lombok.Data;


import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/3/23 15:13
 * @Description:合同相对人
 */
@Data
public class ContractorVo {


    /**
     * 相对人名称
     */
    private String contName;

    /**
     * 相对人简称
     */
    private String aliasName;


    /**
     * 合同相对人性质
     */
    private String contType;

    /**
     * 相对人地址
     */
    private String contAddrss;

    /**
     * 身份证号码
     */
    private String idNo;

    /**
     * 组织机构代码
     */
    private String orgNo;

    /**
     * 负责人
     */
    private String dutyMan;


    /**
     * 注册资本
     */
    private String regCaptial;

    /**
     * 税务登记证号
     */
    private String regCertNum;

    /**
     * 传真
     */
    private String fax;

    /**
     * 联系人
     */
    private String linkman;
    /**
     * 联系电话
     */
    private String linkNum;


    /**
     * 开户银行
     */
    private String contBank;

    /**
     * 开户账号
     */
    private String contAccount;


    /**
     * 电子邮件
     */
    private String linkMail;

    /**
     * 经营范围
     */
    private String bizScope;


    /**
     * 入库时间
     */
    private Date createTime;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 备注
     */
    private String notes;

    // marketType
    private String marketType;

    // companyType
    private String companyType;

    // 交易类型
    private String contractType;

    // 作业区域
    private String workZone;

    // 所属单位
    private String parentName;
}

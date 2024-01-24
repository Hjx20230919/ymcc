package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

import java.util.Date;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 20:38
 * @Description:
 */
@Data
public class ContContractorImportVo {


    /**
     * 承包商名称
     */
    @Excel(name = "承包商名称")
    private String contName;


    /**
     * 准入级别（集团准入(JTZR)
     * 川庆准入(CQZR)
     * 院准入(TZR)）
     */
    @Excel(name = "准入级别")
    private String accessLevel;


    /**
     * 准入编号
     */
    @Excel(name = "准入编号")
    private String accessNo;



    /**
     * 专业类别
     */
    @Excel(name = "专业类别")
    private String projName;

    /**
     * 准入范围
     */
    @Excel(name = "准入范围")
    private String scopeName;


    /**
     * 准入类型
     */
    @Excel(name = "准入类型")
    private String projAccesstype;

    @Excel(name = "推荐单位")
    private String deptName;
    private String deptId;
    /**
     * 税务登记号
     */
//    @Excel(name = "税务登记号")
    private String contTaxNo;

    /**
     * 组织机构代码
     */
    @Excel(name = "统一社会信用代码")
    private String contOrgNo;

    /**
     * 经营范围
     */
    @Excel(name = "经营范围")
    private String contScope;

    /**
     * 注册资金
     */
    @Excel(name = "注册资金（万元）")
    private String contRegCaptial;

    /**
     * 法人代表
     */
    @Excel(name = "法人代表")
    private String corporate;

    /**
     * 联系人
     */
    @Excel(name = "联系人姓名")
    private String linkman;

    /**
     * 联系人电话
     */
    @Excel(name = "联系电话")
    private String linkMobile;


    /**
     * 准入时间
     */
    @Excel(name = "准入时间")
    private Date acceStateAt;

}

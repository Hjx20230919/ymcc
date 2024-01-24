package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2020/7/26 9:22
 * @Description:
 */
@Data
public class ContCreditMaintainExportVo {

    @Excel(name = "承包商名称")
    private String contName;

    @Excel(name = "组织机构代码")
    private String contOrgNo;

    @Excel(name = "法人")
    private String corporate;

    @Excel(name = "承包商电话")
    private String linkMobile;

    @Excel(name = "准入级别")
    private String accessLevel;

    @Excel(name = "经办人")
    private String userName;

    @Excel(name = "经办部门")
    private String deptName;


    @Excel(name = "准入状态")
    private String acceState;

    @Excel(name = "变更状态")
    private String setState;


    @Excel(name = "是否冻结")
    private String contFreezeState;

    @Excel(name = "专业类别")
    private String content;

    /**
     * 项目申请状态时间
     */
    @Excel(name = "准入时间")
    private String projStateAt;


    @Excel(name = "更新地址")
    private String setUrl;

    @Excel(name = "更新码")
    private String setCode;


}

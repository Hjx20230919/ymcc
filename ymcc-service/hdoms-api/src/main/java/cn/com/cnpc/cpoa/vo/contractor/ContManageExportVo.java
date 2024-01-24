package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Auther: hukun
 * @Date: 2022/03/28/20:04
 * @Description:
 */
@Data
public class ContManageExportVo {

    /**
     * 承包商名称
     */
    @Excel(name = "承包商名称")
    private String contName;

    /**
     * 承包商组织机构代码
     */
    @Excel(name = "组织机构代码")
    private String contOrgNo;


    @Excel(name = "准入级别")
    private String accessLevel;


    /**
     * 法定代表人姓名
     */
    @Excel(name = "经办人")
    private String corporate;


    @Excel(name = "经办部门")
    private String deptName;


    /**
     * 承包商状态
     */
    @Excel(name = "当前状态")
    private String contState;

    /**
     * 承包商冻结状态
     */
    @Excel(name = "是否冻结")
    private String contFreezeState;

    @Excel(name = "专业类别")
    private String content;

    @Excel(name = "准入时间")
    private String acessDate;
}

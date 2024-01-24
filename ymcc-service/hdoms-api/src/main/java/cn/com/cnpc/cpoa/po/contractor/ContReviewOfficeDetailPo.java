package cn.com.cnpc.cpoa.po.contractor;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-30  17:02
 * @Description:
 * @Version: 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContReviewOfficeDetailPo {
    private String officeDetailId;

    private String createId;

    @Excel(name = "考评时间")
    private Date createAt;

    /**
     * 待考评   draft
     考评中  reviewing
     审核中  buildAuditing
     退回   back
     考评完成  down


     ("draft","草稿"),("back","退回"),("buildAuditing","审核中"),("down","审核完毕")
     */
    @Excel(name = "任务状态")
    private String taskStatus;

    @Excel(name = "考核年")
    private String reviewYear;

    private String contId;

    private String ownerDeptId;

    @Excel(name = "考评得分")
    private Float totalScore;

    @Excel(name = "比重")
    private Float proportion;

    private Float conversionScore;

    @Excel(name = "扣分原因")
    private String evalConclusion;

    @Excel(name = "评价日期")
    private Date evalAt;

    @Excel(name = "评价人员")
    private String evalMan;

    @Excel(name = "备注")
    private String notes;

    @Excel(name = "考核部门")
    private String deptName;
    private String manId;
    private String stepId;
    @Excel(name = "承包商名称")
    private String contName;
    @Excel(name = "准入级别")
    private String accessLevel;
}

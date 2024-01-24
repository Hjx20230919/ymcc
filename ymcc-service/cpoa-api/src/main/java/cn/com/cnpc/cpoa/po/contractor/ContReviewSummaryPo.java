package cn.com.cnpc.cpoa.po.contractor;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-01  16:44
 * @Description:
 * @Version: 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ContReviewSummaryPo {
    private String reviewSummaryId;

    private String createId;

    @Excel(name = "创建时间")
    private Date createAt;

    /**
     * 待考评   draft
     考评中  reviewing
     考评完成  down

     */
    @Excel(name = "考评状态")
    private String taskStatus;

    @Excel(name = "考核年")
    private String reviewYear;

    private String contId;

    private String ownerDeptId;

    @Excel(name = "基层得分")
    private Float basicScore;

    @Excel(name = "质安得分")
    private Float qhseScore;

    @Excel(name = "生产得分")
    private Float productionScore;

    @Excel(name = "企管得分")
    private Float omScore;

    @Excel(name = "财资得分")
    private Float financialScore;

    @Excel(name = "最终得分")
    private Float totalScore;

    /**
     * 90以上     优秀
     */
    @Excel(name = "评价结果")
    private String evalConclusion;

    private String notes;

    @Excel(name = "承包商名称")
    private String contName;
    @Excel(name = "考评部门")
    private String deptName;

    /**
     * 评价项目数量
     */
    private String projCount;

    /**
     * 完成合同金额
     */
    private Double totalFinishValue;
}

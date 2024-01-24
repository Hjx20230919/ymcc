package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewDetailDto;
import cn.com.cnpc.cpoa.vo.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-26  16:42
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContReviewTaskVo {

    private String reviewTaskId;

    private String createId;
    @Excel(name = "考评时间")
    private Date createAt;

    /**
     * -- 待考评，考评任务表中，无对应的项目
     -- 下面三个状态都是考评单位内部操作，对项目来说，都叫“考评中”
     考评中  reviewing
     审核中  buildAuditing
     退回   back

     -- 基础单位提交完成，考评结算
     考评完成  down

     -- 未完成待考评项目，也要写入一个考评任务数据
     未完成待考评项目     wait_review

     -- 其他不考评项目，也要写入一个考评任务数据
     不考评     no_review

     */
    @Excel(name = "考评状态")
    private String taskStatus;
    @Excel(name = "考核年份")
    private String reviewYear;

    private String contId;

    private String ownerDeptId;

    private String projId;

    /**
     * 默认是采购结果金额，管理员可以修改
     */
    @Excel(name = "合同金额")
    private Float dealValue;

    /**
     * 基层单位填写
     */
    @Excel(name = "当年完成金额")
    private Float dealFinishValue;

    /**
     * 默认：准入类型

     问题以前数据，在选商时没有设置准入类型，问题：如果一个承包商有多个专业类别，就需要手动选择
     */
    @Excel(name = "专业类别")
    private String accName;

    /**
     * 也准入类别一致
     */
    @Excel(name = "准入级别")
    private String accType;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "实际竣工时间")
    private Date finishAt;

    private String ieDesc;
    @Excel(name = "总分")
    private Float totalScore;
    @Excel(name = "拆算得分")
    private Float conversionScore;
    @Excel(name = "评价结论")
    private String evalConclusion;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    @Excel(name = "评价时间")
    private Date evalAt;
    @Excel(name = "评价人员")
    private String evalMan;

    private String notes;
    @Excel(name = "承包商名称")
    private String contName;
    @Excel(name = "项目名称")
    private String projName;
    @Excel(name = "考评部门")
    private String deptName;

    private List<ContReviewDetailDto> basicList;
    private List<ContReviewDetailDto> achieList;
    private List<AttachVo> attachVos;
}

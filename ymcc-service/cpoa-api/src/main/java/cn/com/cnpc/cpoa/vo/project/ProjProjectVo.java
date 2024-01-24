package cn.com.cnpc.cpoa.vo.project;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import cn.com.cnpc.cpoa.vo.AttachVo;
import lombok.Data;

import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 19:30
 * @Description:
 */
@Data
public class ProjProjectVo {

    /**
     * 立项项目ID
     */
    private String projId;

    /**
     * 经办人
     */
    private String createId;

    /**
     * 经办部门
     */
    private String ownerDeptId;

    /**
     * 工程/服务名称
     */
    @Excel(name = "项目名称")
    private String projName;
    /**
     * 选商方式
     */
    @Excel(name = "选商方式")
    private String selContType;

    /**
     * 预计合同金额
     */
    @Excel(name = "预估金额")
    private BigDecimal dealValue;

    @Excel(name = "控制总价")
    private BigDecimal totalValue;

    /**
     * 拟签订合同名称
     */
    @Excel(name = "拟签订合同名称")
    private String dealName;

    /**
     * 项目状态
     */
    @Excel(name = "项目状态")
    private String projStatus;

    /**
     * 项目阶段
     */
    @Excel(name = "项目阶段")
    private String projPhase;


    /**
     * 编号
     */
    private String dealNo;

    /**
     * 我方签约单位
     */
    private String dealContract;

    /**
     * 资金支出渠道
     */
    private String payType;



    /**
     * 对应收入情况
     */
    private String incomeInfo;

    /**
     * 服务合同立项原因描述
     */
    private String applDesc;

    /**
     * 承包商资质要求
     */
    private String contQlyReq;

    /**
     * 承包商技术服务人员要求
     */
    private String contSvrReq;

    /**
     * 预计合同金额测算情况
     */
    private String dealValueMeasure;






    /**
     * 备注
     */
    private String notes;

    private String projNotes;

    @Excel(name = "经办人")
    private String userName;

    @Excel(name = "经办部门")
    private String deptName;
    @Excel(name = "计划编号")
    private String projPlanNo;


    /**
     * 创建时间
     */
    @Excel(name = "创建时间")
    private String createAt;


    private List<String> curAuditUser;

    private List<AttachVo> attachVos;

    private String selContId;
    private String planId;
    private String resultId;


    private String responsibilityMan;
    private String responsibilityPhone;


    private List<ProjContListVo> projContListVos;



    /***公开招标*/
    private String otenderId;
    /**
     * 工程项目/物资采购/服务
     */
    private String otenderProjType;

    private String otenderPlanNo;

    /**
     * 一类 二类 三类 四类
     */
    private String otenderType;

    /**
     * 公开招标/邀请招标
     */
    private String otenderTenderType;

    private String otenderStartDate;

    private String otenderEndDate;

    private String otenderValutionType;

    private String otenderModality;

    private String otenderSupervise;

    private String otenderContent;

    private String otenderCommittee;

    private String otenderQualifications;


    /**可不招标**/

    private String ntenderId;
    /**
     * 工程项目/物资采购/服务
     */
    private String ntenderProjType;

    private String ntenderPlanNo;

    /**
     * 一类 二类 三类 四类
     */
    private String ntenderType;

    private String ntenderPurchaseType;

    private String ntenderStartDate;

    private String ntenderEndDate;

    private String ntenderValutionType;

    private String ntenderGroup;

    private String ntenderSummary;

    private String ntenderReason;

    private String ntenderSupervise;

    /**
     * 承包商参与项目状态
     */
    @Transient
    private String joinStatus;

    /**
     * 参与项目承包商名称
     */
    @Transient
    private String joinName;

    /**
     * 承包商中标项目状态
     */
    @Transient
    private String bidStatus;

    /**
     * 中标承包商名称
     */
    @Transient
    private String bidName;



}

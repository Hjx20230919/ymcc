package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/11/4 19:37
 * @Description:年度考评导入实体
 */
@Data
public class YearEvaluationVo {

    @Excel(name = "序号")
    private Integer num;

    @Excel(name = "承包商名称")
    private String contName;

    @Excel(name = "基层单位业绩考核加权平均分")
    private Double weightMark;

    @Excel(name = "基层单位考核分")
    private Double baseMark;

    @Excel(name = "市场生产部考核分")
    private Double marketMark;

    @Excel(name = "质安部考核分")
    private Double securityMark;

    @Excel(name = "人劳部考核分")
    private Double labourMark;

    @Excel(name = "财资部考核分")
    private Double financeMark;

    @Excel(name = "企管部考核分")
    private Double empMark;

    @Excel(name = "综合得分")
    private Double comprehensiveMark;

    @Excel(name = "评价结论")
    private String conclusion;

}

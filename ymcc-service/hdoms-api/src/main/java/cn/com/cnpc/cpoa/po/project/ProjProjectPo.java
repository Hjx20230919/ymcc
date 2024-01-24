package cn.com.cnpc.cpoa.po.project;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-26  15:49
 * @Description: TODO
 * @Version: 1.0
 */
@Data
public class ProjProjectPo {
    private String resultId;
    private String projId;
    private String projName;
    //项目年份
    private String projAt;
    //承包商名称
    private String contId;
    //准入级别
    private String accessLevel;
    //专业类别
    private String projContent;
    //准入级别
    private String projAccessType;
    private String ownerDeptId;
    //合同结果金额
    private BigDecimal limitTotalPrice;

}

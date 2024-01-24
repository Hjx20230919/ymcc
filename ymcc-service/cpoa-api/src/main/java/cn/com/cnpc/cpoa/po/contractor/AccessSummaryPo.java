package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-06-02  11:03
 * @Description:
 * @Version: 1.0
 */
@Data
public class AccessSummaryPo {
    /**
     *评价项目总数
     */
    private String total;
    /**
     *优秀
     */
    private String excellent;
    /**
     *合格
     */
    private String qualified;
    /**
     *限期整改
     */
    private String rectification;
    /**
     *不合格
     */
    private String disqualified;
    private String ownerDeptId;
    /**
     *评价部门
     */
    private String deptName;
}

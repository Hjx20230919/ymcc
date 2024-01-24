package cn.com.cnpc.cpoa.po.bid;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.Data;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  10:55
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidBizOptPo {

    private String bizOptId;

    @Excel(name = "商机名称")
    private String optName;

    private String deptId;

    @Excel(name = "上传人员")
    private String creator;

    @Excel(name = "上传时间")
    private String createAt;

    @Excel(name = "客户名称")
    private String contName;

    @Excel(name = "商机描述")
    private String bizOptDesc;

    private String nextRemindAt;

    private String remindCtx;

    /**
     * 进行中  Ongoing
     关闭     Closed

     */
    private String bizOptStatus;

    private String reserve1;

    private String reserve2;

    private String reserve3;

    private String notes;

    private String deptName;


}

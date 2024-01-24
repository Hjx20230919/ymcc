package cn.com.cnpc.cpoa.po.bid;

import cn.com.cnpc.cpoa.common.annotation.Excel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-19  15:00
 * @Description:
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidCertiPo {


    private String userCertiId;

    @Excel(name = "所属单位")
    private String companyName;

    /**
     * 高压电工
     低压电工
     防爆电器
     熔化焊接与热切割
     压力焊
     钎焊
     登高架设
     高处安装、
     维护、
     拆除
     其他
     .....可以自定义输入

     */
    @Excel(name = "证书类别")
    private String certiType;

    @Excel(name = "证书名称")
    private String certiName;

    @Excel(name = "证书编号")
    private String certiCode;

    @Excel(name = "证书等级")
    private String certiLevel;

    @Excel(name = "发证日期")
    private String issueDate;

    @Excel(name = "到期日期")
    private String dueDate;

    @Excel(name = "发证机构")
    private String certiAuth;

    private Float auditCycle;

    private String auditDatePre;

    private String auditDateNext;

    /**
     * 到期前多少天提醒
     */
    private Integer alartDays;

    private String notes;
}

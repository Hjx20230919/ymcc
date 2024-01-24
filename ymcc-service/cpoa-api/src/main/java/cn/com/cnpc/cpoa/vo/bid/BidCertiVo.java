package cn.com.cnpc.cpoa.vo.bid;

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
 * @CreateTime: 2022-07-19  15:19
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidCertiVo {
    private String userCertiId;


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

    private String certiType;

    private String certiName;

    private String certiCode;

    private String certiLevel;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date issueDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date dueDate;

    private String certiAuth;

    private Float auditCycle;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date auditDatePre;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date auditDateNext;

    /**
     * 到期前多少天提醒
     */
    private Integer alartDays;

    private String notes;

    private List<AttachVo> attachVos;
}

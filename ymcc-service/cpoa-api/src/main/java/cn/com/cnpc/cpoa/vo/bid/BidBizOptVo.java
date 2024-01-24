package cn.com.cnpc.cpoa.vo.bid;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  10:58
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidBizOptVo {
    private String bizOptId;

    private String optName;

    private String deptId;

    private String creator;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date createAt;

    private String contName;

    private String bizOptDesc;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date nextRemindAt;

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

    List<BidBizOptTrackVo> optTrackVos;
}

package cn.com.cnpc.cpoa.vo.bid;

import cn.com.cnpc.cpoa.vo.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  10:59
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidBizOptTrackVo {
    private String bizOptTrackId;

    private String bizOptId;

    private String deptId;

    private String creator;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
    private Date createAt;

    private String trackDesc;

    private String reserve1;

    private String reserve2;

    private String reserve3;

    private String notes;

    private List<AttachVo> attachVos;
}

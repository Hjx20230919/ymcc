package cn.com.cnpc.cpoa.po.bid;

import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.vo.AttachVo;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  10:56
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidBizOptTrackPo {

    private String bizOptTrackId;

    private String bizOptId;

    private String deptId;

    private String creator;

    private String createAt;

    private String trackDesc;

    private String reserve1;

    private String reserve2;

    private String reserve3;

    private String notes;

    private List<BizAttachDto> attachVos;

}

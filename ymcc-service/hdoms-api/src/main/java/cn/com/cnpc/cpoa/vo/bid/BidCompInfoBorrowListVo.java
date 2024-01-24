package cn.com.cnpc.cpoa.vo.bid;

import cn.com.cnpc.cpoa.vo.AttachVo;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-21  09:12
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidCompInfoBorrowListVo {
    private String compInfoBorrowListId;

    private String certiBorrowId;

    private String compInfoType;

    private String deptId;

    private String compInfoReq;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date lastAt;

    private String notes;

    private AttachVo attachVo;
}

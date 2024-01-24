package cn.com.cnpc.cpoa.vo.bid;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  09:02
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidCompetitorVo {
    private String competitorId;

    private String compName;

    private String copmAddr;

    private String compType;

    private String regAmount;

    private String compScale;

    private String mainAdv;

    private String curPerf;

    private String compAnal;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date createAt;

    private String creator;

    private String notes;
}

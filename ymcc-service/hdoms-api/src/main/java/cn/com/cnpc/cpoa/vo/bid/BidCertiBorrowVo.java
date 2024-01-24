package cn.com.cnpc.cpoa.vo.bid;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-21  08:53
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidCertiBorrowVo {

    private String certiBorrowId;

    private String bidProjId;

    private String deptId;

    private String borrowMan;

    private String borrowReason;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date borrowStartAt;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd")
    private Date borrowEndAt;

    private String watermarkContent;

    private String checkMan;

    /**
     * 不同意   Disagree
     同意       Agree
     */
    private String checkStatus;

    private String notes;

    /**
     * 企业资质id
     */
    private List<String> userCertiIds;

    /**
     * 企业资料
     */
    private List<BidCompInfoBorrowListVo> compInfoBorrowListVos;
}

package cn.com.cnpc.cpoa.po.bid;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-21  09:12
 * @Description:
 * @Version: 1.0
 */
@Data
public class BidCompInfoBorrowListPo {

    private String compInfoBorrowListId;

    private String certiBorrowId;

    private String compInfoType;

    private String deptId;

    private String compInfoReq;

    private String lastAt;

    private String notes;
}

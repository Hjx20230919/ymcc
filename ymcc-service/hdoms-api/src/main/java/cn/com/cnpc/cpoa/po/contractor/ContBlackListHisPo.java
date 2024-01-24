package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-06  15:53
 * @Description:
 * @Version: 1.0
 */
@Data
public class ContBlackListHisPo {

    private String blacklistHisId;

    private String blacklistId;

    private String contName;

    private String contIacNo;

    private String contTaxNo;

    private String contOrgNo;

    private String accessLevel;

    private String blackReason;

    private String blackAt;

    private String blackMan;

    private Integer isRelieve;

    private String relieveReason;

    private String relieveAt;

    private String relieveMan;

    private String notes;
}

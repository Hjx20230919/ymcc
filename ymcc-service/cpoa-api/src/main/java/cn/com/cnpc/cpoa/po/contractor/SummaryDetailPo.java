package cn.com.cnpc.cpoa.po.contractor;

import lombok.Data;

import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-25  18:50
 * @Description:
 * @Version: 1.0
 */
@Data
public class SummaryDetailPo {

    private List<ContReviewOfficeDetailPo> officeDetailPos;

    private List<ContReviewTaskPo> contReviewTaskPos;
}

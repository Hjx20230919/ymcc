package cn.com.cnpc.cpoa.vo.contractor;

import cn.com.cnpc.cpoa.domain.contractor.ContReviewDetailDto;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTiDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-26  09:15
 * @Description: TODO
 * @Version: 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContReviewDetailVo {
    private List<ContReviewDetailDto> basicList;
    private List<ContReviewDetailDto> achieList;
}

package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewDetailDto;
import cn.com.cnpc.cpoa.mapper.contractor.ContReviewDetailDtoMapper;
import cn.com.cnpc.cpoa.vo.contractor.ContReviewDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-05-26  18:49
 * @Description: TODO
 * @Version: 1.0
 */
@Service
public class ContReviewDetailService extends AppService<ContReviewDetailDto> {

    @Autowired
    private ContReviewDetailDtoMapper reviewDetailDtoMapper;

    /**
     * 根据任务id查询所有考评任务明细
     * @return
     */
    public ContReviewDetailVo selectAllReviewDetail(String reviewTaskId) {
        HashMap<String, Object> basicMap = new HashMap<>(16);
        basicMap.put("reviewType","Basic");
        basicMap.put("reviewTaskId",reviewTaskId);
        List<ContReviewDetailDto> basicList = reviewDetailDtoMapper.selectContReviewDetail(basicMap);
        HashMap<String, Object> achieMap = new HashMap<>(16);
        achieMap.put("reviewType","Achie");
        achieMap.put("reviewTaskId",reviewTaskId);
        List<ContReviewDetailDto> achieList = reviewDetailDtoMapper.selectContReviewDetail(achieMap);
        ContReviewDetailVo reviewDetailVo = ContReviewDetailVo.builder().basicList(basicList).achieList(achieList).build();
        return reviewDetailVo;
    }
}

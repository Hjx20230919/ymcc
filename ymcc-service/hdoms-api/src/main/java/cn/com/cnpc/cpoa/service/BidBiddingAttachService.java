package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingAttachDto;
import cn.com.cnpc.cpoa.mapper.bid.BidBiddingAttachDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-06  15:40
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidBiddingAttachService extends AppService<BidBiddingAttachDto> {

    @Autowired
    private BidBiddingAttachDtoMapper biddingAttachDtoMapper;

    public List<BidBiddingAttachDto> selectAttachDto(Map<String, Object> param) {
        return biddingAttachDtoMapper.selectAttachDto(param);
    }

    public void deleteByMap(Map<String, String> allMap) {
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            //2 删除资质附件记录
            if (1 != delete(entry.getValue())) {
                throw new AppException("删除附件记录出错！");
            }
        }
    }
}

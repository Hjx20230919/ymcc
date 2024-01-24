package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingDeptDto;
import cn.com.cnpc.cpoa.mapper.bid.BidBiddingDeptDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-04  17:16
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidBiddingDeptService extends AppService<BidBiddingDeptDto> {

    @Autowired
    private BidBiddingDeptDtoMapper biddingDeptDtoMapper;

    public List<BidBiddingDeptDto> selectBiddingDeptByMap(Map<String,Object> param) {
        return biddingDeptDtoMapper.selectBiddingDeptByMap(param);
    }
}

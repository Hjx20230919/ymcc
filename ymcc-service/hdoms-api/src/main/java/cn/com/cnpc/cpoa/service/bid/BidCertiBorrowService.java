package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.bid.BidCertiBorrowDto;
import cn.com.cnpc.cpoa.mapper.bid.BidCertiBorrowDtoMapper;
import cn.com.cnpc.cpoa.po.bid.BidCertiBorrowPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-21  08:55
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidCertiBorrowService extends AppService<BidCertiBorrowDto> {

    @Autowired
    private BidCertiBorrowDtoMapper certiBorrowDtoMapper;

    public BidCertiBorrowPo selectByBidProjId(String bidProjId) {
        return certiBorrowDtoMapper.selectByBidProjId(bidProjId);
    }

    public List<BidCertiBorrowPo> selectCertiBorrow(Map<String,Object> param) {
        return certiBorrowDtoMapper.selectCertiBorrow(param);
    }

}

package cn.com.cnpc.cpoa.service.bid;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.bid.BidBizOptTrackDto;
import cn.com.cnpc.cpoa.mapper.bid.BidBizOptTrackDtoMapper;
import cn.com.cnpc.cpoa.po.bid.BidBizOptTrackPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: Yuxq
 * @CreateTime: 2022-07-20  11:02
 * @Description:
 * @Version: 1.0
 */
@Service
public class BidBizOptTrackService extends AppService<BidBizOptTrackDto> {

    @Autowired
    private BidBizOptTrackDtoMapper optTrackDtoMapper;

    public List<BidBizOptTrackPo> selectByMap(Map<String,Object> param) {
        return optTrackDtoMapper.selectByMap(param);
    }
}

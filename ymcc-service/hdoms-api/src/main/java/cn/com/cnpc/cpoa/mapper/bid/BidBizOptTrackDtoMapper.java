package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidBizOptTrackDto;
import cn.com.cnpc.cpoa.po.bid.BidBizOptTrackPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidBizOptTrackDtoMapper extends AppMapper<BidBizOptTrackDto> {

    List<BidBizOptTrackPo> selectByMap(Map<String,Object> param);
}

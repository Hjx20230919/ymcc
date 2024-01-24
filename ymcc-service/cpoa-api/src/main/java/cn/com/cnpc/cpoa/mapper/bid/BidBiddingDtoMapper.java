package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingDto;
import cn.com.cnpc.cpoa.po.bid.BidBiddingPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidBiddingDtoMapper extends AppMapper<BidBiddingDto> {

    List<String> selectProjNo();

    List<BidBiddingPo> selectBidding(Map<String,Object> params);

    List<BidBiddingPo> selectAuditBIdding(Map<String,Object> params);
}

package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidCompetitorDto;
import cn.com.cnpc.cpoa.po.bid.BidCompetitorPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BidCompetitorDtoMapper extends AppMapper<BidCompetitorDto> {

    List<BidCompetitorPo> selectAllByMap(HashMap<String, Object> params);

    List<BidCompetitorPo> selectByBidProjId(@Param("bidProjId")String bidProjId);
}

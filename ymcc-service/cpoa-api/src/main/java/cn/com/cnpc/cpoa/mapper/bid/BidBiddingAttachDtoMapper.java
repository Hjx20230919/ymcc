package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidBiddingAttachDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcResultAttachDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidBiddingAttachDtoMapper extends AppMapper<BidBiddingAttachDto> {

    List<BidBiddingAttachDto> selectAttachDto(Map<String, Object> param);
}

package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidBizOptDto;
import cn.com.cnpc.cpoa.po.bid.BidBizOptPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Mapper
public interface BidBizOptDtoMapper extends AppMapper<BidBizOptDto> {

    List<BidBizOptPo> selectAllByMap(HashMap<String, Object> params);
}

package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidKeywordDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidKeywordDtoMapper extends AppMapper<BidKeywordDto> {

    List<BidKeywordDto> selectAllByMap(Map<String,Object> param);
}

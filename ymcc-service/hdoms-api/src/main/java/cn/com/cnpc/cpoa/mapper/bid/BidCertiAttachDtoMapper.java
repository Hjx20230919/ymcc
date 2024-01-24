package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidCertiAttachDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidCertiAttachDtoMapper extends AppMapper<BidCertiAttachDto> {
    List<BidCertiAttachDto> selectAttachDto(Map<String, Object> param);
}

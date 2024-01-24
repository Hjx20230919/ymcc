package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidProjectAttachDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidProjectAttachDtoMapper extends AppMapper<BidProjectAttachDto> {

    List<BidProjectAttachDto> selectAttachDto(Map<String, Object> param);

    String selectByAttachId(@Param("attachId")String attachId);
}

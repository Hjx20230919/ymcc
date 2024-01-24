package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidCertiBorrowDto;
import cn.com.cnpc.cpoa.po.bid.BidCertiBorrowPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidCertiBorrowDtoMapper extends AppMapper<BidCertiBorrowDto> {

    BidCertiBorrowPo selectByBidProjId(@Param("bidProjId") String bidProjId);

    List<BidCertiBorrowPo> selectCertiBorrow(Map<String,Object> param);
}

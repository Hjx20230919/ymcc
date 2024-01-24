package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidCertiAttachDto;
import cn.com.cnpc.cpoa.domain.bid.BidCertiDto;
import cn.com.cnpc.cpoa.domain.bid.BidKeywordDto;
import cn.com.cnpc.cpoa.po.bid.BidCertiPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidCertiDtoMapper extends AppMapper<BidCertiDto> {

    List<BidCertiPo> selectAllByMap(Map<String,Object> param);

    List<BidCertiPo> selectByCertiBorrowId(@Param("certiBorrowId") String certiBorrowId);

    String selectIsAudit(@Param("certiBorrowId") String certiBorrowId);
}

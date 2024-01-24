package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidCompInfoBorrowListDto;
import cn.com.cnpc.cpoa.po.bid.BidCompInfoBorrowListPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface BidCompInfoBorrowListDtoMapper extends AppMapper<BidCompInfoBorrowListDto> {

    List<BidCompInfoBorrowListPo> selectByCertiBorrowId(@Param("certiBorrowId") String certiBorrowId);
}

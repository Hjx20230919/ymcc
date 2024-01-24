package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidCertiBorrowListDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface BidCertiBorrowListDtoMapper extends AppMapper<BidCertiBorrowListDto> {

    List<Object> selectBYCertiBorrowId(@Param("certiBorrowId")String certiBorrowId);
}

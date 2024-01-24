package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizContBlackBillAttachDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface BizContBlackBillAttachDtoMapper extends AppMapper<BizContBlackBillAttachDto> {

    List<BizContBlackBillAttachDto> selectAllByBlackListId(@Param("blackListId")String blackListId);
}

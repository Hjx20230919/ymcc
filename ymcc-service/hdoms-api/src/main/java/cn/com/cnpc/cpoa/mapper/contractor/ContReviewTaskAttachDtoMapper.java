package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTaskAttachDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
public interface ContReviewTaskAttachDtoMapper extends AppMapper<ContReviewTaskAttachDto> {

    List<ContReviewTaskAttachDto> selectByMap();

}

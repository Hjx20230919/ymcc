package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContReviewDetailDtoMapper extends AppMapper<ContReviewDetailDto> {

    List<ContReviewDetailDto> selectContReviewDetail(Map<String,Object> param);
}

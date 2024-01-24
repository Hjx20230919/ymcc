package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTiDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContReviewTiDtoMapper extends AppMapper<ContReviewTiDto> {

    String selectContReviewTiByType(Map<String,Object> param);

    List<ContReviewTiDto> selectContReviewTi(Map<String,Object> param);
}

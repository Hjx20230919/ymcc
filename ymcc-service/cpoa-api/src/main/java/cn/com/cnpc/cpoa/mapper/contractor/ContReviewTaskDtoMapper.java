package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewTaskDto;
import cn.com.cnpc.cpoa.po.contractor.ContReviewTaskPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContReviewTaskDtoMapper extends AppMapper<ContReviewTaskDto> {

    List<ContReviewTaskPo> selectContReviewTaskByMap(Map<String,Object> param);

    List<ContReviewTaskPo> selectReviewTaskAuditItem(Map<String,Object> param);

    int selectContByContIdAndYear(Map<String,Object> param);
}

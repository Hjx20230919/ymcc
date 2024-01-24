package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.ContReviewOfficeDetailDto;
import cn.com.cnpc.cpoa.po.contractor.ContReviewOfficeDetailPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContReviewOfficeDetailDtoMapper extends AppMapper<ContReviewOfficeDetailDto> {

    List<ContReviewOfficeDetailPo> selectOfficeDetailByMap(Map<String,Object> param);

    List<ContReviewOfficeDetailPo> selectOfficeAuditItem(Map<String,Object> param);

    int selectContByContIdAndYear(Map<String,Object> param);
}

package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.BizRptMonthDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BizRptMonthDtoMapper extends AppMapper<BizRptMonthDto> {

    List<BizRptMonthDto> selectList(Map<String, Object> param);

}

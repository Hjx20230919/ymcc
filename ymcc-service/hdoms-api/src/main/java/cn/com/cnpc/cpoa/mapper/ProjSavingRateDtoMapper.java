package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.ProjSavingRateDto;
import cn.com.cnpc.cpoa.vo.ProjSavingRateVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjSavingRateDtoMapper extends AppMapper<ProjSavingRateDto> {

    List<ProjSavingRateVo> selectSavingRateByMap(Map<String,Object> param);

    List<String> selectThisDaySavingRate();
}

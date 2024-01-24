package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizDealExDto;
import cn.com.cnpc.cpoa.domain.BizDealHdDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizDealHdMapper extends AppMapper<BizDealHdDto> {
    List<BizDealExDto> selectList(Map<String, Object> params);

}

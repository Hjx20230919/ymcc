package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizDealBizDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizDealBizMapper extends AppMapper<BizDealBizDto> {
    public List<BizDealBizDto> selectBydealBydealbiz(Map<String, Object> map);

}

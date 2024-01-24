package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;

import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.BizDealInDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizDealInMapper  extends AppMapper<BizDealInDto> {
    List<BizDealInDto> selectList(Map<String, Object> params);
    List<BizDealInDto> selectUserNameList(Map<String, Object> params);
}

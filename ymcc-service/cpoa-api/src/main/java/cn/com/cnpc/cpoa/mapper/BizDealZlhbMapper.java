package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizDealZlhbDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizDealZlhbMapper extends AppMapper<BizDealZlhbDto> {
    List<BizDealZlhbDto> selectList(Map<String, Object> params);
    List<BizDealZlhbDto> selectUserNameList(Map<String, Object> params);
    String selectCurrentDealNo(Map<String, Object> param);
    public void updateByid(Map<String, Object> param);
}

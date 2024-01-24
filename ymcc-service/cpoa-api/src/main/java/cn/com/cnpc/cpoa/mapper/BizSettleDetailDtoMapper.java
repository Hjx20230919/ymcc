package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizSettleDetailDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/5/10 20:32
 * @Description:
 */
@Mapper
public interface BizSettleDetailDtoMapper extends AppMapper<BizSettleDetailDto> {

    List<BizSettleDetailDto> selectList(Map<String, Object> params);
}

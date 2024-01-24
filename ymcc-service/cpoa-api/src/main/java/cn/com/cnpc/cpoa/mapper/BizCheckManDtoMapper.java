package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizCheckManDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 15:57
 * @Description: 审核人映射
 */
@Mapper
public interface BizCheckManDtoMapper extends AppMapper<BizCheckManDto> {


    List<BizCheckManDto> selectList(Map<String, Object> param);

    List<BizCheckManDto> selectBackList(Map<String, Object> params2);

    int deleteByStepId(String stepId);
}

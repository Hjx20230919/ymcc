package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizSubtypeDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 15:57
 * @Description: 合同类别
 */
@Mapper
public interface BizSubtypeDtoMapper extends AppMapper<BizSubtypeDto> {

    List<BizSubtypeDto> selectList(Map<String, Object> params);

    List<BizSubtypeDto> selectListByName(Map<String, Object> params);
}

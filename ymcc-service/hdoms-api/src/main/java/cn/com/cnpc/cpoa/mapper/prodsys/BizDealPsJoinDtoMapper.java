package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsJoinDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * <项目信息表映射>
 *
 * @author anonymous
 * @create 22/02/2020 23:31
 * @since 1.0.0
 */
@Mapper
public interface BizDealPsJoinDtoMapper extends AppMapper<BizDealPsJoinDto> {
    List<BizDealPsJoinDto> selectList(Map<String, Object> params);
}

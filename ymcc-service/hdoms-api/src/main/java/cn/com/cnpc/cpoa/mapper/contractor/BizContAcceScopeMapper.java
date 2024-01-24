package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceScopeDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:40
 * @Description:
 */
@Mapper
public interface BizContAcceScopeMapper extends AppMapper<BizContAccessDto> {


    List<BizContAcceScopeDto> selectContAcceScopeDto(Map<String, Object> params);
}

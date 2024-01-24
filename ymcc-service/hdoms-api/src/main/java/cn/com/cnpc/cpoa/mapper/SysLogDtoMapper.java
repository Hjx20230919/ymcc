package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface SysLogDtoMapper extends AppMapper<SysLogDto> {


    List<SysLogDto> selectList(Map<String, Object> params);
}

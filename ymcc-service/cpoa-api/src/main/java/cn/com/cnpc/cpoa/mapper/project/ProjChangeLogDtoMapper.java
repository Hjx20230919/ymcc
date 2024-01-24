package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.ProjChangeLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface ProjChangeLogDtoMapper extends AppMapper<ProjChangeLogDto> {

    List<ProjChangeLogDto> selectAllByMap(Map<String,Object> param);
}

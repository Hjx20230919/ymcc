package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizImportLogDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/16 14:49
 * @Description: 映射
 */
@Mapper
public interface BizImportLogDtoMapper extends AppMapper<BizImportLogDto> {

    List<BizImportLogDto> selectList(Map<String, Object> params);
}

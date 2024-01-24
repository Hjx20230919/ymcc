package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizCheckTmDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizCheckTmDtoMapper extends AppMapper<BizCheckTmDto> {

    /**
     * 按条件查询模板
     * @param params
     * @return
     */
    List<BizCheckTmDto> selectCheckTm(Map<String, Object> params);
}

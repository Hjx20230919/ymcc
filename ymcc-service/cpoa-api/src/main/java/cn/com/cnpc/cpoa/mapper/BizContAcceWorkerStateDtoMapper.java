package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerStateDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizContAcceWorkerStateDtoMapper extends AppMapper<BizContAcceWorkerStateDto> {

    /**
     * 按条件查询持证实体
     * @param params
     * @return
     */
    List<BizContAcceWorkerStateDto> selectContAcceWorkerStateDto(Map<String, Object> params);
}

package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerStateAttachDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizContAcceWorkerStateAttachDtoMapper extends AppMapper<BizContAcceWorkerStateAttachDto> {


    /**
     *  查询中间表信息
     * @param param
     * @return
     */
    List<BizContAcceWorkerStateAttachDto> selectContAcceWorkerStateAttach(Map<String, Object> param);
}

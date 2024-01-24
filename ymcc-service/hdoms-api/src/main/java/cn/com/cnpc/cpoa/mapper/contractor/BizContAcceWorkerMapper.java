package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:35
 * @Description:
 */
@Mapper
public interface BizContAcceWorkerMapper extends AppMapper<BizContAcceWorkerDto> {


    List<BizContAcceWorkerDto> selectContAcceWorkerDto(Map<String, Object> params);
}

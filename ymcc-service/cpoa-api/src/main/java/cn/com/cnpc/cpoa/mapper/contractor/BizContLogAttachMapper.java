package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContLogAttachDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:20
 * @Description:
 */
@Mapper
public interface BizContLogAttachMapper extends AppMapper<BizContLogAttachDto> {


    List<BizContLogAttachDto> getContLogAttachDto(Map<String, Object> params);

}

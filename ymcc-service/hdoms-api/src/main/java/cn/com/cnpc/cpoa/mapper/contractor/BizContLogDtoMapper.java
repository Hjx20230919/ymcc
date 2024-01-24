package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContLogDto;
import cn.com.cnpc.cpoa.po.contractor.ContLogPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:11
 * @Description:
 */
@Mapper
public interface BizContLogDtoMapper  extends AppMapper<BizContLogDto> {

    List<BizContLogDto> selectContLogDto(Map<String, Object> param);

    List<ContLogPo> selectContLogPo(Map<String, Object> params);
}

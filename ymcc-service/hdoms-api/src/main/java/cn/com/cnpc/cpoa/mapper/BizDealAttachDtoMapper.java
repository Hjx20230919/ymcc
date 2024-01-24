package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizDealAttachDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 10:08
 * @Description:  合同附件映射
 */
@Mapper
public interface BizDealAttachDtoMapper extends AppMapper<BizDealAttachDto> {

    List<BizDealAttachDto> selectList(Map<String, Object> params);
}

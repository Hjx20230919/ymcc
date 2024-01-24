package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditSetItemDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/27 11:55
 * @Description:
 */
@Mapper
public interface BizContCreditSetItemMapper extends AppMapper<BizContCreditSetItemDto> {

    /**
     * 按条件查询中间项
     * @return
     */
    List<BizContCreditSetItemDto> selectContCreditSetItem(Map<String, Object> params);
}

package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditTiDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/29 20:06
 * @Description:
 */
@Mapper
public interface BizContCreditTiMapper  extends AppMapper<BizContCreditTiDto> {

    List<BizContCreditTiDto> selectContCreditDto(Map<String, Object> params);
}

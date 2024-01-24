package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditAttachDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 20:40
 * @Description:
 */
@Mapper
public interface BizContCreditAttachMapper extends AppMapper<BizContCreditAttachDto> {

    List<BizContCreditAttachDto> getCreditAttachDto(Map<String, Object> params);

    List<BizContCreditAttachDto> getCreditAttachDtoBySetId(Map<String, Object> params);

}

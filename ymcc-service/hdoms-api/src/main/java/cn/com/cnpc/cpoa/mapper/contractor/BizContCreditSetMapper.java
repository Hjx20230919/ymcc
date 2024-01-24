package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditSetDto;
import cn.com.cnpc.cpoa.po.contractor.CreditSetAuditPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/26 15:34
 * @Description:
 */
@Mapper
public interface BizContCreditSetMapper extends AppMapper<BizContCreditSetDto> {

    List<CreditSetAuditPo> selectAuditContCreditSet(Map<String, Object> params);
    List<BizContCreditSetDto> selectContCreditSet(Map<String, Object> params);

    Map<String,Object> selectOverdueCreditCount();

}

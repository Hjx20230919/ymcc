package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizReceiptpaymentDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizReceiptpaymentMapper extends AppMapper<BizReceiptpaymentDto> {
    public List<BizReceiptpaymentDto> selectList(Map<String, Object> params);
    public List<BizReceiptpaymentDto> selectBuDealId(Map<String, Object> params);
}

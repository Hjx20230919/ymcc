package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.BizReceiptpaymentDto;
import cn.com.cnpc.cpoa.mapper.BizReceiptpaymentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BizReceiptpaymentService extends AppService<BizReceiptpaymentDto> {
    @Autowired
    private BizReceiptpaymentMapper bizReceiptpaymentMapper;
    public List<BizReceiptpaymentDto> selectList(Map<String, Object> params){
            List<BizReceiptpaymentDto> bizReceiptpaymentDtos = bizReceiptpaymentMapper.selectList(params);
            return bizReceiptpaymentDtos;
    }

    public List<BizReceiptpaymentDto> selectBuDealId(Map<String, Object> params){
        List<BizReceiptpaymentDto> bizReceiptpaymentDto = bizReceiptpaymentMapper.selectBuDealId(params);
        return bizReceiptpaymentDto;
    }

}

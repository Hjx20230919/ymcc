package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizDealAttachDto;
import cn.com.cnpc.cpoa.domain.BizSettlementAttachDto;
import cn.com.cnpc.cpoa.mapper.BizSettlementAttachDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 10:06
 * @Description:  结算附件服务
 */
@Service
public class SettlementAttachService extends AppService<BizSettlementAttachDto> {
    @Autowired
    private BizSettlementAttachDtoMapper bizSettlementAttachDtoMapper;

    public List<BizSettlementAttachDto> selectList(Map<String, Object> params) {
        return  bizSettlementAttachDtoMapper.selectList(params);
    }
}

package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizDealBizDto;
import cn.com.cnpc.cpoa.mapper.BizDealBizMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BizDealBizService extends AppService<BizDealBizDto> {
    @Autowired
    public BizDealBizMapper bizDealBizMapper;
    public List<BizDealBizDto> selectBydealBydealbiz(Map<String, Object> map){
        return bizDealBizMapper.selectBydealBydealbiz(map);
    }
}

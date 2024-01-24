package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizSettleDetailDto;
import cn.com.cnpc.cpoa.mapper.BizSettleDetailDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/5/10 20:30
 * @Description:
 */
@Service
public class SettleDetailService extends AppService<BizSettleDetailDto> {

    @Autowired
    BizSettleDetailDtoMapper bizSettleDetailDtoMapper;

    public List<BizSettleDetailDto> selectList(Map<String, Object> params) {

        return bizSettleDetailDtoMapper.selectList(params);
    }
}

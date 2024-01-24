package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.ContBlackListHisDto;
import cn.com.cnpc.cpoa.mapper.ContBlackListHisDtoMapper;
import cn.com.cnpc.cpoa.po.contractor.ContBlackListHisPo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/4/26 15:35
 */
@Service
public class ConBlackListHisService extends AppService<ContBlackListHisDto> {

    @Autowired
    private ContBlackListHisDtoMapper contBlackListHisDtoMapper;

    public List<ContBlackListHisPo> selectContBlackListHis(Map<String,Object> param) {
        return contBlackListHisDtoMapper.selectContBlackListHis(param);
    }
}

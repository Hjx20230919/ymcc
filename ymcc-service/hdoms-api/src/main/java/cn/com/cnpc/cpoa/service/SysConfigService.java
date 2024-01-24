package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizSysConfigDto;
import cn.com.cnpc.cpoa.mapper.BizSysConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/21 22:26
 * @Description:
 */
@Service
public class SysConfigService extends AppService<BizSysConfigDto> {

    @Autowired
    BizSysConfigMapper bizSysConfigMapper;

    public List<BizSysConfigDto> selectList(Map<String,Object> params){

        return bizSysConfigMapper.selectList(params);
    }



}

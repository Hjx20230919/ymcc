package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.mapper.SysLogDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 日志服务
 *
 * @author scchenyong@189.cn
 * @create 2019-01-14
 */
@Service
public class LogService extends AppService<SysLogDto> {


    @Autowired
    SysLogDtoMapper sysLogDtoMapper;

    public List<SysLogDto> selectList(Map<String,Object> params){

        return  sysLogDtoMapper.selectList(params);
    }

}

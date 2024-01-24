package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.mapper.BizRestInterfaceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/8/26 21:38
 * @Description:
 */
@Service
public class RestInterfaceService {


    @Autowired
    BizRestInterfaceMapper bizRestInterfaceMapper;

    public List<Map> dealQueryApi001(Map<String, Object> param) {

        return bizRestInterfaceMapper.dealQueryApi001(param);
    }
}

package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizCheckTiDto;
import cn.com.cnpc.cpoa.domain.BizCheckTmDto;
import cn.com.cnpc.cpoa.mapper.BizCheckTmDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/20 8:29
 * @Description:
 */
@Service
public class CheckTmService extends AppService<BizCheckTmDto> {

    @Autowired
    BizCheckTmDtoMapper bizCheckTmDtoMapper;


    public List<BizCheckTmDto> selectCheckTm(Map<String, Object> params) {

        return bizCheckTmDtoMapper.selectCheckTm(params);
    }

}

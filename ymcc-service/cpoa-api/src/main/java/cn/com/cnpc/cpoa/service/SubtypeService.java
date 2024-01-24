package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizCategoryDto;
import cn.com.cnpc.cpoa.domain.BizSubtypeDto;
import cn.com.cnpc.cpoa.mapper.BizSubtypeDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 22:06
 * @Description:
 */
@Service
public class SubtypeService  extends AppService<BizSubtypeDto> {

    @Autowired
    BizSubtypeDtoMapper bizSubtypeDtoMapper;
    public List<BizSubtypeDto> selectList(Map<String, Object> params) {

        return bizSubtypeDtoMapper.selectList(params);
    }
    public List<BizSubtypeDto> selectListByName(Map<String, Object> params) {

        return bizSubtypeDtoMapper.selectListByName(params);
    }

}

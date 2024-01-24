package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizCategoryDto;
import cn.com.cnpc.cpoa.domain.BizSubtypeDto;
import cn.com.cnpc.cpoa.mapper.BizCategoryDtoMapper;
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
public class CategoryService extends AppService<BizCategoryDto> {

    @Autowired
    BizCategoryDtoMapper categoryDtoMapper;

    public List<BizCategoryDto> selectList(Map<String, Object> param4) {

        return categoryDtoMapper.selectList(param4);
    }
    public List<BizCategoryDto> selectListByName(Map<String, Object> param) {

        return categoryDtoMapper.selectListByName(param);
    }

}

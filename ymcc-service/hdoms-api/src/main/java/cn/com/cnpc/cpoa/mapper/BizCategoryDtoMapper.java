package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizCategoryDto;
import cn.com.cnpc.cpoa.domain.BizCheckManDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 15:57
 * @Description: 合同类别
 */
@Mapper
public interface BizCategoryDtoMapper extends AppMapper<BizCategoryDto> {


    List<BizCategoryDto> selectList(Map<String, Object> param4);

    List<BizCategoryDto> selectListByName(Map<String, Object> param);
}

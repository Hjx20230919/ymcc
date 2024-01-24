package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.BizDealImportDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/14 11:34
 * @Description:
 */
@Mapper
public interface BizDealImportDtoMapper extends AppMapper<BizDealImportDto> {


    List<BizDealDto> selectList(Map<String, Object> params);
}

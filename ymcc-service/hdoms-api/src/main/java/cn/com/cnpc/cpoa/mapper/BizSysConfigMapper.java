package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizSysConfigDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/21 22:28
 * @Description:
 */
@Mapper
public interface BizSysConfigMapper extends AppMapper<BizSysConfigDto> {


    List<BizSysConfigDto> selectList(Map<String, Object> params);
}

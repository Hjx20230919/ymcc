package cn.com.cnpc.cpoa.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/8/26 22:50
 * @Description:
 */
@Mapper
public interface BizRestInterfaceMapper {


    List<Map> dealQueryApi001(Map<String, Object> param);

}

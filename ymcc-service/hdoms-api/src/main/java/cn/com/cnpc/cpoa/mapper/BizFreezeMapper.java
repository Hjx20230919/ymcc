package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.vo.StatisticsDetailVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/9/22 9:58
 * @Description:
 */
@Mapper
public interface BizFreezeMapper {


    Integer selectUnPlacedCount();

    List<StatisticsDetailVo> selectUnPlacedDetail();

    void placedAll(Map<String, Object> param);
}

package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.po.*;
import cn.com.cnpc.cpoa.vo.StatisticsDetailThreeVo;
import cn.com.cnpc.cpoa.vo.StatisticsDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map; /**
 * @Author: 17742856263
 * @Date: 2019/4/21 9:50
 * @Description:
 */
@Mapper
public interface BizStatisticsMapper {


    List<StatisticsOnePo> selectListOne(Map<String, Object> params);

    List<StatisticsTwoPo> selectListTwo(Map<String, Object> params);

    List<StatisticsThreePo> selectListThree(Map<String, Object> params);

    List<StatisticsFourPo> selectListFour(Map<String, Object> params);

    List<StatisticsFivePo> selectListFive(Map<String, Object> params);

    List<StatisticsDetailThreeVo> selectListDetailThree(Map<String, Object> params);

    List<StatisticsDetailVo> selectListDetailFour(Map<String, Object> params);

    List<StatisticsDetailVo> selectListDetailFive(Map<String, Object> params);
}

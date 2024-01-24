package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.vo.DealSettleCompreVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/9/8 10:11
 * @Description:
 */
@Mapper
public interface BizComprehensiveMapper extends AppMapper<DealSettleCompreVo> {


    List<DealSettleCompreVo> selectCompreList(Map<String, Object> params);

}

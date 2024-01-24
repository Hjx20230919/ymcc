package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.po.ActivitiItemDealPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 合同映射
 */
@Mapper
public interface BizDealDtoMapper extends AppMapper<BizDealDto> {


    List<BizDealDto> selectList(Map<String, Object> params);

    String selectCurrentDealNo(Map<String, Object> param);

    List<BizDealDto> selectUserList(Map<String, Object> param);

    List<BizDealDto> selectUserNameList(Map<String, Object> params);

    List<BizDealDto> selectAuditedList(Map<String, Object> param);

    List<ActivitiItemDealPo> selectActivitiItem(Map<String, Object> params);

    List<BizDealDto> selectList2(Map<String, Object> param);

    List<BizDealDto> selectListStatistics(Map<String, Object> params);

    String selectAuditCount(Map<String, Object> params);

    List<BizDealDto> selectDealOutTime(Map<String, Object> param);

    List<BizDealDto> selectList3(Map<String, Object> params);

    List<BizDealDto> selectDealAndPS(Map<String, Object> params);
    List<BizDealDto> selectuserById(Map<String, Object> params);
}

package cn.com.cnpc.cpoa.mapper.prodsys;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsDto;
import cn.com.cnpc.cpoa.po.ActivitiItemDealPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 提前开工合同映射
 */
@Mapper
public interface BizDealPsDtoMapper extends AppMapper<BizDealPsDto> {

    List<BizDealPsDto> selectList(Map<String, Object> params);

    String selectCurrentDealNo(Map<String, Object> param);

    List<BizDealPsDto> selectUserList(Map<String, Object> param);

    List<BizDealPsDto> selectUserNameList(Map<String, Object> params);

    List<BizDealPsDto> selectAuditedList(Map<String, Object> param);

    List<ActivitiItemDealPo> selectActivitiItem(Map<String, Object> params);

    List<BizDealPsDto> selectList2(Map<String, Object> param);

    /*List<BizDealPsDto> selectListStatistics(Map<String, Object> params);*/

    String selectAuditCount(Map<String, Object> params);

    /*List<BizDealPsDto> selectDealOutTime(Map<String, Object> param);*/

    List<BizDealPsDto> selectList3(Map<String, Object> params);

}

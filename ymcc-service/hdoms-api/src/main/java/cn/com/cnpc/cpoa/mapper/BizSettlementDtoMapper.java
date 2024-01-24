package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizSettlementDto;
import cn.com.cnpc.cpoa.po.ActivitiItemDealPo;
import cn.com.cnpc.cpoa.po.ActivitiItemSettlePo;
import cn.com.cnpc.cpoa.po.SettlementAuditPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 合同映射
 */
@Mapper
public interface BizSettlementDtoMapper extends AppMapper<BizSettlementDto> {


    List<BizSettlementDto> selectList(Map<String, Object> params);


    List<SettlementAuditPo> selectUserList(Map<String, Object> param);

    List<BizSettlementDto> selectAuditList(Map<String, Object> param);

    List<BizSettlementDto> selectUserNameList(Map<String, Object> params);

    List<SettlementAuditPo> selectAuditedList(Map<String, Object> param);

    List<ActivitiItemSettlePo> selectActivitiItem(Map<String, Object> params);

    String selectAuditCount(Map<String, Object> param);

    Double selectThisMonthSettlement(@Param("dealId")String dealId,@Param("yearMonth")String yearMonth);

}
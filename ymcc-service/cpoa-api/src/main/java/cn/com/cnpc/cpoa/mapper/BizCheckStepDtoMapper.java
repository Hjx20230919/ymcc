package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizCheckStepDto;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 15:57
 * @Description: 审核流程映射
 */
@Mapper
public interface BizCheckStepDtoMapper extends AppMapper<BizCheckStepDto> {

    List<BizCheckStepDto> selectList(Map<String, Object> param);

    List<CheckStepPo> selectDetailsByDealId(Map<String, Object> param);


    List<CheckStepPo> selectDetailsBySettleId(Map<String, Object> param);

    List<BizCheckStepDto> selectLastStepByObjectId(Map<String, Object> param);

    List<BizCheckStepDto> getLastStepByDealId(Map<String, Object> param);

    List<BizCheckStepDto> getLastStepByObjIdAndObjType(Map<String, Object> param);

    List<CheckStepPo> selectRemainList(Map<String, Object> param);

    String selectMaxStepNo(Map<String, Object> param);

    List<BizCheckStepDto> selectMinStepNo(Map<String, Object> params);

    List<BizCheckStepDto> selectBidProjectMinStepNo(Map<String, Object> params);

    List<CheckStepPo> selectDetailsByObjId(Map<String, Object> param);

    CheckStepPo selectDetailsByObjIdAndObjType(Map<String, Object> param);

    List<CheckStepPo> selectSelContCheck(String selContId);

    List<CheckStepPo> seelctPlanCheck(String planId);

    List<CheckStepPo> selectResultCheck(String resultId);

    CheckStepPo selectOfficeAuditCheck(@Param("officeDetailId") String officeDetailId);

    List<CheckStepPo> selectLastStepAudting(Map<String, Object> param);

    List<CheckStepPo> selectStartDeptName(Map<String, Object> param);

}

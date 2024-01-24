package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizCheckStepDto;
import cn.com.cnpc.cpoa.mapper.BizCheckStepDtoMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/9 15:55
 * @Description: 审批流程步骤 服务
 */
@Service
public class CheckStepService extends AppService<BizCheckStepDto> {


    @Autowired
    private BizCheckStepDtoMapper bizCheckStepDtoMapper;

    public List<BizCheckStepDto> selectList(Map<String, Object> param) {

        return bizCheckStepDtoMapper.selectList(param);
    }

    public List<CheckStepPo> selectDetailsByDealId(Map<String, Object> param) {

        return bizCheckStepDtoMapper.selectDetailsByDealId(param);
    }

    public List<CheckStepPo> selectDetailsByObjId(Map<String, Object> param) {

        return bizCheckStepDtoMapper.selectDetailsByObjId(param);
    }

    public CheckStepPo selectDetailsByObjIdAndObjType(Map<String, Object> param) {

        return bizCheckStepDtoMapper.selectDetailsByObjIdAndObjType(param);
    }

    public List<CheckStepPo> selectDetailsBySettleId(Map<String, Object> param) {
        return bizCheckStepDtoMapper.selectDetailsBySettleId(param);
    }


    public List<BizCheckStepDto> selectLastStepByObjectId(Map<String, Object> param) {
        return bizCheckStepDtoMapper.selectLastStepByObjectId(param);
    }

    public List<BizCheckStepDto> getLastStepByDealId(Map<String, Object> param) {
        return bizCheckStepDtoMapper.getLastStepByDealId(param);
    }

    public List<BizCheckStepDto> getLastStepByObjIdAndObjType(Map<String, Object> param) {
        return bizCheckStepDtoMapper.getLastStepByObjIdAndObjType(param);
    }

    public List<CheckStepPo> selectRemainList(Map<String, Object> param) {
        return bizCheckStepDtoMapper.selectRemainList(param);
    }

    public String selectMaxStepNo(Map<String, Object> param) {
        return bizCheckStepDtoMapper.selectMaxStepNo(param);
    }

    public List<BizCheckStepDto> selectMinStepNo(Map<String, Object> params) {
        return bizCheckStepDtoMapper.selectMinStepNo(params);
    }

    public List<BizCheckStepDto> selectBidProjectMinStepNo(Map<String, Object> params) {
        return bizCheckStepDtoMapper.selectBidProjectMinStepNo(params);
    }

    public void checkUpdate(Date nowDate, String stepId, String stepState) {
        BizCheckStepDto checkStepDto = new BizCheckStepDto();
        checkStepDto.setStepId(stepId);
        checkStepDto.setStepState(stepState);
        checkStepDto.setStepEndAt(nowDate);
        updateNotNull(checkStepDto);
    }

    public CheckStepPo selectOfficeAuditCheck( String officeDetailId) {
        return bizCheckStepDtoMapper.selectOfficeAuditCheck(officeDetailId);
    }

    public List<CheckStepPo> selectLastStepAudting(Map<String, Object> param) {
        return bizCheckStepDtoMapper.selectLastStepAudting(param);
    }

    public List<CheckStepPo> selectStartDeptName(Map<String, Object> param) {
        return bizCheckStepDtoMapper.selectStartDeptName(param);
    }

}

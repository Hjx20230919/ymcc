/**
 * Copyright (C), 2019-2020, ccssoft.com.cn
 * Java version: 1.8
 * FileName: BizDealPsJoinService
 * Author:   wangjun
 * Date:     22/02/2020 23:29
 */
package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsJoinDto;
import cn.com.cnpc.cpoa.mapper.BizDealDtoMapper;
import cn.com.cnpc.cpoa.mapper.SysLogDtoMapper;
import cn.com.cnpc.cpoa.mapper.prodsys.BizDealPsDtoMapper;
import cn.com.cnpc.cpoa.mapper.prodsys.BizDealPsJoinDtoMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <>
 *
 * @author wangjun
 * @create 22/02/2020 23:29
 * @since 1.0.0
 */
@Service
public class BizDealPsJoinService extends AppService<BizDealPsJoinDto> {

    @Autowired
    BizDealDtoMapper dealDtoMapper;

    @Autowired
    BizDealPsDtoMapper dealPsDtoMapper;

    @Autowired
    BizDealPsJoinDtoMapper dealPsJoinDtoMapper;

    @Autowired
    SysLogDtoMapper sysLogDtoMapper;

    /**
     * 提前开工金额<br>
     * 1：关联合同时用合同金额替换提前开工的预估金额、名称、编码；<br>
     * 2：合同deal变更审批完成后，更新提前开工的预估金额、名称、编码（提供一个service方法供deal那边调用）
     *
     * @param dealId 被关联的合同ID，deal.dealId
     * @param userId 用户ID
     */
    @Deprecated
    public boolean overwriteDealPsValue(String dealId, String userId) {
        BizDealDto dealDto = dealDtoMapper.selectByPrimaryKey(dealId);
        Map<String, Object> params = new HashMap<>();
        params.put("dealJoinId", dealId);
        List<BizDealPsJoinDto> joinDtoList = dealPsJoinDtoMapper.selectList(params);
        if (joinDtoList == null || joinDtoList.isEmpty()) {
            SysLogDto logDto = new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            logDto.setUserId(userId);
            logDto.setLogTime(new Date());
            logDto.setLogModule(LogModule.PRODSYS.toString());
            logDto.setLogObject(dealId);
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogContent("复写提前开工预估金额失败：根据合同未找到关联的提前开工记录！");
            sysLogDtoMapper.insert(logDto);
            return false;
        }
        if (joinDtoList.size() > 1) {
            SysLogDto logDto = new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            logDto.setUserId(userId);
            logDto.setLogTime(new Date());
            logDto.setLogModule(LogModule.PRODSYS.toString());
            logDto.setLogObject(dealId);
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogContent("复写提前开工预估金额失败：根据合同找到多个关联的提前开工记录！");
            sysLogDtoMapper.insert(logDto);
            return false;
        }

        params.clear();
        params.put("dealId", joinDtoList.get(0).getDealId());
        List<BizDealPsDto> dealPsDtos = dealPsDtoMapper.selectList(params);
        if (dealPsDtos == null || dealPsDtos.isEmpty()) {
            SysLogDto logDto = new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            logDto.setUserId(userId);
            logDto.setLogTime(new Date());
            logDto.setLogModule(LogModule.PRODSYS.toString());
            logDto.setLogObject(dealId);
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogContent("复写提前开工预估金额失败：根据合同未找到关联的提前开工记录！");
            sysLogDtoMapper.insert(logDto);
            return false;
        }
        if (dealPsDtos.size() > 1) {
            SysLogDto logDto = new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            logDto.setUserId(userId);
            logDto.setLogTime(new Date());
            logDto.setLogModule(LogModule.PRODSYS.toString());
            logDto.setLogObject(dealId);
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogContent("复写提前开工预估金额失败：根据合同找到多个关联的提前开工记录！");
            sysLogDtoMapper.insert(logDto);
            return false;
        }

        BizDealPsDto dealPsDto = dealPsDtos.get(0);
        double dealValueBefore = dealPsDto.getDealValue();
        double dealValueAfter = dealDto.getDealValue();
        String dealNameBefore = dealPsDto.getDealName();
        String dealNameAfter = dealDto.getDealName();
        String dealNoBefore = dealPsDto.getDealNo();
        String dealNoAfter = dealDto.getDealNo();
        BizDealPsDto entity = new BizDealPsDto();
        entity.setDealId(dealPsDto.getDealId());
        entity.setDealValue(dealDto.getDealValue()); // 预估金额
        entity.setDealName(dealDto.getDealName()); // 合同名称
        entity.setDealNo(dealDto.getDealNo()); // 合同编号
        int ret = dealPsDtoMapper.updateByPrimaryKeySelective(entity);
        if (ret <= 0) {
            SysLogDto logDto = new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            logDto.setUserId(userId);
            logDto.setLogTime(new Date());
            logDto.setLogModule(LogModule.PRODSYS.toString());
            logDto.setLogObject(dealId);
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogContent("复写提前开工预估金额失败！更新了0条记录。dealPs.id=" + dealPsDto.getDealId());
            sysLogDtoMapper.insert(logDto);
            return false;
        } else {
            SysLogDto logDto = new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            logDto.setUserId(userId);
            logDto.setLogTime(new Date());
            logDto.setLogModule(LogModule.PRODSYS.toString());
            logDto.setLogObject(dealId);
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogContent(String.format("复写提前开工预估金额成功！dealPs.id=%s,dealPs[dealValue,dealName,dealNo]#before=[%s,%s,%s],after=[%s,%s,%s]", dealPsDto.getDealId(), dealValueBefore, dealNameBefore, dealNoBefore, dealValueAfter, dealNameAfter, dealNoAfter));
            sysLogDtoMapper.insert(logDto);
        }

        return true;
    }

    public List<BizDealPsJoinDto> selectList(Map<String, Object> params) {
        return dealPsJoinDtoMapper.selectList(params);
    }
}

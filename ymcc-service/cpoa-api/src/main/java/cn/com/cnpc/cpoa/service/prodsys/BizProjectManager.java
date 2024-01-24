package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.domain.BizDealDto;
import cn.com.cnpc.cpoa.domain.SysLogDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.mapper.BizDealDtoMapper;
import cn.com.cnpc.cpoa.mapper.SysLogDtoMapper;
import cn.com.cnpc.cpoa.mapper.prodsys.BizDealPsJoinDtoMapper;
import cn.com.cnpc.cpoa.mapper.prodsys.BizProjectDtoMapper;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.prodsys.SyncRet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: sirjaime
 * @create: 2020-09-14 21:54
 */
@Service
public class BizProjectManager {

    // @Autowired
    // BizDealPsJoinService bizDealPsJoinService;

    @Autowired
    BizDealDtoMapper bizDealDtoMapper;

    @Autowired
    BizProjectDtoMapper bizProjectDtoMapper;

    @Autowired
    BizProjectService bizProjectService;

    @Autowired
    BizDealPsJoinDtoMapper bizDealPsJoinDtoMapper;

    @Autowired
    ContractSyncService contractSyncService;

    @Autowired
    SysLogDtoMapper sysLogDtoMapper;

    /**
     * <p>t_biz_project_join.deal_join_id=t_biz_deal.deal_id对应t_biz_deal数据发生变化，
     * 或t_biz_project_join关联新增或修改时，根据t_biz_deal.deal_id=t_biz_project.deal_id找到t_biz_project数据。
     * 若存在，则用t_biz_deal数据覆盖t_biz_project合同金额+合同名称+合同编号；
     * 同时t_biz_project数据同步到生产系统。若未找到则不做处理。
     *
     * </p>
     *
     * @param dealId t_biz_deal.deal_id
     * @param userId
     * @return
     */
    public AppMessage bizDealChanged(final BizDealDto dealDto) {
        //BizDealDto dealDto = bizDealDtoMapper.selectByPrimaryKey(dealId);
        String userId= ServletUtils.getSessionUserId();
        String dealId = dealDto.getDealId();
        Map<String, Object> params = new HashMap<>();
        params.put("dealId", dealDto.getDealId());
        List<BizProjectDto> projectDtoList = bizProjectDtoMapper.selectList(params);
        if (projectDtoList != null && !projectDtoList.isEmpty()) {
            projectDtoList.parallelStream().forEach(projDto -> {
                // TODO 如果找到多个，应该是有问题的，fixme
                Double beforePrice = projDto.getContractPrice();
                String beforeName = projDto.getContractName();
                String beforeNo = projDto.getContractNumber();
                Double afterPrice = dealDto.getDealValue();
                String afterName = dealDto.getDealName();
                String afterNo = dealDto.getDealNo();

                // update project
                projDto.setContractPrice(dealDto.getDealValue());
                projDto.setContractName(dealDto.getDealName());
                projDto.setContractNumber(dealDto.getDealNo());
                bizProjectDtoMapper.updateByPrimaryKeySelective(projDto);

                // log
                SysLogDto logDto = new SysLogDto();
                logDto.setLogId(StringUtils.getUuid32());
                logDto.setUserId(userId);
                logDto.setLogTime(new Date());
                logDto.setLogModule(LogModule.PRODSYS.toString());
                logDto.setLogObject(dealDto.getDealId());
                logDto.setLogType(LogType.OPERATION.toString());
                logDto.setLogContent(String.format("根据合同更新项目信息成功！项目id=%s,project[contractPrice,contractName,contractNumber]#before=[%s,%s,%s],after=[%s,%s,%s]", projDto.getContractId(), beforePrice, beforeName, beforeNo, afterPrice, afterName, afterNo));
                sysLogDtoMapper.insert(logDto);

                // resync to prodsys
                resyncContractInfo(projDto.getContractId());

            });
        } else {
            // log
            SysLogDto logDto = new SysLogDto();
            logDto.setLogId(StringUtils.getUuid32());
            logDto.setUserId(userId);
            logDto.setLogTime(new Date());
            logDto.setLogModule(LogModule.PRODSYS.toString());
            logDto.setLogObject(dealDto.getDealId());
            logDto.setLogType(LogType.OPERATION.toString());
            logDto.setLogContent(String.format("根据合同更新项目信息失败！未找到项目！合同id=%s", dealId));
            sysLogDtoMapper.insert(logDto);
        }

        return AppMessage.success(dealId, "更新项目成功");
    }

    public AppMessage resyncContractInfo(String contractId) {
        if (bizProjectService.isNeedSync(contractId)) {
            // 按策略同步到生产系统
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("contractId", contractId);
            List<BizProjectDto> result = bizProjectService.selectList(params);
            if (result == null || result.isEmpty())
                return AppMessage.error("错误，项目信息不存在，请确保该合同已进行项目开工");
            BizProjectDto projectDto = result.get(0);
            // SyncRet ret = notify(projectDto);
            SyncRet ret = contractSyncService.contractSync(projectDto);
            if (!"200".equals(ret.getCode())) {
                return AppMessage.error("下发项目信息失败，接口反馈详情：" + ret.getMsg());
            }

            // update sync status
            BizProjectDto projDto = new BizProjectDto();
            projDto.setContractId(contractId);
            projDto.setSyncStatus(1);
            bizProjectService.updateNotNull(projDto);
            return AppMessage.success(contractId, "重新下发成功");
        } else {
            return AppMessage.error(contractId, "未开放同步功能，请联系系统管理员");
        }
    }
}

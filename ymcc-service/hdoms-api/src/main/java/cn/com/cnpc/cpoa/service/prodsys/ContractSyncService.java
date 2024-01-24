package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.common.json.JSON;
import cn.com.cnpc.cpoa.domain.BizContractorDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.domain.prodsys.SysSubDeptDto;
import cn.com.cnpc.cpoa.enums.prodsys.*;
import cn.com.cnpc.cpoa.mapper.prodsys.SysSubDeptDtoMapper;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.http.HttpUtils;
import cn.com.cnpc.cpoa.vo.prodsys.SyncRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * <invoke Interface asynchronously>
 *
 * @author anonymous
 * @create 27/02/2020 14:13
 * @since 1.0.0
 */
@Service
public class ContractSyncService {

    private static final Logger log = LoggerFactory.getLogger(ContractSyncService.class);

    @Value("${prodsys.contractSync.syncContractInfo}")
    private String URL_CONTRACT_SYNC_ADD;

    @Value("${prodsys.contractSync.updateClientInfo}")
    private String URL_CLIENT_SYNC_UPDATE;

    @Autowired
    private SysSubDeptService subDeptService;

    @Async
    public Future<SyncRet> invokeContractSync(BizProjectDto projectDto) {
        return new AsyncResult<SyncRet>(contractSync(projectDto));
    }

    @Async
    public Future<SyncRet> invokeClientInfoSync(BizContractorDto contractorDto) {
        return new AsyncResult<SyncRet>(clientInfoSync(contractorDto));
    }

    /**
     * @param projectDto
     * @return
     */
    public SyncRet contractSync(BizProjectDto projectDto) {
        SyncRet syncRet = null;
        try {
//            ContractSyncVo syncVo = new ContractSyncVo();
            Map<String, Object> syncVo = new HashMap<>();

            // assemble contract data
//            ContractVo contractVo = new ContractVo();
//            contractVo.setCONTRACT_ID(projectDto.getContractId());
//            contractVo.setCLIENT_ID(projectDto.getClientId());
//            contractVo.setCONTRACT_NAME(projectDto.getContractName());
//            contractVo.setREFER_UNIT(projectDto.getReferUnit());
//            contractVo.setMARKET_TYPE(MarketTypeEnum.getEnumByKey(projectDto.getMarketType()));
//            contractVo.setCOMPANY_TYPE(CompanyTypeEnum.getEnumByKey(projectDto.getCompanyType()));
//            contractVo.setCONTRACT_TYPE(ContractTypeEnum.getEnumByKey(projectDto.getContractType()));
//            contractVo.setWORK_ZONE(WorkZoneEnum.getEnumByKey(projectDto.getWorkZone()));
//            contractVo.setWORK_TYPE(WorkTypeEnum.getEnumByKey(projectDto.getWorkType()));
//            contractVo.setCONTRACT_NUMBER(projectDto.getContractNumber());
//            contractVo.setRESPON_DEPART(projectDto.getResponDepart());
//            contractVo.setCONTRACT_BEGIN_DATE(projectDto.getContractBeginDate());
//            contractVo.setCONTRACT_PLAN_END_DATE(projectDto.getContractPlanEndDate());
//            contractVo.setCONTRACT_PRICE(projectDto.getContractPrice());
//            contractVo.setPAY_TYPE(PayTypeEnum.getEnumByKey(projectDto.getPayType()));
//            contractVo.setCREATE_USER(projectDto.getCreateUser());
//            contractVo.setDEAL_ID(projectDto.getDealId());
//            syncVo.setContract(contractVo);

            //lombok 生成的key大小写混乱了，改为map实现
            if (StringUtils.isEmpty(projectDto.getResponDepart())) {
                throw new Exception("负责科室为空");
            }
            SysSubDeptDto subDeptDto = subDeptService.selectByKey(projectDto.getResponDepart());
            if (subDeptDto == null) {
                throw new Exception("负责科室为空");
            }

            subDeptService.selectByKey(projectDto.getReferUnit());
            Map<String, String> contractVo = new HashMap<>();
            contractVo.put("CONTRACT_ID", projectDto.getContractId());
            contractVo.put("CLIENT_ID", projectDto.getClientId());
            contractVo.put("CONTRACT_NAME", projectDto.getContractName());
            contractVo.put("REFER_UNIT", subDeptDto.getDeptName()); // subDept.deptName
            contractVo.put("MARKET_TYPE", MarketTypeEnum.getEnumByKey(projectDto.getMarketType()));
            contractVo.put("COMPANY_TYPE", CompanyTypeEnum.getEnumByKey(projectDto.getCompanyType()));
            contractVo.put("CONTRACT_TYPE", ContractTypeEnum.getEnumByKey(projectDto.getContractType()));
            contractVo.put("WORK_ZONE", WorkZoneEnum.getEnumByKey(projectDto.getWorkZone()));
            contractVo.put("WORK_TYPE", WorkTypeEnum.getEnumByKey(projectDto.getWorkType()));
            contractVo.put("CONTRACT_NUMBER", projectDto.getContractNumber());
            contractVo.put("RESPON_DEPART", projectDto.getResponDepart()); // subDept.subDeptId
            contractVo.put("CONTRACT_BEGIN_DATE", DateUtils.dateTimeYYYY_MM_DD(projectDto.getContractBeginDate()));
            contractVo.put("CONTRACT_PLAN_END_DATE", DateUtils.dateTimeYYYY_MM_DD(projectDto.getContractPlanEndDate()));
            contractVo.put("CONTRACT_PRICE", projectDto.getContractPrice() + "");
            contractVo.put("PAY_TYPE", PayTypeEnum.getEnumByKey(projectDto.getPayType()));
            contractVo.put("CREATE_USER", projectDto.getCreateUserName());
            contractVo.put("DEAL_ID", projectDto.getDealId());
            syncVo.put("contract", contractVo);

            // assemble client data
//            ClientVo clientVo = new ClientVo();
//            clientVo.setCLIENT_ID(projectDto.getClientId());
//            clientVo.setCLIENT_NAME(projectDto.getClientName());
//            clientVo.setMARKET_TYPE(MarketTypeEnum.getEnumByKey(projectDto.getMarketType()));
//            clientVo.setCOMPANY_TYPE(CompanyTypeEnum.getEnumByKey(projectDto.getCompanyType()));
//            clientVo.setCONTRACT_TYPE(ContractTypeEnum.getEnumByKey(projectDto.getContractType()));
//            clientVo.setWORK_ZONE(WorkZoneEnum.getEnumByKey(projectDto.getWorkZone()));
//            syncVo.setClient(clientVo);

            Map<String, String> clientVo = new HashMap<>();
            clientVo.put("CLIENT_ID", projectDto.getClientId());
            clientVo.put("CLIENT_NAME", projectDto.getClientName());
            clientVo.put("MARKET_TYPE", MarketTypeEnum.getEnumByKey(projectDto.getMarketType()));
            clientVo.put("COMPANY_TYPE", CompanyTypeEnum.getEnumByKey(projectDto.getCompanyType()));
            clientVo.put("CONTRACT_TYPE", ContractTypeEnum.getEnumByKey(projectDto.getContractType()));
            clientVo.put("WORK_ZONE", WorkZoneEnum.getEnumByKey(projectDto.getWorkZone()));
            syncVo.put("client", clientVo);

//            ContractSyncDataVo dataVo = new ContractSyncDataVo();
//            dataVo.setData(syncVo);

//            Map<String, Object> dataVo = new HashMap<>();
//            dataVo.put("data", syncVo);

            // production system using not json but form-data to get params
            String data = "data=" + JSON.marshal(syncVo);
            System.out.println(data);
            String ret = HttpUtils.doPost(URL_CONTRACT_SYNC_ADD, data);
            syncRet = JSON.unmarshal(ret, SyncRet.class);
        } catch (Exception e) {
            e.printStackTrace();
            syncRet = new SyncRet();
            syncRet.setCode("400");
            syncRet.setMsg("调用接口出错：" + e.getMessage());
        }
        return syncRet;
    }

    public SyncRet clientInfoSync(BizContractorDto contractorDto) {
        SyncRet syncRet = null;
        try {
//            ClientSyncDataVo dataVo = new ClientSyncDataVo();
//            ClientSyncVo syncVo = new ClientSyncVo();
//
//            ClientVo clientVo = new ClientVo();
//            clientVo.setCLIENT_ID(contractorDto.getContId());
//            clientVo.setCLIENT_NAME(contractorDto.getContName());
//            clientVo.setMARKET_TYPE(MarketTypeEnum.getEnumByKey(contractorDto.getMarketType()));
//            clientVo.setCOMPANY_TYPE(CompanyTypeEnum.getEnumByKey(contractorDto.getCompanyType()));
//            clientVo.setCONTRACT_TYPE(ContractTypeEnum.getEnumByKey(contractorDto.getContractType()));
//            clientVo.setWORK_ZONE(WorkZoneEnum.getEnumByKey(contractorDto.getWorkZone()));
//            syncVo.setClient(clientVo);
//            dataVo.setData(syncVo);

//            Map<String, Object> dataVo = new HashMap<>();
            Map<String, Object> syncVo = new HashMap<>();
            Map<String, String> clientVo = new HashMap<>();
            clientVo.put("CLIENT_ID", contractorDto.getContId());
            clientVo.put("CLIENT_NAME", contractorDto.getContName());
            clientVo.put("MARKET_TYPE", MarketTypeEnum.getEnumByKey(contractorDto.getMarketType()));
            clientVo.put("COMPANY_TYPE", CompanyTypeEnum.getEnumByKey(contractorDto.getCompanyType()));
            clientVo.put("CONTRACT_TYPE", ContractTypeEnum.getEnumByKey(contractorDto.getContractType()));
            clientVo.put("WORK_ZONE", WorkZoneEnum.getEnumByKey(contractorDto.getWorkZone()));
            clientVo.put("UPDATE_USER", contractorDto.getCreateName());
            syncVo.put("client", clientVo);
//            dataVo.put("data", syncVo);

            String data = "data=" + JSON.marshal(syncVo);
            System.out.println(data);
            String ret = HttpUtils.doPost(URL_CLIENT_SYNC_UPDATE, data);
            syncRet = JSON.unmarshal(ret, SyncRet.class);
        } catch (Exception e) {
            e.printStackTrace();
            syncRet = new SyncRet();
            syncRet.setCode("400");
            syncRet.setMsg("调用接口出错：" + e.getMessage());
        }
        return syncRet;
    }
}

package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.BizDealAttachDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizDealPsDto;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.DealWebTypeEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.mapper.prodsys.BizDealPsDtoMapper;
import cn.com.cnpc.cpoa.po.ActivitiItemDealPo;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.MapUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <copy from DealService>
 *
 * @author anonymous
 * @create 22/02/2020 23:29
 * @since 1.0.0
 */
@Service
public class BizDealPsService extends AppService<BizDealPsDto> {

    private static final Logger log = LoggerFactory.getLogger(BizDealPsService.class);
    @Autowired
    private BizDealPsDtoMapper bizDealDtoMapper;

    @Autowired
    private DealAttachService dealAttachService;

    @Autowired
    private AttachService attachService;


    @Autowired
    private BizCheckStepService bizCheckStepService;
    @Autowired
    CheckStepService checkStepService;

    @Autowired
    private BizDealPsApprovalService activitiService;
//    @Autowired
//    private SettlementService settlementService;

    @Autowired
    BizDealPsService dealService;

    @Autowired
    CheckManService checkManService;
    @Value("${file.baseurl}")
    private String BASEURL;
    //    recyclebinurl
    @Value("${file.recyclebinurl}")
    private String RECYCLEBINURL;

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;

    public List<BizDealPsDto> selectList(Map<String, Object> params) {
        List<BizDealPsDto> dealDtos = bizDealDtoMapper.selectList(params);
        for (BizDealPsDto dealDto : dealDtos) {
            Map<String, Object> param = new HashMap<>();
            param.put("dealId", dealDto.getDealId());
            List<String> names = selectUserNameList(param);
            dealDto.setUserNames(names);
        }
        return dealDtos;
    }

    public List<String> selectUserNameList(Map<String, Object> params) {
        List<BizDealPsDto> bizDealDtos = bizDealDtoMapper.selectUserNameList(params);
        List<String> names = new ArrayList<>();
        for (BizDealPsDto bizDealDto : bizDealDtos) {
            names.add(bizDealDto.getUserName());
        }
        return names;
    }

    public List<BizDealPsDto> selectListForExport(Map<String, Object> params) {
        List<BizDealPsDto> dealDtos = bizDealDtoMapper.selectList3(params);
        return dealDtos;
    }

    @Transactional
    public boolean saveChain(BizDealPsDto dealDto, List<BizAttachDto> attachDtos, List<BizDealAttachDto> dealAttachDtos, String type) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("dealNo", dealDto.getDealNo());
        List<BizDealPsDto> bizDealDtos = bizDealDtoMapper.selectList(params);
        if (null != bizDealDtos && bizDealDtos.size() > 0) {
            throw new AppException("合同编码已存在，请重新发起！");
        }

        //1 保存合同
        int save = save(dealDto);
        if (save != 1) {
            throw new AppException("保存提前开工合同出错！");
        }
        //2 保存附件
        for (BizAttachDto attachDto : attachDtos) {
            String fileUri = attachDto.getFileUri();
            String toFileUri = BASEURL + dealDto.getDealType() + "/" + dealDto.getDealNo() + "/" + DateUtils.getDate() + "/";
            attachDto.setFileUri(toFileUri + attachDto.getFileName());
            log.info("fileUri:" + fileUri + ";toFileUri" + toFileUri);
            if (!attachService.removeFile(fileUri, toFileUri)) {
                throw new AppException("保存附件出错！");
            }
            if (1 != attachService.updateNotNull(attachDto)) {
                throw new AppException("保存附件出错！");
            }
        }
        //3 保存合同附件
        for (BizDealAttachDto dealAttachDto : dealAttachDtos) {
            if (1 != dealAttachService.save(dealAttachDto)) {
                throw new AppException("保存合同附件出错！");
            }
        }
        //4 如果状态为 保存提交 则生成审核相关
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            String stepNo = bizCheckStepService.selectMaxStepNo(dealDto.getDealId());
            activitiService.initBaseActiviti(dealDto.getUserId(), dealDto.getDealId(), CheckTypeEnum.DEALPS.getKey(), dealDto.getDeptId(), stepNo, null);

        }
        return true;
    }

    /**
     * @param dealDto
     * @param attachDtos     前端传来的附件
     * @param dealAttachDtos
     * @param type
     * @param userId
     * @return
     * @throws Exception
     */
    @Transactional
    public boolean updateChain(BizDealPsDto dealDto, List<BizAttachDto> attachDtos, List<BizDealAttachDto> dealAttachDtos, String type, String userId) throws Exception {

        String dealId = dealDto.getDealId();
        Map<String, Object> params = new HashMap<>();
        params.put("dealId", dealId);
        //1 删除历史附件
        List<BizDealAttachDto> bizDealAttachDtos = dealAttachService.selectList(params);

        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizDealAttachDto bizDealAttachDto : bizDealAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(bizDealAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), bizDealAttachDto.getId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(bizDealAttachDto.getAttachId(), bizDealAttachDto.getId());

        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        String updProToFileUri = RECYCLEBINURL + dealDto.getDealType() + "/" + dealDto.getDealNo() + "/" + DateUtils.getDate() + "/";
        //保存不要的附件
        Set<String> keys = allMap.keySet();
        for (String key : keys) {
            BizAttachDto bizAttachDto = attachService.selectByKey(key);
            String fileUri = bizAttachDto.getFileUri();
            attachService.removeFile1(fileUri,updProToFileUri);

        }

        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            if (!attachService.deleteById(entry.getKey(),"")) {
                throw new AppException("修改合同时 删除历史附件出错！");
            }
            //2 删除合同附件记录
            if (1 != dealAttachService.delete(entry.getValue())) {
                throw new AppException("修改合同时 合同附件记录出错！");
            }
        }


        //3 修改合同
        updateNotNull(dealDto);

        //4 新增附件
        for (BizAttachDto attachDto : attachDtos) {

            //上传到临时目录时没有添加userId，故可以用来判断是否是新增的。空则表示新增
            if (StringUtils.isEmpty(attachDto.getUserId())) {
                String fileUri = attachDto.getFileUri();
                String toFileUri = BASEURL + dealDto.getDealType() + "/" + dealDto.getDealNo() + "/" + DateUtils.getDate() + "/";
                try {
                    if (!attachService.removeFile(fileUri, toFileUri)) {
                        throw new AppException("保存附件出错！" + attachDto.getFileName());
                    }
                } catch (Exception e) {
                    throw new AppException("保存附件出错！" + e.getMessage());
                }

                attachDto.setFileUri(toFileUri + attachDto.getFileName());
                attachDto.setUserId(userId);
                if (1 != attachService.updateNotNull(attachDto)) {
                    throw new AppException("修改合同时 新增附件出错！");
                }
                //保存合同附件
                BizDealAttachDto dealAttachDto = new BizDealAttachDto();
                dealAttachDto.setId(StringUtils.getUuid32());
                dealAttachDto.setDealId(dealDto.getDealId());
                dealAttachDto.setAttachId(attachDto.getAttachId());
                dealAttachService.save(dealAttachDto);
            } else {
                if (1 != attachService.updateNotNull(attachDto)) {
                    throw new AppException("修改合同时 新增附件出错！");
                }
            }
        }
//        //5 保存合同附件
//        for (BizDealAttachDto dealAttachDto:dealAttachDtos) {
//            if(1!=dealAttachService.save(dealAttachDto)){
//                throw new AppException("修改合同时 保存合同附件出错！");
//            }
//        }
        //6 如果状态为 保存提交 则生成审核相关
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            String stepNo = bizCheckStepService.selectMaxStepNo(dealId);
            activitiService.initBaseActiviti(userId, dealDto.getDealId(), CheckTypeEnum.DEALPS.getKey(), dealDto.getDeptId(), stepNo, null);

        }
        //7 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if (DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)) {
            bizCheckStepService.updateBackObj(dealId);

        }


        return true;
    }

    /**
     * 获取合同编码
     * <p>
     * 按照编码规则生成，规则：[内责书(NZ),3万一下(TH),线下合同(XX)]-安检院(ajy)-年份(4位)-4位数(0001开始增长)
     *
     * @return
     */
    public String getDealNo(String dealType) {

        int year = DateUtils.getYear();
        Map<String, Object> param = new HashMap<>();
        param.put("dealType", dealType);
        param.put("year", year);
        //获取到当前合同 年份 最大的 最后四位数
        String dealNo4 = selectCurrentDealNo(param);
        if (StringUtils.isEmpty(dealNo4)) {
            dealNo4 = "0000";
        }
        return dealType + "-ajy-" + year + "-" + String.format("%04d", (Integer.valueOf(dealNo4) + 1));
    }

    private String selectCurrentDealNo(Map<String, Object> param) {

        return bizDealDtoMapper.selectCurrentDealNo(param);
    }

    public List<BizDealPsDto> selectUserList(Map<String, Object> param) {
        List<BizDealPsDto> dealDtos = bizDealDtoMapper.selectUserList(param);
        for (BizDealPsDto dealDto : dealDtos) {
            Map<String, Object> params = new HashMap<>();
            params.put("dealId", dealDto.getDealId());
            //查询当前审核用户
            List<String> names = selectUserNameList(params);
            dealDto.setUserNames(names);
        }
        return dealDtos;
    }


//    /**
//     * 比较 结算金额 小于 变更的合同金额  《0
//     *
//     * @param dealValue
//     * @param id
//     * @return
//     */
//    public boolean isAvailableAmount(Double dealValue, String id) {
//        Map<String, Object> params = new HashMap<String, Object>();
//        params.put("dealId", id);
//        params.put("settleStatus", SettlementStatusEnum.DOWN.getKey());
//        List<BizSettlementDto> bizSettlementDtoDtos = settlementService.selectList(params);
//        //已审核结清金额
//        Double settleAmount = 0.0;
//        for (BizSettlementDto bizSettlementDto : bizSettlementDtoDtos) {
//            settleAmount = BigDecimalUtils.add(settleAmount, bizSettlementDto.getSettleAmount());
//        }
//        log.info("结算金额为" + settleAmount + ",变更金额为" + dealValue);
//        return BigDecimalUtils.compare(settleAmount, dealValue) < 0;
//    }

    public List<BizDealPsDto> selectAuditedList(Map<String, Object> param) {
        return bizDealDtoMapper.selectAuditedList(param);
    }


    public List<ActivitiItemDealPo> selectActivitiItem(Map<String, Object> params) {

        return bizDealDtoMapper.selectActivitiItem(params);
    }

    public List<BizDealPsDto> selectList2(Map<String, Object> param) {
        return bizDealDtoMapper.selectList2(param);
    }

 /*   public List<BizDealPsDto> selectListStatistics(Map<String, Object> params) {

        return bizDealDtoMapper.selectListStatistics(params);
    }*/

    public String selectAuditCount(Map<String, Object> params) {

        return bizDealDtoMapper.selectAuditCount(params);
    }

   /* public List<BizDealDto> selectDealOutTime(Map<String, Object> param) {

        return bizDealDtoMapper.selectDealOutTime(param);
    }*/

    @Transactional
    public boolean deleteDealAttach(String dealId) {
        Map<String, Object> params = new HashMap<>();
        params.put("dealId", dealId);
        //1 删除历史附件
        List<BizDealAttachDto> bizDealAttachDtos = dealAttachService.selectList(params);
        for (BizDealAttachDto dealAttachDto : bizDealAttachDtos) {
            String attachId = dealAttachDto.getAttachId();
            String id = dealAttachDto.getId();
            attachService.deleteById(attachId,"");
            dealAttachService.delete(id);
        }
        dealService.delete(dealId);

        return true;
    }


    public List<BizDealPsDto> selectList3(Map<String, Object> params) {
        List<BizDealPsDto> dealDtos = bizDealDtoMapper.selectList3(params);
        for (BizDealPsDto dealDto : dealDtos) {
            Map<String, Object> param = new HashMap<>();
            param.put("dealId", dealDto.getDealId());
            List<String> names = selectUserNameList(param);
            dealDto.setUserNames(names);
        }
        return dealDtos;
    }

}

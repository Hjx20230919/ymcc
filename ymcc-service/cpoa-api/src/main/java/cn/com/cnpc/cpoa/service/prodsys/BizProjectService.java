package cn.com.cnpc.cpoa.service.prodsys;

import cn.com.cnpc.cpoa.common.constants.Constants;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.BizSysConfigDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectAttachDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectDto;
import cn.com.cnpc.cpoa.domain.prodsys.BizProjectWorkDto;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.prodsys.ContractStateEnum;
import cn.com.cnpc.cpoa.enums.prodsys.ContractTypeEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.mapper.prodsys.BizProjectDtoMapper;
import cn.com.cnpc.cpoa.service.*;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.MapUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.prodsys.BizProjectYearlySumVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <生产系统项目开工服务>
 *
 * @author anonymous
 * @create 13/02/2020 09:07
 * @since 1.0.0
 */
@Service
public class BizProjectService extends AppService<BizProjectDto> {

    private static final Logger log = LoggerFactory.getLogger(BizProjectService.class);

    @Autowired
    BizProjectDtoMapper bizProjectDtoMapper;

    @Autowired
    BizProjectWorkService bizProjectWorkService;

    @Autowired
    AttachService attachService;

    @Autowired
    BizProjectAttachService projectAttachService;

    @Autowired
    SysConfigService sysConfigService;

    @Autowired
    private BizCheckStepService bizCheckStepService;

    @Autowired
    CheckStepService checkStepService;

    @Autowired
    private BizProjectApprovalService activitiService;

    @Autowired
    UserService userService;

    @Autowired
    BizDealStatisticsService bizDealStatisticsService;

    @Value("${file.baseurl}")
    private String BASEURL;

    @Value("${file.recyclebinurl}")
    private String RECYCLEBINURL;

    public List<BizProjectDto> selectList(Map<String, Object> param) {
        return bizProjectDtoMapper.selectList(param);
    }

    @Transactional
    public boolean saveChain(BizProjectDto projectDto, List<BizProjectWorkDto> pwDtos, List<BizAttachDto> attachDtos, List<BizProjectAttachDto> projectAttachDtos, String type, boolean isMultiProject) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("contractName", projectDto.getContractName());
        List<BizProjectDto> bizProjectDtos = bizProjectDtoMapper.selectList(params);
        if (bizProjectDtos != null && !bizProjectDtos.isEmpty()) {
            throw new AppException("项目名称已存在，请重新发起！");
        }

        int save = save(projectDto);
        if (save != 1) {
            throw new AppException("保存项目出错！");
        }

        /*if (pwDtos != null && !pwDtos.isEmpty()) {
            for (BizProjectWorkDto pwDto : pwDtos) {
                if (1 != bizProjectWorkService.save(pwDto)) {
                    throw new AppException("保存项目业务出错！");
                }
            }
        }*/

        //2 保存附件
        for (BizAttachDto attachDto : attachDtos) {
            String fileUri = attachDto.getFileUri();
            String toFileUri = BASEURL + "PROJ" + "/" + projectDto.getContractNumber() + "/" + DateUtils.getDate() + "/";
            attachDto.setFileUri(toFileUri + attachDto.getFileName());
            log.info("fileUri:" + fileUri + ";toFileUri" + toFileUri);
            if (!attachService.removeFile(fileUri, toFileUri)) {
                throw new AppException("保存附件出错！");
            }
            if (1 != attachService.updateNotNull(attachDto)) {
                throw new AppException("保存附件出错！");
            }
        }
        //3 保存项目附件
        for (BizProjectAttachDto projAttachDto : projectAttachDtos) {
            if (1 != projectAttachService.save(projAttachDto)) {
                throw new AppException("保存项目附件出错！");
            }
        }
        // 4. 初始化审核流程
        if (ContractTypeEnum.TH.getKey().equals(projectDto.getContractType())) {
            // 3w以下直接返回，不走流程
            return true;
        }
        if (ContractTypeEnum.INSTRUCTION.getKey().equals(projectDto.getContractType()) || ContractTypeEnum.TRANSFER.getKey().equals(projectDto.getContractType())) {
            // （三）划拨/指令项目  需要走审批流程，具体流程待定；
            // 提出人-部门负责人-生产系统管理员—后续排流程
            SysUserDto userDto = userService.selectByKey(projectDto.getCreateUser());
            String stepNo = bizCheckStepService.selectMaxStepNo(projectDto.getContractId());
            activitiService.initActivitiOfInstruction(projectDto.getCreateUser(), projectDto.getContractId(), CheckTypeEnum.INSTRUCTION.getKey(), userDto.getDeptId(), stepNo, null);
            //新增履行合同统计记录
            bizDealStatisticsService.addSumDealInStatistics(projectDto);
        } else if (!projectDto.getReferUnit().equals(projectDto.getDealContractId())) {
            // 2、	代签合同，需要有实施单位负责人确认，只有确认后才同步到生产系统；
            // 流程：提出人-实施单位部门负责人审核（确认）
            String stepNo = bizCheckStepService.selectMaxStepNo(projectDto.getContractId());
            activitiService.initActivitiOfDelegation(projectDto.getCreateUser(), projectDto.getContractId(), CheckTypeEnum.DELEGATE.getKey(), projectDto.getReferUnit(), stepNo, null);
            //新增履行合同统计记录
            bizDealStatisticsService.addSumDealInStatistics(projectDto);
        } else if (isMultiProject) {
            // 1、	允许一个合同新建多个项目
            // 3、	该类项目需要走审批流程，具体流程如下；
            // 流程：提出人-实施单位部门负责人审核（确认）
            String stepNo = bizCheckStepService.selectMaxStepNo(projectDto.getContractId());
            activitiService.initActivitiOfMultipleProject(projectDto.getCreateUser(), projectDto.getContractId(), CheckTypeEnum.MULTIPROJECT.getKey(), projectDto.getReferUnit(), stepNo, null);
            //新增履行合同统计记录
            bizDealStatisticsService.addSumDealInStatistics(projectDto);
        } else {
            // otherwise no need approval, just sync contract to product system immediately
        }
        return true;
    }

    @Transactional
    public boolean deleteCascade(String contractId) {
        /*Map<String, Object> params = new HashMap<>();
        params.put("contractId", contractId);
        List<BizProjectWorkDto> projectWorkDtos = bizProjectWorkService.selectList(params);
        for (BizProjectWorkDto projectWorkDto : projectWorkDtos) {
            bizProjectWorkService.delete(projectWorkDto.getPwLinkId());
        }*/

        delete(contractId);
        return true;
    }

    @Transactional
    public boolean updateChain(BizProjectDto projectDto, List<BizProjectWorkDto> pwDtos, List<BizAttachDto> attachDtos, List<BizProjectAttachDto> projectAttachDtos, String type, String userId) throws Exception {
        String projId = projectDto.getContractId();
        Map<String, Object> params = new HashMap<>();
        params.put("projId", projId);
        //1 删除历史附件
        List<BizProjectAttachDto> bizProjAttachDtos = projectAttachService.selectList(params);

        //仍然保留的
        Map<String, String> remainMap = new HashMap<>();
        //数据库中所有的
        Map<String, String> allMap = new HashMap<>();
        for (BizProjectAttachDto bizProjAttachDto : bizProjAttachDtos) {
            for (BizAttachDto attachDto : attachDtos) {
                if (attachDto.getAttachId().equals(bizProjAttachDto.getAttachId())) {
                    //已有attachId 为key ,id值为value
                    remainMap.put(attachDto.getAttachId(), bizProjAttachDto.getId());
                    break;
                }
            }
            //已有attachId 为key ,id值为value
            allMap.put(bizProjAttachDto.getAttachId(), bizProjAttachDto.getId());

        }
        //得到删除了的附件
        allMap = MapUtils.removeAll(allMap, remainMap);
        //修改后不要的附件存放地址
        String updProToFileUri = RECYCLEBINURL + "PROJ" + "/" + projectDto.getContractNumber() + "/" + DateUtils.getDate() + "/";
        //保存不要的附件
        Set<String> keys = allMap.keySet();
        for (String key : keys) {
            BizAttachDto bizAttachDto = attachService.selectByKey(key);
            String fileUri = bizAttachDto.getFileUri();
            attachService.removeFile(fileUri,updProToFileUri);

        }
        for (Map.Entry<String, String> entry : allMap.entrySet()) {
            if (!attachService.deleteById(entry.getKey(),"")) {
                throw new AppException("修改工程时 删除历史附件出错！");
            }
            //2 删除合同附件记录
            if (1 != projectAttachService.delete(entry.getValue())) {
                throw new AppException("修改工程时 工程附件记录出错！");
            }
        }


        //3 修改工程
        updateNotNull(projectDto);

        // 新增履行合同统计记录（含删除）
        bizDealStatisticsService.addDealInStatistics(projectDto);

        //4 新增附件
        for (BizAttachDto attachDto : attachDtos) {

            //上传到临时目录时没有添加userId，故可以用来判断是否是新增的。空则表示新增
            if (StringUtils.isEmpty(attachDto.getUserId())) {
                String fileUri = attachDto.getFileUri();
                String toFileUri = BASEURL + "PROJ" + "/" + projectDto.getContractNumber() + "/" + DateUtils.getDate() + "/";
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
                    throw new AppException("修改项目时 新增附件出错！");
                }
                //保存合同附件
                BizProjectAttachDto projAttachDto = new BizProjectAttachDto();
                projAttachDto.setId(StringUtils.getUuid32());
                projAttachDto.setProjId(projectDto.getContractId());
                projAttachDto.setAttachId(attachDto.getAttachId());
                projectAttachService.save(projAttachDto);
            } else {
                if (1 != attachService.updateNotNull(attachDto)) {
                    throw new AppException("修改项目时 新增附件出错！");
                }
            }
        }

        return true;
    }

    /**
     * get New Project No for INSTRUCTION & TRANSFER project only.
     *
     * @param contractType INSTRUCTION or TRNASTER
     * @return
     */
    public String getProjectNo(String contractType) {
        String ctype = "ZL";
        if (ContractTypeEnum.INSTRUCTION.getKey().equals(contractType)) {
            ctype = "ZL";
        } else if (ContractTypeEnum.TRANSFER.getKey().equals(contractType)) {
            ctype = "HB";
        } else if (ContractTypeEnum.TH.getKey().equals(contractType)) {
            ctype = "TH";
        }
        int year = DateUtils.getYear();
        Map<String, Object> param = new HashMap<>();
        param.put("year", year);
        param.put("contractType", ctype);
        //获取到当前合同 年份 最大的 最后四位数
        String projNo = selectCurrentProjectNo(param);
        if (StringUtils.isEmpty(projNo)) {
            projNo = "0000";
        }
        return ctype + "-ajy-" + year + "-" + String.format("%04d", (Integer.valueOf(projNo) + 1));
    }

    private String selectCurrentProjectNo(Map<String, Object> param) {
        return bizProjectDtoMapper.selectCurrentProjectNo(param);
    }

    /**
     * @param contractId t_biz_project.contract_id
     * @return
     */
    public boolean isNeedSync(String contractId) {
        // 1. configuration first
        Map<String, Object> params = new HashMap<>();
        params.put("cfgCode", Constants.PRODSYS_SYNC_ENABLED);
        List<BizSysConfigDto> bizSysConfigDtos = sysConfigService.selectList(params);
        BizSysConfigDto bizSysConfigDto = bizSysConfigDtos.get(0);
        if (bizSysConfigDto != null && "0".equals(bizSysConfigDto.getCfgValue())) {
            return false;
        }

        params = new HashMap<>();
        params.put("projId", contractId);
        // 2. 三万以下项目直接同步
        BizProjectDto projectDto = selectByKey(contractId);
//        if (ContractTypeEnum.TH.getKey().equals(projectDto.getContractType())) {
//            return true;
//        } else {
//            // TODO 代签、划拨、指令项目需流程审核完
//            if (ContractTypeEnum.INSTRUCTION.getKey().equals(projectDto.getContractType()) || ContractTypeEnum.TRANSFER.getKey().equals(projectDto.getContractType())) {
//                // 划拨、指令
//            } else if (!projectDto.getReferUnit().equals(projectDto.getDealContractId())) {
//                // 代签
//            }
//        }
        Boolean a= !ContractStateEnum.UNCONFIRMED.getKey().equals(projectDto.getContractState()) && !ContractStateEnum.BACK.getKey().equals(projectDto.getContractState());
        return  a;
        //        return  !ContractStateEnum.UNCONFIRMED.getKey().equals(projectDto.getContractState()) && !ContractStateEnum.BACK.getKey().equals(projectDto.getContractState());
    }

    public List<BizProjectDto> selectUserList(Map<String, Object> param) {
        return bizProjectDtoMapper.selectUserList(param);
    }

    public List<BizProjectDto> selectAuditedList(Map<String, Object> param) {
        return bizProjectDtoMapper.selectAuditedList(param);
    }

    public String selectAuditCount(Map<String, Object> param) {
        return bizProjectDtoMapper.selectAuditCount(param);
    }

    @Deprecated
    public List<BizProjectDto> sumBySeason(Map<String, Object> params) {
        return bizProjectDtoMapper.sumBySeason(params);
    }

    @Deprecated
    public List<BizProjectDto> top3BySeason(Map<String, Object> params) {
        return bizProjectDtoMapper.top3BySeason(params);
    }

    @Deprecated
    public List<BizProjectDto> newMarketBySeason(Map<String, Object> params) {
        return bizProjectDtoMapper.newMarketBySeason(params);
    }

    public List<BizProjectYearlySumVo> sumByContractType(Map<String, Object> params) {
        return bizProjectDtoMapper.sumByContractType(params);
    }

    public String sumMonthly(Map<String, Object> params) {
        return bizProjectDtoMapper.sumMonthly(params);
    }
}

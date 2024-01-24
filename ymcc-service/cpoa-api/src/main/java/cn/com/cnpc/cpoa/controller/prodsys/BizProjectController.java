package cn.com.cnpc.cpoa.controller.prodsys;

import cn.com.cnpc.cpoa.common.annotation.Log;
import cn.com.cnpc.cpoa.common.enums.LogModule;
import cn.com.cnpc.cpoa.common.enums.LogType;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.core.page.TableDataInfo;
import cn.com.cnpc.cpoa.domain.*;
import cn.com.cnpc.cpoa.domain.prodsys.*;
import cn.com.cnpc.cpoa.enums.*;
import cn.com.cnpc.cpoa.enums.prodsys.*;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.service.CheckManService;
import cn.com.cnpc.cpoa.service.CheckStepService;
import cn.com.cnpc.cpoa.service.DealService;
import cn.com.cnpc.cpoa.service.prodsys.*;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.poi.ExcelUtil;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.prodsys.*;
import cn.com.cnpc.cpoa.web.base.BaseController;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * <生产系统项目管理>
 *
 * @author anonymous
 * @create 13/02/2020 22:05
 * @since 1.0.0
 */
@RestController
@RequestMapping("/prodsys")
public class BizProjectController extends BaseController {

    @Autowired
    BizProjectService bizProjectService;

    @Autowired
    CheckManService checkManService;

    @Autowired
    CheckStepService checkStepService;

    @Autowired
    BizProjectWorkService bizProjectWorkService;

    @Autowired
    BizDealPsJoinService bizDealPsJoinService;

    @Autowired
    ContractSyncService contractSyncService;

    @Autowired
    SysSubDeptService subDeptService;

    @Autowired
    AttachService attachService;

    @Autowired
    BizWorkTableService bizWorkTableService;

    @Autowired
    DealService dealService;

    @Autowired
    BizProjectManager bizProjectManager;

    @RequestMapping(value = "/project", method = RequestMethod.GET)
    public AppMessage query(@RequestParam(value = "dealId", defaultValue = "") String dealId,
                            @RequestParam(value = "contractId", defaultValue = "") String contractId
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", dealId);
        params.put("contractId", contractId);
        List<BizProjectDto> result = bizProjectService.selectList(params);
        return AppMessage.success(result, "查询项目成功");
    }

    @RequestMapping(value = "/project/all", method = RequestMethod.GET)
    public AppMessage queryAll(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                               @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                               @RequestParam(value = "dealId", defaultValue = "") String dealId,
                               @RequestParam(value = "contractId", defaultValue = "") String contractId,
                               @RequestParam(value = "contractName", defaultValue = "") String contractName,
                               @RequestParam(value = "contractNumber", defaultValue = "") String contractNumber,
                               @RequestParam(value = "contractPrice", defaultValue = "") String contractPrice,
                               @RequestParam(value = "contractPriceMin", defaultValue = "") String contractPriceMin,
                               @RequestParam(value = "contractPriceMax", defaultValue = "") String contractPriceMax,
                               @RequestParam(value = "clientId", defaultValue = "") String clientId,
                               @RequestParam(value = "marketType", defaultValue = "") String marketType,
                               @RequestParam(value = "companyType", defaultValue = "") String companyType,
                               @RequestParam(value = "contractType", defaultValue = "") String contractType,
                               @RequestParam(value = "workZone", defaultValue = "") String workZone,
                               @RequestParam(value = "workType", defaultValue = "") String workType,
                               @RequestParam(value = "payType", defaultValue = "") String payType,
                               @RequestParam(value = "referUnit", defaultValue = "") String referUnit,
                               @RequestParam(value = "responDepart", defaultValue = "") String responDepart,
                               @RequestParam(value = "contractBeginDateFlag", defaultValue = "") String contractBeginDateFlag,
                               @RequestParam(value = "contractBeginDate", defaultValue = "") String contractBeginDate,
                               @RequestParam(value = "contractPlanEndDateFlag", defaultValue = "") String contractPlanEndDateFlag,
                               @RequestParam(value = "contractPlanEndDate", defaultValue = "") String contractPlanEndDate,
                               @RequestParam(value = "dealName", defaultValue = "") String dealName,
                               @RequestParam(value = "syncStatus", defaultValue = "") String syncStatus,
                               @RequestParam(value = "dealContractId", defaultValue = "") String dealContractId,
                               @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                               @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd
    ) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", dealId);
        params.put("contractId", contractId);
        params.put("contractName", contractName);
        params.put("contractNumber", contractNumber);
        params.put("contractPrice", contractPrice);
        params.put("contractPriceMin", contractPriceMin);
        params.put("contractPriceMax", contractPriceMax);
        params.put("clientId", clientId);
        params.put("marketType", marketType);
        params.put("companyType", companyType);
        params.put("contractType", contractType);
        params.put("workZone", workZone);
        params.put("workType", workType);
        params.put("payType", payType);
        params.put("referUnit", referUnit);
        params.put("responDepart", responDepart);
        params.put("contractBeginDateFlag", contractBeginDateFlag);
        params.put("contractBeginDate", contractBeginDate);
        params.put("contractPlanEndDateFlag", contractPlanEndDateFlag);
        params.put("contractPlanEndDate", contractPlanEndDate);
        params.put("dealName", dealName);
        params.put("syncStatus", syncStatus);
        params.put("dealContractId", dealContractId);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        params.put("userId2", userId);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        List<BizProjectDto> result = bizProjectService.selectList(params);
        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(result);
        return AppMessage.success(dataTable, "查询项目成功");
    }

    @RequestMapping(value = "/project/exportAll", method = RequestMethod.GET)
    public AppMessage exportAllProjects(HttpServletResponse response,
                                        @RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                        @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                        @RequestParam(value = "dealId", defaultValue = "") String dealId,
                                        @RequestParam(value = "contractId", defaultValue = "") String contractId,
                                        @RequestParam(value = "contractName", defaultValue = "") String contractName,
                                        @RequestParam(value = "contractNumber", defaultValue = "") String contractNumber,
                                        @RequestParam(value = "contractPrice", defaultValue = "") String contractPrice,
                                        @RequestParam(value = "contractPriceMin", defaultValue = "") String contractPriceMin,
                                        @RequestParam(value = "contractPriceMax", defaultValue = "") String contractPriceMax,
                                        @RequestParam(value = "clientId", defaultValue = "") String clientId,
                                        @RequestParam(value = "marketType", defaultValue = "") String marketType,
                                        @RequestParam(value = "companyType", defaultValue = "") String companyType,
                                        @RequestParam(value = "contractType", defaultValue = "") String contractType,
                                        @RequestParam(value = "workZone", defaultValue = "") String workZone,
                                        @RequestParam(value = "workType", defaultValue = "") String workType,
                                        @RequestParam(value = "payType", defaultValue = "") String payType,
                                        @RequestParam(value = "referUnit", defaultValue = "") String referUnit,
                                        @RequestParam(value = "responDepart", defaultValue = "") String responDepart,
                                        @RequestParam(value = "contractBeginDateFlag", defaultValue = "") String contractBeginDateFlag,
                                        @RequestParam(value = "contractBeginDate", defaultValue = "") String contractBeginDate,
                                        @RequestParam(value = "contractPlanEndDateFlag", defaultValue = "") String contractPlanEndDateFlag,
                                        @RequestParam(value = "contractPlanEndDate", defaultValue = "") String contractPlanEndDate,
                                        @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                        @RequestParam(value = "syncStatus", defaultValue = "") String syncStatus,
                                        @RequestParam(value = "dealContractId", defaultValue = "") String dealContractId,
                                        @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                                        @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd
    ) throws Exception {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", dealId);
        params.put("contractId", contractId);
        params.put("contractName", contractName);
        params.put("contractNumber", contractNumber);
        params.put("contractPrice", contractPrice);
        params.put("contractPriceMin", contractPriceMin);
        params.put("contractPriceMax", contractPriceMax);
        params.put("clientId", clientId);
        params.put("marketType", marketType);
        params.put("companyType", companyType);
        params.put("contractType", contractType);
        params.put("workZone", workZone);
        params.put("workType", workType);
        params.put("payType", payType);
        params.put("referUnit", referUnit);
        params.put("responDepart", responDepart);
        params.put("contractBeginDateFlag", contractBeginDateFlag);
        params.put("contractBeginDate", contractBeginDate);
        params.put("contractPlanEndDateFlag", contractPlanEndDateFlag);
        params.put("contractPlanEndDate", contractPlanEndDate);
        params.put("dealName", dealName);
        params.put("syncStatus", syncStatus);
        params.put("dealContractId", dealContractId);
        params.put("createAtStart", createAtStart);
        params.put("createAtEnd", createAtEnd);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        params.put("userId2", userId);

        PageHelper.startPage(pageNo, pageSize);
        List<BizProjectDto> result = bizProjectService.selectList(params);
        // convert enum value
        if (result != null && !result.isEmpty()) {
            result.forEach(item -> {
                item.setWorkType(WorkTypeEnum.getEnumByKey(item.getWorkType()));
                item.setWorkZone(WorkZoneEnum.getEnumByKey(item.getWorkZone()));
                item.setContractState(ContractStateEnum.getEnumByKey(item.getContractState()));
                item.setMarketType(MarketTypeEnum.getEnumByKey(item.getMarketType()));
                item.setCompanyType(CompanyTypeEnum.getEnumByKey(item.getCompanyType()));
                item.setPayType(PayTypeEnum.getEnumByKey(item.getPayType()));
                item.setContractType(ContractTypeEnum.getEnumByKey(item.getContractType()));
            });
        }
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            ExcelUtil<BizProjectDto> util = new ExcelUtil<BizProjectDto>(BizProjectDto.class);
            return util.exportExcelBrowser(response, result, "项目开工汇总");
        } finally {
            if (null != outputStream) {
                outputStream.close();
            }
        }
    }

    @RequestMapping(value = "/project/pw", method = RequestMethod.GET)
    public AppMessage queryPws(@RequestParam(value = "contractId", defaultValue = "") String contractId
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("contractId", contractId);
        List<BizProjectWorkDto> result = bizProjectWorkService.selectList(params);
        return AppMessage.success(result, "查询项目业务成功");
    }

    @Log(logContent = "新增项目", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/project", method = RequestMethod.POST)
    public AppMessage add(@RequestBody BizProjectParamVo paramVo) throws Exception {
        BizProjectVo projectVo = paramVo.getProjectVo();
        List<BizProjectWorkVo> pwVos = paramVo.getPwVos();
        List<AttachVo> attachVos = paramVo.getAttachVos();
        List<BizProjectVo> projVos = paramVo.getProjVos();

        AppMessage appMsg = null;
        if (projVos != null && !projVos.isEmpty()) {
            // multi projects
            for (BizProjectVo projVo : projVos) {
                appMsg = add(projVo, pwVos, attachVos, true);
                if (!AppMessage.SUCCESS_CODE.equals(appMsg.getCode())) {
                    return appMsg;
                }
            }
        } else {
            appMsg = add(projectVo, pwVos, attachVos, false);
        }
        return appMsg;
    }

    @Log(logContent = "新增项目", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    private AppMessage add(BizProjectVo projectVo, List<BizProjectWorkVo> pwVos, List<AttachVo> attachVos, boolean isMultiProject) throws Exception {
        List<BizAttachDto> attachDtos = new ArrayList<>();
        List<BizProjectAttachDto> projAttachDtos = new ArrayList<>();
        String userId = ServletUtils.getSessionUserId();

        BizProjectDto projectDto = new BizProjectDto();
        projectDto.setContractId(StringUtils.getUuid32());
        projectDto.setDealId(projectVo.getDealId());
        projectDto.setClientId(projectVo.getClientId());
        projectDto.setClientName(projectVo.getClientName());
        projectDto.setApplyId(projectVo.getApplyId());
        projectDto.setApplyName(projectVo.getApplyName());
        projectDto.setContractName(projectVo.getContractName());
        projectDto.setReferUnit(projectVo.getReferUnit());
        projectDto.setReferUnitName(projectVo.getReferUnitName());
        projectDto.setMarketType(projectVo.getMarketType());
        projectDto.setCompanyType(projectVo.getCompanyType());
        projectDto.setContractType(projectVo.getContractType());
        projectDto.setWorkZone(projectVo.getWorkZone());
        projectDto.setWorkType(projectVo.getWorkType());
        projectDto.setContractState(ContractStateEnum.UNCONFIRMED.getKey());
        if (isMultiProject) {
            projectDto.setContractNumber(projectVo.getContractNumber());
            projectDto.setContractState(ContractStateEnum.UNCONFIRMED.getKey());
        } else if (ContractTypeEnum.INSTRUCTION.getKey().equals(projectDto.getContractType()) || ContractTypeEnum.TRANSFER.getKey().equals(projectDto.getContractType())) {
            projectDto.setContractNumber(bizProjectService.getProjectNo(projectDto.getContractType()));
            projectDto.setContractState(ContractStateEnum.UNCONFIRMED.getKey());
        } else if (ContractTypeEnum.TH.getKey().equals(projectDto.getContractType())) {
            projectDto.setContractNumber(bizProjectService.getProjectNo(projectDto.getContractType()));
            projectDto.setContractState(ContractStateEnum.CONFIRMED.getKey());
        } else if (!projectVo.getReferUnit().equals(projectVo.getDealContractId())) {
            projectDto.setContractNumber(projectVo.getContractNumber());
            projectDto.setContractState(ContractStateEnum.UNCONFIRMED.getKey());
        } else {
            projectDto.setContractNumber(projectVo.getContractNumber());
            projectDto.setContractState(ContractStateEnum.CONFIRMED.getKey());
        }
        projectDto.setContractDesc(projectVo.getContractDesc());
        projectDto.setResponDepart(projectVo.getResponDepart());
        projectDto.setContractBeginDate(projectVo.getContractBeginDate());
        projectDto.setContractPlanEndDate(projectVo.getContractPlanEndDate());
        projectDto.setContractEndDate(projectVo.getContractEndDate());
        projectDto.setContractPrice(projectVo.getContractPrice());
        projectDto.setPayType(projectVo.getPayType());
        projectDto.setNotify(projectVo.getNotify());
        projectDto.setCreateUser(userId);
        projectDto.setCreateDate(new Date());
        projectDto.setRemark(projectVo.getRemark());
        projectDto.setSyncStatus(0);
        projectDto.setDealContractId(projectVo.getDealContractId());

        //2 附件
        for (AttachVo attachVo : attachVos) {
            // String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");
            String attachId = attachVo.getAttachId();
            BizAttachDto attachDto = new BizAttachDto();
            attachDto.setAttachId(attachId);
            attachDto.setOwnerId(projectDto.getContractId());
            attachDto.setOwnerType(FileOwnerTypeEnum.PROJ.getKey());
            attachDto.setFileName(attachVo.getFileName());
            attachDto.setFileType(attachVo.getFileType());
            attachDto.setFileUri(attachVo.getFileUri());
            attachDto.setFileSize(attachVo.getFileSize());
            attachDto.setCreateTime(DateUtils.getNowDate());
            attachDto.setUserId(userId);
            attachDto.setNotes(attachVo.getNotes());
            attachDtos.add(attachDto);

            // 3工程附件
            BizProjectAttachDto projAttachDto = new BizProjectAttachDto();
            projAttachDto.setId(StringUtils.getUuid32());
            projAttachDto.setProjId(projectDto.getContractId());
            projAttachDto.setAttachId(attachId);
            projAttachDtos.add(projAttachDto);
        }

        List<BizProjectWorkDto> projectWorkDtos = new ArrayList<>();
        /*for (BizProjectWorkVo pwVo : pwVos) {
            BizProjectWorkDto projectWorkDto = new BizProjectWorkDto();
            projectWorkDto.setPwLinkId(StringUtils.getUuid32());
            projectWorkDto.setContractId(projectDto.getContractId());
            projectWorkDto.setClasId(pwVo.getClasId());
            projectWorkDto.setClasType(pwVo.getClasType());
            projectWorkDto.setPlanWorkNum(pwVo.getPlanWorkNum());
            projectWorkDto.setPlanDocNum(pwVo.getPlanDocNum());
            projectWorkDto.setInitWorkNum(pwVo.getInitWorkNum());
            projectWorkDto.setInitDocNum(pwVo.getInitDocNum());
            projectWorkDto.setUnit(pwVo.getUnit());
            projectWorkDto.setPrice(pwVo.getPrice());
            projectWorkDto.setCreateUser(sysUserDto.getUserName());
            projectWorkDto.setCreateDate(new Date());
            projectWorkDto.setRemark(pwVo.getRemark());

            projectWorkDtos.add(projectWorkDto);
        }*/

        if (!bizProjectService.saveChain(projectDto, projectWorkDtos, attachDtos, projAttachDtos, "", isMultiProject)) {
//        if (1 != bizProjectService.save(projectDto)) {
            return AppMessage.error("保存项目出错");
        }

        if (bizProjectService.isNeedSync(projectDto.getContractId())) {
            // 按策略同步到生产系统
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("contractId", projectDto.getContractId());
            List<BizProjectDto> result = bizProjectService.selectList(params);
            if (result == null || result.isEmpty())
                return AppMessage.error("错误，项目不存在");
            projectDto = result.get(0);
            SyncRet ret = notify(projectDto);
            if (!"200".equals(ret.getCode())) {
                return AppMessage.success(projectDto.getContractId(), "同步失败，您可以稍后重新下发。错误详情：" + ret.getMsg());
            }

            // update sync status
//        BizProjectDto projDto = new BizProjectDto();
//        projDto.setContractId(projectDto.getContractId());
//        projDto.setSyncStatus(1);
            projectDto.setSyncStatus(1);
            bizProjectService.updateNotNull(projectDto);
        }

        return AppMessage.success(projectDto.getContractId(), "保存项目成功");
    }

    @Log(logContent = "删除项目", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/project/{id}", method = RequestMethod.DELETE)
    public AppMessage delete(@PathVariable String id) {
        BizProjectDto projectDto = bizProjectService.selectByKey(id);
        String contractName = projectDto.getContractName();
        boolean delete = bizProjectService.deleteCascade(id);
        if (delete) {
            return AppMessage.success(contractName, "删除项目成功");
        }
        return AppMessage.errorObjId(contractName, "删除项目失败");
    }

    @Log(logContent = "修改项目", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/project/{id}", method = RequestMethod.PUT)
    public AppMessage update(@PathVariable String id, @RequestBody BizProjectParamVo paramVo) throws Exception {
        BizProjectDto project = bizProjectService.selectByKey(id);
        if (null == project) {
            throw new AppException("当前项目不存在");
        }

        BizProjectVo projectVo = paramVo.getProjectVo();
        List<BizProjectWorkVo> pwVos = paramVo.getPwVos();
        List<AttachVo> attachVos = paramVo.getAttachVos();

        List<BizAttachDto> attachDtos = new ArrayList<>();
        List<BizProjectAttachDto> projAttachDtos = new ArrayList<>();

        String userId = ServletUtils.getSessionUserId();
        SysUserDto sysUserDto = userService.selectByKey(userId);

        BizProjectDto projectDto = new BizProjectDto();
        projectDto.setContractId(id);
        projectDto.setDealId(projectVo.getDealId());
        projectDto.setClientId(projectVo.getClientId());
        projectDto.setClientName(projectVo.getClientName());
        projectDto.setApplyId(projectVo.getApplyId());
        projectDto.setApplyName(projectVo.getApplyName());
        projectDto.setContractName(projectVo.getContractName());
        projectDto.setReferUnit(projectVo.getReferUnit());
        projectDto.setMarketType(projectVo.getMarketType());
        projectDto.setCompanyType(projectVo.getCompanyType());
        projectDto.setContractType(projectVo.getContractType());
        projectDto.setWorkZone(projectVo.getWorkZone());
        projectDto.setWorkType(projectVo.getWorkType());
        projectDto.setContractNumber(projectVo.getContractNumber());
        projectDto.setContractDesc(projectVo.getContractDesc());
        projectDto.setResponDepart(projectVo.getResponDepart());
        projectDto.setContractBeginDate(projectVo.getContractBeginDate());
        projectDto.setContractPlanEndDate(projectVo.getContractPlanEndDate());
        projectDto.setContractEndDate(projectVo.getContractEndDate());
        projectDto.setContractPrice(projectVo.getContractPrice());
        projectDto.setPayType(projectVo.getPayType());
        projectDto.setNotify(projectVo.getNotify());
        projectDto.setContractState(projectVo.getContractState());
        projectDto.setCreateUser(projectVo.getCreateUser());
        projectDto.setCreateDate(projectVo.getCreateDate());
        projectDto.setUpdateUser(projectVo.getUpdateUser());
        projectDto.setUpdateDate(new Date());
        projectDto.setRemark(projectVo.getRemark());
        projectDto.setContractState(projectVo.getContractState());

        //2 附件
        for (AttachVo attachVo : attachVos) {
            // String attachId=UUID.randomUUID().toString().trim().replaceAll("-", "");

            if (attachService.isDoubleFile(attachVos)) {
                return AppMessage.errorObjId(id, "不能上传重复的文件！");
            }

            String attachId = attachVo.getAttachId();
            BizAttachDto attachDto = new BizAttachDto();
            attachDto.setAttachId(attachId);
            attachDto.setOwnerId(projectDto.getContractId());
            attachDto.setOwnerType(FileOwnerTypeEnum.PROJ.getKey());
            attachDto.setFileName(attachVo.getFileName());
            attachDto.setFileType(attachVo.getFileType());
            attachDto.setFileUri(attachVo.getFileUri());
            attachDto.setFileSize(attachVo.getFileSize());
            attachDto.setCreateTime(DateUtils.getNowDate());
            attachDto.setUserId(userId);
            attachDto.setNotes(attachVo.getNotes());
            attachDtos.add(attachDto);

            // 3工程附件
            BizProjectAttachDto projAttachDto = new BizProjectAttachDto();
            projAttachDto.setId(StringUtils.getUuid32());
            projAttachDto.setProjId(projectDto.getContractId());
            projAttachDto.setAttachId(attachId);
            projAttachDtos.add(projAttachDto);
        }

        List<BizProjectWorkDto> projectWorkDtos = new ArrayList<>();
        /*for (BizProjectWorkVo pwVo : pwVos) {
            BizProjectWorkDto projectWorkDto = new BizProjectWorkDto();
            projectWorkDto.setPwLinkId(StringUtils.getUuid32());
            projectWorkDto.setContractId(projectDto.getContractId());
            projectWorkDto.setClasId(pwVo.getClasId());
            projectWorkDto.setClasType(pwVo.getClasType());
            projectWorkDto.setPlanWorkNum(pwVo.getPlanWorkNum());
            projectWorkDto.setPlanDocNum(pwVo.getPlanDocNum());
            projectWorkDto.setInitWorkNum(pwVo.getInitWorkNum());
            projectWorkDto.setInitDocNum(pwVo.getInitDocNum());
            projectWorkDto.setUnit(pwVo.getUnit());
            projectWorkDto.setPrice(pwVo.getPrice());
            projectWorkDto.setCreateUser(pwVo.getCreateUser());
            projectWorkDto.setCreateDate(pwVo.getCreateDate());
            projectWorkDto.setUpdateUser(pwVo.getUpdateUser());
            projectWorkDto.setUpdateDate(pwVo.getUpdateDate());
            projectWorkDto.setRemark(pwVo.getRemark());

            projectWorkDtos.add(projectWorkDto);
        }*/

        if (!bizProjectService.updateChain(projectDto, projectWorkDtos, attachDtos, projAttachDtos, "", userId)) {
//        if(1 != bizProjectService.save(projectDto)) {
            return AppMessage.error("保存项目出错");
        }

        if (bizProjectService.isNeedSync(projectDto.getContractId())) {
            // 按策略同步到生产系统
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("contractId", projectDto.getContractId());
            List<BizProjectDto> result = bizProjectService.selectList(params);
            if (result == null || result.isEmpty())
                return AppMessage.error("错误，项目不存在");
            projectDto = result.get(0);
            SyncRet ret = notify(projectDto);
            if (!"200".equals(ret.getCode())) {
                return AppMessage.success(projectDto.getContractId(), "同步失败，您可以稍后重新下发。错误详情：" + ret.getMsg());
            }

            // update sync status
//        BizProjectDto projDto = new BizProjectDto();
//        projDto.setContractId(projectDto.getContractId());
//        projDto.setSyncStatus(1);
            projectDto.setSyncStatus(1);
            bizProjectService.updateNotNull(projectDto);
        }


        return AppMessage.success(projectDto.getContractId(), "保存项目成功");
    }

    @Log(logContent = "关联合同", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/dealjoin", method = RequestMethod.POST)
    public AppMessage joinDeal(@RequestBody BizDealPsJoinVO paramVo) throws Exception {
        BizDealPsJoinDto dealJoinDto = new BizDealPsJoinDto();
        dealJoinDto.setDealId(paramVo.getDealId());
        dealJoinDto.setDealJoinId(paramVo.getDealJoinId());

        Map<String, Object> params = new HashMap<>();
        params.put("dealId", paramVo.getDealId());
        params.put("dealJoinId", paramVo.getDealJoinId());
        List<BizDealPsJoinDto> joinDtos = bizDealPsJoinService.selectList(params);
        if (joinDtos == null || joinDtos.isEmpty()) {
            // new
            dealJoinDto.setId(StringUtils.getUuid32());
            if (1 != bizDealPsJoinService.save(dealJoinDto)) {
                return AppMessage.error("关联合同出错");
            }
        } else {
            // update
            dealJoinDto.setId(joinDtos.get(0).getId());
            if (bizDealPsJoinService.updateNotNull(dealJoinDto) < 1) {
                return AppMessage.error("关联合同出错");
            }
        }

        String userId = ServletUtils.getSessionUserId();
        // boolean isWriteSucc = bizDealPsJoinService.overwriteDealPsValue(paramVo.getDealJoinId(), userId);

        AppMessage projMsg = bizProjectManager.bizDealChanged(dealService.selectByKey(paramVo.getDealJoinId()));
        if (!AppMessage.SUCCESS_CODE.equals(projMsg.getCode())) {
            return AppMessage.success(dealJoinDto.getId(), "关联合同成功，但根据合同更新项目出错，\n错误原因：" + projMsg.getMessage());
        }
        return AppMessage.success(dealJoinDto.getId(), "关联合同成功");
    }

    @Log(logContent = "开工项目提交", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/project/submitt/{contractId}", method = RequestMethod.GET)
    public AppMessage resyncContractInfosubmitt(@PathVariable String contractId) throws Exception {


            try {
                BizProjectDto projDto = new BizProjectDto();
                projDto.setContractState("unconfirmed");

                projDto.setContractId(contractId);
                projDto.setSyncStatus(1);


                //查询
                BizCheckStepDto bizCheckStepDto = new BizCheckStepDto();
                bizCheckStepDto.setCheckObjId(contractId);
                List<BizCheckStepDto> select1 = checkStepService.select(bizCheckStepDto);
                for (BizCheckStepDto checkStepDto : select1) {
                    String stepState = checkStepDto.getStepState();
                    if (stepState==null){
                        continue;
                    }
                    if (stepState.equals("BACK")){
                        String stepId = checkStepDto.getStepId();
                        BizCheckStepDto bizCheckStepDto1 = checkStepService.selectByKey(stepId);
                        bizCheckStepDto1.setStepState(null);
                        checkStepService.updateAll(bizCheckStepDto1);

                        BizCheckManDto bizCheckManDto1 = new BizCheckManDto();
                        bizCheckManDto1.setStepId(stepId);
                        List<BizCheckManDto> select = checkManService.select(bizCheckManDto1);
                        BizCheckManDto bizCheckManDto2 = select.get(0);
//                        bizCheckManDto2.setCheckResult(null);
//                        bizCheckManDto2.setCheckNode(null);
                        bizCheckManDto2.setCheckTime(null);
                        bizCheckManDto2.setCheckState(CheckManStateEnum.PENDING.getKey());
                        checkManService.updateAll(bizCheckManDto2);
                    }
                }

                bizProjectService.updateNotNull(projDto);
            }catch (Exception e){
                return AppMessage.error("下发项目信息失败，接口反馈详情：");
            }
            return AppMessage.success(contractId, "重新下发成功");

    }

    @Log(logContent = "ProjName", logModule = LogModule.PRODSYS, logType = LogType.OPERATION)
    @RequestMapping(value = "/project/resyncContractInfo/{contractId}", method = RequestMethod.GET)
    public AppMessage resyncContractInfo(@PathVariable String contractId) throws Exception {
        if (bizProjectService.isNeedSync(contractId)) {
            // 按策略同步到生产系统
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("contractId", contractId);
            List<BizProjectDto> result = bizProjectService.selectList(params);
            if (result == null || result.isEmpty())
                return AppMessage.error("错误，项目信息不存在，请确保该合同已进行项目开工");
            BizProjectDto projectDto = result.get(0);
            SyncRet ret = notify(projectDto);
            if (!"200".equals(ret.getCode())) {
                return AppMessage.error("下发项目信息失败，接口反馈详情：" + ret.getMsg());
            }

            // update sync status
            try {
                BizProjectDto projDto = new BizProjectDto();
                projDto.setContractState("unconfirmed");

                projDto.setContractId(contractId);
                projDto.setSyncStatus(1);



                bizProjectService.updateNotNull(projDto);
            }catch (Exception e){
                return AppMessage.error("下发项目信息失败，接口反馈详情：");
            }



            return AppMessage.success(contractId, "重新下发成功");
        } else {
            return AppMessage.error(contractId, "未开放同步功能，请联系系统管理员");
        }
    }

    @Log(logContent = "接口同步", logModule = LogModule.PRODSYS, logType = LogType.SYSTEM)
    private SyncRet notify(BizProjectDto projectDto) throws InterruptedException, ExecutionException, TimeoutException {
        // async invoke
//        Future future = contractSyncService.invokeContractSync(projectDto);
//        while (!future.isDone()) {
//        }
//        return (SyncRet) future.get(1000, TimeUnit.MILLISECONDS);

        // sync invoke
        return contractSyncService.contractSync(projectDto);
    }

    @RequestMapping(value = "/subDepts", method = RequestMethod.GET)
    public AppMessage getSubDepts(@RequestParam(value = "refDeptId", defaultValue = "") String refDeptId,
                                  @RequestParam(value = "subDeptId", defaultValue = "") String subDeptId
    ) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("refDeptId", refDeptId);
        params.put("subDeptId", subDeptId);
        List<SysSubDeptDto> result = subDeptService.selectList(params);
        return AppMessage.success(result, "查询科室成功");
    }

    @RequestMapping(value = "/project/audit", method = RequestMethod.GET)
    public AppMessage queryAudit(@RequestParam(value = "pageNum", defaultValue = "1") int pageNo,
                                 @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
                                 @RequestParam(value = "dealId", defaultValue = "") String dealId,
                                 @RequestParam(value = "contractId", defaultValue = "") String contractId,
                                 @RequestParam(value = "contractName", defaultValue = "") String contractName,
                                 @RequestParam(value = "contractNumber", defaultValue = "") String contractNumber,
                                 @RequestParam(value = "contractPrice", defaultValue = "") String contractPrice,
                                 @RequestParam(value = "contractPriceMin", defaultValue = "") String contractPriceMin,
                                 @RequestParam(value = "contractPriceMax", defaultValue = "") String contractPriceMax,
                                 @RequestParam(value = "clientId", defaultValue = "") String clientId,
                                 @RequestParam(value = "marketType", defaultValue = "") String marketType,
                                 @RequestParam(value = "companyType", defaultValue = "") String companyType,
                                 @RequestParam(value = "contractType", defaultValue = "") String contractType,
                                 @RequestParam(value = "workZone", defaultValue = "") String workZone,
                                 @RequestParam(value = "workType", defaultValue = "") String workType,
                                 @RequestParam(value = "payType", defaultValue = "") String payType,
                                 @RequestParam(value = "referUnit", defaultValue = "") String referUnit,
                                 @RequestParam(value = "responDepart", defaultValue = "") String responDepart,
                                 @RequestParam(value = "contractBeginDateFlag", defaultValue = "") String contractBeginDateFlag,
                                 @RequestParam(value = "contractBeginDate", defaultValue = "") String contractBeginDate,
                                 @RequestParam(value = "contractPlanEndDateFlag", defaultValue = "") String contractPlanEndDateFlag,
                                 @RequestParam(value = "contractPlanEndDate", defaultValue = "") String contractPlanEndDate,
                                 @RequestParam(value = "dealName", defaultValue = "") String dealName,
                                 @RequestParam(value = "syncStatus", defaultValue = "") String syncStatus,
                                 @RequestParam(value = "dealContractId", defaultValue = "") String dealContractId,
                                 @RequestParam(value = "status", defaultValue = "") String status
    ) {
        String userId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(userId);
        String dataScope = userDto.getDataScope();

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("dealId", dealId);
        params.put("contractId", contractId);
        params.put("contractName", contractName);
        params.put("contractNumber", contractNumber);
        params.put("contractPrice", contractPrice);
        params.put("contractPriceMin", contractPriceMin);
        params.put("contractPriceMax", contractPriceMax);
        params.put("clientId", clientId);
        params.put("marketType", marketType);
        params.put("companyType", companyType);
        params.put("contractType", contractType);
        params.put("workZone", workZone);
        params.put("workType", workType);
        params.put("payType", payType);
        params.put("referUnit", referUnit);
        params.put("responDepart", responDepart);
        params.put("contractBeginDateFlag", contractBeginDateFlag);
        params.put("contractBeginDate", contractBeginDate);
        params.put("contractPlanEndDateFlag", contractPlanEndDateFlag);
        params.put("contractPlanEndDate", contractPlanEndDate);
        params.put("dealName", dealName);
        params.put("syncStatus", syncStatus);
        params.put("dealContractId", dealContractId);

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            params.put("dataScopeDept", userDto.getDeptId());
        } else {
            params.put("dataScopeSelf", userId);
        }
        params.put("userId2", userId);

        //1、设置分页信息，包括当前页数和每页显示的总计数
        PageHelper.startPage(pageNo, pageSize);
        List<BizProjectDto> result = new ArrayList<>();
        if ("audited".equals(status)) {
            result = bizProjectService.selectAuditedList(params);
        } else {
            result = bizProjectService.selectUserList(params);
        }

        //3、获取分页查询后的数据
        TableDataInfo dataTable = getDataTable(result);
        return AppMessage.success(dataTable, "查询项目成功");
    }

    @RequestMapping(value = "/detail",method = RequestMethod.GET)
    public AppMessage selectBizProjectDetail(@RequestParam(value = "checkObjId", defaultValue = "") String checkObjId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("contractId", checkObjId);
        String userId = ServletUtils.getSessionUserId();
        params.put("userId2", userId);
        List<BizProjectDto> bizProjectDtos = bizProjectService.selectUserList(params);
        if (bizProjectDtos.size() > 0) {
            return AppMessage.success(bizProjectDtos.get(0),"查询成功");
        }
        return AppMessage.error("失败");
    }

    @RequestMapping(value = "/project/audit/count", method = RequestMethod.GET)
    public AppMessage selectAuditCount(HttpServletRequest request) {
        String userId = ServletUtils.getSessionUserId();
        Map<String, Object> params = new HashMap<>();
        params.put("entrustUserId", userId);
        List<SysUserDto> userDtos = userService.selectPage(params);

        Map<String, Object> param = new HashMap<>();
        if (null != userDtos && userDtos.size() > 0) {
            param.put("entrustUserId", "true");
        }
        param.put("userId", userId);
        String selectAuditCount = bizProjectService.selectAuditCount(param);
        return AppMessage.success(selectAuditCount, "查询待审核项目开工数量成功");
    }

    @RequestMapping(value = "/project/projWork", method = RequestMethod.GET)
    public AppMessage getPWwithClas(@RequestParam(value = "contractId", defaultValue = "") String contractId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("contractId", contractId);
        List<BizProjectWorkDto> result = bizProjectWorkService.selectWithWorkList(params);
        return AppMessage.success(result, "查询项目业务成功");
    }

    /**
     * 项目开工查询合同下载
     *
     * @return
     */
    @Log(logContent = "合同（项目开工）下载", logModule = LogModule.DEAL, logType = LogType.OPERATION)
    @RequestMapping(value = "/deal/export", method = RequestMethod.GET)
    public AppMessage export(HttpServletResponse response,
                             @RequestParam(value = "dealNo", defaultValue = "") String dealNo,
                             @RequestParam(value = "dealName", defaultValue = "") String dealName,
                             @RequestParam(value = "userId", defaultValue = "") String userId,
                             @RequestParam(value = "dealValueMax", defaultValue = "") String dealValueMax,
                             @RequestParam(value = "dealValueMin", defaultValue = "") String dealValueMin,
                             @RequestParam(value = "contractId", defaultValue = "") String contractId,
                             @RequestParam(value = "dealSignTimeStart", defaultValue = "") String dealSignTimeStart,
                             @RequestParam(value = "dealSignTimeEnd", defaultValue = "") String dealSignTimeEnd,
                             @RequestParam(value = "createAtStart", defaultValue = "") String createAtStart,
                             @RequestParam(value = "createAtEnd", defaultValue = "") String createAtEnd,
                             @RequestParam(value = "dealType", defaultValue = "") String dealType,
                             @RequestParam(value = "dealId", defaultValue = "") String dealId,
                             @RequestParam(value = "statusList", defaultValue = "") String statusList,
                             @RequestParam(value = "dealStart", defaultValue = "") String dealStart,
                             @RequestParam(value = "dealStartFlag", defaultValue = "") String dealStartFlag,
                             @RequestParam(value = "dealEnd", defaultValue = "") String dealEnd,
                             @RequestParam(value = "dealEndFlag", defaultValue = "") String dealEndFlag,
                             @RequestParam(value = "deptId", defaultValue = "") String deptId,
                             @RequestParam(value = "dealIncome", defaultValue = "") String dealIncome,
                             @RequestParam(value = "typeList", defaultValue = "") String typeList,
                             @RequestParam(value = "hasJoinedDeal", defaultValue = "") String hasJoinedDeal,
                             @RequestParam(value = "forProj", defaultValue = "") String forProj) throws Exception {
        String sessionUserId = ServletUtils.getSessionUserId();
        SysUserDto userDto = userService.selectByKey(sessionUserId);
        String dataScope = userDto.getDataScope();

        Map<String, Object> param = new HashMap<>();
        //    param.put("userId",userId);
        param.put("userId2", userId);
        param.put("dealNo", dealNo);
        param.put("dealName", dealName);
        param.put("dealValueMax", dealValueMax);
        param.put("dealValueMin", dealValueMin);
        param.put("contractId", contractId);
        param.put("dealSignTimeStart", dealSignTimeStart);
        param.put("dealSignTimeEnd", dealSignTimeEnd);
        param.put("createAtStart", createAtStart);
        param.put("createAtEnd", createAtEnd);
        param.put("dealType", dealType);
        param.put("dealId", dealId);
        param.put("deptId", deptId);
        if (StringUtils.isNotEmpty(typeList)) {
            param.put("typeList", typeList.split(","));
        }
        param.put("dealIncome", dealIncome);
        param.put("hasJoinedDeal", hasJoinedDeal);
        param.put("forProj", forProj);

        //仅针对我的合同导出
        if (StringUtils.isNotEmpty(userId)) {
            param.put("userIdSpecial", userId);
            param.put("deptIdSpecial", userDto.getDeptId());
        }

        if (StringUtils.isNotEmpty(statusList)) {
            //合同状态
            param.put("statusList", statusList.split(","));
        }

        //履行开始时间
        param.put("dealStart", dealStart);
        //履行开始符号大于> 小于<
        param.put("dealStartFlag", dealStartFlag);
        //履行结束时间
        param.put("dealEnd", dealEnd);
        //履行结束符号大于> 小于<
        param.put("dealEndFlag", dealEndFlag);

        if (StringUtils.isNotEmpty(typeList)) {
            //合同状态
            param.put("typeList", typeList.split(","));
        }

        if (UserDataScopeEnum.ALL.getKey().equalsIgnoreCase(dataScope)) {
            //params.put("dataScopeAll", "dataScopeAll");
        } else if (UserDataScopeEnum.DEPT.getKey().equalsIgnoreCase(dataScope)) {
            param.put("dataScopeDept", userDto.getDeptId());
        } else {
            param.put("dataScopeSelf", sessionUserId);
        }

        List<BizDealDto> sysDealDtos = dealService.selectListForExport(param);
        List<DealProjVo> dealVoS = new ArrayList<>();

        for (BizDealDto dealDto : sysDealDtos) {
            DealProjVo dealVo = new DealProjVo();
            //dealVo.setDealSignTime(DateUtils.);
            BeanUtils.copyBeanProp(dealVo, dealDto);
            dealVo.setDealStatus(DealStatusEnum.getEnumByKey(dealDto.getDealStatus()));
            dealVo.setSettleStatus(SettlementStatusEnum.getEnumByKey(dealDto.getSettleStatus()));
            dealVo.setDealType(DealTypeEnum.getEnumByKey(dealDto.getDealType()));
            dealVoS.add(dealVo);
        }
        ExcelUtil<DealProjVo> util = new ExcelUtil<DealProjVo>(DealProjVo.class);
        return util.exportExcelBrowser(response, dealVoS, "合同信息（项目开工）");
    }
}

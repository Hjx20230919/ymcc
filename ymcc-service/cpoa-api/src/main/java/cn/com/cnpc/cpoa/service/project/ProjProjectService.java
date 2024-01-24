package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.assembler.project.ProjProjectAssembler;
import cn.com.cnpc.cpoa.assembler.project.ProjProjectNotenderAssembler;
import cn.com.cnpc.cpoa.assembler.project.ProjProjectOpentenderAssembler;
import cn.com.cnpc.cpoa.common.constants.ParamConstant;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.domain.project.*;
import cn.com.cnpc.cpoa.enums.CheckManResultEnum;
import cn.com.cnpc.cpoa.enums.DealWebTypeEnum;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.enums.project.*;
import cn.com.cnpc.cpoa.mapper.project.BizProjProjectDtoMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.po.project.ProActivitiItemPo;
import cn.com.cnpc.cpoa.po.project.ProjProjectPo;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.DealService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.constractor.ContContractorService;
import cn.com.cnpc.cpoa.service.constractor.ContCreditAttachService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.pdf.ProjPurcPDFBuildUtils;
import cn.com.cnpc.cpoa.vo.project.*;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static cn.com.cnpc.cpoa.assembler.project.ProjProjectAssembler.convertDtoToVo;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 19:20
 * @Description:
 */
@Service
public class ProjProjectService extends AppService<BizProjProjectDto> {


    @Autowired
    UserService userService;
    @Autowired
    AttachService attachService;
    @Autowired
    ProjProjectAttachService projProjectAttachService;

    @Autowired
    ContCreditAttachService contCreditAttachService;

    @Autowired
    BizProjProjectDtoMapper bizProjProjectDtoMapper;

    @Autowired
    BizCheckStepService bizCheckStepService;

    @Autowired
    ProjectAuditService projectAuditService;

    @Autowired
    DealService dealService;

    @Autowired
    ProjSelContService projSelContService;

    @Autowired
    ProjContListService projContListService;

    @Autowired
    ProjProjectNotenderService projProjectNotenderService;

    @Autowired
    ProjProjectOpentenderService projProjectOpentenderService;

    @Autowired
    ProjPurcPlanService projPurcPlanService;

    @Autowired
    ProjPurcResultService projPurcResultService;

    @Autowired
    private ProjChangeLogService changeLogService;

    @Autowired
    private ContContractorService contractorService;



    @Transactional(rollbackFor = Exception.class)
    public BizProjProjectDto addProject(ProjProjectVo vo, String userId, String type) throws Exception {

        String projId = StringUtils.getUuid32();
        SysUserDto userDto = userService.selectByKey(userId);

        BizProjProjectDto dto = ProjProjectAssembler.convertVoToDto(vo);
        dto.setProjId(projId);
        dto.setDealNo(getDealNo(ProjectConstant.ProjPhaseType.dealNoMap.get(vo.getSelContType())));
        dto.setProjPhase(ProjPhaseEnum.PROPROJECT.getKey());
        dto.setProjStatus(ProjectWebTypeEnum.getEnumByKey(type));
        dto.setCreateId(userId);
        dto.setOwnerDeptId(userDto.getDeptId());
        dto.setCreateAt(DateUtils.getNowDate());

        //新增公开招标 可不招标逻辑
        if (SelContTypeEnum.OPENTENDER.getKey().equals(vo.getSelContType())) {
            BizProjProjectOpentenderDto opentenderDto = ProjProjectOpentenderAssembler.convertVoToPo(vo);
            opentenderDto.setOtenderId(StringUtils.getUuid32());
            opentenderDto.setProjId(projId);
            opentenderDto.setOtenderSelContType(vo.getSelContType());
            projProjectOpentenderService.save(opentenderDto);
        } else if (SelContTypeEnum.NOTENDER.getKey().equals(vo.getSelContType())) {
            BizProjProjectNotenderDto notenderDto = ProjProjectNotenderAssembler.convertVoToPo(vo);
            notenderDto.setNtenderId(StringUtils.getUuid32());
            notenderDto.setProjId(projId);
            notenderDto.setNtenderSelContType(vo.getSelContType());
            projProjectNotenderService.save(notenderDto);

        }

        //新增附件
        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getProjId(), FileOwnerTypeEnum.PROJECT.getKey(), vo.getAttachVos());
        //获取中间表
        List<BizProjProjectAttachDto> proProjectAttachDtos = projProjectAttachService.getProProjectAttachDtos(dto.getProjId(), attachDtos);

        //保存前确保当前编号没被占用
        Map<String, Object> params = new HashMap<>();
        params.put("dealNo", dto.getDealNo());
        List<BizProjProjectDto> dtos = bizProjProjectDtoMapper.selectProject(params);
        if (StringUtils.isNotEmpty(dtos)) {
            throw new AppException("抱歉，当前编码已被占用，请重新发起");
        }
        save(dto);

        List<BizProjContListDto> projContListDtos = projSelContService.getProjContListDtos(vo.getProjContListVos(), dto.getProjId());
        projContListService.saveList(projContListDtos);

        //保存附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.PROPROJECT.getKey(), dto.getSelContType(), dto.getDealNo());
        attachService.updateAttachs(attachDtos, userId, proToFileUri);

        //保存中間表
        projProjectAttachService.saveList(proProjectAttachDtos);

        //projProjectAuditService
        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.PROPROJECTSERVICE);
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            auditService.initActiviti(userId, dto.getProjId());
        }
        return dto;


    }

    public String getDealNo(String projPhaseType) {

        int year = DateUtils.getYear();
        Map<String, Object> param = new HashMap<>();
        param.put("projPhaseType", projPhaseType);
        param.put("year", year);
        String dealNo4 = null;
        //获取到当前合同 年份 最大的 最后四位数
        if (ProjectConstant.ProjPhaseType.LX.equals(projPhaseType)) {
            dealNo4 = selectCurrentDealNo(param);
        } else {
            dealNo4 = selectCurrentDealNo2(param);
        }
        if (StringUtils.isEmpty(dealNo4)) {
            dealNo4 = "0000";
        }
        return year + "-" + projPhaseType + "-" + String.format("%04d", (Integer.valueOf(dealNo4) + 1));
    }

    private String selectCurrentDealNo(Map<String, Object> param) {

        return bizProjProjectDtoMapper.selectCurrentDealNo(param);
    }

    private String selectCurrentDealNo2(Map<String, Object> param) {

        return bizProjProjectDtoMapper.selectCurrentDealNo2(param);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProject(ProjProjectVo vo, String userId, String type) throws Exception {
        String projId = vo.getProjId();
        BizProjProjectDto projProjectDto = selectByKey(projId);

        BizProjProjectDto dto = ProjProjectAssembler.convertVoToDto(vo);
        dto.setProjStatus(ProjectWebTypeEnum.getEnumByKey(type));
        dto.setDealNo(null);

        //新增公开招标 可不招标逻辑
        if (SelContTypeEnum.OPENTENDER.getKey().equals(vo.getSelContType())) {
            BizProjProjectOpentenderDto opentenderDto = ProjProjectOpentenderAssembler.convertVoToPo(vo);
            projProjectOpentenderService.updateNotNull(opentenderDto);
        } else if (SelContTypeEnum.NOTENDER.getKey().equals(vo.getSelContType())) {
            BizProjProjectNotenderDto notenderDto = ProjProjectNotenderAssembler.convertVoToPo(vo);
            projProjectNotenderService.updateNotNull(notenderDto);
        }

        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getProjId(), FileOwnerTypeEnum.PROJECT.getKey(), vo.getAttachVos());

        //获得已存在的中间表信息
        Map<String, Object> params = new HashMap<>();
        params.put("projId", dto.getProjId());
        List<BizProjProjectAttachDto> projProjectAttachDtos = projProjectAttachService.selectProjProjectAttachDto(params);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getProRemoveMap(attachDtos, projProjectAttachDtos);
        //修改后不要的附件存放地址
        String updProToFileUri = attachService.getupdProToFileUri(ProjPhaseEnum.PROPROJECT.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        //保存不要的附件
        Set<String> keys = removeMap.keySet();
        for (String key : keys) {
            BizAttachDto bizAttachDto = attachService.selectByKey(key);
            String fileUri = bizAttachDto.getFileUri();
            attachService.removeFile1(fileUri,updProToFileUri);

        }

//        attachService.updateAttachs(attachDtos, userId, updProToFileUri);
        //2 删除附件
        attachService.deleteByMap(removeMap);
        //3 删除中间表
        projProjectAttachService.deleteByMap(removeMap);
        //4 执行更新
        updateNotNull(dto);


        //删除选商列表
        Map<String, Object> param = new HashMap<>();
        param.put("selContId", dto.getProjId());
        List<BizProjContListDto> removeContList = projContListService.selectProjContList(param);
        List<Object> contListKey = new ArrayList<>();
        for (BizProjContListDto projContListDto : removeContList) {
            contListKey.add(projContListDto.getContListId());
        }
        projContListService.deleteList(contListKey);

        //保存选商列表
        List<BizProjContListDto> projContListDtos = projSelContService.getProjContListDtos(vo.getProjContListVos(), dto.getProjId());
        projContListService.saveList(projContListDtos);


        //5 新增附件 返回新增的附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.PROPROJECT.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri);
        //6 为新增的附件保存 中间表
        List<BizProjProjectAttachDto> newprojProjectAttachDtos = projProjectAttachService.getProProjectAttachDtos(dto.getProjId(), newAttachDtos);
        projProjectAttachService.saveList(newprojProjectAttachDtos);

        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.PROPROJECTSERVICE);

        //7 如果状态为 保存提交 则生成审核相关
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            auditService.initActiviti(userId, dto.getProjId());
        }
        //8 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if (DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)) {
            getBackPendingMessage(projId, ProjectConstant.AuditService.PROPROJECTSERVICE, auditService);
            bizCheckStepService.updateBackObj(dto.getProjId());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void maintainProject(ProjProjectVo vo, String userId, String projId) throws Exception {
        BizProjProjectDto projProjectDto = selectByKey(projId);
        StringBuffer sb = new StringBuffer();
        //日志记录
        ProjChangeLogDto changeLogDto = new ProjChangeLogDto();
        changeLogDto.setChgLogId(StringUtils.getUuid32());
        changeLogDto.setCreateId(userId);
        changeLogDto.setCreateAt(DateUtils.getNowDate());
        changeLogDto.setChgObjType("proProject");
        changeLogDto.setChgObjId(projId);

        Console.error("projProjectDto-"+projProjectDto.getProjId());
        //修改工程服务名称
        if(StrUtil.isNotBlank(vo.getProjName())){
            sb.append("工程,服务名称修改:").append("\n");
            sb.append(projProjectDto.getProjName()).append("->").append(vo.getProjName()).append("\n");
            projProjectDto.setProjName(vo.getProjName());
        }
        if(StrUtil.isNotBlank(vo.getDealContract())){
            sb.append("我方签约单位修改:").append("\n");
            sb.append(projProjectDto.getDealContract()).append("->").append(vo.getDealContract()).append("\n");
            projProjectDto.setDealContract(vo.getDealContract());
        }
        if(StrUtil.isNotBlank(vo.getDealName())){
            sb.append("拟签订合同名称修改:").append("\n");
            sb.append(projProjectDto.getDealName()).append("->").append(vo.getDealName()).append("\n");
            projProjectDto.setDealName(vo.getDealName());
        }

        if (SelContTypeEnum.OPENTENDER.getKey().equals(vo.getSelContType())) {
            updateOpenTender(sb,vo);
        } else if (SelContTypeEnum.NOTENDER.getKey().equals(vo.getSelContType())) {
            updateNoTender(sb,vo,projId);
        }else {
           updateProjProject(sb,projProjectDto,vo);
        }

        //附件修改
        updateAttach(sb,projProjectDto,vo,projId,userId);
        // 执行更新
        updateNotNull(projProjectDto);
        changeLogDto.setChgObjCtx(sb.toString());
        changeLogService.save(changeLogDto);
    }

    /**
     * 单一来源，竞争谈判，内则书修改
     * @param sb
     * @param projProjectDto
     * @param vo
     */
    private void updateProjProject(StringBuffer sb,BizProjProjectDto projProjectDto,ProjProjectVo vo){
        //对应收入情况，服务合同立项原因描述，承包商资质要求，承包商技术服务人员要求，商务计价方式及费用测算情况，备注，单一来源理由
        if (!projProjectDto.getIncomeInfo().equals(vo.getIncomeInfo())){
            sb.append("对应收入情况修改：").append("\n");
            sb.append(projProjectDto.getIncomeInfo()).append("->").append(vo.getIncomeInfo()).append("\n");
            projProjectDto.setIncomeInfo(vo.getIncomeInfo());
        }
        if (!projProjectDto.getApplDesc().equals(vo.getApplDesc())){
            sb.append("服务合同立项原因描述修改：").append("\n");
            sb.append(projProjectDto.getApplDesc()).append("->").append(vo.getApplDesc()).append("\n");
            projProjectDto.setApplDesc(vo.getApplDesc());
        }
        if (!projProjectDto.getContQlyReq().equals(vo.getContQlyReq())){
            sb.append("承包商资质要求修改：").append("\n");
            sb.append(projProjectDto.getContQlyReq()).append("->").append(vo.getContQlyReq()).append("\n");
            projProjectDto.setContQlyReq(vo.getContQlyReq());
        }
        if (!projProjectDto.getContSvrReq().equals(vo.getContSvrReq())){
            sb.append("承包商技术服务人员要求修改：").append("\n");
            sb.append(projProjectDto.getContSvrReq()).append("->").append(vo.getContSvrReq()).append("\n");
            projProjectDto.setContSvrReq(vo.getContSvrReq());
        }
        if (!projProjectDto.getDealValueMeasure().equals(vo.getDealValueMeasure())){
            sb.append("商务计价方式及费用测算情况修改：").append("\n");
            sb.append(projProjectDto.getDealValueMeasure()).append("->").append(vo.getDealValueMeasure()).append("\n");
            projProjectDto.setDealValueMeasure(vo.getDealValueMeasure());
        }
        if (!(projProjectDto.getNotes() == null ? "" : projProjectDto.getNotes()).equals(vo.getNotes())){
            sb.append("备注修改：").append("\n");
            sb.append(projProjectDto.getNotes()).append("->").append(vo.getNotes()).append("\n");
            projProjectDto.setNotes(vo.getNotes());
        }
        if (!(projProjectDto.getProjNotes() == null ? "" : projProjectDto.getProjNotes()).equals(vo.getProjNotes())){
            sb.append("单一来源理由修改：").append("\n");
            sb.append(projProjectDto.getProjNotes()).append("->").append(vo.getProjNotes()).append("\n");
            projProjectDto.setProjNotes(vo.getProjNotes());
        }

    }

    /**
     * 可不招标修改
     * @param sb
     * @param vo
     * @param projId
     */
    private void updateNoTender(StringBuffer sb,ProjProjectVo vo,String projId){
        String ntenderId = vo.getNtenderId();
        BizProjProjectNotenderDto notenderDto = projProjectNotenderService.selectByKey(ntenderId);
        //采购起止日期，商务计价方式，项目概述，可不招标事项理由，采购小组，监督组成，
        if (!DateUtils.dateTimeYYYY_MM_DD(notenderDto.getNtenderStartDate()).equals(vo.getNtenderStartDate())){
            sb.append("采购开始时间修改：").append("\n");
            sb.append(DateUtils.dateTimeYYYY_MM_DD(notenderDto.getNtenderStartDate())).append("->").append(vo.getNtenderStartDate()).append("\n");
            notenderDto.setNtenderStartDate(DateUtils.dateTime("yyyy-MM-dd",vo.getNtenderStartDate()));
        }
        if (!DateUtils.dateTimeYYYY_MM_DD(notenderDto.getNtenderEndDate()).equals(vo.getNtenderEndDate())){
            sb.append("采购结束时间修改：").append("\n");
            sb.append(DateUtils.dateTimeYYYY_MM_DD(notenderDto.getNtenderEndDate())).append("->").append(vo.getNtenderEndDate()).append("\n");
            notenderDto.setNtenderEndDate(DateUtils.dateTime("yyyy-MM-dd",vo.getNtenderEndDate()));
        }
        if (!notenderDto.getNtenderValutionType().equals(vo.getNtenderValutionType())){
            sb.append("商务计价方式修改：").append("\n");
            sb.append(notenderDto.getNtenderValutionType()).append("->").append(vo.getNtenderValutionType()).append("\n");
            notenderDto.setNtenderValutionType(vo.getNtenderValutionType());
        }
        if (!notenderDto.getNtenderSummary().equals(vo.getNtenderSummary())){
            sb.append("项目概述修改：").append("\n");
            sb.append(notenderDto.getNtenderSummary()).append("->").append(vo.getNtenderSummary()).append("\n");
            notenderDto.setNtenderSummary(vo.getNtenderSummary());
        }
        if (!notenderDto.getNtenderReason().equals(vo.getNtenderReason())){
            sb.append("可不招标事项理由修改：").append("\n");
            sb.append(notenderDto.getNtenderReason()).append("->").append(vo.getNtenderReason()).append("\n");
            notenderDto.setNtenderReason(vo.getNtenderReason());
        }
        if (!notenderDto.getNtenderGroup().equals(vo.getNtenderGroup())){
            sb.append("采购小组修改：").append("\n");
            sb.append(notenderDto.getNtenderGroup()).append("->").append(vo.getNtenderGroup()).append("\n");
            notenderDto.setNtenderGroup(vo.getNtenderGroup());
        }
        if (!notenderDto.getNtenderSupervise().equals(vo.getNtenderSupervise())){
            sb.append("监督组成修改：").append("\n");
            sb.append(notenderDto.getNtenderSupervise()).append("->").append(vo.getNtenderSupervise()).append("\n");
            notenderDto.setNtenderSupervise(vo.getNtenderSupervise());
        }

        //选商修改
        updateSelCont(sb,vo,projId);

        projProjectNotenderService.updateNotNull(notenderDto);
    }

    /**
     * 选商修改
     * @param sb
     * @param vo
     * @param projId
     */
    private void updateSelCont(StringBuffer sb,ProjProjectVo vo,String projId){
        //删除选商列表
        Map<String, Object> param = new HashMap<>();
        param.put("selContId", projId);
        List<BizProjContListDto> removeContList = projContListService.selectProjContList(param);
        List<String> oldContList = new ArrayList<>();
        for (BizProjContListDto projContListDto : removeContList) {
            oldContList.add(projContListDto.getContId());
        }
        List<String> newContList = new ArrayList<>();
        for (ProjContListVo projContListVo : vo.getProjContListVos()) {
            newContList.add(projContListVo.getContId());
        }

        sb.append("新增承包商:");
        for (String contId : newContList) {
            //如果新的承包商没在原来里面，则为新增的承包商
            if (!oldContList.contains(contId)){
                BizContContractorDto contractorDto = contractorService.selectByKey(contId);
                sb.append(contractorDto.getContName()).append(";");
            }
        }
        sb.append("\n");
        sb.append("删除承包商:");
        for (String contId : oldContList) {
            //如果原来的承包商不在新加的里面，则该承包商已被删除
            if (!newContList.contains(contId)){
                BizContContractorDto contractorDto = contractorService.selectByKey(contId);
                sb.append(contractorDto.getContName()).append(";");
            }
        }
        sb.append("\n");

        List<Object> contListKey = new ArrayList<>();
        for (BizProjContListDto projContListDto : removeContList) {
            contListKey.add(projContListDto.getContListId());
        }
        projContListService.deleteList(contListKey);

        //保存选商列表
        List<BizProjContListDto> projContListDtos = projSelContService.getProjContListDtos(vo.getProjContListVos(),projId);
        projContListService.saveList(projContListDtos);
    }


    /**
     * 公开招标修改
     * @param sb
     * @param vo
     */
    private void updateOpenTender(StringBuffer sb,ProjProjectVo vo){
        String otenderId = vo.getOtenderId();
        BizProjProjectOpentenderDto opentenderDto = projProjectOpentenderService.selectByKey(otenderId);
        //招标起止日期，商务计价方式，招标内容，招标组织形式，招标评委会组成，监督组成，投标人资格要求
        if (!DateUtils.dateTimeYYYY_MM_DD(opentenderDto.getOtenderStartDate()).equals(vo.getOtenderStartDate())){
            sb.append("招标开始时间修改：").append("\n");
            sb.append(DateUtils.dateTimeYYYY_MM_DD(opentenderDto.getOtenderStartDate())).append("->").append(vo.getOtenderStartDate()).append("\n");
            opentenderDto.setOtenderStartDate(DateUtils.dateTime("yyyy-MM-dd",vo.getOtenderStartDate()));
        }
        if (!DateUtils.dateTimeYYYY_MM_DD(opentenderDto.getOtenderEndDate()).equals(vo.getOtenderEndDate())){
            sb.append("招标结束时间修改：").append("\n");
            sb.append(DateUtils.dateTimeYYYY_MM_DD(opentenderDto.getOtenderEndDate())).append("->").append(vo.getOtenderEndDate()).append("\n");
            opentenderDto.setOtenderEndDate(DateUtils.dateTime("yyyy-MM-dd",vo.getOtenderEndDate()));
        }
        if (!opentenderDto.getOtenderValutionType().equals(vo.getOtenderValutionType())){
            sb.append("商务计价方式修改：").append("\n");
            sb.append(opentenderDto.getOtenderValutionType()).append("->").append(vo.getOtenderValutionType()).append("\n");
            opentenderDto.setOtenderValutionType(vo.getOtenderValutionType());
        }
        if (!opentenderDto.getOtenderContent().equals(vo.getOtenderContent())){
            sb.append("招标内容修改：").append("\n");
            sb.append(opentenderDto.getOtenderContent()).append("->").append(vo.getOtenderContent()).append("\n");
            opentenderDto.setOtenderContent(vo.getOtenderContent());
        }
        if (!opentenderDto.getOtenderModality().equals(vo.getOtenderModality())){
            sb.append("招标组织形式修改：").append("\n");
            sb.append(opentenderDto.getOtenderModality()).append("->").append(vo.getOtenderModality()).append("\n");
            opentenderDto.setOtenderModality(vo.getOtenderModality());
        }
        if (!opentenderDto.getOtenderCommittee().equals(vo.getOtenderCommittee())){
            sb.append("招标评委会组成修改：").append("\n");
            sb.append(opentenderDto.getOtenderCommittee()).append("->").append(vo.getOtenderCommittee()).append("\n");
            opentenderDto.setOtenderCommittee(vo.getOtenderCommittee());
        }
        if (!opentenderDto.getOtenderSupervise().equals(vo.getOtenderSupervise())){
            sb.append("监督组成修改：").append("\n");
            sb.append(opentenderDto.getOtenderSupervise()).append("->").append(vo.getOtenderSupervise()).append("\n");
            opentenderDto.setOtenderSupervise(vo.getOtenderSupervise());
        }
        if (!opentenderDto.getOtenderQualifications().equals(vo.getOtenderQualifications())){
            sb.append("投标人资格要求修改：").append("\n");
            sb.append(opentenderDto.getOtenderQualifications()).append("->").append(vo.getOtenderQualifications()).append("\n");
            opentenderDto.setOtenderQualifications(vo.getOtenderQualifications());
        }
        projProjectOpentenderService.updateNotNull(opentenderDto);
    }

    /**
     * 附件修改
     * @param sb
     * @param projProjectDto
     * @param vo
     * @param projId
     * @param userId
     */
    private void updateAttach(StringBuffer sb,BizProjProjectDto projProjectDto,ProjProjectVo vo,String projId,String userId) throws Exception {
        if (Optional.ofNullable(vo.getAttachVos()).isPresent()){
            List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(projId, FileOwnerTypeEnum.PROJECT.getKey(), vo.getAttachVos());
            //获得已存在的中间表信息
            sb.append("附件修改:").append("\n");
            Map<String, Object> params = new HashMap<>();
            params.put("projId", projId);
            List<BizProjProjectAttachDto> projProjectAttachDtos = projProjectAttachService.selectProjProjectAttachDto(params);
            //1获取要删除的附件信息
            Map<String, String> removeMap = attachService.getProRemoveMap(attachDtos, projProjectAttachDtos);
            //修改后不要的附件存放地址
            String updProToFileUri = attachService.getupdProToFileUri(ProjPhaseEnum.PROPROJECT.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
            //保存不要的附件
            Set<String> keys = removeMap.keySet();
            for (String key : keys) {
                BizAttachDto bizAttachDto1 = attachService.selectByKey(key);
                String fileUri = bizAttachDto1.getFileUri();
                attachService.removeFile1(fileUri,updProToFileUri);

            }




            sb.append("删除附件:");
            for (Map.Entry<String, String> entry : removeMap.entrySet()) {
                BizAttachDto bizAttachDto = attachService.selectByKey(entry.getKey());
                sb.append(bizAttachDto.getFileName()).append(";");
            }
            //2 删除附件
            attachService.deleteByMap(removeMap);
            //3 删除中间表
            projProjectAttachService.deleteByMap(removeMap);

            //5 新增附件 返回新增的附件
            String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.PROPROJECT.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
            List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri);
            sb.append("\n").append("新增附件:");
            for (BizAttachDto newAttachDto : newAttachDtos) {
                sb.append(newAttachDto.getFileName()).append(";");
            }
            //6 为新增的附件保存 中间表
            List<BizProjProjectAttachDto> newprojProjectAttachDtos = projProjectAttachService.getProProjectAttachDtos(projId, newAttachDtos);
            projProjectAttachService.saveList(newprojProjectAttachDtos);
        }
    }

    public List<ProjProjectVo> selectProject(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjProjectDtoMapper.selectProject(params);
        List<ProjProjectVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            vos.add(convertDtoToVo(dto));
        }
        return vos;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteProAttach(String projId) {
        Map<String, Object> params = new HashMap<>();
        params.put("projId", projId);
        //1 删除历史附件
        List<BizProjProjectAttachDto> projProjectAttachDtos = projProjectAttachService.selectProjProjectAttachDto(params);
        for (BizProjProjectAttachDto projProjectAttachDto : projProjectAttachDtos) {
            String attachId = projProjectAttachDto.getAttachId();
            String id = projProjectAttachDto.getId();
            attachService.deleteById(attachId,"");
            projProjectAttachService.delete(id);
        }
        delete(projId);

        return true;
    }

    public void submitProject(String id, String userId, String type) throws Exception {
        BizProjProjectDto projProjectDto = new BizProjProjectDto();
        projProjectDto.setProjId(id);
        projProjectDto.setProjStatus(ProProjectStatusEnum.AUDITING.getKey());
        updateNotNull(projProjectDto);

        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.PROPROJECTSERVICE);
        if (ProjectWebTypeEnum.AUDITING.getKey().equals(type)) {
            auditService.initActiviti(userId, projProjectDto.getProjId());
        } else {
            getBackPendingMessage(id, ProjectConstant.AuditService.PROPROJECTSERVICE, auditService);
            bizCheckStepService.updateBackObj(id);

        }
    }

    public void getBackPendingMessage(String objId, String objType, ProjectAuditService auditService) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("objId", objId);
        params.put("stepstate", CheckManResultEnum.BACK.getKey());
        //查询当前那个驳回的人 并且向他发送消息
        List<CheckStepPo> checkStepPoList = bizCheckStepService.selectDetailsPdf(params);
        if (StringUtils.isNotEmpty(checkStepPoList)) {
            SysUserDto sysUserDto = userService.selectByKey(checkStepPoList.get(0).getUserId());
            auditService.getPendingMessage(objType, objId, sysUserDto.getWxopenid()
                    , DateUtils.getNowDate(), ProjectConstant.ContWXContent.projMap.get(objType));
        }
    }

    public List<ProjProjectVo> selectAuditProject(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjProjectDtoMapper.selectAuditProject(params);
        dtos = selectUserNameList(dtos);
        List<ProjProjectVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            vos.add(convertDtoToVo(dto));
        }
        return vos;
    }

    public List<ProjProjectVo> selectAuditedProject(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjProjectDtoMapper.selectAuditedProject(params);
        dtos = selectUserNameList(dtos);
        List<ProjProjectVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            vos.add(convertDtoToVo(dto));
        }
        return vos;
    }


    public List<BizProjProjectDto> selectUserNameList(List<BizProjProjectDto> projProjectDtos) {
        Map<String, BizProjProjectDto> projProjectDtoHashMap = new HashMap<>();
        if (StringUtils.isEmpty(projProjectDtos)) {
            return projProjectDtos;
        }
        List<String> list = new ArrayList<>();
        //拿取参数和map
        for (BizProjProjectDto projProjectDto : projProjectDtos) {
            list.add(projProjectDto.getProjId());
            projProjectDtoHashMap.put(projProjectDto.getProjId(), projProjectDto);
        }

        //查询当前审核人员结果
        Map<String, Object> param = new HashMap<>();
        param.put("objIdList", list);
        List<BizProjProjectDto> dtos = bizProjProjectDtoMapper.selectUserNameList(param);

        //遍历设置 审核人员
        for (BizProjProjectDto dto : dtos) {
            BizProjProjectDto projProjectDto = projProjectDtoHashMap.get(dto.getProjId());
            List<String> curAuditUsers = projProjectDto.getCurAuditUser();
            if (StringUtils.isEmpty(curAuditUsers)) {
                curAuditUsers = new ArrayList<>();
                //这里的username就是当前审核人员的name 偷懒没有重新新建实体了
                curAuditUsers.add(dto.getUserName());
                projProjectDto.setCurAuditUser(curAuditUsers);
            } else {
                curAuditUsers.add(dto.getUserName());
            }
        }
        return projProjectDtos;
    }

    public List<ProActivitiItemVo> selectAuditItem(Map<String, Object> params) {
        List<ProActivitiItemPo> pos = bizProjProjectDtoMapper.selectAuditItem(params);
        pos = selectUserNameListPo(pos);
        List<ProActivitiItemVo> vos = new ArrayList<>();
        for (ProActivitiItemPo po : pos) {
            vos.add(ProjProjectAssembler.convertItemPoToItemVo(po));
        }
        return vos;
    }

    public List<ProActivitiItemVo> selectAuditedItem(Map<String, Object> params) {
        List<ProActivitiItemPo> pos = bizProjProjectDtoMapper.selectAuditedItem(params);
        pos = selectUserNameListPo(pos);
        List<ProActivitiItemVo> vos = new ArrayList<>();
        for (ProActivitiItemPo po : pos) {
            vos.add(ProjProjectAssembler.convertItemPoToItemVo(po));
        }
        return vos;
    }

    public List<ProActivitiItemPo> selectUserNameListPo(List<ProActivitiItemPo> proActivitiItemPos) {
        Map<String, ProActivitiItemPo> proActivitiItemPoHashMap = new HashMap<>();
        if (StringUtils.isEmpty(proActivitiItemPos)) {
            return proActivitiItemPos;
        }
        List<String> list = new ArrayList<>();
        //拿取参数和map
        for (ProActivitiItemPo proActivitiItemPo : proActivitiItemPos) {
            list.add(proActivitiItemPo.getObjId());
            proActivitiItemPoHashMap.put(proActivitiItemPo.getObjId(), proActivitiItemPo);
        }

        //查询当前审核人员结果
        Map<String, Object> param = new HashMap<>();
        param.put("objIdList", list);
        List<ProActivitiItemPo> pos = bizProjProjectDtoMapper.selectUserNameListPo(param);

        //遍历设置 审核人员
        for (ProActivitiItemPo po : pos) {
            ProActivitiItemPo proActivitiItemPo = proActivitiItemPoHashMap.get(po.getObjId());
            List<String> curAuditUsers = proActivitiItemPo.getCurAuditUser();
            if (StringUtils.isEmpty(curAuditUsers)) {
                curAuditUsers = new ArrayList<>();
                //这里的username就是当前审核人员的name 偷懒没有重新新建实体了
                curAuditUsers.add(po.getUserName());
                proActivitiItemPo.setCurAuditUser(curAuditUsers);
            } else {
                curAuditUsers.add(po.getUserName());
            }
        }
        return proActivitiItemPos;
    }

    public List<ProActivitiItemVo> selectActivitiItem(Map<String, Object> params) {
        List<ProActivitiItemPo> pos = bizProjProjectDtoMapper.selectActivitiItem(params);
        pos = selectUserNameListPo(pos);
        List<ProActivitiItemVo> vos = new ArrayList<>();
        for (ProActivitiItemPo po : pos) {
            vos.add(ProjProjectAssembler.convertItemPoToItemVo(po));
        }
        return vos;
    }

    public List<ProjProjectVo> selectProjectDetails(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjProjectDtoMapper.selectProject(params);
        List<ProjProjectVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            ProjProjectVo vo = ProjProjectAssembler.convertDtoToVo(dto);
            vo.setAttachVos(projProjectAttachService.getProjectAttachVoById(dto.getProjId()));
            vos.add(vo);
        }

        for (ProjProjectVo vo : vos) {
            List<ProjContListVo> contListVos = projContListService.selectContList(params);
            vo.setProjContListVos(contListVos);
        }


        return vos;
    }

    public List<ProjProjectVo> selectSelContPro(Map<String, Object> param) {
        List<BizProjProjectDto> dtos = bizProjProjectDtoMapper.selectSelContPro(param);
        List<ProjProjectVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            ProjProjectVo vo = ProjProjectAssembler.convertDtoToVo(dto);
            vos.add(vo);
        }
        return vos;
    }

    public List<ProjProjectVo> selectProjPurcPlan(Map<String, Object> param) {
        List<BizProjProjectDto> dtos = bizProjProjectDtoMapper.selectProjPurcPlan(param);
        List<ProjProjectVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            ProjProjectVo vo = ProjProjectAssembler.convertDtoToVo(dto);
            vos.add(vo);
        }
        return vos;
    }

    public List<ProjProjectVo> selectProjPurcResult(Map<String, Object> param) {
        List<BizProjProjectDto> dtos = bizProjProjectDtoMapper.selectProjPurcResult(param);
        List<ProjProjectVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            ProjProjectVo vo = ProjProjectAssembler.convertDtoToVo(dto);
            vos.add(vo);
        }
        return vos;
    }

    public List<ProActivitiItemPo> selectUserNameListWx(List<String> list) {
        Map<String, Object> param = new HashMap<>();
        param.put("objIdList", list);
        return bizProjProjectDtoMapper.selectUserNameListPo(param);

    }

    public void buildOpenTenderPDF(HttpServletResponse response, String tempurl,
                                   String pdfPicUrl, String baseFontUrl,
                                   String projId,
                                   String selContId,
                                   String planId,
                                   String resultId) throws Exception {

        /**立项信息**/
        Map<String, Object> resMap = getProjExportInfo(projId);
        ProjProjectVo projProjectVo=(ProjProjectVo)resMap.get("projProjectVo");
        String selContType = projProjectVo.getSelContType();
        String projName = projProjectVo.getProjName();
        /**选商信息  只有竞争性谈判才有选商**/
        if(SelContTypeEnum.BUILDPROJECTNEGOTIATION.getKey().equals(selContType)){
            getSelContExportInfo(resMap,selContId,projName);
        }

        //公开招标无采购
        if(!SelContTypeEnum.OPENTENDER.getKey().equals(selContType)){
            /**采购方案信息**/
            if(StringUtils.isNotEmpty(planId)){
                getPlanExportInfo(resMap,planId);
            }

            /**采购结果信息**/
            if(StringUtils.isNotEmpty(resultId)){
                getResultExportInfo(resMap,resultId);
            }
    }

        if (SelContTypeEnum.OPENTENDER.getKey().equals(selContType)) {
            ProjPurcPDFBuildUtils.buildOpenTenderPDF(response, tempurl, pdfPicUrl, baseFontUrl, resMap);
        } else if (SelContTypeEnum.NOTENDER.getKey().equals(selContType)) {
            ProjPurcPDFBuildUtils.buildNoTenderPDF(response, tempurl, pdfPicUrl, baseFontUrl, resMap);
        } else if (SelContTypeEnum.BUILDPROJECTSINGLE.getKey().equals(selContType)) {
            ProjPurcPDFBuildUtils.buildProjectSinglePDF(response, tempurl, pdfPicUrl, baseFontUrl,resMap);
        } else if (SelContTypeEnum.BUILDPROJECTNEGOTIATION.getKey().equals(selContType)) {
            ProjPurcPDFBuildUtils.buildProjectNegotiationPDF(response, tempurl, pdfPicUrl, baseFontUrl,resMap);
        }else if (SelContTypeEnum.BUILDPROJECTINSIDE.getKey().equals(selContType)){
            ProjPurcPDFBuildUtils.buildProjectinsidePDF(response, tempurl, pdfPicUrl, baseFontUrl,resMap);
        }
    }

    /**
     * 获取导出采购结果
     * @param resMap
     * @param resultId
     */
    private void getResultExportInfo(Map<String, Object> resMap, String resultId) {

        Map<String, Object> params = new HashMap<>();
        params.put("resultId", resultId);
        ProjPurcResultVo projPurcResultVo = projPurcResultService.selectPurcResultDetails(params);
        if(!ProResultStatusEnum.DONE.getKey().equals(projPurcResultVo.getProjStatus())){
            return ;
        }

        List<Map<String, Object>> tableResult = new ArrayList<>();
        //添加 合同签约审查审批表审核意见
        Map<String, Object> params2 = new HashMap<>();
        params2.put("objId", resultId);
        List<CheckStepPo> checkStepPoList = bizCheckStepService.selectDetailsPdf(params2);
        for (int i = 0; i < checkStepPoList.size(); i++) {
            CheckStepPo checkStepPo = checkStepPoList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("deptName", checkStepPo.getDeptName());
            map.put("userName", checkStepPo.getUserName());
            map.put("checkNode", checkStepPo.getCheckNode());
            map.put("checkTime", null != checkStepPo.getCheckTime() ? DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()) : null);
            tableResult.add(map);
        }
        resMap.put(ParamConstant.PROJPURCRESULTVO,projPurcResultVo);
        resMap.put(ParamConstant.TABLERESULT,tableResult);
    }

    /**
     * 获取导出采购方案信息
     * @param resMap
     * @param planId
     */
    private void getPlanExportInfo(Map<String, Object> resMap, String planId) {
        Map<String, Object> params = new HashMap<>();
        params.put("planId", planId);
        ProjPurcPlanVo projPurcPlanVo = projPurcPlanService.selectPurcPlanDetails(params);
        if(!ProPlanStatusEnum.DONE.getKey().equals(projPurcPlanVo.getProjStatus())){
            return ;
        }

        List<Map<String, Object>> tablePlan = new ArrayList<>();
        //添加 合同签约审查审批表审核意见
        Map<String, Object> params2 = new HashMap<>();
        params2.put("objId", planId);
        List<CheckStepPo> checkStepPoList = bizCheckStepService.selectDetailsPdf(params2);
        for (int i = 0; i < checkStepPoList.size(); i++) {
            CheckStepPo checkStepPo = checkStepPoList.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("deptName", checkStepPo.getDeptName());
            map.put("userName", checkStepPo.getUserName());
            map.put("checkNode", checkStepPo.getCheckNode());
            map.put("checkTime", null != checkStepPo.getCheckTime() ? DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()) : null);
            tablePlan.add(map);
        }

        resMap.put(ParamConstant.PROJPURCPLANVO,projPurcPlanVo);
        resMap.put(ParamConstant.TABLEPLAN,tablePlan);
    }

    /**
     * 获取导出的选商信息
     * @param resMap
     * @param projName
     * @param selContId
     */
    private void getSelContExportInfo(Map<String, Object> resMap, String selContId, String projName) {
        Map<String, Object> paramMapSelCont = new HashMap<>();
        paramMapSelCont.put("selContId", selContId);
        ProjSelContVo projSelContVo = projSelContService.selectSelDetails(paramMapSelCont);

        projSelContVo.setProjName(projName);

        List<Map<String, Object>> tableSelCont = new ArrayList<Map<String, Object>>();

        //添加 合同签约审查审批表审核意见
        Map<String, Object> params2 = new HashMap<>();
        params2.put("objId", selContId);
        List<CheckStepPo> checkStepPoListSelCont = bizCheckStepService.selectDetailsPdf(params2);
        for (int i = 0; i < checkStepPoListSelCont.size(); i++) {
            CheckStepPo checkStepPo = checkStepPoListSelCont.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("deptName", checkStepPo.getDeptName());
            map.put("userName", checkStepPo.getUserName());
            map.put("checkNode", checkStepPo.getCheckNode());
            map.put("checkTime", null != checkStepPo.getCheckTime() ? DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()) : null);
            tableSelCont.add(map);
        }

        resMap.put(ParamConstant.PROJSELCONTVO,projSelContVo);
        resMap.put(ParamConstant.TABLESELCONT,tableSelCont);
    }

    /**
     * 获取导出的立项信息
     * @param projId
     * @return
     */
    private Map<String, Object> getProjExportInfo(String projId) {

        Map<String, Object> resMap = new HashMap<>();

        Map<String, Object> paramMapProj = new HashMap<>();
        paramMapProj.put("projId", projId);
        paramMapProj.put("selContId", projId);
        List<ProjProjectVo> projProjectVos = selectProjectDetails(paramMapProj);
        ProjProjectVo projProjectVo = projProjectVos.get(0);

        List<Map<String, Object>> tableProj = new ArrayList<Map<String, Object>>();

        //添加 合同签约审查审批表审核意见
        Map<String, Object> stepMapProj = new HashMap<>();
        stepMapProj.put("objId", projProjectVo.getProjId());
        List<CheckStepPo> checkStepPoListProj = bizCheckStepService.selectDetailsPdf(stepMapProj);
        for (int i = 0; i < checkStepPoListProj.size(); i++) {
            CheckStepPo checkStepPo = checkStepPoListProj.get(i);
            Map<String, Object> map = new HashMap<>();
            map.put("deptName", checkStepPo.getDeptName());
            map.put("userName", checkStepPo.getUserName());
            map.put("checkNode", checkStepPo.getCheckNode());
            map.put("checkTime", null != checkStepPo.getCheckTime() ? DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(checkStepPo.getCheckTime()) : null);
            tableProj.add(map);
        }
        resMap.put(ParamConstant.PROJPROJECTVO,projProjectVo);
        resMap.put(ParamConstant.TABLEPROJ,tableProj);
        return resMap;

    }

    /**
     * 查询项目立项完成的项目信息
     * @param resultId
     * @return
     */
    public ProjProjectPo selectProjComplete(String resultId){
        return bizProjProjectDtoMapper.selectProjComplete(resultId);
    }
}

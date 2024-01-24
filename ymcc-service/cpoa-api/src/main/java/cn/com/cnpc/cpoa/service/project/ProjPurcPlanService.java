package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.assembler.project.ProjPlanContAssembler;
import cn.com.cnpc.cpoa.assembler.project.ProjPurcPlanAssembler;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditDto;
import cn.com.cnpc.cpoa.domain.project.*;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.DealWebTypeEnum;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.enums.project.ProPlanStatusEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.enums.project.ProjSelContStatusEnum;
import cn.com.cnpc.cpoa.enums.project.ProjectWebTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizCheckManDtoMapper;
import cn.com.cnpc.cpoa.mapper.BizCheckStepDtoMapper;
import cn.com.cnpc.cpoa.mapper.ProjPurcHideDtoMapper;
import cn.com.cnpc.cpoa.mapper.contractor.BizContAccessDtoMapper;
import cn.com.cnpc.cpoa.mapper.contractor.BizContContractorDtoMapper;
import cn.com.cnpc.cpoa.mapper.contractor.BizContCreditMapper;
import cn.com.cnpc.cpoa.mapper.project.BizProjProjectDtoMapper;
import cn.com.cnpc.cpoa.mapper.project.BizProjPurcPlanDtoMapper;
import cn.com.cnpc.cpoa.mapper.project.ProjBackLogDtoMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.ActivitiService;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.project.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:20
 * @Description:
 */
@Service
public class ProjPurcPlanService extends AppService<BizProjPurcPlanDto> {


    @Autowired
    BizProjPurcPlanDtoMapper bizProjPurcPlanDtoMapper;

    @Autowired
    UserService userService;

    @Autowired
    ProjPlanListService projPlanListService;

    @Autowired
    ProjectAuditService projectAuditService;

    @Autowired
    BizCheckStepService bizCheckStepService;

    @Autowired
    ProjProjectService projProjectService;

    @Autowired
    AttachService attachService;

    @Autowired
    ProjPurcPlanAttachService projPurcPlanAttachService;

    @Autowired
    ProjContListService projContListService;

    @Autowired
    ProjPlanContService projPlanContService;

    @Autowired
    BizProjProjectDtoMapper bizProjProjectDtoMapper;

    @Autowired
    private ActivitiService activitiService;

    @Autowired
    private BizContCreditMapper creditMapper;

    @Autowired
    private BizContAccessDtoMapper accessDtoMapper;

    @Autowired
    private ProjPurcHideDtoMapper projPurcHideDtoMapper;

    @Autowired
    private ProjSelContService selContService;

    @Autowired
    private BizCheckStepDtoMapper checkStepDtoMapper;

    @Autowired
    private BizCheckManDtoMapper checkManDtoMapper;

    @Autowired
    private ProjBackLogDtoMapper projBackLogDtoMapper;

    @Autowired
    private ProjChangeLogService changeLogService;

    @Autowired
    private ProjPurcResultService projPurcResultService;

    @Autowired
    private BizContContractorDtoMapper contContractorDtoMapper;


    public List<ProjProjectPlanVo> selectProjPurcPlan(Map<String, Object> params) {

        List<BizProjProjectDto> dtos = bizProjPurcPlanDtoMapper.selectProjPurcPlan(params);
        // dtos = projProjectService.selectUserNameList(dtos);
        List<ProjProjectPlanVo> vos = new ArrayList<>();
        for (BizProjProjectDto projProjectDto : dtos) {
            vos.add(ProjPurcPlanAssembler.convertDtoToPlanVo(projProjectDto));
        }

        return vos;
    }


    public List<ProjProjectPlanVo> selectAuditProjPurcPlan(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjPurcPlanDtoMapper.selectAuditProjPurcPlan(params);


        dtos = selectUserNameList(dtos);
        List<ProjProjectPlanVo> vos = new ArrayList<>();
        for (BizProjProjectDto projProjectDto : dtos) {
            vos.add(ProjPurcPlanAssembler.convertDtoToPlanVo(projProjectDto));
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
            list.add(projProjectDto.getPlanId());
            projProjectDtoHashMap.put(projProjectDto.getPlanId(), projProjectDto);
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


    public List<ProjProjectPlanVo> selectAuditedProjPurcPlan(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjPurcPlanDtoMapper.selectAuditedProjPurcPlan(params);
        dtos = selectUserNameList(dtos);
        List<ProjProjectPlanVo> vos = new ArrayList<>();
        for (BizProjProjectDto projProjectDto : dtos) {
            vos.add(ProjPurcPlanAssembler.convertDtoToPlanVo(projProjectDto));
        }

        return vos;
    }


    @Transactional(rollbackFor = Exception.class)
    public BizProjPurcPlanDto addProjPurcPlan(ProjPurcPlanVo vo, String userId, String type) throws Exception {
        SysUserDto userDto = userService.selectByKey(userId);
        BizProjPurcPlanDto dto = ProjPurcPlanAssembler.convertVoToDto(vo);
        dto.setProjStatus(ProjectWebTypeEnum.getEnumByKey(type));
        dto.setCreateId(userId);
        dto.setOwnerDeptId(userDto.getDeptId());
        dto.setPlanId(StringUtils.getUuid32());
        dto.setCreateAt(DateUtils.getNowDate());
        dto.setProjPhase(ProjPhaseEnum.PURCHASE.getKey());
        dto.setPlanNo(getDealNo(ProjectConstant.ProjPhaseType.CGFA));


        //新增明细记录
        List<ProjPlanListVo> projPlanListVos = vo.getProjPlanListVos();

        List<BizProjPlanListDto> projPlanListDtos = new ArrayList();
        List<BizProjPlanContDto> projPlanContDtos = new ArrayList();
        int count = 1;
        for (ProjPlanListVo projPlanListVo : projPlanListVos) {
            BizProjPlanListDto projPlanListDto = ProjPurcPlanAssembler.convertVoToListDto(projPlanListVo, dto.getPlanId());
            projPlanListDto.setPlanListNo(count);
            count++;
            projPlanListDtos.add(projPlanListDto);
            List<ProjPlanContVo> planContVos = projPlanListVo.getProjPlanContVos();
            // 添加选商列表信息
            for (ProjPlanContVo projPlanContVo : planContVos) {
                projPlanContDtos.add(ProjPlanContAssembler.convertDtoToVo(projPlanContVo, projPlanListDto.getPlanListId()));
            }

        }


        save(dto);
        projPlanListService.saveList(projPlanListDtos);
        projPlanContService.saveList(projPlanContDtos);

        //新增附件
        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getPlanId(), FileOwnerTypeEnum.PROJECT.getKey(), vo.getAttachVos());

        //获取中间表
        List<BizProjPurcPlanAttachDto> projPurcPlanAttachDtos = projPurcPlanAttachService.getProjPurcPlanAttachDtos(dto.getPlanId(), attachDtos);

        BizProjProjectDto projProjectDto = projProjectService.selectByKey(dto.getProjId());
        //保存附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.PURCHASE.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        attachService.updateAttachs(attachDtos, userId, proToFileUri);
        //保存中間表
        projPurcPlanAttachService.saveList(projPurcPlanAttachDtos);


        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.PURCHASESERVICE);
        //7 如果状态为 保存提交 则生成审核相关
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            activitiService.buildDiyProjPurcActiviti(vo.getAuditVo(), userId);
        }
        //8 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if (DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)) {
            projProjectService.getBackPendingMessage(dto.getPlanId(), ProjectConstant.AuditService.PURCHASESERVICE, auditService);

            bizCheckStepService.updateBackObj(dto.getPlanId());
        }

        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProjPurcPlan(ProjPurcPlanVo vo, String userId, String type) throws Exception {
        SysUserDto userDto = userService.selectByKey(userId);

        BizProjPurcPlanDto dto = ProjPurcPlanAssembler.convertVoToDto(vo);
        dto.setProjStatus(ProjectWebTypeEnum.getEnumByKey(type));
        dto.setCreateId(userId);
        dto.setOwnerDeptId(userDto.getDeptId());
        dto.setPlanNo(null);

        //删除现有的明细记录
        Map<String, Object> params = new HashMap<>();
        params.put("planId", dto.getPlanId());
        List<BizProjPlanListDto> planListDtos = projPlanListService.selectPlanList(params);
        List<Object> planListKeys = new ArrayList<>();
        List<Object> planListContKeys = new ArrayList<>();

        for (BizProjPlanListDto projPlanListDto : planListDtos) {
            planListKeys.add(projPlanListDto.getPlanListId());

            Map<String, Object> param = new HashMap<>();
            param.put("planListId", projPlanListDto.getPlanListId());
            List<ProjPlanContVo> projPlanContVos = projPlanContService.selectPlanCont(param);
            for (ProjPlanContVo projPlanContVo : projPlanContVos) {
                planListContKeys.add(projPlanContVo.getId());
            }
        }


        List<ProjPlanListVo> projPlanListVos = vo.getProjPlanListVos();

        List<BizProjPlanListDto> projPlanListDtos = new ArrayList();
        List<BizProjPlanContDto> projPlanContDtos = new ArrayList();
        int count = 1;
        for (ProjPlanListVo projPlanListVo : projPlanListVos) {
            BizProjPlanListDto projPlanListDto = ProjPurcPlanAssembler.convertVoToListDto(projPlanListVo, dto.getPlanId());
            projPlanListDto.setPlanListNo(count);
            count++;
            projPlanListDtos.add(projPlanListDto);
            List<ProjPlanContVo> planContVos = projPlanListVo.getProjPlanContVos();
            // 添加选商列表信息
            for (ProjPlanContVo projPlanContVo : planContVos) {
                projPlanContDtos.add(ProjPlanContAssembler.convertDtoToVo(projPlanContVo, projPlanListDto.getPlanListId()));

            }
        }

        projPlanListService.deleteList(planListKeys);
        projPlanContService.deleteList(planListContKeys);
        updateNotNull(dto);
        projPlanListService.saveList(projPlanListDtos);
        projPlanContService.saveList(projPlanContDtos);

        BizProjProjectDto projProjectDto = projProjectService.selectByKey(dto.getProjId());

        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getPlanId(), FileOwnerTypeEnum.PROJECT.getKey(), vo.getAttachVos());

        //获得已存在的中间表信息
        Map<String, Object> param = new HashMap<>();
        param.put("planId", dto.getPlanId());
        List<BizProjPurcPlanAttachDto> projPurcPlanAttachDtos = projPurcPlanAttachService.selectProjPurcPlanAttachDto(param);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getPlanRemoveMap(attachDtos, projPurcPlanAttachDtos);
        //2 删除附件
        attachService.deleteByMap(removeMap);
        //3 删除中间表
        projPurcPlanAttachService.deleteByMap(removeMap);
        //4 执行更新
        updateNotNull(dto);
        //5 新增附件 返回新增的附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.PURCHASE.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri);
        //6 为新增的附件保存 中间表
        List<BizProjPurcPlanAttachDto> newProjPurcPlanAttachDtos = projPurcPlanAttachService.getProjPurcPlanAttachDtos(dto.getPlanId(), newAttachDtos);
        projPurcPlanAttachService.saveList(newProjPurcPlanAttachDtos);


        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.PURCHASESERVICE);
        //7 如果状态为 保存提交 则生成审核相关
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            activitiService.buildDiyProjPurcActiviti(vo.getAuditVo(), userId);
        }
        //8 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if (DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)) {
            projProjectService.getBackPendingMessage(dto.getPlanId(), ProjectConstant.AuditService.PURCHASESERVICE, auditService);

            bizCheckStepService.updateBackObj(dto.getPlanId());
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void maintainProjPlan(String planId,String userId,String planNotes,List<ProjPlanListVo> projPlanListVos,List<AttachVo> attachVos,String recomContReason) throws Exception {
        BizProjPurcPlanDto dto = selectByKey(planId);
        StringBuffer sb = new StringBuffer();
        //日志记录
        ProjChangeLogDto changeLogDto = new ProjChangeLogDto();
        changeLogDto.setChgLogId(StringUtils.getUuid32());
        changeLogDto.setCreateId(userId);
        changeLogDto.setCreateAt(DateUtils.getNowDate());
        changeLogDto.setChgObjType("purchase");
        changeLogDto.setChgObjId(planId);
        if (Optional.ofNullable(planNotes).isPresent()){
            sb.append("采购方案说明修改:").append("\n");
            sb.append(dto.getPlanNotes()).append("->").append(planNotes).append("\n");
            dto.setPlanNotes(planNotes);
        }

        if (Optional.ofNullable(recomContReason).isPresent()){
            sb.append("邀请服务商理由修改:").append("\n");
            //查询现有明细记录
            Map<String, Object> params = new HashMap<>();
            params.put("planId", dto.getPlanId());
            List<BizProjPlanListDto> planListDtos = projPlanListService.selectPlanList(params);
            for (int i = 0; i < planListDtos.size(); i++) {
                BizProjPlanListDto planListDto = planListDtos.get(i);
                if (i == 0){
                    sb.append(planListDto.getRecomContReason()).append("->").append(recomContReason).append("\n");
                }
                planListDto.setRecomContReason(recomContReason);
                projPlanListService.updateNotNull(planListDto);
            }
        }

        if (Optional.ofNullable(projPlanListVos).isPresent()){
            //服务审批表修改
            updatePlanList(projPlanListVos,planId);
        }
        if (Optional.ofNullable(attachVos).isPresent()){
            //附件修改
            updateAttach(sb,userId,dto,attachVos);
        }
        // 执行更新
        updateNotNull(dto);
        changeLogDto.setChgObjCtx(sb.toString());
        changeLogService.save(changeLogDto);

    }

    /**
     * 服务审批表修改
     * @param projPlanListVos
     */
    private void updatePlanList(List<ProjPlanListVo> projPlanListVos,String planId) {
        //删除现有的明细记录
        Map<String, Object> params = new HashMap<>();
        params.put("planId", planId);
        List<BizProjPlanListDto> planListDtos = projPlanListService.selectPlanList(params);
        List<Object> planListKeys = new ArrayList<>();
        List<Object> planListContKeys = new ArrayList<>();

        for (BizProjPlanListDto projPlanListDto : planListDtos) {
            planListKeys.add(projPlanListDto.getPlanListId());

            Map<String, Object> param = new HashMap<>();
            param.put("planListId", projPlanListDto.getPlanListId());
            List<ProjPlanContVo> projPlanContVos = projPlanContService.selectPlanCont(param);
            for (ProjPlanContVo projPlanContVo : projPlanContVos) {
                planListContKeys.add(projPlanContVo.getId());
            }
        }
        List<BizProjPlanListDto> projPlanListDtos = new ArrayList();
        List<BizProjPlanContDto> projPlanContDtos = new ArrayList();
        int count = 1;
        for (ProjPlanListVo projPlanListVo : projPlanListVos) {
            BizProjPlanListDto projPlanListDto = ProjPurcPlanAssembler.convertVoToListDto(projPlanListVo, planId);
            projPlanListDto.setPlanListNo(count);
            count++;
            projPlanListDtos.add(projPlanListDto);
            List<ProjPlanContVo> planContVos = projPlanListVo.getProjPlanContVos();
            // 添加选商列表信息
            for (ProjPlanContVo projPlanContVo : planContVos) {
                projPlanContDtos.add(ProjPlanContAssembler.convertDtoToVo(projPlanContVo, projPlanListDto.getPlanListId()));

            }
        }
        projPlanListService.deleteList(planListKeys);
        projPlanContService.deleteList(planListContKeys);
        projPlanListService.saveList(projPlanListDtos);
        projPlanContService.saveList(projPlanContDtos);
    }

    /**
     * 附件修改
     * @param sb
     * @param userId
     * @param dto
     * @param attachVos
     */
    private void updateAttach(StringBuffer sb,String userId,BizProjPurcPlanDto dto,List<AttachVo> attachVos) throws Exception {
        sb.append("附件修改:").append("\n");
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(dto.getProjId());

        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getPlanId(), FileOwnerTypeEnum.PROJECT.getKey(), attachVos);

        //获得已存在的中间表信息
        Map<String, Object> param = new HashMap<>();
        param.put("planId", dto.getPlanId());
        List<BizProjPurcPlanAttachDto> projPurcPlanAttachDtos = projPurcPlanAttachService.selectProjPurcPlanAttachDto(param);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getPlanRemoveMap(attachDtos, projPurcPlanAttachDtos);
        //修改后不要的附件存放地址
        String updProToFileUri = attachService.getupdProToFileUri(ProjPhaseEnum.PURCHASE.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        //保存不要的附件
        Set<String> keys = removeMap.keySet();
        for (String key : keys) {
            BizAttachDto bizAttachDto = attachService.selectByKey(key);
            String fileUri = bizAttachDto.getFileUri();
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
        projPurcPlanAttachService.deleteByMap(removeMap);

        //5 新增附件 返回新增的附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.PURCHASE.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri);
        sb.append("\n").append("新增附件:");
        for (BizAttachDto newAttachDto : newAttachDtos) {
            sb.append(newAttachDto.getFileName()).append(";");
        }
        //6 为新增的附件保存 中间表
        List<BizProjPurcPlanAttachDto> newProjPurcPlanAttachDtos = projPurcPlanAttachService.getProjPurcPlanAttachDtos(dto.getPlanId(), newAttachDtos);
        projPurcPlanAttachService.saveList(newProjPurcPlanAttachDtos);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteChain(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("planId", id);
        List<BizProjPlanListDto> planListDtos = projPlanListService.selectPlanList(params);
        List<Object> planListKeys = new ArrayList<>();
        for (BizProjPlanListDto projPlanListDto : planListDtos) {
            planListKeys.add(projPlanListDto.getPlanListId());
        }

        List<BizProjPurcPlanAttachDto> projPurcPlanAttachDtos = projPurcPlanAttachService.selectProjPurcPlanAttachDto(params);

        for (BizProjPurcPlanAttachDto projPurcPlanAttachDto : projPurcPlanAttachDtos) {
            String attachId = projPurcPlanAttachDto.getAttachId();
            attachService.deleteById(attachId,"");
            projPurcPlanAttachService.delete(projPurcPlanAttachDto.getId());
        }

        delete(id);
        projPlanListService.deleteList(planListKeys);
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitProjPurcPlan(String id, String userId, String type, AuditVo auditVo) throws Exception {

        BizProjPurcPlanDto dto = new BizProjPurcPlanDto();
        dto.setPlanId(id);
        dto.setProjStatus(ProPlanStatusEnum.AUDITING.getKey());
        updateNotNull(dto);

        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.PURCHASESERVICE);
        if (ProjectWebTypeEnum.AUDITING.getKey().equals(type)) {
            activitiService.buildDiyProjPurcActiviti(auditVo, userId);

        } else {
            projProjectService.getBackPendingMessage(id, ProjectConstant.AuditService.PURCHASESERVICE, auditService);
            bizCheckStepService.updateBackObj(id);

        }
    }


    public ProjPurcPlanVo selectPurcPlanDetails(Map<String, Object> params) {
        //1 查询基础信息
        List<BizProjProjectDto> projProjectDtos = bizProjPurcPlanDtoMapper.selectProjPurcPlan(params);

        List<ProjPurcPlanVo> projPurcPlanVos = new ArrayList<>();
        for (BizProjProjectDto projProjectDto : projProjectDtos) {
            projPurcPlanVos.add(ProjPurcPlanAssembler.convertDtoToProjPurcPlanVo(projProjectDto));
        }
        ProjPurcPlanVo projPurcPlanVo = projPurcPlanVos.get(0);

        //2 查询选商列表
        List<ProjPlanListVo> projPlanListVos = projPlanListService.selectPlanListVo(params);

        projPurcPlanVo.setProjPlanListVos(projPlanListVos);

        //3 查询附件信息
        Map<String, Object> param = new HashMap<>();
        param.put("objId", params.get("planId"));
        List<AttachVo> attachVos = new ArrayList<>();
        List<BizAttachDto> attachDtos = attachService.selectListByObjId(param);
        for (BizAttachDto attachDto : attachDtos) {
            AttachVo attachVo = new AttachVo();
            BeanUtils.copyBeanProp(attachVo, attachDto);
            attachVos.add(attachVo);
        }

        projPurcPlanVo.setAttachVos(attachVos);


        return projPurcPlanVo;

    }

    public List<IviteContVo> selectIviteConts(Map<String, Object> params) {
        List<ProjContListVo> contList = projContListService.selectContList(params);
        List<IviteContVo> iviteContVos = new ArrayList<>();
        for (ProjContListVo projContListVo : contList) {
            IviteContVo iviteContVo = new IviteContVo();
            BeanUtils.copyBeanProp(iviteContVo, projContListVo);
            iviteContVos.add(iviteContVo);
        }

        return iviteContVos;
    }

    public String getDealNo(String projPhaseType) {

        int year = DateUtils.getYear();
        Map<String, Object> param = new HashMap<>();
        param.put("projPhaseType", projPhaseType);
        param.put("year", year);
        //获取到当前合同 年份 最大的 最后四位数
        String dealNo4 = selectCurrentDealNo(param);
        if (StringUtils.isEmpty(dealNo4)) {
            dealNo4 = "0000";
        }
        return year + "-" + projPhaseType + "-" + String.format("%04d", (Integer.valueOf(dealNo4) + 1));
    }

    private String selectCurrentDealNo(Map<String, Object> param) {

        return bizProjPurcPlanDtoMapper.selectCurrentDealNo(param);
    }

    /**
     * 导出采购计划审批表
     *
     * @param response
     * @param tempurl
     * @param pdfPicUrl
     * @param baseFontUrl
     * @param planId      计划id
     */
    public void buildPlanPDF(HttpServletResponse response, String tempurl,
                             String pdfPicUrl, String baseFontUrl, String planId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("planId", planId);
        ProjPurcPlanVo projPurcPlanVo = selectPurcPlanDetails(params);

        List<Map<String, Object>> table2 = new ArrayList<>();
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
            table2.add(map);
        }
        //ProjPurcPDFBuildUtils.buildPlanPDF(response, tempurl, pdfPicUrl, baseFontUrl, projPurcPlanVo, table2);
    }

    /**
     * 根据planId查询承包商资质
     * @param planId
     * @return
     */
    public List<String> selectContractorAptitude(String planId){
        //过期承包商名称
        List<String> contNameList = new ArrayList<>();
        HashMap<String, Object> params = new HashMap<>(4);
        params.put("planId",planId);
        //2 查询选商列表
        List<ProjPlanListVo> projPlanListVos = projPlanListService.selectPlanListVo(params);
        for (ProjPlanListVo projPlanListVo : projPlanListVos) {
            List<ProjPlanContVo> planContVos = projPlanListVo.getProjPlanContVos();
            planContVos.forEach(planContVo -> {
                List<BizContCreditDto> list = creditMapper.selectAptitudeByContName(planContVo.getContId());
                if (list.size() > 0){
                    contNameList.add(planContVo.getContName());
                }
            });
        }
        return contNameList;
    }

    /**
     * 根据承包商名称查询资质
     * @param projPlanListVos
     * @return
     */
    public List<String> selectAptitudeByContName(List<ProjPlanListVo> projPlanListVos){
        List<String> contNameList = new ArrayList<>();
        for (ProjPlanListVo projPlanListVo : projPlanListVos) {
            List<ProjPlanContVo> planContVos = projPlanListVo.getProjPlanContVos();
            planContVos.forEach(planContVo -> {
                List<BizContCreditDto> list = creditMapper.selectAptitudeByContName(planContVo.getContId());
                if (list.size() > 0){
                    contNameList.add(planContVo.getContName());
                }
            });

        }
        return contNameList;
    }

    /**
     * 采购方案冻结
     * @param planId
     * @return
     */
    public int freeze(String planId) {
        ProjPurcHideDto purcHideDto = ProjPurcHideDto.builder()
                .purcHideId(StringUtils.getUuid32())
                .purcType(CheckTypeEnum.PURCHASE.getKey())
                .purcId(planId)
                .build();
        return projPurcHideDtoMapper.insert(purcHideDto);

    }

    /**
     * 采购方案解冻
     * @param planId
     * @return
     */
    public int unfreeze(String planId) {
        ProjPurcHideDto purcHideDto = ProjPurcHideDto.builder()
                .purcType(CheckTypeEnum.PURCHASE.getKey())
                .purcId(planId)
                .build();
        return projPurcHideDtoMapper.delete(purcHideDto);

    }

    /**
     * 采购方案回退
     * @param planId
     * @param projId
     * @param selContId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage planBack(String planId, String projId, String selContId,String resultId) {
        //1.记录选商内容到日志记录表
        String selContLog = selContOperation(selContId);

        //2.记录采购方案到日志记录表
        String planLog = planOperation(planId, projId);

        if (resultId != null) {
            String resultLog = resultOperation(resultId, projId);
        }

        //3.记录到日志记录表
        ProjBackLogDto projBackLogDto = ProjBackLogDto.builder()
                .backLogId(StringUtils.getUuid32())
                .projId(projId)
                .createAt(DateUtils.getNowDate())
                .planLog(planLog)
                .selContLog(selContLog)
                .createId(ServletUtils.getSessionUserId())
                .build();
        projBackLogDtoMapper.insert(projBackLogDto);
        return AppMessage.result("采购方案回退成功");
    }

    /**
     * 选商操作
     * @param selContId
     * @return
     */
    private String selContOperation(String selContId){
        //1.记录选商内容到日志记录表
        Map<String, Object> params = new HashMap<>();
        params.put("selContId", selContId);
        ProjSelContVo projSelContVo = selContService.selectSelDetails(params);
        //审核意见，以及审批步骤
        List<CheckStepPo> checkStepPos = checkStepDtoMapper.selectSelContCheck(selContId);
        HashMap<String, Object> selContMap = new HashMap<>();
        StringBuffer selCont = new StringBuffer();
        selCont.append("服务商资质描述:").append(projSelContVo.getContQlyReq()).append("。");
        selCont.append("拟签订合同名称：").append(projSelContVo.getDealName()).append("。");
        selCont.append("选商列表：").append(projSelContVo.getProjContListVos()).append("。");
        selCont.append("项目名称：").append(projSelContVo.getProjName()).append("。");
        selContMap.put("selCont",selCont.toString());
        selContMap.put("checkMan",checkStepPos);
        String selContLog = JSONObject.toJSONString(selContMap);
        //删除选商数据，审核列表,并修改状态为待选商
        //1.删除选商列表
        List<ProjContListVo> projContListVos = projSelContVo.getProjContListVos();
        for (ProjContListVo projContListVo : projContListVos) {
            String contListId = projContListVo.getContListId();
            projContListService.delete(contListId);
        }
        //2.删除选商附件
        List<AttachVo> attachVos = projSelContVo.getAttachVos();
        for (AttachVo attachVo : attachVos) {
            String attachId = attachVo.getAttachId();
            attachService.delete(attachId);
        }
        //3.删除审核步骤
        deleteCheckStep(checkStepPos);
        //4.修改待选商状态
        BizProjSelContDto bizProjSelContDto = new BizProjSelContDto();
        bizProjSelContDto.setSelContId(selContId);
        bizProjSelContDto.setProjStatus(ProjSelContStatusEnum.WAITSELCONT.getKey());
        selContService.updateNotNull(bizProjSelContDto);
        return selContLog;
    }

    /**
     * 采购计划操作
     * @param planId
     * @param projId
     * @return
     */
    private String planOperation(String planId,String projId){
        Map<String, Object> planParam = new HashMap<>();
        planParam.put("planId", planId);
        ProjPurcPlanVo projPurcPlanVo = selectPurcPlanDetails(planParam);
        //审核意见，以及审批步骤
        List<CheckStepPo> planCheckStep = checkStepDtoMapper.seelctPlanCheck(planId);
        HashMap<String, Object> planMap = new HashMap<>();
        StringBuffer plan = new StringBuffer();
//        plan.append("采购方案说明：").append(projPurcPlanVo.getPlanNotes()).append("。");
//        plan.append("服务采购方案审批列表：").append(projPurcPlanVo.getProjPlanListVos()).append("。");
//        plan.append("经办部门：").append(projPurcPlanVo.getDeptName()).append("。");
//        planMap.put("plan",plan.toString());
        planMap.put("planCheckMan",projPurcPlanVo);
        String planLog = JSONObject.toJSONString(planMap);
        //删除采购计划，审核数据修改项目为选商状态
        //1.删除采购计划及附件
        deleteChain(planId);
        //2.删除审核步骤
        deleteCheckStep(planCheckStep);
        //3.删除计划列表
        List<ProjPlanListVo> projPlanListVos = projPurcPlanVo.getProjPlanListVos();
        for (ProjPlanListVo projPlanListVo : projPlanListVos) {
            String planListId = projPlanListVo.getPlanListId();
            projPlanListService.delete(planListId);
        }
        //4.修改项目状态为选商
        BizProjProjectDto bizProjProjectDto = new BizProjProjectDto();
        bizProjProjectDto.setProjId(projId);
        bizProjProjectDto.setProjPhase(ProjPhaseEnum.SELCONT.getKey());
        projProjectService.updateNotNull(bizProjProjectDto);
        return planLog;
    }

    /**
     * 采购结果操作
     * @param resultId
     * @param projId
     * @return
     */
    private String resultOperation(String resultId,String projId){
        Map<String, Object> resultParam = new HashMap<>();
        resultParam.put("resultId", resultId);
        ProjPurcResultVo projPurcResultVo = projPurcResultService.selectPurcResultDetails(resultParam);
        if (Optional.ofNullable(projPurcResultVo).isPresent()) {
            //审核意见，以及审批步骤
            List<CheckStepPo> resultCheckStep = checkStepDtoMapper.selectResultCheck(resultId);
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("result",projPurcResultVo);
            resultMap.put("resultCheckMan",resultCheckStep);
            String resultLog = JSONObject.toJSONString(resultMap);
            //删除采购计划，审核数据修改项目为选商状态
            //1.删除采购结果及附件
            projPurcResultService.deleteChain(resultId);
            //2.删除审核步骤
            deleteCheckStep(resultCheckStep);

            //4.修改项目状态为选商
            BizProjProjectDto bizProjProjectDto = new BizProjProjectDto();
            bizProjProjectDto.setProjId(projId);
            bizProjProjectDto.setProjPhase(ProjPhaseEnum.SELCONT.getKey());
            projProjectService.updateNotNull(bizProjProjectDto);
            return resultLog;
        }
        return null;
    }


    /**
     * 删除审核步骤
     * @param checkStepPos
     */
    private void deleteCheckStep(List<CheckStepPo> checkStepPos){
        for (CheckStepPo checkStepPo : checkStepPos) {
            if (checkStepPo != null){
                String stepId = checkStepPo.getStepId();
                checkStepDtoMapper.deleteByPrimaryKey(stepId);
                checkManDtoMapper.deleteByStepId(stepId);
            }
        }
    }
}

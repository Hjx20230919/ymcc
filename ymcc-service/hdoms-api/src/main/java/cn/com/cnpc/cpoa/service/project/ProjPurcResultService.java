package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.assembler.project.ProjPlanContAssembler;
import cn.com.cnpc.cpoa.assembler.project.ProjPurcPlanAssembler;
import cn.com.cnpc.cpoa.assembler.project.ProjPurcResultAssembler;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.project.*;
import cn.com.cnpc.cpoa.enums.CheckTypeEnum;
import cn.com.cnpc.cpoa.enums.DealWebTypeEnum;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.enums.project.ProPlanStatusEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.enums.project.ProjectWebTypeEnum;
import cn.com.cnpc.cpoa.mapper.ProjPurcHideDtoMapper;
import cn.com.cnpc.cpoa.mapper.project.BizProjProjectDtoMapper;
import cn.com.cnpc.cpoa.mapper.project.BizProjPurcResultDtoMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.ActivitiService;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.pdf.ProjPurcPDFBuildUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.AuditVo;
import cn.com.cnpc.cpoa.vo.project.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:22
 * @Description:
 */
@Service
public class ProjPurcResultService extends AppService<BizProjPurcResultDto> {


    @Autowired
    BizProjPurcResultDtoMapper bizProjPurcResultDtoMapper;

    @Autowired
    ProjProjectService projProjectService;

    @Autowired
    UserService userService;

    @Autowired
    ProjResultListService projResultListService;

    @Autowired
    ProjectAuditService projectAuditService;

    @Autowired
    BizCheckStepService bizCheckStepService;


    @Autowired
    BizProjProjectDtoMapper bizProjProjectDtoMapper;

    @Autowired
    ProjPlanListService projPlanListService;

    @Autowired
    ProjPlanContService projPlanContService;

    @Autowired
    AttachService attachService;

    @Autowired
    ProjPurcResultAttachService projPurcResultAttachService;

    @Autowired
    ActivitiService activitiService;

    @Autowired
    private ProjPurcHideDtoMapper projPurcHideDtoMapper;

    @Autowired
    private ProjChangeLogService changeLogService;

    @Autowired
    private ProjPurcPlanService purcPlanService;


    public List<ProjProjectResultVo> selectProjPurcResult(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjPurcResultDtoMapper.selectProjPurcResult(params);

        List<ProjProjectResultVo> vos = new ArrayList<>();
        for (BizProjProjectDto projProjectDto : dtos) {
            vos.add(ProjPurcResultAssembler.convertDtoToResultVo(projProjectDto));
        }
        for (ProjProjectResultVo vo : vos) {
            BizProjPurcResultDto bizProjPurcResultDto = selectByKey(vo.getResultId());
            vo.setSelContId(bizProjPurcResultDto.getSelContId());
        }
        return vos;
    }

    public List<ProjProjectResultVo> selectAuditProjPurcResult(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjPurcResultDtoMapper.selectAuditProjPurcResult(params);
        dtos = projProjectService.selectUserNameList(dtos);
        List<ProjProjectResultVo> vos = new ArrayList<>();
        for (BizProjProjectDto projProjectDto : dtos) {
            vos.add(ProjPurcResultAssembler.convertDtoToResultVo(projProjectDto));
        }

        return vos;
    }

    public List<ProjProjectResultVo> selectAuditedProjPurcResult(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjPurcResultDtoMapper.selectAuditedProjPurcResult(params);
        dtos = projProjectService.selectUserNameList(dtos);
        List<ProjProjectResultVo> vos = new ArrayList<>();
        for (BizProjProjectDto projProjectDto : dtos) {
            vos.add(ProjPurcResultAssembler.convertDtoToResultVo(projProjectDto));
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
            list.add(projProjectDto.getResultId());
            projProjectDtoHashMap.put(projProjectDto.getResultId(), projProjectDto);
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

    @Transactional(rollbackFor = Exception.class)
    public BizProjPurcResultDto addProjPurcResult(ProjPurcResultVo vo, String userId, String type) throws Exception {
        SysUserDto userDto = userService.selectByKey(userId);
        BizProjPurcResultDto dto = ProjPurcResultAssembler.convertVoToDto(vo);
        dto.setProjStatus(ProjectWebTypeEnum.getEnumByKey(type));
        dto.setCreateId(userId);
        dto.setOwnerDeptId(userDto.getDeptId());
        dto.setResultId(StringUtils.getUuid32());
        dto.setCreateAt(DateUtils.getNowDate());
        dto.setProjPhase(ProjPhaseEnum.SELCONT.getKey());
        dto.setPlanNo(getDealNo(ProjectConstant.ProjPhaseType.CGJG));


        //新增明细记录
        List<ProjRsultListVo> projRsultListVos = vo.getProjRsultListVos();

        List<BizProjResultListDto> projResultListDtos = new ArrayList();
        for (ProjRsultListVo projRsultListVo : projRsultListVos) {
            projResultListDtos.add(ProjPurcResultAssembler.convertVoToListDto(projRsultListVo, dto.getResultId()));
        }


        save(dto);
        projResultListService.saveList(projResultListDtos);


        //新增附件
        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getResultId(), FileOwnerTypeEnum.PROJECT.getKey(), vo.getAttachVos());

        //获取中间表
        List<BizProjPurcResultAttachDto> projPurcResultAttachDtos = projPurcResultAttachService.getProjPurcPlanAttachDtos(dto.getResultId(), attachDtos);

        BizProjProjectDto projProjectDto = projProjectService.selectByKey(dto.getProjId());
        //保存附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.PURCHASE.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        attachService.updateAttachs(attachDtos, userId, proToFileUri);
        //保存中間表
        projPurcResultAttachService.saveList(projPurcResultAttachDtos);


        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.REPURCHASESERVICE);
        //7 如果状态为 保存提交 则生成审核相关
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            activitiService.buildDiyProjPurcActiviti(vo.getAuditVo(), userId);

        }
        //8 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if (DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)) {
            projProjectService.getBackPendingMessage(dto.getResultId(), ProjectConstant.AuditService.REPURCHASESERVICE, auditService);

            bizCheckStepService.updateBackObj(dto.getResultId());
        }

        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateProjPurcResult(ProjPurcResultVo vo, String userId, String type) throws Exception {
        SysUserDto userDto = userService.selectByKey(userId);

        BizProjPurcResultDto dto = ProjPurcResultAssembler.convertVoToDto(vo);
        dto.setProjStatus(ProjectWebTypeEnum.getEnumByKey(type));
        dto.setCreateId(userId);
        dto.setOwnerDeptId(userDto.getDeptId());
        dto.setPlanNo(null);

        //删除现有的明细记录
        Map<String, Object> params = new HashMap<>();
        params.put("resultId", dto.getResultId());
        List<BizProjResultListDto> resultListDtos = projResultListService.selectResultList(params);
        List<Object> resListKeys = new ArrayList<>();
        for (BizProjResultListDto projResultListDto : resultListDtos) {
            resListKeys.add(projResultListDto.getResultListId());
        }


        List<ProjRsultListVo> projRsultListVos = vo.getProjRsultListVos();

        List<BizProjResultListDto> projResultListDtos = new ArrayList();
        for (ProjRsultListVo projRsultListVo : projRsultListVos) {
            projResultListDtos.add(ProjPurcResultAssembler.convertVoToListDto(projRsultListVo, dto.getResultId()));
        }

        projResultListService.deleteList(resListKeys);
        updateNotNull(dto);
        projResultListService.saveList(projResultListDtos);


        BizProjProjectDto projProjectDto = projProjectService.selectByKey(dto.getProjId());

        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getResultId(), FileOwnerTypeEnum.PROJECT.getKey(), vo.getAttachVos());

        //获得已存在的中间表信息
        Map<String, Object> param = new HashMap<>();
        param.put("resultId", dto.getResultId());
        List<BizProjPurcResultAttachDto> projPurcResultAttachDtos = projPurcResultAttachService.selectProjPurcResultAttachDto(param);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getResultRemoveMap(attachDtos, projPurcResultAttachDtos);
        //2 删除附件
        attachService.deleteByMap(removeMap);
        //3 删除中间表
        projPurcResultAttachService.deleteByMap(removeMap);
        //4 执行更新
        updateNotNull(dto);
        //5 新增附件 返回新增的附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.PURCHASE.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri);
        //6 为新增的附件保存 中间表
        List<BizProjPurcResultAttachDto> newProjPurcResultAttachDtos = projPurcResultAttachService.getProjPurcPlanAttachDtos(dto.getResultId(), newAttachDtos);
        projPurcResultAttachService.saveList(newProjPurcResultAttachDtos);


        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.REPURCHASESERVICE);
        //7 如果状态为 保存提交 则生成审核相关
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            activitiService.buildDiyProjPurcActiviti(vo.getAuditVo(), userId);
        }
        //8 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if (DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)) {
            projProjectService.getBackPendingMessage(dto.getResultId(), ProjectConstant.AuditService.REPURCHASESERVICE, auditService);

            bizCheckStepService.updateBackObj(dto.getResultId());
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public void maintainProjPurcResult(String resultId,String userId,String resultNotes,List<ProjRsultListVo> projRsultListVos,List<AttachVo> attachVos)throws Exception {
        BizProjPurcResultDto dto = selectByKey(resultId);
        StringBuffer sb = new StringBuffer();
        //日志记录
        ProjChangeLogDto changeLogDto = new ProjChangeLogDto();
        changeLogDto.setChgLogId(StringUtils.getUuid32());
        changeLogDto.setCreateId(userId);
        changeLogDto.setCreateAt(DateUtils.getNowDate());
        changeLogDto.setChgObjType("rePurchase");
        changeLogDto.setChgObjId(resultId);
        if (Optional.ofNullable(resultNotes).isPresent()){
            sb.append("采购结果说明修改:").append("\n");
            sb.append(dto.getResultNotes()).append("->").append(resultNotes).append("\n");
            dto.setResultNotes(resultNotes);
        }

        if (Optional.ofNullable(projRsultListVos).isPresent()){
            //删除现有的明细记录
            Map<String, Object> params = new HashMap<>();
            params.put("resultId", resultId);
            List<BizProjResultListDto> resultListDtos = projResultListService.selectResultList(params);
            List<Object> resListKeys = new ArrayList<>();
            for (BizProjResultListDto projResultListDto : resultListDtos) {
                resListKeys.add(projResultListDto.getResultListId());
            }

            List<BizProjResultListDto> projResultListDtos = new ArrayList();
            for (ProjRsultListVo projRsultListVo : projRsultListVos) {
                projResultListDtos.add(ProjPurcResultAssembler.convertVoToListDto(projRsultListVo, dto.getResultId()));
            }

            projResultListService.deleteList(resListKeys);
            projResultListService.saveList(projResultListDtos);
//            sb.append("服务结果审批表修改:").append("\n");
//            //服务内容，计费内容，计价方式，数量，单价，控制总价,服务地点，备注
//            for (ProjRsultListVo projRsultListVo : projRsultListVos) {
//                String resultListId = projRsultListVo.getResultListId();
//                BizProjResultListDto oldResultList = projResultListService.selectByKey(resultListId);
//                if (!(projRsultListVo.getServiceContext() == null ? "" : projRsultListVo.getServiceContext()).equals((oldResultList.getServiceContext() == null ? "" : oldResultList.getServiceContext()))){
//                    sb.append("服务方式:").append(oldResultList.getServiceContext()).append("->").append(projRsultListVo.getServiceContext()).append("\n");
//                    oldResultList.setServiceContext(projRsultListVo.getServiceContext());
//                }
//                if (!(projRsultListVo.getBillContext() == null ? "" : projRsultListVo.getBillContext()).equals((oldResultList.getBillContext() == null ? "" : oldResultList.getBillContext()))){
//                    sb.append("计费内容:").append(oldResultList.getBillContext()).append("->").append(projRsultListVo.getBillContext()).append("\n");
//                    oldResultList.setBillContext(projRsultListVo.getBillContext());
//                }
//                if (!(projRsultListVo.getValuationContext() == null ? "" : projRsultListVo.getValuationContext()).equals((oldResultList.getValuationContext() == null ? "" : oldResultList.getValuationContext()))){
//                    sb.append("计价方式:").append(oldResultList.getValuationContext()).append("->").append(projRsultListVo.getValuationContext()).append("\n");
//                    oldResultList.setValuationContext(projRsultListVo.getValuationContext());
//                }
//                if ((oldResultList.getUnitPrice() == null ? new BigDecimal(0) : oldResultList.getUnitPrice()).compareTo((projRsultListVo.getEstUnitPrice() == null ? new BigDecimal(0) : projRsultListVo.getEstUnitPrice())) != 0){
//                    sb.append("单价:").append(oldResultList.getUnitPrice()).append("->").append(projRsultListVo.getEstUnitPrice()).append("\n");
//                    oldResultList.setUnitPrice(projRsultListVo.getEstUnitPrice());
//                }
//                if ((oldResultList.getLimitTotalPrice() == null ? new BigDecimal(0) : oldResultList.getLimitTotalPrice()).compareTo((projRsultListVo.getEstTotalPrice() == null ? new BigDecimal(0) : projRsultListVo.getEstTotalPrice())) != 0){
//                    sb.append("控制总价:").append(oldResultList.getLimitTotalPrice()).append("->").append(projRsultListVo.getEstTotalPrice()).append("\n");
//                    oldResultList.setLimitTotalPrice(projRsultListVo.getEstTotalPrice());
//                }
//                if (!(projRsultListVo.getServicePlace() == null ? "" : projRsultListVo.getServicePlace()).equals((oldResultList.getServicePlace() == null ? "" : oldResultList.getServicePlace()))){
//                    sb.append("服务地点:").append(oldResultList.getServicePlace()).append("->").append(projRsultListVo.getServicePlace()).append("\n");
//                    oldResultList.setServicePlace(projRsultListVo.getServicePlace());
//                }
//                if (!(projRsultListVo.getNotes() == null ? "" : projRsultListVo.getNotes()).equals((oldResultList.getNotes() == null ? "" : oldResultList.getNotes()))){
//                    sb.append("备注:").append(oldResultList.getNotes()).append("->").append(projRsultListVo.getNotes()).append("\n");
//                    oldResultList.setNotes(projRsultListVo.getNotes());
//                }
//                if (!(projRsultListVo.getRecomContReason() == null ? "" : projRsultListVo.getRecomContReason()).equals((oldResultList.getRecomContReason() == null ? "" : oldResultList.getRecomContReason()))){
//                    sb.append("推荐服务商理由:").append(oldResultList.getRecomContReason()).append("->").append(projRsultListVo.getRecomContReason()).append("\n");
//                    oldResultList.setRecomContReason(projRsultListVo.getRecomContReason());
//                }
//                projResultListService.updateNotNull(oldResultList);
//            }
        }
        if (Optional.ofNullable(attachVos).isPresent()){
            sb.append("附件修改:").append("\n");
            BizProjProjectDto projProjectDto = projProjectService.selectByKey(dto.getProjId());

            List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getResultId(), FileOwnerTypeEnum.PROJECT.getKey(),attachVos);

            //获得已存在的中间表信息
            Map<String, Object> param = new HashMap<>();
            param.put("resultId", dto.getResultId());
            List<BizProjPurcResultAttachDto> projPurcResultAttachDtos = projPurcResultAttachService.selectProjPurcResultAttachDto(param);
            //1获取要删除的附件信息
            Map<String, String> removeMap = attachService.getResultRemoveMap(attachDtos, projPurcResultAttachDtos);
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
            projPurcResultAttachService.deleteByMap(removeMap);
            //5 新增附件 返回新增的附件
            String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.PURCHASE.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
            List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, dto.getCreateId(), proToFileUri);
            sb.append("\n").append("新增附件:");
            for (BizAttachDto newAttachDto : newAttachDtos) {
                sb.append(newAttachDto.getFileName()).append(";");
            }
            //6 为新增的附件保存 中间表
            List<BizProjPurcResultAttachDto> newProjPurcResultAttachDtos = projPurcResultAttachService.getProjPurcPlanAttachDtos(dto.getResultId(), newAttachDtos);
            projPurcResultAttachService.saveList(newProjPurcResultAttachDtos);
        }
        //4 执行更新
        updateNotNull(dto);
        changeLogDto.setChgObjCtx(sb.toString());
        changeLogService.save(changeLogDto);
    }



    @Transactional(rollbackFor = Exception.class)
    public void deleteChain(String id) {
        Map<String, Object> params = new HashMap<>();
        params.put("resultId", id);
        List<BizProjResultListDto> resultListDtos = projResultListService.selectResultList(params);
        List<Object> resListKeys = new ArrayList<>();
        for (BizProjResultListDto projResultListDto : resultListDtos) {
            resListKeys.add(projResultListDto.getResultListId());
        }

        List<BizProjPurcResultAttachDto> projPurcResultAttachDtos = projPurcResultAttachService.selectProjPurcResultAttachDto(params);

        for (BizProjPurcResultAttachDto projPurcResultAttachDto : projPurcResultAttachDtos) {
            String attachId = projPurcResultAttachDto.getAttachId();
            attachService.deleteById(attachId,"");
            projPurcResultAttachService.delete(projPurcResultAttachDto.getId());
        }

        delete(id);
        projResultListService.deleteList(resListKeys);
    }

    @Transactional(rollbackFor = Exception.class)
    public void submitProjPurcResult(String id, String userId, String type, AuditVo auditVo) throws Exception {
        BizProjPurcResultDto dto = new BizProjPurcResultDto();
        dto.setResultId(id);
        dto.setProjStatus(ProPlanStatusEnum.AUDITING.getKey());
        updateNotNull(dto);

        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.REPURCHASESERVICE);
        if (ProjectWebTypeEnum.AUDITING.getKey().equals(type)) {
            activitiService.buildDiyProjPurcActiviti(auditVo, userId);
        } else {
            projProjectService.getBackPendingMessage(id, ProjectConstant.AuditService.REPURCHASESERVICE, auditService);
            bizCheckStepService.updateBackObj(id);

        }
    }

    public ProjPurcResultVo selectPurcResultDetails(Map<String, Object> params) {

        //1 查询基础信息
        List<BizProjProjectDto> projProjectDtos = bizProjPurcResultDtoMapper.selectProjPurcResult(params);
        BizProjPurcResultDto bizProjPurcResultDto = selectByKey(params);
        List<ProjPurcResultVo> projPurcResultVos = new ArrayList<>();
        for (BizProjProjectDto projProjectDto : projProjectDtos) {
            projPurcResultVos.add(ProjPurcResultAssembler.convertDtoToProjPurcResultVo(projProjectDto));
        }
        if (projPurcResultVos.size() > 0) {

            ProjPurcResultVo projPurcResultVo = projPurcResultVos.get(0);

            //2 查询选商列表
            List<BizProjResultListDto> projResultListDtos = projResultListService.selectResultList(params);

            List<ProjRsultListVo> projRsultListVos = new ArrayList<>();
            for (BizProjResultListDto projResultListDto : projResultListDtos) {
                ProjRsultListVo projRsultListVo = new ProjRsultListVo();
                BeanUtils.copyBeanProp(projRsultListVo, projResultListDto);
                projRsultListVo.setEstUnitPrice(projResultListDto.getUnitPrice());
                projRsultListVo.setEstTotalPrice(projResultListDto.getLimitTotalPrice());
                projRsultListVos.add(projRsultListVo);
            }

            projPurcResultVo.setProjRsultListVos(projRsultListVos);

            //3 查询附件信息
            Map<String, Object> param = new HashMap<>();
            param.put("objId", params.get("resultId"));
            List<AttachVo> attachVos = new ArrayList<>();
            List<BizAttachDto> attachDtos = attachService.selectListByObjId(param);
            for (BizAttachDto attachDto : attachDtos) {
                AttachVo attachVo = new AttachVo();
                BeanUtils.copyBeanProp(attachVo, attachDto);
                attachVos.add(attachVo);
            }

            projPurcResultVo.setAttachVos(attachVos);
            projPurcResultVo.setSelContId(bizProjPurcResultDto.getSelContId());


            return projPurcResultVo;
        }
        return null;
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

        return bizProjPurcResultDtoMapper.selectCurrentDealNo(param);
    }

    public void buildResultPDF(HttpServletResponse response, String tempurl, String pdfPicUrl,
                               String baseFontUrl, String resultId) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("resultId", resultId);
        ProjPurcResultVo projPurcResultVo = selectPurcResultDetails(params);


        List<Map<String, Object>> table2 = new ArrayList<>();
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
            table2.add(map);
        }
        //ProjPurcPDFBuildUtils.buildResultPDF(response, tempurl, pdfPicUrl, baseFontUrl, projPurcResultVo, table2);
    }

    /**
     * 采购方案冻结
     * @param resultId
     * @return
     */
    public int freeze(String resultId) {
        ProjPurcHideDto purcHideDto = ProjPurcHideDto.builder()
                .purcHideId(StringUtils.getUuid32())
                .purcType(CheckTypeEnum.REPURCHASE.getKey())
                .purcId(resultId)
                .build();
        return projPurcHideDtoMapper.insert(purcHideDto);

    }

    /**
     * 采购方案解冻
     * @param resultId
     * @return
     */
    public int unfreeze(String resultId) {
        ProjPurcHideDto purcHideDto = ProjPurcHideDto.builder()
                .purcType(CheckTypeEnum.REPURCHASE.getKey())
                .purcId(resultId)
                .build();
        return projPurcHideDtoMapper.delete(purcHideDto);

    }

    public List<String> selectResultId() {
        return bizProjPurcResultDtoMapper.selectResultId();
    }
}

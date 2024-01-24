package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.assembler.project.ProjContListAssembler;
import cn.com.cnpc.cpoa.assembler.project.ProjSelContAssembler;
import cn.com.cnpc.cpoa.common.constants.ProjectConstant;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.domain.project.*;
import cn.com.cnpc.cpoa.enums.DealWebTypeEnum;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.enums.project.ProProjectStatusEnum;
import cn.com.cnpc.cpoa.enums.project.ProjPhaseEnum;
import cn.com.cnpc.cpoa.enums.project.ProjectWebTypeEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContContractorDtoMapper;
import cn.com.cnpc.cpoa.mapper.project.BizProjProjectDtoMapper;
import cn.com.cnpc.cpoa.mapper.project.BizProjSelContDtoMapper;
import cn.com.cnpc.cpoa.po.CheckStepPo;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.constractor.ContContractorService;
import cn.com.cnpc.cpoa.service.project.audit.ProjectAuditService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.utils.pdf.ProjPurcPDFBuildUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.project.ProjContListVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectSelVo;
import cn.com.cnpc.cpoa.vo.project.ProjPurcPlanVo;
import cn.com.cnpc.cpoa.vo.project.ProjSelContVo;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:08
 * @Description:
 */
@Service
public class ProjSelContService extends AppService<BizProjSelContDto> {

    @Autowired
    BizProjSelContDtoMapper bizProjSelContDtoMapper;

    @Autowired
    UserService userService;

    @Autowired
    AttachService attachService;

    @Autowired
    ProjSelContAttachService projSelContAttachService;

    @Autowired
    ProjProjectService projProjectService;

    @Autowired
    ProjectAuditService projectAuditService;

    @Autowired
    BizCheckStepService bizCheckStepService;

    @Autowired
    ProjContListService projContListService;

    @Autowired
    BizProjProjectDtoMapper bizProjProjectDtoMapper;

    @Autowired
    private ProjChangeLogService changeLogService;

    @Autowired
    private ContContractorService contractorService;


    public List<ProjProjectSelVo> selectProjSelCont(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjSelContDtoMapper.selectProjSelCont(params);
        List<ProjProjectSelVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            vos.add(ProjSelContAssembler.convertDtoToSelVo(dto));
        }
        return vos;
    }

    public BizProjSelContDto selectByprojId(String pojId){
        BizProjSelContDto bizProjSelContDto = bizProjSelContDtoMapper.selectByprojId(pojId);
        return bizProjSelContDto;
    }
    public  void updateByselContId(BizProjSelContDto selContId){
        bizProjSelContDtoMapper.updateByselContId(selContId);
    }


    @Transactional(rollbackFor = Exception.class)
    public BizProjSelContDto addSelCont(ProjSelContVo vo, String userId, String type) throws Exception {
        SysUserDto userDto = userService.selectByKey(userId);
        BizProjSelContDto dto = ProjSelContAssembler.convertVoToDto(vo);
        dto.setProjStatus(ProjectWebTypeEnum.getEnumByKey(type));
        dto.setCreateId(userId);
        dto.setOwnerDeptId(userDto.getDeptId());
        dto.setSelContId(StringUtils.getUuid32());
        dto.setCreateAt(DateUtils.getNowDate());
        dto.setProjPhase(ProjPhaseEnum.SELCONT.getKey());

        //新增附件
        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getSelContId(), FileOwnerTypeEnum.PROJECT.getKey(), vo.getAttachVos());

        //获取中间表
        List<BizProjSelContAttachDto> proProjectAttachDtos = projSelContAttachService.getSelContAttachDtos(dto.getSelContId(), attachDtos);

        save(dto);

        //保存选商列表
        List<BizProjContListDto> projContListDtos = getProjContListDtos(vo.getProjContListVos(), dto.getSelContId());
        projContListService.saveList(projContListDtos);

        BizProjProjectDto projProjectDto = projProjectService.selectByKey(dto.getProjId());
        //保存附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.SELCONT.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        attachService.updateAttachs(attachDtos, userId, proToFileUri);

        //保存中間表
        projSelContAttachService.saveList(proProjectAttachDtos);


        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.SELCONTSERVICE);
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            auditService.initActiviti(userId, dto.getSelContId());
        }
        return dto;
    }

    public List<BizProjContListDto> getProjContListDtos(List<ProjContListVo> projContListVos, String selContId) {
        List<BizProjContListDto> projContListDtos = new ArrayList<>();
        for (ProjContListVo contListVo : projContListVos) {
            BizProjContListDto projContListDto = ProjContListAssembler.convertVoToDto(contListVo);
            projContListDto.setContListId(StringUtils.getUuid32());
            projContListDto.setSelContId(selContId);
            projContListDtos.add(projContListDto);
        }

        return projContListDtos;
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateSelCont(ProjSelContVo vo, String userId, String type) throws Exception {
        SysUserDto userDto = userService.selectByKey(userId);
        String selContId = vo.getSelContId();
        BizProjSelContDto projSelContDto = selectByKey(selContId);
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projSelContDto.getProjId());

        BizProjSelContDto dto = ProjSelContAssembler.convertVoToDto(vo);
        dto.setSelContId(selContId);
        dto.setProjStatus(ProjectWebTypeEnum.getEnumByKey(type));
        dto.setCreateId(userId);
        dto.setOwnerDeptId(userDto.getDeptId());

        //删除选商列表
        Map<String, Object> param = new HashMap<>();
        param.put("selContId", selContId);
        List<BizProjContListDto> removeContList = projContListService.selectProjContList(param);
        List<Object> contListKey = new ArrayList<>();
        for (BizProjContListDto projContListDto : removeContList) {
            contListKey.add(projContListDto.getContListId());
        }
        projContListService.deleteList(contListKey);

        //保存选商列表
        List<BizProjContListDto> projContListDtos = getProjContListDtos(vo.getProjContListVos(), dto.getSelContId());
        projContListService.saveList(projContListDtos);


        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getSelContId(), FileOwnerTypeEnum.PROJECT.getKey(), vo.getAttachVos());

        //获得已存在的中间表信息
        Map<String, Object> params = new HashMap<>();
        params.put("selContId", selContId);
        List<BizProjSelContAttachDto> projSelContAttachDtos = projSelContAttachService.selectProjSelContAttachDto(params);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getSelRemoveMap(attachDtos, projSelContAttachDtos);
        //2 删除附件
        attachService.deleteByMap(removeMap);
        //3 删除中间表
        projSelContAttachService.deleteByMap(removeMap);
        //4 执行更新
        updateNotNull(dto);
        //5 新增附件 返回新增的附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.SELCONT.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri);
        //6 为新增的附件保存 中间表
        List<BizProjSelContAttachDto> newProjSelContAttachDtos = projSelContAttachService.getSelContAttachDtos(dto.getSelContId(), newAttachDtos);
        projSelContAttachService.saveList(newProjSelContAttachDtos);

        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.SELCONTSERVICE);

        //7 如果状态为 保存提交 则生成审核相关
        if (DealWebTypeEnum.BUILDAUDITING.getKey().equals(type)) {
            auditService.initActiviti(userId, selContId);
        }
        //8 若是退回修改该合同那个回退记录。状态改为待处理，结果保持不变
        if (DealWebTypeEnum.BACKBUILDAUDITING.getKey().equals(type)) {
            projProjectService.getBackPendingMessage(dto.getSelContId(), ProjectConstant.AuditService.SELCONTSERVICE, auditService);
            bizCheckStepService.updateBackObj(selContId);
        }

    }


    @Transactional(rollbackFor = Exception.class)
    public void maintainSelCont(String selContId,List<AttachVo> attachVos,List<ProjContListVo> projContListVos,String notes,String contQlyReq,String userId,
    String projName,String dealName) throws Exception {
        BizProjSelContDto projSelContDto = selectByKey(selContId);
        //项目信息
        StringBuffer sb = new StringBuffer();
        BizProjProjectDto projProjectDto = projProjectService.selectByKey(projSelContDto.getProjId());
        //日志记录
        ProjChangeLogDto changeLogDto = new ProjChangeLogDto();
        changeLogDto.setChgLogId(StringUtils.getUuid32());
        changeLogDto.setCreateId(userId);
        changeLogDto.setCreateAt(DateUtils.getNowDate());
        changeLogDto.setChgObjType("selCont");
        changeLogDto.setChgObjId(selContId);
        if (Optional.ofNullable(notes).isPresent()){
            sb.append("选商备注修改:").append("\n");
            sb.append(projSelContDto.getNotes()).append("->").append(notes).append("\n");
            projSelContDto.setNotes(notes);
        }
        if (Optional.ofNullable(contQlyReq).isPresent()){
            sb.append("服务商资质描述修改:").append("\n");
            sb.append(projSelContDto.getContQlyReq()).append("->").append(contQlyReq).append("\n");
            projSelContDto.setContQlyReq(contQlyReq);
        }

        if (Optional.ofNullable(projContListVos).isPresent()){
            //选商修改
            updateSelContList(sb,selContId,projContListVos);
        }
        if (Optional.ofNullable(attachVos).isPresent()){
            //附件修改
            updateAttach(sb,selContId,userId,attachVos,projSelContDto,projProjectDto);
        }
        // 执行更新
        updateNotNull(projSelContDto);
        changeLogDto.setChgObjCtx(sb.toString());
        changeLogService.save(changeLogDto);
    }

    /**
     * 选商列表修改
     * @param sb
     * @param selContId
     * @param projContListVos
     */
    private void updateSelContList(StringBuffer sb,String selContId,List<ProjContListVo> projContListVos){
        //查询原选商列表
        sb.append("承包商修改:").append("\n");
        Map<String, Object> param = new HashMap<>();
        param.put("selContId", selContId);
        List<BizProjContListDto> removeContList = projContListService.selectProjContList(param);
        List<String> oldContList = new ArrayList<>();
        for (BizProjContListDto projContListDto : removeContList) {
            oldContList.add(projContListDto.getContId());
        }
        List<String> newContList = new ArrayList<>();
        for (ProjContListVo projContListVo : projContListVos) {
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
        List<BizProjContListDto> projContListDtos = getProjContListDtos(projContListVos, selContId);
        projContListService.saveList(projContListDtos);
    }

    /**
     * 附件修改
     * @param sb
     * @param selContId
     * @param userId
     * @param attachVos
     * @param projSelContDto
     * @param projProjectDto
     */
    private void updateAttach(StringBuffer sb,String selContId,String userId,List<AttachVo> attachVos,BizProjSelContDto projSelContDto,BizProjProjectDto projProjectDto) throws Exception {
        sb.append("附件修改:").append("\n");
        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(selContId, FileOwnerTypeEnum.PROJECT.getKey(), attachVos);

        //获得已存在的中间表信息
        Map<String, Object> params = new HashMap<>();
        params.put("selContId", selContId);
        List<BizProjSelContAttachDto> projSelContAttachDtos = projSelContAttachService.selectProjSelContAttachDto(params);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getSelRemoveMap(attachDtos, projSelContAttachDtos);
        //修改后不要的附件存放地址
        String updProToFileUri = attachService.getupdProToFileUri(ProjPhaseEnum.SELCONT.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
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
        projSelContAttachService.deleteByMap(removeMap);

        //5 新增附件 返回新增的附件
        String proToFileUri = attachService.getProToFileUri(ProjPhaseEnum.SELCONT.getKey(), projProjectDto.getSelContType(), projProjectDto.getDealNo());
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, userId, proToFileUri);
        sb.append("\n").append("新增附件:");
        for (BizAttachDto newAttachDto : newAttachDtos) {
            sb.append(newAttachDto.getFileName()).append(";");
        }
        //6 为新增的附件保存 中间表
        List<BizProjSelContAttachDto> newProjSelContAttachDtos = projSelContAttachService.getSelContAttachDtos(selContId, newAttachDtos);
        projSelContAttachService.saveList(newProjSelContAttachDtos);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteSelAttach(String selContId) {
        Map<String, Object> params = new HashMap<>();
        params.put("selContId", selContId);
        List<BizProjSelContAttachDto> projSelContAttachDtos = projSelContAttachService.selectProjSelContAttachDto(params);

        for (BizProjSelContAttachDto projSelContAttachDto : projSelContAttachDtos) {
            String attachId = projSelContAttachDto.getAttachId();
            String id = projSelContAttachDto.getId();
            attachService.deleteById(attachId,"");
            projSelContAttachService.delete(id);
        }
        delete(selContId);

        return true;

    }


    @Transactional(rollbackFor = Exception.class)
    public void submitSelCont(String id, String userId, String type) throws Exception {
        BizProjSelContDto projSelContDto = new BizProjSelContDto();
        projSelContDto.setSelContId(id);
        projSelContDto.setProjStatus(ProProjectStatusEnum.AUDITING.getKey());
        updateNotNull(projSelContDto);

        ProjectAuditService auditService = projectAuditService.getAuditService(ProjectConstant.AuditService.SELCONTSERVICE);
        if (ProjectWebTypeEnum.AUDITING.getKey().equals(type)) {
            auditService.initActiviti(userId, id);
        } else {
            projProjectService.getBackPendingMessage(id, ProjectConstant.AuditService.SELCONTSERVICE, auditService);
            bizCheckStepService.updateBackObj(id);
        }
    }


    public List<ProjProjectSelVo> selectAuditProjSelCont(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjSelContDtoMapper.selectAuditProjSelCont(params);
        dtos = selectUserNameList(dtos);
        List<ProjProjectSelVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            vos.add(ProjSelContAssembler.convertDtoToSelVo(dto));
        }
        return vos;
    }

    public List<ProjProjectSelVo> selectAuditedProjSelCont(Map<String, Object> params) {
        List<BizProjProjectDto> dtos = bizProjSelContDtoMapper.selectAuditedProjSelCont(params);
        dtos = selectUserNameList(dtos);
        List<ProjProjectSelVo> vos = new ArrayList<>();
        for (BizProjProjectDto dto : dtos) {
            vos.add(ProjSelContAssembler.convertDtoToSelVo(dto));
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
            list.add(projProjectDto.getSelContId());
            projProjectDtoHashMap.put(projProjectDto.getSelContId(), projProjectDto);
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

    public ProjSelContVo selectSelDetails(Map<String, Object> params) {

        //1 查询基础信息
        List<BizProjProjectDto> projProjectDtos = bizProjSelContDtoMapper.selectProjSelCont(params);
        List<ProjSelContVo> projSelContVos = new ArrayList<>();
        for (BizProjProjectDto projProjectDto : projProjectDtos) {
            projSelContVos.add(ProjSelContAssembler.convertDtoToProjSelContVo(projProjectDto));
        }
        ProjSelContVo projSelContVo = projSelContVos.get(0);

        //2 查询选商列表
        List<ProjContListVo> contListVos = projContListService.selectContList(params);
        projSelContVo.setProjContListVos(contListVos);


        //3 查询附件信息
        Map<String, Object> param = new HashMap<>();
        param.put("objId", params.get("selContId"));
        List<AttachVo> attachVos = new ArrayList<>();
        List<BizAttachDto> attachDtos = attachService.selectListByObjId(param);
        for (BizAttachDto attachDto : attachDtos) {
            AttachVo attachVo = new AttachVo();
            BeanUtils.copyBeanProp(attachVo, attachDto);
            attachVos.add(attachVo);
        }

        projSelContVo.setAttachVos(attachVos);


        return projSelContVo;


    }

    public void buildSelContPDF(HttpServletResponse response, String tempurl, String pdfPicUrl, String baseFontUrl, String selContId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("selContId", selContId);
        ProjSelContVo projSelContVo = selectSelDetails(params);

        projSelContVo.setProjName(projProjectService.selectByKey(projSelContVo.getProjId()).getProjName());
        List<Map<String, Object>> table2 = new ArrayList<Map<String, Object>>();

        //添加 合同签约审查审批表审核意见
        Map<String, Object> params2 = new HashMap<>();
        params2.put("objId", selContId);
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

        //ProjPurcPDFBuildUtils.buildSelContPDF(response, tempurl, pdfPicUrl, baseFontUrl, projSelContVo,table2);
    }
}

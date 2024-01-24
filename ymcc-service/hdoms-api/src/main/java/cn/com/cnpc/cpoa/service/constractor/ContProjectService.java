package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContAccessAssembler;
import cn.com.cnpc.cpoa.assembler.ContContractorAssembler;
import cn.com.cnpc.cpoa.assembler.ContProjectAssembler;
import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizContBlackListDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.contractor.*;
import cn.com.cnpc.cpoa.enums.contractor.ContProjectStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContWebTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizContBlackListDtoMapper;
import cn.com.cnpc.cpoa.mapper.contractor.BizContContractorDtoMapper;
import cn.com.cnpc.cpoa.mapper.contractor.BizContProjectDtoMapper;
import cn.com.cnpc.cpoa.mapper.contractor.ContProjectHisDtoMapper;
import cn.com.cnpc.cpoa.po.contractor.ContContractorPo;
import cn.com.cnpc.cpoa.po.contractor.ContProjectPo;
import cn.com.cnpc.cpoa.service.BizCheckStepService;
import cn.com.cnpc.cpoa.service.ContBlackListService;
import cn.com.cnpc.cpoa.service.UserService;
import cn.com.cnpc.cpoa.service.constractor.audit.ConstractorAuditService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAccessProjVo;
import cn.com.cnpc.cpoa.vo.contractor.ContProjectVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/9 20:46
 * @Description:
 */
@Service
public class ContProjectService extends AppService<BizContProjectDto> {

    @Autowired
    BizContProjectDtoMapper bizContProjectDtoMapper;

    @Autowired
    BizContContractorDtoMapper bizContContractorDtoMapper;

    @Autowired
    ContAccessService contAccessService;

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    ContAccessProjService contAccessProjService;

    @Autowired
    ConstractorAuditService constractorAuditService;

    @Autowired
    BizCheckStepService bizCheckStepService;

    @Autowired
    UserService userService;

    @Autowired
    private ContBlackListService blackListService;

    @Autowired
    private ContProjectHisDtoMapper contProjectHisDtoMapper;



    @Transactional
    public BizContProjectDto addContProject(String userId, ContProjectVo contProjectVo) throws Exception {
        //基础信息校验
        checkContProjectParam(contProjectVo);

        //准入项目是否为当前用户所属部门
        SysUserDto userDto = userService.selectByKey(userId);
//        if (StringUtils.isNotEmpty(contProjectVo.getOwnerDeptId())&&!contProjectVo.getOwnerDeptId().equals(userDto.getDeptId())) {
//            throw new AppException("您不能新增其他部门的准入项目！");
//        }

        BizContProjectDto contProjectDto = ContProjectAssembler.convertVoToDto(contProjectVo);
        contProjectDto.setProjId(StringUtils.getUuid32());
        contProjectDto.setOwnerId(userId);
        contProjectDto.setOwnerDeptId(userDto.getDeptId());
        contProjectDto.setProjState(ContWebTypeEnum.getEnumByKey(contProjectVo.getType()));
        contProjectDto.setProjAt(DateUtils.getNowDate());
        contProjectDto.setProjStateAt(DateUtils.getNowDate());
        save(contProjectDto);
        if (ContWebTypeEnum.AUDITING.getKey().equals(contProjectVo.getType())) {
            ConstractorAuditService projectAuditService = constractorAuditService.getAuditService(ContractorConstant.AuditService.PROJECTAUDITSERVICE);
            projectAuditService.initActiviti(userId, contProjectDto.getProjId());
        }
        return contProjectDto;
    }

    public void checkContProjectParam(ContProjectVo contProjectVo) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("projContCode", contProjectVo.getProjContCode());
        List<ContProjectPo> pos = bizContProjectDtoMapper.selectContProject(params);


        //校验当前项目是否有在审核中的
        isAuditing(pos, contProjectVo.getProjId());

        //校验名称是否重复
        isOnly(contProjectVo.getProjContName(), contProjectVo.getProjContCode(), pos);

        //校验当前项目是否已存在
        isExistsProj(contProjectVo.getProjContCode(), contProjectVo.getProjContent());
    }

    private void isAuditing(List<ContProjectPo> pos, String newProjId) {
        List<String> exsitsKeys = new ArrayList<>();
        for (ContProjectPo po : pos) {
            if (ContProjectStateEnum.AUDITING.getKey().equals(po.getProjState()) || ContProjectStateEnum.BACK.getKey().equals(po.getProjState())) {
                exsitsKeys.add(po.getProjId());
            }
        }

        //存在流程中的项目，并且是新准入
        if (StringUtils.isNotEmpty(exsitsKeys) && !exsitsKeys.contains(newProjId)) {
            throw new AppException("您有处于流程中的项目准入，请先处理完后再进行新的准入");
        }
    }

    @Transactional
    public BizContProjectDto upateContProject(String id, String userId, ContProjectVo contProjectVo) throws Exception {
        //基础信息校验
        checkContProjectParam(contProjectVo);

        BizContProjectDto contProjectDto = ContProjectAssembler.convertVoToDto(contProjectVo);
        contProjectDto.setProjId(id);
        contProjectDto.setProjStateAt(DateUtils.getNowDate());
        contProjectDto.setProjState(ContWebTypeEnum.getEnumByKey(contProjectVo.getType()));
        updateNotNull(contProjectDto);
        ConstractorAuditService projectAuditService = constractorAuditService.getAuditService(ContractorConstant.AuditService.PROJECTAUDITSERVICE);
        if (ContWebTypeEnum.AUDITING.getKey().equals(contProjectVo.getType())) {
            projectAuditService.initActiviti(userId, contProjectDto.getProjId());
        }

        if (ContWebTypeEnum.BACKAUDITING.getKey().equals(contProjectVo.getType())) {
            contAccessService.getBackPendingMessage(id, ContractorConstant.AuditService.PROJECTAUDITSERVICE, projectAuditService);

            bizCheckStepService.updateBackObj(id);
//            SysUserDto sysUserDto = userService.selectByKey(contProjectDto.getOwnerId());
//            projectAuditService.getPendingMessage(ContractorConstant.AuditService.PROJECTAUDITSERVICE, id, sysUserDto.getWxopenid()
//                    , DateUtils.getNowDate(), ContractorConstant.ContWXContent.PROJECTCONTENT);


        }

        return contProjectDto;
    }


    public List<ContProjectVo> selectContProject(Map<String, Object> params) {
        List<ContProjectPo> pos = bizContProjectDtoMapper.selectContProject(params);
        List<ContProjectVo> list = new ArrayList<>();
        for (ContProjectPo po : pos) {
            ContProjectVo vo = ContProjectAssembler.convertPoToVo(po);
            if (StringUtils.isNotEmpty(po.getAcessCode())) {
                vo.setAcessUrl(po.getAcceId());
            }
            list.add(vo);
        }
        return list;
    }

    public boolean isOnly(String projContName, String projContCode, List<ContProjectPo> codePos) {

        Map<String, Object> params = new HashMap<>();
        params.put("projContName2", projContName);
        List<ContProjectPo> namePos = bizContProjectDtoMapper.selectContProject(params);
//        Map<String, Object> param = new HashMap<>();
//        param.put("contName", projContName);
//        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(param);
        //TODO  验证承包商是否在黑名单
        HashMap<String, Object> param = new HashMap<>(16);
        param.put("contName",projContName);
        List<BizContBlackListDto> bizContBlackListDtos = blackListService.vaildationBlackList(param);
        if (bizContBlackListDtos.size() > 0){
            throw new AppException(String.format("抱歉，承包商已被列入黑名单，请确认后再试"));
        }

        if (StringUtils.isNotEmpty(namePos)) {
            String contCode = namePos.get(0).getProjContCode();
            if (!projContCode.equals(contCode)) {
                throw new AppException(String.format("抱歉，承包商名称或统一社会信用代码重复在系统中已存在，请确认后再试"));
            }
            return false;
        }

        if (StringUtils.isNotEmpty(codePos)) {
            ContProjectPo contProjectPo = codePos.get(0);
            if (!projContName.equals(contProjectPo.getProjContName())) {
                throw new AppException(String.format("抱歉，承包商名称或统一社会信用代码重复在系统中已存在，请确认后再试"));
            }

        }
        return true;
    }

    public boolean isExistsProj(String projContCode, String content) throws Exception {
        Map<String, Object> param = new HashMap<>();
        param.put("contOrgNo", projContCode);
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(param);
        if (StringUtils.isNotEmpty(contContractorPos)) {
            String[] projContent = content.split(",");
            Map<String, String> contentMap = new HashMap<>();
            List<String> contents = ContractorConstant.proCategoryList;
            List<String> exists = new ArrayList<>();

            Map<String, Object> params = new HashMap<>();
            params.put("contId", contContractorPos.get(0).getContId());
            List<ContAccessProjVo> contAccessProjVos = contAccessProjService.getContAccessProjVo(params);
            for (ContAccessProjVo accessProjVo : contAccessProjVos) {
                contentMap.put(accessProjVo.getProjName(), ContractorConstant.proCategoryMap.get(accessProjVo.getProjName()));
                contents.remove(ContractorConstant.proCategoryMap.get(accessProjVo.getProjName()));
            }

            for (int i = 0; i < projContent.length; i++) {
                if (StringUtils.isNotEmpty(contentMap.get(projContent[i]))) {
                    exists.add(contentMap.get(projContent[i]));
                }
            }
            if (StringUtils.isNotEmpty(exists)) {
                throw new AppException(String.format("准入项目%s已存在,您还可以选择以下项目%s", exists, StringUtils.isNotEmpty(contents) ? contents : "无"));
            }

        }
        return true;
    }

    /**
     * 项目申请通过时 新增准入申请和承包商初始信息
     */
    public Map<String, Object> getProApplyEntity(BizContProjectDto contProjectDto) {

        BizContContractorDto contContractor = null;
        BizContAccessDto contAccess;
        List<BizContAccessProjDto> list;

        //1 查询当前组织机构是否存在
        String projContCode = contProjectDto.getProjContCode();
        Map<String, Object> params = new HashMap<>();
        params.put("contOrgNo", projContCode);
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(params);
        if (StringUtils.isNotEmpty(contContractorPos)) {
            //2 已存在则直接添加 准入申请 和 承包商准入项目
            ContContractorPo contContractorPo = contContractorPos.get(0);
            BizContContractorDto exist = new BizContContractorDto();
            BeanUtils.copyBeanProp(exist, contContractorPo);
            contAccess = ContAccessAssembler.getInitContAccess(exist.getContId(), contProjectDto);
            list = contAccessProjService.getContAccessProjDtoList(exist, contAccess, contProjectDto);
        } else {
            //3 不存在都要新增
            contContractor = ContContractorAssembler.getInitContContractor(contProjectDto);
            contContractor.setAccessLevel(ContractorConstant.AccessLevel.accessLevelMap.get(contProjectDto.getProjAccessType()));
            contAccess = ContAccessAssembler.getInitContAccess(contContractor.getContId(), contProjectDto);
            //设置准入码
            list = contAccessProjService.getContAccessProjDtoList(contContractor, contAccess, contProjectDto);
        }

        Map<String, Object> saveEntity = new HashMap<>();
        saveEntity.put("contContractor", contContractor);
        saveEntity.put("contAccess", contAccess);
        saveEntity.put("list", list);
        return saveEntity;
    }


    @Transactional
    public void saveProApplyEntity(Map<String, Object> saveEntity) {
        BizContContractorDto contContractor = (BizContContractorDto) saveEntity.get("contContractor");
        BizContAccessDto contAccess = (BizContAccessDto) saveEntity.get("contAccess");
        List<BizContAccessProjDto> list = (List<BizContAccessProjDto>) saveEntity.get("list");

        if (null != contContractor) {
            contContractorService.save(contContractor);
        }
        if (null != contAccess) {
            contAccessService.save(contAccess);
        }
        contAccessProjService.saveList(list);
    }


    public List<ContProjectVo> selectAuditContProject(Map<String, Object> params) {
        List<ContProjectPo> pos = bizContProjectDtoMapper.selectAuditContProject(params);
        pos = selectUserNameList(pos);
        List<ContProjectVo> list = new ArrayList<>();
        for (ContProjectPo po : pos) {
            // 设置当前审核人员
            list.add(ContProjectAssembler.convertPoToVo(po));
        }
        return list;

    }

    public List<ContProjectPo> selectUserNameList(List<ContProjectPo> epos) {
        Map<String, ContProjectPo> contProjectPoHashMap = new HashMap<>();
        if (StringUtils.isEmpty(epos)) {
            return epos;
        }
        List<String> list = new ArrayList<>();
        //拿取参数和map
        for (ContProjectPo epo : epos) {
            list.add(epo.getObjId());
            contProjectPoHashMap.put(epo.getObjId(), epo);
        }

        //查询当前审核人员结果
        Map<String, Object> param = new HashMap<>();
        param.put("objIdList", list);
        List<ContProjectPo> pos = bizContProjectDtoMapper.selectUserNameList(param);

        //遍历设置 审核人员
        for (ContProjectPo po : pos) {
            ContProjectPo epo = contProjectPoHashMap.get(po.getObjId());
            List<String> curAuditUsers = epo.getCurAuditUser();
            if (StringUtils.isEmpty(curAuditUsers)) {
                curAuditUsers = new ArrayList<>();
                //这里的username就是当前审核人员的name 偷懒没有重新新建实体了
                curAuditUsers.add(po.getUserName());
                epo.setCurAuditUser(curAuditUsers);
            } else {
                curAuditUsers.add(po.getUserName());
                //epo.setCurAuditUser(curAuditUsers);
            }
        }
        return epos;
    }

    public List<ContProjectPo> selectUserNameListWx(List<String> list) {
        Map<String, Object> param = new HashMap<>();
        param.put("objIdList", list);
        return bizContProjectDtoMapper.selectUserNameList(param);
    }


    @Transactional
    public void submitContProject(String id, String userId, String type) throws Exception {

        BizContProjectDto contProjectDto = selectByKey(id);

        checkContProjectParam(ContProjectAssembler.convertDtoToVo(contProjectDto));

        contProjectDto.setProjState(ContProjectStateEnum.AUDITING.getKey());
        updateNotNull(contProjectDto);

        ConstractorAuditService projectAuditService = constractorAuditService.getAuditService(ContractorConstant.AuditService.PROJECTAUDITSERVICE);
        if (ContWebTypeEnum.AUDITING.getKey().equals(type)) {
            projectAuditService.initActiviti(userId, contProjectDto.getProjId());
        } else if (ContWebTypeEnum.BACKAUDITING.getKey().equals(type)) {
            contAccessService.getBackPendingMessage(id, ContractorConstant.AuditService.PROJECTAUDITSERVICE, projectAuditService);

            bizCheckStepService.updateBackObj(id);
//            SysUserDto sysUserDto = userService.selectByKey(contProjectDto.getOwnerId());
//            projectAuditService.getPendingMessage(ContractorConstant.AuditService.PROJECTAUDITSERVICE, id, sysUserDto.getWxopenid()
//                    , DateUtils.getNowDate(), ContractorConstant.ContWXContent.PROJECTCONTENT);
        }

    }

    public Map<String, String> selectContProjectContent(Map<String, Object> params) {
        Map<String, String> allProCategoryMap = ContractorConstant.proCategoryMap;
        List<ContContractorPo> contContractorPos = bizContContractorDtoMapper.selectContContractor(params);
        if (StringUtils.isNotEmpty(contContractorPos)) {
            ContContractorPo contContractorPo = contContractorPos.get(0);
            Map<String, Object> param = new HashMap<>();
            param.put("contId", contContractorPo.getContId());
            List<BizContAccessProjDto> contAccessProjList = contAccessProjService.getContAccessProjList(param);
            for (BizContAccessProjDto dto : contAccessProjList) {
                allProCategoryMap.remove(dto.getProjName());
            }
        }
        return allProCategoryMap;
    }

    public List<ContProjectVo> selectActivitiItem(Map<String, Object> params) {
        List<ContProjectPo> pos = bizContProjectDtoMapper.selectActivitiItem(params);
        pos = selectUserNameList(pos);
        List<ContProjectVo> list = new ArrayList<>();
        for (ContProjectPo po : pos) {
            list.add(ContProjectAssembler.convertPoToVo(po));
        }
        return list;
    }

    public List<ContProjectVo> selectAuditedContProject(Map<String, Object> params) {
        List<ContProjectPo> pos = bizContProjectDtoMapper.selectAuditedContProject(params);
        pos = selectUserNameList(pos);
        List<ContProjectVo> list = new ArrayList<>();
        for (ContProjectPo po : pos) {
            list.add(ContProjectAssembler.convertPoToVo(po));
        }
        return list;
    }

    /**
     * 对退回的准入申请删除
     * @param projId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public AppMessage deleteContProjectByProjId(String projId){
        BizContProjectDto bizContProjectDto = selectByKey(projId);
        ContProjectHisDto contProjectHisDto = ContProjectHisDto.builder().build();
        BeanUtils.copyBeanProp(contProjectHisDto,bizContProjectDto);
        int insert = contProjectHisDtoMapper.insert(contProjectHisDto);
        int delete = deleteAll(bizContProjectDto);
        if (insert == 1 && delete == 1 ){
            return AppMessage.result("准入项目已删除");
        }else {
            return AppMessage.error("准入项目删除失败！！");
        }
    }

}

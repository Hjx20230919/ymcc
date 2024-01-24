package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContCreditAccessAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.core.exception.AppException;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.*;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.enums.contractor.AcceStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.AttachStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.CreditStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.SetStateEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContCreditMapper;
import cn.com.cnpc.cpoa.po.contractor.ContCreditMaintainPo;
import cn.com.cnpc.cpoa.po.contractor.ContCreditPo;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.contractor.ContCreditMaintainVo;
import cn.com.cnpc.cpoa.vo.contractor.ContCreditVo;
import cn.com.cnpc.cpoa.vo.contractor.data.AttachData;
import cn.com.cnpc.cpoa.vo.contractor.data.ContCreditData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:46
 * @Description:
 */
@Service
public class ContCreditService extends AppService<BizContCreditDto> {

    @Autowired
    BizContCreditMapper bizContCreditMapper;

    @Autowired
    AttachService attachService;


    @Autowired
    ContCreditAttachService contCreditAttachService;

    @Autowired
    ContAccessService contAccessService;

    @Autowired
    ContCreditSetService contCreditSetService;

    @Autowired
    ContContractorService contContractorService;

    @Autowired
    ContCreditTiService contCreditTiService;

    @Autowired
    ContCreditSetItemService contCreditSetItemService;

    @Value("${file.baseurl}")
    private String BASEURL;


    public static final String REASON = "管理员统一发起变更";

    /**
     * 根据准入信息批量更新 资质
     *
     * @param acceId
     * @param creditSate
     */
    public void updateContCreditByAcceId(String acceId, String creditSate) {
        Map<String, Object> params = new HashMap<>();
        params.put("acceId", acceId);
        List<BizContCreditDto> bizContCreditDtos = bizContCreditMapper.selectContCreditDto(params);
        for (BizContCreditDto contCreditDto : bizContCreditDtos) {
            //未使用的资质 不再更新
            if (StringUtils.isNotEmpty(contCreditDto.getCreditName()) && !"无".equals(contCreditDto.getCreditName().trim())) {
                contCreditDto.setCreditState(creditSate);
            }
        }
        updateList(bizContCreditDtos);
    }


    public List<BizContCreditDto> selectContCreditDto(Map<String, Object> params) {

        return bizContCreditMapper.selectContCreditDto(params);
    }


    public List<ContCreditData> getContCreditDatas(Map<String, Object> params) {
        List<BizContCreditDto> bizContCreditDtos = selectContCreditDto(params);
        List<ContCreditData> dataList = new ArrayList();
        for (BizContCreditDto dto : bizContCreditDtos) {
            dataList.add(ContCreditAccessAssembler.convertDtoToData(dto));

        }
        return dataList;
    }

    @Transactional
    public BizContCreditDto addContCredit(ContCreditVo vo) throws Exception {

        BizContCreditDto dto = ContCreditAccessAssembler.convertVoToDto(vo);
        BizContAccessDto accessDto = contAccessService.selectByKey(dto.getAcceId());

        String userId = accessDto.getOwnerId();
        save(dto);
        List<BizAttachDto> attachDtos = attachService.getAttachDtos(accessDto.getAcceId(), FileOwnerTypeEnum.ACCESS.getKey(), vo.getAttachVos());
        List<BizContCreditAttachDto> creditAttachDtos = contCreditAttachService.getAttachDtos(dto.getCreditId(), attachDtos);


        // String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(accessDto.getContId()).getContOrgNo(), dto.getCreditId(), StringUtils.getUuid32());
        attachService.updateContAttachs(attachDtos, userId, contContractorService.selectByKey(accessDto.getContId()).getContOrgNo(), dto.getCreditId());
        contCreditAttachService.saveList(creditAttachDtos);
        return dto;
    }

    @Transactional
    public void updateContCredit(String id, ContCreditVo vo) throws Exception {
        BizContCreditDto dto = ContCreditAccessAssembler.convertVoToDto(vo);
        dto.setCreditId(id);

        BizContAccessDto accessDto = contAccessService.selectByKey(dto.getAcceId());

        //获得传入的附件信息
        List<BizAttachDto> attachDtos = attachService.getAttachDtos(accessDto.getAcceId(), FileOwnerTypeEnum.ACCESS.getKey(), vo.getAttachVos());

        //获得已存在的中间表信息
        Map<String, Object> params = new HashMap<>();
        params.put("creditId", dto.getCreditId());
        List<BizContCreditAttachDto> creditAttachDtos = contCreditAttachService.getCreditAttachDto(params);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getCreditRemoveMap(attachDtos, creditAttachDtos);
        //2 删除附件
        attachService.deleteByMap(removeMap);
        //3 删除中间表
        contCreditAttachService.deleteByMap(removeMap);
        //4 执行更新
        updateNotNull(dto);
        //5 新增附件 返回新增的附件   由于准入填报无用户id 所以存了准入id
        //   String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(accessDto.getContId()).getContOrgNo(), dto.getCreditId(), StringUtils.getUuid32());
        List<BizAttachDto> newAttachDtos = attachService.updateContAttachs(attachDtos, accessDto.getOwnerId(), contContractorService.selectByKey(accessDto.getContId()).getContOrgNo(), dto.getCreditId());
        //6 为新增的附件保存 中间表
        List<BizContCreditAttachDto> newCreditAttachDtos = contCreditAttachService.getAttachDtos(dto.getCreditId(), newAttachDtos);
        contCreditAttachService.saveList(newCreditAttachDtos);

    }

    @Transactional(rollbackFor = Exception.class)
    public void updateContCreditEntity(String id, String createReason, List<ContCreditVo> vos) throws Exception {
        dealContCreditEntity(id, createReason, vos);
    }

    private void dealContCreditEntity(String id, String createReason, List<ContCreditVo> vos) {
        List<BizContCreditDto> dtos = new ArrayList<>();
        for (ContCreditVo vo : vos) {
            BizContCreditDto dto = new BizContCreditDto();
            dto.setCreditId(vo.getCreditId());
            dtos.add(dto);
        }
        contCreditSetService.initContCreditSet(id, createReason, dtos);
    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteChain(String id) {
        //1 删除主表
        delete(id);

        //2 关联删除
        Map<String, Object> params = new HashMap<>();
        params.put("creditId", id);
        List<BizContCreditAttachDto> creditAttachDtos = contCreditAttachService.getCreditAttachDto(params);
        for (BizContCreditAttachDto contCreditAttachDto : creditAttachDtos) {
            contCreditAttachService.delete(contCreditAttachDto.getId());
            attachService.deleteById(contCreditAttachDto.getAttachId(),"");
        }
    }


    /**
     * 查询资质--所有附件
     *
     * @param params
     * @return
     */
    public List<ContCreditVo> selectContCreditVo(Map<String, Object> params) {
        List<ContCreditVo> vos = new ArrayList<>();
        List<BizContCreditDto> dtos = bizContCreditMapper.selectContCreditDto(params);
        for (BizContCreditDto dto : dtos) {
            ContCreditVo contCreditVo = ContCreditAccessAssembler.convertDtoToVo(dto);
            Map<String, Object> param = new HashMap<>();
            param.put("creditId", dto.getCreditId());
            List<AttachData> attachDatas = attachService.getCreditAttachByCreditId(param);
            List<AttachVo> attachVos = new ArrayList<>();
            for (AttachData data : attachDatas) {
                AttachVo vo = new AttachVo();
                BeanUtils.copyBeanProp(vo, data);
                attachVos.add(vo);
            }
            contCreditVo.setAttachVos(attachVos);
            vos.add(contCreditVo);
        }
        return vos;
    }


    public List<ContCreditMaintainVo> selectContCreditMaintain(Map<String, Object> params) {
        List<ContCreditMaintainVo> vos = new ArrayList();
        List<ContCreditMaintainPo> pos = bizContCreditMapper.selectContCreditMaintain(params);
        for (ContCreditMaintainPo po : pos) {
            if (StringUtils.isNotEmpty(po.getContent()) && StringUtils.isNotEmpty(po.getProjContent())) {
                po.setContent(contContractorService.setDeleteContent(Arrays.asList(po.getContent().split(",")), Arrays.asList(po.getProjContent().split(","))));
            }
            vos.add(ContCreditAccessAssembler.convertPoToVo(po));
        }
        return vos;
    }

    @Transactional
    public BizContCreditDto addContSetCredit(ContCreditVo vo) throws Exception {

        BizContCreditDto dto = ContCreditAccessAssembler.convertVoToDto(vo);
        BizContAccessDto accessDto = contAccessService.selectByKey(dto.getAcceId());

        String userId = accessDto.getOwnerId();
        //导入的承包商无owner信息
        if(StringUtils.isEmpty(userId)){
            userId="uploadUser";
        }
        save(dto);

        Map<String, Object> param = new HashMap<>();
        param.put("accessId", dto.getAcceId());
        List<BizContCreditSetDto> contCreditSetDtos = contCreditSetService.selectContCreditSet(param);

        List<BizAttachDto> attachDtos = attachService.getAttachDtos(accessDto.getAcceId(), FileOwnerTypeEnum.ACCESS.getKey(), vo.getAttachVos());

        for(BizAttachDto attachDto:attachDtos){
            attachDto.setSetId(contCreditSetDtos.get(0).getSetId());
        }

        List<BizContCreditAttachDto> creditAttachDtos = contCreditAttachService.getAttachDtos(dto.getCreditId(), attachDtos);

        //给资质添加资质中间项
        List<BizContCreditDto> dtos = new ArrayList<>();
        dtos.add(dto);

        List<BizContCreditSetItemDto> contCreditSetItemDtos = contCreditSetItemService.getCreditItems(contCreditSetDtos.get(0).getSetId(), dtos);
        contCreditSetItemService.saveList(contCreditSetItemDtos);



        // String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(accessDto.getContId()).getContOrgNo(), dto.getCreditId(), StringUtils.getUuid32());
        attachService.updateContAttachs(attachDtos, userId, contContractorService.selectByKey(accessDto.getContId()).getContOrgNo(), dto.getCreditId());
        contCreditAttachService.saveList(creditAttachDtos);
        return dto;
    }


    @Transactional(rollbackFor = Exception.class)
    public void updateContSetCredit(String id, ContCreditVo vo) throws Exception {

        BizContCreditDto dto = ContCreditAccessAssembler.convertVoToDto(vo);
        dto.setCreditId(id);

        Map<String, Object> param = new HashMap<>();
        param.put("accessId", dto.getAcceId());
        List<BizContCreditSetDto> contCreditSetDtos = contCreditSetService.selectContCreditSet(param);
        String setId = contCreditSetDtos.get(0).getSetId();

        BizContAccessDto accessDto = contAccessService.selectByKey(dto.getAcceId());

        //获得传入的附件信息
        List<BizAttachDto> attachDtos = attachService.getAttachDtos(accessDto.getAcceId(), FileOwnerTypeEnum.ACCESS.getKey(), vo.getAttachVos());

        //获得已存在的中间表信息
        Map<String, Object> params = new HashMap<>();
        params.put("creditId", dto.getCreditId());
        List<BizContCreditAttachDto> creditAttachDtos = contCreditAttachService.getCreditAttachDto(params);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getCreditRemoveMap(attachDtos, creditAttachDtos);
        //2 删除附件
        attachService.deleteByMap(removeMap);
        //3 删除中间表
        contCreditAttachService.deleteByMap(removeMap);
        //4 执行更新
        updateNotNull(dto);
        //5 新增附件 返回新增的附件   由于准入填报无用户id 所以存了准入id
//        String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(accessDto.getContId()).getContOrgNo(), dto.getCreditId(), StringUtils.getUuid32());

        String ownerId = accessDto.getOwnerId();
        //导入的承包商无owner信息
        if(StringUtils.isEmpty(ownerId)){
            ownerId="uploadUser";
        }
        List<BizAttachDto> newAttachDtos = attachService.updateContAttachs(attachDtos, ownerId, contContractorService.selectByKey(accessDto.getContId()).getContOrgNo(), dto.getCreditId());

        for (BizAttachDto attachDto : newAttachDtos) {
            attachDto.setSetId(setId);
        }
        //6 为新增的附件保存 中间表
        List<BizContCreditAttachDto> newCreditAttachDtos = contCreditAttachService.getAttachDtos(dto.getCreditId(), newAttachDtos);
        contCreditAttachService.saveList(newCreditAttachDtos);

    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteChainSet(String id) {
        //1 删除主表
        delete(id);

        //2 关联删除
        Map<String, Object> params = new HashMap<>();
        params.put("creditId", id);
        List<BizContCreditAttachDto> creditAttachDtos = contCreditAttachService.getCreditAttachDto(params);
        for (BizContCreditAttachDto contCreditAttachDto : creditAttachDtos) {
            contCreditAttachService.delete(contCreditAttachDto.getId());
            attachService.deleteById(contCreditAttachDto.getAttachId(),"");
        }

        //3 删除中间项
        List<BizContCreditSetItemDto> contCreditSetItemDtos = contCreditSetItemService.selectContCreditSetItem(params);
        for (BizContCreditSetItemDto contCreditSetItemDto : contCreditSetItemDtos) {
            contCreditSetItemService.delete(contCreditSetItemDto.getItemId());
        }
    }


    public List<ContCreditVo> selectContCreditByAcceId(Map<String, Object> params) {
        List<ContCreditVo> vos = new ArrayList<>();
        List<ContCreditPo> contCreditPos = bizContCreditMapper.selectContCreditByAcceId(params);

        for (ContCreditPo po : contCreditPos) {
            ContCreditVo contCreditVo = ContCreditAccessAssembler.convertPoToVo(po);
            Map<String, Object> param = new HashMap<>();
            param.put("creditId", po.getCreditId());
            //若没有变更则只查询 在有效的
            if (StringUtils.isEmpty(po.getSetId())) {
                param.put("attachState", AttachStateEnum.INUSE.getKey());
            } else {
                //查询当前变更id下所有的
                param.put("setId", po.getSetId());
            }
            List<AttachData> attachDatas = attachService.getCreditAttachByCreditId(param);
            List<AttachVo> attachVos = new ArrayList<>();
            for (AttachData data : attachDatas) {
                AttachVo vo = new AttachVo();
                BeanUtils.copyBeanProp(vo, data);
                attachVos.add(vo);
            }
            contCreditVo.setAttachVos(attachVos);
            vos.add(contCreditVo);
        }
        return vos;
    }


    public List<ContCreditMaintainVo> selectOverdueCredit(Map<String, Object> params) {
        List<ContCreditMaintainVo> vos = new ArrayList();
        List<ContCreditMaintainPo> pos = bizContCreditMapper.selectOverdueCredit(params);
        for (ContCreditMaintainPo po : pos) {
            vos.add(ContCreditAccessAssembler.convertPoToVo(po));
        }
        return vos;
    }


    public void getDataFromCreditTi(Map<String, Object> params, List<ContCreditVo> vos) {
        String acceId = (String) params.get("acceId");
        BizContAccessDto accessDto = contAccessService.selectByKey(acceId);
        if (!AcceStateEnum.FILLIN.getKey().equals(accessDto.getAcceState())) {
            return;
        }
        List<BizContCreditTiDto> contCreditTiDtos = contCreditTiService.selectContCreditDto(params);
        for (BizContCreditTiDto contCreditTiDto : contCreditTiDtos) {
            ContCreditVo contCreditVo = new ContCreditVo();
            contCreditVo.setCreditProjName(contCreditTiDto.getItemProjName());
            contCreditVo.setIsMust(contCreditTiDto.getItemMust());
            contCreditVo.setCreditNo(contCreditTiDto.getItemNo());
            contCreditVo.setItemId(contCreditTiDto.getItemId());
            vos.add(contCreditVo);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateContCreditInvalid(List<ContCreditVo> contCreditVos) {
        List<BizContCreditDto> dtos = new ArrayList<>();
        for (ContCreditVo vo : contCreditVos) {
            BizContCreditDto dto = new BizContCreditDto();
            dto.setCreditId(vo.getCreditId());
            dto.setCreditState(CreditStateEnum.INVALID.getKey());
            dtos.add(dto);
        }
        updateList(dtos);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateContCreditEntitys(List<String> accessIds) {
        //1 按照准入查询所有资质-排除资质名称creditName为空的值
        Map<String, Object> params = new HashMap<>();
        params.put("acceIds", accessIds);
        List<BizContCreditDto> contCreditDtos = bizContCreditMapper.selectContCreditDto(params);


        List<BizContCreditSetDto> dtos = contCreditSetService.selectContCreditSet(params);
        for (BizContCreditSetDto contCreditSetDto : dtos) {
            if (SetStateEnum.FILLIN.getKey().equals(contCreditSetDto.getSetState())
                    || SetStateEnum.AUDITING.getKey().equals(contCreditSetDto.getSetState())
                    || SetStateEnum.BACK.getKey().equals(contCreditSetDto.getSetState())
                    || SetStateEnum.INVALID.getKey().equals(contCreditSetDto.getSetState())) {
                throw new AppException("您选择的要更新的准入中有正处于流程中的变更，请取消");
            }
        }

        //排除掉资质名称为空的 既是没有使用的模板
        List<ContCreditVo> contCreditVos = new ArrayList<>();
        for (BizContCreditDto contCreditDto : contCreditDtos) {
            //if(StringUtils.isNotEmpty(contCreditDto.getCreditName())&&!"无".equals(contCreditDto.getCreditName().trim())){
            contCreditVos.add(ContCreditAccessAssembler.convertDtoToVo(contCreditDto));
            //}
        }


        Map<String, List<ContCreditVo>> contCreditMap = new HashMap<>();


        //遍历 向map中添加元素
        for (ContCreditVo contCreditVo : contCreditVos) {
            List<ContCreditVo> creditVos = contCreditMap.get(contCreditVo.getAcceId());
            if (StringUtils.isNotEmpty(creditVos)) {
                creditVos.add(contCreditVo);
            } else {
                creditVos = new ArrayList<>();
                creditVos.add(contCreditVo);
                contCreditMap.put(contCreditVo.getAcceId(), creditVos);
            }
        }

//        if(accessIds.size()!=contCreditMap.size()){
//            throw new AppException("您选择的要更新的准入中有资质都为空的，请取消");
//        }

        //遍历 向map中添加元素
        for (String accessId : accessIds) {
            List<ContCreditVo> contCreditVos1 = contCreditMap.get(accessId);
            //为空就意味着是导入的
            if (StringUtils.isEmpty(contCreditVos1)) {
                contCreditVos1 = new ArrayList<>();
                Map<String, Object> param2s = new HashMap<>();
                param2s.put("acceId", accessId);
                List<BizContCreditDto> dataFromCreditTi = contCreditTiService.getDataFromCreditTi(param2s);
                for (BizContCreditDto contCreditDto : dataFromCreditTi) {
                    contCreditVos1.add(ContCreditAccessAssembler.convertDtoToVo(contCreditDto));
                }
            }
            dealContCreditEntity(accessId, REASON, contCreditVos1);
        }

    }
}

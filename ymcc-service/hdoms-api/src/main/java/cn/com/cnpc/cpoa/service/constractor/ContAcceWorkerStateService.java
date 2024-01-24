package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContAcceWorkerStateAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerStateAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerStateDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.mapper.BizContAcceWorkerStateDtoMapper;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceWorkerStateVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/11 21:34
 * @Description:
 */
@Service
public class ContAcceWorkerStateService extends AppService<BizContAcceWorkerStateDto> {

    @Autowired
    BizContAcceWorkerStateDtoMapper bizContAcceWorkerStateDtoMapper;

    @Autowired
    ContAccessService contAccessService;

    @Autowired
    AttachService attachService;

    @Autowired
    ContAcceWorkerStateAttachService contAcceWorkerStateAttachService;

    @Autowired
    ContContractorService contContractorService;


    @Transactional(rollbackFor = Exception.class)
    public void dealContAcceWorkerState(String accessId, List<ContAcceWorkerStateVo> contAcceWorkerStateVos) throws Exception {

        Map<String, Object> params = new HashMap<>();
        params.put("acceId", accessId);
        List<BizContAcceWorkerStateDto> bizContAcceWorkerStateDtos = bizContAcceWorkerStateDtoMapper.selectContAcceWorkerStateDto(params);
        List<String> exitsKeys=new ArrayList<>();
        for (BizContAcceWorkerStateDto contAcceWorkerStateDto:bizContAcceWorkerStateDtos) {
            exitsKeys.add(contAcceWorkerStateDto.getStateId());
        }

        List<String> newKeys=new ArrayList<>();
        for (ContAcceWorkerStateVo contAcceWorkerStateVo:contAcceWorkerStateVos) {
            if(StringUtils.isNotEmpty(contAcceWorkerStateVo.getStateId())){
                newKeys.add(contAcceWorkerStateVo.getStateId());
            }
        }

        exitsKeys.removeAll(newKeys);

        for (String key:exitsKeys) {
            deleteChain(key);
        }




        for (ContAcceWorkerStateVo workerStateVo : contAcceWorkerStateVos) {
            //1 不存在就新增
            if (StringUtils.isEmpty(workerStateVo.getStateId())) {
                addContAcceWorkerState(accessId, workerStateVo);
            } else {
                //2 存在就更新
                updateContAcceWorkerState(accessId, workerStateVo);
            }
        }

    }

    private void updateContAcceWorkerState(String accessId, ContAcceWorkerStateVo workerStateVo) throws Exception {
        //数据准备
        BizContAccessDto contAccessDto = contAccessService.selectByKey(accessId);
        BizContAcceWorkerStateDto dto = ContAcceWorkerStateAssembler.convertVoToDto(workerStateVo);

        // 获取附件实体
        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(workerStateVo.getStateId(), FileOwnerTypeEnum.ACCESS.getKey(), workerStateVo.getAttachVos());

        //获得已存在的中间表信息
        Map<String, Object> param = new HashMap<>();
        param.put("stateId", workerStateVo.getStateId());
        List<BizContAcceWorkerStateAttachDto> workerStateAttachDtos = contAcceWorkerStateAttachService.selectContAcceWorkerStateAttach(param);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getStateRemoveMap(attachDtos, workerStateAttachDtos);
        //2 删除附件
        attachService.deleteByMap(removeMap);
        //3 删除中间表
        contAcceWorkerStateAttachService.deleteByMap(removeMap);
        //4 执行更新
        updateNotNull(dto);
        //5 新增附件 返回新增的附件
        String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(contAccessDto.getContId()).getContOrgNo(), accessId, StringUtils.getUuid32());
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, contAccessDto.getOwnerId(), contToFileUri);
        //6 为新增的附件保存 中间表
        List<BizContAcceWorkerStateAttachDto> newWorkerStateAttachs = contAcceWorkerStateAttachService.getContAcceWorkerStateAttachs(dto.getStateId(), newAttachDtos);
        contAcceWorkerStateAttachService.saveList(newWorkerStateAttachs);

    }

    /**
     * 新增持证情况
     *
     * @param accessId
     * @param workerStateVo
     */
    private void addContAcceWorkerState(String accessId, ContAcceWorkerStateVo workerStateVo) throws Exception {

        //数据准备
        BizContAccessDto contAccessDto = contAccessService.selectByKey(accessId);
        BizContAcceWorkerStateDto dto = ContAcceWorkerStateAssembler.convertVoToDto(workerStateVo);
        dto.setStateId(StringUtils.getUuid32());

        save(dto);

        // 获取附件实体
        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(dto.getStateId(), FileOwnerTypeEnum.ACCESS.getKey(), workerStateVo.getAttachVos());

        //获得已存在的中间表信息
        List<BizContAcceWorkerStateAttachDto> newWorkerStateAttachs = contAcceWorkerStateAttachService.getContAcceWorkerStateAttachs(dto.getStateId(), attachDtos);

        //保存附件
        String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(contAccessDto.getContId()).getContOrgNo(), accessId, StringUtils.getUuid32());
        attachService.updateAttachs(attachDtos, contAccessDto.getOwnerId(), contToFileUri);
        //保存中間表
        contAcceWorkerStateAttachService.saveList(newWorkerStateAttachs);

    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteChain(String stateId) {
        Map<String, Object> param = new HashMap<>();
        param.put("stateId", stateId);
        List<BizContAcceWorkerStateAttachDto> workerStateAttachDtos = contAcceWorkerStateAttachService.selectContAcceWorkerStateAttach(param);

        for (BizContAcceWorkerStateAttachDto workerStateAttachDto : workerStateAttachDtos) {
            String attachId = workerStateAttachDto.getAttachId();
            String id = workerStateAttachDto.getId();
            //删除文件
            attachService.deleteById(attachId,"");
            //删除中间表
            contAcceWorkerStateAttachService.delete(id);
        }

        // 删除实体
        delete(stateId);
    }

    public List<ContAcceWorkerStateVo> selectContAcceWorkerState(Map<String, Object> params) {
        List<ContAcceWorkerStateVo> contAcceWorkerStateVos = new ArrayList<>();

        List<BizContAcceWorkerStateDto> bizContAcceWorkerStateDtos = bizContAcceWorkerStateDtoMapper.selectContAcceWorkerStateDto(params);
        for (BizContAcceWorkerStateDto contAcceWorkerStateDto : bizContAcceWorkerStateDtos) {

            ContAcceWorkerStateVo contAcceWorkerStateVo = ContAcceWorkerStateAssembler.convertDtoToVo(contAcceWorkerStateDto);

            // 查询附件
            Map<String, Object> param = new HashMap<>();
            param.put("objId", contAcceWorkerStateVo.getStateId());
            List<AttachVo> attachVos = new ArrayList<>();
            List<BizAttachDto> attachDtos = attachService.selectListByObjId(param);
            for (BizAttachDto attachDto : attachDtos) {
                AttachVo attachVo = new AttachVo();
                BeanUtils.copyBeanProp(attachVo, attachDto);
                attachVos.add(attachVo);
            }
            contAcceWorkerStateVo.setAttachVos(attachVos);
            contAcceWorkerStateVos.add(contAcceWorkerStateVo);

        }

        return contAcceWorkerStateVos;
    }

}

package cn.com.cnpc.cpoa.service.constractor;

import cn.com.cnpc.cpoa.assembler.ContAcceAchievementAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.BizAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceAchievementAttachDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceAchievementDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.enums.FileOwnerTypeEnum;
import cn.com.cnpc.cpoa.mapper.contractor.BizContAcceAchievementDtoMapper;
import cn.com.cnpc.cpoa.service.AttachService;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.AttachVo;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceAchievementVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2020/5/5 12:05
 * @Description:
 */
@Service
public class ContAcceAchievementService extends AppService<BizContAcceAchievementDto> {


    @Autowired
    ContAccessService contAccessService;

    @Autowired
    AttachService attachService;

    @Autowired
    ContAcceAchievementAttachService contAcceAchievementAttachService;

    @Autowired
    ContContractorService contContractorService;


    @Autowired
    BizContAcceAchievementDtoMapper bizContAcceAchievementDtoMapper;


    @Transactional(rollbackFor = Exception.class)
    public void dealContAcceAchievement(String accessId, List<ContAcceAchievementVo> contAcceAchievementVos) {

        Map<String, Object> params = new HashMap<>();
        params.put("acceId", accessId);
        List<BizContAcceAchievementDto> bizContAcceAchievementDtos = bizContAcceAchievementDtoMapper.selectContAcceAchievement(params);
        List<String> exitsKeys=new ArrayList<>();
        for (BizContAcceAchievementDto contAcceAchievementDto:bizContAcceAchievementDtos) {
            exitsKeys.add(contAcceAchievementDto.getAchId());
        }

        List<String> newKeys=new ArrayList<>();
        for (ContAcceAchievementVo contAcceAchievementVo:contAcceAchievementVos) {
            if(StringUtils.isNotEmpty(contAcceAchievementVo.getAchId())){
                newKeys.add(contAcceAchievementVo.getAchId());
            }
        }

        exitsKeys.removeAll(newKeys);

        for (String key:exitsKeys) {
            deleteChain(key);
        }


        for (ContAcceAchievementVo achievementVo : contAcceAchievementVos) {
            //1 不存在就新增
            if (StringUtils.isEmpty(achievementVo.getAchId())) {
                addContAcceAchievement(accessId, achievementVo);
            } else {
                //2 存在就更新
                updateContAcceAchievement(accessId, achievementVo);
            }
        }

    }

    private void updateContAcceAchievement(String accessId, ContAcceAchievementVo achievementVo) {

        //数据准备
        BizContAccessDto contAccessDto = contAccessService.selectByKey(accessId);

        BizContAcceAchievementDto achievementDto = ContAcceAchievementAssembler.convertVoToDto(achievementVo);

        // 获取附件实体
        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(achievementVo.getAchId(), FileOwnerTypeEnum.ACCESS.getKey(), achievementVo.getAttachVos());

        //获得已存在的中间表信息
        Map<String, Object> param = new HashMap<>();
        param.put("achId", achievementVo.getAchId());
        List<BizContAcceAchievementAttachDto> acceAchievementAttachDtos = contAcceAchievementAttachService.selectContAcceAchievementAttach(param);
        //1获取要删除的附件信息
        Map<String, String> removeMap = attachService.getAchievementRemoveMap(attachDtos, acceAchievementAttachDtos);
        //2 删除附件
        attachService.deleteByMap(removeMap);
        //3 删除中间表
        contAcceAchievementAttachService.deleteByMap(removeMap);
        //4 执行更新
        updateNotNull(achievementDto);
        //5 新增附件 返回新增的附件
        String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(contAccessDto.getContId()).getContOrgNo(), accessId, StringUtils.getUuid32());
        List<BizAttachDto> newAttachDtos = attachService.updateAttachs(attachDtos, contAccessDto.getOwnerId(), contToFileUri);
        //6 为新增的附件保存 中间表
        List<BizContAcceAchievementAttachDto> newAcceAchievementAttachs = contAcceAchievementAttachService.getContAcceAchievementAttachs(achievementDto.getAchId(), newAttachDtos);
        contAcceAchievementAttachService.saveList(newAcceAchievementAttachs);

    }

    private void addContAcceAchievement(String accessId, ContAcceAchievementVo achievementVo) {
        //数据准备
        BizContAccessDto contAccessDto = contAccessService.selectByKey(accessId);
        BizContAcceAchievementDto achievementDto = ContAcceAchievementAssembler.convertVoToDto(achievementVo);
        achievementDto.setAchId(StringUtils.getUuid32());

        save(achievementDto);

        // 获取附件实体
        List<BizAttachDto> attachDtos = attachService.getNoRepeatAttachDtos(achievementDto.getAchId(), FileOwnerTypeEnum.ACCESS.getKey(), achievementVo.getAttachVos());

        //获得已存在的中间表信息
        List<BizContAcceAchievementAttachDto> newAcceAchievementAttachs = contAcceAchievementAttachService.getContAcceAchievementAttachs(achievementDto.getAchId(), attachDtos);

        //保存附件
        String contToFileUri = attachService.getContToFileUri(contContractorService.selectByKey(contAccessDto.getContId()).getContOrgNo(), accessId, StringUtils.getUuid32());
        attachService.updateAttachs(attachDtos, contAccessDto.getOwnerId(), contToFileUri);
        //保存中間表
        contAcceAchievementAttachService.saveList(newAcceAchievementAttachs);

    }


    @Transactional(rollbackFor = Exception.class)
    public void deleteChain(String achId) {
        Map<String, Object> param = new HashMap<>();
        param.put("achId", achId);
        List<BizContAcceAchievementAttachDto> acceAchievementAttachDtos = contAcceAchievementAttachService.selectContAcceAchievementAttach(param);

        for (BizContAcceAchievementAttachDto acceAchievementAttachDto : acceAchievementAttachDtos) {
            String attachId = acceAchievementAttachDto.getAttachId();
            String id = acceAchievementAttachDto.getId();
            //删除文件
            attachService.deleteById(attachId,"");
            //删除中间表
            contAcceAchievementAttachService.delete(id);
        }

        // 删除实体
        delete(achId);
    }

    public List<ContAcceAchievementVo> selectContAcceAchievement(Map<String, Object> params) {
        List<ContAcceAchievementVo> contAcceAchievementVos = new ArrayList<>();

        List<BizContAcceAchievementDto> bizContAcceAchievementDtos = bizContAcceAchievementDtoMapper.selectContAcceAchievement(params);
        for (BizContAcceAchievementDto contAcceAchievementDto : bizContAcceAchievementDtos) {

            ContAcceAchievementVo contAcceAchievementVo = ContAcceAchievementAssembler.convertDtoToVo(contAcceAchievementDto);

            // 查询附件
            Map<String, Object> param = new HashMap<>();
            param.put("objId", contAcceAchievementVo.getAchId());
            List<AttachVo> attachVos = new ArrayList<>();
            List<BizAttachDto> attachDtos = attachService.selectListByObjId(param);
            for (BizAttachDto attachDto : attachDtos) {
                AttachVo attachVo = new AttachVo();
                BeanUtils.copyBeanProp(attachVo, attachDto);
                attachVos.add(attachVo);
            }
            contAcceAchievementVo.setAttachVos(attachVos);
            contAcceAchievementVos.add(contAcceAchievementVo);

        }

        return contAcceAchievementVos;
    }
}

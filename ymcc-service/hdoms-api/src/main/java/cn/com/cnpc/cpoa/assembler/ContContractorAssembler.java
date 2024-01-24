package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.enums.contractor.ContFreezeStateEnum;
import cn.com.cnpc.cpoa.enums.contractor.ContractorStateEnum;
import cn.com.cnpc.cpoa.po.contractor.ContContractorPo;
import cn.com.cnpc.cpoa.po.contractor.ContManageQueryPo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContContractorVo;
import cn.com.cnpc.cpoa.vo.contractor.ContManageQueryVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContContractorData;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 20:44
 * @Description:承包商装配
 */
public class ContContractorAssembler {



    public static BizContContractorDto getInitContContractor(BizContProjectDto contProjectDto){
        BizContContractorDto contContractorDto=new BizContContractorDto();
        contContractorDto.setContId(StringUtils.getUuid32());
        contContractorDto.setContName(contProjectDto.getProjContName());
        contContractorDto.setContOrgNo(contProjectDto.getProjContCode());
        contContractorDto.setLinkman(contProjectDto.getProjContLinkman());
        contContractorDto.setLinkMail(contProjectDto.getProjContMail());
        contContractorDto.setLinkPhone(contProjectDto.getProjContPhone());
        contContractorDto.setContFreezeState(ContFreezeStateEnum.USING.getKey());
        contContractorDto.setContState(ContractorStateEnum.FILLIN.getKey());
        contContractorDto.setContStateAt(DateUtils.getNowDate());
        contContractorDto.setCreateAt(DateUtils.getNowDate());
        return contContractorDto;

    }

    public static ContContractorVo convertPoToVo(ContContractorPo po){
        ContContractorVo vo=new ContContractorVo();
        BeanUtils.copyBeanProp(vo,po);
        vo.setSetupTime(DateUtils.dateTimeYYYY_MM_DD(po.getSetupTime()));
        vo.setCheckAt(DateUtils.dateTimeYYYY_MM_DD(po.getCheckAt()));
        vo.setContStateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getContStateAt()));
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getCreateAt()));
        return vo;
    }

    public static ContContractorData convertPoToData(BizContContractorDto dto){
        ContContractorData data=new ContContractorData();
        BeanUtils.copyBeanProp(data,dto);
        data.setSetupTime(DateUtils.dateTimeYYYY_MM_DD(dto.getSetupTime()));
        data.setContStateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getContStateAt()));
        data.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getCreateAt()));
        return data;
    }

    public static BizContContractorDto convertVoToDto(ContContractorVo vo) throws Exception{
        BizContContractorDto dto=new BizContContractorDto();
        BeanUtils.copyBeanProp(dto,vo);
        dto.setSetupTime(DateUtils.parseDate(vo.getSetupTime()));
//        dto.setContStateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getContStateAt()));
//        dto.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getCreateAt()));
        return dto;
    }

    public static ContManageQueryVo convertQueryPoToVo(ContManageQueryPo po){
        ContManageQueryVo vo=new ContManageQueryVo();
        BeanUtils.copyBeanProp(vo,po);
        vo.setAcessDate(DateUtils.dateTimeYYYY_MM_DD(null!=po.getAcceStateAt()?po.getAcceStateAt():po.getAcessDate()));
        vo.setCheckAt(DateUtils.dateTimeYYYY_MM_DD(po.getCheckAt()));
        //获取上次审核时间
        vo.setPreCheckAt(DateUtils.dateTimeYYYY_MM_DD(DateUtils.getPreYear(po.getCheckAt())));
        vo.setAccessLevel(StringUtils.isEmpty(po.getAccessLevel()) ? ContractorConstant.AccessLevel.accessLevelMap.get(po.getProjAccessType()) : po.getAccessLevel());
        return vo;
    }

}

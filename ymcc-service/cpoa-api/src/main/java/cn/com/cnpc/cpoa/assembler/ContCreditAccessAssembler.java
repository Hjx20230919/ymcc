package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.contractor.BizContCreditDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContCreditTiDto;
import cn.com.cnpc.cpoa.enums.contractor.CreditStateEnum;
import cn.com.cnpc.cpoa.po.contractor.ContCreditMaintainPo;
import cn.com.cnpc.cpoa.po.contractor.ContCreditPo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContCreditMaintainVo;
import cn.com.cnpc.cpoa.vo.contractor.ContCreditVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContCreditData;

/**
 * @Author: 17742856263
 * @Date: 2019/10/15 19:13
 * @Description:
 */
public class ContCreditAccessAssembler {

    public static BizContCreditDto getInitCredit(BizContCreditTiDto contCreditTiDto, String acceId){
        BizContCreditDto contCreditDto=new BizContCreditDto();
        contCreditDto.setCreditId(StringUtils.getUuid32());
        contCreditDto.setCreditProjName(contCreditTiDto.getItemProjName());
        contCreditDto.setAcceId(acceId);
        contCreditDto.setCreditNo(contCreditTiDto.getItemNo());
        contCreditDto.setItemId(contCreditTiDto.getItemId());
        contCreditDto.setCreditState(CreditStateEnum.NOTVALID.getKey());
        return contCreditDto;
    }

    public static ContCreditData convertDtoToData(BizContCreditDto dto){
        ContCreditData data=new ContCreditData();
        BeanUtils.copyBeanProp(data,dto);
        data.setCreditValidStart(DateUtils.dateTimeYYYY_MM_DD(dto.getCreditValidStart()));
        data.setCreditValidEnd(DateUtils.dateTimeYYYY_MM_DD(dto.getCreditValidEnd()));
        return data;
    }

    public static ContCreditData convertPoToData(ContCreditPo po){
        ContCreditData data=new ContCreditData();
        BeanUtils.copyBeanProp(data,po);
        data.setCreditValidStart(DateUtils.dateTimeYYYY_MM_DD(po.getCreditValidStart()));
        data.setCreditValidEnd(DateUtils.dateTimeYYYY_MM_DD(po.getCreditValidEnd()));
        return data;
    }

    public static BizContCreditDto convertVoToDto(ContCreditVo vo)throws Exception{
        BizContCreditDto dto=new BizContCreditDto();
        BeanUtils.copyBeanProp(dto,vo);
        dto.setCreditId(StringUtils.getUuid32());
        dto.setCreditValidStart(DateUtils.parseDate(vo.getCreditValidStart()));
        dto.setCreditValidEnd(DateUtils.parseDate(vo.getCreditValidEnd()));
        return dto;
    }

    public static ContCreditVo convertDtoToVo(BizContCreditDto dto){
        ContCreditVo vo=new ContCreditVo();
        BeanUtils.copyBeanProp(vo,dto);
        vo.setCreditValidStart(DateUtils.dateTimeYYYY_MM_DD(dto.getCreditValidStart()));
        vo.setCreditValidEnd(DateUtils.dateTimeYYYY_MM_DD(dto.getCreditValidEnd()));
        return vo;
    }

    public static ContCreditVo convertPoToVo(ContCreditPo po){
        ContCreditVo vo=new ContCreditVo();
        BeanUtils.copyBeanProp(vo,po);
        vo.setCreditValidStart(DateUtils.dateTimeYYYY_MM_DD(po.getCreditValidStart()));
        vo.setCreditValidEnd(DateUtils.dateTimeYYYY_MM_DD(po.getCreditValidEnd()));
        return vo;
    }

    public static ContCreditMaintainVo convertPoToVo(ContCreditMaintainPo po){
        ContCreditMaintainVo vo=new ContCreditMaintainVo();
        BeanUtils.copyBeanProp(vo,po);
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getCreateAt()));
        vo.setProjAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getProjAt()));
        vo.setProjStateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getProjStateAt()));
        if(StringUtils.isNotEmpty(vo.getSetCode())){
            vo.setSetFillTime(DateUtils.dateTimeYYYY_MM_DD(po.getSetFillTime()));
            vo.setSetUrl(po.getAcceId());
        }
        return vo;
    }



}

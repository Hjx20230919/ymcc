package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.contractor.BizContLogDto;
import cn.com.cnpc.cpoa.po.contractor.ContLogPo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContLogVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContLogData;

/**
 * @Author: 17742856263
 * @Date: 2019/10/19 8:58
 * @Description:
 */
public class ContLogAssembler {

    public static BizContLogDto convertVoToDto(ContLogVo vo){
        BizContLogDto dto=new BizContLogDto();
        BeanUtils.copyBeanProp(dto,vo);
      //  dto.setLogObjId(StringUtils.getUuid32());
        dto.setLogId(StringUtils.getUuid32());
        dto.setLogTime(DateUtils.getNowDate());
        return dto;
    }

    public static ContLogData convertDtoData(BizContLogDto dto)  {
        ContLogData data=new ContLogData();
        BeanUtils.copyBeanProp(data,dto);
        data.setLogObjId(StringUtils.getUuid32());
        data.setLogTime(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getLogTime()));
        return data;
    }

    public static ContLogVo convertDtoToVo(BizContLogDto dto)  {
        ContLogVo vo=new ContLogVo();
        BeanUtils.copyBeanProp(vo,dto);
        vo.setLogObjId(StringUtils.getUuid32());
        vo.setLogTime(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getLogTime()));
        return vo;
    }

    public static ContLogVo convertPoToVo(ContLogPo po)  {
        ContLogVo vo=new ContLogVo();
        BeanUtils.copyBeanProp(vo,po);
        vo.setLogObjId(StringUtils.getUuid32());
        vo.setLogTime(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getLogTime()));
        return vo;
    }
}

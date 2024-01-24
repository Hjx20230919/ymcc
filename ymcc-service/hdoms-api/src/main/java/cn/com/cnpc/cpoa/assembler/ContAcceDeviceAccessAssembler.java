package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.contractor.BizContAcceDeviceDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceDeviceVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContAcceDeviceData;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 21:03
 * @Description:
 */
public class ContAcceDeviceAccessAssembler {

    public static ContAcceDeviceData convertDtoToData(BizContAcceDeviceDto dto){
        ContAcceDeviceData data=new ContAcceDeviceData();
        BeanUtils.copyBeanProp(data,dto);
        return data;
    }

    public static BizContAcceDeviceDto convertVoToDto(ContAcceDeviceVo vo){
        BizContAcceDeviceDto dto=new BizContAcceDeviceDto();
        BeanUtils.copyBeanProp(dto,vo);
        return dto;
    }

    public static ContAcceDeviceVo convertDtoToVo(BizContAcceDeviceDto dto){
        ContAcceDeviceVo vo=new ContAcceDeviceVo();
        BeanUtils.copyBeanProp(vo,dto);
        return vo;
    }

}

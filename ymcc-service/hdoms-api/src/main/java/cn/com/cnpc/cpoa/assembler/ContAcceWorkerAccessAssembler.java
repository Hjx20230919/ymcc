package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.contractor.BizContAcceWorkerDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceWorkerVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContAcceWorkerData;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 21:03
 * @Description:
 */
public class ContAcceWorkerAccessAssembler {


    public static ContAcceWorkerData convertPoToData(BizContAcceWorkerDto dto){
        ContAcceWorkerData data=new ContAcceWorkerData();
        BeanUtils.copyBeanProp(data,dto);
        return data;
    }


    public static BizContAcceWorkerDto convertVoToDto(ContAcceWorkerVo vo){
        BizContAcceWorkerDto dto=new BizContAcceWorkerDto();
        BeanUtils.copyBeanProp(dto,vo);
        return dto;
    }

    public static ContAcceWorkerVo convertDtoToVo(BizContAcceWorkerDto dto){
        ContAcceWorkerVo vo=new ContAcceWorkerVo();
        BeanUtils.copyBeanProp(vo,dto);
        return vo;
    }
}

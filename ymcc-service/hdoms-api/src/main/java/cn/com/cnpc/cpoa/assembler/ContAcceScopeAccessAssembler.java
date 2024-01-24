package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.domain.contractor.BizContAcceScopeDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAcceScopeVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContAcceScopeData;

/**
 * @Author: 17742856263
 * @Date: 2019/10/14 21:03
 * @Description:
 */
public class ContAcceScopeAccessAssembler {

    public static ContAcceScopeData convertDtoToData(BizContAcceScopeDto dto){
        ContAcceScopeData data=new ContAcceScopeData();
        BeanUtils.copyBeanProp(data,dto);
        return data;
    }

    public static BizContAcceScopeDto convertVoToDto(ContAcceScopeVo vo){
        BizContAcceScopeDto dto=new BizContAcceScopeDto();
        BeanUtils.copyBeanProp(dto,vo);
        return dto;
    }

    public static ContAcceScopeVo convertDtoToVo(BizContAcceScopeDto dto){
        ContAcceScopeVo vo=new ContAcceScopeVo();
        BeanUtils.copyBeanProp(vo,dto);
        return vo;
    }

}

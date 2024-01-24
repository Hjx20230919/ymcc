package cn.com.cnpc.cpoa.assembler;

import cn.com.cnpc.cpoa.common.constants.ContractorConstant;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessProjDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContContractorDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.enums.contractor.ContAccessProjEnum;
import cn.com.cnpc.cpoa.po.contractor.ContAccessProjPo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.contractor.ContAccessProjVo;
import cn.com.cnpc.cpoa.vo.contractor.data.ContAccessProjData;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 22:38
 * @Description:
 */
public class ContAccessProjAssembler {


    public static BizContAccessProjDto getInitContAccessProj(BizContContractorDto contContractor, BizContAccessDto contAccess,BizContProjectDto contProjectDto,String proName){
        BizContAccessProjDto contAccessProjDto=new BizContAccessProjDto();
        contAccessProjDto.setProjId(StringUtils.getUuid32());
        contAccessProjDto.setAcceId(contAccess.getAcceId());
        contAccessProjDto.setContId(contContractor.getContId());
        contAccessProjDto.setProjName(proName);
        contAccessProjDto.setProjAccessType(contProjectDto.getProjAccessType());
        contAccessProjDto.setProjState(ContAccessProjEnum.APPLY.getKey());
        contAccessProjDto.setAcceLimitTime(DateUtils.getNowDate());
        return contAccessProjDto;
    }

    public static ContAccessProjVo convertPoToVo(ContAccessProjPo po){
        ContAccessProjVo vo=new ContAccessProjVo();
        BeanUtils.copyBeanProp(vo,po);
        vo.setAcceLimitTime(DateUtils.dateTimeYYYY_MM_DD(po.getAcceLimitTime()));
        return vo;
    }

    public static ContAccessProjData convertPoToData(ContAccessProjPo po){
        ContAccessProjData data=new ContAccessProjData();
        BeanUtils.copyBeanProp(data,po);
        data.setProjName(po.getProjName());
        return data;
    }
}

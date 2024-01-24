package cn.com.cnpc.cpoa.assembler.project;

import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.po.project.ProActivitiItemPo;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.vo.project.ProActivitiItemVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectVo;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 19:34
 * @Description:
 */
public class ProjProjectAssembler {


    public static BizProjProjectDto convertVoToDto(ProjProjectVo vo) {
        BizProjProjectDto dto = new BizProjProjectDto();
        BeanUtils.copyBeanProp(dto, vo);
        return dto;
    }

    public static ProjProjectVo convertDtoToVo(BizProjProjectDto dto) {
        ProjProjectVo vo = new ProjProjectVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getCreateAt()));
        vo.setNtenderStartDate(DateUtils.dateTimeYYYY_MM_DD(dto.getNtenderStartDate()));
        vo.setNtenderEndDate(DateUtils.dateTimeYYYY_MM_DD(dto.getNtenderEndDate()));
        vo.setOtenderStartDate(DateUtils.dateTimeYYYY_MM_DD(dto.getOtenderStartDate()));
        vo.setOtenderEndDate(DateUtils.dateTimeYYYY_MM_DD(dto.getOtenderEndDate()));
        return vo;
    }

    public static ProActivitiItemVo convertItemPoToItemVo(ProActivitiItemPo po) {
        ProActivitiItemVo vo = new ProActivitiItemVo();
        BeanUtils.copyBeanProp(vo, po);
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(po.getCreateAt()));
        return vo;
    }
}

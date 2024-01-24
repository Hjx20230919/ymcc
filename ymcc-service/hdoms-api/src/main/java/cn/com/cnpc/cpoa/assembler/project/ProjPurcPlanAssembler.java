package cn.com.cnpc.cpoa.assembler.project;

import cn.com.cnpc.cpoa.domain.project.BizProjPlanListDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcPlanDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.project.ProjPlanListVo;
import cn.com.cnpc.cpoa.vo.project.ProjProjectPlanVo;
import cn.com.cnpc.cpoa.vo.project.ProjPurcPlanVo;

/**
 * @Author: 17742856263
 * @Date: 2019/12/13 20:31
 * @Description:
 */
public class ProjPurcPlanAssembler {


    public static ProjProjectPlanVo convertDtoToPlanVo(BizProjProjectDto dto) {
        ProjProjectPlanVo vo = new ProjProjectPlanVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getCreateAt()));
        return vo;
    }

    public static BizProjPurcPlanDto convertVoToDto(ProjPurcPlanVo vo) {
        BizProjPurcPlanDto dto = new BizProjPurcPlanDto();
        BeanUtils.copyBeanProp(dto, vo);
        return dto;
    }


    public static BizProjPlanListDto convertVoToListDto(ProjPlanListVo vo, String planId) {
        BizProjPlanListDto dto = new BizProjPlanListDto();
        BeanUtils.copyBeanProp(dto, vo);
        dto.setPlanListId(StringUtils.getUuid32());
        dto.setPlanId(planId);
        return dto;
    }

    public static ProjPurcPlanVo convertDtoToProjPurcPlanVo(BizProjProjectDto dto) {
        ProjPurcPlanVo vo = new ProjPurcPlanVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getCreateAt()));
        return vo;
    }

}

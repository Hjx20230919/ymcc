package cn.com.cnpc.cpoa.assembler.project;

import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.domain.project.BizProjSelContDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.vo.project.ProjProjectSelVo;
import cn.com.cnpc.cpoa.vo.project.ProjSelContVo;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:45
 * @Description:
 */
public class ProjSelContAssembler {


    public static ProjProjectSelVo convertDtoToSelVo(BizProjProjectDto dto) {
        ProjProjectSelVo vo = new ProjProjectSelVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getCreateAt()));
        return vo;
    }

    public static BizProjSelContDto convertVoToDto(ProjSelContVo vo) {
        BizProjSelContDto dto = new BizProjSelContDto();
        BeanUtils.copyBeanProp(dto, vo);
        return dto;
    }

    public static ProjSelContVo convertDtoToProjSelContVo(BizProjProjectDto dto) {
        ProjSelContVo vo = new ProjSelContVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getCreateAt()));
        return vo;
    }


}

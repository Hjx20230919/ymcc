package cn.com.cnpc.cpoa.assembler.project;

import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcResultDto;
import cn.com.cnpc.cpoa.domain.project.BizProjResultListDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.project.ProjProjectResultVo;
import cn.com.cnpc.cpoa.vo.project.ProjPurcResultVo;
import cn.com.cnpc.cpoa.vo.project.ProjRsultListVo;

/**
 * @Author: 17742856263
 * @Date: 2019/12/13 20:31
 * @Description:
 */
public class ProjPurcResultAssembler {


    public static ProjProjectResultVo convertDtoToResultVo(BizProjProjectDto dto) {
        ProjProjectResultVo vo = new ProjProjectResultVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getCreateAt()));
        return vo;
    }

    public static BizProjPurcResultDto convertVoToDto(ProjPurcResultVo vo) {
        BizProjPurcResultDto dto = new BizProjPurcResultDto();
        BeanUtils.copyBeanProp(dto, vo);
        return dto;
    }


    public static BizProjResultListDto convertVoToListDto(ProjRsultListVo vo, String resultId) {
        BizProjResultListDto dto = new BizProjResultListDto();
        BeanUtils.copyBeanProp(dto, vo);
        dto.setResultListId(StringUtils.getUuid32());
        dto.setResultId(resultId);
        dto.setUnitPrice(vo.getEstUnitPrice());
        dto.setLimitTotalPrice(vo.getEstTotalPrice());
        return dto;
    }


    public static ProjPurcResultVo convertDtoToProjPurcResultVo(BizProjProjectDto dto) {
        ProjPurcResultVo vo = new ProjPurcResultVo();
        BeanUtils.copyBeanProp(vo, dto);
        vo.setCreateAt(DateUtils.dateTimeYYYY_MM_DD_HH_MM_SS(dto.getCreateAt()));
        return vo;
    }
}

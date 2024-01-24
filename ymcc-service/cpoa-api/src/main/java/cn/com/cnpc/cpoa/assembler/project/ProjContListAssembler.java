package cn.com.cnpc.cpoa.assembler.project;

import cn.com.cnpc.cpoa.domain.project.BizProjContListDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.vo.project.ProjContListVo;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:45
 * @Description:
 */
public class ProjContListAssembler {

    public static ProjContListVo convertDtoToVo(BizProjContListDto dto) {
        ProjContListVo vo = new ProjContListVo();
        BeanUtils.copyBeanProp(vo, dto);
        return vo;
    }

    public static BizProjContListDto convertVoToDto(ProjContListVo vo) {
        BizProjContListDto dto = new BizProjContListDto();
        BeanUtils.copyBeanProp(dto, vo);
        return dto;
    }


}

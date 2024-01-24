package cn.com.cnpc.cpoa.assembler.project;

import cn.com.cnpc.cpoa.domain.project.BizProjProjectOpentenderDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.vo.project.ProjProjectVo;

public class ProjProjectOpentenderAssembler {

    public static BizProjProjectOpentenderDto convertVoToPo(ProjProjectVo vo) throws Exception {
        BizProjProjectOpentenderDto dto = new BizProjProjectOpentenderDto();
        BeanUtils.copyBeanProp(dto, vo);
        dto.setOtenderStartDate(DateUtils.parseDate(vo.getOtenderStartDate()));
        dto.setOtenderEndDate(DateUtils.parseDate(vo.getOtenderEndDate()));
        return dto;
    }

}
package cn.com.cnpc.cpoa.assembler.project;

import cn.com.cnpc.cpoa.domain.project.BizProjProjectNotenderDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.DateUtils;
import cn.com.cnpc.cpoa.vo.project.ProjProjectVo;

/**
 * @Author: 17742856263
 * @Date: 2020/4/2 21:53
 * @Description:
 */
public class ProjProjectNotenderAssembler {


    public static BizProjProjectNotenderDto convertVoToPo(ProjProjectVo vo) throws Exception {
        BizProjProjectNotenderDto dto = new BizProjProjectNotenderDto();
        BeanUtils.copyBeanProp(dto,vo);
        dto.setNtenderStartDate(DateUtils.parseDate(vo.getNtenderStartDate()));
        dto.setNtenderEndDate(DateUtils.parseDate(vo.getNtenderEndDate()));
        return dto;
    }


}

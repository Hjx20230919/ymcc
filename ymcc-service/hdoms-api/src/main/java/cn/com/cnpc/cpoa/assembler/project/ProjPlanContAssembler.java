package cn.com.cnpc.cpoa.assembler.project;

import cn.com.cnpc.cpoa.domain.project.BizProjPlanContDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.project.ProjPlanContVo;

/**
 * @Author: 17742856263
 * @Date: 2019/12/21 15:05
 * @Description:
 */
public class ProjPlanContAssembler {


    public static BizProjPlanContDto convertDtoToVo(ProjPlanContVo vo, String planListId) {
        BizProjPlanContDto dto = new BizProjPlanContDto();
        BeanUtils.copyBeanProp(dto, vo);
        dto.setPlanListId(planListId);
        dto.setId(StringUtils.getUuid32());
        return dto;
    }
}

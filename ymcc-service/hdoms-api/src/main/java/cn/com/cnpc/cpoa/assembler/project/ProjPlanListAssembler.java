package cn.com.cnpc.cpoa.assembler.project;

import cn.com.cnpc.cpoa.domain.project.BizProjPlanListDto;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.vo.project.ProjPlanListVo;

/**
 * @Author: 17742856263
 * @Date: 2019/12/21 15:05
 * @Description:
 */
public class ProjPlanListAssembler {


    public static ProjPlanListVo convertDtoToVo(BizProjPlanListDto projPlanListDto) {
        ProjPlanListVo projPlanListVo = new ProjPlanListVo();
        BeanUtils.copyBeanProp(projPlanListVo, projPlanListDto);

        return projPlanListVo;
    }
}

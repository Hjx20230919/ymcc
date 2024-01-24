package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.assembler.project.ProjPlanListAssembler;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.project.BizProjPlanListDto;
import cn.com.cnpc.cpoa.mapper.project.BizProjPlanListDtoMapper;
import cn.com.cnpc.cpoa.vo.project.ProjPlanContVo;
import cn.com.cnpc.cpoa.vo.project.ProjPlanListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:21
 * @Description:
 */
@Service
public class ProjPlanListService extends AppService<BizProjPlanListDto> {


    @Autowired
    BizProjPlanListDtoMapper bizProjPlanListDtoMapper;

    @Autowired
    ProjPlanContService projPlanContService;


    public List<BizProjPlanListDto> selectPlanList(Map<String, Object> params) {

        return bizProjPlanListDtoMapper.selectPlanList(params);
    }

    public List<ProjPlanListVo> selectPlanListVo(Map<String, Object> params) {
        List<BizProjPlanListDto> planListDtos = bizProjPlanListDtoMapper.selectPlanList(params);
        List<ProjPlanListVo> projPlanListVos = new ArrayList<>();
        planListDtos.forEach(projPlanListDto -> {
            ProjPlanListVo projPlanListVo = ProjPlanListAssembler.convertDtoToVo(projPlanListDto);
            params.put("planListId",projPlanListVo.getPlanListId());
            List<ProjPlanContVo> projPlanContVos = projPlanContService.selectPlanCont(params);
            projPlanListVo.setProjPlanContVos(projPlanContVos);
            projPlanListVos.add(projPlanListVo);

        });
        return projPlanListVos;
    }


}

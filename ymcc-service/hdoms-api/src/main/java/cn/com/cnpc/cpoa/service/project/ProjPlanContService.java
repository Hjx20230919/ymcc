package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.project.BizProjPlanContDto;
import cn.com.cnpc.cpoa.mapper.project.BizProjPlanContDtoMapper;
import cn.com.cnpc.cpoa.vo.project.ProjPlanContVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/21 15:00
 * @Description:
 */
@Service
public class ProjPlanContService extends AppService<BizProjPlanContDto> {

    @Autowired
    BizProjPlanContDtoMapper bizProjPlanContDtoMapper;

    public List<ProjPlanContVo> selectPlanCont(Map<String, Object> params) {
        return bizProjPlanContDtoMapper.selectPlanCont(params);
    }
}

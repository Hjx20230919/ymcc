package cn.com.cnpc.cpoa.service.project;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.project.BizProjPlanListDto;
import cn.com.cnpc.cpoa.domain.project.BizProjResultListDto;
import cn.com.cnpc.cpoa.mapper.project.BizProjResultListDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:23
 * @Description:
 */
@Service
public class ProjResultListService extends AppService<BizProjResultListDto> {

    @Autowired
    BizProjResultListDtoMapper bizProjResultListDtoMapper;


    public List<BizProjResultListDto> selectResultList(Map<String, Object> params) {

        return bizProjResultListDtoMapper.selectResultList(params);
    }
}

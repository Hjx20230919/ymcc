package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjPlanContDto;
import cn.com.cnpc.cpoa.vo.project.ProjPlanContVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/21 15:00
 * @Description:
 */
@Mapper
public interface BizProjPlanContDtoMapper extends AppMapper<BizProjPlanContDto> {


    /**
     * 根据planlistId查询 所选承包商
     * @param params
     * @return
     */
    List<ProjPlanContVo> selectPlanCont(Map<String, Object> params);
}

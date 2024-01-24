package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjPlanListDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:25
 * @Description:
 */
@Mapper
public interface BizProjPlanListDtoMapper extends AppMapper<BizProjPlanListDto> {

    /**
     * 根据panid查询实体
     * @param params
     * @return
     */
    List<BizProjPlanListDto> selectPlanList(Map<String, Object> params);


}

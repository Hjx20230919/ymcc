package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcPlanAttachDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/21 10:42
 * @Description:
 */
@Mapper
public interface BizProjPurcPlanAttachMapper extends AppMapper<BizProjPurcPlanAttachDto> {


    /**
     * 根据panId查询中间表
     * @param param
     * @return
     */
    List<BizProjPurcPlanAttachDto> selectProjPurcPlanAttachDto(Map<String, Object> param);


}

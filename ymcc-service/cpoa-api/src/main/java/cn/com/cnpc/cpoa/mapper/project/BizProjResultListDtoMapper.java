package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjPlanListDto;
import cn.com.cnpc.cpoa.domain.project.BizProjResultListDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:26
 * @Description:
 */
@Mapper
public interface BizProjResultListDtoMapper extends AppMapper<BizProjResultListDto> {

    /**
     * 查根据resultId询采购结果
     * @param params
     * @return
     */
    List<BizProjResultListDto> selectResultList(Map<String, Object> params);
}

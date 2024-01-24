package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcPlanDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/11 20:24
 * @Description:
 */
@Mapper
public interface BizProjPurcPlanDtoMapper  extends AppMapper<BizProjPurcPlanDto> {

    /**
     * 查询采购方案综合信息
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectProjPurcPlan(Map<String, Object> params);


    /**
     * 查询采购方案待审核
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectAuditProjPurcPlan(Map<String, Object> params);


    /**
     * 查询采购方案已审核
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectAuditedProjPurcPlan(Map<String, Object> params);

    /**
     * 查询当前编码
     * @param param
     * @return
     */
    String selectCurrentDealNo(Map<String, Object> param);
}

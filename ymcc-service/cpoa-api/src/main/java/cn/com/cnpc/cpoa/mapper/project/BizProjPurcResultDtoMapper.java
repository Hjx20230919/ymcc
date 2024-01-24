package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.domain.project.BizProjPurcResultDto;
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
public interface BizProjPurcResultDtoMapper extends AppMapper<BizProjPurcResultDto> {

    /**
     * 采购综合查询
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectProjPurcResult(Map<String, Object> params);

    /**
     * 查询待审核
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectAuditProjPurcResult(Map<String, Object> params);

    /**
     * 查询已审核
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectAuditedProjPurcResult(Map<String, Object> params);


    /**
     * 查询当前编码
     * @param param
     * @return
     */
    String selectCurrentDealNo(Map<String, Object> param);

    List<String> selectResultId();
}

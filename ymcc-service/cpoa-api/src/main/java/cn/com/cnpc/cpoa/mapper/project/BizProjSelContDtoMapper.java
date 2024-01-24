package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjContListDto;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.domain.project.BizProjSelContDto;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/7 10:09
 * @Description:
 */
@Mapper
public interface BizProjSelContDtoMapper extends AppMapper<BizProjSelContDto> {


    /**
     *  查询选商基础信息
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectProjSelCont(Map<String, Object> params);


    /**
     * 查询待审核
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectAuditProjSelCont(Map<String, Object> params);

    /**
     * 查询已审核
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectAuditedProjSelCont(Map<String, Object> params);

    BizProjSelContDto selectByprojId(String projId);

    void updateByselContId(BizProjSelContDto selContId);
}

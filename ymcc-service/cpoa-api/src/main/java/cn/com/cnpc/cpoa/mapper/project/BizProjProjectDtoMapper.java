package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectDto;
import cn.com.cnpc.cpoa.po.project.ProActivitiItemPo;
import cn.com.cnpc.cpoa.po.project.ProjProjectPo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 19:21
 * @Description:
 */
@Mapper
public interface BizProjProjectDtoMapper extends AppMapper<BizProjProjectDto> {


    /**
     * 立项综合查询
     *
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectProject(Map<String, Object> params);

    /**
     * 待审核查询
     *
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectAuditProject(Map<String, Object> params);

    /**
     * 已审核查询
     *
     * @param params
     * @return
     */
    List<BizProjProjectDto> selectAuditedProject(Map<String, Object> params);

    /**
     * 查询当前审核人
     *
     * @param param
     * @return
     */
    List<BizProjProjectDto> selectUserNameList(Map<String, Object> param);

    /**
     * 查询待办事项--未审核
     *
     * @param params
     * @return
     */
    List<ProActivitiItemPo> selectAuditItem(Map<String, Object> params);

    /**
     * 查询待办事项--已审核
     *
     * @param params
     * @return
     */
    List<ProActivitiItemPo> selectAuditedItem(Map<String, Object> params);

    /**
     * 查询待办事项--当前审核人员
     *
     * @param param
     * @return
     */
    List<ProActivitiItemPo> selectUserNameListPo(Map<String, Object> param);

    /**
     * 查询流程中事项
     *
     * @param params
     * @return
     */
    List<ProActivitiItemPo> selectActivitiItem(Map<String, Object> params);

    /**
     * 查询当前编码
     * @param param
     * @return
     */
    String selectCurrentDealNo(Map<String, Object> param);

    /**
     * 查询当前编码-公开招标可不招标
     * @param param
     * @return
     */
    String selectCurrentDealNo2(Map<String, Object> param);

    /**
     * 查询可用选商
     * @param
     * @return
     */
    List<BizProjProjectDto> selectSelContPro(Map<String, Object> param);

    /**
     * 查询采购方案可用项目
     * @param param
     * @return
     */
    List<BizProjProjectDto> selectProjPurcPlan(Map<String, Object> param);

    /**
     * 查询采购结果
     * @param param
     * @return
     */
    List<BizProjProjectDto> selectProjPurcResult(Map<String, Object> param);

    ProjProjectPo selectProjComplete(@Param("resultId")String resultId);
}

package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceScopeDto;
import cn.com.cnpc.cpoa.domain.contractor.BizContAccessDto;
import cn.com.cnpc.cpoa.po.contractor.ContAccessAuditPo;
import cn.com.cnpc.cpoa.po.contractor.ContAccessPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/10 19:36
 * @Description:
 */
@Mapper
public interface BizContAccessDtoMapper extends AppMapper<BizContAcceScopeDto> {

    List<ContAccessPo> selectContAccess(Map<String, Object> params);

    List<BizContAccessDto> selectContAccessDto(Map<String, Object> params);

    List<ContAccessAuditPo> selectAuditContAccess(Map<String, Object> params);

    List<HashMap<String,Object>> selectAcceIdByPlanId(String planId);

    String selectAccessIdByProjIdAndContId(Map<String, Object> params);
}

package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContProjectDto;
import cn.com.cnpc.cpoa.po.contractor.ContProjectPo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/10/9 21:59
 * @Description:
 */
@Mapper
public interface BizContProjectDtoMapper extends AppMapper<BizContProjectDto> {


    List<ContProjectPo> selectContProject(Map<String, Object> params);

    List<ContProjectPo> selectAuditContProject(Map<String, Object> params);

    List<ContProjectPo> selectActivitiItem(Map<String, Object> params);

    List<ContProjectPo> selectAuditedContProject(Map<String, Object> params);

    List<ContProjectPo> selectUserNameList(Map<String, Object> param);

}

package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceAchievementDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizContAcceAchievementDtoMapper extends AppMapper<BizContAcceAchievementDto> {

    /**
     *  查询实体
     * @param params
     * @return
     */
    List<BizContAcceAchievementDto> selectContAcceAchievement(Map<String, Object> params);
}

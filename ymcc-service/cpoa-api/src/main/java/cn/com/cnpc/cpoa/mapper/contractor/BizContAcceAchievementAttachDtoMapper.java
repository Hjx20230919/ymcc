package cn.com.cnpc.cpoa.mapper.contractor;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.contractor.BizContAcceAchievementAttachDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;
@Mapper
public interface BizContAcceAchievementAttachDtoMapper extends AppMapper<BizContAcceAchievementAttachDto> {


    /**
     * 查询中间表
     * @param param
     * @return
     */
    List<BizContAcceAchievementAttachDto> selectContAcceAchievementAttach(Map<String, Object> param);
}

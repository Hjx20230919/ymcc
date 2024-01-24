package cn.com.cnpc.cpoa.mapper.bid;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.bid.BidProjectDto;
import cn.com.cnpc.cpoa.po.bid.BidProjectPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BidProjectDtoMapper extends AppMapper<BidProjectDto> {

    List<BidProjectPo> selectBidProjectByMap(Map<String,Object> param);

    List<BidProjectPo> selectActivitiBidProject(Map<String,Object> param);

    List<BidProjectPo> selectStopBidProject(Map<String,Object> param);

    List<BidProjectPo> selectActivitiItemBidProject(Map<String,Object> param);

    List<BidProjectPo> selectAuditingBidProject(Map<String,Object> param);

    List<BidProjectPo> selectActivitiBidTender(Map<String,Object> param);

    List<BidProjectPo> selectActivitiPersonnel(Map<String,Object> param);
}

package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.ContBlackListHisDto;
import cn.com.cnpc.cpoa.po.contractor.ContBlackListHisPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface ContBlackListHisDtoMapper extends AppMapper<ContBlackListHisDto> {

    List<ContBlackListHisPo> selectContBlackListHis(Map<String,Object> param);
}

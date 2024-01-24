package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizContBlackListDto;
import cn.com.cnpc.cpoa.po.contractor.BizContBlackListPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BizContBlackListDtoMapper extends AppMapper<BizContBlackListDto> {
    List<BizContBlackListPo> selectAllBlackList(Map<String,Object> map);

    int deleteByBlackListId(String blackListId);

    /**
     * 用于验证黑名单承包商
     * @param map
     * @return
     */
    List<BizContBlackListDto> vaildationBlackList(Map<String,Object> map);
}

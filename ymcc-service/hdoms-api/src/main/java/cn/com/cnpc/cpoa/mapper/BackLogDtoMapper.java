package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.po.BackLogPo;
import cn.com.cnpc.cpoa.po.MyProcessPo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
public interface BackLogDtoMapper {

    List<BackLogPo> selectBackLogByUserId(Map<String,Object> params);

    List<BackLogPo> selectAuditByUserId(Map<String,Object> params);

    List<MyProcessPo> selectMyProcessByUserId(Map<String,Object> params);
}

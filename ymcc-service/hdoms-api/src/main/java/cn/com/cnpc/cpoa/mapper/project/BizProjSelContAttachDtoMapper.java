package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjSelContAttachDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 20:07
 * @Description:
 */
@Mapper
public interface BizProjSelContAttachDtoMapper extends AppMapper<BizProjSelContAttachDto> {


    /**
     * 查询中间表
     * @param params
     * @return
     */
    List<BizProjSelContAttachDto> selectProjSelContAttachDto(Map<String, Object> params);

}

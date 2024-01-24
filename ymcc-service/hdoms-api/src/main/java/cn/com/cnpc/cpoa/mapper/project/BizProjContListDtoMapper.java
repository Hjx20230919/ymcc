package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjContListDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/9 11:06
 * @Description:
 */
@Mapper
public interface BizProjContListDtoMapper extends AppMapper<BizProjContListDto> {


    /**
     *  查询选商列表
     * @param param
     * @return
     */
    List<BizProjContListDto> selectProjContList(Map<String, Object> param);




}

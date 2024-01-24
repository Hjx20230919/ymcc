package cn.com.cnpc.cpoa.mapper.project;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.project.BizProjProjectAttachDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/12/2 20:07
 * @Description:
 */
@Mapper
public interface BizProjProjectAttachDtoMapper  extends AppMapper<BizProjProjectAttachDto> {


    List<BizProjProjectAttachDto> selectProjProjectAttachDto(Map<String, Object> params);

}

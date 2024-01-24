package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.BizCheckTiDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/4/20 8:31
 * @Description:
 */
@Mapper
public interface BizCheckTiDtoMapper extends AppMapper<BizCheckTiDto> {


    /**
     * 查询流程模板
     * @param param
     * @return
     */
    List<BizCheckTiDto> selectOwnerCheckTi(Map<String, Object> param);


    /**
     * 查询模板项基础信息
     * @param param
     * @return
     */
    List<BizCheckTiDto> selectCheckTi(Map<String, Object> param);


    /**
     * 根据id查询模板项
     * @param params
     * @return
     */
    List<BizCheckTiDto> selectCheckTiDetails(Map<String, Object> params);
}

package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.SysDeptDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 18:35
 * @Description: 部门映射
 */
@Mapper
public interface SysDeptDtoMapper extends AppMapper<SysDeptDto> {

    /**
     * 查询条件查询菜单
     * @param params
     * @return
     */
    List<SysDeptDto> selectList(Map<String, Object> params);

    List<SysDeptDto> selectList2(Map<String, Object> params);

    List<SysDeptDto> selectList4LaborScore();
}

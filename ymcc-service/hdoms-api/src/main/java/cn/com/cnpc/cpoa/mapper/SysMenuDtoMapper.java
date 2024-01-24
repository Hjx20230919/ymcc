package cn.com.cnpc.cpoa.mapper;

import cn.com.cnpc.cpoa.core.AppMapper;
import cn.com.cnpc.cpoa.domain.SysMenuDto;
import cn.com.cnpc.cpoa.vo.MenuVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 11:54
 * @Description: 菜单映射
 */
@Mapper
public interface SysMenuDtoMapper  extends AppMapper<SysMenuDto> {


    /**
     * 查询条件查询菜单
     * @param params
     * @return
     */
    List<SysMenuDto> selectList(Map<String, String> params);


    List<SysMenuDto> selectList2(Map<String, Object> params);

    List<SysMenuDto> selecMenuList(Map<String, String> params);

    /**
     * 查询个人菜单权限
     *
     * @param params
     * @return
     */
    List<SysMenuDto> selectSelfActions(Map<String, Object> params);

    List<String> selectMenuByUserId(String userId);

    List<MenuVo> selectActionsByUserId(Map<String, Object> params);

    List<String> selectButtonByPath(Map<String, Object> params);
}

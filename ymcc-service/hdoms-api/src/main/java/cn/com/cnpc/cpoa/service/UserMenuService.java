package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.SysUserMenuDto;
import cn.com.cnpc.cpoa.mapper.SysUserMenuDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 15:34
 * @Description: 用户权限服务
 */
@Service
public class UserMenuService extends AppService<SysUserMenuDto> {


    @Autowired
    SysUserMenuDtoMapper sysUserMenuDtoMapper;

    /**
     * 通过userId删除菜单权限
     * @param params
     */
    @Transactional
    public int deleteByAny(Map<String, Object> params){

        return sysUserMenuDtoMapper.deleteByAny(params);
    }


}

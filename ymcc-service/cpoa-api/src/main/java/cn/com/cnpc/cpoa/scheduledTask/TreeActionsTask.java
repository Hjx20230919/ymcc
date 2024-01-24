package cn.com.cnpc.cpoa.scheduledTask;

import cn.com.cnpc.cpoa.service.MenuService;
import cn.com.cnpc.cpoa.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

/**
 * @Author: YuXq
 * @CreateTime: 2022-09-28  08:57
 * @Description:
 * @Version: 1.0
 */
@Component
//@DependsOn("SysUserRoleDtoMapper")
public class TreeActionsTask {

    @Autowired
    private MenuService menuService;

    @Autowired
    private SysUserRoleService userRoleService;

//    @PostConstruct
//    @Transactional
    public void initTreeActions() {
        menuService.initTreeActions();
    }

//    @PostConstruct
//    @Transactional
    public void initUserRole() {
        userRoleService.initUserRole();
    }
}

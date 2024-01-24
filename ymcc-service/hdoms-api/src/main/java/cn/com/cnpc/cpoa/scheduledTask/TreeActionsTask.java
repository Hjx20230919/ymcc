package cn.com.cnpc.cpoa.scheduledTask;

import cn.com.cnpc.cpoa.service.MenuService;
import cn.com.cnpc.cpoa.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @Author: YuXq
 * @CreateTime: 2022-09-28  08:57
 * @Description:
 * @Version: 1.0
 */
@Component
public class TreeActionsTask {

    @Autowired
    private MenuService menuService;

    @Autowired
    private SysUserRoleService userRoleService;

//    @PostConstruct
    public void initTreeActions() {
        menuService.initTreeActions();
    }

//    @PostConstruct
    public void initUserRole() {
        userRoleService.initUserRole();
    }
}

package cn.com.cnpc.cpoa.vo;

import lombok.Data;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 16:09
 * @Description: 用户权限
 */
@Data
public class UserMenuVo {

    /**
     * 菜单权限ID
     */
    private String actionId;

    /**
     * 用户ID
     */
    private String userId;
}

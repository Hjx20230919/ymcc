package cn.com.cnpc.cpoa.vo;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.List;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 14:18
 * @Description: 菜单视图层实体
 */
@Data
public class MenuVo {

    /**
     * 菜单标识
     */
    private String actionId;

    /**
     * 权限名称
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 父action_id
     */
    private String parentActionId;

    /**
     * Action URL
     */
    private String actionUrl;

    /**
     * Action_icon
     */
    private String actionIcon;

    /**
     * 序号
     */
    private Integer seq;

    /**
     * 备注
     */
    private String notes;

    private String path;

    private String redirect;

    private String component;

    private String title;

    private String icon;

    private String syskind;

    private Meta meta;

    private List<MenuVo> children;


}

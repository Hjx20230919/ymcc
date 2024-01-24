package cn.com.cnpc.cpoa.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/5/9 16:16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleVo {
    private String roleId;

    private String name;

    private String dataScope;

    /**
     * 0 系统默认的角色，不能删除
     * 1 自定义角色
     */
    private String type;

    private String notes;


    private String haveFlag;

    private List<String> actionIds;
}

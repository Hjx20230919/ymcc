package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.SysRoleActionDto;
import cn.com.cnpc.cpoa.mapper.SysRoleActionDtoMapper;
import cn.com.cnpc.cpoa.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author Yuxq
 * @version 1.0
 * @description: TODO
 * @date 2022/4/25 16:02
 */
@Service
public class SysRoleActionService extends AppService<SysRoleActionDto> {

    @Autowired
    private SysRoleActionDtoMapper roleActionDtoMapper;

    /**
     * 查询角色拥有菜单权限
     * @param roleID
     * @return
     */
    public AppMessage selectMenuAuth(String roleID) {
        List<String> menuAuths = roleActionDtoMapper.selectMenuAuthByRoleID(roleID);
        return AppMessage.success(menuAuths,"查询所有菜单权限成功");
    }


    /**
     * 修改角色菜单权限
     * @param roleID    角色ID
     * @param addID 新增菜单权限ID
     * @return
     */
    public AppMessage addRoleMenuAuth(String roleID, List<String> addID) {
        try {
            //修改之前先删除原来角色菜单权限
            roleActionDtoMapper.deleteByRoleID(roleID);

            //增加权限
            if (addID.size() > 0){
                addID.forEach(actionID -> {
                    SysRoleActionDto sysRoleActionDto = SysRoleActionDto.builder()
                            .actionId(StringUtils.getUuid32())
                            .roleId(roleID)
                            .actionId(actionID)
                            .build();
                    save(sysRoleActionDto);
                });
            }

            return AppMessage.result("权限修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return AppMessage.error("权限修改失败！！");
        }
    }

    /**
     * 根据用户id查询该角色拥有菜单权限名
     * @param userId    用户ID
     * @return
     */
    public List<String> selectActionUrlByRoleID(String userId){
        return roleActionDtoMapper.selectActionUrlByRoleID(userId);
    }

    public List<SysRoleActionDto> selectRoleActions(Map<String, Object> params) {

        return roleActionDtoMapper.selectRoleActions(params);
    }
}

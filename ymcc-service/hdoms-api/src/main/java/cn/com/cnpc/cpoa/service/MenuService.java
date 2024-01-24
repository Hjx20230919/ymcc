package cn.com.cnpc.cpoa.service;

import cn.com.cnpc.cpoa.core.AppMessage;
import cn.com.cnpc.cpoa.core.AppService;
import cn.com.cnpc.cpoa.domain.SysMenuDto;
import cn.com.cnpc.cpoa.domain.SysRoleDto;
import cn.com.cnpc.cpoa.domain.SysUserDto;
import cn.com.cnpc.cpoa.domain.SysUserRoleDto;
import cn.com.cnpc.cpoa.mapper.SysMenuDtoMapper;
import cn.com.cnpc.cpoa.mapper.SysRoleDtoMapper;
import cn.com.cnpc.cpoa.utils.BeanUtils;
import cn.com.cnpc.cpoa.utils.LocalCache;
import cn.com.cnpc.cpoa.utils.ServletUtils;
import cn.com.cnpc.cpoa.utils.StringUtils;
import cn.com.cnpc.cpoa.vo.MenuVo;
import cn.com.cnpc.cpoa.vo.Meta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: 17742856263
 * @Date: 2019/3/4 11:48
 * @Description:  菜单服务
 */
@Service
public class MenuService extends AppService<SysMenuDto> {

    @Autowired
    SysMenuDtoMapper sysMenuDtoMapper;

    @Autowired
    private SysUserRoleService userRoleService;

    public List<SysMenuDto> selectList(Map<String, String> params){

        return sysMenuDtoMapper.selectList(params);
    }

    public List<SysMenuDto> selectList2(Map<String, Object> params){

        return sysMenuDtoMapper.selectList2(params);
    }

    /**
     * 查询所有菜单
     * @param params
     * @return
     */
    public AppMessage selectMenuList(Map<String, String> params) {
        List<SysMenuDto> list = sysMenuDtoMapper.selecMenuList(params);
        return AppMessage.success(list,"查询菜单项成功");
    }

    public List<MenuVo> selectTreeActions(String menuFlag) {
        String userId = ServletUtils.getSessionUserId();
        String roleId = userRoleService.selectRoleIdByUserId(userId);
        //获取缓存
        LocalCache localCache = LocalCache.getInstance();
        localCache.clear();
        //先从缓存中获取，如果缓存中没有再去数据库查询,并存入缓存
        if (localCache.containsKey(roleId)) {
            List<MenuVo> oneActions = localCache.getValue(roleId);
            return oneActions;
        } else {
            //先查询一级菜单数据
            Map<String, Object> one = new HashMap<>();
            one.put("roleId", roleId);
            one.put("parentActionId", null);
            List<MenuVo> oneActions = sysMenuDtoMapper.selectActionsByUserId(one);
            setMeta(oneActions);
            //获取子级菜单
            selectChildren(oneActions,roleId);
            //存入缓存
            localCache.putValue(roleId,oneActions);
            return oneActions;
        }
    }

    public void initTreeActions() {
        HashMap<String, Object> param = new HashMap<>();
        List<SysUserRoleDto> roleDtos = userRoleService.selectUserRoles(param);
        //获取缓存
        LocalCache localCache = LocalCache.getInstance();

        roleDtos.parallelStream().forEach(sysUserRoleDto -> {
            String roleId = sysUserRoleDto.getRoleId();
            //先查询一级菜单数据
            Map<String, Object> one = new HashMap<>();
            one.put("roleId", roleId);
            one.put("parentActionId", null);
            List<MenuVo> oneActions = sysMenuDtoMapper.selectActionsByUserId(one);
            setMeta(oneActions);
            //获取子级菜单
            selectChildren(oneActions,roleId);
            //存入缓存
            localCache.putValue(roleId,oneActions);
        });
    }



//    /**
//     * 减少频繁查询数据库
//     * @param menuFlag
//     * @return
//     */
//    public List<MenuVo> selectTreeActions(String menuFlag) {
//        String userId = ServletUtils.getSessionUserId();
//        String roleId = userRoleService.selectRoleIdByUserId(userId);
//        //获取缓存
//        LocalCache localCache = LocalCache.getInstance();
//        //先从缓存中获取，如果缓存中没有再去数据库查询,并存入缓存
//        if (localCache.containsKey(roleId)) {
//            List<MenuVo> oneActions = localCache.getValue(roleId);
//            return oneActions;
//        } else {
//            Map<String, Object> one = new HashMap<>();
//            one.put("roleId", roleId);
//            List<MenuVo> all = sysMenuDtoMapper.selectActionsByUserId(one);
//            List<MenuVo> oneActions = all.stream().filter(menuVo -> menuVo.getParentActionId() == null)
//                    .map(menuVo -> {
//                        menuVo.setChildren(getChildrens(menuVo, all));
//                        return menuVo;
//                    }).sorted((menuVo1, menuVo2) -> menuVo1.getSeq() - menuVo2.getSeq())
//                    .collect(Collectors.toList());
//            //填充meta数据
//            setChildrenMeta(oneActions);
//            //存入缓存
//            localCache.putValue(roleId,oneActions);
//            return oneActions;
//        }
//    }
//
//    private List<MenuVo> getChildrens(MenuVo parent,List<MenuVo> all) {
//        List<MenuVo> collect = all.stream().filter(menuVo -> parent.getActionId().equals(menuVo.getParentActionId()))
//                .map(menuVo -> {
//            menuVo.setChildren(getChildrens(menuVo, all));
//            return menuVo;
//                }).sorted((menuVo1, menuVo2) -> menuVo1.getSeq() - menuVo2.getSeq())
//                .collect(Collectors.toList());
//        return collect;
//    }

    public void selectChildren(List<MenuVo> actions,String roleId){
        if (actions.size() > 0) {
            actions.parallelStream().forEach(menuVo -> {
                //查询下一级菜单
                Map<String, Object> two = new HashMap<>();
                two.put("roleId", roleId);
                two.put("parentActionId", menuVo.getActionId());
                List<MenuVo> twoActions = sysMenuDtoMapper.selectActionsByUserId(two);
                setMeta(twoActions);
                menuVo.setChildren(twoActions);
                if (twoActions.size() > 0){
                    this.selectChildren(twoActions,roleId);
                }
            });
        }
    }

    public void setChildrenMeta(List<MenuVo> actions){
        if (actions.size() > 0) {
            actions.parallelStream().forEach(menuVo -> {
                Meta meta = new Meta();
                meta.setIcon(menuVo.getIcon());
                meta.setSyskind(menuVo.getSyskind());
                meta.setTitle(menuVo.getTitle());
                meta.setType(menuVo.getType());
                menuVo.setMeta(meta);
                if (menuVo.getChildren().size() > 0){
                    this.setChildrenMeta(menuVo.getChildren());
                }
            });
        }
    }

    public void setMeta(List<MenuVo> actions){
        if (actions.size() > 0) {
            actions.parallelStream().forEach(menuVo -> {
                Meta meta = new Meta();
                meta.setIcon(menuVo.getIcon());
                meta.setSyskind(menuVo.getSyskind());
                meta.setTitle(menuVo.getTitle());
                meta.setType(menuVo.getType());
                menuVo.setMeta(meta);
            });
        }
    }

    private Map<String, List<MenuVo>> getBaseTreeDto(List<SysMenuDto> sysMenuDtos, Set<SysMenuDto> selfActions) {
        Map<String, List<MenuVo>> actionsMap = new HashMap<>();
        //1 数据装配
        Map<String, MenuVo> allActionsMap = new HashMap<>();
        List<MenuVo> allActionsDto = new ArrayList<>();
        List<MenuVo> selfActionsDto = new ArrayList<>();
        for (SysMenuDto actions : sysMenuDtos) {
            MenuVo treeVo = new MenuVo();
            BeanUtils.copyBeanProp(treeVo,actions);
            allActionsDto.add(treeVo);

            allActionsMap.put(actions.getActionId(), treeVo);
        }

        for (SysMenuDto selfAction : selfActions) {
            MenuVo treeVo = new MenuVo();
            BeanUtils.copyBeanProp(treeVo,selfAction);
            selfActionsDto.add(treeVo);
        }


        //2 获取根目录
        Set<MenuVo> rootActionsDto = new LinkedHashSet<>();
        for (MenuVo actionsTreeDto : selfActionsDto) {
            getRootActions(actionsTreeDto, allActionsMap, rootActionsDto);
        }

        actionsMap.put("allActionsTreeDto", allActionsDto);
        actionsMap.put("rootActionsTreeDto", new ArrayList<>(rootActionsDto));
        return actionsMap;
    }

    public static void getRootActions(MenuVo actionsNode, Map<String, MenuVo> allActionsMapeptMap,
                                      Set<MenuVo> rootActions) {
        String parentPartId = actionsNode.getParentActionId();
        if (StringUtils.isNotEmpty(parentPartId)) {
            MenuVo parentActionsNode = allActionsMapeptMap.get(parentPartId);
            getRootActions(parentActionsNode, allActionsMapeptMap, rootActions);
        } else {
            rootActions.add(actionsNode);
        }
        return;
    }

    /**
     * 查询当前页面按钮
     * @param path
     * @return
     */
    public List<String> selectButton(String path) {
        String userId = ServletUtils.getSessionUserId();
        HashMap<String, Object> param = new HashMap<>(8);
        param.put("userId",userId);
        param.put("path",path);
        List<String> list = sysMenuDtoMapper.selectButtonByPath(param);
        return list;
    }

//    public static List<MenuVo> genActionsUpTree(List<MenuVo> rootActionsTreeDto,
//                                                    List<MenuVo> allActionsTreeDto,
//                                                    Map<String, SysMenuDto> existsMap) {
//        for (MenuVo root : rootActionsTreeDto) {
//            root.setChildActions(getActionUpChild(root.getActionId(), allActionsTreeDto, existsMap));
//        }
//        return rootActionsTreeDto;
//    }
//
//    public static List<MenuVo> getActionUpChild(String id, List<MenuVo> allActionsTreeDto, Map<String
//            , SysMenuDto> existsMap) {
//        // 子单位
//        List<MenuVo> childList = new ArrayList<>();
//        for (MenuVo treeDto : allActionsTreeDto) {
//            // 遍历所有节点，将父单位id与传过来的id比较
//            if (StringUtils.isNotBlank(treeDto.getParentActionId())) {
//                if (treeDto.getParentActionId().equals(id)) {
//                    if (null != existsMap.get(treeDto.getActionId())) {
//                        childList.add(treeDto);
//                    }
//                }
//            }
//        }
//        // 把子单位的子单位再循环一遍
//        for (MenuVo treeDto : childList) {
//            // 递归
//            treeDto.setChildActions(getActionsChild(treeDto.getActionId(), allActionsTreeDto));
//        }
//
//        // 递归退出条件
//        if (childList.size() == 0) {
//            return null;
//        }
//        return childList;
//    }
//
//    public static List<MenuVo> getActionsChild(String id, List<MenuVo> allActionsTreeDto) {
//        // 子单位
//        List<MenuVo> childList = new ArrayList<>();
//        for (MenuVo treeDto : allActionsTreeDto) {
//            // 遍历所有节点，将父单位id与传过来的id比较
//            if (StringUtils.isNotBlank(treeDto.getParentActionId())) {
//                if (treeDto.getParentActionId().equals(id)) {
//                    childList.add(treeDto);
//                }
//            }
//        }
//        // 把子单位的子单位再循环一遍
//        for (MenuVo treeDto : childList) {
//            // 递归
//            treeDto.setChildActions(getActionsChild(treeDto.getActionId(), allActionsTreeDto));
//        }
//
//        // 递归退出条件
//        if (childList.size() == 0) {
//            return null;
//        }
//        return childList;
//    }
//
//    public Map<String, List<MenuVo>> getBaseTreeDto(List<SysMenuDto> allActions) {
//        Map<String, List<MenuVo>> actionsMap = new HashMap<>();
//        List<MenuVo> allActionsDto = new ArrayList<>();
//        List<MenuVo> rootActionsDto = new ArrayList<>();
//        for (SysMenuDto actions : allActions) {
//            MenuVo treeDto = new MenuVo();
//            BeanUtils.copyBeanProp(treeDto,actions);
//            allActionsDto.add(treeDto);
//            if (StringUtils.isEmpty(actions.getParentActionId())) {
//                rootActionsDto.add(treeDto);
//            }
//        }
//        actionsMap.put("allActionsTreeDto", allActionsDto);
//        actionsMap.put("rootActionsTreeDto", rootActionsDto);
//
//        return actionsMap;
//    }
//
//    public static List<MenuVo> genActionsTree(List<MenuVo> rootActionsTreeDto,
//                                                  List<MenuVo> allActionsTreeDto) {
//        for (MenuVo root : rootActionsTreeDto) {
//            root.setChildActions(getActionsChild(root.getActionId(), allActionsTreeDto));
//        }
//        return rootActionsTreeDto;
//    }
}

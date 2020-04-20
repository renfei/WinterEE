package com.winteree.uaa.service;

import java.util.List;

/**
 * <p>Title: MenuService</p>
 * <p>Description: 菜单服务</p>
 *
 * @author RenFei
 * @date : 2020-04-20 12:47
 */
public interface MenuService {
    /**
     * 获取所有菜单权限列表
     *
     * @return 所有权限列表
     */
    List<String> getAllMenuPermission();

    /**
     * 根据角色ID列表获取菜单UUID列表
     *
     * @param roleUuid 角色ID列表
     * @return 菜单UUID列表
     */
    List<String> getMenuUuidByRoleUuid(List<String> roleUuid);

    /**
     * 根据菜单列表获取权限列表
     *
     * @param menuUuid 菜单列表
     * @return 权限列表
     */
    List<String> getMenuPermissionByMenuUuid(List<String> menuUuid);
}

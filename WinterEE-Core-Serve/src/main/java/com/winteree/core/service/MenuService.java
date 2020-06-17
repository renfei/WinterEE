package com.winteree.core.service;

import com.winteree.api.entity.MenuVO;
import net.renfei.sdk.entity.APIResult;

import java.util.List;

/**
 * <p>Title: MenuService</p>
 * <p>Description: 菜单服务</p>
 *
 * @author RenFei
 * @date : 2020-04-18 16:15
 */
public interface MenuService {
    /**
     * 获取登录用户的菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @param language 语言
     * @return
     */
    APIResult<List<MenuVO>> getMenuListBySignedUser(String language);

    /**
     * 获取登录用户的菜单和权限列表，注意不是菜单管理中的查询菜单列表
     *
     * @param language 语言
     * @return
     */
    APIResult<List<MenuVO>> getMenuAndAuthorityListBySignedUser(String language);

    /**
     * 获取所有菜单
     *
     * @return
     */
    APIResult<List<MenuVO>> getAllMenuTree();

    APIResult<List<MenuVO>> getAllMenuList();

    APIResult deleteMenuByUuid(String uuid);

    APIResult<MenuVO> getMenuByUuid(String uuid);

    APIResult updateMenu(MenuVO menuVO);

    APIResult addMenu(MenuVO menuVO);

    /**
     * 获取所有菜单ID
     *
     * @return 所有菜单ID
     */
    List<String> getAllMenuId();
}

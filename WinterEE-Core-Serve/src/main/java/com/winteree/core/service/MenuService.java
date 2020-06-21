package com.winteree.core.service;

import com.winteree.api.entity.MenuVO;
import com.winteree.api.exception.FailureException;

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
    List<MenuVO> getMenuListBySignedUser(String language);

    /**
     * 获取登录用户的菜单和权限列表，注意不是菜单管理中的查询菜单列表
     *
     * @param language 语言
     * @return
     */
    List<MenuVO> getMenuAndAuthorityListBySignedUser(String language);

    /**
     * 获取所有菜单
     *
     * @return
     */
    List<MenuVO> getAllMenuTree();

    List<MenuVO> getAllMenuList();

    int deleteMenuByUuid(String uuid) throws FailureException;

    MenuVO getMenuByUuid(String uuid) throws FailureException;

    int updateMenu(MenuVO menuVO) throws FailureException;

    int addMenu(MenuVO menuVO) throws FailureException;

    /**
     * 获取所有菜单ID
     *
     * @return 所有菜单ID
     */
    List<String> getAllMenuId();
}

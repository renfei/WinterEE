package com.winteree.uaa.service.impl;

import com.winteree.uaa.dao.MenuDOMapper;
import com.winteree.uaa.dao.RoleMenuDOMapper;
import com.winteree.uaa.dao.entity.MenuDO;
import com.winteree.uaa.dao.entity.MenuDOExample;
import com.winteree.uaa.dao.entity.RoleMenuDO;
import com.winteree.uaa.dao.entity.RoleMenuDOExample;
import com.winteree.uaa.service.MenuService;
import net.renfei.sdk.utils.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: MenuServiceImpl</p>
 * <p>Description: 菜单服务</p>
 *
 * @author RenFei
 * @date : 2020-04-20 12:47
 */
@Service
public class MenuServiceImpl implements MenuService {
    private final MenuDOMapper menuDoMapper;
    private final RoleMenuDOMapper roleMenuDOMapper;

    public MenuServiceImpl(MenuDOMapper menuDoMapper,
                           RoleMenuDOMapper roleMenuDOMapper) {
        this.menuDoMapper = menuDoMapper;
        this.roleMenuDOMapper = roleMenuDOMapper;
    }

    /**
     * 获取所有菜单权限列表
     *
     * @return 所有权限列表
     */
    @Override
    public List<String> getAllMenuPermission() {
        MenuDOExample menuDoExample = new MenuDOExample();
        menuDoExample.createCriteria()
                .andPermissionIsNotNull()
                .andPermissionNotEqualTo("");
        return getStrings(menuDoExample);
    }

    @Override
    public List<String> getMenuUuidByRoleUuid(List<String> roleUuid) {
        RoleMenuDOExample roleMenuDOExample = new RoleMenuDOExample();
        roleMenuDOExample.createCriteria()
                .andRoleUuidIn(roleUuid);
        List<RoleMenuDO> roleMenuDOS = roleMenuDOMapper.selectByExample(roleMenuDOExample);
        List<String> menuUuid = new ArrayList<>();
        if (BeanUtils.isEmpty(roleMenuDOS)) {
            return menuUuid;
        }
        for (RoleMenuDO roleMenuDO : roleMenuDOS
        ) {
            menuUuid.add(roleMenuDO.getMenuUuid());
        }
        return menuUuid;
    }

    @Override
    public List<String> getMenuPermissionByMenuUuid(List<String> menuUuid) {
        MenuDOExample menuDoExample = new MenuDOExample();
        menuDoExample.createCriteria()
                .andUuidIn(menuUuid)
                .andPermissionIsNotNull()
                .andPermissionNotEqualTo("");
        return getStrings(menuDoExample);
    }

    private List<String> getStrings(MenuDOExample menuDoExample) {
        List<MenuDO> menuDoList = menuDoMapper.selectByExample(menuDoExample);
        List<String> permission = new ArrayList<>();
        if (BeanUtils.isEmpty(menuDoList)) {
            return permission;
        }
        for (MenuDO menuDO : menuDoList
        ) {
            permission.add(menuDO.getPermission());
        }
        return permission;
    }
}

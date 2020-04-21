package com.winteree.core.service.impl;

import com.winteree.api.entity.MenuVO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.MenuDOMapper;
import com.winteree.core.dao.RoleMenuDOMapper;
import com.winteree.core.dao.entity.MenuDO;
import com.winteree.core.dao.entity.MenuDOExample;
import com.winteree.core.dao.entity.RoleMenuDO;
import com.winteree.core.dao.entity.RoleMenuDOExample;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.*;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: MenuServiceImpl</p>
 * <p>Description: 菜单服务</p>
 *
 * @author RenFei
 * @date : 2020-04-18 16:16
 */
@Service
public class MenuServiceImpl extends BaseService implements MenuService {
    private final RoleService roleService;
    private final RoleMenuDOMapper roleMenuDOMapper;
    private final MenuDOMapper menuDOMapper;
    private final I18nMessageService i18nMessageService;

    protected MenuServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                              AccountService accountService,
                              RoleService roleService,
                              RoleMenuDOMapper roleMenuDOMapper,
                              MenuDOMapper menuDOMapper,
                              I18nMessageService i18nMessageService) {
        super(accountService, wintereeCoreConfig);
        this.roleService = roleService;
        this.roleMenuDOMapper = roleMenuDOMapper;
        this.menuDOMapper = menuDOMapper;
        this.i18nMessageService = i18nMessageService;
    }

    /**
     * 获取登录用户的菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @Override
    public APIResult<List<MenuVO>> getMenuListBySignedUser(String language) {
        if (BeanUtils.isEmpty(language)) {
            language = "zh-CN";
        }
        AccountDTO accountDTO = getSignedUser();
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 平台超级管理员，直接加载全部菜单
            List<String> menuIds = getAllMenuId();
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("")
                    .data(generateMenuTree(language, menuIds))
                    .build();
        } else {
            // 查询用户所属的角色
            List<String> roles = roleService.selectRoleUuidByUserUuid(accountDTO.getUuid());
            // 根据角色查询拥有的菜单
            List<String> menuIds = getMenuUuidByRoleUuid(roles);
            return APIResult.builder()
                    .code(StateCode.OK)
                    .message("")
                    .data(generateMenuTree(language, menuIds))
                    .build();
        }
    }

    /**
     * 获取所有菜单
     *
     * @return
     */
    @Override
    public APIResult<List<MenuVO>> getAllMenuList() {
        return APIResult.builder()
                .code(StateCode.OK)
                .message("")
                .data(generateAllMenuTree())
                .build();
    }

    @Override
    public List<String> getMenuUuidByRoleUuid(List<String> roleUuid) {
        if (BeanUtils.isEmpty(roleUuid)) {
            return new ArrayList<>();
        }
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
    public List<String> getAllMenuId() {
        MenuDOExample menuDoExample = new MenuDOExample();
        menuDoExample.setOrderByClause("sort DESC");
        menuDoExample.createCriteria()
                .andIsShowEqualTo(true)
                .andIsDeleteEqualTo(false);
        List<MenuDO> menuDoList = menuDOMapper.selectByExample(menuDoExample);
        List<String> uuids = new ArrayList<>();
        if (BeanUtils.isEmpty(menuDoList)) {
            return uuids;
        }
        for (MenuDO menuDO : menuDoList
        ) {
            uuids.add(menuDO.getUuid());
        }
        return uuids;
    }

    /**
     * 获取菜单树
     *
     * @param menuIds 拥有的菜单列表
     * @return 菜单树
     */
    private List<MenuVO> generateMenuTree(String language, List<String> menuIds) {
        MenuDOExample menuDOExample = new MenuDOExample();
        menuDOExample.setOrderByClause("sort DESC");
        menuDOExample.createCriteria()
                .andParentUuidEqualTo("root")
                .andUuidIn(menuIds)
                .andIsMenuEqualTo(true)
                .andIsShowEqualTo(true)
                .andIsDeleteEqualTo(false);
        List<MenuDO> menuDOS = menuDOMapper.selectByExample(menuDOExample);
        return getMenuVOS(language, menuIds, menuDOS);
    }

    /**
     * 获取菜单树
     *
     * @return 菜单树
     */
    private List<MenuVO> generateAllMenuTree() {
        MenuDOExample menuDOExample = new MenuDOExample();
        menuDOExample.setOrderByClause("sort DESC");
        menuDOExample.createCriteria()
                .andParentUuidEqualTo("root");
        List<MenuDO> menuDOS = menuDOMapper.selectByExample(menuDOExample);
        return getAllMenuVOS(menuDOS);
    }

    /**
     * 递归获取子菜单
     *
     * @param parent  父级ID
     * @param menuIds 拥有的菜单列表
     * @return 子级菜单
     */
    private List<MenuVO> generateMenuChildrenTree(String language, String parent, List<String> menuIds) {
        MenuDOExample menuDOExample = new MenuDOExample();
        menuDOExample.setOrderByClause("sort DESC");
        menuDOExample.createCriteria()
                .andParentUuidEqualTo(parent)
                .andUuidIn(menuIds)
                .andIsMenuEqualTo(true)
                .andIsShowEqualTo(true)
                .andIsDeleteEqualTo(false);
        List<MenuDO> menuDOS = menuDOMapper.selectByExample(menuDOExample);
        if (BeanUtils.isEmpty(menuDOS)) {
            return null;
        } else {
            return getMenuVOS(language, menuIds, menuDOS);
        }
    }

    /**
     * 递归获取子菜单
     *
     * @param parent  父级ID
     * @return 子级菜单
     */
    private List<MenuVO> generateAllMenuChildrenTree(String parent) {
        MenuDOExample menuDOExample = new MenuDOExample();
        menuDOExample.setOrderByClause("sort DESC");
        menuDOExample.createCriteria()
                .andParentUuidEqualTo(parent);
        List<MenuDO> menuDOS = menuDOMapper.selectByExample(menuDOExample);
        if (BeanUtils.isEmpty(menuDOS)) {
            return null;
        } else {
            return getAllMenuVOS(menuDOS);
        }
    }

    private List<MenuVO> getAllMenuVOS(List<MenuDO> menuDOS) {
        List<MenuVO> menuVOS = new ArrayList<>();
        for (MenuDO menuDO : menuDOS
        ) {
            MenuVO menuVO = convert(menuDO);
            List<MenuVO> menuVOSChildren = generateAllMenuChildrenTree(menuDO.getUuid());
            menuVO.setChildren(menuVOSChildren);
            menuVOS.add(menuVO);
        }
        return menuVOS;
    }

    private List<MenuVO> getMenuVOS(String language, List<String> menuIds, List<MenuDO> menuDOS) {
        List<MenuVO> menuVOS = new ArrayList<>();
        for (MenuDO menuDO : menuDOS
        ) {
            MenuVO menuVO = convert(language, menuDO);
            List<MenuVO> menuVOSChildren = generateMenuChildrenTree(language, menuDO.getUuid(), menuIds);
            if (!BeanUtils.isEmpty(menuVOSChildren)) {
                menuVO.setIcon("mdi-chevron-up");
                menuVO.setIcondown("mdi-chevron-down");
            }
            menuVO.setChildren(menuVOSChildren);
            menuVOS.add(menuVO);
        }
        return menuVOS;
    }

    private MenuVO convert(String language, MenuDO menuDO) {
        return Builder.of(MenuVO::new)
                .with(MenuVO::setIcon, menuDO.getIcon())
                .with(MenuVO::setHref, menuDO.getHref())
                .with(MenuVO::setText, getText(language, menuDO))
                .build();
    }

    private MenuVO convert(MenuDO menuDO) {
        return Builder.of(MenuVO::new)
                .with(MenuVO::setId, menuDO.getId())
                .with(MenuVO::setUuid, menuDO.getUuid())
                .with(MenuVO::setIcon, menuDO.getIcon())
                .with(MenuVO::setHref, menuDO.getHref())
                .with(MenuVO::setText, menuDO.getName())
                .build();
    }

    private String getText(String language, MenuDO menuDO) {
        if (BeanUtils.isEmpty(menuDO.getI18n())) {
            return menuDO.getName();
        } else {
            return i18nMessageService.getMessage(language, menuDO.getI18n(), menuDO.getName());
        }
    }
}

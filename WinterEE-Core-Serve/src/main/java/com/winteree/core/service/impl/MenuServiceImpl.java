package com.winteree.core.service.impl;

import com.winteree.api.entity.MenuVO;
import com.winteree.api.exception.FailureException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.MenuDOMapper;
import com.winteree.core.dao.entity.MenuDO;
import com.winteree.core.dao.entity.MenuDOExample;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.*;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>Title: MenuServiceImpl</p>
 * <p>Description: 菜单服务</p>
 *
 * @author RenFei
 * @date : 2020-04-18 16:16
 */
@Slf4j
@Service
public class MenuServiceImpl extends BaseService implements MenuService {
    private final RoleService roleService;
    private final MenuDOMapper menuDOMapper;
    private final I18nMessageService i18nMessageService;
    private final AccountService accountService;

    protected MenuServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                              AccountService accountService,
                              RoleService roleService,
                              MenuDOMapper menuDOMapper,
                              I18nMessageService i18nMessageService) {
        super(wintereeCoreConfig);
        this.roleService = roleService;
        this.menuDOMapper = menuDOMapper;
        this.i18nMessageService = i18nMessageService;
        this.accountService = accountService;
    }

    /**
     * 获取登录用户的菜单列表，注意不是菜单管理中的查询菜单列表
     *
     * @return
     */
    @Override
    public List<MenuVO> getMenuListBySignedUser(String language) {
        if (BeanUtils.isEmpty(language)) {
            language = "zh-CN";
        }
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 平台超级管理员，直接加载全部菜单
            List<String> menuIds = getAllMenuId();
            return generateMenuTree(language, menuIds);
        } else {
            // 查询用户所属的角色
            List<String> roles = roleService.selectRoleUuidByUserUuid(accountDTO.getUuid());
            // 根据角色查询拥有的菜单
            List<String> menuIds = roleService.getMenuUuidByRoleUuid(roles);
            return generateMenuTree(language, menuIds);
        }
    }

    /**
     * 获取登录用户的菜单和权限列表，注意不是菜单管理中的查询菜单列表
     *
     * @param language 语言
     * @return
     */
    @Override
    public List<MenuVO> getMenuAndAuthorityListBySignedUser(String language) {
        if (BeanUtils.isEmpty(language)) {
            language = "zh-CN";
        }
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 平台超级管理员，直接加载全部菜单
            List<String> menuIds = getAllMenuId();
            return generateMenuTreeAndAuthority(language, menuIds);
        } else {
            // 查询用户所属的角色
            List<String> roles = roleService.selectRoleUuidByUserUuid(accountDTO.getUuid());
            // 根据角色查询拥有的菜单
            List<String> menuIds = roleService.getMenuUuidByRoleUuid(roles);
            return generateMenuTreeAndAuthority(language, menuIds);
        }
    }

    /**
     * 获取所有菜单
     *
     * @return
     */
    @Override
    public List<MenuVO> getAllMenuTree() {
        return generateAllMenuTree();
    }

    @Override
    public List<MenuVO> getAllMenuList() {
        MenuDOExample menuDOExample = new MenuDOExample();
        menuDOExample.createCriteria()
                .andIsMenuEqualTo(true);
        List<MenuDO> menuDOS = menuDOMapper.selectByExample(menuDOExample);
        List<MenuVO> menuVOS = new ArrayList<>();
        menuVOS.add(Builder.of(MenuVO::new)
                .with(MenuVO::setUuid, "root")
                .with(MenuVO::setText, "System Root")
                .build());
        if (menuDOS != null) {
            for (MenuDO menuDO : menuDOS
            ) {
                menuVOS.add(convert(menuDO));
            }
        }
        return menuVOS;
    }

    @Override
    public int deleteMenuByUuid(String uuid) throws FailureException {
        try {
            MenuDOExample menuDOExample = new MenuDOExample();
            menuDOExample.createCriteria()
                    .andUuidEqualTo(uuid);
            return menuDOMapper.deleteByExample(menuDOExample);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("");
        }
    }

    @Override
    public MenuVO getMenuByUuid(String uuid) throws FailureException {
        try {
            MenuDOExample menuDOExample = new MenuDOExample();
            menuDOExample.createCriteria()
                    .andUuidEqualTo(uuid);
            MenuDO menuDO = ListUtils.getOne(menuDOMapper.selectByExample(menuDOExample));
            if (menuDO == null) {
                throw new FailureException("Failure");
            }
            return convert(menuDO);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("Failure");
        }
    }

    @Override
    public int updateMenu(MenuVO menuVO) throws FailureException {
        MenuDO menuDO = convert(menuVO);
        MenuDOExample menuDOExample = new MenuDOExample();
        menuDOExample.createCriteria()
                .andUuidEqualTo(menuVO.getUuid());
        try {
            return menuDOMapper.updateByExampleSelective(menuDO, menuDOExample);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("Failure");
        }
    }

    @Override
    public int addMenu(MenuVO menuVO) throws FailureException {
        MenuDO menuDO = convert(menuVO);
        try {
            menuDO.setUuid(UUID.randomUUID().toString());
            menuDO.setCreateTime(new Date());
            menuDO.setCreateBy(getSignedUser(accountService).getUuid());
            if (menuDO.getSort() == null) {
                menuDO.setSort(0L);
            }
            return menuDOMapper.insertSelective(menuDO);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new FailureException("Failure");
        }
    }

    /**
     * 获取所有菜单ID
     *
     * @return 所有菜单ID
     */
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
        return getMenuVOS(language, menuIds, menuDOS, false);
    }

    /**
     * 获取菜单树
     *
     * @param menuIds 拥有的菜单列表
     * @return 菜单树
     */
    private List<MenuVO> generateMenuTreeAndAuthority(String language, List<String> menuIds) {
        MenuDOExample menuDOExample = new MenuDOExample();
        menuDOExample.setOrderByClause("sort DESC");
        menuDOExample.createCriteria()
                .andParentUuidEqualTo("root")
                .andUuidIn(menuIds)
                .andIsShowEqualTo(true)
                .andIsDeleteEqualTo(false);
        List<MenuDO> menuDOS = menuDOMapper.selectByExample(menuDOExample);
        return getMenuVOS(language, menuIds, menuDOS, true);
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
    private List<MenuVO> generateMenuChildrenTree(String language, String parent, List<String> menuIds, boolean haveAuth) {
        MenuDOExample menuDOExample = new MenuDOExample();
        menuDOExample.setOrderByClause("sort DESC");
        MenuDOExample.Criteria criteria = menuDOExample.createCriteria();
        if (!haveAuth) {
            criteria.andIsMenuEqualTo(true);
        }
        criteria
                .andParentUuidEqualTo(parent)
                .andUuidIn(menuIds)
                .andIsShowEqualTo(true)
                .andIsDeleteEqualTo(false);
        List<MenuDO> menuDOS = menuDOMapper.selectByExample(menuDOExample);
        if (BeanUtils.isEmpty(menuDOS)) {
            return null;
        } else {
            return getMenuVOS(language, menuIds, menuDOS, haveAuth);
        }
    }

    /**
     * 递归获取子菜单
     *
     * @param parent 父级ID
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

    private List<MenuVO> getMenuVOS(String language, List<String> menuIds, List<MenuDO> menuDOS, boolean haveAuth) {
        List<MenuVO> menuVOS = new ArrayList<>();
        for (MenuDO menuDO : menuDOS
        ) {
            MenuVO menuVO = convert(language, menuDO);
            List<MenuVO> menuVOSChildren = generateMenuChildrenTree(language, menuDO.getUuid(), menuIds, haveAuth);
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
                .with(MenuVO::setUuid, menuDO.getUuid())
                .with(MenuVO::setIcon, menuDO.getIcon())
                .with(MenuVO::setHref, menuDO.getHref())
                .with(MenuVO::setText, getText(language, menuDO))
                .build();
    }

    public static MenuVO convert(MenuDO menuDO) {
        return Builder.of(MenuVO::new)
                .with(MenuVO::setId, menuDO.getId())
                .with(MenuVO::setUuid, menuDO.getUuid())
                .with(MenuVO::setIcon, menuDO.getIcon())
                .with(MenuVO::setHref, menuDO.getHref())
                .with(MenuVO::setText, menuDO.getName())
                .with(MenuVO::setI18n, menuDO.getI18n())
                .with(MenuVO::setIsDelete, menuDO.getIsDelete())
                .with(MenuVO::setIsMenu, menuDO.getIsMenu())
                .with(MenuVO::setIsShow, menuDO.getIsShow())
                .with(MenuVO::setParentUuid, menuDO.getParentUuid())
                .with(MenuVO::setPermission, menuDO.getPermission())
                .with(MenuVO::setRemarks, menuDO.getRemarks())
                .with(MenuVO::setSort, menuDO.getSort())
                .with(MenuVO::setTarget, menuDO.getTarget())
                .build();
    }

    private String getText(String language, MenuDO menuDO) {
        if (BeanUtils.isEmpty(menuDO.getI18n())) {
            return menuDO.getName();
        } else {
            return i18nMessageService.getMessage(language, menuDO.getI18n(), menuDO.getName());
        }
    }

    private MenuDO convert(MenuVO menuVO) {
        MenuDO menuDO = Builder.of(MenuDO::new)
                .with(MenuDO::setUuid, menuVO.getUuid())
                .with(MenuDO::setHref, menuVO.getHref())
                .with(MenuDO::setI18n, menuVO.getI18n())
                .with(MenuDO::setIcon, menuVO.getIcon())
                .with(MenuDO::setIsDelete, menuVO.getIsDelete())
                .with(MenuDO::setIsMenu, menuVO.getIsMenu())
                .with(MenuDO::setIsShow, menuVO.getIsShow())
                .with(MenuDO::setName, menuVO.getText())
                .with(MenuDO::setParentUuid, menuVO.getParentUuid())
                .with(MenuDO::setPermission, menuVO.getPermission())
                .with(MenuDO::setRemarks, menuVO.getRemarks())
                .with(MenuDO::setSort, menuVO.getSort())
                .with(MenuDO::setTarget, menuVO.getTarget())
                .with(MenuDO::setUpdateBy, getSignedUser(accountService).getUuid())
                .with(MenuDO::setUpdateTime, new Date())
                .build();
        return menuDO;
    }
}

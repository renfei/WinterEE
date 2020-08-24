package com.winteree.core.service.impl;

import com.winteree.api.entity.DataScopeEnum;
import com.winteree.api.entity.MenuVO;
import com.winteree.api.entity.OrgEnum;
import com.winteree.api.entity.RoleDTO;
import com.winteree.api.exception.FailureException;
import com.winteree.api.exception.ForbiddenException;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.*;
import com.winteree.core.dao.entity.*;
import com.winteree.core.entity.AccountDTO;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.RoleService;
import com.winteree.core.service.TenantService;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>Title: RoleServiceImpl</p>
 * <p>Description: 角色服务</p>
 *
 * @author RenFei
 * @date : 2020-04-20 20:48
 */
@Service
public class RoleServiceImpl extends BaseService implements RoleService {
    private final AccountService accountService;
    private final TenantService tenantService;
    private final RoleDOMapper roleDOMapper;
    private final UserRoleDOMapper userRoleDOMapper;
    private final RoleMenuDOMapper roleMenuDOMapper;
    private final OrganizationDOMapper organizationDOMapper;
    private final MenuDOMapper menuDOMapper;

    protected RoleServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                              AccountService accountService,
                              TenantService tenantService,
                              RoleDOMapper roleDOMapper,
                              UserRoleDOMapper userRoleDOMapper,
                              RoleMenuDOMapper roleMenuDOMapper,
                              OrganizationDOMapper organizationDOMapper,
                              MenuDOMapper menuDOMapper) {
        super(wintereeCoreConfig);
        this.accountService = accountService;
        this.tenantService = tenantService;
        this.roleDOMapper = roleDOMapper;
        this.userRoleDOMapper = userRoleDOMapper;
        this.roleMenuDOMapper = roleMenuDOMapper;
        this.organizationDOMapper = organizationDOMapper;
        this.menuDOMapper = menuDOMapper;
    }

    /**
     * 根据UUID获取角色对象
     *
     * @param uuid UUID
     * @return 角色对象
     */
    @Override
    public RoleDO getRoleByUuid(String uuid) {
        RoleDOExample example = new RoleDOExample();
        example.createCriteria().andUuidEqualTo(uuid);
        return ListUtils.getOne(roleDOMapper.selectByExample(example));
    }

    /**
     * 获取角色列表
     *
     * @param tenantUuid 租户ID
     * @return 角色列表
     */
    @Override
    public List<RoleDTO> getRoleList(String tenantUuid) {
        AccountDTO accountDTO = getSignedUser(accountService);
        RoleDOExample roleDOExample = new RoleDOExample();
        RoleDOExample.Criteria criteria = roleDOExample.createCriteria();
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 如果是超管，直接获取所有角色列表，可以跨租户
        } else {
            // 不是超管，不可跨租户
            tenantUuid = accountDTO.getTenantUuid();
            criteria.andTenantUuidEqualTo(tenantUuid);
            //校验数据范围的权限
            DataScopeEnum dataScopeEnum = this.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    break;
                case COMPANY:
                    // 只能查看自己公司的
                    criteria.andOfficeUuidEqualTo(accountDTO.getOfficeUuid());
                    break;
                default:
                    // 其他数据范围的不让查看
                    return null;
            }
        }
        List<RoleDO> roleDOS = roleDOMapper.selectByExample(roleDOExample);
        return fillMenu(roleDOS);
    }

    /**
     * 添加角色
     *
     * @param roleDTO 角色信息传输对象
     * @return 结果
     */
    @Override
    public int addRole(RoleDTO roleDTO) throws ForbiddenException, FailureException {
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 如果是超管，完全信任
            if (BeanUtils.isEmpty(roleDTO.getTenantUuid())) {
                if ("".equals(roleDTO.getOfficeUuid())) {
                    roleDTO.setOfficeUuid(null);
                }
            }
        } else {
            // 不是超管，不可跨租户,检查权限
            roleDTO.setTenantUuid(accountDTO.getTenantUuid());
            //校验数据范围的权限
            DataScopeEnum dataScopeEnum = this.getDataScope();
            switch (dataScopeEnum) {
                case ALL:
                    if ("".equals(roleDTO.getOfficeUuid())) {
                        roleDTO.setOfficeUuid(null);
                    }
                    break;
                case COMPANY:
                    // 只能添加自己公司的
                    roleDTO.setOfficeUuid(accountDTO.getOfficeUuid());
                    // 只能添加小于自己权限的角色，防止权限泄露，数字越小权限越大
                    if (roleDTO.getDataScope() < dataScopeEnum.value()) {
                        roleDTO.setDataScope(dataScopeEnum.value());
                    }
                    break;
                default:
                    // 其他数据范围的不允许添加
                    throw new ForbiddenException(StateCode.Forbidden.getDescribe());
            }
        }
        RoleDO roleDO = new RoleDO();
        roleDO.setUuid(UUID.randomUUID().toString().toUpperCase());
        roleDO.setTenantUuid(roleDTO.getTenantUuid());
        roleDO.setOfficeUuid(roleDTO.getOfficeUuid());
        roleDO.setName(roleDTO.getName());
        roleDO.setDataScope(roleDTO.getDataScope());
        roleDO.setUseable(true);
        roleDO.setCreateBy(accountDTO.getUuid());
        roleDO.setCreateTime(new Date());
        roleDO.setUpdateBy(accountDTO.getUuid());
        roleDO.setUpdateTime(new Date());
        roleDO.setRemarks(roleDTO.getRemarks());
        roleDO.setDelFlag("0");
        int insertRows = roleDOMapper.insertSelective(roleDO);
        if (insertRows > 0) {
            // 查询用户所属的角色
            List<String> roles = this.selectRoleUuidByUserUuid(accountDTO.getUuid());
            // 根据角色查询拥有的菜单
            List<String> menuIds = this.getMenuUuidByRoleUuid(roles);
            for (String uuid : roleDTO.getMenuUuid()
            ) {
                if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                    // 如果是超管，不做检查，直接信任
                    RoleMenuDO roleMenuDO = new RoleMenuDO();
                    roleMenuDO.setRoleUuid(roleDO.getUuid());
                    roleMenuDO.setMenuUuid(uuid);
                    roleMenuDO.setUuid(UUID.randomUUID().toString().toUpperCase());
                    roleMenuDOMapper.insertSelective(roleMenuDO);
                } else {
                    // 检查用户是否拥有该菜单权限，防止权限泄露
                    boolean have = false;
                    for (String menuId : menuIds
                    ) {
                        if (menuId.equals(uuid)) {
                            have = true;
                            break;
                        }
                    }
                    if (have) {
                        RoleMenuDO roleMenuDO = new RoleMenuDO();
                        roleMenuDO.setRoleUuid(roleDO.getUuid());
                        roleMenuDO.setMenuUuid(uuid);
                        roleMenuDO.setUuid(UUID.randomUUID().toString().toUpperCase());
                        roleMenuDOMapper.insertSelective(roleMenuDO);
                    }
                }
            }
            return 1;
        } else {
            throw new FailureException(StateCode.Failure.getDescribe());
        }
    }

    /**
     * 修改角色
     *
     * @param roleDTO 角色传输对象
     * @return 结果
     */
    @Override
    public int updateRole(RoleDTO roleDTO) throws ForbiddenException, FailureException {
        RoleDO oldRole = this.getRoleByUuid(roleDTO.getUuid());
        if (BeanUtils.isEmpty(oldRole)) {
            throw new FailureException(StateCode.Failure.getDescribe());
        } else {
            AccountDTO accountDTO = getSignedUser(accountService);
            if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                // 如果是超管，完全信任
                if ("".equals(roleDTO.getOfficeUuid())) {
                    roleDTO.setOfficeUuid(null);
                }
            } else {
                if (accountDTO.getTenantUuid().equals(roleDTO.getTenantUuid())) {
                    //校验数据范围的权限
                    DataScopeEnum dataScopeEnum = this.getDataScope();
                    switch (dataScopeEnum) {
                        case ALL:
                            if ("".equals(roleDTO.getOfficeUuid())) {
                                roleDTO.setOfficeUuid(null);
                            }
                            break;
                        case COMPANY:
                            if (accountDTO.getOfficeUuid().equals(roleDTO.getOfficeUuid())) {
                                // 只能编辑自己公司的
                            } else {
                                // 无权编辑其他公司的角色
                                throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                            }
                            break;
                        default:
                            // 其他数据范围的不允许添加
                            throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                    }
                } else {
                    // 无权跨租户编辑
                    throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                }
                // 检查分配的权限是否是登陆账户拥有的，防止权限泄露
                // 查询用户所属的角色
                List<String> roles = this.selectRoleUuidByUserUuid(accountDTO.getUuid());
                // 根据角色查询拥有的菜单
                List<String> menuIds = this.getMenuUuidByRoleUuid(roles);
                for (String uuid : roleDTO.getMenuUuid()
                ) {
                    boolean have = false;
                    for (String menuId : menuIds
                    ) {
                        if (menuId.equals(uuid)) {
                            have = true;
                            break;
                        }
                    }
                    if (!have) {
                        // 不允许分配自己没有的权限
                        throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                    }
                }
            }
            oldRole.setTenantUuid(roleDTO.getTenantUuid());
            oldRole.setOfficeUuid(roleDTO.getOfficeUuid());
            oldRole.setName(roleDTO.getName());
            oldRole.setDataScope(roleDTO.getDataScope());
            oldRole.setUseable(roleDTO.getUseable());
            oldRole.setUpdateBy(accountDTO.getUuid());
            oldRole.setUpdateTime(new Date());
            oldRole.setRemarks(roleDTO.getRemarks());
            RoleDOExample example = new RoleDOExample();
            example.createCriteria().andUuidEqualTo(oldRole.getUuid());
            int updateRows = roleDOMapper.updateByExample(oldRole, example);
            if (updateRows > 0) {
                //先删除旧的，再添加新的
                RoleMenuDOExample roleMenuDOExample = new RoleMenuDOExample();
                roleMenuDOExample.createCriteria().andRoleUuidEqualTo(oldRole.getUuid());
                roleMenuDOMapper.deleteByExample(roleMenuDOExample);
                if (!BeanUtils.isEmpty(roleDTO.getMenuUuid())) {
                    for (String uuid : roleDTO.getMenuUuid()
                    ) {
                        RoleMenuDO roleMenuDO = new RoleMenuDO();
                        roleMenuDO.setRoleUuid(oldRole.getUuid());
                        roleMenuDO.setMenuUuid(uuid);
                        roleMenuDO.setUuid(UUID.randomUUID().toString().toUpperCase());
                        roleMenuDOMapper.insertSelective(roleMenuDO);
                    }
                }
                return 1;
            } else {
                throw new FailureException(StateCode.Failure.getDescribe());
            }
        }
    }

    /**
     * 删除角色
     *
     * @param uuid 角色ID
     * @return 删除结果
     */
    @Override
    public int deleteRole(String uuid) throws ForbiddenException, FailureException {
        RoleDO oldRole = this.getRoleByUuid(uuid);
        if (BeanUtils.isEmpty(oldRole)) {
            throw new FailureException(StateCode.Failure.getDescribe());
        } else {
            AccountDTO accountDTO = getSignedUser(accountService);
            if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
                // 如果是超管，完全信任，执行删除
                execDelete(oldRole);
            } else {
                if (accountDTO.getTenantUuid().equals(oldRole.getTenantUuid())) {
                    //校验数据范围的权限
                    DataScopeEnum dataScopeEnum = this.getDataScope();
                    switch (dataScopeEnum) {
                        case ALL:
                            execDelete(oldRole);
                            break;
                        case COMPANY:
                            if (accountDTO.getOfficeUuid().equals(oldRole.getOfficeUuid())) {
                                // 只能删除自己公司的
                                execDelete(oldRole);
                            } else {
                                // 无权删除其他公司的角色
                                throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                            }
                            break;
                        default:
                            // 其他数据范围的不允许删除
                            throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                    }
                } else {
                    // 无权跨租户删除
                    throw new ForbiddenException(StateCode.Forbidden.getDescribe());
                }
            }
            return 1;
        }
    }

    /**
     * 根据角色ID列表获取菜单UUID列表
     *
     * @param roleUuid 角色ID列表
     * @return
     */
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

    /**
     * 获取数据权限范围（取最大值）
     *
     * @return 数据权限范围
     */
    @Override
    public DataScopeEnum getDataScope() {
        AccountDTO accountDTO = getSignedUser(accountService);
        if (wintereeCoreConfig.getRootAccount().equals(accountDTO.getUuid())) {
            // 如果是超管，直接最大权限
            return DataScopeEnum.ALL;
        } else {
            List<String> roleIds = this.selectRoleUuidByUserUuid(accountDTO.getUuid());
            if (BeanUtils.isEmpty(roleIds)) {
                return DataScopeEnum.DEPARTMENT;
            }
            RoleDOExample roleDOExample = new RoleDOExample();
            roleDOExample.createCriteria().andUuidIn(roleIds);
            List<RoleDO> roleDOS = roleDOMapper.selectByExample(roleDOExample);
            if (BeanUtils.isEmpty(roleDOS)) {
                return DataScopeEnum.DEPARTMENT;
            }
            DataScopeEnum maxDataScopeEnum = DataScopeEnum.DEPARTMENT;
            for (RoleDO role : roleDOS
            ) {
                if (role.getDataScope() != null && role.getDataScope() > maxDataScopeEnum.value()) {
                    maxDataScopeEnum = DataScopeEnum.valueOf(role.getDataScope());
                }
            }
            return maxDataScopeEnum;
        }
    }

    /**
     * 获取数据权限范围（取最大值）
     *
     * @param accountUuid 账户UUID
     * @return
     */
    @Override
    public DataScopeEnum getDataScope(String accountUuid) {
        if (wintereeCoreConfig.getRootAccount().equals(accountUuid)) {
            // 如果是超管，直接最大权限
            return DataScopeEnum.ALL;
        } else {
            List<String> roleIds = this.selectRoleUuidByUserUuid(accountUuid);
            if (BeanUtils.isEmpty(roleIds)) {
                return DataScopeEnum.DEPARTMENT;
            }
            RoleDOExample roleDOExample = new RoleDOExample();
            roleDOExample.createCriteria().andUuidIn(roleIds);
            List<RoleDO> roleDOS = roleDOMapper.selectByExample(roleDOExample);
            if (BeanUtils.isEmpty(roleDOS)) {
                return DataScopeEnum.DEPARTMENT;
            }
            DataScopeEnum maxDataScopeEnum = DataScopeEnum.DEPARTMENT;
            for (RoleDO role : roleDOS
            ) {
                if (role.getDataScope() != null && role.getDataScope() > maxDataScopeEnum.value()) {
                    maxDataScopeEnum = DataScopeEnum.valueOf(role.getDataScope());
                }
            }
            return maxDataScopeEnum;
        }
    }

    /**
     * 根据用户UUID获取角色列表UUID
     *
     * @param userUuid 用户UUID
     * @return 角色列表UUID
     */
    @Override
    public List<String> selectRoleUuidByUserUuid(String userUuid) {
        UserRoleDOExample userRoleDOExample = new UserRoleDOExample();
        userRoleDOExample.createCriteria()
                .andAccountUuidEqualTo(userUuid);
        List<UserRoleDO> userRoleDOS = userRoleDOMapper.selectByExample(userRoleDOExample);
        List<String> roleUuid = new ArrayList<>();
        if (BeanUtils.isEmpty(userRoleDOS)) {
            return roleUuid;
        }
        for (UserRoleDO userRole : userRoleDOS
        ) {
            roleUuid.add(userRole.getRoleUuid());
        }
        return roleUuid;
    }

    /**
     * 删除角色的逻辑
     *
     * @param roleDO
     */
    private void execDelete(RoleDO roleDO) {
        // 先删除角色与用户的关联表
        UserRoleDOExample userRoleDOExample = new UserRoleDOExample();
        userRoleDOExample.createCriteria().andRoleUuidEqualTo(roleDO.getUuid());
        userRoleDOMapper.deleteByExample(userRoleDOExample);
        // 删除角色与菜单的关联表
        RoleMenuDOExample roleMenuDOExample = new RoleMenuDOExample();
        roleMenuDOExample.createCriteria().andRoleUuidEqualTo(roleDO.getUuid());
        roleMenuDOMapper.deleteByExample(roleMenuDOExample);
        // 删除角色表
        RoleDOExample roleDOExample = new RoleDOExample();
        roleDOExample.createCriteria().andUuidEqualTo(roleDO.getUuid());
        roleDOMapper.deleteByExample(roleDOExample);
    }

    /**
     * 填充所属菜单
     *
     * @param roleDOS 角色列表
     * @return 填充好菜单的角色传输对象列表
     */
    private List<RoleDTO> fillMenu(List<RoleDO> roleDOS) {
        if (BeanUtils.isEmpty(roleDOS)) {
            return null;
        }
        List<RoleDTO> roleDTOS = new ArrayList<>();
        for (RoleDO roleDo : roleDOS
        ) {
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(roleDo.getId());
            roleDTO.setUuid(roleDo.getUuid());
            roleDTO.setTenantUuid(roleDo.getTenantUuid());
            if (!BeanUtils.isEmpty(roleDo.getTenantUuid())) {
                TenantDO tenantDO = tenantService.getTenantDOByUUID(roleDo.getTenantUuid());
                if (tenantDO != null) {
                    roleDTO.setTenantName(tenantDO.getName());
                }
            } else {
                roleDTO.setTenantName("All");
            }
            if (!BeanUtils.isEmpty(roleDo.getOfficeUuid())) {
                OrganizationDOExample example = new OrganizationDOExample();
                example.createCriteria().andUuidEqualTo(roleDo.getOfficeUuid()).andOrgTypeEqualTo(OrgEnum.COMPANY.value());
                OrganizationDO organizationDO = ListUtils.getOne(organizationDOMapper.selectByExample(example));
                if (organizationDO != null) {
                    roleDTO.setOfficeName(organizationDO.getName());
                }
            }
            if (!BeanUtils.isEmpty(roleDo.getCreateBy())) {
                com.winteree.api.entity.AccountDTO accountDTO = accountService.getAccountById(roleDo.getCreateBy());
                if (accountDTO != null) {
                    roleDTO.setCreateByName(accountDTO.getUserName());
                }
            }
            if (!BeanUtils.isEmpty(roleDo.getUpdateBy())) {
                com.winteree.api.entity.AccountDTO accountDTO = accountService.getAccountById(roleDo.getUpdateBy());
                if (accountDTO != null) {
                    roleDTO.setUpdateByName(accountDTO.getUserName());
                }
            }
            roleDTO.setOfficeUuid(roleDo.getOfficeUuid());
            roleDTO.setName(roleDo.getName());
            roleDTO.setEnname(roleDo.getEnname());
            roleDTO.setRoleType(roleDo.getRoleType());
            roleDTO.setDataScope(roleDo.getDataScope());
            roleDTO.setUseable(roleDo.getUseable());
            roleDTO.setCreateBy(roleDo.getCreateBy());
            roleDTO.setCreateTime(roleDo.getCreateTime());
            roleDTO.setUpdateBy(roleDo.getUpdateBy());
            roleDTO.setUpdateTime(roleDo.getUpdateTime());
            roleDTO.setRemarks(roleDo.getRemarks());
            roleDTO.setDelFlag(roleDo.getDelFlag());
            RoleMenuDOExample roleMenuDOExample = new RoleMenuDOExample();
            roleMenuDOExample.createCriteria().andRoleUuidEqualTo(roleDo.getUuid());
            List<RoleMenuDO> roleMenuDOS = roleMenuDOMapper.selectByExample(roleMenuDOExample);
            if (!BeanUtils.isEmpty(roleMenuDOS)) {
                List<String> ids = new ArrayList<>();
                List<MenuVO> menuVOS = new ArrayList<>();
                for (RoleMenuDO roleMenu : roleMenuDOS
                ) {
                    MenuDOExample menuDOExample = new MenuDOExample();
                    menuDOExample.createCriteria()
                            .andUuidEqualTo(roleMenu.getMenuUuid());
                    MenuDO menuDO = ListUtils.getOne(menuDOMapper.selectByExample(menuDOExample));
                    ids.add(roleMenu.getMenuUuid());
                    if (menuDO != null) {
                        menuVOS.add(MenuServiceImpl.convert(menuDO));
                    }
                }
                roleDTO.setMenuUuid(ids);
                roleDTO.setMenuVos(menuVOS);
            }
            roleDTOS.add(roleDTO);
        }
        return roleDTOS;
    }
}

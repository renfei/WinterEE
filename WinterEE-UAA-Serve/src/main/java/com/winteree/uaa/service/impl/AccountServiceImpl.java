package com.winteree.uaa.service.impl;

import com.winteree.uaa.config.WintereeUaaConfig;
import com.winteree.uaa.dao.AccountDOMapper;
import com.winteree.uaa.dao.entity.AccountDO;
import com.winteree.uaa.dao.entity.AccountDOExample;
import com.winteree.uaa.service.AccountService;
import com.winteree.uaa.service.MenuService;
import com.winteree.uaa.service.RoleService;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 账号服务
 *
 * @authorenFei
 */
@Service
public class AccountServiceImpl implements AccountService {
    private static final String AUTHORITY_DELIMITERS = ",";
    private final AccountDOMapper accountDOMapper;
    private final WintereeUaaConfig wintereeUaaConfig;
    private final MenuService menuService;
    private final RoleService roleService;

    public AccountServiceImpl(AccountDOMapper accountDOMapper,
                              WintereeUaaConfig wintereeUaaConfig,
                              MenuService menuService,
                              RoleService roleService) {
        this.accountDOMapper = accountDOMapper;
        this.wintereeUaaConfig = wintereeUaaConfig;
        this.menuService = menuService;
        this.roleService = roleService;
    }

    /**
     * 根据用户名获取账号对象
     *
     * @param username 用户名
     * @return AccountDTO
     */
    @Override
    public AccountDO findAccountByUsername(String username) {
        AccountDOExample accountDOExample = new AccountDOExample();
        accountDOExample.createCriteria()
                .andUserNameEqualTo(username);
        return ListUtils.getOne(accountDOMapper.selectByExample(accountDOExample));
    }

    /**
     * 根据邮箱地址获取账号对象
     *
     * @param email 电子邮件
     * @return AccountDTO
     */
    @Override
    public AccountDO findAccountByEmail(String email) {
        AccountDOExample accountDOExample = new AccountDOExample();
        accountDOExample.createCriteria()
                .andEmailEqualTo(email);
        return ListUtils.getOne(accountDOMapper.selectByExample(accountDOExample));
    }

    /**
     * 根据手机号获取账号对象
     *
     * @param phone 手机号
     * @return AccountDTO
     */
    @Override
    public AccountDO findAccountByPhoneNumber(String phone) {
        AccountDOExample accountDOExample = new AccountDOExample();
        accountDOExample.createCriteria()
                .andPhoneEqualTo(phone);
        return ListUtils.getOne(accountDOMapper.selectByExample(accountDOExample));
    }

    @Override
    public int updateByPrimaryKeySelective(AccountDO accountDO) {
        accountDO.setUpdateTime(new Date());
        return accountDOMapper.updateByPrimaryKeySelective(accountDO);
    }

    /**
     * 获取用户的权限列表
     *
     * @param accountDO 账户实体
     * @return 权限列表
     */
    @Override
    public List<GrantedAuthority> getGrantedAuthority(AccountDO accountDO) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("signed,");
        if (wintereeUaaConfig.getRootAccountId().equals(accountDO.getUuid())) {
            // 如果是平台管理员，加载全部权限列表
            for (String permission : menuService.getAllMenuPermission()
            ) {
                stringBuilder.append(permission);
                stringBuilder.append(AUTHORITY_DELIMITERS);
            }
        } else {
            // 根据用户获得角色列表
            List<String> roles = roleService.selectRoleUuidByUserUuid(accountDO.getUuid());
            // 根据角色列表获得菜单列表，从而获得权限列表
            List<String> menuIds = menuService.getMenuUuidByRoleUuid(roles);
            for (String permission : menuService.getMenuPermissionByMenuUuid(menuIds)
            ) {
                stringBuilder.append(permission);
                stringBuilder.append(AUTHORITY_DELIMITERS);
            }
        }
        String authority = stringBuilder.toString();
        if (authority.endsWith(AUTHORITY_DELIMITERS)) {
            authority = authority.substring(0, authority.length() - 1);
        }
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authority);
    }
}

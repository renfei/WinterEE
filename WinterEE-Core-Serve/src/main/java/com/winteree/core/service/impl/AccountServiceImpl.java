package com.winteree.core.service.impl;

import com.winteree.api.entity.AccountDTO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.AccountDOMapper;
import com.winteree.core.dao.entity.AccountDO;
import com.winteree.core.dao.entity.AccountDOExample;
import com.winteree.core.service.AccountService;
import com.winteree.core.service.BaseService;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 账户数据服务
 *
 * @author RenFei
 */
@Service
public class AccountServiceImpl extends BaseService implements AccountService {
    private final AccountDOMapper accountDOMapper;

    protected AccountServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                 AccountDOMapper accountDOMapper) {
        super(wintereeCoreConfig);
        this.accountDOMapper = accountDOMapper;
    }


    @Override
    public AccountDTO getAccountById(String uuid) {
        if (!BeanUtils.isEmpty(uuid)) {
            AccountDOExample accountDoExample = new AccountDOExample();
            accountDoExample.createCriteria()
                    .andUuidEqualTo(uuid)
                    .andUserStatusGreaterThan(0);
            return getAccountDTO(accountDOMapper.selectByExample(accountDoExample));
        }
        return null;
    }

    @Override
    public AccountDTO getAccountIdByUserName(String username) {
        if (!BeanUtils.isEmpty(username)) {
            AccountDOExample accountDoExample = new AccountDOExample();
            accountDoExample.createCriteria()
                    .andUserNameEqualTo(username);
            return getAccountDTO(accountDOMapper.selectByExample(accountDoExample));
        }
        return null;
    }

    @Override
    public AccountDTO getAccountIdByEmail(String email) {
        if (!BeanUtils.isEmpty(email)) {
            AccountDOExample accountDoExample = new AccountDOExample();
            accountDoExample.createCriteria()
                    .andEmailEqualTo(email);
            return getAccountDTO(accountDOMapper.selectByExample(accountDoExample));
        }
        return null;
    }

    @Override
    public AccountDTO getAccountIdByPhone(String phone) {
        if (!BeanUtils.isEmpty(phone)) {
            AccountDOExample accountDoExample = new AccountDOExample();
            accountDoExample.createCriteria()
                    .andPhoneEqualTo(phone);
            return getAccountDTO(accountDOMapper.selectByExample(accountDoExample));
        }
        return null;
    }

    @Override
    public AccountDTO getAccountInfo() {
        com.winteree.core.entity.AccountDTO accountDTO = getSignedUser();
        return Builder.of(AccountDTO::new)
                .with(AccountDTO::setAuthorities, accountDTO.getAuthorities())
                .with(AccountDTO::setUserName, accountDTO.getUserName())
                .with(AccountDTO::setUuid, accountDTO.getUuid())
                .with(AccountDTO::setTenantUuid, accountDTO.getTenantUuid())
                .with(AccountDTO::setEmail, accountDTO.getEmail())
                .with(AccountDTO::setPhone, accountDTO.getPhone())
                .with(AccountDTO::setCreateTime, accountDTO.getCreateTime())
                .build();
    }

    /**
     * 获取当前登录的账户(账户服务私有方法，共有的在BaseService里)
     *
     * @return 当前登录的账户
     */
    private com.winteree.core.entity.AccountDTO getSignedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accountId = (String) authentication.getPrincipal();
        List<GrantedAuthority> grantedAuthorityList = (List<GrantedAuthority>) authentication.getAuthorities();
        com.winteree.api.entity.AccountDTO accountDTO = this.getAccountById(accountId);
        if (accountDTO != null) {
            List<String> authorities = null;
            if (!BeanUtils.isEmpty(grantedAuthorityList)) {
                authorities = new ArrayList<>();
                for (GrantedAuthority g : grantedAuthorityList
                ) {
                    authorities.add(g.getAuthority());
                }
            }
            return Builder.of(com.winteree.core.entity.AccountDTO::new)
                    .with(com.winteree.core.entity.AccountDTO::setUuid, accountDTO.getUuid())
                    .with(com.winteree.core.entity.AccountDTO::setCreateTime, accountDTO.getCreateTime())
                    .with(com.winteree.core.entity.AccountDTO::setUserName, accountDTO.getUserName())
                    .with(com.winteree.core.entity.AccountDTO::setEmail, accountDTO.getEmail())
                    .with(com.winteree.core.entity.AccountDTO::setPhone, accountDTO.getPhone())
                    .with(com.winteree.core.entity.AccountDTO::setPasswd, accountDTO.getPasswd())
                    .with(com.winteree.core.entity.AccountDTO::setUserStatus, accountDTO.getUserStatus())
                    .with(com.winteree.core.entity.AccountDTO::setLockTime, accountDTO.getLockTime())
                    .with(com.winteree.core.entity.AccountDTO::setUuid, accountDTO.getUuid())
                    .with(com.winteree.core.entity.AccountDTO::setTenantUuid, accountDTO.getTenantUuid())
                    .with(com.winteree.core.entity.AccountDTO::setAuthorities, authorities)
                    .build();
        } else {
            return null;
        }
    }

    private AccountDTO getAccountDTO(List<AccountDO> accountDOS) {
        AccountDO accountDO = ListUtils.getOne(accountDOS);
        if (!BeanUtils.isEmpty(accountDO)) {
            AccountDTO accountDTO = new AccountDTO();
            org.springframework.beans.BeanUtils.copyProperties(accountDO, accountDTO);
            return accountDTO;
        }
        return null;
    }
}

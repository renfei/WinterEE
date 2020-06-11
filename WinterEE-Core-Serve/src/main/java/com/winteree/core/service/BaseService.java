package com.winteree.core.service;

import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.entity.AccountDTO;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: BaseService</p>
 * <p>Description: 基础服务类</p>
 *
 * @author RenFei
 * @date : 2020-04-17 13:12
 */
public abstract class BaseService {
    protected final WintereeCoreConfig wintereeCoreConfig;

    protected BaseService(WintereeCoreConfig wintereeCoreConfig) {
        this.wintereeCoreConfig = wintereeCoreConfig;
    }

    /**
     * 获取当前登录的账户
     *
     * @return 当前登录的账户
     */
    protected AccountDTO getSignedUser(AccountService accountService) {
        if (accountService != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String accountId = (String) authentication.getPrincipal();
            List<GrantedAuthority> grantedAuthorityList = (List<GrantedAuthority>) authentication.getAuthorities();
            com.winteree.api.entity.AccountDTO accountDTO = accountService.getAccountById(accountId);
            if (accountDTO != null) {
                List<String> authorities = null;
                if (!BeanUtils.isEmpty(grantedAuthorityList)) {
                    authorities = new ArrayList<>();
                    for (GrantedAuthority g : grantedAuthorityList
                    ) {
                        authorities.add(g.getAuthority());
                    }
                }
                return Builder.of(AccountDTO::new)
                        .with(AccountDTO::setUuid, accountDTO.getUuid())
                        .with(AccountDTO::setCreateTime, accountDTO.getCreateTime())
                        .with(AccountDTO::setUserName, accountDTO.getUserName())
                        .with(AccountDTO::setEmail, accountDTO.getEmail())
                        .with(AccountDTO::setPhone, accountDTO.getPhone())
                        .with(AccountDTO::setPasswd, accountDTO.getPasswd())
                        .with(AccountDTO::setUserStatus, accountDTO.getUserStatus())
                        .with(AccountDTO::setLockTime, accountDTO.getLockTime())
                        .with(AccountDTO::setUuid, accountDTO.getUuid())
                        .with(AccountDTO::setAuthorities, authorities)
                        .build();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}

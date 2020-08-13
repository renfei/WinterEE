package com.winteree.core.service;

import com.winteree.api.entity.OAuthClientDTO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.entity.AccountDTO;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

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
    @Autowired
    private OAuthClientService oAuthClientService;

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
            if (accountId == null) {
                // 如果登陆用户没获取到的话
                try {
                    OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
                    if (oAuth2Authentication.getOAuth2Request().isApproved()) {
                        // 但是认证通过了，那么可能是客户端模式访问的，那么给他超管的身份
                        accountId = wintereeCoreConfig.getRootAccount();
                    }
                } catch (Exception exception) {
                    return null;
                }
            }
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
                        .with(AccountDTO::setTenantUuid, accountDTO.getTenantUuid())
                        .with(AccountDTO::setOfficeUuid, accountDTO.getOfficeUuid())
                        .with(AccountDTO::setDepartmentUuid, accountDTO.getDepartmentUuid())
                        .with(AccountDTO::setCreateTime, accountDTO.getCreateTime())
                        .with(AccountDTO::setUserName, accountDTO.getUserName())
                        .with(AccountDTO::setEmail, accountDTO.getEmail())
                        .with(AccountDTO::setPhone, accountDTO.getPhone())
                        .with(AccountDTO::setUserStatus, accountDTO.getUserStatus())
                        .with(AccountDTO::setLockTime, accountDTO.getLockTime())
                        .with(AccountDTO::setUuid, accountDTO.getUuid())
                        .with(AccountDTO::setAuthorities, authorities)
                        .build();
            } else {
                // 没有查到用户，那么可能是客户端模式访问的
                OAuthClientDTO oAuthClientDTO = oAuthClientService.getOAuthClientByClientId(accountId);
                if (oAuthClientDTO == null) {
                    return null;
                }
                // 客户端模式访问的
                if ("client_credentials".equals(oAuthClientDTO.getAuthorizedGrantTypes())) {
                    // 客户端模式访问给予超管权限
                    accountDTO = accountService.getAccountById(wintereeCoreConfig.getRootAccount());
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
                            .with(AccountDTO::setTenantUuid, accountDTO.getTenantUuid())
                            .with(AccountDTO::setOfficeUuid, accountDTO.getOfficeUuid())
                            .with(AccountDTO::setDepartmentUuid, accountDTO.getDepartmentUuid())
                            .with(AccountDTO::setCreateTime, accountDTO.getCreateTime())
                            .with(AccountDTO::setUserName, accountDTO.getUserName())
                            .with(AccountDTO::setEmail, accountDTO.getEmail())
                            .with(AccountDTO::setPhone, accountDTO.getPhone())
                            .with(AccountDTO::setUserStatus, accountDTO.getUserStatus())
                            .with(AccountDTO::setLockTime, accountDTO.getLockTime())
                            .with(AccountDTO::setUuid, accountDTO.getUuid())
                            .with(AccountDTO::setAuthorities, authorities)
                            .build();
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }
    }
}

package com.winteree.uaa.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * 验证码登录令牌颁发
 *
 * @author RenFei
 */
public class VerificationCodeCustomTokenGranter extends AbstractCustomTokenGranter {
    private CustomUserDetailsService userDetailsService;

    public VerificationCodeCustomTokenGranter(CustomUserDetailsService userDetailsService, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, "verification_code");
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected User getCustomUser(Map<String, String> parameters) {
        String phone = parameters.get("name");
        String smsCode = parameters.get("code");
        return userDetailsService.loadUserByVerificationCode(phone, smsCode);
    }
}

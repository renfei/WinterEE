package com.winteree.uaa.granter;

import com.winteree.uaa.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.security.core.userdetails.User;
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
    private final CustomUserDetailsServiceImpl userDetailsService;

    public VerificationCodeCustomTokenGranter(CustomUserDetailsServiceImpl userDetailsService, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, "verification_code");
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected User getCustomUser(Map<String, String> parameters) {
        String phone = parameters.get("name");
        String smsCode = parameters.get("code");
        String language = parameters.get("language");
        String keyId = parameters.get("keyid");
        return userDetailsService.loadUserByVerificationCode(phone, smsCode, language, keyId);
    }
}

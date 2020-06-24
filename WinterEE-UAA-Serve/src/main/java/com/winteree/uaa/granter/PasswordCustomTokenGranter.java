package com.winteree.uaa.granter;

import com.winteree.uaa.service.impl.CustomUserDetailsServiceImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * @author RenFei
 */
public class PasswordCustomTokenGranter extends AbstractCustomTokenGranter {
    private final CustomUserDetailsServiceImpl userDetailsService;

    public PasswordCustomTokenGranter(CustomUserDetailsServiceImpl userDetailsService, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, "auto_password");
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected User getCustomUser(Map<String, String> parameters) {
        String username = parameters.get("name");
        String password = parameters.get("password");
        String language = parameters.get("language");
        String keyId = parameters.get("keyid");
        return userDetailsService.loadUserByUsernameAndPassword(username, password, language, keyId);
    }
}

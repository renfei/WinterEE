package com.winteree.uaa.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;

import java.util.Map;

/**
 * @author RenFei
 */
public class PasswordCustomTokenGranter extends AbstractCustomTokenGranter {
    private CustomUserDetailsService userDetailsService;

    public PasswordCustomTokenGranter(CustomUserDetailsService userDetailsService, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory, "auto_password");
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected User getCustomUser(Map<String, String> parameters) {
        String username = parameters.get("name");
        String password = parameters.get("password");
        return userDetailsService.loadUserByUsernameAndPassword(username, password);
    }
}

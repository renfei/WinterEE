package com.winteree.uaa.service;

import com.winteree.uaa.config.AuthorizationServerConfiguration;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author RenFei
 */
@FrameworkEndpoint
public class RevokeTokenEndpoint {

    private final AuthorizationServerConfiguration authorizationServerConfiguration;

    public RevokeTokenEndpoint(AuthorizationServerConfiguration authorizationServerConfiguration) {
        this.authorizationServerConfiguration = authorizationServerConfiguration;
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/token")
    @ResponseBody
    public void revokeToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer")) {
            String tokenId = authorization.substring("Bearer".length() + 1);
            ((DefaultTokenServices) authorizationServerConfiguration.tokenServices()).revokeToken(tokenId);
        }
    }
}

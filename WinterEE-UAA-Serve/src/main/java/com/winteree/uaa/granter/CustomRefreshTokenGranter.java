package com.winteree.uaa.granter;

import org.springframework.security.oauth2.common.*;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;

import java.util.Date;
import java.util.UUID;

/**
 * Token 刷新
 *
 * @author RenFei
 */
public class CustomRefreshTokenGranter extends RefreshTokenGranter {

    private final int REFRESH_TOKEN_VALIDITY_SECONDS = 2592000;
    private final TokenStore tokenStore;
    private final boolean reuseRefreshToken;

    public CustomRefreshTokenGranter(boolean reuseRefreshToken, TokenStore tokenStore, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        super(tokenServices, clientDetailsService, requestFactory);
        this.reuseRefreshToken = reuseRefreshToken;
        this.tokenStore=tokenStore;
    }

    @Override
    protected OAuth2AccessToken getAccessToken(ClientDetails client, TokenRequest tokenRequest) {
        String refreshTokenValue = tokenRequest.getRequestParameters().get("refresh_token");
        OAuth2RefreshToken refreshToken = this.tokenStore.readRefreshToken(refreshTokenValue);
        if (refreshToken == null) {
            throw new InvalidGrantException("Invalid refresh token: " + refreshTokenValue);
        }
        OAuth2Authentication authentication = this.tokenStore.readAuthenticationForRefreshToken(refreshToken);
        String clientId = authentication.getOAuth2Request().getClientId();
        if (clientId != null && clientId.equals(tokenRequest.getClientId())) {
            tokenStore.removeAccessTokenUsingRefreshToken(refreshToken);
            if (this.isExpired(refreshToken)) {
                tokenStore.removeRefreshToken(refreshToken);
                throw new InvalidTokenException("Invalid refresh token (expired): " + refreshToken);
            } else {
                if (!reuseRefreshToken) {
                    tokenStore.removeRefreshToken(refreshToken);
                    Integer validity = client.getRefreshTokenValiditySeconds();
                    if (validity == null) {
                        validity = REFRESH_TOKEN_VALIDITY_SECONDS;
                    }
                    String value = UUID.randomUUID().toString();
                    refreshToken = (validity > 0 ? new DefaultExpiringOAuth2RefreshToken(value, new Date(System.currentTimeMillis() + (long)validity * 1000L)) : new DefaultOAuth2RefreshToken(value));
                }
                OAuth2AccessToken accessToken = getTokenServices().createAccessToken(authentication);
                ((DefaultOAuth2AccessToken)accessToken).setRefreshToken(refreshToken);
                this.tokenStore.storeAccessToken(accessToken, authentication);
                this.tokenStore.storeRefreshToken(accessToken.getRefreshToken(), authentication);
                return accessToken;
            }
        } else {
            throw new InvalidGrantException("Wrong client for this refresh token: " + refreshTokenValue);
        }
    }

    private boolean isExpired(OAuth2RefreshToken refreshToken) {
        if (!(refreshToken instanceof ExpiringOAuth2RefreshToken)) {
            return false;
        } else {
            ExpiringOAuth2RefreshToken expiringToken = (ExpiringOAuth2RefreshToken)refreshToken;
            return expiringToken.getExpiration() == null || System.currentTimeMillis() > expiringToken.getExpiration().getTime();
        }
    }
}

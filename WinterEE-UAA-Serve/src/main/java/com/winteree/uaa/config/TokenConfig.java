package com.winteree.uaa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

/**
 * TokenConfig
 *
 * @author RenFei
 */
@Configuration
public class TokenConfig {
    private final WintereeUaaConfig wintereeUaaConfig;

    public TokenConfig(WintereeUaaConfig wintereeUaaConfig) {
        this.wintereeUaaConfig = wintereeUaaConfig;
    }

    /**
     * 令牌存储策略
     *
     * @return
     */
    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(wintereeUaaConfig.getOauth2().getPrivateKey());
        converter.setVerifierKey(wintereeUaaConfig.getOauth2().getPublicKey());
        return converter;
    }
}

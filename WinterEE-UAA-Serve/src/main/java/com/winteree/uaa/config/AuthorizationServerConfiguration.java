package com.winteree.uaa.config;

import com.winteree.uaa.granter.CustomRefreshTokenGranter;
import com.winteree.uaa.granter.PasswordCustomTokenGranter;
import com.winteree.uaa.granter.VerificationCodeCustomTokenGranter;
import com.winteree.uaa.service.*;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.*;

/**
 * 认证服务器配置
 *
 * @author RenFei
 */
@Slf4j
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private TokenStore tokenStore;
    @Autowired
    private WintereeUaaConfig wintereeUaaConfig;
    @Autowired
    private JwtAccessTokenConverter accessTokenConverter;
    @Autowired
    private JdbcClientDetailsService jdbcClientDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .passwordEncoder(passwordEncoder)
                .allowFormAuthenticationForClients();
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        List<TokenGranter> tokenGranters = getTokenGranters(endpoints.getAuthorizationCodeServices(), tokenStore, tokenServices(), endpoints.getClientDetailsService(), endpoints.getOAuth2RequestFactory());
        endpoints.tokenGranter(new CompositeTokenGranter(tokenGranters))
                .allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenServices(tokenServices())
                .reuseRefreshTokens(false)
                .exceptionTranslator(new WebResponseExceptionTranslatorImpl());
    }

    private List<TokenGranter> getTokenGranters(AuthorizationCodeServices authorizationCodeServices, TokenStore tokenStore, AuthorizationServerTokenServices tokenServices, ClientDetailsService clientDetailsService, OAuth2RequestFactory requestFactory) {
        return new ArrayList<>(Arrays.asList(
                new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory),
                new CustomRefreshTokenGranter(false, tokenStore, tokenServices, clientDetailsService, requestFactory),
                new PasswordCustomTokenGranter(customUserDetailsService, tokenServices, clientDetailsService, requestFactory),
                new VerificationCodeCustomTokenGranter(customUserDetailsService, tokenServices, clientDetailsService, requestFactory)
        ));
    }

    @Bean
    public JdbcClientDetailsService jdbcClientDetailsService(DataSource dataSource) {
        JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
        clientDetailsService.setPasswordEncoder(passwordEncoder);
        return clientDetailsService;
    }

    /**
     * 令牌管理服务
     *
     * @return
     */
    @Bean
    @Primary
    public AuthorizationServerTokenServices tokenServices() {
        DefaultTokenServices service = new DefaultTokenServices();
        service.setClientDetailsService(jdbcClientDetailsService);
        service.setSupportRefreshToken(true);
        service.setTokenStore(tokenStore);
        //令牌增强
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(accessTokenConverter));
        service.setTokenEnhancer(tokenEnhancerChain);
        log.info("wintereeUaaConfig.getAccessTokenValiditySeconds({})", wintereeUaaConfig.getAccessTokenValiditySeconds());
        log.info("wintereeUaaConfig.getRefreshTokenValiditySeconds({})", wintereeUaaConfig.getRefreshTokenValiditySeconds());
        // 令牌默认有效期
        service.setAccessTokenValiditySeconds(wintereeUaaConfig.getAccessTokenValiditySeconds());
        // 刷新令牌默认有效期
        service.setRefreshTokenValiditySeconds(wintereeUaaConfig.getRefreshTokenValiditySeconds());
        return service;
    }

    /**
     * 密码编码器
     *
     * @return
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return PasswordUtils.verifyPassword((String) rawPassword, encodedPassword);
            }

            @Override
            public String encode(CharSequence rawPassword) {
                try {
                    return PasswordUtils.createHash((String) rawPassword);
                } catch (PasswordUtils.CannotPerformOperationException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }
}

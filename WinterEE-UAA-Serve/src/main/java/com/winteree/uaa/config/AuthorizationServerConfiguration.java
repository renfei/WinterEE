package com.winteree.uaa.config;

import com.winteree.uaa.granter.CustomRefreshTokenGranter;
import com.winteree.uaa.granter.PasswordCustomTokenGranter;
import com.winteree.uaa.granter.VerificationCodeCustomTokenGranter;
import com.winteree.uaa.service.impl.CustomUserDetailsServiceImpl;
import com.winteree.uaa.service.impl.WebResponseExceptionTranslatorImpl;
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
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.CompositeTokenGranter;
import org.springframework.security.oauth2.provider.OAuth2RequestFactory;
import org.springframework.security.oauth2.provider.TokenGranter;
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private JdbcClientDetailsService jdbcClientDetailsService;
    private final TokenStore tokenStore;
    private final WintereeUaaConfig wintereeUaaConfig;
    private final JwtAccessTokenConverter accessTokenConverter;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserDetailsServiceImpl customUserDetailsServiceImpl;

    public AuthorizationServerConfiguration(TokenStore tokenStore,
                                            WintereeUaaConfig wintereeUaaConfig,
                                            JwtAccessTokenConverter accessTokenConverter,
                                            PasswordEncoder passwordEncoder,
                                            CustomUserDetailsServiceImpl customUserDetailsServiceImpl) {
        this.tokenStore = tokenStore;
        this.wintereeUaaConfig = wintereeUaaConfig;
        this.accessTokenConverter = accessTokenConverter;
        this.passwordEncoder = passwordEncoder;
        this.customUserDetailsServiceImpl = customUserDetailsServiceImpl;
    }

    /**
     * 定义客户详细信息服务的配置器。客户端详细信息可以被初始化，或者您可以直接引用一个现有的存储。（client_id ，client_secret，redirect_uri 等配置信息）
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.withClientDetails(jdbcClientDetailsService);
    }

    /**
     * 配置令牌端点的安全约束
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()")
                .passwordEncoder(passwordEncoder);
    }

    /**
     * 配置授权以及令牌的访问端点和令牌服务（比如：配置令牌的签名与存储方式）
     * @param endpoints
     */
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
                // 客户端模式
                new ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory),
                // 授权码模式
                new AuthorizationCodeTokenGranter(tokenServices, authorizationCodeServices, clientDetailsService, requestFactory),
                // 刷新Token
                new CustomRefreshTokenGranter(false, tokenStore, tokenServices, clientDetailsService, requestFactory),
                // 密码模式（自定义）
                new PasswordCustomTokenGranter(customUserDetailsServiceImpl, tokenServices, clientDetailsService, requestFactory),
                // 验证码模式（自定义）
                new VerificationCodeCustomTokenGranter(customUserDetailsServiceImpl, tokenServices, clientDetailsService, requestFactory)
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

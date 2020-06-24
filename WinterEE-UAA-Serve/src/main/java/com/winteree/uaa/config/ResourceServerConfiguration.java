package com.winteree.uaa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * <p>Title: ResourceServerConfigurerAdapter</p>
 * <p>Description: ResourceServerConfigurerAdapter 配置类</p>
 * ResourceServerConfigurerAdapter 用于保护 OAuth2 要开放的资源，
 * 同时主要作用于client端以及token的认证(Bearer Auth)，
 * 由于后面 OAuth2 服务端后续还需要提供用户信息，所以也是一个 Resource Server
 *
 * @author RenFei
 * @date : 2020-04-15 13:57
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfiguration implements org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer {
    private static final String RESOURCE_ID = "WinterEE";
    private final TokenStore tokenStore;

    public ResourceServerConfiguration(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID)
                .tokenStore(tokenStore)
                .stateless(true);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/oauth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
    }
}

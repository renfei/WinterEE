package com.winteree.gateway.config;

import com.winteree.gateway.service.RoleBasedVoterImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.UnanimousBased;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.web.access.expression.WebExpressionVoter;

import java.util.Arrays;
import java.util.List;

/**
 * ResourceServerConfig
 *
 * @author RenFei
 */
@Configuration
public class ResourceServerConfig {
    public static final String RESOURCE_ID = "WinterEE";
    /**
     * UAA的资源配置
     */
    @Configuration
    @EnableResourceServer
    public static class UaaServerConfig extends ResourceServerConfigurerAdapter {
        private final TokenStore tokenStore;

        public UaaServerConfig(TokenStore tokenStore) {
            this.tokenStore = tokenStore;
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/uaa/**").permitAll();
        }
    }

    /**
     * Core的资源配置
     */
    @Configuration
    @EnableResourceServer
    public static class CoreServerConfig extends ResourceServerConfigurerAdapter {
        private final TokenStore tokenStore;

        public CoreServerConfig(TokenStore tokenStore) {
            this.tokenStore = tokenStore;
        }

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.tokenStore(tokenStore).resourceId(RESOURCE_ID).stateless(true);
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/*/v2/api-docs/**").permitAll()
                    .antMatchers("/core/account/check/**").permitAll()
                    .antMatchers("/core/secretkey/**").permitAll()
                    .antMatchers("/core/**").access("#oauth2.hasScope('WinterEE-Core-Serve')");
        }
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        List<AccessDecisionVoter<? extends Object>> decisionVoters
                = Arrays.asList(
                new WebExpressionVoter(),
                new RoleBasedVoterImpl(),
                new AuthenticatedVoter());
        return new UnanimousBased(decisionVoters);
    }
}

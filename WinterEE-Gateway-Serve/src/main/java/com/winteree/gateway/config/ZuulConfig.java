package com.winteree.gateway.config;

import com.winteree.gateway.client.WintereeUaaServiceClient;
import com.winteree.gateway.filter.AccessFilter;
import com.winteree.gateway.filter.AuthFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

/**
 * @author RenFei
 */
@Configuration
public class ZuulConfig {
    private final WintereeUaaServiceClient wintereeUaaServiceClient;

    public ZuulConfig(WintereeUaaServiceClient wintereeUaaServiceClient) {
        this.wintereeUaaServiceClient = wintereeUaaServiceClient;
    }

    @Bean
    public AuthFilter perFilter() {
        return new AuthFilter(wintereeUaaServiceClient);
    }

    @Bean
    public AccessFilter accessFilter() {
        return new AccessFilter();
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setMaxAge(18000L);
        source.registerCorsConfiguration("/**", configuration);
        CorsFilter corsFilter = new CorsFilter(source);
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(corsFilter);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}

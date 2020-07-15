package com.winteree.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Repository;

/**
 * CoreApplication
 *
 * @author RenFei
 */
@EnableAsync
@EnableOAuth2Sso
@EnableFeignClients
@SpringCloudApplication
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(prePostEnabled = true)
@MapperScan(basePackages = "com.winteree.core.dao", annotationClass = Repository.class)
public class CoreApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(CoreApplication.class);
    }
}

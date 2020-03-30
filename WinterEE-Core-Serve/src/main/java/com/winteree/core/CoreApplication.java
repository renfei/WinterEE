package com.winteree.core;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * CoreApplication
 *
 * @author RenFei
 */
@EnableAsync
@EnableOAuth2Sso
@SpringCloudApplication
@MapperScan(basePackages = "com.winteree.core.dao")
public class CoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoreApplication.class, args);
    }
}

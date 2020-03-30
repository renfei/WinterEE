package com.winteree.uaa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

/**
 * User Authorization Authentication
 *
 * @author RenFei
 */
@EnableAsync
@EnableResourceServer
@SpringCloudApplication
@MapperScan(basePackages = "com.winteree.uaa.dao")
public class UaaApplication {
    public static void main(String[] args) {
        SpringApplication.run(UaaApplication.class, args);
    }
}

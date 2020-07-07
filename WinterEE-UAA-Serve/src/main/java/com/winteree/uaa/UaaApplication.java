package com.winteree.uaa;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

/**
 * User Authorization Authentication
 *
 * @author RenFei
 */
@SpringCloudApplication
@EnableConfigurationProperties
@EnableAsync(proxyTargetClass = true)
@EnableFeignClients(basePackages = "com.winteree.uaa.client")
@MapperScan(basePackages = "com.winteree.uaa.dao", annotationClass = Repository.class)
public class UaaApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(UaaApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(UaaApplication.class);
    }
}

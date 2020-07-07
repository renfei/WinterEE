package com.winteree.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Config Application
 *
 * @author RenFei
 */
@EnableConfigServer
@SpringCloudApplication
public class ConfigApplication extends SpringBootServletInitializer  {

    public static void main(String[] args) {
        SpringApplication.run(ConfigApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ConfigApplication.class);
    }

}

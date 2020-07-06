package com.philisense.approve.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * <p>Title: YourConfig</p>
 * <p>Description: 你的配置类</p>
 *
 * @author RenFei
 * @date : 2020-06-21 18:44
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "approve")
public class YourConfig {
    private Oauth2 oauth2;

    @Data
    public static class Oauth2 {
        private String privateKey;
        private String publicKey;
    }
}

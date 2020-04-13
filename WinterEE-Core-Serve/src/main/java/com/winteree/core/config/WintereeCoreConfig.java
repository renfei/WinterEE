package com.winteree.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @author RenFei
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "winteree.core")
public class WintereeCoreConfig {
    private Oauth2 oauth2;
    private String systemname;
    private String totpseed;

    @Data
    public static class Oauth2{
        private String privateKey;
        private String publicKey;
    }
}

package com.winteree.uaa.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * UAA配置
 *
 * @author RenFei
 */
@Data
@Component
@RefreshScope
@ConfigurationProperties(prefix = "winteree.uaa")
public class WintereeUaaConfig {
    private String runMode;
    private String rootAccountId;
    private String schemaName;
    private Boolean enableSwagger;
    private Oauth2 oauth2;
    private Integer accessTokenValiditySeconds;
    private Integer refreshTokenValiditySeconds;

    @Data
    public static class Oauth2{
        private String privateKey;
        private String publicKey;
    }
}

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
    private String runMode;
    private String rootAccount;
    private Boolean enableEmail;
    private Boolean enableSMS;
    private Boolean enableSwagger;
    private Oauth2 oauth2;
    private Aliyun aliyun;
    private AliyunSms aliyunSms;
    private AliyunGreen aliyunGreen;
    private String aliyunOssEndpoint;
    private String aliyunOssPublicBuckename;
    private String aliyunOssPrivateBuckename;
    private String storageOssPrivateUrl;
    private String storageType;
    private String storagePath;
    private String storageUrl;
    private String storagePublicUrl;
    private String systemname;
    private String totpseed;
    private String smsService;
    private String ipv4DataPath;
    private String ipv6DataPath;

    @Data
    public static class Oauth2 {
        private String privateKey;
        private String publicKey;
    }

    @Data
    public static class Aliyun {
        private String regionId;
        private String accessKeyId;
        private String secret;
    }

    @Data
    public static class AliyunSms{
        private String signName;
        private String templateCode;
    }

    @Data
    public static class AliyunGreen{
        private String endpointName;
        private String regionId;
        private String domain;
    }
}

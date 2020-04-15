package com.winteree.uaa.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.winteree.uaa.client.WintereeCoreServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author RenFei
 */
@Service
public class I18nService {
    private final WintereeCoreServiceClient wintereeCoreServiceClient;

    public I18nService(WintereeCoreServiceClient wintereeCoreServiceClient) {
        this.wintereeCoreServiceClient = wintereeCoreServiceClient;
    }

    @HystrixCommand(fallbackMethod = "getMessageFallback")
    public String getMessage(String language, String message, String defaultMessage) {
        if (StringUtils.isEmpty(language)) {
            return defaultMessage;
        }
        return wintereeCoreServiceClient.getMessage(language, message, defaultMessage);
    }

    public String getMessageFallback(String language, String message, String defaultMessage) {
        return defaultMessage;
    }
}

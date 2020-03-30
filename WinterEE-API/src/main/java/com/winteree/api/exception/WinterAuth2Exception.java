package com.winteree.api.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2Exception
 * @author RenFei
 */
@JsonSerialize(using = WinterAuth2ExceptionSerializer.class)
public class WinterAuth2Exception extends OAuth2Exception {
    @Getter
    private String errorCode;

    public WinterAuth2Exception(String msg) {
        super(msg);
    }

    public WinterAuth2Exception(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }
}

package com.winteree.api.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * @author RenFei
 */
@JsonSerialize(using = WinterAuth2ExceptionSerializer.class)
public class InvalidException extends WinterAuth2Exception {

    public InvalidException(String msg, Throwable t) {
        super(msg);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "invalid_exception";
    }

    @Override
    public int getHttpErrorCode() {
        return 426;
    }

}

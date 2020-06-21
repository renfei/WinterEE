package com.winteree.api.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.renfei.sdk.comm.StateCode;
import org.springframework.http.HttpStatus;

/**
 * 权限不足异常
 *
 * @author RenFei
 */
@JsonSerialize(using = WinterAuth2ExceptionSerializer.class)
public class ForbiddenException extends WinterAuth2Exception {

    public ForbiddenException(String msg) {
        super(msg);
        setStateCode(StateCode.Forbidden);
    }

    public ForbiddenException(String msg, Throwable t) {
        super(msg);
        setStateCode(StateCode.Forbidden);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "Forbidden";
    }

    @Override
    public int getHttpErrorCode() {
        return HttpStatus.FORBIDDEN.value();
    }

}

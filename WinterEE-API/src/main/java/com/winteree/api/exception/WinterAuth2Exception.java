package com.winteree.api.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import net.renfei.sdk.comm.StateCode;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * 自定义OAuth2Exception
 *
 * @author RenFei
 */
@JsonSerialize(using = WinterAuth2ExceptionSerializer.class)
public class WinterAuth2Exception extends OAuth2Exception {
    @Getter
    private StateCode stateCode;

    public WinterAuth2Exception(String msg) {
        super(msg);
    }

    public WinterAuth2Exception(StateCode stateCode, String msg) {
        super(msg);
        this.stateCode = stateCode;
    }

    protected StateCode getStateCode() {
        return stateCode;
    }

    protected void setStateCode(StateCode stateCode) {
        this.stateCode = stateCode;
    }
}

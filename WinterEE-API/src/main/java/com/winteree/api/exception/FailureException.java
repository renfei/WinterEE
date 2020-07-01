package com.winteree.api.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import net.renfei.sdk.comm.StateCode;

/**
 * <p>Title: FailureException</p>
 * <p>Description: 执行失败异常</p>
 *
 * @author RenFei
 * @date : 2020-06-20 11:50
 */
@JsonSerialize(using = WinterAuth2ExceptionSerializer.class)
public class FailureException extends WinterAuth2Exception {
    public FailureException(String msg) {
        super(msg);
        setStateCode(StateCode.Failure);
    }

    @Override
    public String getOAuth2ErrorCode() {
        return "Failure";
    }

    @Override
    public int getHttpErrorCode() {
        return StateCode.Failure.getCode();
    }
}

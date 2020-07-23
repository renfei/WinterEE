package com.winteree.core.controller;

import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * <p>Title: GlobalExceptionHandler</p>
 * <p>Description: @ControllerAdvice + @ExceptionHandler 实现全局的异常处理</p>
 *
 * @author RenFei
 * @date : 2020-07-23 22:14
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理所有不可知的异常
     *
     * @param exception 异常
     * @return APIResult
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    APIResult handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return APIResult.builder().code(StateCode.Error).message("服务暂时不可用").build();
    }
}

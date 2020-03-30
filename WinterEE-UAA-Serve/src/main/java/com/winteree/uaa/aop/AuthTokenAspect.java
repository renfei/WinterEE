package com.winteree.uaa.aop;

import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;

/**
 * @author RenFei
 */
@Slf4j
@Aspect
@Component
public class AuthTokenAspect {
    @Around("execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.postAccessToken(..)) || " +
            "execution(* org.springframework.security.oauth2.provider.endpoint.TokenEndpoint.getAccessToken(..))")
    public Object handleControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        if (proceed != null) {
            ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) proceed;
            OAuth2AccessToken body = responseEntity.getBody();
            if (responseEntity.getStatusCode().is2xxSuccessful()) {
                return new ResponseEntity(APIResult.builder()
                        .code(StateCode.OK)
                        .message("Success")
                        .data(body)
                        .build(), responseEntity.getHeaders(), responseEntity.getStatusCode());
            } else {
                log.error("error:{}", responseEntity.getStatusCode().toString());
                return new ResponseEntity(APIResult.builder()
                        .code(StateCode.Failure)
                        .message("获取授权码失败")
                        .data(body)
                        .build(), responseEntity.getHeaders(), responseEntity.getStatusCode());
            }
        }
        return new ResponseEntity(APIResult.builder()
                .code(StateCode.Failure)
                .message("获取授权码失败")
                .build(),null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

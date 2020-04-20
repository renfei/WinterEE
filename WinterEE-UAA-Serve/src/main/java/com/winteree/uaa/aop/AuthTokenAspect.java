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

import java.util.LinkedHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

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
        AtomicBoolean isNotClientCredentials = new AtomicBoolean(true);
        //1.这里获取到所有的参数值的数组
        Object[] args = pjp.getArgs();
        for (Object obj : args
        ) {
            if (obj instanceof LinkedHashMap) {
                LinkedHashMap linkedHashMap = (LinkedHashMap) obj;
                linkedHashMap.forEach((name, value) -> {
                    if ("grant_type".equals(name.toString().toLowerCase()) && "client_credentials".equals(value.toString().toLowerCase())) {
                        isNotClientCredentials.set(false);
                    }
                });
            }
        }
        Object proceed = pjp.proceed();
        if (proceed != null) {
            ResponseEntity<OAuth2AccessToken> responseEntity = (ResponseEntity<OAuth2AccessToken>) proceed;
            OAuth2AccessToken body = responseEntity.getBody();
            if (isNotClientCredentials.get()) {
                responseEntity.getHeaders();
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
                            .build(), responseEntity.getHeaders(), HttpStatus.OK);
                }
            } else {
                return responseEntity;
            }
        }
        return new ResponseEntity(APIResult.builder()
                .code(StateCode.Failure)
                .message("获取授权码失败")
                .build(), null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

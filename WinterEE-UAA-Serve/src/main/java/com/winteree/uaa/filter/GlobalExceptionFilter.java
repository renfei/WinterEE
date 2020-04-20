package com.winteree.uaa.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Title: GlobalExceptionFilter</p>
 * <p>Description: 全局异常捕获</p>
 *
 * @author RenFei
 * @date : 2020-04-20 16:45
 */
@Slf4j
@Component
public class GlobalExceptionFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        try {
            // 执行各filter
            chain.doFilter(request, response);
        } catch (Exception ex) {
            // 捕获并处理相关异常，若不能处理的，直接抛出
            if (ex instanceof InvalidTokenException
                    || ex instanceof org.springframework.security.oauth2.common.exceptions.InvalidTokenException) {
                if(ex.getMessage().startsWith("Access token expired:")){
                    response.setContentType("application/json;charset=UTF-8");
                    APIResult apiResult = APIResult.builder()
                            .code(StateCode.Unauthorized)
                            .message(ex.getMessage())
                            .build();
                    response.getWriter().write(JSON.toJSONString(apiResult));
                }else {
                    throw ex;
                }
            } else {
                throw ex;
            }
        }
    }
}

package com.philisense.approve.config;

import com.alibaba.fastjson.JSON;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <p>Title: CustomAccessDeniedHandler</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-04-21 13:40
 */
public class CustomAccessDeniedHandlerImpl implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        APIResult apiResult = APIResult.builder()
                .code(StateCode.Forbidden)
                .message("权限不足，服务器理解请求客户端的请求，但是拒绝执行此请求")
                .build();
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.getWriter().write(JSON.toJSONString(apiResult));
    }
}

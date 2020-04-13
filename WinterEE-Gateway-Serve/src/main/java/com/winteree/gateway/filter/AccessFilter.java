package com.winteree.gateway.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.winteree.gateway.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SEND_RESPONSE_FILTER_ORDER;

/**
 * <p>Title: AccessFilter</p>
 * <p>Description: 访问日志记录</p>
 *
 * @author RenFei
 * @date : 2020-04-13 13:37
 */
@Slf4j
public class AccessFilter extends ZuulFilter {
    @Autowired
    private LogService logService;

    @Override
    public String filterType() {
        // 后置拦截器
        return FilterConstants.POST_TYPE;
    }

    @Override
    public int filterOrder() {
        // 在SendResponse之前记录Access记录
        return SEND_RESPONSE_FILTER_ORDER - 1;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        HttpServletResponse response = requestContext.getResponse();
        /*
         * 获取令牌内容
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ((authentication instanceof OAuth2Authentication)) {
            OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
            Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        }
        logService.log(request, response);
        return null;
    }
}

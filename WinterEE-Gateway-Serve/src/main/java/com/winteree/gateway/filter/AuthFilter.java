package com.winteree.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import java.util.*;

/**
 * @author RenFei
 */
@Slf4j
public class AuthFilter extends ZuulFilter {
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext ctx = RequestContext.getCurrentContext();
        /*
         * 获取令牌内容
         */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof OAuth2Authentication)) {
            return null;
        }
        OAuth2Authentication oAuth2Authentication = (OAuth2Authentication) authentication;
        Authentication userAuthentication = oAuth2Authentication.getUserAuthentication();
        String name = userAuthentication.getName();
//        log.info("getCredentials:{}", JSON.toJSONString(userAuthentication.getCredentials()));
//        log.info("getDetails:{}", JSON.toJSONString(userAuthentication.getDetails()));
//        log.info("getPrincipal:{}", JSON.toJSONString(userAuthentication.getPrincipal()));
        /*
         * 组装明文token
         */
        List<String> authorities = new ArrayList<>();
        userAuthentication.getAuthorities().forEach(c -> authorities.add(c.getAuthority()));
        OAuth2Request oAuth2Request = oAuth2Authentication.getOAuth2Request();
        Map<String, String> requestParameters = oAuth2Request.getRequestParameters();
        Map<String, Object> jsonToken = new HashMap<>(requestParameters);
        jsonToken.put("name", name);
        jsonToken.put("authorities", authorities);
        String json_token = StringUtils.encodeUTF8StringBase64(JSON.toJSONString(jsonToken));
        log.info("json_token:{}", json_token);
        ctx.addZuulRequestHeader("json-token", json_token);
        return null;
    }
}

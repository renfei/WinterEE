package com.winteree.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.winteree.gateway.client.WintereeUaaServiceClient;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author RenFei
 */
@Slf4j
public class AuthFilter extends ZuulFilter {
    private final WintereeUaaServiceClient wintereeUaaServiceClient;

    public AuthFilter(WintereeUaaServiceClient wintereeUaaServiceClient) {
        this.wintereeUaaServiceClient = wintereeUaaServiceClient;
    }

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
        // 如果安全上下文不存在，那么尝试从 Cookie 中获取认证信息
        if (!(authentication instanceof OAuth2Authentication)) {
            // 从 Cookie 中获取认证信息
            setJsonTokenByCookie(ctx);
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

    /**
     * 从 Cookie 中获取认证信息
     *
     * @param ctx
     */
    private void setJsonTokenByCookie(RequestContext ctx) {
        Cookie[] cookies = ctx.getRequest().getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies
            ) {
                if ("Authorization".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    token = token.replace("Bearer ", "");
                    JSONObject jsonObject = JSON.parseObject(wintereeUaaServiceClient.checkToken(token));
                    if (jsonObject.getString("error") != null) {
                        // 验证失败，直接返回
                        return;
                    } else {
                        // 验证成功
                        String name = jsonObject.getString("user_name");
                        JSONArray jsonArray = jsonObject.getJSONArray("authorities");
                        List<String> authorities = new ArrayList<>();
                        jsonArray.forEach(auth -> authorities.add((String) auth));
                        Map<String, Object> jsonToken = new HashMap<>();
                        jsonToken.put("name", name);
                        jsonToken.put("authorities", authorities);
                        String json_token = StringUtils.encodeUTF8StringBase64(JSON.toJSONString(jsonToken));
                        log.info("json_token:{}", json_token);
                        ctx.addZuulRequestHeader("json-token", json_token);
                        return;
                    }
                }
            }
        }
    }
}

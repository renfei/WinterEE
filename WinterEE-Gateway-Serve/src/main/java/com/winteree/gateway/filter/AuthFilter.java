package com.winteree.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import com.winteree.api.entity.AccountDTO;
import com.winteree.api.entity.TenantDTO;
import com.winteree.gateway.client.WintereeCoreServiceClient;
import com.winteree.gateway.client.WintereeUaaServiceClient;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.DateUtils;
import net.renfei.sdk.utils.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * @author RenFei
 */
@Slf4j
public class AuthFilter extends ZuulFilter {
    private final WintereeUaaServiceClient wintereeUaaServiceClient;
    private final WintereeCoreServiceClient wintereeCoreServiceClient;

    public AuthFilter(WintereeUaaServiceClient wintereeUaaServiceClient,
                      WintereeCoreServiceClient wintereeCoreServiceClient) {
        this.wintereeUaaServiceClient = wintereeUaaServiceClient;
        this.wintereeCoreServiceClient = wintereeCoreServiceClient;
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
        APIResult<AccountDTO> apiResult =
                wintereeCoreServiceClient.getAccountByUuid(name);
        if (StateCode.OK.getCode().equals(apiResult.getCode())) {
            if (apiResult.getData() != null) {
                AccountDTO accountDTO = apiResult.getData();
                TenantDTO tenant =
                        wintereeCoreServiceClient.getTenantDTO(accountDTO.getTenantUuid()).getData();
                if (tenant != null) {
                    // 判定所属租户是否到期
                    Date expiryDate = tenant.getExpiryDate();
                    if (expiryDate == null|| DateUtils.pastDays(expiryDate) > 0) {
                        APIResult result = APIResult.builder()
                                .code(StateCode.Unavailable)
                                .message("当前服务租约已到期，服务被暂停。请稍后再次尝试。")
                                .build();
                        returnJson(ctx.getResponse(), JSON.toJSONString(result));
                        return null;
                    }
                }
            }
        } else {
            APIResult result = APIResult.builder()
                    .code(StateCode.Unavailable)
                    .message("核心服务暂时不可用，请稍后再试。")
                    .build();
            returnJson(ctx.getResponse(), JSON.toJSONString(result));
            return null;
        }
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

    private void returnJson(HttpServletResponse response, String json) {
        PrintWriter writer = null;
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        try {
            writer = response.getWriter();
            writer.print(json);
        } catch (IOException e) {
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }
}

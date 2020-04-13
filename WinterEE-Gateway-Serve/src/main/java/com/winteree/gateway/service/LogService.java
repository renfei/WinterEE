package com.winteree.gateway.service;

import com.winteree.api.entity.LogDTO;
import com.winteree.api.entity.LogSubTypeEnum;
import com.winteree.api.entity.LogTypeEnum;
import com.winteree.gateway.client.WintereeCoreServiceClient;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.UUID;

/**
 * <p>Title: LogService</p>
 * <p>Description: 日志服务</p>
 *
 * @author RenFei
 * @date : 2020-04-13 13:43
 */
@Slf4j
@Service
public class LogService {
    private static final String CHECK_TOKEN = "/uaa/oauth/check_token";
    @Autowired
    private WintereeCoreServiceClient wintereeCoreServiceClient;

    @Async
    public void log(HttpServletRequest request, HttpServletResponse response) {
        if (CHECK_TOKEN.equals(request.getRequestURI().toLowerCase())) {
            return;
        }
        LogDTO logDTO = Builder.of(LogDTO::new)
                .with(LogDTO::setId, UUID.randomUUID().toString())
                .with(LogDTO::setDateTime, new Date())
                .with(LogDTO::setLogType, LogTypeEnum.ACCESS)
//                .with(LogDTO::setAccountId, logDTO.getAccountId())
//                .with(LogDTO::setClientId, logDTO.getClientId())
                .with(LogDTO::setClientIp, IpUtils.getIpAddress(request))
                .with(LogDTO::setRequestUrl, request.getRequestURI())
                .with(LogDTO::setRequestMethod, request.getMethod())
                .with(LogDTO::setRequestHead, getHeaders(request))
                .with(LogDTO::setRequestBody, getRequestBody(request))
                .with(LogDTO::setResponseHead, getHeaders(response))
                .with(LogDTO::setResponseBody, getResponseBody(response))
                .with(LogDTO::setStatusCode, response.getStatus() + "")
                .with(LogDTO::setLogValue, "")
                .build();
        if (response.getStatus() == 200) {
            logDTO.setLogSubType(LogSubTypeEnum.SUCCESS);
        } else {
            logDTO.setLogSubType(LogSubTypeEnum.FAIL);
        }
        APIResult apiResult = wintereeCoreServiceClient.log(logDTO);
        if (StateCode.OK.getCode().equals(apiResult.getCode())) {
        } else {
            log.error("Access日志插入失败");
        }
    }

    private String getRequestBody(HttpServletRequest request) {
        try {
            BufferedReader br = request.getReader();
            StringBuilder sb = new StringBuilder();
            String str;
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            return sb.toString();
        } catch (Exception ex) {
            return null;
        }
    }

    private String getResponseBody(HttpServletResponse response) {
        ContentCachingResponseWrapper wrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
        if (wrapper != null) {
            byte[] buf = wrapper.getContentAsByteArray();
            if (buf.length > 0) {
                String payload;
                try {
                    payload = new String(buf, 0, buf.length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    payload = "[unknown]";
                }
                return payload;
            }
        }
        return null;
    }

    private String getHeaders(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            sb.append(name);
            sb.append(":");
            sb.append(request.getHeader(name));
            sb.append("\n");
        }
        return sb.toString();
    }

    private String getHeaders(HttpServletResponse response) {
        Collection<String> headerNames = response.getHeaderNames();
        StringBuilder sb = new StringBuilder();
        for (String name : headerNames
        ) {
            sb.append(name);
            sb.append(":");
            sb.append(response.getHeader(name));
            sb.append("\n");
        }
        return sb.toString();
    }
}

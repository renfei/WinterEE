package com.winteree.api.entity;

import lombok.Data;

import java.util.Date;

/**
 * <p>Title: LogDTO</p>
 * <p>Description: 日志传输对象</p>
 *
 * @author RenFei
 * @date : 2020-04-12 22:24
 */
@Data
public class LogDTO {
    private String id;
    private Date dateTime;
    private LogTypeEnum logType;
    private LogSubTypeEnum logSubType;
    private String tenantId;
    private String accountId;
    private String clientId;
    private String clientIp;
    private String requestUrl;
    private String requestMethod;
    private String requestHead;
    private String requestBody;
    private String responseHead;
    private String responseBody;
    private String statusCode;
    private String logValue;
}

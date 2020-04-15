package com.winteree.uaa.dao.entity;

public class LogDOWithBLOBs extends LogDO {
    private String logvalue;

    private String requestUrl;

    private String requestHead;

    private String requestBody;

    private String responseHead;

    private String responseBody;

    public String getLogvalue() {
        return logvalue;
    }

    public void setLogvalue(String logvalue) {
        this.logvalue = logvalue == null ? null : logvalue.trim();
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl == null ? null : requestUrl.trim();
    }

    public String getRequestHead() {
        return requestHead;
    }

    public void setRequestHead(String requestHead) {
        this.requestHead = requestHead == null ? null : requestHead.trim();
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody == null ? null : requestBody.trim();
    }

    public String getResponseHead() {
        return responseHead;
    }

    public void setResponseHead(String responseHead) {
        this.responseHead = responseHead == null ? null : responseHead.trim();
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody == null ? null : responseBody.trim();
    }
}
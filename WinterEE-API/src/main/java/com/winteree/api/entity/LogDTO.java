package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value = "日志传输对象", description = "日志传输对象")
public class LogDTO {
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "发生时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "日志类型")
    private LogTypeEnum logType;
    @ApiModelProperty(value = "日志子类型")
    private LogSubTypeEnum logSubType;
    @ApiModelProperty(value = "租户唯一编码")
    private String tenantUuid;
    @ApiModelProperty(value = "账户唯一编号")
    private String accountUuid;
    @ApiModelProperty(value = "客户端唯一编号")
    private String clientUuid;
    @ApiModelProperty(value = "客户端IP")
    private String clientIp;
    @ApiModelProperty(value = "请求地址")
    private String requestUrl;
    @ApiModelProperty(value = "请求方法")
    private String requestMethod;
    @ApiModelProperty(value = "请求头")
    private String requestHead;
    @ApiModelProperty(value = "请求体")
    private String requestBody;
    @ApiModelProperty(value = "响应头")
    private String responseHead;
    @ApiModelProperty(value = "响应体")
    private String responseBody;
    @ApiModelProperty(value = "状态码")
    private String statusCode;
    @ApiModelProperty(value = "日志内容")
    private String logValue;
    private Long total;
}

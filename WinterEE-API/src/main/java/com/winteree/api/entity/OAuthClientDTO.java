package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>Title: OAuthClientDTO</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-04-27 14:48
 */
@Data
@ApiModel(value = "OAuth2客户端数据传输对象", description = "OAuth2客户端数据传输对象")
public class OAuthClientDTO {
    @ApiModelProperty(value = "客户端ID")
    private String clientId;
    @ApiModelProperty(value = "被授权的资源ID列表")
    private String resourceIds;
    @ApiModelProperty(value = "授权范围")
    private String scope;
    @ApiModelProperty(value = "可以使用的授权类型")
    private String authorizedGrantTypes;
    @ApiModelProperty(value = "回调地址")
    private String webServerRedirectUri;
    @ApiModelProperty(value = "权限列表")
    private String authorities;
    @ApiModelProperty(value = "申请令牌")
    private Integer accessTokenValidity;
    @ApiModelProperty(value = "刷新令牌")
    private Integer refreshTokenValidity;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "存档")
    private Byte archived;
    @ApiModelProperty(value = "可信的")
    private Byte trusted;
    @ApiModelProperty(value = "自动批准")
    private String autoapprove;
    @ApiModelProperty(value = "附加信息")
    private String additionalInformation;
    @ApiModelProperty(value = "语言")
    private String lang;
    @ApiModelProperty(value = "租户唯一编码")
    private String tenantUuid;
}

package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class OAuthClientDTO {
    private String clientId;
    private String resourceIds;
    private String scope;
    private String authorizedGrantTypes;
    private String webServerRedirectUri;
    private String authorities;
    private Integer accessTokenValidity;
    private Integer refreshTokenValidity;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    private Byte archived;
    private Byte trusted;
    private String autoapprove;
    private String additionalInformation;
    private String lang;
}

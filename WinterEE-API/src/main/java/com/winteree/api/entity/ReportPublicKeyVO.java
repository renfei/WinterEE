package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 上报RSA公钥
 *
 * @author RenFei
 */
@Data
@ApiModel(value = "上报RSA公钥")
public class ReportPublicKeyVO {
    /**
     * 加密使用的KeyID
     */
    @ApiModelProperty(value = "加密使用的KeyID")
    private String secretKeyId;

    /**
     * 上报经过加密的公钥
     */
    @ApiModelProperty(value = "上报经过加密的公钥")
    private String publicKey;
}

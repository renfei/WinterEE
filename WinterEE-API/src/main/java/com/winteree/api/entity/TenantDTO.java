package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>Title: TenantDTO</p>
 * <p>Description: 租户传输类</p>
 *
 * @author RenFei
 * @date : 2020-04-26 21:28
 */
@Data
@ApiModel(value = "租户传输类", description = "租户传输类")
public class TenantDTO {
    @ApiModelProperty(value = "自增ID")
    private Long id;
    @ApiModelProperty(value = "唯一编号")
    private String uuid;
    @ApiModelProperty(value = "租户名称")
    private String name;
    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    @ApiModelProperty(value = "过期时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expiryDate;
    @ApiModelProperty(value = "状态")
    private Integer status;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
}

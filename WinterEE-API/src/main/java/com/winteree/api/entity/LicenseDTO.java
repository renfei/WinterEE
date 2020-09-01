package com.winteree.api.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * <p>Title: LicenseDTO</p>
 * <p>Description: 授权文件数据传输类</p>
 *
 * @author RenFei
 * @date : 2020-08-05 22:49
 */
@Data
@ApiModel(value = "授权文件数据传输类", description = "授权文件数据传输类")
public class LicenseDTO {
    @ApiModelProperty(value = "授权SN号码")
    private String licenseSerialNumber;
    @ApiModelProperty(value = "授权类型")
    private LicenseType licenseType;
    @ApiModelProperty(value = "系统名称")
    private String systemName;
    @ApiModelProperty(value = "过期时间")
    private Date expiredDate;
}

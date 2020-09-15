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
    public final static String PERMANENT_DATE = "3000-12-31 23:59:59";
    @ApiModelProperty(value = "授权SN号码")
    private String sn;
    @ApiModelProperty(value = "授权类型")
    private LicenseType type;
    @ApiModelProperty(value = "系统名称")
    private String name;
    @ApiModelProperty(value = "过期时间")
    private Date expired;
    @ApiModelProperty(value = "机器码")
    private String macCode;
    private LicenseState state;
    private String reason;
}

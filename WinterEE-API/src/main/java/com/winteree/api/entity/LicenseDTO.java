package com.winteree.api.entity;

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
public class LicenseDTO {
    private String licenseSerialNumber;
    private LicenseType licenseType;
    private String systemName;
    private Date expiredDate;
}

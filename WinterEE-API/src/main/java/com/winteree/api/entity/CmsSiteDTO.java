package com.winteree.api.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * <p>Title: CmsSiteDTO</p>
 * <p>Description: CMS系统站点数据传输类</p>
 *
 * @author RenFei
 * @date : 2020-06-26 23:09
 */
@Data
public class CmsSiteDTO {
    private String uuid;
    private String tenantUuid;
    private String tenantName;
    private String officeUuid;
    private String officeName;
    private String departmentUuid;
    private String departmentName;
    private String siteName;
    private String siteDomain;
    private String siteKeyword;
    private String siteDescription;
    private String icpNo;
    private Boolean isComment;
    private String gonganNo;
    private String analysisCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createTime;
    private String createBy;
    private String createByName;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateTime;
    private String updateBy;
    private String updateByName;
    private Boolean siteEnable;
}

package com.winteree.api.entity;

import lombok.Data;

/**
 * <p>Title: CmsTagDTO</p>
 * <p>Description: CMS系统文章标签传输对象</p>
 *
 * @author RenFei
 * @date : 2020-06-28 20:04
 */
@Data
public class CmsTagDTO {
    private String siteUuid;
    private String enName;
    private String zhName;
    private String uuid;
    private String describe;
    private Long count;
}

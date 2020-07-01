package com.winteree.api.entity;

import lombok.Data;

/**
 * <p>Title: CmsCategoryDTO</p>
 * <p>Description: CMS系统分类数据传输对象</p>
 *
 * @author RenFei
 * @date : 2020-06-27 20:51
 */
@Data
public class CmsCategoryDTO {
    private String siteUuid;
    private String enName;
    private String zhName;
    private String uuid;
}

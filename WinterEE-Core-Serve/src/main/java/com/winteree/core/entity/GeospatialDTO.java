package com.winteree.core.entity;

import com.winteree.core.dao.entity.GeospatialDOEx;
import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 地图传输对象
 *
 * @author RenFei
 */
@Data
@ApiModel(value = "地图")
public class GeospatialDTO extends GeospatialDOEx {
}

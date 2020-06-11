package com.winteree.core.service;

import com.winteree.api.entity.GeospatialEnum;
import com.winteree.core.dao.entity.GeospatialDO;
import com.winteree.core.entity.GeospatialDTO;

import java.util.List;

/**
 * <p>Title: GeospatialService</p>
 * <p>Description: 地理空间服务</p>
 *
 * @author RenFei
 * @date : 2020-06-04 17:26
 */
public interface GeospatialService {
    /**
     * 根据经纬度和距离搜索周边
     *
     * @param latitude  经度
     * @param longitude 纬度
     * @param distance  距离
     * @param page      页数
     * @param rows      每页行数
     * @return
     */
    List<GeospatialDTO> getGeospatialAroundByDistance(double latitude, double longitude, int distance, int page, int rows);

    /**
     * 根据UUID获取空间地理信息
     *
     * @param fkId           UUID
     * @param geospatialEnum 类型
     * @return
     */
    GeospatialDO getGeospatialByFkIdAndType(String fkId, GeospatialEnum geospatialEnum);

    /**
     * 更新地理空间记录
     *
     * @param geospatialDO 地理空间记录
     * @return
     */
    int updateGeospatial(GeospatialDO geospatialDO);
}

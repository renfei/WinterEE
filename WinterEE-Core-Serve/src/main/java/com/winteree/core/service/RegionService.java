package com.winteree.core.service;

import com.winteree.api.entity.RegionDTO;

import java.util.List;

/**
 * <p>Title: RegionService</p>
 * <p>Description: 行政区划服务</p>
 *
 * @author RenFei
 * @date : 2020-07-21 22:02
 */
public interface RegionService {
    /**
     * 根据行政代码查询行政区划数据
     *
     * @param code 行政代码
     * @return RegionDTO
     */
    RegionDTO getRegionByCode(String code);

    /**
     * 获取子级行政区划列表
     *
     * @param code 本级行政代码，为空时查询顶级行政区划
     * @return List<RegionDTO>
     */
    List<RegionDTO> getChildRegion(String code);
}

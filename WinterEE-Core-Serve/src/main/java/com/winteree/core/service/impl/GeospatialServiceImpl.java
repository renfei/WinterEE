package com.winteree.core.service.impl;

import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.GeospatialEnum;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.GeospatialDOMapper;
import com.winteree.core.dao.entity.GeospatialDO;
import com.winteree.core.dao.entity.GeospatialDOEx;
import com.winteree.core.dao.entity.GeospatialDOExample;
import com.winteree.core.entity.GeospatialDTO;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.GeospatialService;
import lombok.extern.slf4j.Slf4j;
import net.renfei.sdk.utils.BeanUtils;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>Title: GeospatialServiceImpl</p>
 * <p>Description: 地理空间服务</p>
 *
 * @author RenFei
 * @date : 2020-06-04 17:26
 */
@Slf4j
@Service
public class GeospatialServiceImpl extends BaseService implements GeospatialService {
    private final GeospatialDOMapper geospatialDOMapper;

    protected GeospatialServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                    GeospatialDOMapper geospatialDOMapper) {
        super(wintereeCoreConfig);
        this.geospatialDOMapper = geospatialDOMapper;
    }

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
    @Override
    public List<GeospatialDTO> getGeospatialAroundByDistance(double latitude, double longitude, int distance, int page, int rows) {
        List<GeospatialDTO> geospatialDTOS = new ArrayList<>();
        GeospatialDOExample geospatialDOExample = new GeospatialDOExample();
        geospatialDOExample.createCriteria();
        PageHelper.startPage(page, rows);
        List<GeospatialDOEx> geospatialDOExes = geospatialDOMapper.selectGeospatialByDistance(latitude, longitude, distance, geospatialDOExample);
        if (BeanUtils.isEmpty(geospatialDOExes)) {
            return geospatialDTOS;
        }
        geospatialDOExes.forEach(x -> {
            GeospatialDTO geospatialDTO = new GeospatialDTO();
            org.springframework.beans.BeanUtils.copyProperties(x, geospatialDTO);
            geospatialDTOS.add(geospatialDTO);
        });
        return geospatialDTOS;
    }

    /**
     * 根据UUID获取空间地理信息
     *
     * @param fkId           UUID
     * @param geospatialEnum 类型
     * @return
     */
    @Override
    public GeospatialDO getGeospatialByFkIdAndType(String fkId, GeospatialEnum geospatialEnum) {
        GeospatialDOExample geospatialDOExample = new GeospatialDOExample();
        geospatialDOExample.createCriteria()
                .andFkIdEqualTo(fkId)
                .andFkTypeEqualTo(geospatialEnum.getType());
        return ListUtils.getOne(geospatialDOMapper.selectByExample(geospatialDOExample));
    }

    @Override
    public int updateGeospatial(GeospatialDO geospatialDO) {
        if (geospatialDO.getFkType() == null || geospatialDO.getFkId() == null) {
            return 0;
        }
        GeospatialDO geospatialDOByDB = this.getGeospatialByFkIdAndType(geospatialDO.getFkId(), GeospatialEnum.valueOf(geospatialDO.getFkType()));
        if (geospatialDOByDB == null) {
            geospatialDO.setId(UUID.randomUUID().toString());
            return geospatialDOMapper.insertSelective(geospatialDO);
        } else {
            geospatialDO.setId(geospatialDOByDB.getId());
            return geospatialDOMapper.updateByPrimaryKeySelective(geospatialDO);
        }
    }
}

package com.winteree.core.dao;

import com.winteree.core.dao.entity.GeospatialDO;
import com.winteree.core.dao.entity.GeospatialDOEx;
import com.winteree.core.dao.entity.GeospatialDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GeospatialDOMapper {
    long countByExample(GeospatialDOExample example);

    int deleteByExample(GeospatialDOExample example);

    int deleteByPrimaryKey(String id);

    int insert(GeospatialDO record);

    int insertSelective(GeospatialDO record);

    List<GeospatialDO> selectByExample(GeospatialDOExample example);

    GeospatialDO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") GeospatialDO record, @Param("example") GeospatialDOExample example);

    int updateByExample(@Param("record") GeospatialDO record, @Param("example") GeospatialDOExample example);

    int updateByPrimaryKeySelective(GeospatialDO record);

    int updateByPrimaryKey(GeospatialDO record);

    List<GeospatialDOEx> selectGeospatialByDistance(@Param("latitude") Double latitude,
                                                    @Param("longitude") Double longitude,
                                                    @Param("distance") int distance,
                                                    GeospatialDOExample example);
}
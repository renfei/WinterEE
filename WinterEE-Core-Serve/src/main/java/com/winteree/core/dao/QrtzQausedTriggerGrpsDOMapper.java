package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzQausedTriggerGrpsDOExample;
import com.winteree.core.dao.entity.QrtzQausedTriggerGrpsDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzQausedTriggerGrpsDOMapper {
    long countByExample(QrtzQausedTriggerGrpsDOExample example);

    int deleteByExample(QrtzQausedTriggerGrpsDOExample example);

    int deleteByPrimaryKey(QrtzQausedTriggerGrpsDOKey key);

    int insert(QrtzQausedTriggerGrpsDOKey record);

    int insertSelective(QrtzQausedTriggerGrpsDOKey record);

    List<QrtzQausedTriggerGrpsDOKey> selectByExample(QrtzQausedTriggerGrpsDOExample example);

    int updateByExampleSelective(@Param("record") QrtzQausedTriggerGrpsDOKey record, @Param("example") QrtzQausedTriggerGrpsDOExample example);

    int updateByExample(@Param("record") QrtzQausedTriggerGrpsDOKey record, @Param("example") QrtzQausedTriggerGrpsDOExample example);
}
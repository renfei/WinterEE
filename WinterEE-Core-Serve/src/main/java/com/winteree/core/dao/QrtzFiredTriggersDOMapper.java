package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzFiredTriggersDO;
import com.winteree.core.dao.entity.QrtzFiredTriggersDOExample;
import com.winteree.core.dao.entity.QrtzFiredTriggersDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzFiredTriggersDOMapper {
    long countByExample(QrtzFiredTriggersDOExample example);

    int deleteByExample(QrtzFiredTriggersDOExample example);

    int deleteByPrimaryKey(QrtzFiredTriggersDOKey key);

    int insert(QrtzFiredTriggersDO record);

    int insertSelective(QrtzFiredTriggersDO record);

    List<QrtzFiredTriggersDO> selectByExample(QrtzFiredTriggersDOExample example);

    QrtzFiredTriggersDO selectByPrimaryKey(QrtzFiredTriggersDOKey key);

    int updateByExampleSelective(@Param("record") QrtzFiredTriggersDO record, @Param("example") QrtzFiredTriggersDOExample example);

    int updateByExample(@Param("record") QrtzFiredTriggersDO record, @Param("example") QrtzFiredTriggersDOExample example);

    int updateByPrimaryKeySelective(QrtzFiredTriggersDO record);

    int updateByPrimaryKey(QrtzFiredTriggersDO record);
}
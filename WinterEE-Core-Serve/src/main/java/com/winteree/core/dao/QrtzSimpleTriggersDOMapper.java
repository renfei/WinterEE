package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzSimpleTriggersDO;
import com.winteree.core.dao.entity.QrtzSimpleTriggersDOExample;
import com.winteree.core.dao.entity.QrtzSimpleTriggersDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzSimpleTriggersDOMapper {
    long countByExample(QrtzSimpleTriggersDOExample example);

    int deleteByExample(QrtzSimpleTriggersDOExample example);

    int deleteByPrimaryKey(QrtzSimpleTriggersDOKey key);

    int insert(QrtzSimpleTriggersDO record);

    int insertSelective(QrtzSimpleTriggersDO record);

    List<QrtzSimpleTriggersDO> selectByExample(QrtzSimpleTriggersDOExample example);

    QrtzSimpleTriggersDO selectByPrimaryKey(QrtzSimpleTriggersDOKey key);

    int updateByExampleSelective(@Param("record") QrtzSimpleTriggersDO record, @Param("example") QrtzSimpleTriggersDOExample example);

    int updateByExample(@Param("record") QrtzSimpleTriggersDO record, @Param("example") QrtzSimpleTriggersDOExample example);

    int updateByPrimaryKeySelective(QrtzSimpleTriggersDO record);

    int updateByPrimaryKey(QrtzSimpleTriggersDO record);
}
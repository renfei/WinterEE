package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzTriggersDO;
import com.winteree.core.dao.entity.QrtzTriggersDOExample;
import com.winteree.core.dao.entity.QrtzTriggersDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzTriggersDOMapper {
    long countByExample(QrtzTriggersDOExample example);

    int deleteByExample(QrtzTriggersDOExample example);

    int deleteByPrimaryKey(QrtzTriggersDOKey key);

    int insert(QrtzTriggersDO record);

    int insertSelective(QrtzTriggersDO record);

    List<QrtzTriggersDO> selectByExampleWithBLOBs(QrtzTriggersDOExample example);

    List<QrtzTriggersDO> selectByExample(QrtzTriggersDOExample example);

    QrtzTriggersDO selectByPrimaryKey(QrtzTriggersDOKey key);

    int updateByExampleSelective(@Param("record") QrtzTriggersDO record, @Param("example") QrtzTriggersDOExample example);

    int updateByExampleWithBLOBs(@Param("record") QrtzTriggersDO record, @Param("example") QrtzTriggersDOExample example);

    int updateByExample(@Param("record") QrtzTriggersDO record, @Param("example") QrtzTriggersDOExample example);

    int updateByPrimaryKeySelective(QrtzTriggersDO record);

    int updateByPrimaryKeyWithBLOBs(QrtzTriggersDO record);

    int updateByPrimaryKey(QrtzTriggersDO record);
}
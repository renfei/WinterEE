package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzBlobTriggersDO;
import com.winteree.core.dao.entity.QrtzBlobTriggersDOExample;
import com.winteree.core.dao.entity.QrtzBlobTriggersDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzBlobTriggersDOMapper {
    long countByExample(QrtzBlobTriggersDOExample example);

    int deleteByExample(QrtzBlobTriggersDOExample example);

    int deleteByPrimaryKey(QrtzBlobTriggersDOKey key);

    int insert(QrtzBlobTriggersDO record);

    int insertSelective(QrtzBlobTriggersDO record);

    List<QrtzBlobTriggersDO> selectByExampleWithBLOBs(QrtzBlobTriggersDOExample example);

    List<QrtzBlobTriggersDO> selectByExample(QrtzBlobTriggersDOExample example);

    QrtzBlobTriggersDO selectByPrimaryKey(QrtzBlobTriggersDOKey key);

    int updateByExampleSelective(@Param("record") QrtzBlobTriggersDO record, @Param("example") QrtzBlobTriggersDOExample example);

    int updateByExampleWithBLOBs(@Param("record") QrtzBlobTriggersDO record, @Param("example") QrtzBlobTriggersDOExample example);

    int updateByExample(@Param("record") QrtzBlobTriggersDO record, @Param("example") QrtzBlobTriggersDOExample example);

    int updateByPrimaryKeySelective(QrtzBlobTriggersDO record);

    int updateByPrimaryKeyWithBLOBs(QrtzBlobTriggersDO record);
}
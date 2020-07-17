package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzLocksDOExample;
import com.winteree.core.dao.entity.QrtzLocksDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzLocksDOMapper {
    long countByExample(QrtzLocksDOExample example);

    int deleteByExample(QrtzLocksDOExample example);

    int deleteByPrimaryKey(QrtzLocksDOKey key);

    int insert(QrtzLocksDOKey record);

    int insertSelective(QrtzLocksDOKey record);

    List<QrtzLocksDOKey> selectByExample(QrtzLocksDOExample example);

    int updateByExampleSelective(@Param("record") QrtzLocksDOKey record, @Param("example") QrtzLocksDOExample example);

    int updateByExample(@Param("record") QrtzLocksDOKey record, @Param("example") QrtzLocksDOExample example);
}
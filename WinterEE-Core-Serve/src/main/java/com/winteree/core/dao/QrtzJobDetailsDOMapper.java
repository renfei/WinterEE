package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzJobDetailsDO;
import com.winteree.core.dao.entity.QrtzJobDetailsDOExample;
import com.winteree.core.dao.entity.QrtzJobDetailsDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzJobDetailsDOMapper {
    long countByExample(QrtzJobDetailsDOExample example);

    int deleteByExample(QrtzJobDetailsDOExample example);

    int deleteByPrimaryKey(QrtzJobDetailsDOKey key);

    int insert(QrtzJobDetailsDO record);

    int insertSelective(QrtzJobDetailsDO record);

    List<QrtzJobDetailsDO> selectByExampleWithBLOBs(QrtzJobDetailsDOExample example);

    List<QrtzJobDetailsDO> selectByExample(QrtzJobDetailsDOExample example);

    QrtzJobDetailsDO selectByPrimaryKey(QrtzJobDetailsDOKey key);

    int updateByExampleSelective(@Param("record") QrtzJobDetailsDO record, @Param("example") QrtzJobDetailsDOExample example);

    int updateByExampleWithBLOBs(@Param("record") QrtzJobDetailsDO record, @Param("example") QrtzJobDetailsDOExample example);

    int updateByExample(@Param("record") QrtzJobDetailsDO record, @Param("example") QrtzJobDetailsDOExample example);

    int updateByPrimaryKeySelective(QrtzJobDetailsDO record);

    int updateByPrimaryKeyWithBLOBs(QrtzJobDetailsDO record);

    int updateByPrimaryKey(QrtzJobDetailsDO record);
}
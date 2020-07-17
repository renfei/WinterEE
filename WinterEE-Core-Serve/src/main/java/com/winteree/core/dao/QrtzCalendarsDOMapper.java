package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzCalendarsDO;
import com.winteree.core.dao.entity.QrtzCalendarsDOExample;
import com.winteree.core.dao.entity.QrtzCalendarsDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzCalendarsDOMapper {
    long countByExample(QrtzCalendarsDOExample example);

    int deleteByExample(QrtzCalendarsDOExample example);

    int deleteByPrimaryKey(QrtzCalendarsDOKey key);

    int insert(QrtzCalendarsDO record);

    int insertSelective(QrtzCalendarsDO record);

    List<QrtzCalendarsDO> selectByExampleWithBLOBs(QrtzCalendarsDOExample example);

    List<QrtzCalendarsDO> selectByExample(QrtzCalendarsDOExample example);

    QrtzCalendarsDO selectByPrimaryKey(QrtzCalendarsDOKey key);

    int updateByExampleSelective(@Param("record") QrtzCalendarsDO record, @Param("example") QrtzCalendarsDOExample example);

    int updateByExampleWithBLOBs(@Param("record") QrtzCalendarsDO record, @Param("example") QrtzCalendarsDOExample example);

    int updateByExample(@Param("record") QrtzCalendarsDO record, @Param("example") QrtzCalendarsDOExample example);

    int updateByPrimaryKeySelective(QrtzCalendarsDO record);

    int updateByPrimaryKeyWithBLOBs(QrtzCalendarsDO record);
}
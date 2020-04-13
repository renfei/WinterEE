package com.winteree.core.dao;

import com.winteree.core.dao.entity.LogDO;
import com.winteree.core.dao.entity.LogDOExample;
import com.winteree.core.dao.entity.LogDOWithBLOBs;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface LogDOMapper {
    long countByExample(LogDOExample example);

    int deleteByExample(LogDOExample example);

    int deleteByPrimaryKey(String id);

    int insert(LogDOWithBLOBs record);

    int insertSelective(LogDOWithBLOBs record);

    List<LogDOWithBLOBs> selectByExampleWithBLOBs(LogDOExample example);

    List<LogDO> selectByExample(LogDOExample example);

    LogDOWithBLOBs selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") LogDOWithBLOBs record, @Param("example") LogDOExample example);

    int updateByExampleWithBLOBs(@Param("record") LogDOWithBLOBs record, @Param("example") LogDOExample example);

    int updateByExample(@Param("record") LogDO record, @Param("example") LogDOExample example);

    int updateByPrimaryKeySelective(LogDOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(LogDOWithBLOBs record);

    int updateByPrimaryKey(LogDO record);
}
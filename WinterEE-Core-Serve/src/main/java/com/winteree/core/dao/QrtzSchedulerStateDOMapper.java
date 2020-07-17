package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzSchedulerStateDO;
import com.winteree.core.dao.entity.QrtzSchedulerStateDOExample;
import com.winteree.core.dao.entity.QrtzSchedulerStateDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzSchedulerStateDOMapper {
    long countByExample(QrtzSchedulerStateDOExample example);

    int deleteByExample(QrtzSchedulerStateDOExample example);

    int deleteByPrimaryKey(QrtzSchedulerStateDOKey key);

    int insert(QrtzSchedulerStateDO record);

    int insertSelective(QrtzSchedulerStateDO record);

    List<QrtzSchedulerStateDO> selectByExample(QrtzSchedulerStateDOExample example);

    QrtzSchedulerStateDO selectByPrimaryKey(QrtzSchedulerStateDOKey key);

    int updateByExampleSelective(@Param("record") QrtzSchedulerStateDO record, @Param("example") QrtzSchedulerStateDOExample example);

    int updateByExample(@Param("record") QrtzSchedulerStateDO record, @Param("example") QrtzSchedulerStateDOExample example);

    int updateByPrimaryKeySelective(QrtzSchedulerStateDO record);

    int updateByPrimaryKey(QrtzSchedulerStateDO record);
}
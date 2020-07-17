package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzCronTriggersDO;
import com.winteree.core.dao.entity.QrtzCronTriggersDOExample;
import com.winteree.core.dao.entity.QrtzCronTriggersDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzCronTriggersDOMapper {
    long countByExample(QrtzCronTriggersDOExample example);

    int deleteByExample(QrtzCronTriggersDOExample example);

    int deleteByPrimaryKey(QrtzCronTriggersDOKey key);

    int insert(QrtzCronTriggersDO record);

    int insertSelective(QrtzCronTriggersDO record);

    List<QrtzCronTriggersDO> selectByExample(QrtzCronTriggersDOExample example);

    QrtzCronTriggersDO selectByPrimaryKey(QrtzCronTriggersDOKey key);

    int updateByExampleSelective(@Param("record") QrtzCronTriggersDO record, @Param("example") QrtzCronTriggersDOExample example);

    int updateByExample(@Param("record") QrtzCronTriggersDO record, @Param("example") QrtzCronTriggersDOExample example);

    int updateByPrimaryKeySelective(QrtzCronTriggersDO record);

    int updateByPrimaryKey(QrtzCronTriggersDO record);
}
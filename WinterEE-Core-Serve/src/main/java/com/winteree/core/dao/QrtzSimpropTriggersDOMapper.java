package com.winteree.core.dao;

import com.winteree.core.dao.entity.QrtzSimpropTriggersDO;
import com.winteree.core.dao.entity.QrtzSimpropTriggersDOExample;
import com.winteree.core.dao.entity.QrtzSimpropTriggersDOKey;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QrtzSimpropTriggersDOMapper {
    long countByExample(QrtzSimpropTriggersDOExample example);

    int deleteByExample(QrtzSimpropTriggersDOExample example);

    int deleteByPrimaryKey(QrtzSimpropTriggersDOKey key);

    int insert(QrtzSimpropTriggersDO record);

    int insertSelective(QrtzSimpropTriggersDO record);

    List<QrtzSimpropTriggersDO> selectByExample(QrtzSimpropTriggersDOExample example);

    QrtzSimpropTriggersDO selectByPrimaryKey(QrtzSimpropTriggersDOKey key);

    int updateByExampleSelective(@Param("record") QrtzSimpropTriggersDO record, @Param("example") QrtzSimpropTriggersDOExample example);

    int updateByExample(@Param("record") QrtzSimpropTriggersDO record, @Param("example") QrtzSimpropTriggersDOExample example);

    int updateByPrimaryKeySelective(QrtzSimpropTriggersDO record);

    int updateByPrimaryKey(QrtzSimpropTriggersDO record);
}
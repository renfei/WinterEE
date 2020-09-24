package com.winteree.core.dao;

import com.winteree.core.dao.entity.MessageContextDO;
import com.winteree.core.dao.entity.MessageContextDOExample;
import com.winteree.core.dao.entity.MessageContextDOWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageContextDOMapper {
    long countByExample(MessageContextDOExample example);

    int deleteByExample(MessageContextDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MessageContextDOWithBLOBs record);

    int insertSelective(MessageContextDOWithBLOBs record);

    List<MessageContextDOWithBLOBs> selectByExampleWithBLOBs(MessageContextDOExample example);

    List<MessageContextDO> selectByExample(MessageContextDOExample example);

    MessageContextDOWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MessageContextDOWithBLOBs record, @Param("example") MessageContextDOExample example);

    int updateByExampleWithBLOBs(@Param("record") MessageContextDOWithBLOBs record, @Param("example") MessageContextDOExample example);

    int updateByExample(@Param("record") MessageContextDO record, @Param("example") MessageContextDOExample example);

    int updateByPrimaryKeySelective(MessageContextDOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(MessageContextDOWithBLOBs record);

    int updateByPrimaryKey(MessageContextDO record);
}
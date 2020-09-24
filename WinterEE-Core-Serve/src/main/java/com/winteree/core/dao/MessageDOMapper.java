package com.winteree.core.dao;

import com.winteree.core.dao.entity.MessageDO;
import com.winteree.core.dao.entity.MessageDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageDOMapper {
    long countByExample(MessageDOExample example);

    int deleteByExample(MessageDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MessageDO record);

    int insertSelective(MessageDO record);

    List<MessageDO> selectByExample(MessageDOExample example);

    MessageDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MessageDO record, @Param("example") MessageDOExample example);

    int updateByExample(@Param("record") MessageDO record, @Param("example") MessageDOExample example);

    int updateByPrimaryKeySelective(MessageDO record);

    int updateByPrimaryKey(MessageDO record);
}
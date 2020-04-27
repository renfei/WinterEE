package com.winteree.core.dao;

import com.winteree.core.dao.entity.OAuthClientDO;
import com.winteree.core.dao.entity.OAuthClientDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OAuthClientDOMapper {
    long countByExample(OAuthClientDOExample example);

    int deleteByExample(OAuthClientDOExample example);

    int deleteByPrimaryKey(String clientId);

    int insert(OAuthClientDO record);

    int insertSelective(OAuthClientDO record);

    List<OAuthClientDO> selectByExampleWithBLOBs(OAuthClientDOExample example);

    List<OAuthClientDO> selectByExample(OAuthClientDOExample example);

    OAuthClientDO selectByPrimaryKey(String clientId);

    int updateByExampleSelective(@Param("record") OAuthClientDO record, @Param("example") OAuthClientDOExample example);

    int updateByExampleWithBLOBs(@Param("record") OAuthClientDO record, @Param("example") OAuthClientDOExample example);

    int updateByExample(@Param("record") OAuthClientDO record, @Param("example") OAuthClientDOExample example);

    int updateByPrimaryKeySelective(OAuthClientDO record);

    int updateByPrimaryKeyWithBLOBs(OAuthClientDO record);

    int updateByPrimaryKey(OAuthClientDO record);
}
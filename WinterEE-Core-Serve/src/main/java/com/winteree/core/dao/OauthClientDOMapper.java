package com.winteree.core.dao;

import com.winteree.core.dao.entity.OauthClientDO;
import com.winteree.core.dao.entity.OauthClientDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface OauthClientDOMapper {
    long countByExample(OauthClientDOExample example);

    int deleteByExample(OauthClientDOExample example);

    int deleteByPrimaryKey(String clientId);

    int insert(OauthClientDO record);

    int insertSelective(OauthClientDO record);

    List<OauthClientDO> selectByExampleWithBLOBs(OauthClientDOExample example);

    List<OauthClientDO> selectByExample(OauthClientDOExample example);

    OauthClientDO selectByPrimaryKey(String clientId);

    int updateByExampleSelective(@Param("record") OauthClientDO record, @Param("example") OauthClientDOExample example);

    int updateByExampleWithBLOBs(@Param("record") OauthClientDO record, @Param("example") OauthClientDOExample example);

    int updateByExample(@Param("record") OauthClientDO record, @Param("example") OauthClientDOExample example);

    int updateByPrimaryKeySelective(OauthClientDO record);

    int updateByPrimaryKeyWithBLOBs(OauthClientDO record);

    int updateByPrimaryKey(OauthClientDO record);
}
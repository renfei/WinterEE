package com.winteree.uaa.dao;

import com.winteree.uaa.dao.entity.OauthClientDetailsDO;
import com.winteree.uaa.dao.entity.OauthClientDetailsDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OauthClientDetailsDOMapper {
    long countByExample(OauthClientDetailsDOExample example);

    int deleteByExample(OauthClientDetailsDOExample example);

    int deleteByPrimaryKey(String clientId);

    int insert(OauthClientDetailsDO record);

    int insertSelective(OauthClientDetailsDO record);

    List<OauthClientDetailsDO> selectByExampleWithBLOBs(OauthClientDetailsDOExample example);

    List<OauthClientDetailsDO> selectByExample(OauthClientDetailsDOExample example);

    OauthClientDetailsDO selectByPrimaryKey(String clientId);

    int updateByExampleSelective(@Param("record") OauthClientDetailsDO record, @Param("example") OauthClientDetailsDOExample example);

    int updateByExampleWithBLOBs(@Param("record") OauthClientDetailsDO record, @Param("example") OauthClientDetailsDOExample example);

    int updateByExample(@Param("record") OauthClientDetailsDO record, @Param("example") OauthClientDetailsDOExample example);

    int updateByPrimaryKeySelective(OauthClientDetailsDO record);

    int updateByPrimaryKeyWithBLOBs(OauthClientDetailsDO record);

    int updateByPrimaryKey(OauthClientDetailsDO record);
}
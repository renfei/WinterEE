package com.winteree.uaa.dao;

import com.winteree.uaa.dao.entity.SecretKeyDO;
import com.winteree.uaa.dao.entity.SecretKeyDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SecretKeyDOMapper {
    long countByExample(SecretKeyDOExample example);

    int deleteByExample(SecretKeyDOExample example);

    int deleteByPrimaryKey(String id);

    int insert(SecretKeyDO record);

    int insertSelective(SecretKeyDO record);

    List<SecretKeyDO> selectByExampleWithBLOBs(SecretKeyDOExample example);

    List<SecretKeyDO> selectByExample(SecretKeyDOExample example);

    SecretKeyDO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") SecretKeyDO record, @Param("example") SecretKeyDOExample example);

    int updateByExampleWithBLOBs(@Param("record") SecretKeyDO record, @Param("example") SecretKeyDOExample example);

    int updateByExample(@Param("record") SecretKeyDO record, @Param("example") SecretKeyDOExample example);

    int updateByPrimaryKeySelective(SecretKeyDO record);

    int updateByPrimaryKeyWithBLOBs(SecretKeyDO record);
}
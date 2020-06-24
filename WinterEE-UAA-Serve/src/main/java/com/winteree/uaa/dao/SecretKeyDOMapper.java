package com.winteree.uaa.dao;

import com.winteree.uaa.dao.entity.SecretKeyDO;
import com.winteree.uaa.dao.entity.SecretKeyDOExample;
import com.winteree.uaa.dao.entity.SecretKeyDOWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SecretKeyDOMapper {
    long countByExample(SecretKeyDOExample example);

    int deleteByExample(SecretKeyDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SecretKeyDOWithBLOBs record);

    int insertSelective(SecretKeyDOWithBLOBs record);

    List<SecretKeyDOWithBLOBs> selectByExampleWithBLOBs(SecretKeyDOExample example);

    List<SecretKeyDO> selectByExample(SecretKeyDOExample example);

    SecretKeyDOWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SecretKeyDOWithBLOBs record, @Param("example") SecretKeyDOExample example);

    int updateByExampleWithBLOBs(@Param("record") SecretKeyDOWithBLOBs record, @Param("example") SecretKeyDOExample example);

    int updateByExample(@Param("record") SecretKeyDO record, @Param("example") SecretKeyDOExample example);

    int updateByPrimaryKeySelective(SecretKeyDOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(SecretKeyDOWithBLOBs record);

    int updateByPrimaryKey(SecretKeyDO record);
}
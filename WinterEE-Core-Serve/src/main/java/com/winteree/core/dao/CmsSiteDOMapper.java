package com.winteree.core.dao;

import com.winteree.core.dao.entity.CmsSiteDO;
import com.winteree.core.dao.entity.CmsSiteDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsSiteDOMapper {
    long countByExample(CmsSiteDOExample example);

    int deleteByExample(CmsSiteDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsSiteDO record);

    int insertSelective(CmsSiteDO record);

    List<CmsSiteDO> selectByExample(CmsSiteDOExample example);

    CmsSiteDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsSiteDO record, @Param("example") CmsSiteDOExample example);

    int updateByExample(@Param("record") CmsSiteDO record, @Param("example") CmsSiteDOExample example);

    int updateByPrimaryKeySelective(CmsSiteDO record);

    int updateByPrimaryKey(CmsSiteDO record);
}
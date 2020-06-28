package com.winteree.core.dao;

import com.winteree.core.dao.entity.CmsCategoryDO;
import com.winteree.core.dao.entity.CmsCategoryDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsCategoryDOMapper {
    long countByExample(CmsCategoryDOExample example);

    int deleteByExample(CmsCategoryDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsCategoryDO record);

    int insertSelective(CmsCategoryDO record);

    List<CmsCategoryDO> selectByExample(CmsCategoryDOExample example);

    CmsCategoryDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsCategoryDO record, @Param("example") CmsCategoryDOExample example);

    int updateByExample(@Param("record") CmsCategoryDO record, @Param("example") CmsCategoryDOExample example);

    int updateByPrimaryKeySelective(CmsCategoryDO record);

    int updateByPrimaryKey(CmsCategoryDO record);
}
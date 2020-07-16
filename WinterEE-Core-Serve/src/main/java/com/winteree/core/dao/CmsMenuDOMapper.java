package com.winteree.core.dao;

import com.winteree.core.dao.entity.CmsMenuDO;
import com.winteree.core.dao.entity.CmsMenuDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsMenuDOMapper {
    long countByExample(CmsMenuDOExample example);

    int deleteByExample(CmsMenuDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsMenuDO record);

    int insertSelective(CmsMenuDO record);

    List<CmsMenuDO> selectByExample(CmsMenuDOExample example);

    CmsMenuDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsMenuDO record, @Param("example") CmsMenuDOExample example);

    int updateByExample(@Param("record") CmsMenuDO record, @Param("example") CmsMenuDOExample example);

    int updateByPrimaryKeySelective(CmsMenuDO record);

    int updateByPrimaryKey(CmsMenuDO record);
}
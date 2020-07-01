package com.winteree.core.dao;

import com.winteree.core.dao.entity.CmsTagPostsDO;
import com.winteree.core.dao.entity.CmsTagPostsDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsTagPostsDOMapper {
    long countByExample(CmsTagPostsDOExample example);

    int deleteByExample(CmsTagPostsDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsTagPostsDO record);

    int insertSelective(CmsTagPostsDO record);

    List<CmsTagPostsDO> selectByExample(CmsTagPostsDOExample example);

    CmsTagPostsDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsTagPostsDO record, @Param("example") CmsTagPostsDOExample example);

    int updateByExample(@Param("record") CmsTagPostsDO record, @Param("example") CmsTagPostsDOExample example);

    int updateByPrimaryKeySelective(CmsTagPostsDO record);

    int updateByPrimaryKey(CmsTagPostsDO record);
}
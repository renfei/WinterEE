package com.winteree.core.dao;

import com.winteree.core.dao.entity.CmsMenuDO;
import com.winteree.core.dao.entity.CmsMenuDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

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
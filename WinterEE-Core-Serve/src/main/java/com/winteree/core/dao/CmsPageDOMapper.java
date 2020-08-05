package com.winteree.core.dao;

import com.winteree.core.dao.entity.CmsPageDO;
import com.winteree.core.dao.entity.CmsPageDOExample;
import com.winteree.core.dao.entity.CmsPageDOWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsPageDOMapper {
    long countByExample(CmsPageDOExample example);

    int deleteByExample(CmsPageDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsPageDOWithBLOBs record);

    int insertSelective(CmsPageDOWithBLOBs record);

    List<CmsPageDOWithBLOBs> selectByExampleWithBLOBs(CmsPageDOExample example);

    List<CmsPageDO> selectByExample(CmsPageDOExample example);

    CmsPageDOWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsPageDOWithBLOBs record, @Param("example") CmsPageDOExample example);

    int updateByExampleWithBLOBs(@Param("record") CmsPageDOWithBLOBs record, @Param("example") CmsPageDOExample example);

    int updateByExample(@Param("record") CmsPageDO record, @Param("example") CmsPageDOExample example);

    int updateByPrimaryKeySelective(CmsPageDOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CmsPageDOWithBLOBs record);

    int updateByPrimaryKey(CmsPageDO record);
}
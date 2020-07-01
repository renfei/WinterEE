package com.winteree.core.dao;

import com.winteree.core.dao.entity.CmsPostsDO;
import com.winteree.core.dao.entity.CmsPostsDOExample;
import com.winteree.core.dao.entity.CmsPostsDOWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsPostsDOMapper {
    long countByExample(CmsPostsDOExample example);

    int deleteByExample(CmsPostsDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsPostsDOWithBLOBs record);

    int insertSelective(CmsPostsDOWithBLOBs record);

    List<CmsPostsDOWithBLOBs> selectByExampleWithBLOBs(CmsPostsDOExample example);

    List<CmsPostsDO> selectByExample(CmsPostsDOExample example);

    CmsPostsDOWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsPostsDOWithBLOBs record, @Param("example") CmsPostsDOExample example);

    int updateByExampleWithBLOBs(@Param("record") CmsPostsDOWithBLOBs record, @Param("example") CmsPostsDOExample example);

    int updateByExample(@Param("record") CmsPostsDO record, @Param("example") CmsPostsDOExample example);

    int updateByPrimaryKeySelective(CmsPostsDOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CmsPostsDOWithBLOBs record);

    int updateByPrimaryKey(CmsPostsDO record);
}
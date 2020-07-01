package com.winteree.core.dao;

import com.winteree.core.dao.entity.CmsCommentsDO;
import com.winteree.core.dao.entity.CmsCommentsDOExample;
import com.winteree.core.dao.entity.CmsCommentsDOWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsCommentsDOMapper {
    long countByExample(CmsCommentsDOExample example);

    int deleteByExample(CmsCommentsDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsCommentsDOWithBLOBs record);

    int insertSelective(CmsCommentsDOWithBLOBs record);

    List<CmsCommentsDOWithBLOBs> selectByExampleWithBLOBs(CmsCommentsDOExample example);

    List<CmsCommentsDO> selectByExample(CmsCommentsDOExample example);

    CmsCommentsDOWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsCommentsDOWithBLOBs record, @Param("example") CmsCommentsDOExample example);

    int updateByExampleWithBLOBs(@Param("record") CmsCommentsDOWithBLOBs record, @Param("example") CmsCommentsDOExample example);

    int updateByExample(@Param("record") CmsCommentsDO record, @Param("example") CmsCommentsDOExample example);

    int updateByPrimaryKeySelective(CmsCommentsDOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CmsCommentsDOWithBLOBs record);

    int updateByPrimaryKey(CmsCommentsDO record);
}
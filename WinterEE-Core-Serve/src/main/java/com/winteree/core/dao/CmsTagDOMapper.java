package com.winteree.core.dao;

import com.winteree.core.dao.entity.CmsTagDO;
import com.winteree.core.dao.entity.CmsTagDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CmsTagDOMapper {
    long countByExample(CmsTagDOExample example);

    int deleteByExample(CmsTagDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CmsTagDO record);

    int insertSelective(CmsTagDO record);

    List<CmsTagDO> selectByExampleWithBLOBs(CmsTagDOExample example);

    List<CmsTagDO> selectByExample(CmsTagDOExample example);

    CmsTagDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CmsTagDO record, @Param("example") CmsTagDOExample example);

    int updateByExampleWithBLOBs(@Param("record") CmsTagDO record, @Param("example") CmsTagDOExample example);

    int updateByExample(@Param("record") CmsTagDO record, @Param("example") CmsTagDOExample example);

    int updateByPrimaryKeySelective(CmsTagDO record);

    int updateByPrimaryKeyWithBLOBs(CmsTagDO record);

    int updateByPrimaryKey(CmsTagDO record);
}
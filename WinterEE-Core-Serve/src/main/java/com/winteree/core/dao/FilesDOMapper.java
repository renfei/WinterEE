package com.winteree.core.dao;

import com.winteree.core.dao.entity.FilesDO;
import com.winteree.core.dao.entity.FilesDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilesDOMapper {
    long countByExample(FilesDOExample example);

    int deleteByExample(FilesDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(FilesDO record);

    int insertSelective(FilesDO record);

    List<FilesDO> selectByExample(FilesDOExample example);

    FilesDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") FilesDO record, @Param("example") FilesDOExample example);

    int updateByExample(@Param("record") FilesDO record, @Param("example") FilesDOExample example);

    int updateByPrimaryKeySelective(FilesDO record);

    int updateByPrimaryKey(FilesDO record);
}
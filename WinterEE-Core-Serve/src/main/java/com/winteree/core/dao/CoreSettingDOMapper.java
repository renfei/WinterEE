package com.winteree.core.dao;

import com.winteree.core.dao.entity.CoreSettingDO;
import com.winteree.core.dao.entity.CoreSettingDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CoreSettingDOMapper {
    long countByExample(CoreSettingDOExample example);

    int deleteByExample(CoreSettingDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CoreSettingDO record);

    int insertSelective(CoreSettingDO record);

    List<CoreSettingDO> selectByExample(CoreSettingDOExample example);

    CoreSettingDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CoreSettingDO record, @Param("example") CoreSettingDOExample example);

    int updateByExample(@Param("record") CoreSettingDO record, @Param("example") CoreSettingDOExample example);

    int updateByPrimaryKeySelective(CoreSettingDO record);

    int updateByPrimaryKey(CoreSettingDO record);
}
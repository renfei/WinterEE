package com.winteree.uaa.dao;

import com.winteree.uaa.dao.entity.RoleMenuDO;
import com.winteree.uaa.dao.entity.RoleMenuDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleMenuDOMapper {
    long countByExample(RoleMenuDOExample example);

    int deleteByExample(RoleMenuDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(RoleMenuDO record);

    int insertSelective(RoleMenuDO record);

    List<RoleMenuDO> selectByExample(RoleMenuDOExample example);

    RoleMenuDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") RoleMenuDO record, @Param("example") RoleMenuDOExample example);

    int updateByExample(@Param("record") RoleMenuDO record, @Param("example") RoleMenuDOExample example);

    int updateByPrimaryKeySelective(RoleMenuDO record);

    int updateByPrimaryKey(RoleMenuDO record);
}
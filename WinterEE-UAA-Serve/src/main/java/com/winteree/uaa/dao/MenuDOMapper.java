package com.winteree.uaa.dao;

import com.winteree.uaa.dao.entity.MenuDO;
import com.winteree.uaa.dao.entity.MenuDOExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MenuDOMapper {
    long countByExample(MenuDOExample example);

    int deleteByExample(MenuDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MenuDO record);

    int insertSelective(MenuDO record);

    List<MenuDO> selectByExample(MenuDOExample example);

    MenuDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MenuDO record, @Param("example") MenuDOExample example);

    int updateByExample(@Param("record") MenuDO record, @Param("example") MenuDOExample example);

    int updateByPrimaryKeySelective(MenuDO record);

    int updateByPrimaryKey(MenuDO record);
}
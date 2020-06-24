package com.winteree.core.dao;

import com.winteree.core.dao.entity.OrganizationDO;
import com.winteree.core.dao.entity.OrganizationDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrganizationDOMapper {
    long countByExample(OrganizationDOExample example);

    int deleteByExample(OrganizationDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(OrganizationDO record);

    int insertSelective(OrganizationDO record);

    List<OrganizationDO> selectByExample(OrganizationDOExample example);

    OrganizationDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") OrganizationDO record, @Param("example") OrganizationDOExample example);

    int updateByExample(@Param("record") OrganizationDO record, @Param("example") OrganizationDOExample example);

    int updateByPrimaryKeySelective(OrganizationDO record);

    int updateByPrimaryKey(OrganizationDO record);
}
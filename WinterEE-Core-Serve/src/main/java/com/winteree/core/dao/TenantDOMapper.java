package com.winteree.core.dao;

import com.winteree.core.dao.entity.TenantDO;
import com.winteree.core.dao.entity.TenantDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantDOMapper {
    long countByExample(TenantDOExample example);

    int deleteByExample(TenantDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TenantDO record);

    int insertSelective(TenantDO record);

    List<TenantDO> selectByExample(TenantDOExample example);

    TenantDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TenantDO record, @Param("example") TenantDOExample example);

    int updateByExample(@Param("record") TenantDO record, @Param("example") TenantDOExample example);

    int updateByPrimaryKeySelective(TenantDO record);

    int updateByPrimaryKey(TenantDO record);
}
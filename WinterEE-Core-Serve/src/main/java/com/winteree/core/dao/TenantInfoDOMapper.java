package com.winteree.core.dao;

import com.winteree.core.dao.entity.TenantInfoDO;
import com.winteree.core.dao.entity.TenantInfoDOExample;
import com.winteree.core.dao.entity.TenantInfoDOWithBLOBs;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantInfoDOMapper {
    long countByExample(TenantInfoDOExample example);

    int deleteByExample(TenantInfoDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(TenantInfoDOWithBLOBs record);

    int insertSelective(TenantInfoDOWithBLOBs record);

    List<TenantInfoDOWithBLOBs> selectByExampleWithBLOBs(TenantInfoDOExample example);

    List<TenantInfoDO> selectByExample(TenantInfoDOExample example);

    TenantInfoDOWithBLOBs selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") TenantInfoDOWithBLOBs record, @Param("example") TenantInfoDOExample example);

    int updateByExampleWithBLOBs(@Param("record") TenantInfoDOWithBLOBs record, @Param("example") TenantInfoDOExample example);

    int updateByExample(@Param("record") TenantInfoDO record, @Param("example") TenantInfoDOExample example);

    int updateByPrimaryKeySelective(TenantInfoDOWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(TenantInfoDOWithBLOBs record);

    int updateByPrimaryKey(TenantInfoDO record);
}
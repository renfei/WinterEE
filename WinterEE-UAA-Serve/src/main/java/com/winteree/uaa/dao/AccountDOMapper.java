package com.winteree.uaa.dao;

import com.winteree.uaa.dao.entity.AccountDO;
import com.winteree.uaa.dao.entity.AccountDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountDOMapper {
    long countByExample(AccountDOExample example);

    int deleteByExample(AccountDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(AccountDO record);

    int insertSelective(AccountDO record);

    List<AccountDO> selectByExample(AccountDOExample example);

    AccountDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") AccountDO record, @Param("example") AccountDOExample example);

    int updateByExample(@Param("record") AccountDO record, @Param("example") AccountDOExample example);

    int updateByPrimaryKeySelective(AccountDO record);

    int updateByPrimaryKey(AccountDO record);
}
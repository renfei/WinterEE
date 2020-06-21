package com.winteree.core.dao;

import com.winteree.core.dao.entity.VerificationCodeDO;
import com.winteree.core.dao.entity.VerificationCodeDOExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VerificationCodeDOMapper {
    long countByExample(VerificationCodeDOExample example);

    int deleteByExample(VerificationCodeDOExample example);

    int deleteByPrimaryKey(Long id);

    int insert(VerificationCodeDO record);

    int insertSelective(VerificationCodeDO record);

    List<VerificationCodeDO> selectByExample(VerificationCodeDOExample example);

    VerificationCodeDO selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") VerificationCodeDO record, @Param("example") VerificationCodeDOExample example);

    int updateByExample(@Param("record") VerificationCodeDO record, @Param("example") VerificationCodeDOExample example);

    int updateByPrimaryKeySelective(VerificationCodeDO record);

    int updateByPrimaryKey(VerificationCodeDO record);
}
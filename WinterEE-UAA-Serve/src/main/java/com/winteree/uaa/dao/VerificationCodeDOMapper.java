package com.winteree.uaa.dao;

import com.winteree.uaa.dao.entity.VerificationCodeDO;
import com.winteree.uaa.dao.entity.VerificationCodeDOExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface VerificationCodeDOMapper {
    long countByExample(VerificationCodeDOExample example);

    int deleteByExample(VerificationCodeDOExample example);

    int deleteByPrimaryKey(String id);

    int insert(VerificationCodeDO record);

    int insertSelective(VerificationCodeDO record);

    List<VerificationCodeDO> selectByExample(VerificationCodeDOExample example);

    VerificationCodeDO selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") VerificationCodeDO record, @Param("example") VerificationCodeDOExample example);

    int updateByExample(@Param("record") VerificationCodeDO record, @Param("example") VerificationCodeDOExample example);

    int updateByPrimaryKeySelective(VerificationCodeDO record);

    int updateByPrimaryKey(VerificationCodeDO record);
}
package com.winteree.core.service;

import com.winteree.api.entity.ValidationType;
import com.winteree.api.entity.VerificationCodeDTO;

/**
 * <p>Title: VerificationCode</p>
 * <p>Description: 验证码服务</p>
 *
 * @author RenFei
 * @date : 2020-07-21 22:53
 */
public interface VerificationCodeService {
    /**
     * 保存验证码到数据库
     *
     * @param verificationCodeDTO 验证码数据传输对象
     * @return int
     */
    int saveVerificationCode(VerificationCodeDTO verificationCodeDTO);

    boolean verifyRate(String userName);

    /**
     * 获取验证码并标记验证码为已使用状态
     *
     * @param userName       手机号/邮箱地址
     * @param validationType 验证码类别
     * @return VerificationCodeDTO
     */
    VerificationCodeDTO getVerificationCode(String userName, ValidationType validationType);
}

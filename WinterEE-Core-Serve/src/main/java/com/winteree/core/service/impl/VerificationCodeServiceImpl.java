package com.winteree.core.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.winteree.api.entity.ValidationType;
import com.winteree.api.entity.VerificationCodeDTO;
import com.winteree.core.config.WintereeCoreConfig;
import com.winteree.core.dao.VerificationCodeDOMapper;
import com.winteree.core.dao.entity.VerificationCodeDO;
import com.winteree.core.dao.entity.VerificationCodeDOExample;
import com.winteree.core.service.BaseService;
import com.winteree.core.service.VerificationCodeService;
import net.renfei.sdk.utils.Builder;
import net.renfei.sdk.utils.DateUtils;
import net.renfei.sdk.utils.ListUtils;
import net.renfei.sdk.utils.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * <p>Title: VerificationCodeServiceImpl</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-07-21 22:59
 */
@Service
public class VerificationCodeServiceImpl extends BaseService implements VerificationCodeService {
    private final VerificationCodeDOMapper verificationCodeDOMapper;

    protected VerificationCodeServiceImpl(WintereeCoreConfig wintereeCoreConfig,
                                          VerificationCodeDOMapper verificationCodeDOMapper) {
        super(wintereeCoreConfig);
        this.verificationCodeDOMapper = verificationCodeDOMapper;
    }

    /**
     * 保存验证码到数据库
     *
     * @param verificationCodeDTO 验证码数据传输对象
     * @return int
     */
    @Override
    public int saveVerificationCode(VerificationCodeDTO verificationCodeDTO) {
        verificationCodeDTO.setUuid(UUID.randomUUID().toString().toUpperCase());
        verificationCodeDTO.setUsable(true);
        verificationCodeDTO.setCreateTime(new Date());
        return verificationCodeDOMapper.insertSelective(convert(verificationCodeDTO));
    }

    @Override
    public boolean verifyRate(String userName) {
        VerificationCodeDOExample example = new VerificationCodeDOExample();
        VerificationCodeDOExample.Criteria criteria = example.createCriteria();
        criteria.andCreateTimeGreaterThanOrEqualTo(DateUtils.nextMinutes(-1));
        if (StringUtils.isChinaPhone(userName)) {
            criteria.andPhoneEqualTo(userName);
        } else if (StringUtils.isEmail(userName)) {
            criteria.andEmailEqualTo(userName);
        } else {
            // 用户名没有验证码
            return false;
        }
        Page page = PageHelper.startPage(1, 1);
        verificationCodeDOMapper.selectByExample(example);
        if (page.getTotal() > 2) {
            return false;
        }
        example = new VerificationCodeDOExample();
        VerificationCodeDOExample.Criteria criteria2 = example.createCriteria();
        criteria2.andCreateTimeGreaterThanOrEqualTo(DateUtils.nextHours(-1));
        if (StringUtils.isChinaPhone(userName)) {
            criteria2.andPhoneEqualTo(userName);
        } else if (StringUtils.isEmail(userName)) {
            criteria2.andEmailEqualTo(userName);
        }
        page = PageHelper.startPage(1, 1);
        verificationCodeDOMapper.selectByExample(example);
        if (page.getTotal() > wintereeCoreConfig.getAliyunSms().getSendRateHour()) {
            return false;
        }
        example = new VerificationCodeDOExample();
        VerificationCodeDOExample.Criteria criteria3 = example.createCriteria();
        criteria3.andCreateTimeGreaterThanOrEqualTo(DateUtils.nextDay(-1));
        if (StringUtils.isChinaPhone(userName)) {
            criteria3.andPhoneEqualTo(userName);
        } else if (StringUtils.isEmail(userName)) {
            criteria3.andEmailEqualTo(userName);
        }
        page = PageHelper.startPage(1, 1);
        verificationCodeDOMapper.selectByExample(example);
        if (page.getTotal() > wintereeCoreConfig.getAliyunSms().getSendRateDay()) {
            return false;
        }
        return true;
    }

    /**
     * 获取验证码并标记验证码为已使用状态
     *
     * @param userName       手机号/邮箱地址
     * @param validationType 验证码类别
     * @return VerificationCodeDTO
     */
    @Override
    public VerificationCodeDTO getVerificationCode(String userName, ValidationType validationType) {
        VerificationCodeDOExample example = new VerificationCodeDOExample();
        VerificationCodeDOExample.Criteria criteria = example.createCriteria();
        criteria.andDeadDateGreaterThan(new Date())
                .andUsableEqualTo(true)
                .andValidationTypeEqualTo(validationType.value());
        if (StringUtils.isChinaPhone(userName)) {
            criteria.andPhoneEqualTo(userName);
        } else if (StringUtils.isEmail(userName)) {
            criteria.andEmailEqualTo(userName);
        } else {
            // 用户名没有验证码
            return null;
        }
        VerificationCodeDO verificationCodeDO = ListUtils.getOne(verificationCodeDOMapper.selectByExample(example));
        if (verificationCodeDO == null) {
            return null;
        }
        // 更新验证码有效状态，验证码只能使用一次
        updateVerificationCodeUsable(verificationCodeDO);
        return convert(verificationCodeDO);
    }

    /**
     * 更新验证码有效状态，验证码只能使用一次
     *
     * @param verificationCodeDO
     */
    @Async
    public void updateVerificationCodeUsable(VerificationCodeDO verificationCodeDO) {
        VerificationCodeDOExample example = new VerificationCodeDOExample();
        example.createCriteria().andUuidEqualTo(verificationCodeDO.getUuid());
        verificationCodeDO.setUsable(false);
        verificationCodeDO.setUpdateTime(new Date());
        verificationCodeDOMapper.updateByExampleSelective(verificationCodeDO, example);
    }

    private VerificationCodeDTO convert(VerificationCodeDO verificationCodeDO) {
        return Builder.of(VerificationCodeDTO::new)
                .with(VerificationCodeDTO::setAccountUuid, verificationCodeDO.getAccountUuid())
                .with(VerificationCodeDTO::setContentText, verificationCodeDO.getContentText())
                .with(VerificationCodeDTO::setCreateTime, verificationCodeDO.getCreateTime())
                .with(VerificationCodeDTO::setDeadDate, verificationCodeDO.getDeadDate())
                .with(VerificationCodeDTO::setEmail, verificationCodeDO.getEmail())
                .with(VerificationCodeDTO::setPhone, verificationCodeDO.getPhone())
                .with(VerificationCodeDTO::setSended, verificationCodeDO.getSended())
                .with(VerificationCodeDTO::setUpdateTime, verificationCodeDO.getUpdateTime())
                .with(VerificationCodeDTO::setUsable, verificationCodeDO.getUsable())
                .with(VerificationCodeDTO::setUuid, verificationCodeDO.getUuid())
                .with(VerificationCodeDTO::setValidationType, ValidationType.valueOf(verificationCodeDO.getValidationType()))
                .with(VerificationCodeDTO::setVerificationCode, verificationCodeDO.getVerificationCode())
                .build();
    }

    private VerificationCodeDO convert(VerificationCodeDTO verificationCodeDTO) {
        return Builder.of(VerificationCodeDO::new)
                .with(VerificationCodeDO::setAccountUuid, verificationCodeDTO.getAccountUuid())
                .with(VerificationCodeDO::setContentText, verificationCodeDTO.getContentText())
                .with(VerificationCodeDO::setCreateTime, verificationCodeDTO.getCreateTime())
                .with(VerificationCodeDO::setDeadDate, verificationCodeDTO.getDeadDate())
                .with(VerificationCodeDO::setEmail, verificationCodeDTO.getEmail())
                .with(VerificationCodeDO::setPhone, verificationCodeDTO.getPhone())
                .with(VerificationCodeDO::setSended, verificationCodeDTO.getSended())
                .with(VerificationCodeDO::setUpdateTime, verificationCodeDTO.getUpdateTime())
                .with(VerificationCodeDO::setUsable, verificationCodeDTO.getUsable())
                .with(VerificationCodeDO::setUuid, verificationCodeDTO.getUuid())
                .with(VerificationCodeDO::setValidationType, verificationCodeDTO.getValidationType().value())
                .with(VerificationCodeDO::setVerificationCode, verificationCodeDTO.getVerificationCode())
                .build();
    }
}

package com.winteree.uaa.service;

import com.winteree.uaa.dao.AccountDOMapper;
import com.winteree.uaa.dao.entity.AccountDO;
import com.winteree.uaa.dao.entity.AccountDOExample;
import net.renfei.sdk.utils.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 账号服务
 *
 * @author RenFei
 */
@Service
public class AccountService {
    @Autowired
    private AccountDOMapper accountDOMapper;

    /**
     * 根据用户名获取账号对象
     *
     * @param username 用户名
     * @return AccountDTO
     */
    AccountDO findAccountByUsername(String username) {
        AccountDOExample accountDOExample = new AccountDOExample();
        accountDOExample.createCriteria()
                .andUserNameEqualTo(username);
        return ListUtils.getOne(accountDOMapper.selectByExample(accountDOExample));
    }

    /**
     * 根据邮箱地址获取账号对象
     *
     * @param email 电子邮件
     * @return AccountDTO
     */
    AccountDO findAccountByEmail(String email) {
        AccountDOExample accountDOExample = new AccountDOExample();
        accountDOExample.createCriteria()
                .andEmailEqualTo(email);
        return ListUtils.getOne(accountDOMapper.selectByExample(accountDOExample));
    }

    /**
     * 根据手机号获取账号对象
     *
     * @param phone 手机号
     * @return AccountDTO
     */
    AccountDO findAccountByPhoneNumber(String phone) {
        AccountDOExample accountDOExample = new AccountDOExample();
        accountDOExample.createCriteria()
                .andPhoneEqualTo(phone);
        return ListUtils.getOne(accountDOMapper.selectByExample(accountDOExample));
    }

    int updateByPrimaryKeySelective(AccountDO accountDO) {
        return accountDOMapper.updateByPrimaryKeySelective(accountDO);
    }
}

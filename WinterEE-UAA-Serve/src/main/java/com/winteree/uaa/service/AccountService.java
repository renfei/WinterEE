package com.winteree.uaa.service;

import com.winteree.uaa.dao.entity.AccountDO;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * <p>Title: AccountService</p>
 * <p>Description: 账户服务</p>
 *
 * @author RenFei
 * @date : 2020-04-18 20:59
 */
public interface AccountService {
    /**
     * 根据用户名获取账号对象
     *
     * @param username 用户名
     * @return AccountDTO
     */
    AccountDO findAccountByUsername(String username);

    /**
     * 根据邮箱地址获取账号对象
     *
     * @param email 电子邮件
     * @return AccountDTO
     */
    AccountDO findAccountByEmail(String email);

    /**
     * 根据手机号获取账号对象
     *
     * @param phone 手机号
     * @return AccountDTO
     */
    AccountDO findAccountByPhoneNumber(String phone);

    int updateByPrimaryKeySelective(AccountDO accountDO);

    /**
     * 获取用户的权限列表
     *
     * @param accountDO 账户实体
     * @return 权限列表
     */
    List<GrantedAuthority> getGrantedAuthority(AccountDO accountDO);
}

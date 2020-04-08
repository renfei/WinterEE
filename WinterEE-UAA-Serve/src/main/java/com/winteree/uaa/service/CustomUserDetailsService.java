package com.winteree.uaa.service;

import com.winteree.uaa.dao.entity.AccountDO;
import net.renfei.sdk.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author RenFei
 */
@Service
public class CustomUserDetailsService {
    @Autowired
    private AccountService accountService;
    @Autowired
    private I18nService i18nService;

    /**
     * 使用用户名和密码登陆
     *
     * @param userName 用户名
     * @param password 密码
     * @param language 语言
     * @return Spring Security 的对象
     */
    public User loadUserByUsernameAndPassword(String userName, String password, String language) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            throw new InvalidGrantException(i18nService.getMessage(language, "uaa.invalidusernameorpassword", "无效的用户名或密码"));
        }
        // 判断成功后返回用户细节
        AccountDO accountDO = getAccountByName(userName);
        if (accountDO == null) {
            throw new UsernameNotFoundException(i18nService.getMessage(language, "uaa.invalidusernameorpassword", "无效的用户名或密码"));
        }
        //检查账户的状态
        checkAccountState(accountDO, language);
        if (!PasswordUtils.verifyPassword(password, accountDO.getPasswd())) {
            //TODO 错误计数
            throw new UsernameNotFoundException(i18nService.getMessage(language, "uaa.invalidusernameorpassword", "无效的用户名或密码"));
        }
        //TODO TOTP两步验证
        return getUser(accountDO);
    }

    /**
     * 使用验证码登陆
     *
     * @param userName 用户名（手机号）
     * @param code     验证码
     * @param language 语言
     * @return Spring Security 的对象
     */
    public User loadUserByVerificationCode(String userName, String code, String language) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(code)) {
            throw new InvalidGrantException(i18nService.getMessage(language, "uaa.invalidphonenumberorverificationcode", "无效的手机号或验证码"));
        }
        // 判断成功后返回用户细节
        AccountDO accountDO = getAccountByName(userName);
        if (accountDO == null) {
            throw new UsernameNotFoundException(i18nService.getMessage(language, "uaa.invalidphonenumberorverificationcode", "无效的手机号或验证码"));
        }
        //检查账户的状态
        checkAccountState(accountDO, language);
        //TODO 验证验证码
        return getUser(accountDO);
    }

    /**
     * 检查账户的状态
     *
     * @param accountDO
     * @param language
     */
    private void checkAccountState(AccountDO accountDO, String language) {
        if (accountDO.getLockTime() != null &&
                new Date().before(accountDO.getLockTime())) {
            throw new LockedException(i18nService.getMessage(language, "uaa.accountlocked", "账户被锁定，请稍后再试"));
        }
        if (accountDO.getUserStatus() <= 0) {
            throw new DisabledException(i18nService.getMessage(language, "uaa.Accountnotactivatedordisabled", "账户未激活或被禁用"));
        }
    }

    /**
     * 获取 UserDetails
     *
     * @param accountDO 来自数据库的账户对象
     * @return Spring Security 的对象
     */
    private User getUser(AccountDO accountDO) {
        //TODO 用户角色
        return new User(accountDO.getUuid(), accountDO.getPasswd(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin,user,root"));
    }

    private AccountDO getAccountByName(String name) {
        AccountDO accountDO = null;
        if (net.renfei.sdk.utils.StringUtils.isEmail(name)) {
            accountDO = accountService.findAccountByEmail(name);
        } else if (net.renfei.sdk.utils.StringUtils.isEmail(name)) {
            accountDO = accountService.findAccountByPhoneNumber(name);
        } else {
            accountDO = accountService.findAccountByUsername(name);
        }
        return accountDO;
    }
}

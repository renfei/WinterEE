package com.winteree.uaa.service;

import com.winteree.api.account.entity.AccountDTO;
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
    private AccountDataService accountDataService;

    public User loadUserByUsernameAndPassword(String userName, String password) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            throw new InvalidGrantException("无效的用户名或密码");
        }
        // 判断成功后返回用户细节
        AccountDTO accountDTO = getAccountByName(userName);
        if (accountDTO == null) {
            throw new UsernameNotFoundException("无效的用户名或密码");
        }
        checkAccountState(accountDTO);
        if (!PasswordUtils.verifyPassword(password, accountDTO.getPasswd())) {
            //TODO 错误计数
            throw new UsernameNotFoundException("无效的用户名或密码");
        }
        return getUser(accountDTO);
    }

    public User loadUserByVerificationCode(String userName, String code) {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(code)) {
            throw new InvalidGrantException("无效的手机号或验证码");
        }
        // 判断成功后返回用户细节
        AccountDTO accountDTO = getAccountByName(userName);
        if (accountDTO == null) {
            throw new UsernameNotFoundException("无效的用户名或密码");
        }
        checkAccountState(accountDTO);
        //TODO 验证验证码
        return getUser(accountDTO);
    }

    private void checkAccountState(AccountDTO accountDTO) {
        if (accountDTO.getLockTime() != null &&
                new Date().before(accountDTO.getLockTime())) {
            throw new LockedException("账户被锁定，请稍后再试");
        }
        if (accountDTO.getUserStatus() <= 0) {
            throw new DisabledException("账户未激活或被禁用");
        }
    }

    private User getUser(AccountDTO accountDTO) {
        return new User(accountDTO.getUuid(), accountDTO.getPasswd(), AuthorityUtils.commaSeparatedStringToAuthorityList("admin,user,root"));
    }

    private AccountDTO getAccountByName(String name) {
        AccountDTO accountDTO = null;
        if (net.renfei.sdk.utils.StringUtils.isEmail(name)) {
            accountDTO = accountDataService.getAccountIdByEmail(name);
        } else if (net.renfei.sdk.utils.StringUtils.isEmail(name)) {
            accountDTO = accountDataService.getAccountIdByPhone(name);
        } else {
            accountDTO = accountDataService.getAccountIdByUserName(name);
        }
        return accountDTO;
    }
}

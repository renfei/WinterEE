package com.winteree.core.service;

import com.winteree.api.entity.AccountDTO;

/**
 * <p>Title: AccountService</p>
 * <p>Description: 账户服务</p>
 *
 * @author RenFei
 * @date : 2020-04-17 12:53
 */
public interface AccountService {
    AccountDTO getAccountById(String uuid);
    AccountDTO getAccountIdByUserName(String username);
    AccountDTO getAccountIdByEmail(String email);
    AccountDTO getAccountIdByPhone(String phone);
    AccountDTO getAccountInfo();
}

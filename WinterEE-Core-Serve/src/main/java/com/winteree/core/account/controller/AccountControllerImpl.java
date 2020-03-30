package com.winteree.core.account.controller;

import com.winteree.api.account.entity.AccountDTO;
import com.winteree.api.account.service.AccountService;
import com.winteree.core.account.service.AccountDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author RenFei
 */
@RestController
public class AccountControllerImpl implements AccountService {
    @Autowired
    private AccountDataService accountDataService;

    @Override
    public AccountDTO findAccountByUsername(String username) {
        return accountDataService.getAccountIdByUserName(username);
    }

    @Override
    public AccountDTO findAccountByPhoneNumber(String phone) {
        return accountDataService.getAccountIdByPhone(phone);
    }
}

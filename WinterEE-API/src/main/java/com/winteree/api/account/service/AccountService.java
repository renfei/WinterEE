package com.winteree.api.account.service;

import com.winteree.api.account.entity.AccountDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface AccountService {
    @GetMapping("/account/username/{username}")
    AccountDTO findAccountByUsername(@PathVariable("username") String username);
    @GetMapping("/account/phone/{phone}")
    AccountDTO findAccountByPhoneNumber(@PathVariable("phone") String phone);
}

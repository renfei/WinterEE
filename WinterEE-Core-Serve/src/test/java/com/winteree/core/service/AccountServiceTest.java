package com.winteree.core.service;

import com.winteree.core.service.impl.AccountServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * <p>Title: AccountServiceTest</p>
 * <p>Description: 账户服务测试</p>
 *
 * @author RenFei
 * @date : 2020-04-16 13:09
 */
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DisplayName("Test AccountService")
@TestPropertySource(locations = "classpath:bootstrap.yml")
public class AccountServiceTest {
    private final AccountService accountService;

    @Autowired
    public AccountServiceTest(AccountService accountService) {
        this.accountService = accountService;
    }

    @Test
    public void getAccountByIdTest(){
        Assertions.assertNull(accountService.getAccountById(null));
        Assertions.assertNull(accountService.getAccountById(""));
        Assertions.assertNull(accountService.getAccountById("tests"));
        Assertions.assertNotNull(accountService.getAccountById("9369919A-F95E-44CF-AB0A-6BCD1D933403"));
    }

    @Test
    public void getAccountIdByUserNameTest(){
        Assertions.assertNull(accountService.getAccountIdByUserName(null));
        Assertions.assertNull(accountService.getAccountIdByUserName(""));
        Assertions.assertNull(accountService.getAccountIdByUserName("tests"));
        Assertions.assertNotNull(accountService.getAccountIdByUserName("admin"));
    }

    @Test
    public void getAccountIdByEmailTest(){
        Assertions.assertNull(accountService.getAccountIdByEmail(null));
        Assertions.assertNull(accountService.getAccountIdByEmail(""));
        Assertions.assertNull(accountService.getAccountIdByEmail("tests"));
        Assertions.assertNotNull(accountService.getAccountIdByEmail("i@renfei.net"));
        Assertions.assertNotNull(accountService.getAccountIdByEmail("I@RENFEI.NET"));
    }

    @Test
    public void getAccountIdByPhoneTest(){
        Assertions.assertNull(accountService.getAccountIdByPhone(null));
        Assertions.assertNull(accountService.getAccountIdByPhone(""));
        Assertions.assertNull(accountService.getAccountIdByPhone("tests"));
        Assertions.assertNotNull(accountService.getAccountIdByPhone("13001000000"));
    }
}

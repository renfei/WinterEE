package com.winteree.uaa.controller;

import com.winteree.api.entity.AccountSignUpDTO;
import com.winteree.uaa.service.AccountService;
import net.renfei.sdk.entity.APIResult;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * <p>Title: OauthController</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-07-21 21:41
 */
@Controller
public class OauthController {
    private final AccountService accountService;

    public OauthController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 账户注册
     *
     * @param accountSignUpDTO 账户注册数据传输对象
     * @return
     */
    @PostMapping("/oauth/sign_up")
    APIResult signUp(@RequestBody AccountSignUpDTO accountSignUpDTO) {
        return accountService.signUp(accountSignUpDTO);
    }
}

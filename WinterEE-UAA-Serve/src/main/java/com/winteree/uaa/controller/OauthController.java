package com.winteree.uaa.controller;

import com.winteree.api.entity.AccountSignUpDTO;
import com.winteree.api.exception.FailureException;
import com.winteree.uaa.service.AccountService;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import org.springframework.web.bind.annotation.*;

/**
 * <p>Title: OauthController</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-07-21 21:41
 */
@RestController
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
    public APIResult signUp(@RequestBody AccountSignUpDTO accountSignUpDTO) {
        return accountService.signUp(accountSignUpDTO);
    }

    @GetMapping("/oauth/verificationCode")
    public APIResult sendVerificationCode(@RequestParam("phoneOrEmail") String phoneOrEmail,
                                          @RequestParam("tenantUuid") String tenantUuid) {
        try {
            accountService.sendVerificationCode(phoneOrEmail, tenantUuid);
        } catch (FailureException failureException) {
            return APIResult.builder()
                    .code(StateCode.Failure)
                    .message(failureException.getMessage())
                    .build();
        }
        return APIResult.success();
    }
}

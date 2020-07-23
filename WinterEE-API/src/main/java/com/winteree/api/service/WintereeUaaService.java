package com.winteree.api.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.winteree.api.entity.AccountSignUpDTO;
import net.renfei.sdk.comm.StateCode;
import net.renfei.sdk.entity.APIResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>Title: WintereeUaaService</p>
 * <p>Description: UAA服务</p>
 *
 * @author RenFei
 * @date : 2020-07-13 16:35
 */
public interface WintereeUaaService {
    /**
     * 默认降级方法，实现类中可以重写降级方法
     *
     * @return
     */
    default APIResult defaultFallbackMethod() {
        System.err.println("\nUAA（用户帐户和身份验）服务不可达。\nUAA (User Account and Authentication) Services Not Available.\n");
        return APIResult.builder()
                .code(StateCode.Unavailable)
                .message("\nUAA（用户帐户和身份验）服务不可达。\nUAA (User Account and Authentication) Services Not Available.\n")
                .build();
    }

    /**
     * 登陆端点获取token令牌
     *
     * @param name          用户名/邮箱/手机号
     * @param password      密码/验证码
     * @param keyid         秘钥UUID
     * @param grant_type    登陆类型：验证码（verification_code）密码（auto_password）
     * @param client_id     客户端ID
     * @param client_secret 客户端秘钥
     * @param language      语言
     * @return
     */
    @GetMapping("/oauth/token")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult signIn(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("keyid") String keyid,
            @RequestParam("grant_type") String grant_type,
            @RequestParam("client_id") String client_id,
            @RequestParam("client_secret") String client_secret,
            @RequestParam("language") String language
    );

    /**
     * 账户注册
     *
     * @param accountSignUpDTO 账户注册数据传输对象
     * @return
     */
    @PostMapping("/oauth/sign_up")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    APIResult signUp(@RequestBody AccountSignUpDTO accountSignUpDTO);

    /**
     * Token检查端点
     *
     * @param token
     * @return
     */
    @GetMapping("/oauth/check_token")
    @HystrixCommand(fallbackMethod = "defaultFallbackMethod")
    String checkToken(@RequestParam("token") String token);
}

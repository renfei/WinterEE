package com.winteree.api.service;

import net.renfei.sdk.entity.APIResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>Title: WintereeUaaService</p>
 * <p>Description: UAA服务</p>
 *
 * @author RenFei
 * @date : 2020-07-13 16:35
 */
public interface WintereeUaaService {
    @GetMapping("/oauth/token")
    APIResult signIn(
            @RequestParam("name") String name,
            @RequestParam("password") String password,
            @RequestParam("keyid") String keyid,
            @RequestParam("grant_type") String grant_type,
            @RequestParam("client_id") String client_id,
            @RequestParam("client_secret") String client_secret,
            @RequestParam("language") String language
    );

    @GetMapping("/oauth/check_token")
    String checkToken(@RequestParam("token") String token);
}

package com.winteree.uaa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * <p>Title: OAuth2Controller</p>
 * <p>Description: </p>
 *
 * @author RenFei
 * @date : 2020-04-14 21:57
 */
@RestController
@RequestMapping("/oauth2")
public class OAuth2Controller {
    @GetMapping("userinfo")
    public Principal getUser(Principal principal) {
        return principal;
    }
}

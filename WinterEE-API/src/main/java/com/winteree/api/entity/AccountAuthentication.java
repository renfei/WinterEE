package com.winteree.api.entity;

import lombok.Data;

/**
 * @author RenFei
 */
@Data
public class AccountAuthentication {
    private String id;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private String status;
}

package com.winteree.api.entity;

import lombok.Data;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>Title: EmailConfiguration</p>
 * <p>Description: Email配置</p>
 *
 * @author RenFei
 * @date : 2020-04-17 13:31
 */
@Data
public class EmailConfiguration {
    private static final String DEFAULT_PROTOCOL = "smtp";
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private String host;
    private Integer port;
    private String username;
    private String password;
    private String protocol = DEFAULT_PROTOCOL;
    private Charset defaultEncoding = DEFAULT_CHARSET;
    private Map<String, String> properties = new HashMap<>();
}
